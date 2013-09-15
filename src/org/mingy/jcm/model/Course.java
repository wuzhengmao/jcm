package org.mingy.jcm.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mingy.jcm.model.orm.CourseEntity;

public class Course extends PropertyChangeSupportBean implements
		ICachable<Course>, INamedObject {

	private Long id;

	@NotBlank(message = "{course.code.NotNull}")
	@Pattern(regexp = "^[A-Za-z0-9_]$", message = "{course.code.WrongFormat}")
	@Length(max = 20, message = "{course.code.MaxLength}")
	private String code;

	@NotBlank(message = "{course.name.NotNull}")
	@Length(max = 50, message = "{course.name.MaxLength}")
	private String name;

	private Teacher teacher;

	@Length(max = 50, message = "{classroom.MaxLength}")
	private String classroom;

	private int type = CourseEntity.TYPE_LONG_PERIOD;

	@Length(max = 200, message = "{schedule.MaxLength}")
	private String schedule;

	private Date startDate;

	private Date endDate;

	@Min(value = 1, message = "{times.LargerThanZero}")
	private Integer times;

	@Min(value = 0, message = "{price.NotLessThanZero}")
	private Double price;

	@Min(value = 0, message = "{materialFee.NotLessThanZero}")
	private Double materialFee;

	private List<Student> students;

	@Override
	public void copyTo(Course bean) {
		bean.setId(id);
		bean.setCode(code);
		bean.setName(name);
		bean.setTeacher(teacher);
		bean.setClassroom(classroom);
		bean.setType(type);
		bean.setSchedule(schedule);
		bean.setStartDate(startDate);
		bean.setEndDate(endDate);
		bean.setTimes(times);
		bean.setPrice(price);
		bean.setMaterialFee(materialFee);
		bean.setStudents(new ArrayList<Student>(students));
	}

	@Override
	public void addNameChangeListener(PropertyChangeListener listener) {
		addPropertyChangeListener("name", listener);
	}

	@Override
	public void removeNameChangeListener(PropertyChangeListener listener) {
		removePropertyChangeListener("name", listener);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMaterialFee() {
		return materialFee;
	}

	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
}
