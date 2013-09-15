package org.mingy.jcm.model;

import java.beans.PropertyChangeListener;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Teacher extends PropertyChangeSupportBean implements
		ICachable<Teacher>, INamedObject {

	private Long id;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	@NotNull(message = "{sex.NotNull}")
	private Integer sex;

	@Length(max = 100, message = "{contacts.MaxLength}")
	private String contacts;

	@Length(max = 100, message = "{specialty.MaxLength}")
	private String specialty;

	@Override
	public void copyTo(Teacher bean) {
		bean.setId(id);
		bean.setName(name);
		bean.setSex(sex);
		bean.setContacts(contacts);
		bean.setSpecialty(specialty);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
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
}
