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

import java.util.List;

/**
 * Smoke tests for the draft of fluent jackrabbit API.
 */
public class WebDavCollectionIT extends BaseWebDavTestIT {

    private static final Logger LOG = LoggerFactory.getLogger(WebDavCollectionIT.class);

    private static final String NEW_RESOURCE_TEMPLATE = "/%s";

    private static final String ROOT_PATH = String.format(URL_TEMPLATE, HOST_NAME, PORT);

    private static final String RESOURCE_NAME = "TEST-FOLDER";

    @Test
    public void testCreateNode() throws DavException {

        String result = DavRequest
                .Mkcol()
                .setAbsoluteResourceUrl(ROOT_PATH + String.format(NEW_RESOURCE_TEMPLATE, RESOURCE_NAME))
                .build()
                .perform(getHttpClient(),
                        response -> Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED,
                                "Expected http status code = 201"));

        String resourceAbsolutePath = ROOT_PATH + String.format(NEW_RESOURCE_TEMPLATE, RESOURCE_NAME);

        Assert.assertEquals(result, resourceAbsolutePath,
                "Expected absolute path which equal [" + resourceAbsolutePath + "]");
    }

    @Test(dependsOnMethods = "testCreateNode")
    public void testFindResourceContentByName() throws DavException {
        List<String> paths = DavRequest
                .Propfind()
                .setResourceName(ROOT_PATH)
                .setContentType(PropfindOperation.ContentType.ANY)
                .setDepth(DavConstants.DEPTH_INFINITY).build()
                .perform(getHttpClient(),
                        response -> Assert.assertEquals(
                                response.getStatusLine().getStatusCode(),
                                HttpStatus.SC_MULTI_STATUS,
                                "Expected http status code = 207."));
        Assert.assertFalse(paths.isEmpty(),
                "List is empty.");
    }

    @Test(dependsOnMethods = "testFindResourceContentByName")
    public void testDeleteResourceByName() throws DavException {
        String targetResource = ROOT_PATH + String.format(NEW_RESOURCE_TEMPLATE, RESOURCE_NAME);

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
