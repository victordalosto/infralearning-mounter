package infralearning.mounter.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;


@Data
public class Modulo {

    private String name;
    private List<String> groups;
    private Map<String, List<Integer>> imagesIDs = new HashMap<>();


}
