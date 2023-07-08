package infralearning.mounter.handler;
import java.io.File;
import java.nio.file.Path;
import org.springframework.stereotype.Service;


@Service
public class FolderHandler {


    public void deleteAndCreateFolder(String folder) {
        File f = new File(folder);
        if (f.exists() && f.isDirectory()) {
            deleteFolderContents(f);
        }
        createFolder(folder);
    }


    public void createFolder(String folder) {
        File f = new File(folder);
        if (!f.exists()) {
            f.mkdirs();
        }
    }


    public void createFolder(Path folder) {
        createFolder(folder.toString());
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
