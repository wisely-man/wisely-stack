package com.wisely.starter.core.lock;

public interface LockManager {

    /**
     * 名称
     *
     * @return 锁类型
     */
    String name();

    /**
     * 尝试加锁
     *
     * @param key        锁key
     * @param ttlSeconds 锁过期时间，单位秒
     * @return 是否加锁成功
     */
    boolean tryLock(String key, int ttlSeconds);

    /**
     * 解锁
     *
     * @param key 锁key
     * @return
     */
    boolean unlock(String key);

    /**
     * 续期
     *
     * @param key        锁key
     * @param ttlSeconds 锁过期时间，单位秒
     */
    boolean renew(String key, int ttlSeconds);
}
