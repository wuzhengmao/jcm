package org.mingy.jcm.model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_ATTENDANCE")
public class AttendanceEntity implements IEntity {

	private static final long serialVersionUID = -5558839261198634276L;

	public static final int STATE_OK = 0; // 出勤
	public static final int STATE_MISS = 1; // 缺勤
	public static final int STATE_AFL = 2; // 请假

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SCHEDULE_ID", nullable = false)
	private ScheduleEntity schedule;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	private StudentEntity student;

	@Column(name = "STATE", nullable = false)
	private int state = STATE_OK;

	@Column(name = "COMMENT", length = 200)
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ScheduleEntity getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleEntity schedule) {
		this.schedule = schedule;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
