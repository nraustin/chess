package response;

import spark.Response;

public interface HTTPResponse {

    public abstract Object serializeResponse(Object response);
}
