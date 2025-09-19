package io.github.wisely.starter.core.data.getter;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.wisely.starter.core.data.helper.JsonHelper;
import io.github.wisely.starter.core.exception.SystemException;
import io.github.wisely.starter.core.helper.ValidHelper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基于 Spring BeanWrapper 的 Pojo 属性代理
 * 支持：嵌套路径、自动创建对象、类型转换、属性变更监听
 */
public class PojoProxy implements Getter<String, Object> {

    private final Object target;
    private final BeanWrapper wrapper;

    // 【新增】监听器列表
    private final List<Consumer<PropertyChangeEvent>> listeners = new ArrayList<>();

    private PojoProxy(Object target) {

        this.target = target;
        if (ValidHelper.isNull(target)) {
            this.wrapper = null;
            return;
        }

        this.wrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
        ((BeanWrapperImpl) this.wrapper).setConversionService(DefaultConversionService.getSharedInstance());
    }

    @Override
    public Object get(String fieldName) {

        if (ValidHelper.isNull(target)) {
            return null;
        }

        try {
            return wrapper.getPropertyValue(fieldName);
        } catch (Exception e) {
            throw SystemException.of(e, "Failed to get field: " + fieldName);
        }
    }

    /**
     * 设置属性值，并触发监听事件
     */
    public PojoProxy set(String fieldName, Object value) {

        if (ValidHelper.isNull(target)) {
            return this;
        }

        try {
            // 开启自动创建中间对象（user.address.city 中的 address 自动 new）
            wrapper.setAutoGrowNestedPaths(true);

            // 获取旧值（可能为 null）
            Object oldValue = wrapper.getPropertyValue(fieldName);

            // 执行设置（自动类型转换）
            wrapper.setPropertyValue(fieldName, value);

            // 触发监听（仅当值真正改变时）
            Object newValue = wrapper.getPropertyValue(fieldName);
            firePropertyChanged(fieldName, oldValue, newValue);

        } catch (Exception e) {
            throw SystemException.of(e, "Failed to set field: " + fieldName);
        }
        return this;
    }

    /**
     * 触发属性变更事件
     */
    private void firePropertyChanged(String propertyName, Object oldValue, Object newValue) {
        boolean changed = (oldValue == null && newValue != null) ||
                (oldValue != null && !oldValue.equals(newValue));

        if (changed && !listeners.isEmpty()) {
            PropertyChangeEvent event = new PropertyChangeEvent(propertyName, oldValue, newValue, target);
            for (Consumer<PropertyChangeEvent> listener : listeners) {
                listener.accept(event);
            }
        }
    }

    /**
     * 添加属性变更监听器
     *
     * @param listener 监听函数，接收 PropertyChangeEvent
     * @return 当前实例（支持链式调用）
     */
    public PojoProxy onPropertyChange(Consumer<PropertyChangeEvent> listener) {
        if (listener != null) {
            synchronized (listeners) {
                listeners.add(listener);
            }
        }
        return this;
    }

    /**
     * 移除监听器
     */
    public PojoProxy removeListener(Consumer<PropertyChangeEvent> listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
        return this;
    }

    /**
     * 清空所有监听器
     */
    public PojoProxy clearListeners() {
        synchronized (listeners) {
            listeners.clear();
        }
        return this;
    }

    // === 工具方法 ===

    public Map<String, Object> toMap() {
        return JsonHelper.copyTo(target, new TypeReference<>() {
        });
    }

    @Nonnull
    public Object getPojo() {
        return target;
    }

    @Override
    @Nonnull
    public String toString() {
        return JsonHelper.obj2Json(target);
    }

    public static PojoProxy proxy(Object pojo) {
        return new PojoProxy(pojo);
    }

    // === 事件类 ===

    /**
     * 属性变更事件
     *
     * @param source 原始对象
     */
    public record PropertyChangeEvent(
            String propertyName,
            Object oldValue,
            Object newValue,
            Object source) {

        @Nonnull
        @Override
        public String toString() {
            return String.format("PropertyChangeEvent{prop='%s', old=%s, new=%s}", propertyName, oldValue, newValue);
        }
    }
}