package project.diploma.agreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.diploma.agreement.domain.ImageDB;

import javax.transaction.Transactional;

@Repository
public interface ImageDBRepository extends JpaRepository<ImageDB, String> {

    @Transactional
    @Modifying
    @Query("delete from ImageDB i where i.id=:id")
    void deleteById(@Param("id") String id);
}
