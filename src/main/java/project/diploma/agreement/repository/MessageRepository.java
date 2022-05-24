package project.diploma.agreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.diploma.agreement.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
