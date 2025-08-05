package mg.apprologic.apprologic.repository;

import mg.apprologic.apprologic.model.TestFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestFileRepository extends JpaRepository<TestFile,Integer> {
}
