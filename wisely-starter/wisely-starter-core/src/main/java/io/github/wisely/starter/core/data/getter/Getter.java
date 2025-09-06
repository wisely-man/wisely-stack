package io.github.wisely.starter.core.data.getter;

import io.github.wisely.starter.core.data.helper.DataHelper;
import io.github.wisely.starter.core.data.helper.DateHelper;
import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Getter接口<br>
 * 提供各种类型的Getter方法
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Big_程
 * @since 2.0.0
 */
public interface Getter<K, V> {

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    V get(K key);


    /**
     * 获取值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    default V get(K key, V defaultValue) {
        V v = get(key);
        return v == null ? defaultValue : v;
    }


    /**
     * 获取boolean型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Boolean getBoolean(K key, Boolean defaultValue) {
        return DataHelper.getBoolean(get(key), defaultValue);
    }


    /**
     * 获取boolean型属性值<br>
     * 无值或获取错误返回false
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Boolean getBoolean(K key) {
        return this.getBoolean(key, false);
    }


    /**
     * 获取byte型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Byte getByte(K key, Byte defaultValue) {
        return DataHelper.getByte(get(key), defaultValue);
    }


    /**
     * 获取byte型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Byte getByte(K key) {
        return this.getByte(key, (byte) 0);
    }


    /**
     * 获取char型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Character getChar(K key, Character defaultValue) {
        return DataHelper.getCharacter(get(key), defaultValue);
    }


    /**
     * 获取char型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Character getChar(K key) {
        return this.getChar(key, (char) 0);
    }


    /**
     * 获取short型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Short getShort(K key, Short defaultValue) {
        return DataHelper.getShort(get(key), defaultValue);
    }


    /**
     * 获取short型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Short getShort(K key) {
        return this.getShort(key, (short) 0);
    }


    /**
     * 获取int型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Integer getInt(K key, Integer defaultValue) {
        return DataHelper.getInt(get(key), defaultValue);
    }


    /**
     * 获取int型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Integer getInt(K key) {
        return this.getInt(key, 0);
    }


    /**
     * 获取long型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Long getLong(K key, Long defaultValue) {
        return DataHelper.getLong(get(key), defaultValue);
    }


    /**
     * 获取long型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Long getLong(K key) {
        return this.getLong(key, 0L);
    }


    /**
     * 获取float型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Float getFloat(K key, Float defaultValue) {
        return DataHelper.getFloat(get(key), defaultValue);
    }


    /**
     * 获取float型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Float getFloat(K key) {
        return this.getFloat(key, 0F);
    }


    /**
     * 获取double型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Double getDouble(K key, Double defaultValue) {
        return DataHelper.getDouble(get(key), defaultValue);
    }


    /**
     * 获取double型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default Double getDouble(K key) {
        return this.getDouble(key, 0D);
    }


    /**
     * 获取bigint型属性值<br>
     * 若获得的值为不可见字符，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default BigInteger getBigInt(K key, BigInteger defaultValue) {
        return DataHelper.getBigInt(get(key), defaultValue);
    }


    /**
     * 获取bigint型属性值<br>
     * 无值或获取错误返回0
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default BigInteger getBigInt(K key) {
        return this.getBigInt(key, BigInteger.ZERO);
    }


    /**
     * 获取String型属性值<br>
     * 若获得的值为null，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default String getString(K key, String defaultValue) {
        return DataHelper.getString(get(key), defaultValue);
    }


    /**
     * 获取String型属性值<br>
     * 无值返回空字符串
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default String getString(K key) {
        return this.getString(key, "");
    }


    /**
     * 获取BigDecimal型属性值<br>
     * 若获得的值为null，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default BigDecimal getBigDecimal(K key, BigDecimal defaultValue) {
        return DataHelper.getBigDecimal(get(key), defaultValue);
    }


    /**
     * 获取BigDecimal型属性值<br>
     * 无值返回null
     *
     * @param key 属性名
     * @return 属性值
     */
    @Nonnull
    default BigDecimal getBigDecimal(K key) {
        return this.getBigDecimal(key, BigDecimal.ZERO);
    }


    /**
     * 获取Date型属性值<br>
     * 若获得的值为null，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应值返回defaultValue
     */
    default Date getDate(K key, Date defaultValue) {
        Date date = DateHelper.getDate(get(key));
        return date == null ? defaultValue : date;
    }


    /**
     * 获取LocalDateTime类型属性值<br>
     * 若获得的值为null，使用默认值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值，无对应的值返回defaultValue
     */
    default LocalDateTime getLocalDateTime(K key, LocalDateTime defaultValue) {
        LocalDateTime localDateTime = DateHelper.getLocalDateTime(get(key));
        return localDateTime == null ? defaultValue : localDateTime;
    }


    /**
     * 获取HashSet类型属性值
     *
     * @param key 键值
     * @return HashSet对象，不会返回空值
     */
    @Nonnull
    default HashSet<?> getHashSet(K key) {
        return DataHelper.getHashSet(get(key));
    }

    /**
     * 获取TreeSet类型属性值
     *
     * @param key 键值
     * @return TreeSet对象，不会返回空值
     */
    @Nonnull
    default TreeSet<?> getTreeSet(K key) {
        return DataHelper.getTreeSet(get(key));
    }


    /**
     * 获取ArrayList类型属性值
     *
     * @param key 键值
     * @return Array对象，不会返回空值
     */
    @Nonnull
    default ArrayList<?> getArrayList(K key) {
        return DataHelper.getArrayList(get(key));
    }


    /**
     * 获取LinkedList类型属性值
     *
     * @param key 键值
     * @return LinkedList对象，不会返回空值
     */
    default LinkedList<?> getLinkedList(K key) {
        return DataHelper.getLinkedList(get(key));
    }
}
