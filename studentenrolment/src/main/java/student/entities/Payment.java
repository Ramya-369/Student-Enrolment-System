package student.entities;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Paymentid")
	private int paymentId;

	@Column(name = "Studentid")
	@Positive(message = "Student ID must be a positive number")
	private int studentId;

	@Column(name = "Amount")
	@DecimalMin(value = "0.0", message = "Amount must be a non-negative value")
	private double amount;

	@Column(name = "Paydate")
	@PastOrPresent(message = "Payment must be in the past or present")
	private LocalDate payDate;

	@Column(name = "Paymode")
	private char mode;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "Studentid", referencedColumnName = "Studentid", insertable = false, updatable = false)
	private Student student;

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getPayDate() {
		return payDate;
	}

	public void setPayDate(LocalDate payDate) {
		this.payDate = payDate;
	}

	public char getMode() {
		return mode;
	}

	public void setMode(char mode) {
		this.mode = mode;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", studentId=" + studentId + ", amount=" + amount + ", payDate="
				+ payDate + ", mode=" + mode + ", student=" + student + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(studentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Payment) {
			Payment other = (Payment) obj;
			return Objects.equals(studentId, other.studentId);
		} else
			return false;
	}

}