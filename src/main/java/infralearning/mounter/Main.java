package infralearning.mounter;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import infralearning.mounter.repository.DomainRepository;
import infralearning.mounter.service.FolderService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Main {

    @Autowired
    private FolderService folderHandler;

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
            List<Integer> ids = dbRepository.getIdsByGroup(group);
            int numOfImages = folderHandler.FoldersLayers(group, ids);
            log.warn("  Creating group: " +  group + " with " + numOfImages + " images");
        }

        log.warn("Done!");
    }


	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
