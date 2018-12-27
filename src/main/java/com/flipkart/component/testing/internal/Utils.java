package com.flipkart.component.testing.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.http.HttpDirectInput;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dbunit.dataset.IDataSet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Utils {

    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static HttpClient httpClient;

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

    /**
     * returns the dataset of dbunit from the map
     *
     * @param map
     * @return
     */
    public static IDataSet getDataSet(Map<String, List<Map<String, Object>>> map) {
        return new JSONDataSet(map);
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


    public static HttpResponse getPostResponse(String url, Object body, ContentType contentType) throws IOException {
        return getPostResponse(url, body, new HashMap<>(), contentType);
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

    public static HttpResponse getPostResponse(String url, Object body, Map<String, String> headers, ContentType contentType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        httpClient = HttpClientBuilder.create().build(); //TODO:PAVAN
        HttpPost httpPost = new HttpPost(url);
        Optional.ofNullable(headers).orElse(new HashMap<>()).forEach(httpPost::addHeader);
        httpPost.setConfig(RequestConfig.copy(RequestConfig.DEFAULT).build());
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(body), contentType));
        return httpClient.execute(httpPost);
    }
}
