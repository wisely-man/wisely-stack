package com.wisely.starter.core.helper;

import com.google.common.collect.Lists;
import com.wisely.starter.core.exception.SystemException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomHelper
 * 随机数工具类
 */
public class RandomHelper {


    /**
     * 随机获取list中的一个元素
     *
     * @param collection 内容
     * @return 随机内容
     */
    public static <T> T get(Collection<T> collection) {

        if (ValidHelper.isEmpty(collection)) {
            return null;
        }

        int idx = getInt(collection.size());
        if (collection instanceof List) {
            return ((List<T>) collection).get(idx);
        }

        int i = 0;
        for (T t : collection) {
            if (i++ == idx) return t;
        }

        throw SystemException.of();
    }

    /**
     * 根据list里的内容随机获取 number个
     *
     * @param contents   内容
     * @param returnSize 返回数量
     * @return 随机内容
     */
    public static <T> List<T> get(List<T> contents, int returnSize) {
        if (returnSize < 0 || returnSize >= contents.size()) {
            return contents;
        }

        List<T> newList = Lists.newArrayList();
        ThreadLocalRandom.current()
                .ints(0, returnSize - 1)
                .distinct()
                .limit(returnSize)
                .forEach(index -> newList.add(contents.get(index)));
        return newList;
    }


    /**
     * 32位UUID
     *
     * @param length 指定的UUID长度
     * @return UUID
     */
    public static String uuid(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String uuid = new UUID(random.nextLong(), random.nextLong()).toString()
                .replace("-", "").toUpperCase();
        if (length == 16) {
            return uuid.substring(8, 24);
        } else {
            return uuid.substring(0, length);
        }
    }

    public static String uuid() {
        return uuid(32);
    }


    public static Integer getInt(int value) {
        if (value == 0) {
            return 1;
        }
        return ThreadLocalRandom.current().nextInt(value);
    }


    public static Long getLong(long value) {
        if (value == 0) {
            return 1L;
        }
        return ThreadLocalRandom.current().nextLong(value);

    }
}
