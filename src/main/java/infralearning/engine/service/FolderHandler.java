package infralearning.engine.service;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FolderHandler {

    @Value("${mounting.url}")
    private String mountRoot;

    @Value("${images.url}")
    private String imagesRepositoryURL;

    private List<String> layers = List.of("DETECTION", "CLASSIFICATION");
    private Set<String> imagesInRepository = new HashSet<>();

    private int maxNumberOfImagesPerGroup = Integer.MAX_VALUE;
    private int maxNumberOfImagesInterpolated = 30;


    public void setup() throws IOException {
        File root = new File(mountRoot);
        if (root.exists() && root.isDirectory()) {
            deleteFolderContents(root);
        }
        createFolder(mountRoot);
        imagesInRepository = fetchRepository(imagesRepositoryURL);
    }


    /**
     * Creates the Folders containing the labels and its images
     */
    public void createLayers(String group, List<Integer> ids) {
        List<String> folders = new ArrayList<>();
        for (String folderLayerCategory : layers) {
            String nameLayer = getNameLayer(group, folderLayerCategory);
            String folder = mountRoot 
                            + "/" + folderLayerCategory 
                            + "/" + nameLayer;
            createFolder(folder);
            folders.add(folder);
        }
        moveImagesToLayer(ids, folders);
    }


    private void deleteFolderContents(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolderContents(file);
                }
                file.delete();
            }
        }
    }


    private void createFolder(String folderSTR) {
        File folder = new File(folderSTR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
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


    private void moveImagesToLayer(List<Integer> ids, List<String> categorys) {
        int count = 0;
        for (Integer id : ids) {
            List<String> images = listMatchImages(id, imagesInRepository);
            for (String image : images) {
                try {
                    Path source = Paths.get(imagesRepositoryURL + "/" + image);
                    for (String category : categorys) {
                        Files.copy(source, Paths.get(category + "/" + image), REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (++count > maxNumberOfImagesPerGroup) {
                break;
            }
        }
    }


    private List<String> listMatchImages(Integer id, Set<String> imagesInRepository) {
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
