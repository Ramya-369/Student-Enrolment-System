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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Studentid")
	private int studentId;

	@Column(name = "Name")
	@NotBlank(message = "Student name is required")
	private String name;

	@Column(name = "Email")
	@Email(message = "Invalid email format")
	private String email;

	@Column(name = "Mobile")
	@Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
	private String mobile;

	@Column(name = "Batchid")
	private int batchid;

	@Column(name = "Dateofjoining")
	@NotNull(message = "Start date is required")
	private LocalDate date;

	@JsonIgnore
	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Payment payment;

	@ManyToOne
	@JoinColumn(name = "batchid", insertable = false, updatable = false)
	@JsonIgnore
	private Batch batch;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getBatchid() {
		return batchid;
	}

	public void setBatchid(int batchid) {
		this.batchid = batchid;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", name=" + name + ", email=" + email + ", mobile=" + mobile
				+ ", batchid=" + batchid + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(mobile, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Student) {
			Student other = (Student) obj;
			return Objects.equals(mobile, other.mobile) & Objects.equals(name, other.name);
		} else
			return false;
	}

}
