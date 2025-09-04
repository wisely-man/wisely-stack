package com.wisely.starter.core.data.helper;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * 基于 Protostuff 的零依赖序列化工具。
 * 线程安全、空指针安全、自动回收 buffer。
 */
public final class ProtoBufHelper {

    /* ------------ 常量 ------------ */
    private static final int BUFFER_SIZE = 512;          // 单对象默认 512 B
    private static final int LIST_BUFFER_SIZE = 64 * 1024; // 列表默认 64 KB


    /**
     * 序列化/反序列化包装类 Schema 对象
     */
    @SuppressWarnings("rawtypes")
    private static final Schema<SerializeDeserializeWrapper> WRAPPER_SCHEMA = RuntimeSchema.getSchema(SerializeDeserializeWrapper.class);


    /**
     * 序列化对象
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的二进制数组
     */
    @SuppressWarnings("unchecked")
    public static byte[] serialize(Object obj) {


        Schema schema;
        LinkedBuffer buffer = null;
        Class<?> clazz = obj.getClass();
        try {
            Object serializeObject = obj;
            if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)
                    || Map.class.isAssignableFrom(clazz) || Set.class.isAssignableFrom(clazz)) {//Protostuff 不支持序列化/反序列化数组、集合等对象,特殊处理
                schema = WRAPPER_SCHEMA;
                serializeObject = SerializeDeserializeWrapper.builder(obj);
                buffer = LinkedBuffer.allocate(LIST_BUFFER_SIZE);
            } else {
                schema = RuntimeSchema.getSchema(clazz);
                buffer = LinkedBuffer.allocate(BUFFER_SIZE);
            }
            return ProtostuffIOUtil.toByteArray(serializeObject, schema, buffer);
        } finally {
            if (buffer != null) {
                buffer.clear();
            }
        }
    }

    /**
     * 反序列化对象
     *
     * @param data  需要反序列化的二进制数组
     * @param clazz 反序列化后的对象class
     * @param <T>   反序列化后的对象类型
     * @return 反序列化后的实例对象
     */
    public static <T> T deserialize(Class<T> clazz, byte[] data) {
        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz) || Set.class.isAssignableFrom(clazz)) {//Protostuff 不支持序列化/反序列化数组、集合等对象,特殊处理
            SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
            ProtostuffIOUtil.mergeFrom(data, wrapper, WRAPPER_SCHEMA);
            return wrapper.getData();
        } else {
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        }
    }

    /**
     * <p>
     * 序列化/反序列化对象包装类 专为基于 Protostuff 进行序列化/反序列化而定义。 Protostuff
     * 是基于POJO进行序列化和反序列化操作。 如果需要进行序列化/反序列化的对象不知道其类型，不能进行序列化/反序列化；
     * 比如Map、List、String、Enum等是不能进行正确的序列化/反序列化。
     * 因此需要映入一个包装类，把这些需要序列化/反序列化的对象放到这个包装类中。 这样每次 Protostuff
     * 都是对这个类进行序列化/反序列化,不会出现不能/不正常的操作出现
     * </p>
     *
     * @author butioy
     */
    @Setter
    @Getter
    static class SerializeDeserializeWrapper<T> {

        private T data;

        public static <T> SerializeDeserializeWrapper<T> builder(T data) {
            SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
            wrapper.setData(data);
            return wrapper;
        }

    }

    /* ------------ 私有构造 ------------ */
    private ProtoBufHelper() {
        throw new UnsupportedOperationException("ProtoBufHelper 不允许实例化");
    }
}