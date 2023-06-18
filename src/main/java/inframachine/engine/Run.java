package inframachine.engine;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import inframachine.engine.repository.DomainRepository;
import inframachine.engine.service.FolderHandler;
import inframachine.engine.service.LayersHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class Run {

    @Autowired
    private FolderHandler folderHandler;

    @Autowired
    private DomainRepository repository;

    @Autowired
    private LayersHandler layersHandler;


    @PostConstruct
    public void init() throws InterruptedException, IOException {
        Thread.sleep(2000);

        log.info("Reseting folder");
        folderHandler.resetFolder();

        log.info("Setting up folders");
        folderHandler.setupFolders();

        log.info("Getting groups from database");
        List<String> groups = repository.getGroups();

        log.info("Creating layers");
        for (String group : groups) {
            folderHandler.createLayer(group);
            List<Integer> ids = repository.getIdsByPrimaryLayer(group);
            layersHandler.handleLayer(group, ids);
        }

        log.info("Done!");
    }

}
