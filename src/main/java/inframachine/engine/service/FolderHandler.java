package inframachine.engine.service;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import inframachine.engine.model.DataType;
import lombok.Getter;


@Service
public class FolderHandler {

    @Value("${inframachine.mounting.url}")
    private String root;
    
    @Value("${inframachine.images.url}")
    private String imagesURL;

    @Getter
    private Path trainFolder;

    @Getter
    private Path testFolder;


    public void resetFolder() {
        File folder = new File(root);
        if (folder.exists() && folder.isDirectory()) {
            deleteFolderContents(folder);
        }
    }


    public void setupFolders() {
        createFolder(root);
        trainFolder = createFolder(root + "/" + DataType.TRAIN.toString());
        testFolder  = createFolder(root + "/" + DataType.TEST.toString());
    }


    public void createLayer(String group) {
        createFolder(trainFolder + "/" + group);
        createFolder(testFolder  + "/" + group);
    }


    public void copyImageToLayer(DataType type, String group, String id) throws IOException {
        Path originalImage = Paths.get(imagesURL, id + ".jpg");
        Path destImage = Paths.get(root, type.toString(), group, id + ".jpg");
        Files.copy(originalImage, destImage, REPLACE_EXISTING);
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


    private Path createFolder(String folderSTR) {
        File folder = new File(folderSTR);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.toPath();
    }


}
