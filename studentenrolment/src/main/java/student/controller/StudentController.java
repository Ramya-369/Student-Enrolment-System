package student.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import student.entities.Batch;
import student.entities.BatchRepo;
import student.entities.Course;
import student.entities.CourseRepo;
import student.entities.Payment;
import student.entities.PaymentRepo;
import student.entities.Student;
import student.entities.StudentPaymentDTO;
import student.entities.StudentRepo;

@RestController
public class StudentController {

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private BatchRepo batchRepo;

	@Autowired
	private CourseRepo courseRepo;

	// 1 Add,Update,Delete a Course
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/courses/add")
	@Operation(summary = "Add a new course", description = "Create and add a new course to the courses.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course added successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request, Course Code Already Present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Course addNewCourse(@Valid @RequestBody Course course) {
		try {
			List<Course> existingcourses = courseRepo.findAll();
			boolean ifexist = existingcourses.contains(course);
			if (ifexist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course Code Already Present");
			courseRepo.save(course);
			return course;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/course/update/{code}")
	@Operation(summary = "Update course information", description = "Update the details of an existing course")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course updated successfully"),
			@ApiResponse(responseCode = "404", description = "Course code Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateOneCourse(@Valid @PathVariable("code") String code, @RequestBody Course updatedCourse) {
		try {
			Optional<Course> optCourse = courseRepo.findById(code);

			if (optCourse.isPresent()) {
				Course courseToUpdate = optCourse.get();
				courseToUpdate.setName(updatedCourse.getName());
				courseToUpdate.setFee(updatedCourse.getFee());
				courseToUpdate.setDurationDays(updatedCourse.getDurationDays());
				courseRepo.save(courseToUpdate);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course code Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/course/delete/{code}")
	@Operation(summary = "Delete a course", description = "Delete a course from the course using its unique code.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Course code Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneCourse(@PathVariable("code") String code) {
		try {
			Optional<Course> optCourse = courseRepo.findById(code);
			if (optCourse.isPresent())
				courseRepo.delete(optCourse.get());
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course code Not Found!");

		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	// 2 Add,Update,Delete a Batch
	@CrossOrigin
	@GetMapping("/batches")
	@Operation(summary = "Get information about all batches", description = "Retrieve information about all batches")
	public List<Batch> getAllBatches() {
		return batchRepo.findAll();
	}

	@GetMapping("/batches/{id}")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the batches by given id"),
			@ApiResponse(responseCode = "404", description = "Batch id not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error"), })
	@Operation(summary = "Get information about a specific batch by its ID")
	public Batch getOneBatch(@PathVariable("id") Integer id) {
		try {
			Optional<Batch> optBatch = batchRepo.findById(id);
			if (optBatch.isPresent()) {
				return optBatch.get();
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/batches/add")
	@Operation(summary = "Add a new batch", description = "Create and add a new batch")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch added successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request, Batch Id Already Present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Batch addOneBatch(@Valid @RequestBody Batch batch) {
		try {
			List<Batch> existingbatches = batchRepo.findAll();
			boolean ifexist = existingbatches.contains(batch);
			if (ifexist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Batch Already Present");
			batchRepo.save(batch);
			return batch;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/batch/update/{id}")
	@Operation(summary = "Update batch information", description = "update and details of existing batch")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch updated successfully"),
			@ApiResponse(responseCode = "404", description = "Batch not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateOneBatch(@Valid @PathVariable("id") Integer id, @RequestBody Batch updatedBatch) {
		try {
			Optional<Batch> optBatch = batchRepo.findById(id);
			if (optBatch.isPresent()) {
				Batch batchToUpdate = optBatch.get();
				batchToUpdate.setCode(updatedBatch.getCode());
				batchToUpdate.setStartDate(updatedBatch.getStartDate());
				batchToUpdate.setEndDate(updatedBatch.getEndDate());
				batchToUpdate.setTimings(updatedBatch.getTimings());
				batchToUpdate.setDuration(updatedBatch.getDuration());
				batchToUpdate.setFee(updatedBatch.getFee());
				batchRepo.save(batchToUpdate);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
			}
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/batch/delete/{id}")
	@Operation(summary = "Delete a batch", description = "Remove a batch from the batches using its unique identifier.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batch deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Batch not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneBatch(@PathVariable("id") Integer id) {
		try {
			Optional<Batch> optBatch = batchRepo.findById(id);
			if (optBatch.isPresent())
				batchRepo.delete(optBatch.get());
			else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batch id not found");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 3 Add,Update,Delete a Student & Payment
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addstudentandpayment")
	@Transactional
	@Operation(summary = "Add a new student and payment information together in a transaction", description = "Create and add a new student along with their payment information in a single transaction.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Student and payment added successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request - Student Already Present"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public StudentPaymentDTO addNewStudentPayment(@Valid @RequestBody StudentPaymentDTO studentandpayment) {
		try {
			Student s = new Student();
			s.setName(studentandpayment.getName());
			s.setEmail(studentandpayment.getEmail());
			s.setMobile(studentandpayment.getMobile());
			s.setBatchid(studentandpayment.getBatchid());
			s.setDate(studentandpayment.getJoinDate());
			List<Student> existingstudent = studentRepo.findAll();
			boolean exist = existingstudent.contains(s);
			if (exist)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student Already Present");
			studentRepo.save(s);
			Payment p = new Payment();
			p.setStudentId(s.getStudentId());
			p.setAmount(studentandpayment.getAmount());
			p.setPayDate(studentandpayment.getPayDate());
			p.setMode(studentandpayment.getPayMode());
			paymentRepo.save(p);
			return studentandpayment;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatestudentandpayment/{id}")
	@Transactional
	@Operation(summary = "Update student and payment information together in a transaction", description = "Update and modify the details of a student along with their payment information in a single transaction.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student and payment information updated successfully"),
			@ApiResponse(responseCode = "404", description = "Student Id Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public StudentPaymentDTO updateStudentAndPayment(@Valid @PathVariable("id") Integer id,
			@RequestBody StudentPaymentDTO studentandpayment) {
		try {
			Optional<Student> student = studentRepo.findById(id);
			if (student.isPresent()) {
				Student s = student.get();
				s.setName(studentandpayment.getName());
				s.setEmail(studentandpayment.getEmail());
				s.setMobile(studentandpayment.getMobile());
				s.setBatchid(studentandpayment.getBatchid());
				s.setDate(studentandpayment.getJoinDate());
				studentRepo.save(s);
				Optional<Payment> payment = paymentRepo.findByStudentId(id);
				Payment p = payment.get();
				p.setAmount(studentandpayment.getAmount());
				p.setPayDate(studentandpayment.getPayDate());
				p.setMode(studentandpayment.getPayMode());
				paymentRepo.save(p);
				return studentandpayment;
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Id Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/student/delete/{id}")
	@Transactional
	@Operation(summary = "Delete a student and associated payment", description = "Remove a student and their associated payment information")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student and associated payment deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Student Id Not Found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneStudent(@PathVariable("id") Integer id) {
		try {
			Optional<Student> optStudent = studentRepo.findById(id);
			if (optStudent.isPresent()) {
				studentRepo.deleteById(id);
			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Id Not Found!");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 4 Update a Payment
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/payment/update/{payid}")
	@Operation(summary = "Update payment information by payment ID", description = "Update and modify the details of a payment using its unique payment ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Payment information updated successfully"),
			@ApiResponse(responseCode = "404", description = "Payment with specified ID not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Payment updateOneCourse(@Valid @PathVariable("payid") int payid, @RequestBody Payment updatedPayment) {
		try {
			var payment = paymentRepo.findById(payid);
			if (payment.isPresent()) {
				Payment paymentToUpdate = payment.get();
				paymentToUpdate.setStudentId(updatedPayment.getStudentId());
				paymentToUpdate.setAmount(updatedPayment.getAmount());
				paymentToUpdate.setPayDate(updatedPayment.getPayDate());
				paymentToUpdate.setMode(updatedPayment.getMode());
				return paymentRepo.save(paymentToUpdate);

			} else
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment with id " + payid + " not found");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	// 5 List all Courses
	@GetMapping("/courses")
	@Operation(summary = "Get information about all courses", description = "Retrieve information about all courses")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of all courses"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Course> getAllCourses() {
		return courseRepo.findAll();
	}

	// 6 List all Batches currently running
	@GetMapping("/batches/currently-running")
	@Operation(summary = "List batches that are currently running", description = "Retrieve a list of batches that are currently in progress or running.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of currently running batches"),
			@ApiResponse(responseCode = "204", description = "No currently running batches found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> listCurrentlyRunningBatches() {
		try {
			LocalDate currentDate = LocalDate.now();
			List<Batch> currentlyRunningBatches = batchRepo.findRunningBatches(currentDate);
			if (!currentlyRunningBatches.isEmpty()) {
				return currentlyRunningBatches;
			} else
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No currently running batches ");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	// 7 List all Batches that were completed
	@GetMapping("/batches/completed")
	@Operation(summary = "List batches that have been completed", description = "")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of completed batches"),
			@ApiResponse(responseCode = "204", description = "No completed batches found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> listCompletedBatches() {
		try {
			LocalDate currentDate = LocalDate.now();
			List<Batch> completedBatches = batchRepo.findCompletedBatches(currentDate);
			if (!completedBatches.isEmpty()) {
				return completedBatches;
			} else
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No completed batches");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 8 List all Students of currently running batches
	@GetMapping("/runningbatchesstudents")
	@Operation(summary = "List students currently in running batches", description = "Retrieve a list of students who are currently enrolled in batches that are in progress or running.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of students in running batches"),
			@ApiResponse(responseCode = "204", description = "No students found in running batches"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> getPresentRunningBatchesStudent() {
		try {
			List<Student> batches = batchRepo.getRunningBatchStudent(LocalDate.now());
			if (!batches.isEmpty()) {
				return batches;
			} else
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No students found in running batches");
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 9 List all Students of a particular Course
	@GetMapping("/students/{code}")
	@Operation(summary = "List students for a specific course by its code", description = "Retrieve a list of students who are enrolled in a course with a specific code.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of students for the specified course"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> getStudentForCourse(@PathVariable String code) {
		List<Student> students = studentRepo.findStudentsByCourse(code);
		return students; // Return the list, which may be empty if no students are found.
	}

	// 10 List all Students of a particular Batch
	@GetMapping("/students/batch/{batchId}")
	@Operation(summary = "List students for a specific batch by its batch ID", description = "Retrieve a list of students who are enrolled in a batch with a specific batch ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of students for the specified batch"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> listStudentsForBatch(@PathVariable("batchId") int batchId) {
		List<Student> studentsForBatch = studentRepo.findByBatchid(batchId);
		return studentsForBatch; // Return the list, which may be empty if no students are found.
	}

	// 11 List all Batches of a particular Course
	@GetMapping("/batchesbycode/{code}")
	@Operation(summary = "List batches by course code", description = "Retrieve a list of batches that are associated with a course having a specific course code.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of batches for the specified course"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> getBatchesByCode(@PathVariable("code") String code) {
		List<Batch> batches = batchRepo.findByCode(code);
		return batches; // Return the list, which may be empty if no batches are found.
	}

	// 12 List all Students based on the given partial name
	@GetMapping("/students/search")
	@Operation(summary = "Search for students by partial name", description = "Retrieve a list of students whose names match a partial search query.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved the list of students matching the search query"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Student> findStudentsByPartialName(@RequestParam String partialName) {
		List<Student> students = studentRepo.findStudentsByPartialName(partialName);
		return students; // Return the list, which may be empty if no students are found.
	}

	// 13 List all batches that were started between two given dates
	@GetMapping("/batches_btw_dates")
	@Operation(summary = "Search for batches with start dates within a date range", description = "Retrieve a list of batches whose start dates fall within a specified date range.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of batches within the specified date range"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Batch> findBatchesBetweenDates(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		List<Batch> batches = batchRepo.findBatchesByStartDateBetween(startDate, endDate);
		return batches; // Return the list, which may be empty if no batches are found.
	}

	// 14 List all payments for particular batch
	@GetMapping("/payments/batch/{batchId}")
	@Operation(summary = "List payments for a specific batch by its batch ID", description = "Retrieve a list of payments associated with a specific batch using its unique batch ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of payments for the specified batch"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> listPaymentsForBatch(@PathVariable("batchId") Integer batchId) {
		List<Payment> payments = paymentRepo.findPaymentsByBatchId(batchId);
		return payments; // Return the list, which may be empty if no payments are found.
	}

	// 15 List all payments between two dates
	@GetMapping("/payments_btw_dates")
	@Operation(summary = "List payments with pay dates within a date range", description = "Retrieve a list of payments with payment dates that fall within a specified date range.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of payments with pay dates within the specified date range"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> getPayments(@RequestParam("startDate") LocalDate date1,
			@RequestParam("endDate") LocalDate date2) {
		List<Payment> payments = paymentRepo.findByPayDateBetween(date1, date2);
		return payments; // Return the list, which may be empty if no payments are found.
	}

	// 16 List all payments of a particular mode
	@GetMapping("/payments/by-paymode")
	@Operation(summary = "Search for payments by payment mode", description = "Retrieve a list of payments based on their payment mode, such as 'U/N/C.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of payments with the specified payment mode"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public List<Payment> findPaymentsByPayMode(@RequestParam Character mode) {
		List<Payment> payments = paymentRepo.findPaymentsByMode(mode);
		return payments; // Return the list, which may be empty if no payments are found.
	}

	// pagenation
	@GetMapping("/getAllBatchesByCourseCode/{courseCode}")
	@Operation(summary = "Get batches by course code", description = "Retrieve batches by a given course code and sort by batch ID using pagination")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Batches found by the given course code"),
			@ApiResponse(responseCode = "404", description = "No batches for the given Course Code"),
			@ApiResponse(responseCode = "500", description = "There is some issue in Internal Server") })
	public Page<Batch> getBatchesByCourseCode(@PathVariable String courseCode,
			@RequestParam(name = "pageSize", defaultValue = "2") int pageSize, Pageable pageable) {
		Sort sort = Sort.by("batchid").ascending(); // Sort by batch ID in ascending order
		try {
			Page<Batch> batches = batchRepo.findBatchesByCourseCode(courseCode,
					PageRequest.of(pageable.getPageNumber(), pageSize, sort));

			if (batches.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No batches for the given Course Code!");
			}

			return batches;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// catch the error
	@GetMapping("/courses/{code}")
	@Operation(summary = "Get information about a specific course by its code", description = "Retrieve details of a course using its unique code.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the course by given code"),
			@ApiResponse(responseCode = "404", description = "Course code not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Object getCourse(@PathVariable("code") String code) {
		try {
			return getOneCourse(code);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public Course getOneCourse(String code) {
		var optCourse = courseRepo.findById(code);
		if (optCourse.isPresent())
			return optCourse.get();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Code Not Found!");
	}

}