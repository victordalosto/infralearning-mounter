package infralearning.engine.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import infralearning.engine.model.Domain;


public interface DomainRepository extends JpaRepository<Domain, Integer> {

    @Query("""
        SELECT DISTINCT p.primaryLayer
        FROM placas p
        WHERE p.isMapped = true
            """)
    List<String> getGroups();


    @Query("""
        SELECT p.id
        FROM placas p
        WHERE p.isMapped = true
        AND p.primaryLayer = :group
            """)
    List<Integer> getIdsByGroup(String group);

}
