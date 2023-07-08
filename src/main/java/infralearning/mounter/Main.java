package infralearning.mounter;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import infralearning.mounter.model.Modulo;
import infralearning.mounter.service.FolderService;
import jakarta.annotation.PostConstruct;


@SpringBootApplication
public class Main {

    @Autowired
    private List<Modulo> modulos;

    @Autowired
    private FolderService service;


    @PostConstruct
    public void init() throws InterruptedException, IOException {
        service.run(modulos);
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
