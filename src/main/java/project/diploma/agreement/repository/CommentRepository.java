package project.diploma.agreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.diploma.agreement.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
