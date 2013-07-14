package org.mingy.jcm.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.mingy.jcm.Activator;
import org.mingy.jcm.model.orm.Teacher;
import org.mingy.jcm.ui.model.Resource;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Langs;

public class ResourceView extends ViewPart {

	public static final String ID = "org.mingy.jcm.ui.ResourceView"; //$NON-NLS-1$

	private static final Log logger = LogFactory.getLog(ResourceView.class);

	private TreeViewer treeViewer;
	private Action refreshAction;
	private Action addAction;
	private Action editAction;
	private Action deleteAction;
	private IEntityDaoFacade entityDao = GlobalBeanContext.getInstance()
			.getBean(IEntityDaoFacade.class);

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
				BeansObservables.listFactory("children", Resource.class), null);
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
				if (node.getType() != Resource.TYPE_LEAF) {
					return folderImage;
				}
				return fileImage;
			}

			@Override
			public void dispose() {
				folderImage.dispose();
				fileImage.dispose();
				super.dispose();
			}
		};
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setAutoExpandLevel(2);
		treeViewer.setInput(loadAll());
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Resource node = getSelectedItem();
				if (node != null) {
					addAction.setEnabled(node.getType() != Resource.TYPE_ROOT
							&& node.getType() != Resource.TYPE_LEAF);
					editAction.setEnabled(node.getType() == Resource.TYPE_LEAF);
					deleteAction.setEnabled(node.getType() == Resource.TYPE_LEAF);
				} else {
					addAction.setEnabled(false);
					editAction.setEnabled(false);
					deleteAction.setEnabled(false);
				}
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Resource node = getSelectedItem();
				if (node != null) {
					if (node.getType() != Resource.TYPE_LEAF) {
						treeViewer.setExpandedState(node,
								!treeViewer.getExpandedState(node));
					} else {
						editAction.run();
					}
				}
			}
		});

		Tree tree = treeViewer.getTree();

		createActions();
		initializeToolBar();
		initializeMenu();
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
				treeViewer.setInput(loadAll());
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
					case Resource.TYPE_TEACHERS:
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
					case Resource.TYPE_TEACHERS:
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
				String title, message;
				switch (resource.getParent().getType()) {
				case Resource.TYPE_TEACHERS:
					Teacher teacher = (Teacher) resource.getValue();
					title = Langs.getText("confirm.delete_teacher.title");
					message = Langs.getText("confirm.delete_teacher.title",
							teacher.getName());
					break;
				default:
					return;
				}
				if (MessageDialog.openConfirm(getSite().getShell(), title,
						message)) {
					entityDao.delete((IEntity) resource.getValue());
					List<Resource> list = new ArrayList<Resource>(resource
							.getParent().getChildren());
					list.remove(resource);
					resource.getParent().setChildren(list);
					IEditorPart editor = getSite().getWorkbenchWindow()
							.getActivePage()
							.findEditor(new ResourceEditorInput(resource));
					if (editor != null) {
						getSite().getWorkbenchWindow().getActivePage()
								.closeEditor(editor, false);
					}
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

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private Resource loadAll() {
		Resource rootNode = new Resource(Resource.TYPE_ROOT);
		Resource teachersNode = new Resource(Resource.TYPE_TEACHERS);
		teachersNode.setParent(rootNode);
		rootNode.getChildren().add(teachersNode);
		Resource coursesNode = new Resource(Resource.TYPE_COURSES);
		coursesNode.setParent(rootNode);
		rootNode.getChildren().add(coursesNode);
		Resource studentsNode = new Resource(Resource.TYPE_STUDENTS);
		studentsNode.setParent(rootNode);
		rootNode.getChildren().add(studentsNode);
		Resource data = new Resource(Resource.TYPE_NONE);
		data.getChildren().add(rootNode);
		loadTeachers(teachersNode);
		return data;
	}

	private void loadTeachers(Resource parent) {
		for (Teacher teacher : entityDao.loadAll(Teacher.class)) {
			Resource item = new Resource(teacher);
			item.setParent(parent);
			parent.getChildren().add(item);
		}
	}
}
