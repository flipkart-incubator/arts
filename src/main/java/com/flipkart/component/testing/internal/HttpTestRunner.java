package com.flipkart.component.testing.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.http.HttpDirectInput;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;

public class HttpTestRunner {

    /**
     * execute the http request and store the context
     *
     * @param directInput
     * @param url
     * @return
     */
    public void run(HttpDirectInput directInput, String url) throws IOException {
        HttpResponse httpResponse = null;
        HttpClient httpClient = HttpClientBuilder.create().build(); //TODO:PAVAN

        if (directInput.isPost()) {
            httpResponse = Utils.getPostResponse(url + "/" + directInput.getPath(), directInput.getRequest(), directInput.getHeaders(), ContentType.APPLICATION_JSON);
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

        Map<String, Object> httpResponseMap = new HashMap<>();
        httpResponseMap.put("responseHeaders", httpResponse.getAllHeaders());
        httpResponseMap.put("responseStatus", httpResponse.getStatusLine().getStatusCode());

        if (httpResponse.getEntity() != null) {
            httpResponseMap.put("response", EntityUtils.toString(httpResponse.getEntity()));
        }

        TestContext.httpResponseMap = httpResponseMap;
    }

}
