package io.github.wisely.starter.core.lock;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import io.github.wisely.starter.core.helper.ValidHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;

import java.util.concurrent.TimeUnit;

/**
 * 非可重入锁
 */
@Slf4j
public class MemoryLockManager implements LockManager {

    public MemoryLockManager() {
        this(10_000, 5 * 60);
    }

    public MemoryLockManager(int maxSize, long seconds) {
        maxSize = maxSize <= 0 ? 10_000 : maxSize;
        seconds = seconds <= 0 ? 5 * 60 : seconds;

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(seconds, TimeUnit.SECONDS)
                .build();
    }

    private final Cache<@NonNull String, @NonNull LockEntry> cache;

    @Override
    public String name() {
        return "MemoryLock";
    }

    @Override
    public boolean tryLock(String key, int ttlSeconds, String owner) {
        long now = System.currentTimeMillis();
        long expire = now + ttlSeconds * 1000L;
        try {
            LockEntry entry = cache.asMap().compute(key, (k, v) -> {
                if (v == null || now > v.expire) {
                    return new LockEntry(owner, expire);
                }
                return v;
            });
            // 只有当前线程持有锁才算加锁成功
            return Strings.CS.equals(entry.owner, owner);
        } catch (Exception e) {
            log.error("MemoryLockManager failed:", e);
        }
        return false;
    }

    @Override
    public boolean unlock(String key, String owner) {
        LockEntry entry = cache.getIfPresent(key);
        if (entry != null && Strings.CS.equals(entry.owner(), owner)) {
            cache.invalidate(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean renew(String key, int ttlSeconds, String owner) {
        return cache.asMap()
                .computeIfPresent(key, (k, old) ->
                        ValidHelper.isEquals(old.owner(), owner) ? old.renew(ttlSeconds) : old) != null;
    }


    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    public CacheStats stats() {
        return cache.stats();   // 暴露统计
    }


    record LockEntry(String owner, long expire) {
        LockEntry renew(int ttlSeconds) {
            return new LockEntry(owner, System.currentTimeMillis() + ttlSeconds * 1000L);
        }
    }
}
