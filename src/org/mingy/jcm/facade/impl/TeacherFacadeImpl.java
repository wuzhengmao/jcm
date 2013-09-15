package org.mingy.jcm.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.mingy.jcm.facade.ITeacherFacade;
import org.mingy.jcm.model.Caches;
import org.mingy.jcm.model.Teacher;
import org.mingy.jcm.model.orm.CourseEntity;
import org.mingy.jcm.model.orm.TeacherEntity;
import org.mingy.kernel.facade.IEntityDaoFacade;

public class TeacherFacadeImpl implements ITeacherFacade {

	private IEntityDaoFacade entityDao;

	@Override
	public List<Teacher> getTeachers() {
		List<Teacher> list = new ArrayList<Teacher>();
		for (TeacherEntity entity : entityDao
				.loadAll(TeacherEntity.class, true)) {
			Teacher teacher = Caches.load(Teacher.class, entity.getId());
			if (teacher == null) {
				teacher = new Teacher();
				teacher.setId(entity.getId());
				Caches.save(Teacher.class, teacher);
			}
			teacher.setName(entity.getName());
			teacher.setSex(entity.getSex());
			teacher.setContacts(entity.getContacts());
			teacher.setSpecialty(entity.getSpecialty());
			list.add(teacher);
		}
		return list;
	}

	@Override
	public void saveTeacher(Teacher teacher) {
		TeacherEntity entity = null;
		if (teacher.getId() != null) {
			entity = entityDao.load(TeacherEntity.class, teacher.getId());
		}
		if (entity == null) {
			entity = new TeacherEntity();
		}
		entity.setId(teacher.getId());
		entity.setName(teacher.getName());
		entity.setSex(teacher.getSex());
		entity.setContacts(teacher.getContacts());
		entity.setSpecialty(teacher.getSpecialty());
		entityDao.save(entity);
		teacher.setId(entity.getId());
		Caches.save(Teacher.class, teacher);
	}

	@Override
	public void deleteTeacher(Long id) {
		if (entityDao.load(CourseEntity.class, id, "teacher.id") != null) {
			entityDao.logicDelete(TeacherEntity.class, id);
			Caches.remove(Teacher.class, id);
		} else {
			entityDao.delete(TeacherEntity.class, id);
			Caches.remove(Teacher.class, id);
		}
	}

	public void setEntityDao(IEntityDaoFacade entityDao) {
		this.entityDao = entityDao;
	}
}
