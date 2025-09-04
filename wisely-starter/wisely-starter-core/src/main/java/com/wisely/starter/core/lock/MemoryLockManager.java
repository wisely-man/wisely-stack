package com.wisely.starter.core.lock;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MemoryLockManager implements LockManager {

    public MemoryLockManager() {
        this(10_000, 5 * 60);
    }

    public MemoryLockManager(int maxSize, long senconds) {
        maxSize = maxSize <= 0 ? 10_000 : maxSize;
        senconds = senconds <= 0 ? 5 * 60 : senconds;

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(senconds, TimeUnit.SECONDS)
                .build();
    }

    private final Cache<@NonNull String, @NonNull LockEntry> cache;

    @Override
    public String name() {
        return "MemoryLock";
    }

    @Override
    public boolean tryLock(String key, int ttlSeconds) {
        long now = System.currentTimeMillis();
        long expire = now + ttlSeconds * 1000L;
        try {
            cache.asMap().compute(key, (k, v) -> {
                if (v == null || now > v.expire) {
                    return new LockEntry(Thread.currentThread(), expire);
                }
                return v;
            });
            return true;
        } catch (Exception e) {
            log.error("MemoryLockManager failed:", e);
            return false;
        }
    }

    @Override
    public boolean unlock(String key) {
        cache.invalidate(key);
        return false;
    }

    @Override
    public boolean renew(String key, int ttlSeconds) {
        try {
            // 原子地“拿旧值→续签→写回”
            cache.asMap().computeIfPresent(key, (k, oldEntry) -> {
                long now = System.currentTimeMillis();
                if (now > oldEntry.expire) return null;   // 已过期，直接删
                return oldEntry.renew(ttlSeconds);        // 续签
            });
            return cache.getIfPresent(key) != null;       // 确认还在
        } catch (Exception e) {
            log.warn("MemoryLockManager renew failed, key={}", key, e);
            return false;
        }
    }


    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    public CacheStats stats() {
        return cache.stats();   // 暴露统计
    }


    record LockEntry(Thread owner, long expire) {
        /**
         * 续签：返回新的 expire 时间
         */
        LockEntry renew(int ttlSeconds) {
            return new LockEntry(owner, System.currentTimeMillis() + ttlSeconds * 1000L);
        }
    }
}
