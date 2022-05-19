package project.diploma.agreement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.diploma.agreement.domain.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {

}
