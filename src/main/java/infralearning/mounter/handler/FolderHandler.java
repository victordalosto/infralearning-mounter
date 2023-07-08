package infralearning.mounter.handler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


@Service
public class FolderHandler {

    
    public void deleteAndCreateFolder(String folder) {
        File f = new File(folder);
        if (f.exists() && f.isDirectory()) {
            deleteFolderContents(f);
        }
        createFolder(f.toPath());
    }


    public void createFolder(Path file) {
        File f = file.toFile();
        if (!f.exists()) {
            f.mkdirs();
        }
    }


    public Set<String> fetchRepository(String directoryPath) throws IOException {
        return Files.list(Paths.get(directoryPath))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
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
}
