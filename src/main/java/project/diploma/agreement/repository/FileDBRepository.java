package project.diploma.agreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.diploma.agreement.domain.FileDB;

import java.util.List;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

    List<FileDB> findBySolution_Id(Integer id);

    @Modifying
    @Query("delete from FileDB f where f.id=:id")
    void deleteById(@Param("id") String id);
}
