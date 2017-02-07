package com.yakuza.integration.tests;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jackrabbit.webdav.client.methods.HttpMkcol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;

public class MyFirstIT {

    private static final Logger LOG = LoggerFactory.getLogger(MyFirstIT.class);

    private static final String USERNAME = "admin";
    private static final String PASSWORD = USERNAME;
    private static final String HOST_NAME = "localhost";
    private static final int PORT = 8080;
    private static final String URL_TEMPLATE = "http://%s:%s/repository/default";
    private static final String NEW_FOLDER_TEMPLATE = "/%s";

    private static final String URL = String.format(URL_TEMPLATE, HOST_NAME, PORT);

    private static final String FOLDER_NAME = "TEST-FOLDER";


    private UsernamePasswordCredentials getCredentials() {
        return new UsernamePasswordCredentials(USERNAME, PASSWORD);
    }

    private AuthScope getAuthscope() {
        return new AuthScope(HOST_NAME, PORT, AuthScope.ANY_REALM);
    }

    private CredentialsProvider getProvider() {
        BasicCredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(getAuthscope(), getCredentials());
        return provider;
    }


    private HttpClient httpClient;

    @BeforeTest
    public void initTest() {
        httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(getProvider())
                .build();

        LOG.info("Initialized.");
    }


    @Test()
    public void testCreateNode() throws IOException {
        String newFolderURL = URL + String.format(NEW_FOLDER_TEMPLATE, FOLDER_NAME);

        LOG.debug("New folder URL is [{}].", newFolderURL);

        HttpMkcol httpMkcol = new HttpMkcol(URI.create(newFolderURL));
        HttpResponse httpResponse = httpClient.execute(httpMkcol);

        LOG.debug("Http status is [{}].", httpResponse.getStatusLine());

        Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
    }

    @Test(dependsOnMethods = "testCreateNode")
    public void testGetNode() throws IOException {
        String folderURL = URL + String.format(NEW_FOLDER_TEMPLATE, FOLDER_NAME);

        LOG.debug("Get folder [{}] by URL.", folderURL);

        HttpGet httpGet = new HttpGet(URI.create(folderURL));
        HttpResponse httpResponse = httpClient.execute(httpGet);

        LOG.debug("Http status is [{}].", httpResponse.getStatusLine());

        Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "testGetNode")
    public void testDeleteNode() throws IOException {
        String targetFolderURL = URL + String.format(NEW_FOLDER_TEMPLATE, FOLDER_NAME);

        LOG.debug("Target folder URL is [{}].", targetFolderURL);

        HttpDelete httpDelete = new HttpDelete(URI.create(targetFolderURL));
        HttpResponse httpResponse = httpClient.execute(httpDelete);

        LOG.debug("Http status is [{}].", httpResponse.getStatusLine());

        Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
    }

    @AfterTest
    public void finishTest() {
        LOG.info("Destroyed.");
    }
}
