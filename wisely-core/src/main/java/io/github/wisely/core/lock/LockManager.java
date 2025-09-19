package io.github.wisely.core.lock;

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
     * @param owner      锁的持有者
     * @return 是否加锁成功
     */
    boolean tryLock(String key, int ttlSeconds, String owner);

    /**
     * 解锁
     *
     * @param key   锁key
     * @param owner 锁的持有者
     * @return 是否解锁成功
     */
    boolean unlock(String key, String owner);

    /**
     * 续期
     *
     * @param key        锁key
     * @param ttlSeconds 锁过期时间，单位秒
     * @param owner      锁持有者
     */
    boolean renew(String key, int ttlSeconds, String owner);
}
