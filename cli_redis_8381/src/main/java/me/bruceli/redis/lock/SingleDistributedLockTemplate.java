package me.bruceli.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

public class SingleDistributedLockTemplate implements DistributedLockTemplate {
    private RedissonClient redisson;

    public SingleDistributedLockTemplate() {
    }

    public SingleDistributedLockTemplate(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public <T> T lock(DistributedLockCallback<T> callback, boolean fairLock) {
        return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        try {
            lock.lock(leaseTime, timeUnit);
            return callback.process();
        } finally {
            if (lock != null) {
                if(lock.isLocked()){// 是否还是锁定状态
                    if(lock.isHeldByCurrentThread()){// 时候是当前执行线程的锁
                        lock.unlock();// 释放锁
                    }
                }
            }
        }
    }

    @Override
    public <T> void unlock(DistributedLockCallback<T> callback, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        if (lock != null) {
            if(lock.isLocked()){// 是否还是锁定状态
                if(lock.isHeldByCurrentThread()){// 时候是当前执行线程的锁
                    lock.unlock();// 释放锁
                }
            }
        }
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock) {
        return tryLock(callback, DEFAULT_WAIT_TIME, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        try {
            if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
                return callback.process();
            }
        } catch (InterruptedException e) {

        } finally {
            if (lock != null) {
                if(lock.isLocked()){
                    if(lock.isHeldByCurrentThread()){
                        lock.unlock();
                    }
                }
            }
        }
        return null;
    }

    private RLock getLock(String lockName, boolean fairLock) {
        RLock lock;
        if (fairLock) {
            lock = redisson.getFairLock(lockName);
        } else {
            lock = redisson.getLock(lockName);
        }
        return lock;
    }

    public void setRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }
}
