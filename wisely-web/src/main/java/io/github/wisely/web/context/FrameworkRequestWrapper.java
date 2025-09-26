package io.github.wisely.web.context;

import io.github.wisely.core.exception.SystemException;
import io.github.wisely.core.helper.ValidHelper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class FrameworkRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 构造器
     *
     * @param request 请求对象
     */
    public FrameworkRequestWrapper(HttpServletRequest request) {
        super(request);
        // 将参数表，赋予给当前Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
        try {
            this.body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw SystemException.of(e, "Request body copy failed!!!");
        }
    }

    /**
     * 构造器
     *
     * @param request      请求对象
     * @param extendParams 需要扩展的参数
     */
    public FrameworkRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
        this(request);
        addAllParameters(extendParams);
    }


    private final Map<String, String[]> params = new HashMap<>();
    private final byte[] body;


    /**
     * 重写getInputStream方法
     *
     * @return ServletInputStream
     */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream in = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() throws IOException {
                return in.read();
            }
        };
    }

    /**
     * 重写getReader方法
     *
     * @return BufferedReader
     * @throws IOException IO异常
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 重写getParameter方法
     *
     * @param name 参数名
     * @return 参数数值
     */
    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null) {
            return null;
        }
        return values[0];
    }

    /**
     * 重写getParameterValues方法
     *
     * @param name 参数名
     * @return 参数数值数组
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }

    /**
     * 在获取所有的参数名,必须重写此方法，
     * 否则对象中参数值映射不上
     *
     * @return 参数名枚举
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<>(params.keySet()).elements();
    }

    public void addAllParameters(Map<String, Object> extendParams) {
        if (ValidHelper.isNotEmpty(extendParams)) {
            for (Map.Entry<String, Object> entry : extendParams.entrySet())
                addParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     */
    public void addParameter(String key, Object value) {
        if (value != null) {
            if (value instanceof String[])
                params.put(key, (String[]) value);
            else if (value instanceof String)
                params.put(key, new String[]{(String) value});
            else
                params.put(key, new String[]{String.valueOf(value)});
        }
    }
}
