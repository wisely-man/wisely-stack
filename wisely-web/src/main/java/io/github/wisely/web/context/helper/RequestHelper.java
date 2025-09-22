package io.github.wisely.web.context.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import io.github.wisely.core.data.getter.MapProxy;
import io.github.wisely.core.data.helper.DataHelper;
import io.github.wisely.core.data.helper.JsonHelper;
import io.github.wisely.core.exception.BaseException;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.helper.StringHelper;
import io.github.wisely.core.helper.ValidHelper;
import io.github.wisely.core.spring.helper.SpringHelper;
import io.github.wisely.web.context.FrameworkRequestWrapper;
import io.github.wisely.web.context.Result;
import io.github.wisely.web.i18n.MessageConvert;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

/**
 * RequestHelper
 */
@UtilityClass
@Slf4j
public class RequestHelper {

    private final static String REQUEST_DATA = "REQUEST_DATA";

    private final static ThreadLocal<FrameworkRequestWrapper> THREAD_LOCAL_MAP = new ThreadLocal<>();


    // ================================
    // ThreadLocal 管理
    // ================================

    public static void setRequest(FrameworkRequestWrapper request) {
        THREAD_LOCAL_MAP.set(request);
    }

    /**
     * 清理
     */
    public static void clear() {
        THREAD_LOCAL_MAP.remove();
    }


    // ================================
    // 基础对象获取
    // ================================

    /**
     * 获取当前请求的 Request对象
     *
     * @return Request对象
     */
    @Nullable
    public static HttpServletRequest getRequest() {

        FrameworkRequestWrapper requestWrapper = THREAD_LOCAL_MAP.get();
        if (ValidHelper.isNotNull(requestWrapper)) {
            return requestWrapper;
        }

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra instanceof ServletRequestAttributes sra) {
            return sra.getRequest();
        }

        return null;
    }

    /**
     * 获取当前请求的 Request对象
     *
     * @return Response对象
     */
    @Nullable
    public static HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra instanceof ServletRequestAttributes sra) {
            return sra.getResponse();
        }

        return null;
    }


    /**
     * 获取session
     *
     * @return session
     */
    @Nullable
    public static HttpSession getSession() {
        return getRequest() == null ? null : getRequest().getSession();
    }

    /**
     * 获取sessionId
     *
     * @return sessionId
     */
    @Nullable
    public static String getSessionId() {
        return getSession() == null ? null : getSession().getId();
    }

    /**
     * 获取cookies
     *
     * @return cookies
     */
    @Nullable
    public static Cookie[] getCookies() {
        return getRequest() == null ? null : getRequest().getCookies();
    }


    // ================================
    // Cookie 操作
    // ================================

    /**
     * 获取cookie中指定值
     *
     * @param key cookie key
     * @return cookie value
     */
    @Nullable
    public static String getCookie(String key) {
        return getCookie(key, null);
    }

    /**
     * 获取cookie中指定值
     *
     * @param key cookie key
     * @return cookie value
     */
    @Nullable
    public static String getCookie(String key, String defaultVal) {
        Cookie[] cookies = getCookies();
        if (ValidHelper.isEmpty(cookies)) {
            return defaultVal;
        }

        for (Cookie cookie : cookies) {
            if (Strings.CS.equals(cookie.getName(), key)) {
                try {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    log.warn("Failed to decode cookie value for key: {}", key, e);
                    return defaultVal;
                }
            }
        }
        return defaultVal;
    }


    // ================================
    // IP 获取
    // ================================

    /**
     * 获取发起当前请求的客户端IP
     *
     * @return 客户端IP地址
     */
    public static String getIP() {
        return getIP(getRequest());
    }

    /**
     * 获取IP地址
     * 多次反向代理后会有多个ip值，第一个ip才是真实ip
     *
     * @param request 请求
     * @return request发起客户端的IP地址
     */
    public static String getIP(HttpServletRequest request) {
        if (request == null) return "0.0.0.0";

        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String value = request.getHeader(header);
            if (StringHelper.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
                // X-Forwarded-For: client, proxy1, proxy2
                String[] ips = value.split(",");
                for (String ip : ips) {
                    ip = ip.trim();
                    if (StringHelper.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                        return ip;
                    }
                }
            }
        }

        return request.getRemoteAddr();
    }


    // ================================
    // 请求参数解析
    // ================================

    /**
     * 获取请求头中的参数
     *
     * @param name 参数名称
     * @return 参数值
     */
    @Nullable
    public static String getHeader(String name) {
        return Optional.ofNullable(getRequest())
                .map(r -> r.getHeader(name))   // 优先 param
                .orElse(null);
    }

    /**
     * 获取请求参数
     *
     * @param name 参数名称
     * @return 参数值
     */
    @Nullable
    public static String getParameter(String name) {
        return Optional.ofNullable(getRequest())
                .map(r -> r.getParameter(name))   // 优先 param
                .orElse(null);
    }

    @Nonnull
    public static MapProxy<String, Object> getInput() {
        return getInput(getRequest(), false);
    }

    @Nonnull
    public static MapProxy<String, Object> getInput(@Nullable HttpServletRequest request) {
        return getInput(request, false);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static MapProxy<String, Object> getInput(@Nullable HttpServletRequest request, boolean clearHistory) {
        if (request == null) {
            return MapProxy.proxy(Maps.newHashMap());
        }

        // 尝试从缓存读取
        MapProxy<String, Object> input = (MapProxy<String, Object>) request.getAttribute(REQUEST_DATA);
        if (input != null && !clearHistory) {
            return input;
        }

        input = MapProxy.proxy(Maps.newHashMap());

        // 1. 查询参数（URL ?a=1&b=2）
        input.putAll(getInputFromQueryParams(request));

        // 2. 判断是否为 Multipart 请求
        MultipartResolver resolver = SpringHelper.getBean(MultipartResolver.class);
        if (resolver != null && resolver.isMultipart(request)) {
            input.putAll(getInputFromMultipart(request));
        }
        // 3. 非 Multipart：交由注册的内容类型处理器处理
        else {
            handleRequestBody(input, request);
        }

        // 缓存结果
        request.setAttribute(REQUEST_DATA, input);
        return input;
    }


    // ================================
    // 可扩展 Content-Type 处理机制
    // ================================

    @FunctionalInterface
    public interface RequestBodyHandler {
        void handle(Map<String, Object> input, HttpServletRequest request);
    }

    private static final Map<String, RequestBodyHandler> BODY_HANDLERS = Maps.newLinkedHashMap();

    // 静态注册常用处理器
    static {
        BODY_HANDLERS.put("application/json", RequestHelper::getInputFromJson);
        // 可在此注册更多默认处理器，如：
        // BODY_HANDLERS.put("application/xml", RequestHelper::getInputFromXml);
    }

    /**
     * 请求体处理器注册（支持运行时扩展）
     *
     * @param contentType 请求内容类型
     *                    例如：application/json
     * @param handler     处理器函数
     */
    public static void registerBodyHandler(String contentType, RequestBodyHandler handler) {
        BODY_HANDLERS.put(contentType.toLowerCase(), handler);
    }

    /**
     * 移除已注册的处理器
     */
    public static void removeBodyHandler(String contentType) {
        BODY_HANDLERS.remove(contentType.toLowerCase());
    }

    /**
     * 根据 Content-Type 分发请求体解析
     *
     * @param input   请求参数集合
     * @param request 请求对象
     */
    private static void handleRequestBody(MapProxy<String, Object> input, HttpServletRequest request) {

        String contentType = request.getContentType();
        if (StringHelper.isBlank(contentType)) {
            return;
        }

        String mainType = extractMainContentType(contentType);

        BODY_HANDLERS.entrySet().stream()
                .filter(entry -> matchesContentType(mainType, entry.getKey()))
                .findFirst()
                .ifPresent(entry -> {
                    try {
                        entry.getValue().handle(input.toMap(), request);
                    } catch (Exception e) {
                        log.warn("Failed to handle request body for Content-Type: {}", entry.getKey(), e);
                    }
                });
    }

    /**
     * 提取主类型，如 "application/json; charset=UTF-8" → "application/json"
     *
     * @param contentType 内容类型
     */
    private static String extractMainContentType(String contentType) {
        return contentType.split(";")[0].trim().toLowerCase();
    }

    /**
     * 匹配 Content-Type（支持通配符，如 text/*）
     */
    private static boolean matchesContentType(String actual, String expected) {
        if (expected.endsWith("/*")) {
            return actual.startsWith(expected.substring(0, expected.length() - 1));
        }
        return actual.equals(expected);
    }

    // ================================
    // 具体处理器实现
    // ================================

    /**
     * 获取或创建请求包装器（支持重复读）
     */
    private static FrameworkRequestWrapper getOrCreateWrapper(HttpServletRequest request) {
        if (request instanceof FrameworkRequestWrapper wrapper) {
            return wrapper;
        }
        return new FrameworkRequestWrapper(request);
    }

    /**
     * 解析 application/json 请求体
     */
    private static void getInputFromJson(Map<String, Object> input, HttpServletRequest request) {
        try {
            FrameworkRequestWrapper wrapper = getOrCreateWrapper(request);
            try (BufferedReader reader = wrapper.getReader()) {
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                String content = body.toString().trim();
                if (content.isEmpty()) {
                    return;
                }

                Map<String, Object> map =
                        JsonHelper.json2Obj(content, new TypeReference<>() {
                        });
                if (map != null) {
                    input.putAll(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse JSON request body", e);
        }
    }

    /**
     * 从 URL 查询参数中提取数据
     *
     * @param request 请求对象
     */
    private static Map<String, Object> getInputFromQueryParams(HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            String[] values = request.getParameterValues(key);
            result.put(key, StringHelper.join(values, ","));
        }
        return result;
    }

    /**
     * 解析 multipart/form-data（文件上传）
     *
     * @param request 请求对象
     */
    private static Map<String, Object> getInputFromMultipart(HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();

        MultipartResolver resolver = SpringHelper.getBean(MultipartResolver.class);
        if (resolver == null || !resolver.isMultipart(request)) {
            return result;
        }

        try {
            MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

            // 文件字段
            multipartRequest.getMultiFileMap().forEach((key, files) -> {
                result.put(key, files.size() == 1 ? files.getFirst() : files);
            });

            // 文本字段（避免覆盖文件字段）
            multipartRequest.getParameterMap().forEach((key, values) -> {
                if (!result.containsKey(key)) {
                    result.put(key, StringHelper.join(values, ","));
                }
            });
        } catch (Exception e) {
            log.warn("Failed to parse multipart request", e);
        }

        return result;
    }


    // ================================
    // 响应输出
    // ================================

    /**
     * 前台消息返回
     *
     * @param code    状态码
     * @param message 提示信息
     * @param data    数据
     * @param params  参数
     */
    public static void writeResponse(int code, String message, Object data, Object... params) {

        HttpServletResponse response = getResponse();
        CommonExceptionEnum.UNREACHABLE_CODE.assertNotNull(response);

        try (PrintWriter out = response.getWriter()) {
            // 国际化消息转换
            if (SpringHelper.hasBean(MessageConvert.class)) {
                MessageConvert converter = SpringHelper.getBean(MessageConvert.class);
                if (converter != null) {
                    message = converter.messageConvert(message, params);
                }
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            String resp = JsonHelper.obj2Json(Result.response(code, message, data));
            out.write(DataHelper.getString(resp, "failed"));
        } catch (Exception e) {
            log.error("Failed to write response", e);
        }
    }

    /**
     * 前台错误消息返回
     *
     * @param e    错误信息
     * @param data 数据
     */
    public static void writeResponse(BaseException e, Object data) {
        writeResponse(e.getCode(), e.getMessage(), data, e.getObjects());
    }

}
