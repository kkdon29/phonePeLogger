package com.phonepe.logger.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import exception.MethodNotImplementedException;

public class NoOPLock implements Lock {

    @Override
    public void lock() {
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        throw new MethodNotImplementedException("NoOPLock.newCondition");
    }

    @Override
    public boolean tryLock() {
        return true;
    }

    @Override
    public boolean tryLock(long arg0, TimeUnit arg1)
                    throws InterruptedException {
        return true;
    }

    @Override
    public void unlock() {
    }

}
