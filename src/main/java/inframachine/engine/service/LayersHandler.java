package inframachine.engine.service;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import inframachine.engine.model.DataType;


@Service
public class LayersHandler {


    @Autowired
    private FolderHandler folderHandler;


    public void handleLayer(String layerName, List<Integer> ids) throws IOException {
        Collections.shuffle(ids);
        
        int size = ids.size();
        List<Integer> trainIDs = ids.subList(0, size/2);
        List<Integer> testIDs = ids.subList(size/2, size);

        copyImagesToLayer(DataType.TRAIN, layerName, trainIDs);
        copyImagesToLayer(DataType.TEST, layerName, testIDs);
    }


    private void copyImagesToLayer(DataType type, String layerName, List<Integer> ids) throws IOException {
        for (Integer id : ids) {
            folderHandler.copyImageToLayer(type, layerName, id.toString());
        }
    }
    
}
