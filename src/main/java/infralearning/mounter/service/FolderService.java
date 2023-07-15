package infralearning.mounter.service;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import infralearning.mounter.handler.FolderHandler;
import infralearning.mounter.model.Modulo;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class FolderService {

    @Value("${mounting.url}")
    private String mountRoot;

    @Value("${images.url}")
    private String imagesRepositoryURL;

    @Autowired
    private FolderHandler handler;

    @Value("${mount.max_num_images_per_group}")
    private int maxNumberOfImagesPerGroup;

    @Value("${mount.max_num_interpolated_images}")
    private int maxNumberOfInterpolatedImages;

    private Set<String> imagesInRepository;

    // private ExecutorService executor = Executors.newFixedThreadPool(6);


    public void run(List<Modulo> modulos) throws IOException, InterruptedException {
        setup();
        for (Modulo modulo : modulos) {
            log.warn("");
            log.warn("Creating modulo: " + modulo.getName());
            runModulo(modulo);
        }
        // executor.awaitTermination(24l, java.util.concurrent.TimeUnit.HOURS);
    }


    private void runModulo(Modulo modulo) {
        for (String group : modulo.getGroups()) {
            Path folder = Paths.get(mountRoot, modulo.getName(), group);
            handler.createFolder(folder);
            List<Integer> imagesIDs = modulo.getImagesIDs().get(group);
            int numOfImages = moveImagesToFolder(folder, imagesIDs);
            log.warn("  Creating group: " +  group + " with " + numOfImages + " images");
        }
    }


    private void setup() throws IOException {
        if (mountRoot == null || imagesInRepository == null) {
            handler.deleteAndCreateFolder(mountRoot);
            imagesInRepository = handler.fetchRepository(imagesRepositoryURL);
        }
    }

    
    private int moveImagesToFolder(Path folder, List<Integer> imagesIDs) {
        int count = 0;
        for (Integer id : imagesIDs) {
            List<String> images = listImagesWithSameId(id, imagesInRepository);
            for (String image : images) {
                copyImageToFolders(folder, image);
                if (folder.getParent().getFileName().toString().equals("DETECTION")) {
                    if (++count >= 5*maxNumberOfImagesPerGroup * maxNumberOfInterpolatedImages) {
                        return count;
                    }
                } else {
                    if (++count >= maxNumberOfImagesPerGroup * maxNumberOfInterpolatedImages) {
                        return count;
                    }
                }
            }
        }
        return count;
    }


    private List<String> listImagesWithSameId(Integer id, Set<String> imagesInRepository) {
        List<String> images = new ArrayList<>();
        if (imagesInRepository.contains(id + ".jpg")) {
            images.add(id + ".jpg");
        }
        for (int i = 0; i < maxNumberOfInterpolatedImages; i++) {
            if (imagesInRepository.contains(id + "_" + i + ".jpg")) {
                images.add(id + "_" + i + ".jpg");
            }
        }
        return images;
    }


    private void copyImageToFolders(Path folder, String image) {
        // executor.submit(() -> {
            try {
                Path source = Paths.get(imagesRepositoryURL, image);
                Path destination = Paths.get(folder.toString(), image);
                Files.copy(source, destination, REPLACE_EXISTING);
            } catch (IOException e) {
                Path source = Paths.get(imagesRepositoryURL, image);
                Path destination = Paths.get(folder.toString(), image);
                log.error("Error copying image: " + source + " to " + destination);
            }
        // });
    }

}
