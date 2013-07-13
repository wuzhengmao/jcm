package org.mingy.jcm.model.orm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_SCHEDULE")
public class Schedule implements IEntity {

	private static final long serialVersionUID = 6481789318794060656L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "COURSE_ID", nullable = false)
	private Course course;

	@Column(name = "START_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "END_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "CLASSROOM", nullable = false, length = 100)
	private String classroom;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TEACHER_ID", nullable = false)
	private Teacher teacher;

	@Column(name = "ACTUAL_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actualStartTime;

	@Column(name = "ACTUAL_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actualEndTime;

	@Column(name = "ACTUAL_CLASSROOM", length = 100)
	private String actualClassroom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTUAL_TEACHER_ID")
	private Teacher actualTeacher;

	@Column(name = "COMMENT", length = 200)
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Date getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Date getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public String getActualClassroom() {
		return actualClassroom;
	}

	public void setActualClassroom(String actualClassroom) {
		this.actualClassroom = actualClassroom;
	}

	public Teacher getActualTeacher() {
		return actualTeacher;
	}

	public void setActualTeacher(Teacher actualTeacher) {
		this.actualTeacher = actualTeacher;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
