package infralearning.engine.service;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FolderHandler {

    @Value("${mounting.url}")
    private String root;
    
    @Value("${images.url}")
    private String imagesURL;

    private List<String> layers = List.of("IDENTIFIER", "CLASSIFIER");

    private List<String> imagesInRepository = new ArrayList<>();


    public void setup() throws IOException {
        File folder = new File(root);
        if (folder.exists() && folder.isDirectory()) {
            deleteFolderContents(folder);
        }
        createFolder(root);
        imagesInRepository = fetchRepository(imagesURL);
    }


    public void createLayers(String group, List<Integer> ids) {
        String identifierGroup = root + "/" + layers.get(0) + "/" + (group.equals("null") ? "null" : "not_null");
        String classifierGroup = root + "/" + layers.get(1) + "/" + group;

        createFolder(identifierGroup);
        createFolder(classifierGroup);
        
        for (Integer id : ids) {
            List<String> images = findImages(id);
            for (String image : images) {
                try {
                    Path source = Paths.get(imagesURL + "/" + image);
                    Files.copy(source, Paths.get(identifierGroup + "/" + image), REPLACE_EXISTING);
                    Files.copy(source, Paths.get(classifierGroup + "/" + image), REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private List<String> findImages(Integer id) {
        return imagesInRepository.stream()
                                 .filter(image -> image.startsWith(id.toString()))
                                 .toList();
    }


    public List<String> fetchRepository(String directoryPath) throws IOException {
        return Files.list(Paths.get(directoryPath))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
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


}
