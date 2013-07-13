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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_STUDENT")
public class Student implements IEntity {

	private static final long serialVersionUID = -7322340602835650661L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;

	@Column(name = "BIRTHDAY")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(name = "SEX", nullable = false)
	private Integer sex;

	@Column(name = "CONTACTS", length = 200)
	private String contacts;

	@Column(name = "ADDRESS", length = 200)
	private String address;

	@ManyToMany(targetEntity = Course.class, cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(name = "T_COURSE_STUDENT", joinColumns = { @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID", nullable = false) })
	private List<Course> courses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}
