package io.github.wisely.core.spring.helper;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.helper.ValidHelper;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;

@UtilityClass
public class SpelHelper {

    /**
     * 线程安全的 SpEL 工具类
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 表达式缓存：key -> 编译后的 Expression
     */
    private static final Cache<@NonNull String, @NonNull Expression> EXPRESSION_CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(1024)
                    .expireAfterAccess(Duration.ofMinutes(5))
                    .build();

    /**
     * 默认模板解析上下文：#{...}
     */
    private static final TemplateParserContext TEMPLATE_PARSER_CONTEXT = new TemplateParserContext("#{", "}");

    /* -------------------------------------------------- 简单解析 -------------------------------------------------- */

    public static <T> T parseExpression(String expression, Map<String, Object> variables, Class<T> clazz) {
        return parseExpression(expression, null, variables, clazz);
    }

    public static <T> T parseExpression(String expression, Object root, Map<String, Object> variables, Class<T> clazz) {
        EvaluationContext context = new StandardEvaluationContext(root);
        return parseExpression(context, expression, variables, clazz);
    }

    public static <T> T parseExpression(EvaluationContext context, String expression, Map<String, Object> variables, Class<T> clazz) {
        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(context, "context");
        if (ValidHelper.isNotEmpty(variables)) {
            variables.forEach(context::setVariable);
        }
        return getOrCache(expression).getValue(context, clazz);
    }

    /* -------------------------------------------------- 模板解析 -------------------------------------------------- */

    public static String parseTemplate(String template, Map<String, Object> variables) {
        return parseTemplate(template, null, variables);
    }

    public static String parseTemplate(String template, Object root, Map<String, Object> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        return parseTemplate(context, template, variables);
    }

    public static String parseTemplate(EvaluationContext context, String template, Map<String, Object> variables) {
        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(context, "context");
        if (ValidHelper.isNotEmpty(variables)) {
            variables.forEach(context::setVariable);
        }
        return PARSER.parseExpression(template, TEMPLATE_PARSER_CONTEXT).getValue(context, String.class);
    }

    /* -------------------------------------------------- 注册自定义函数 -------------------------------------------------- */

    public static void registerFunction(EvaluationContext context, String name, Method method) {
        ((StandardEvaluationContext) context).registerFunction(name, method);
    }

    /* -------------------------------------------------- 私有方法 -------------------------------------------------- */

    private static Expression getOrCache(String expression) {
        return EXPRESSION_CACHE.asMap().computeIfAbsent(expression, PARSER::parseExpression);
    }

}
