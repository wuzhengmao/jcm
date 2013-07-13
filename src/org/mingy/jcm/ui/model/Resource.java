package org.mingy.jcm.ui.model;

public class Resource {

	public static final int TYPE_ROOT = 0;
	public static final int TYPE_TEACHERS = 1;
	public static final int TYPE_COURSES = 2;
	public static final int TYPE_STUDENTS = 3;

	private int type;

	public Resource(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
