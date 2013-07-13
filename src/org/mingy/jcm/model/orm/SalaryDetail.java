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
@Table(name = "T_SALARY_DETAIL")
public class SalaryDetail implements IEntity {

	private static final long serialVersionUID = 2161255440738454950L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SALARY_ID", nullable = false)
	private Salary salary;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "COURSE_ID", nullable = false)
	private Course course;

	@Column(name = "TIMES", nullable = false)
	private int times;

	@Column(name = "UNIT_PRICE", nullable = false, precision = 7, scale = 2)
	private double unitPrice;

	@Column(name = "TOTAL_PRICE", nullable = false, precision = 7, scale = 2)
	private double totalPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
