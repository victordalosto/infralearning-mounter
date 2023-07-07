package infralearning.engine;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import infralearning.engine.repository.DomainRepository;
import infralearning.engine.service.FolderHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Main {

    @Autowired
    private FolderHandler folderHandler;

    @Autowired
    private DomainRepository dbRepository;


    @PostConstruct
    public void init() throws InterruptedException, IOException {

        log.warn("Setting up folders");
        folderHandler.setup();

        log.warn("Getting groups from database");
        List<String> groups = dbRepository.getGroups();

        log.warn("Creating layers");
        for (String group : groups) {
            log.warn("  Creating group: " +  group);
            List<Integer> ids = dbRepository.getIdsByGroup(group);
            folderHandler.createLayers(group, ids);
        }

        log.warn("Done!");
    }


	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
