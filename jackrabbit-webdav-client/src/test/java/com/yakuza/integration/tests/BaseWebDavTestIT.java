package com.yakuza.integration.tests;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

public abstract class BaseWebDavTestIT {

    private static final Logger LOG = LoggerFactory.getLogger(WebDavCollectionIT.class);

    public static final String URL_TEMPLATE = "http://%s:%s/repository/default";

    private static final int MAX_TOTAL = 10;

    private static final String USERNAME = "admin";
    private static final String PASSWORD = USERNAME;
    public static final String HOST_NAME = "localhost";
    public static final int PORT = 8080;

    private final CloseableHttpClient httpClient = HttpClients
            .custom()
            .setConnectionManager(getConnManager())
            .setDefaultCredentialsProvider(getProvider())
            .build();

    protected CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    private PoolingHttpClientConnectionManager getConnManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(MAX_TOTAL);
        return manager;
    }

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

    @BeforeTest
    public void startTest() {
        LOG.info("Initialized.");
    }

    @AfterTest
    public void endTest() throws IOException {
        getHttpClient().close();
        LOG.info("Used resources destroyed.");
    }
}
