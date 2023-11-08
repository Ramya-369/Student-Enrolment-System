package student.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BatchRepo extends JpaRepository<Batch, Integer> {

	// 6
	@Query("SELECT b FROM Batch b WHERE :currentDate BETWEEN b.startDate AND b.endDate")
	List<Batch> findRunningBatches(LocalDate currentDate);

	// 7
	@Query("SELECT b FROM Batch b WHERE b.endDate < :currentDate")
	List<Batch> findCompletedBatches(LocalDate currentDate);

	// 11
	List<Batch> findByCode(String code);

	// 13
	List<Batch> findBatchesByStartDateBetween(LocalDate startDate, LocalDate endDate);

	// 8
	@Query("SELECT b.students FROM Batch b WHERE :currentDate BETWEEN b.startDate AND b.endDate")
	List<Student> getRunningBatchStudent(LocalDate currentDate);

	// pagenation

	@Query(value = "SELECT * from batches b where b.CourseCode=:courseCode", nativeQuery = true)
	Page<Batch> findBatchesByCourseCode(@Param("courseCode") String courseCode, Pageable pageable);

}
