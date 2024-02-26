package request;

import spark.Request;

public interface HTTPRequest {
    public Object deserializeRequest(Request request);
}
