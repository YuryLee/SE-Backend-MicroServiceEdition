package com.bilimili.video.response;

/**
 * Description:
 *
 * @author Yury
 */
public class Response <T> {

    private T data;
    private boolean success;
    private String message;

    public static <K> Response<K> newSuccess(K data) {
        Response<K> response = new Response<>();
        response.setData(data);
        response.setSuccess(true);
        return response;
    }

    public static <K> Response<K> newFail (String message) {
        Response<K> response = new Response<>();
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
