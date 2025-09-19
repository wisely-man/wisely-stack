package io.github.wisely.core.data.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.wisely.core.data.eum.Common;
import io.github.wisely.core.exception.SystemException;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.helper.StringHelper;
import io.github.wisely.core.helper.ValidHelper;
import io.github.wisely.core.spring.helper.SpringHelper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Contract;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * JSON转换处理工具类
 */
@UtilityClass
@Slf4j
public class JsonHelper {

    private static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = SpringHelper.getBean(ObjectMapper.class);
        if (OBJECT_MAPPER == null) {
            setObjectMapper(FrameworkObjectMapper.INSTANCE);
        }
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }


    public static class FrameworkObjectMapper {

        public static ObjectMapper INSTANCE;

        /*
          初始化预定义的ObjectMapper
         */
        static {
            INSTANCE = new ObjectMapper();
            // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
            INSTANCE.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            // 设置Date类型的序列化及反序列化格式
            INSTANCE.setDateFormat(new SimpleDateFormat(Common.DATETIME.getPattern()));
            // 忽略空Bean转json的错误
            INSTANCE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
            INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
            INSTANCE.registerModule(new JavaTimeModule());
            // 允许使用未带引号的字段名
            INSTANCE.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            // 允许使用单引号
            INSTANCE.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        }
    }


    /**
     * 对象 => json字符串
     *
     * @param obj 源对象
     */
    @Nullable
    @Contract("!null -> !null; null -> null")
    public static <T> String obj2Json(@Nullable T obj) {

        if (ValidHelper.isNull(obj)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw SystemException.of(e, "json.json_parser_error");
        }
    }

    /**
     * 对象 => JsonNode
     *
     * @param obj 源对象
     * @param <T> 泛型
     * @return JsonNode
     */
    @Nullable
    @Contract("null -> null; !null -> !null")
    public static <T> JsonNode obj2JsonNode(T obj) {
        return json2Obj(obj2Json(obj), JsonNode.class);
    }

    /**
     * json字符串 => 对象
     *
     * @param json  源json串
     * @param clazz 对象类
     * @param <T>   泛型
     */
    @Nullable
    @Contract("null,_ -> null")
    public static <T> T json2Obj(@Nullable String json, @Nonnull Class<T> clazz) {

        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(clazz, "clazz");

        if (StringHelper.isBlank(json)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw SystemException.of(e, "JsonHelper.json2Obj failed");
        }
    }

    /**
     * json字符串 => 对象
     *
     * @param json 源json串
     * @param type 对象类型
     * @param <T>  泛型
     */
    @Nullable
    @Contract("null,_ -> null")
    public static <T> T json2Obj(@Nullable String json, @Nonnull TypeReference<T> type) {

        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(type, "type");

        if (StringHelper.isBlank(json)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw SystemException.of(e, "JsonHelper.json2Obj failed");
        }
    }


    /**
     * 复制为指定类型对象
     *
     * @param obj   待转换对象
     * @param clazz 目标类
     * @return 目标类型对象
     */
    @Contract("null,_ -> null; !null,_ -> !null")
    public static <T> T copyTo(@Nullable Object obj, @Nonnull Class<T> clazz) {
        return json2Obj(obj2Json(obj), clazz);
    }


    /**
     * 复制为指定类型对象
     *
     * @param obj  待转换对象
     * @param type 目标类型
     * @return 目标类型对象
     */
    @Contract("null,_ -> null; !null,_ -> !null")
    public static <T> T copyTo(@Nullable Object obj, @Nonnull TypeReference<T> type) {
        return json2Obj(obj2Json(obj), type);
    }


    /**
     * 对象转换为格式化的Json字符串
     *
     * @param object 待转换对象
     * @return 格式化后的字符串
     */
    @Contract("null -> null; !null -> !null")
    public static String toPrettyString(@Nullable Object object) {
        if (ValidHelper.isNull(object)) {
            return null;
        }

        JsonNode node = obj2JsonNode(object);
        return node.toPrettyString();
    }

}
