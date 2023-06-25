package infralearning.engine;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import infralearning.engine.repository.DomainRepository;
import infralearning.engine.service.FolderHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class Run {

    @Autowired
    private FolderHandler folderHandler;

    @Autowired
    private DomainRepository repository;


    @PostConstruct
    public void init() throws InterruptedException, IOException {
        Thread.sleep(3000);

        log.info("Setting up folders");
        folderHandler.setup();

        log.info("Getting groups from database");
        List<String> groups = repository.getGroups();

        log.info("Creating layers");
        for (String group : groups) {
            List<Integer> ids = repository.getIdsByPrimaryLayer(group);
            folderHandler.createLayers(group, ids);
        }

        log.info("Done!");
    }

}
