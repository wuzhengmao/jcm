package org.mingy.jcm.model.orm;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_TEACHER")
public class Teacher implements IEntity {

	private static final long serialVersionUID = 6113019262565286354L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 100)
	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@Column(name = "SEX", nullable = false)
	@NotNull(message = "{sex.NotNull}")
	private Integer sex;

	@Column(name = "CONTACTS", length = 200)
	private String contacts;

	@Column(name = "SPECIALTY", length = 200)
	private String specialty;

	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
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

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}
