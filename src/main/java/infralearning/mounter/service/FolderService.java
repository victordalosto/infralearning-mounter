package infralearning.mounter.service;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import infralearning.mounter.handler.FolderHandler;


@Service
public class FolderService {

    @Value("${mounting.url}")
    private String mountRoot;

    @Value("${images.url}")
    private String imagesRepositoryURL;

    @Autowired
    private FolderHandler handler;

    private List<String> activities = List.of("DETECTION", "CLASSIFICATION");
    private Set<String> imagesInRepository = new HashSet<>();

    private int maxNumberOfImagesPerGroup = 100;
    private int maxNumberOfImagesInterpolated = 1;


    public void setup() throws IOException {
        handler.deleteAndCreateFolder(mountRoot);
        imagesInRepository = fetchRepository(imagesRepositoryURL);
    }


    
    public int FoldersLayers(String group, List<Integer> imagesIDs) {
        List<Path> folders = new ArrayList<>();
        for (String activity : activities) {
            Path folder = Paths.get(mountRoot, activity, getNameLayer(group, activity));
            handler.createFolder(folder);
            folders.add(folder);
        }
        int numImages = moveImagesToFolder(folders, imagesIDs);
        return numImages;
    }

    


    private Set<String> fetchRepository(String directoryPath) throws IOException {
        return Files.list(Paths.get(directoryPath))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
    }


    private String getNameLayer(String group, String folderLayerCategory) {
        if (folderLayerCategory.toLowerCase().contains("DETECTION"))
            return (group.toLowerCase().equals("null") ? "null" : "not_null");
        else
            return group;
    }


    private int moveImagesToFolder(List<Path> folders, List<Integer> imagesIDs) {
        int count = 0;
        for (Integer id : imagesIDs) {
            List<String> images = listImagesWithSameId(id, imagesInRepository);
            for (String image : images) {
                copyImageToFolders(folders, image);
                if (++count >= maxNumberOfImagesPerGroup) {
                    return count;
                }
            }
        
        }
        return count;
    }



    private void copyImageToFolders(List<Path> folders, String image) {
        try {
            Path source = Paths.get(imagesRepositoryURL, image);
            for (Path folder : folders) {
                Files.copy(source, Paths.get(folder.toString(), image), REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<String> listImagesWithSameId(Integer id, Set<String> imagesInRepository) {
        List<String> images = new ArrayList<>();
        if (imagesInRepository.contains(id + ".jpg")) {
            images.add(id + ".jpg");
        }
        for (int i = 0; i <= maxNumberOfImagesInterpolated; i++) {
            if (imagesInRepository.contains(id + "_" + i + ".jpg")) {
                images.add(id + "_" + i + ".jpg");
            }
        }
        return images;
    }

}
