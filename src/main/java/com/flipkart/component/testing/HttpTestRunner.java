package com.flipkart.component.testing;

import com.flipkart.component.testing.model.HttpDirectInput;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpTestRunner {

    /**
     * execute the http request and store the context
     *
     * @param directInput
     * @param url
     * @return
     */
    public void run(HttpDirectInput directInput, String url) throws IOException {
        HttpDirectInput httpDirectInput = directInput;
        HttpResponse httpResponse = null;
        if(directInput.isPost()) {
            httpResponse = Utils.getPostResponse(url + "/" + httpDirectInput.getPath(), httpDirectInput.getRequest(), ContentType.APPLICATION_JSON);
        }else if(directInput.isGet()){
            HttpClient httpClient = HttpClientBuilder.create().build(); //TODO:PAVAN
            HttpGet httpGet = new HttpGet(url + httpDirectInput.getPath());
            httpResponse = httpClient.execute(httpGet);
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
