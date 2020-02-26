package test.Runner;

import com.flipkart.component.testing.SpecificationRunner;
import org.junit.BeforeClass;
import service.ApiService;

public class SuiteSetUp {

    protected static SpecificationRunner specificationRunner;

    @BeforeClass
    public static void setUp() {
        String configPath = "src/main/resources/config.yml";
        String serviceUrl = "http://localhost:9000";
        if(specificationRunner==null)
            specificationRunner = SpecificationRunner.getInstance(configPath, serviceUrl, ApiService.class);
    }
}
