package org.mingy.jcm.model.orm;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_COURSE")
public class Course implements IEntity {

	private static final long serialVersionUID = -6752139214046766745L;

	public static final int TYPE_LONG_PERIOD = 0; // 长期的课程，没有固定的开始日期和结束日期
	public static final int TYPE_FIXED_PERIOD = 1; // 定期的课程，有固定的开始日期和结束日期

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "CODE", nullable = false, length = 20)
	private String code;

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TEACHER_ID")
	private Teacher teacher;

	@Column(name = "CLASSROOM", length = 100)
	private String classroom;

	@Column(name = "TYPE", nullable = false)
	private int type = TYPE_LONG_PERIOD;

	@Column(name = "SCHEDULE", length = 200)
	private String schedule;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column(name = "TIMES")
	private Integer times;

	@Column(name = "PRICE", precision = 7, scale = 2)
	private Double price;

	@Column(name = "MATERIAL_FEE", precision = 7, scale = 2)
	private Double materialFee;

	@ManyToMany(targetEntity = Student.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "T_COURSE_STUDENT", joinColumns = { @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", nullable = false) })
	private List<Student> students;

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
		this.name = name;
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
