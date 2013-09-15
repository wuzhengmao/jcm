package org.mingy.jcm.ui;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.mingy.jcm.facade.ITeacherFacade;
import org.mingy.jcm.model.Teacher;
import org.mingy.jcm.ui.model.Resource;
import org.mingy.jcm.ui.model.Resources;
import org.mingy.jcm.ui.util.UIUtils;
import org.mingy.kernel.context.GlobalBeanContext;

public class TeacherEditor extends AbstractFormEditor<Teacher> {

	public static final String ID = "org.mingy.jcm.ui.TeacherEditor"; //$NON-NLS-1$
	private Text txtName;
	private ComboViewer cvSex;
	private Text txtContacts;
	private Text txtSpecialty;

	public TeacherEditor() {
	}

	@Override
	protected Teacher init() {
		Resource resource = (Resource) getEditorInput().getAdapter(
				Resource.class);
		Teacher teacher = (Teacher) resource.getValue();
		setPartName(resource != null ? "修改教师 - " + teacher.getName() : "新增教师");
		Teacher bean = new Teacher();
		if (teacher != null) {
			teacher.copyTo(bean);
		}
		return bean;
	}

	@Override
	protected void createControls(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		container.setLayout(layout);

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
		cvSex.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		UIUtils.initCombo(cvSex, "sex", Integer.class);
		cvSex.addSelectionChangedListener(defaultModifyListener);

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
	}

	@Override
	protected void initDataBindings(Teacher bean) {
		bindText(txtName, bean, "name");
		bindSelection(cvSex, bean, "sex");
		bindText(txtContacts, bean, "contacts");
		bindText(txtSpecialty, bean, "specialty");
	}

	@Override
	protected void fillForm(Teacher bean) {
		// do nothing
	}

	@Override
	protected void fillData(Teacher bean) {
		// do nothing
	}

	@Override
	protected void save(Teacher bean) {
		GlobalBeanContext.getInstance().getBean(ITeacherFacade.class)
				.saveTeacher(bean);
	}

	@Override
	protected void postSave(Teacher bean) {
		Resource resource = (Resource) getEditorInput().getAdapter(
				Resource.class);
		Resources.updateResource(resource, bean);
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}
}
