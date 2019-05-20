print 'Create a module without any parent in your repo.. Done?',
raw_input()

print "--------------------------------------------------"
print "Step 1: Prepare the Project POM"
print "--------------------------------------------------"
print 'Add two dependencies along with build and repository tags'
print '1) your container jar'
print '2) component-testing jar mentioned below'

print '''
<dependency>
    <artifactId>component-testing</artifactId>
    <groupId>com.flipkart</groupId>
    <version>1.1-SNAPSHOT</version>
</dependency>
'''

print "Done with preparing pom?",
raw_input()

print "------------------------------------------------"
print 'Step 2: Time to Prepare the Test config file'
print "------------------------------------------------"

print 'Create a file (src/test/resources/service-config/test-config.yml) and copy your production config here.'
print ''
print ''

print '/*--------------------------------------------------------------------------------------------------------------'
print "IMPORTANT: replace all your original dependencies following below conventions: Don't keep a Prodution endpoint"
print '--------------------------------------------------------------------------------------------------------------*/'

print 'for ES: localhost:9300'
print 'for Http: localhost:7777'
print 'for Mysql: localhost:3306 : userName and password respectively'
print 'for Zookeeper: localhost:2181'
print 'for aerospike localhost:3000'
print 'for redis sentinel localhost:26379'
print 'for solr localhost:8983'

print ''

print "Done with config file preparion ?",
raw_input()

print '------------------------------------------------------'
print 'Step 3: Outline Preparation'
print '-----------------------------------------------------'

print 'Enter the port on which your service will spawn:',
port = int(raw_input())
print 'Enter the Main class of your application (Eg: A.class)',
serviceClass = raw_input()

print 'create a class BaseIntegrationTest with contents below in test directory'

print('''
import com.flipkart.component.testing.orchestrators.SpecificationRunner;
import org.junit.BeforeClass;

public abstract class BaseIntegrationTest{
    
    protected static SpecificationRunner specificationRunner;

    @BeforeClass
    public static void setUp() {
        String configPath = "src/test/resources/service-config/test-config.yaml";
        String serviceUrl = "http://localhost:%s"; 
        
        if(specificationRunner==null)
            specificationRunner = new SpecificationRunner(configPath, serviceUrl, %s.class );
        }
    }
''' % (str(port), serviceClass))

print 'Done with base test class ?'
raw_input()



print '------------------------------------------------------'
print 'Step 4: Creating Test Specification file'
print '------------------------------------------------------'
print 'Provide the inputs as asked below and copy the generated test specifications to the specification file under folder src/test/resources'
execfile("create-spec.py")

print 'Done with creating the test specification json ?'
raw_input()

print '------------------------------------------------------'
print 'Step 5: Creating Test class'
print '------------------------------------------------------'

print 'Create a test class with the content as shown below : for ex TestClass.class'

print '''

public class TestClass extends BaseIntegrationTest {
    List<Observation> observations;
    
    @Test
    public void testCase1() throws Exception {
        String specFilePath ="path_to_specification_file.json";
        observations = specificationRunner.runLite(specFilePath);
       
       //add assertions as required
        Assert.assertEquals(200,((HttpObservation) observations.get(0)).getStatuscode());
        
    }
    
     @Test
    public void testCase2() throws Exception {
        String specFilePath ="path_to_specification_file.json";
        observations = specificationRunner.runLite(specFilePath);
       
       //add assertions as required
        Assert.assertEquals(200,((HttpObservation) observations.get(0)).getStatuscode());
        
    }
    
    }
'''