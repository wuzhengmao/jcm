package org.mingy.jcm.facade;

import java.util.List;

import org.mingy.jcm.model.Teacher;

public interface ITeacherFacade {

	List<Teacher> getTeachers();

	void saveTeacher(Teacher teacher);

	void deleteTeacher(Long id);
}
