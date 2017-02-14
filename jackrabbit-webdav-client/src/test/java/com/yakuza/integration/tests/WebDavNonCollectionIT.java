package com.yakuza.integration.tests;

import com.yakuza.jackrabbit.client.fluent.DavException;
import com.yakuza.jackrabbit.client.fluent.DavRequest;
import com.yakuza.jackrabbit.client.fluent.operations.PropfindOperation;
import org.apache.http.HttpStatus;
import org.apache.jackrabbit.webdav.DavConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Smoke tests for the draft of fluent jackrabbit API.
 */
public class WebDavNonCollectionIT extends BaseWebDavTestIT {

    private static final Logger LOG = LoggerFactory.getLogger(WebDavNonCollectionIT.class);

    private static final String NEW_RESOURCE_TEMPLATE = "/%s";

    private static final String ROOT_PATH = String.format(URL_TEMPLATE, HOST_NAME, PORT);

    private static final String NON_COL_RESOURCE_NAME = "testFile";
    public static final String SAMPLE_TEXT_FILE = ".\\files\\textFile.txt";


    private InputStream readFile(String pathToFile) throws FileNotFoundException {
        File file = new File(pathToFile);
        InputStream inputStream = new FileInputStream(file);
        return inputStream;
    }

    @Test
    public void testCreateNonCollectionNode() throws DavException, IOException {
        String targetNode = ROOT_PATH + String.format(NEW_RESOURCE_TEMPLATE, NON_COL_RESOURCE_NAME);

        String result = null;
        try (InputStream inputStream = readFile(SAMPLE_TEXT_FILE)) {

            result = DavRequest
                    .Put()
                    .setAbsoluteResourceUrl(targetNode)
                    .setInputStream(inputStream)
                    .setContentLength(inputStream.available())
                    .build()
                    .perform(getHttpClient(),
                            response -> Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,
                                    "Expected http status code = 201"));
        }

        String resourceAbsolutePath = ROOT_PATH
                + String.format(NEW_RESOURCE_TEMPLATE, NON_COL_RESOURCE_NAME);

        Assert.assertEquals(result, resourceAbsolutePath,
                "Expected absolute path which equal [" + resourceAbsolutePath + "]");
    }


    @Test(dependsOnMethods = "testCreateNonCollectionNode")
    public void testFindResourceByName() throws DavException {
        List<String> paths = DavRequest
                .Propfind()
                .setResourceName(ROOT_PATH)
                .setContentType(PropfindOperation.ContentType.RESOURCE)
                .setDepth(DavConstants.DEPTH_INFINITY).build()
                .perform(getHttpClient(),
                        response -> Assert.assertEquals(
                                response.getStatusLine().getStatusCode(),
                                HttpStatus.SC_MULTI_STATUS,
                                "Expected http status code = 207."));
        Assert.assertFalse(paths.isEmpty(), "List is empty.");
    }


    @Test(dependsOnMethods = "testFindResourceByName")
    public void testDeleteResourceByName() throws DavException {
        String targetResource = ROOT_PATH + String.format(NEW_RESOURCE_TEMPLATE, NON_COL_RESOURCE_NAME);

        Boolean result = DavRequest
                .Delete()
                .setAbsoluteResourceUrl(targetResource)
                .build()
                .perform(getHttpClient(),
                        response -> Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT,
                                "Expected http status code = 204"));

        Assert.assertEquals(result, Boolean.TRUE, "Expected value = TRUE.");

    }
}
