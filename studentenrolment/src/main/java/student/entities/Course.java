package student.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Courses")

public class Course {

	@Id
	@Column(name = "Coursecode")
	@NotBlank(message = "Course code is required")
	private String code;

	@Column(name = "Name")
	@NotBlank(message = "Course name is required")
	private String name;

	@Column(name = "Durationindays")
	@Positive(message = "Duration must be a positive number")
	private int durationDays;

	@Column(name = "Fee")
	@Positive(message = "Fee must be a positive number")
	private double fee;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
	private List<Batch> batches = new ArrayList<Batch>();

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

	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}

	@Override
	public String toString() {
		return "Course [code=" + code + ", name=" + name + ", durationDays=" + durationDays + ", fee=" + fee
				+ ", batches=" + batches + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, name);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Course) {
			Course other = (Course) obj;
			return Objects.equals(code, other.code) && Objects.equals(name, other.name);
		} else
			return false;
	}

}
