package com.flipkart.component.testing;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class DropwizardServiceStarterTest {

    public DropwizardServiceStarterTest(){}

    @FunctionalInterface
    public interface ConstructorFailedActionTemplate {

        void action();

        default void create(Class expected) {
            try {
                action();
                Assert.assertTrue(false);
            } catch (Exception actual) {
                Assert.assertEquals(actual.getClass(), expected);
            }
        }
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private static boolean runMethodInvoked = false;

    private static String[] args;

    public void run (String[] arg) {
        runMethodInvoked = true;
        args = arg;
    }

    @Test
    public void testConstructor() throws IOException {

        // should fail for null config
        ConstructorFailedActionTemplate template = () -> new ServiceStarter(null, "http://localhost:8080", DropwizardServiceStarterTest.class);
        template.create(NullPointerException.class);

        //should fail when file doesn't exist
        template = () -> new ServiceStarter("nonexistentFilePath", "http://localhost:8080", DropwizardServiceStarterTest.class);
        template.create(IllegalArgumentException.class);

        String tempFile = temporaryFolder.newFile("tempFile").getAbsolutePath();

        //should fail for null serviceUrl
        template = () -> new ServiceStarter(tempFile, null, DropwizardServiceStarterTest.class);
        template.create(NullPointerException.class);

        //should fail for serviceUrl other than localhost
        template = () -> new ServiceStarter(tempFile, "http://10.32.45.67:8888", DropwizardServiceStarterTest.class);
        template.create(IllegalArgumentException.class);

        //should fail for null service class
        template = () -> new ServiceStarter(tempFile, "http://localhost:8888", null);
        template.create(NullPointerException.class);


        //should not fail
        new ServiceStarter(tempFile, "http://localhost:8888", ServiceStarter.class);

    }

    @Test
    public void testGetUrl() throws IOException {
        String tempFile = temporaryFolder.newFile("tempFile").getAbsolutePath();
        String url = new ServiceStarter(tempFile, "http://localhost:8888", DropwizardServiceStarterTest.class).getUrl();
        Assert.assertEquals("http://localhost:8888", url);
    }

    @Test
    public void testRunMethodInvocation() throws IOException {
        String tempFile = temporaryFolder.newFile("tempFile").getAbsolutePath();
        new ServiceStarter(tempFile, "http://localhost:8888", DropwizardServiceStarterTest.class).start();
        Assert.assertTrue(runMethodInvoked);
        Assert.assertEquals(2, args.length);
        Assert.assertEquals("server", args[0]);
        Assert.assertEquals(args[1], tempFile);
    }

}
