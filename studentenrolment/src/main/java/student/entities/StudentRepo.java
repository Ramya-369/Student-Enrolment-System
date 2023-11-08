package student.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepo extends JpaRepository<Student, Integer> {

	// 10
	List<Student> findByBatchid(Integer batchId);

	// 12
	@Query("SELECT s FROM Student s WHERE s.name LIKE %:partialName%")
	List<Student> findStudentsByPartialName(String partialName);

	// 9
	@Query("SELECT s FROM Student s WHERE s.batch.course.code = :code")
	List<Student> findStudentsByCourse(@Param(value = "code") String courseCode);

}
