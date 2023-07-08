package infralearning.mounter.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name = "placas")
public class Domain {

    @Id
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "condicao")
    private String condicao;

    @Column(name = "is_mapped")
    private boolean isMapped;

    @Column(name = "is_valid")
    private boolean isValid;

    
}
