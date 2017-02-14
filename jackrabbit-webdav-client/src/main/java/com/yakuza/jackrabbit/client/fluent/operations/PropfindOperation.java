package com.yakuza.jackrabbit.client.fluent.operations;

import com.yakuza.jackrabbit.client.fluent.DavException;
import com.yakuza.jackrabbit.client.fluent.DavRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.BaseDavRequest;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropfindOperation extends DavRequest<PropfindOperation, List<String>> implements Operation<List<String>> {

    public enum ContentType {
        RESOURCE, COLLECTION, ANY
    }

    private ContentType contentType = ContentType.ANY;

    private int depth = DavConstants.DEPTH_0;

    private String resourceName;

    public PropfindOperation setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public PropfindOperation setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public PropfindOperation setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    private ContentType getContentType() {
        return contentType;
    }

    private int getDepth() {
        return depth;
    }

    private String getResourceName() {
        return resourceName;
    }

    private List<String> paths = new ArrayList<>();

    private List<String> getPaths() {
        return paths;
    }

    @Override
    protected void handleResponse(CloseableHttpResponse response) throws org.apache.jackrabbit.webdav.DavException {
        MultiStatusResponse[] responses = getRequest().getResponseBodyAsMultiStatus(response).getResponses();

        final String path = getResourceName();
        for (MultiStatusResponse statusResponse : responses) {
            String href = statusResponse.getHref();
            if (href != null && !(href.endsWith(path) || href.endsWith(path + "/"))) {
                //Check whether response contains file.
                if (contentType == ContentType.ANY) {
                    paths.add(href);
                } else {
                    int statusCode = statusResponse.getStatus()[0].getStatusCode();
                    String value = (String) statusResponse.getProperties(statusCode).get(DavPropertyName.ISCOLLECTION).getValue();
                    boolean isDirectory = "1".equals(value);

                    if (contentType == ContentType.COLLECTION && isDirectory) {
                        paths.add(href);
                    } else if (contentType == ContentType.RESOURCE && !isDirectory) {
                        paths.add(href);
                    }
                }
            }
        }
    }

    @Override
    public List<String> perform(CloseableHttpClient client, DavRequest.ResponseHandler handler) throws DavException {
        execute(client, handler);
        return getPaths();
    }

    @Override
    public List<String> perform(CloseableHttpClient client) throws DavException {
        return perform(client, null);
    }


    @Override
    protected BaseDavRequest createDavRequest() throws IOException {
        final DavPropertyNameSet davPropertyNames = new DavPropertyNameSet();
        if (contentType != ContentType.ANY) {
            davPropertyNames.add(DavPropertyName.ISCOLLECTION);
        }

        return new HttpPropfind(getResourceName(), DavConstants.PROPFIND_BY_PROPERTY, davPropertyNames, getDepth());
    }
}
