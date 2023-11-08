package student.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "Batches")
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Batchid")
	private int batchid;

	@Column(name = "Coursecode")
	@NotBlank(message = "Course code is required")
	private String code;

	@Column(name = "Startdate")
	@NotNull(message = "Start date is required")
	private LocalDate startDate;

	@Column(name = "Enddate")
	@NotNull(message = "Start date is required")
	private LocalDate endDate;

	@Column(name = "Timings")
	@NotBlank(message = "Timings are required")
	private String timings;

	@Column(name = "Durationindays")
	@Positive(message = "Duration must be a positive number")
	private int duration;

	@Column(name = "Fee")
	@Positive(message = "fees must be a positive number")
	private double fee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "Coursecode", insertable = false, updatable = false)
	private Course course;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "batch")
	@JsonIgnore
	private List<Student> students = new ArrayList<Student>();

	public int getBatchId() {
		return batchid;
	}

	public void setBatchId(int batchId) {
		this.batchid = batchId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getTimings() {
		return timings;
	}

	public void setTimings(String timings) {
		this.timings = timings;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Batch [batchId=" + batchid + ", code=" + code + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", timings=" + timings + ", duration=" + duration + ", fee=" + fee + ", course=" + course
				+ ", students=" + students + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(startDate, timings);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Batch) {
			Batch other = (Batch) obj;
			return (Objects.equals(startDate, other.startDate) & Objects.equals(timings, other.timings));
		} else
			return false;
	}

}
