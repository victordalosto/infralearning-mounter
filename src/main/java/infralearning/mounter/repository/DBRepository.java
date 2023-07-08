package infralearning.mounter.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import infralearning.mounter.model.Domain;


public interface DBRepository extends JpaRepository<Domain, Integer> {
    
    @Query(value = """
            SELECT p.id
            FROM placas p
            WHERE p.nome = :nome
            AND p.is_mapped = true
            AND p.is_valid = true
                """, nativeQuery = true)
    List<Integer> getIdsByGroup(@Param("nome") String nome);


    @Query(value = """
            SELECT p.id 
            FROM placas p 
            WHERE p.nome != 'null' 
            AND p.is_mapped = true 
            AND p.is_valid = true 
            """, nativeQuery = true)
    List<Integer> getIdsNotNull();


}
