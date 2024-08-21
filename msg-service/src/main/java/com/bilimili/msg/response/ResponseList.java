package com.bilimili.msg.response;

/**
 * Description:
 *
 * @author Yury
 */
public class ResponseList <T> {

    private T data;
    private Long count;
    private boolean success;
    private String message;

    public static <K> ResponseList<K> newSuccess(K data, Long count) {
        ResponseList<K> responseList = new ResponseList<>();
        responseList.setData(data);
        responseList.setCount(count);
        responseList.setSuccess(true);
        return responseList;
    }


    public static <K> ResponseList<K> newFail (String message) {
        ResponseList<K> responseList = new ResponseList<>();
        responseList.setMessage(message);
        responseList.setSuccess(false);
        return responseList;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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
