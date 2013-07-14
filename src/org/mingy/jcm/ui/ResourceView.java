package org.mingy.jcm.ui;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.mingy.jcm.Activator;
import org.mingy.jcm.model.orm.Course;
import org.mingy.jcm.model.orm.Student;
import org.mingy.jcm.model.orm.Teacher;
import org.mingy.jcm.ui.model.Resource;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Langs;

public class ResourceView extends ViewPart {

	public static final String ID = "org.mingy.jcm.ui.ResourceView"; //$NON-NLS-1$

	private static final Log logger = LogFactory.getLog(ResourceView.class);

	private TreeNode resources;
	private TreeViewer treeViewer;
	private Action collapseAction;
	private Action addAction;
	private IEntityDaoFacade entityDao = GlobalBeanContext.getInstance()
			.getBean(IEntityDaoFacade.class);

	public ResourceView() {
		resources = new TreeNode(new Resource(Resource.TYPE_ROOT));
		TreeNode teachers = new TreeNode(new Resource(Resource.TYPE_TEACHERS));
		teachers.setParent(resources);
		teachers.setChildren(loadTeachers(teachers));
		TreeNode courses = new TreeNode(new Resource(Resource.TYPE_COURSES));
		courses.setParent(resources);
		TreeNode students = new TreeNode(new Resource(Resource.TYPE_STUDENTS));
		students.setParent(resources);
		resources.setChildren(new TreeNode[] { teachers, courses, students });
	}

	private TreeNode[] loadTeachers(TreeNode parent) {
		List<Teacher> teachers = entityDao.loadAll(Teacher.class);
		TreeNode[] nodes = new TreeNode[teachers.size()];
		for (int i = 0; i < nodes.length; i++) {
			TreeNode node = new TreeNode(teachers.get(i));
			node.setParent(parent);
			nodes[i] = node;
		}
		return nodes;
	}

	public static class ResourceTreeLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			Object o = ((TreeNode) element).getValue();
			if (o instanceof Resource) {
				return Activator.getImageDescriptor("/icons/folder.gif")
						.createImage();
			}
			return Activator.getImageDescriptor("/icons/file.gif")
					.createImage();
		}

		@Override
		public String getText(Object element) {
			Object o = ((TreeNode) element).getValue();
			if (o instanceof Resource) {
				return Langs
						.getLabel("resource.type", ((Resource) o).getType());
			} else if (o instanceof Teacher) {
				return ((Teacher) o).getName();
			} else if (o instanceof Course) {
				return ((Course) o).getName();
			} else if (o instanceof Student) {
				return ((Student) o).getName();
			} else {
				return super.getText(element);
			}
		}
	}

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
		treeViewer.setContentProvider(new TreeNodeContentProvider());
		treeViewer.setLabelProvider(new ResourceTreeLabelProvider());
		treeViewer.setInput(new TreeNode[] { resources });
		treeViewer.expandToLevel(2);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeNode node = getSelectedItem();
				if (node != null) {
					addAction.setEnabled(node.getParent() == resources);
				} else {
					addAction.setEnabled(false);
				}
			}
		});

		Tree tree = treeViewer.getTree();

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	private TreeNode getSelectedItem() {
		ISelection selection = treeViewer.getSelection();
		if (selection.isEmpty()) {
			return null;
		} else if (selection instanceof IStructuredSelection) {
			return (TreeNode) ((IStructuredSelection) selection)
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
		collapseAction = new Action() {
			@Override
			public void run() {
				treeViewer.collapseAll();
				treeViewer.expandToLevel(2);
			}
		};
		collapseAction.setImageDescriptor(Activator
				.getImageDescriptor("/icons/collapse_all.gif"));
		collapseAction.setToolTipText("收拢");
		addAction = new Action() {
			@Override
			public void run() {
				try {
					Resource resource = (Resource) getSelectedItem().getValue();
					switch (resource.getType()) {
					case Resource.TYPE_TEACHERS:
						getSite()
								.getWorkbenchWindow()
								.getActivePage()
								.openEditor(
										new TeacherEditorInput(new Teacher()),
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
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(collapseAction);
		toolbarManager.add(new Separator());
		toolbarManager.add(addAction);
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
}
