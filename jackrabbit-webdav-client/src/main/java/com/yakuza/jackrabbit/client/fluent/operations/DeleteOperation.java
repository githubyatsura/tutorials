package com.yakuza.jackrabbit.client.fluent.operations;

import com.yakuza.jackrabbit.client.fluent.DavException;
import com.yakuza.jackrabbit.client.fluent.DavRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.jackrabbit.webdav.client.methods.BaseDavRequest;
import org.apache.jackrabbit.webdav.client.methods.HttpDelete;

import java.net.URI;

public class DeleteOperation extends DavRequest<DeleteOperation, Boolean> {

    private String absoluteResourceUrl;

    public DeleteOperation setAbsoluteResourceUrl(String absoluteResourceUrl) {
        this.absoluteResourceUrl = absoluteResourceUrl;
        return this;
    }

    private String getAbsoluteResourceUrl() {
        return absoluteResourceUrl;
    }

    @Override
    public Boolean perform(CloseableHttpClient client, ResponseHandler handler) throws DavException {
        execute(client, handler);
        return Boolean.TRUE;
    }

    @Override
    public Boolean perform(CloseableHttpClient client) throws DavException {
        return this.perform(client, null);
    }

    @Override
    protected BaseDavRequest createDavRequest() {
        return new HttpDelete(URI.create(getAbsoluteResourceUrl()));
    }
}
