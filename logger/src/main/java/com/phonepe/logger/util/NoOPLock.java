package com.phonepe.logger.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import exception.MethodNotImplementedException;

/**
 * a NoOP {@link Lock} class created in order to simplify code for handling
 * cases where you may or may not need thread safety. Usually in such cases the
 * thread safety is specified in cinfig. So in order to avoid
 * <code>if(threadsafe)
 * {do threadsafe op }
 * else
 * { proceed with threadunsafe}
 *</code>
 * kind of blocks everywhere in code, we just create a {@link NoOPLock} where
 * thread safety may or may not be needed and use that everywhere
 * 
 * @author ASUS
 */
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
