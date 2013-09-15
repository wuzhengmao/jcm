package org.mingy.jcm.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.DecoratingObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.mingy.jcm.Activator;
import org.mingy.jcm.facade.ITeacherFacade;
import org.mingy.jcm.model.Teacher;
import org.mingy.jcm.ui.model.Resource;
import org.mingy.jcm.ui.model.Resources;
import org.mingy.jcm.ui.util.ActionWrapper;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.util.ApplicationException;
import org.mingy.kernel.util.Langs;

public class ResourceView extends ViewPart {

	public static final String ID = "org.mingy.jcm.ui.ResourceView"; //$NON-NLS-1$

	private static final Log logger = LogFactory.getLog(ResourceView.class);

	private TreeViewer treeViewer;
	private Action refreshAction;
	private Action addAction;
	private Action editAction;
	private Action deleteAction;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		treeViewer = new TreeViewer(container, SWT.BORDER);
		ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(
				new IObservableFactory() {
					@Override
					public IObservable createObservable(Object target) {
						return new DecoratingObservableList(
								((Resource) target).getObservableChildren(),
								false);
					}
				}, null);
		treeViewer.setContentProvider(contentProvider);
		ObservableMapLabelProvider labelProvider = new ObservableMapLabelProvider(
				BeanProperties.value("label").observeDetail(
						contentProvider.getKnownElements())) {
			private Image folderImage = Activator.getImageDescriptor(
					"/icons/folder.gif").createImage();
			private Image fileImage = Activator.getImageDescriptor(
					"/icons/file.gif").createImage();

			@Override
			public Image getImage(Object element) {
				Resource node = (Resource) element;
				if (node.getType() != Resource.TYPE_ITEM) {
					return folderImage;
				} else {
					return fileImage;
				}
			}

			@Override
			public void dispose() {
				folderImage.dispose();
				fileImage.dispose();
				super.dispose();
			}
		};
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				syncToolbarStatus();
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Resource node = getSelectedItem();
				if (node != null) {
					if (node.getType() != Resource.TYPE_ITEM) {
						treeViewer.setExpandedState(node,
								!treeViewer.getExpandedState(node));
					} else {
						editAction.run();
					}
				}
			}
		});

		createActions();
		initializeToolBar();
		initializeMenu();

		loadTree(false);
	}

	private Resource getSelectedItem() {
		ISelection selection = treeViewer.getSelection();
		if (selection.isEmpty()) {
			return null;
		} else if (selection instanceof IStructuredSelection) {
			return (Resource) ((IStructuredSelection) selection)
					.getFirstElement();
		} else {
			return null;
		}
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		refreshAction = new Action() {
			@Override
			public void run() {
				List<IEditorPart> editors = getDirtyEditors();
				if (!editors.isEmpty()) {
					if (MessageDialog.openConfirm(getSite().getShell(),
							Langs.getText("confirm.save.title"),
							Langs.getText("confirm.save.message.onRefresh"))) {
						for (IEditorPart editor : editors) {
							if (!getSite().getPage().saveEditor(editor, false)) {
								return;
							}
						}
					} else {
						return;
					}
				}
				loadTree(true);
			}
		};
		refreshAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/refresh.gif"));
		refreshAction.setToolTipText("刷新");

		addAction = new Action() {
			@Override
			public void run() {
				try {
					Resource resource = getSelectedItem();
					switch (resource.getType()) {
					case Resource.TYPE_TEACHER:
						getSite()
								.getWorkbenchWindow()
								.getActivePage()
								.openEditor(new ResourceEditorInput(resource),
										TeacherEditor.ID);
						break;
					}
				} catch (PartInitException e) {
					if (logger.isErrorEnabled()) {
						logger.error("error on open editor", e);
					}
					MessageDialog.openError(getSite().getShell(), "Error",
							"Error opening editor:" + e.getMessage());
				}
			}
		};
		addAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/add.gif"));
		addAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/add_disabled.gif"));
		addAction.setToolTipText("新增");
		addAction.setEnabled(false);

		editAction = new Action() {
			@Override
			public void run() {
				try {
					Resource resource = getSelectedItem();
					switch (resource.getParent().getType()) {
					case Resource.TYPE_TEACHER:
						getSite()
								.getWorkbenchWindow()
								.getActivePage()
								.openEditor(new ResourceEditorInput(resource),
										TeacherEditor.ID);
						break;
					}
				} catch (PartInitException e) {
					if (logger.isErrorEnabled()) {
						logger.error("error on open editor", e);
					}
					MessageDialog.openError(getSite().getShell(), "Error",
							"Error opening editor:" + e.getMessage());
				}
			}
		};
		editAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/edit.gif"));
		editAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/edit_disabled.gif"));
		editAction.setToolTipText("修改");
		editAction.setEnabled(false);

		deleteAction = new Action() {
			@Override
			public void run() {
				Resource resource = getSelectedItem();
				switch (resource.getParent().getType()) {
				case Resource.TYPE_TEACHER:
					deleteTeacher(resource);
					break;
				default:
					return;
				}
			}
		};
		deleteAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/delete.gif"));
		deleteAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("/icons/delete_disabled.gif"));
		deleteAction.setToolTipText("删除");
		deleteAction.setEnabled(false);
	}

	private boolean deleteTeacher(Resource resource) {
		Teacher teacher = (Teacher) resource.getValue();
		if (MessageDialog.openConfirm(
				getSite().getShell(),
				Langs.getText("confirm.delete.title"),
				Langs.getText("confirm.delete_teacher.message",
						teacher.getName()))) {
			try {
				GlobalBeanContext.getInstance().getBean(ITeacherFacade.class)
						.deleteTeacher(teacher.getId());
				Resources.removeResource(resource, teacher);
				return true;
			} catch (Exception e) {
				handleExceptionOnDelete(e);
			}
		}
		return false;
	}

	private void handleExceptionOnDelete(Exception e) {
		if (logger.isErrorEnabled() && !(e instanceof ApplicationException)) {
			logger.error("error on delete", e);
		}
		MessageDialog.openError(getSite().getShell(), Langs
				.getText("error.delete.title"), Langs.getText(
				"error.delete.message",
				(e instanceof ApplicationException ? "" : e.getClass()
						.getName() + ": ")
						+ e.getLocalizedMessage()));
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(refreshAction);
		toolbarManager.add(new Separator());
		toolbarManager.add(addAction);
		toolbarManager.add(editAction);
		toolbarManager.add(deleteAction);
	}

	void syncToolbarStatus() {
		Resource resource = getSelectedItem();
		if (resource != null) {
			addAction.setEnabled(resource.getType() != Resource.TYPE_ITEM);
			editAction.setEnabled(resource.getType() == Resource.TYPE_ITEM);
			deleteAction.setEnabled(resource.getType() == Resource.TYPE_ITEM);
		} else {
			addAction.setEnabled(false);
			editAction.setEnabled(false);
			deleteAction.setEnabled(false);
		}
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		final IAction addTeacherAction = new ActionWrapper(addAction, "新增教师",
				null, null);
		final IAction addCourseAction = new ActionWrapper(addAction, "新增课程",
				null, null);
		final IAction addStudentAction = new ActionWrapper(addAction, "新增学生",
				null, null);
		final IAction editTeacherAction = new ActionWrapper(editAction, "修改教师",
				null, null);
		final IAction editCourseAction = new ActionWrapper(editAction, "修改课程",
				null, null);
		final IAction editStudentAction = new ActionWrapper(editAction, "修改学生",
				null, null);
		final IAction delTeacherAction = new ActionWrapper(deleteAction,
				"删除教师", null, null);
		final IAction delCourseAction = new ActionWrapper(deleteAction, "删除课程",
				null, null);
		final IAction delStudentAction = new ActionWrapper(deleteAction,
				"删除学生", null, null);
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Resource resource = getSelectedItem();
				switch (resource.getType()) {
				case Resource.TYPE_TEACHER:
					manager.add(addTeacherAction);
					break;
				case Resource.TYPE_COURSE:
					manager.add(addCourseAction);
					break;
				case Resource.TYPE_STUDENT:
					manager.add(addStudentAction);
					break;
				case Resource.TYPE_ITEM:
					switch (resource.getParent().getType()) {
					case Resource.TYPE_TEACHER:
						manager.add(editTeacherAction);
						manager.add(delTeacherAction);
						break;
					case Resource.TYPE_COURSE:
						manager.add(editCourseAction);
						manager.add(delCourseAction);
						break;
					case Resource.TYPE_STUDENT:
						manager.add(editStudentAction);
						manager.add(delStudentAction);
						break;
					}
					break;
				}
			}
		});
		treeViewer.getTree().setMenu(
				menuManager.createContextMenu(treeViewer.getTree()));
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private List<IEditorPart> getEditors() {
		List<IEditorPart> list = new ArrayList<IEditorPart>();
		for (IEditorReference editorRef : getSite().getPage()
				.getEditorReferences()) {
			IEditorPart editor = editorRef.getEditor(true);
			if (editor instanceof AbstractFormEditor) {
				list.add(editor);
			}
		}
		return list;
	}

	private List<IEditorPart> getDirtyEditors() {
		List<IEditorPart> list = new ArrayList<IEditorPart>();
		for (IEditorPart editor : getSite().getPage().getDirtyEditors()) {
			if (editor instanceof AbstractFormEditor) {
				list.add(editor);
			}
		}
		return list;
	}

	private void loadTree(boolean forceLoad) {
		if (forceLoad || !Resources.isInited()) {
			Resources.loadAll();
		}
		treeViewer.setInput(Resources.getResource(Resource.TYPE_ROOT));
		for (IEditorPart editor : getEditors()) {
			Resource resource = (Resource) editor.getEditorInput().getAdapter(
					Resource.class);
			Resource newResource = Resources.getResource(resource.getValue());
			if (newResource == null) {
				getSite().getPage().closeEditor(editor, false);
			} else {
				((AbstractFormEditor<?>) editor).init(new ResourceEditorInput(
						newResource));
			}
		}
	}
}
