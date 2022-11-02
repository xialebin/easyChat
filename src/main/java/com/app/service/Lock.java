package com.app.service;

public interface Lock {

    //获取锁
    boolean getLock() throws Exception;
    //释放锁
    void unLock() throws Exception;
}
