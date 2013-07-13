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
@Table(name = "T_CHARGE_LOG")
public class ChargeLog implements IEntity {

	private static final long serialVersionUID = -2073083549331026407L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "APPLY_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date applyDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "COURSE_ID", nullable = false)
	private Course course;

	@Column(name = "TIMES", nullable = false)
	private int times;

	@Column(name = "PRICE", nullable = false, precision = 7, scale = 2)
	private double price;

	@Column(name = "MATERIAL_FEE", nullable = false, precision = 7, scale = 2)
	private double materialFee;

	@Column(name = "RECEIPT_NO", length = 100)
	private String receiptNo;

	@Column(name = "COMMENT", length = 200)
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMaterialFee() {
		return materialFee;
	}

	public void setMaterialFee(double materialFee) {
		this.materialFee = materialFee;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
