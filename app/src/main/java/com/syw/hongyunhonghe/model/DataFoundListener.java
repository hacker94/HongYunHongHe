package com.syw.hongyunhonghe.model;

/**
 * Created by Hacker_PK on 14/12/6.
 */
public interface DataFoundListener<T> {
    public void onSuccess(T object);
    public void onFail(T object);
}
