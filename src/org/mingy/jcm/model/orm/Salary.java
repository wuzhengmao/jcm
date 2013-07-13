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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.mingy.kernel.bean.IEntity;

@Entity
@Table(name = "T_SALARY")
public class Salary implements IEntity {

	private static final long serialVersionUID = -8113922626698910203L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TEACHER_ID", nullable = false)
	private Teacher teacher;

	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@OneToMany(mappedBy = "salary", fetch = FetchType.LAZY)
	private List<SalaryDetail> details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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

	public List<SalaryDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SalaryDetail> details) {
		this.details = details;
	}
}
