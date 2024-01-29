package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    /**
     * 响应数据
     */
    private T data;
    /**
     * 响应码
     * 响应码为0代表接口正常，非0则代表接口请求异常
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应时间戳
     */
    private long times;

    private Result() {
    }

    public static Result<Void> succeed() {
        return succeedWith(null);
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model);
    }

    public static <T> Result<T> succeedWith(T responseData) {
        if (null == responseData) {
            responseData = (T) new HashMap<>();
        }
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(responseData);
        result.setTimes(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> failed(int code, String msg) {
        return failedWith(code, msg);
    }

    public static <T> Result<T> failedWith(int errorCode, String errorMsg) {
        Result<T> result = new Result<>();
        result.setCode(errorCode);
        result.setMsg(errorMsg);
        result.setTimes(System.currentTimeMillis());
        return result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }
}
