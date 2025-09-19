package io.github.wisely.web.context;

import io.github.wisely.core.helper.RandomHelper;
import io.github.wisely.web.context.helper.RequestHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import java.io.IOException;


/**
 * 用于request的包装，支持流的重复读取
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public class FrameworkFilter implements Filter {

    /**
     * 请求ID
     */
    public static final String TRACE_ID = "wisely-trace-id";

    /**
     * 过滤器的doFilter方法，用于对请求进行过滤。
     * 1. 支持日志的检索，MDC.put(TRACE_ID, traceId)
     * 2. 提供对request.getInputStream()的封装，支持可重复读
     *
     * @param servletRequest  Servlet请求对象。
     * @param servletResponse Servlet响应对象。
     * @param filterChain     过滤器链，用于将请求传递给下一个过滤器或servlet。
     * @throws IOException      如果发生I/O错误。
     * @throws ServletException 如果发生Servlet相关错误。
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 获取并记录请求头中的TRACE_ID
        MDC.put(TRACE_ID, RandomHelper.uuid());

        // 设置请求和响应的字符编码为UTF-8
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        // 将ServletRequest和ServletResponse转换为HttpServletRequest和HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 创建并设置一个请求包装器，用于拦截和修改请求信息
        FrameworkRequestWrapper wrapper = new FrameworkRequestWrapper(request);
        RequestHelper.setRequest(wrapper);

        try {
            // 调用过滤器链，将请求传递给下一个过滤器或servlet
            filterChain.doFilter(wrapper, response);
        } finally {
            // 在请求处理完成后，清理线程变量，防止污染其他请求
            RequestHelper.clear();
            // 清理MDC中的TRACE_ID
            MDC.remove(TRACE_ID);
        }
    }

}
