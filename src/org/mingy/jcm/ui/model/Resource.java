package org.mingy.jcm.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.mingy.jcm.model.orm.Course;
import org.mingy.jcm.model.orm.Student;
import org.mingy.jcm.model.orm.Teacher;
import org.mingy.kernel.util.Langs;

public class Resource extends PropertyChangeSupportBean {

	public static final int TYPE_ROOT = 0;
	public static final int TYPE_TEACHERS = 1;
	public static final int TYPE_COURSES = 2;
	public static final int TYPE_STUDENTS = 3;
	public static final int TYPE_LEAF = 99;
	public static final int TYPE_NONE = -1;

	private int type;
	private Resource parent;
	private List<Resource> children = new ArrayList<Resource>();

	private Object value;

	public Resource(int type) {
		this.type = type;
	}

	public Resource(Object value) {
		this.type = TYPE_LEAF;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		String oldLabel = getLabel();
		this.value = value;
		firePropertyChange("label", oldLabel, getLabel());
	}

	public String getLabel() {
		if (type != TYPE_LEAF) {
			return Langs.getLabel("resource.type", type);
		} else if (value instanceof Teacher) {
			return ((Teacher) value).getName();
		} else if (value instanceof Course) {
			return ((Course) value).getName();
		} else if (value instanceof Student) {
			return ((Student) value).getName();
		} else {
			return value != null ? value.toString() : null;
		}
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		firePropertyChange("parent", this.parent, this.parent = parent);
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		firePropertyChange("children", this.children, this.children = children);
	}
}
