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
                Assert.assertTrue(actual.getClass().equals(expected));
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
        ConstructorFailedActionTemplate template = () -> new DropwizardServiceStarter(null, "http://localhost:8080", DropwizardServiceStarterTest.class);
        template.create(NullPointerException.class);

        //should fail when file doesn't exist
        template = () -> new DropwizardServiceStarter("nonexistentFilePath", "http://localhost:8080", DropwizardServiceStarterTest.class);
        template.create(IllegalArgumentException.class);

        String tempFile = temporaryFolder.newFile("tempFile").getAbsolutePath();

        //should fail for null serviceUrl
        template = () -> new DropwizardServiceStarter(tempFile, null, DropwizardServiceStarterTest.class);
        template.create(NullPointerException.class);

        //should fail for serviceUrl other than localhost
        template = () -> new DropwizardServiceStarter(tempFile, "http://10.32.45.67:8888", DropwizardServiceStarterTest.class);
        template.create(IllegalArgumentException.class);

        //should fail for null service class
        template = () -> new DropwizardServiceStarter(tempFile, "http://localhost:8888", null);
        template.create(NullPointerException.class);


        //should not fail
        new DropwizardServiceStarter(tempFile, "http://localhost:8888", DropwizardServiceStarter.class);

    }

    @Test
    public void testGetUrl() throws IOException {
        String tempFile = temporaryFolder.newFile("tempFile").getAbsolutePath();
        String url = new DropwizardServiceStarter(tempFile, "http://localhost:8888", DropwizardServiceStarterTest.class).getUrl();
        Assert.assertEquals("http://localhost:8888", url);
    }

    @Test
    public void testRunMethodInvocation() throws IOException {
        String tempFile = temporaryFolder.newFile("tempFile").getAbsolutePath();
        new DropwizardServiceStarter(tempFile, "http://localhost:8888", DropwizardServiceStarterTest.class).start();
        Assert.assertTrue(runMethodInvoked);
        Assert.assertTrue(args.length == 2);
        Assert.assertTrue(args[0].equals("server"));
        Assert.assertTrue(args[1].equals(tempFile));
    }

}
