package com.yakuza.jackrabbit.client.fluent.operations;

import com.yakuza.jackrabbit.client.fluent.DavException;
import com.yakuza.jackrabbit.client.fluent.DavRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.jackrabbit.webdav.DavMethods;
import org.apache.jackrabbit.webdav.DavServletResponse;
import org.apache.jackrabbit.webdav.client.methods.BaseDavRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class PutOperation extends DavRequest<PutOperation, String> {

    private static final Logger LOG = LoggerFactory.getLogger(PutOperation.class);

    private InputStream inputStream;

    private long contentLength;

    public PutOperation setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public PutOperation setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    private InputStream getInputStream() {
        return inputStream;
    }

    private long getContentLength() {
        return contentLength;
    }

    private String absoluteResourceUrl;

    private String getAbsoluteResourceUrl() {
        return absoluteResourceUrl;
    }

    public PutOperation setAbsoluteResourceUrl(String absoluteResourceUrl) {
        this.absoluteResourceUrl = absoluteResourceUrl;
        return this;
    }


    @Override
    public String perform(CloseableHttpClient client, ResponseHandler handler) throws DavException {
        execute(client, handler);
        return getAbsoluteResourceUrl();
    }

    @Override
    public String perform(CloseableHttpClient client) throws DavException {
        return perform(client, null);
    }

    @Override
    protected BaseDavRequest createDavRequest() throws IOException {
        LOG.debug("Resource absolute url is [{}].", getAbsoluteResourceUrl());

        URI uri = URI.create(getAbsoluteResourceUrl());

        HttpPut httpPut = new HttpPut(uri);

        httpPut.setEntity(new BufferedHttpEntity(new InputStreamEntity(getInputStream(), getContentLength())));
        return httpPut;
    }


    private static class HttpPut extends BaseDavRequest {

        public HttpPut(URI uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return DavMethods.METHOD_PUT;
        }

        @Override
        public boolean succeeded(HttpResponse response) {
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == DavServletResponse.SC_CREATED;
        }
    }
}
