package inframachine.engine;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class EngineApplicationTests {

	@Test
	void contextLoads() {
		List<Integer> ids = List.of(1,2,3,4,5,6,7,8,9);
		 
        int size = ids.size();
        List<Integer> trainIDs = ids.subList(0, size/2);
        List<Integer> testIDs = ids.subList(size/2, size);

	}

}
