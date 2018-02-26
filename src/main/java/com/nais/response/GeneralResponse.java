package com.nais.response;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GeneralResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1501625965585346719L;

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int ERROR = 1;

    /**
     * 告警
     */
    public static final int WARN = 2;

    private int code;
    private String comment;
    private String version;

    private T data;


    public GeneralResponse(int code, String comment, String version, T t) {
        super();
        this.code = code;
        this.comment = comment;
        this.version = version;

        this.data = t;

    }

    public GeneralResponse() {
        super();
    }

    public GeneralResponse(int code, String comment, T t) {
        this(code, comment, "1.0.0", t);
    }

    public static <T> GeneralResponse<T> success(String comment, T t) {
        return new GeneralResponse<T>(SUCCESS, comment, t);
    }

    public static <T> GeneralResponse<T> success(T t) {
        return new GeneralResponse<T>(SUCCESS, StringUtils.EMPTY, t);
    }

    public static <T> GeneralResponse<T> failure(String comment, T t) {
        return new GeneralResponse<T>(ERROR, comment, t);
    }

    public static <T> GeneralResponse<T> failure(String comment) {
        return new GeneralResponse<T>(ERROR, comment, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("comment", comment)
                .append("version", version)
                .append("data", data)
                .toString();
    }
}
