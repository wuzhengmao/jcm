package org.mingy.jcm.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.mingy.jcm.model.orm.Teacher;
import org.mingy.jcm.ui.model.Resource;
import org.mingy.kernel.context.GlobalBeanContext;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.util.Langs;
import org.mingy.kernel.util.Validators;

public class TeacherEditor extends EditorPart {

	public static final String ID = "org.mingy.jcm.ui.TeacherEditor"; //$NON-NLS-1$
	private Text txtName;
	private ComboViewer cvSex;
	private Text txtContacts;
	private Text txtSpecialty;
	private Teacher teacher;
	private boolean dirty = false;
	private DefaultModifyListener defaultModifyListener = new DefaultModifyListener();
	private IEntityDaoFacade entityDao = GlobalBeanContext.getInstance()
			.getBean(IEntityDaoFacade.class);

	private class DefaultModifyListener implements ModifyListener,
			ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			setDirty(true);
		}

		@Override
		public void modifyText(ModifyEvent e) {
			setDirty(true);
		}
	}

	public TeacherEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("姓名：");

		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		txtName.addModifyListener(defaultModifyListener);

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_1.setText("性别：");

		cvSex = new ComboViewer(container, SWT.READ_ONLY);
		cvSex.setContentProvider(new ArrayContentProvider());
		final String[] sexLabels = Langs.enumValues("sex").toArray(
				new String[0]);
		cvSex.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return sexLabels[(Integer) element];
			}
		});
		cvSex.addSelectionChangedListener(defaultModifyListener);
		Integer[] sexValues = new Integer[sexLabels.length];
		for (int i = 0; i < sexValues.length; i++)
			sexValues[i] = i;
		cvSex.setInput(sexValues);
		Combo cmbSex = cvSex.getCombo();
		cmbSex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));

		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("联系方式：");

		txtContacts = new Text(container, SWT.BORDER);
		txtContacts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtContacts.addModifyListener(defaultModifyListener);

		Label label_3 = new Label(container, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_3.setText("专业方向：");

		txtSpecialty = new Text(container, SWT.BORDER);
		txtSpecialty.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtSpecialty.addModifyListener(defaultModifyListener);

		fillForm();
	}

	private void init() {
		IEditorInput input = getEditorInput();
		Teacher teacher = (Teacher) input.getAdapter(Teacher.class);
		setPartName(teacher != null ? "修改教师 - " + teacher.getName() : "新增教师");
		this.teacher = new Teacher();
		if (teacher != null) {
			try {
				BeanUtils.copyProperties(this.teacher, teacher);
			} catch (Exception e) {
				throw new RuntimeException("error on clone bean", e);
			}
		}
	}

	private void fillForm() {
		txtName.setText(teacher.getName() != null ? teacher.getName() : "");
		cvSex.setSelection(teacher.getSex() != null ? new StructuredSelection(
				teacher.getSex()) : StructuredSelection.EMPTY);
		txtContacts.setText(teacher.getContacts() != null ? teacher
				.getContacts() : "");
		txtSpecialty.setText(teacher.getSpecialty() != null ? teacher
				.getSpecialty() : "");
		setDirty(false);
	}

	private void fillData() {
		teacher.setName(txtName.getText());
		teacher.setSex(!cvSex.getSelection().isEmpty() ? (Integer) ((IStructuredSelection) cvSex
				.getSelection()).getFirstElement() : null);
		teacher.setContacts(txtContacts.getText());
		teacher.setSpecialty(txtSpecialty.getText());
	}

	private <T> boolean validate(T bean) {
		Set<ConstraintViolation<T>> violations = Validators.validate(bean);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<T> violation : violations) {
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(violation.getMessage());
			}
			MessageDialog
					.openError(getSite().getShell(), "输入错误", sb.toString());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		fillData();
		if (validate(teacher)) {
			boolean insert = teacher.getId() == null;
			entityDao.save(teacher);
			setDirty(false);
			Resource item = (Resource) getEditorInput().getAdapter(
					Resource.class);
			item.setValue(teacher);
			if (insert) {
				List<Resource> list = new ArrayList<Resource>(item.getParent()
						.getChildren());
				list.add(item);
				item.getParent().setChildren(list);
			}
			init();
		}
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		if (!(input instanceof ResourceEditorInput))
			throw new PartInitException("unsupported type: " + input.getClass());
		setInput(input);
		init();
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(EditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
