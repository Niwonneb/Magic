package com.ngw.toowyn.apiservice.errorhandling;


import java.util.HashMap;
import java.util.Map;

public class HttpError extends Throwable {

    public enum HttpErrorType {
        badRequest(400),
        unauthorized(401),
        forbidden(403),
        notFound(404),
        requestTimeout(408),
        conflict(409),
        preconditionFailed(412),
        unsupportedMediaType(415),
        unknown(-1);

        private final int code;
        HttpErrorType(int code) {
            this.code = code;
        }

        private static Map<Integer, HttpErrorType> map = new HashMap<>();

        static {
            for (HttpErrorType httpErrorCode: HttpErrorType.values()) {
                map.put(httpErrorCode.code, httpErrorCode);
            }
        }

        public static HttpErrorType get(int code) {
            if (map.containsKey(code)) {
                return map.get(code);
            }
            return unknown;
        }
    }

    private HttpErrorType httpErrorType;

    public HttpError(int httpCode) {
        super("HttpError " + String.valueOf(httpCode));
        httpErrorType = HttpErrorType.get(httpCode);
    }

    public HttpErrorType getErrorCode() {
        return httpErrorType;
    }
}
