package project.diploma.agreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.diploma.agreement.domain.Solution;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    @Modifying
    @Query("delete from Solution s where s.id=:id")
    void deleteById(@Param("id") Integer id);
}
