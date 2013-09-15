package org.mingy.jcm.ui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.mingy.jcm.facade.ITeacherFacade;
import org.mingy.jcm.model.Course;
import org.mingy.jcm.model.Student;
import org.mingy.jcm.model.Teacher;
import org.mingy.kernel.context.GlobalBeanContext;

public abstract class Resources {

	private static final ITeacherFacade teacherFacade = GlobalBeanContext
			.getInstance().getBean(ITeacherFacade.class);

	private static Resource root;
	private static Map<Object, Resource> resources;
	@SuppressWarnings("unchecked")
	private static List<Teacher> teacherList = new WritableList();
	@SuppressWarnings("unchecked")
	private static List<Course> courseList = new WritableList();
	@SuppressWarnings("unchecked")
	private static List<Student> studentList = new WritableList();
	private static boolean inited = false;

	public static boolean isInited() {
		return inited;
	}

	public static void loadAll() {
		root = new Resource(Resource.TYPE_ROOT);
		Resource teacherNode = new Resource(Resource.TYPE_TEACHER);
		teacherNode.setParent(root);
		root.getChildren().add(teacherNode);
		Resource courseNode = new Resource(Resource.TYPE_COURSE);
		courseNode.setParent(root);
		root.getChildren().add(courseNode);
		Resource studentNode = new Resource(Resource.TYPE_STUDENT);
		studentNode.setParent(root);
		root.getChildren().add(studentNode);
		resources = new HashMap<Object, Resource>();
		teacherList.clear();
		courseList.clear();
		teacherList.clear();
		loadTeachers(teacherNode);
		loadCourses(courseNode);
		loadStudents(studentNode);
		inited = true;
	}

	private static void loadTeachers(Resource parent) {
		for (Teacher teacher : teacherFacade.getTeachers()) {
			Resource resource = new Resource(teacher);
			resource.setParent(parent);
			parent.getChildren().add(resource);
			resources.put(teacher, resource);
			teacherList.add(teacher);
		}
	}

	private static void loadCourses(Resource parent) {
		// TODO
	}

	private static void loadStudents(Resource parent) {
		// TODO
	}

	public static void updateResource(Resource resource, Teacher teacher) {
		if (resource.getValue() == null) {
			resource.setValue(teacher);
			getResource(Resource.TYPE_TEACHER).getChildren().add(resource);
			resources.put(teacher, resource);
			teacherList.add(teacher);
		} else if (teacher != resource.getValue()) {
			teacher.copyTo((Teacher) resource.getValue());
		}
	}

	public static void updateResource(Resource resource, Course course) {
		if (resource.getValue() == null) {
			resource.setValue(course);
			getResource(Resource.TYPE_COURSE).getChildren().add(resource);
			resources.put(course, resource);
			courseList.add(course);
		} else if (course != resource.getValue()) {
			course.copyTo((Course) resource.getValue());
		}
	}

	public static void updateResource(Resource resource, Student student) {
		if (resource.getValue() == null) {
			resource.setValue(student);
			getResource(Resource.TYPE_STUDENT).getChildren().add(resource);
			resources.put(student, resource);
			studentList.add(student);
		} else if (student != resource.getValue()) {
			student.copyTo((Student) resource.getValue());
		}
	}

	public static void removeResource(Resource resource, Teacher teacher) {
		resource.getParent().getChildren().remove(resource);
		resources.remove(resource);
		teacherList.remove(teacher);
	}

	public static void removeResource(Resource resource, Course course) {
		resource.getParent().getChildren().remove(resource);
		resources.remove(resource);
		courseList.remove(course);
	}

	public static void removeResource(Resource resource, Student student) {
		resource.getParent().getChildren().remove(resource);
		resources.remove(resource);
		studentList.remove(student);
	}

	public static Resource getResource(int type) {
		if (type == Resource.TYPE_ROOT) {
			return root;
		} else {
			for (Resource resource : root.getChildren()) {
				if (resource.getType() == type) {
					return resource;
				}
			}
			return null;
		}
	}

	public static Resource getResource(Object value) {
		return resources.get(value);
	}

	public static List<Teacher> getTeacherList() {
		return teacherList;
	}

	public static List<Course> getCourseList() {
		return courseList;
	}

	public static List<Student> getStudentList() {
		return studentList;
	}
}
