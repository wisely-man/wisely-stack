package io.github.wisely.starter.core.data.getter;

import com.google.common.collect.Maps;
import io.github.wisely.starter.core.data.helper.JsonHelper;
import io.github.wisely.starter.core.helper.ValidHelper;
import jakarta.annotation.Nonnull;
import org.springframework.lang.Contract;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Map 代理类
 * 提供链式调用和类型转换等功能
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author wisely-man
 * @since 2.0.0
 */
public record MapProxy<K, V>(Map<K, V> map) implements Getter<K, V> {

    public MapProxy {
        map = map == null ? Maps.newHashMap() : map;
    }


    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Contract("null, _ -> param2")
    public V getOrDefault(K key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public MapProxy<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public V putIfAbsent(K key, V value) {
        return map.putIfAbsent(key, value);
    }

    public MapProxy<K, V> remove(K key) {
        map.remove(key);
        return this;
    }

    public MapProxy<K, V> putAll(Map<K, V> map) {
        if (ValidHelper.isNotNull(map)) {
            this.map.putAll(map);
        }
        return this;
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public MapProxy<K, V> clear() {
        map.clear();
        return this;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    public Map<K, V> replaceAll(BiConsumer<? super K, ? super V> function) {
        map.forEach(function);
        return map;
    }

    @Nonnull
    public Map<K, V> toMap() {
        return map;
    }

    /**
     * 转换为指定类型对象
     *
     * @param clazz 指定类型
     * @return 对象
     */
    @Nonnull
    public <T> T toPojo(Class<T> clazz) {
        return JsonHelper.copyTo(this, clazz);
    }

    @Override
    @Nonnull
    public String toString() {
        return JsonHelper.obj2Json(this);
    }

    @Nonnull
    public static <K, V> MapProxy<K, V> proxy(Map<K, V> map) {
        return new MapProxy<>(map);
    }
}
