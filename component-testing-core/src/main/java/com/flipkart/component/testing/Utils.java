package com.flipkart.component.testing;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Utils {

    private static final int TEMP_DIR_ATTEMPTS = 10000;

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { // some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static byte[] toByteArray(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
    }

    public static File createFile(String fileName) {

        OutputStream outputStream = null;
        try {
            InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(fileName);
            File tmpFile = File.createTempFile("pre", "suf");
            tmpFile.deleteOnExit();
            Files.copy(inputStream, Paths.get(tmpFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
            return tmpFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.print("couldn't close the output stream");
                }
            }
        }
    }

    /**
     * From Google Guava Lib: In case when dependency is added directly : remove this and use the code from Lib
     *
     * @return
     */
    public static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        @SuppressWarnings("GoodTime") // reading system time without TimeSource
                String baseName = System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException(
                "Failed to create directory within "
                        + TEMP_DIR_ATTEMPTS
                        + " attempts (tried "
                        + baseName
                        + "0 to "
                        + baseName
                        + (TEMP_DIR_ATTEMPTS - 1)
                        + ')');
    }


    /**
     * returns the file content as string
     * @param resource
     * @return
     */
    public static String getFileString(String resource){
        try {
           return  new String(Files.readAllBytes(Paths.get(new File(resource).getAbsolutePath())));
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
