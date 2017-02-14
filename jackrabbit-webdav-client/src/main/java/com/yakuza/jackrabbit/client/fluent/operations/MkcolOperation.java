package com.yakuza.jackrabbit.client.fluent.operations;

import com.yakuza.jackrabbit.client.fluent.DavException;
import com.yakuza.jackrabbit.client.fluent.DavRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.jackrabbit.webdav.client.methods.BaseDavRequest;
import org.apache.jackrabbit.webdav.client.methods.HttpMkcol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class MkcolOperation extends DavRequest<MkcolOperation, String> {

    private static final Logger LOG = LoggerFactory.getLogger(MkcolOperation.class);

    private String absoluteResourceUrl;

    private String getAbsoluteResourceUrl() {
        return absoluteResourceUrl;
    }

    public MkcolOperation setAbsoluteResourceUrl(String absoluteResourceUrl) {
        this.absoluteResourceUrl = absoluteResourceUrl;
        return this;
    }

    @Override
    protected BaseDavRequest createDavRequest() {
        LOG.debug("Absolute resource url is [{}].", getAbsoluteResourceUrl());

        URI uri = URI.create(getAbsoluteResourceUrl());
        return new HttpMkcol(uri);
    }

    @Override
    public String perform(CloseableHttpClient client, ResponseHandler handler) throws DavException {
        execute(client, handler);
        return getAbsoluteResourceUrl();
    }

    @Override
    public String perform(CloseableHttpClient client) throws DavException {
        return this.perform(client, null);
    }
}
