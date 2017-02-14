package com.yakuza.jackrabbit.client.fluent.operations;


import com.yakuza.jackrabbit.client.fluent.DavException;
import com.yakuza.jackrabbit.client.fluent.DavRequest;
import org.apache.http.impl.client.CloseableHttpClient;

public interface Operation<T> {

    public abstract T perform(CloseableHttpClient client, DavRequest.ResponseHandler handler) throws DavException;

    public abstract T perform(CloseableHttpClient client) throws DavException;


}