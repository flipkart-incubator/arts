package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.http.HttpDirectInput;
import com.flipkart.component.testing.model.http.HttpObservation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


class HttpResponseConsumer implements ObservationCollector<HttpObservation> {

    private ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * prepares the httpObservation from the testContext
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public HttpObservation actualObservations(HttpObservation expectedObservation) {
        try {
            return new HttpTestRunner().run((HttpDirectInput) expectedObservation.getDirectInput(), expectedObservation.getUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<HttpObservation> getObservationClass() {
        return HttpObservation.class;
    }


    private class HttpTestRunner {

        private CloseableHttpClient httpClient;

        /**
         * execute the http request and store the context
         *
         * @param directInput
         * @param url
         * @return
         */
        public HttpObservation run(HttpDirectInput directInput, String url) throws IOException {
            HttpResponse httpResponse = null;
            httpClient = HttpClientBuilder.create().build();
            if (directInput.isPost()) {
                httpResponse = getPostResponse(url, directInput);
            } else if (directInput.isGet()) {
                HttpGet httpGet = new HttpGet(url + directInput.getPath());
                Optional.ofNullable(directInput.getHeaders()).ifPresent(x -> x.forEach(httpGet::addHeader));
                httpResponse = httpClient.execute(httpGet);
            } else if (directInput.isPut()) {
                HttpPut httpPut = new HttpPut(url + directInput.getPath());
                Optional.ofNullable(directInput.getHeaders()).ifPresent(x -> x.forEach(httpPut::addHeader));
                httpPut.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(directInput.getRequest()), ContentType.APPLICATION_JSON));
                httpResponse = httpClient.execute(httpPut);
            } else {
                HttpDelete httpDelete = new HttpDelete(url + directInput.getPath());
                Optional.ofNullable(directInput.getHeaders()).ifPresent(x -> x.forEach(httpDelete::addHeader));
                httpResponse = httpClient.execute(httpDelete);
            }


            String response = null;
            if (httpResponse.getEntity() != null) {
                response = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            }

            return new HttpObservation(httpResponse.getStatusLine().getStatusCode(), response, httpResponse.getAllHeaders());
        }


        private HttpResponse getPostResponse(String url, HttpDirectInput directInput) throws IOException {

            if(directInput.isMultiPart()){
                return getResponse(url, directInput);
            }
            HttpPost httpPost = createHttpPost(url, directInput);
            httpPost.setConfig(RequestConfig.copy(RequestConfig.DEFAULT).build());
            httpPost.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(directInput.getRequest()), ContentType.APPLICATION_JSON));
            return httpClient.execute(httpPost);
        }

        private HttpResponse getResponse(String url, HttpDirectInput directInput) throws IOException {

            HttpPost httpPost = createHttpPost(url, directInput);
            File file = new File(directInput.getMultiPartInput().getFilePath());
            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart(directInput.getMultiPartInput().getFileKeyInMultipart(), fileBody);
            directInput.getMultiPartInput().getOthers().forEach((k,v) -> builder.addPart(k, new StringBody(v, ContentType.MULTIPART_FORM_DATA)));
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            return httpClient.execute(httpPost);
        }

        private HttpPost createHttpPost(String url, HttpDirectInput directInput){
            url = url + "/" + directInput.getPath();
            HttpPost post = new HttpPost(url);
            Optional.ofNullable(directInput.getHeaders()).orElse(new HashMap<>()).forEach(post::addHeader);
            return post;
        }

    }

}



