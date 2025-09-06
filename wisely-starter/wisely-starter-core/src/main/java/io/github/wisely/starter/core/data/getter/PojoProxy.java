package io.github.wisely.starter.core.data.getter;


import com.fasterxml.jackson.core.type.TypeReference;
import io.github.wisely.starter.core.data.helper.JsonHelper;
import io.github.wisely.starter.core.exception.SystemException;
import io.github.wisely.starter.core.helper.ValidHelper;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 获取对象属性值
 */
public class PojoProxy implements Getter<String, Object> {

    private PojoProxy(Object target) {
        this.target = target;
        if (target != null) {
            cacheAccessors(target.getClass());
        }
    }

    private final Object target;

    private final Map<String, Method> getterCache = new ConcurrentHashMap<>();
    private final Map<String, Method> setterCache = new ConcurrentHashMap<>();
    private final Map<String, Field> fieldCache = new ConcurrentHashMap<>();

    private void cacheAccessors(Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            String name = method.getName();
            if (name.startsWith("get") && method.getParameterCount() == 0) {
                String key = decapitalize(name.substring(3));
                getterCache.putIfAbsent(key, method);
            } else if (name.startsWith("set") && method.getParameterCount() == 1) {
                String key = decapitalize(name.substring(3));
                setterCache.putIfAbsent(key, method);
            }
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            fieldCache.put(field.getName(), field);
        }
    }

    private String decapitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }


    @Override
    public Object get(String fieldName) {
        if (ValidHelper.isNull(target)) {
            return null;
        }

        try {
            // 嵌套属性
            if (fieldName.contains(".")) {
                return getNested(fieldName);
            }

            Method getter = getterCache.get(fieldName);
            if (getter != null) {
                return getter.invoke(target);
            }

            Field field = fieldCache.get(fieldName);
            return field != null ? field.get(target) : null;
        } catch (Exception e) {
            throw SystemException.of(e, "Failed to get field: " + fieldName);
        }
    }

    private Object getNested(String path) {
        String[] parts = path.split("\\.");
        Object current = target;
        for (String part : parts) {
            if (current == null) {
                return null;
            }
            current = getValue(current, part);
        }
        return current;
    }

    private Object getValue(Object obj, String fieldName) {
        try {
            Method getter = findGetter(obj.getClass(), fieldName);
            if (getter != null) return getter.invoke(obj);
            Field field = findField(obj.getClass(), fieldName);
            return field != null ? field.get(obj) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public PojoProxy set(String fieldName, Object value) {

        if (ValidHelper.isNull(target)) {
            return this;
        }

        try {
            if (fieldName.contains(".")) {
                setNested(fieldName, value);
            } else {
                setValue(target, fieldName, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
        return this;
    }

    private void setNested(String path, Object value) {
        String[] parts = path.split("\\.");
        Object current = target;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            Object next = getValue(current, part);

            if (next == null) {
                next = createIntermediateObject(current, part);
                setValue(current, part, next);
            }

            current = next;
        }

        setValue(current, parts[parts.length - 1], value);
    }

    private Object createIntermediateObject(Object parent, String fieldName) {
        try {
            Class<?> parentClass = parent.getClass();
            Method getter = findGetter(parentClass, fieldName);
            Field field = findField(parentClass, fieldName);

            Class<?> type = getter != null ? getter.getReturnType() :
                    field != null ? field.getType() : null;

            if (type == null || type.isPrimitive() || type.isArray() || Collection.class.isAssignableFrom(type)) {
                throw SystemException.of("Cannot create intermediate object for field: " + fieldName);
            }

            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw SystemException.of("Failed to create intermediate object for field: " + fieldName, e);
        }
    }

    private void setValue(Object obj, String fieldName, Object value) {
        try {
            Method setter = findSetter(obj.getClass(), fieldName);
            if (setter != null) {
                Class<?> paramType = setter.getParameterTypes()[0];
                setter.invoke(obj, convertType(value, paramType));
                return;
            }

            Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.set(obj, convertType(value, field.getType()));
            }
        } catch (Exception e) {
            throw SystemException.of(e, "Failed to set field: " + fieldName);
        }
    }

    private Object convertType(Object value, Class<?> targetType) {
        if (targetType.isInstance(value)) {
            return value;
        }
        return JsonHelper.copyTo(value, targetType);
    }

    private Method findGetter(Class<?> clazz, String name) {
        return getterCache.computeIfAbsent(name, k -> {
            try {
                return clazz.getMethod("get" + capitalize(k));
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
    }

    private Method findSetter(Class<?> clazz, String name) {
        return setterCache.computeIfAbsent(name, k -> {
            try {
                Field field = findField(clazz, k);
                if (field != null) {
                    return clazz.getMethod("set" + capitalize(k), field.getType());
                }
            } catch (Exception ignored) {
            }
            return null;
        });
    }

    private Field findField(Class<?> clazz, String name) {
        return fieldCache.computeIfAbsent(name, k -> {
            try {
                return clazz.getDeclaredField(k);
            } catch (NoSuchFieldException e) {
                return null;
            }
        });
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

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
}
