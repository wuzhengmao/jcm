package org.mingy.jcm.model;

import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Student extends PropertyChangeSupportBean implements
		ICachable<Student>, INamedObject {

	private Long id;

	@NotBlank(message = "{name.NotNull}")
	@Length(max = 50, message = "{name.MaxLength}")
	private String name;

	private Date birthday;

	@NotNull(message = "{sex.NotNull}")
	private Integer sex;

	@Length(max = 100, message = "{contacts.MaxLength}")
	private String contacts;

	@Length(max = 100, message = "{address.MaxLength}")
	private String address;

	@Override
	public void copyTo(Student bean) {
		bean.setId(id);
		bean.setName(name);
		bean.setBirthday(birthday);
		bean.setSex(sex);
		bean.setContacts(contacts);
		bean.setAddress(address);
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
}
