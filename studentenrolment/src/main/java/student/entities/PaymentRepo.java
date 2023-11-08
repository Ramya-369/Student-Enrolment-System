package student.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

	// 14
	@Query("SELECT p FROM Payment p JOIN p.student s WHERE s.batchid = :batchId")
	List<Payment> findPaymentsByBatchId(Integer batchId);

	// 16
	List<Payment> findPaymentsByMode(Character Mode);

	// 15
	public List<Payment> findByPayDateBetween(LocalDate startDate, LocalDate endDate);

	// 3
	Optional<Payment> findByStudentId(Integer id);

}
