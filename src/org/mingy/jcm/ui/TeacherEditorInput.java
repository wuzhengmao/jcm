package org.mingy.jcm.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jcm.model.orm.Teacher;

public class TeacherEditorInput implements IEditorInput {

	private Teacher teacher;

	public TeacherEditorInput(Teacher teacher) {
		this.teacher = teacher;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return adapter.isAssignableFrom(Teacher.class) ? teacher : null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return teacher.getId() != null ? "修改教师" : "新增教师";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}
}
