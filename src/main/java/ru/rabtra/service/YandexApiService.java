package ru.rabtra.service;


import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rabtra.dto.TreeResponse;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
public class YandexApiService {

    @Value("${yandex.disk.api.url}")
    private String BASE_URL;

    @Value("${yandex.disk.api.key}")
    private String TOKEN;

    private final CloseableHttpClient client;
    private final Gson gson;

    @Autowired
    public YandexApiService(CloseableHttpClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }


    public String getDiskData() throws IOException {
        HttpGet httpGet = new HttpGet(BASE_URL);
        httpGet.setHeader("Authorization", TOKEN);
        httpGet.setHeader("Accept", "application/json");

        HttpResponse jsonResponse = client.execute(httpGet);

        String responseBody = EntityUtils.toString(jsonResponse.getEntity());

        JSONObject response = new JSONObject(responseBody);
        String name = response.getJSONObject("user").getString("display_name");

        return "Name: " + name;
    }

    public TreeResponse getTree(
            Integer limit,
            String mediaType,
            Integer offset
    ) throws URISyntaxException, IOException {

        HttpGet httpGet = new HttpGet(BASE_URL+"resources/files");
        httpGet.setHeader("Authorization", TOKEN);
        httpGet.setHeader("Accept", "application/json");
        URIBuilder uri = new URIBuilder(httpGet.getURI());

        if (limit != null && limit > 0) {
            uri.addParameter("limit", String.valueOf(limit));
        }
        if (mediaType != null) {
            uri.addParameter("media_type", mediaType);
        }
        if (offset != null && offset > 0) {
            uri.addParameter("offset", String.valueOf(offset));
        }
        httpGet.setURI(uri.build());

        try (CloseableHttpResponse response = client.execute(httpGet)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return gson.fromJson(jsonResponse, TreeResponse.class);
            } else {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }
        }

    }

    public String uploadFile(String pathToUpload, String pathToFile, boolean overwrite)
            throws URISyntaxException, IOException {

        String urlToUpload;

        HttpGet httpGet = new HttpGet(BASE_URL+"resources/upload");
        httpGet.setHeader("Authorization", TOKEN);
        httpGet.setHeader("Accept", "application/json");
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("path", pathToUpload)
                .addParameter("overwrite", String.valueOf(overwrite))
                .build();
        httpGet.setURI(uri);

        try (CloseableHttpResponse response = client.execute(httpGet)) {

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }

            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);
            urlToUpload = jsonObject.getString("href");
        }

        System.out.println(urlToUpload);

        HttpPut httpPut = new HttpPut(urlToUpload);

        File file = new File(pathToFile);
        FileEntity fileEntity = new FileEntity(file);

        httpPut.setEntity(fileEntity);

        try (CloseableHttpResponse response = client.execute(httpPut)) {

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 201 || statusCode == 200) return "File has been uploaded";
            else throw new IOException("API error: " + statusCode);
        }
    }

    public String downloadFile(String path) throws URISyntaxException, IOException {

        HttpGet httpGet = new HttpGet(BASE_URL+"resources/download");
        httpGet.setHeader("Authorization", TOKEN);
        httpGet.setHeader("Accept", "application/json");
        URI uri = new URIBuilder(httpGet.getURI())
                .addParameter("path", path)
                .addParameter("fields", "name")
                .build();
        httpGet.setURI(uri);

        try (CloseableHttpResponse response = client.execute(httpGet)) {

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }

            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String linkToDownload = jsonObject.getString("href");

            return "To download follow the link: " + linkToDownload;
        }

    }

    public String copyTo(String pathFrom, String pathTo, boolean overwrite) throws URISyntaxException, IOException {

        HttpPost httpPost = new HttpPost(BASE_URL+"resources/copy");
        setHeaders(httpPost);
        URI uri = new URIBuilder(httpPost.getURI())
                .addParameter("from", pathFrom)
                .addParameter("path", pathTo)
                .addParameter("overwrite", String.valueOf(overwrite))
                .build();
        httpPost.setURI(uri);

        try (CloseableHttpResponse response = client.execute(httpPost)) {

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201 || statusCode == 202) {
                return "done.";
            } else {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }

        }
    }

    public String moveTo(String pathFrom, String pathTo, boolean overwrite) throws URISyntaxException, IOException {
        HttpPost httpPost = new HttpPost(BASE_URL+"resources/move");
        setHeaders(httpPost);
        URI uri = new URIBuilder(httpPost.getURI())
                .addParameter("from", pathFrom)
                .addParameter("path", pathTo)
                .addParameter("overwrite", String.valueOf(overwrite))
                .build();
        httpPost.setURI(uri);


        try (CloseableHttpResponse response = client.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201 || statusCode == 202) {
                return "done.";
            } else {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }
        }
    }

    public String delete(String path) throws URISyntaxException, IOException {

        HttpDelete httpDelete = new HttpDelete(BASE_URL+"resources");
        setHeaders(httpDelete);
        URI uri = new URIBuilder(httpDelete.getURI())
                .addParameter("path", path)
                .build();
        httpDelete.setURI(uri);
        try (CloseableHttpResponse response = client.execute(httpDelete)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 204 || statusCode == 202) {
                return "deleted";
            } else {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }
        }
    }

    public String makeDir(String path) throws URISyntaxException, IOException {
        HttpPut httpPut = new HttpPut(BASE_URL+"resources");
        setHeaders(httpPut);
        URI uri = new URIBuilder(httpPut.getURI())
                .addParameter("path", path)
                .build();
        httpPut.setURI(uri);

        try (CloseableHttpResponse response = client.execute(httpPut)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
                return "done.";
            } else {
                System.out.println(EntityUtils.toString(response.getEntity()));
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }
        }
    }

    public String eraseTrash(String path) throws URISyntaxException, IOException {
        HttpDelete httpDelete = new HttpDelete(BASE_URL+"trash/resources");
        setHeaders(httpDelete);

        if (path != null && !path.isEmpty()) {
            URI uri = new URIBuilder(httpDelete.getURI())
                    .addParameter("path", path)
                    .build();
            httpDelete.setURI(uri);
        }

        try (CloseableHttpResponse response = client.execute(httpDelete)) {
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 204 || statusCode == 202) {
                return "done.";
            }
            else {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }
        }
    }

    public void restoreResourceFromTrash(String path, String name, boolean overwrite)
            throws URISyntaxException, IOException {
        HttpPut httpPut = new HttpPut(BASE_URL+"trash/resources/restore");
        setHeaders(httpPut);
        URI uri = new URIBuilder(httpPut.getURI())
                .addParameter("path", path)
                .addParameter("name", name)
                .addParameter("overwrite", String.valueOf(overwrite))
                .build();
        httpPut.setURI(uri);

        try (CloseableHttpResponse response = client.execute(httpPut)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201 || statusCode == 202) {
                System.out.println("done.");
            } else {
                throw new IOException("API error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
            }
        }
    }

    private void setHeaders(HttpRequest http) {
        http.setHeader("Authorization", TOKEN);
        http.setHeader("Accept", "application/json");
    }


}
