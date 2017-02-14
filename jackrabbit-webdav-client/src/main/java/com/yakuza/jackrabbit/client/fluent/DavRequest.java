package com.yakuza.jackrabbit.client.fluent;

import com.yakuza.jackrabbit.client.fluent.operations.DeleteOperation;
import com.yakuza.jackrabbit.client.fluent.operations.MkcolOperation;
import com.yakuza.jackrabbit.client.fluent.operations.Operation;
import com.yakuza.jackrabbit.client.fluent.operations.PropfindOperation;
import com.yakuza.jackrabbit.client.fluent.operations.PutOperation;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.jackrabbit.webdav.client.methods.BaseDavRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public abstract class DavRequest<M extends DavRequest, T> implements Operation<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DavRequest.class);

    public static final String NEW_RESOURCE_TEMPLATE = "/%s";

    private BaseDavRequest request;

    public Operation<T> build() throws DavException {
        try {
            setRequest(createDavRequest());
        } catch (IOException e) {
            LOG.error("Build was failed.", e);
            throw new DavException("Build was failed.");
        }
        return (Operation<T>) this;
    }

    protected abstract BaseDavRequest createDavRequest() throws IOException;

    protected BaseDavRequest getRequest() {
        return request;
    }

    protected void setRequest(BaseDavRequest request) {
        this.request = request;
    }

    public static MkcolOperation Mkcol() {
        return new MkcolOperation();
    }

    public static DeleteOperation Delete() {
        return new DeleteOperation();
    }

    public static PropfindOperation Propfind() {
        return new PropfindOperation();
    }

    public static PutOperation Put() {
        return new PutOperation();
    }

    protected void handleResponse(CloseableHttpResponse response) throws org.apache.jackrabbit.webdav.DavException {
        //Nothing.
    }

    protected void execute(CloseableHttpClient client, ResponseHandler responseHandler) throws DavException {
        try (CloseableHttpResponse response = client.execute(this.getRequest())) {
            if (!this.getRequest().succeeded(response)) {
                DavException exception = new DavException("Operation was failed see log files.");
                LOG.error("Operation was failed due to error {} take place. The reason is {}.",
                        response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
                throw exception;
            } else {
                //Standard response handling.
                handleResponse(response);

                //Custom handling of response.
                if (responseHandler != null) {
                    responseHandler.handle(response);
                }
            }
            if (response != null && response.getEntity() != null) {
                EntityUtils.consume(response.getEntity());
            }
        } catch (ClientProtocolException e) {
            LOG.error("Execution failed.", e);
            throw new DavException("Execution was failed.");
        } catch (IOException e) {
            LOG.error("Execution failed.", e);
            throw new DavException();
        } catch (org.apache.jackrabbit.webdav.DavException e) {
            LOG.error("Execution failed.", e);
            throw new DavException("Execution was failed.");
        }
    }

    protected void execute(CloseableHttpClient client) throws DavException {
        execute(client, null);
    }

    public interface ResponseHandler {
        void handle(CloseableHttpResponse response);
    }
}

