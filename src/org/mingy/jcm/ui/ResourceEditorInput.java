package org.mingy.jcm.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.mingy.jcm.model.orm.Course;
import org.mingy.jcm.model.orm.Student;
import org.mingy.jcm.model.orm.Teacher;
import org.mingy.jcm.ui.model.Resource;

public class ResourceEditorInput implements IEditorInput {

	private Resource node;
	private Resource item;

	public ResourceEditorInput(Resource resource) {
		if (resource.getType() == Resource.TYPE_LEAF) {
			item = resource;
			node = item.getParent();
		} else {
			node = resource;
			item = new Resource(null);
			item.setParent(node);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ResourceEditorInput ? item
				.equals(((ResourceEditorInput) obj).item) : false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isAssignableFrom(Resource.class)) {
			return item;
		} else {
			return (node.getType() == Resource.TYPE_TEACHERS && adapter
					.isAssignableFrom(Teacher.class))
					|| (node.getType() == Resource.TYPE_COURSES && adapter
							.isAssignableFrom(Course.class))
					|| (node.getType() == Resource.TYPE_STUDENTS && adapter
							.isAssignableFrom(Student.class)) ? item.getValue()
					: null;
		}
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
		return "";
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
