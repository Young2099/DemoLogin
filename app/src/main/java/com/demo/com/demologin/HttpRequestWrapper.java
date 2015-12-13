package com.demo.com.demologin;

/**
 * 把对象转换成json
 * Created by 杨放 on 2015/12/12.
 */
public abstract class HttpRequestWrapper<T> {

    private String json;

    public String getjson() {
        return convertToJson();
    }

    protected abstract String convertToJson();

}
