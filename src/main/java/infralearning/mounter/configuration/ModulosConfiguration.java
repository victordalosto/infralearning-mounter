package infralearning.mounter.configuration;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import infralearning.mounter.model.Modulo;
import infralearning.mounter.repository.DBRepository;


@Configuration
public class ModulosConfiguration {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DBRepository dbRepository;


    @Bean
    public List<Modulo> modulos() throws StreamReadException, DatabindException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource("classpath:modulo-schema.json");
        List<Modulo> modulos = Arrays.asList(objectMapper.readValue(resource.getInputStream(), Modulo[].class));
        populateModulosWithIdsFromDatabase(modulos);
        return modulos;
    }




    private void populateModulosWithIdsFromDatabase(List<Modulo> modulos) {
        for (Modulo modulo : modulos) {
            for (String group : modulo.getGroups()) {
                List<Integer> idsByGroup;
                if (group.equals("not_null"))
                    idsByGroup = dbRepository.getIdsNotNull();
                else 
                    idsByGroup = dbRepository.getIdsByGroup(group);
                modulo.getImagesIDs().put(group, idsByGroup);
            }
        }
    }

}


