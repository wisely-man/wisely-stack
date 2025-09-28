package io.github.wisely.core.data.getter;

import com.google.common.cache.Cache;
import io.github.wisely.core.spring.helper.SpELHelper;
import lombok.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SpELHelper 全覆盖单元测试
 */
public class SpELHelperTest {

    /* ========================= 简单表达式 ========================= */

    @Test
    @DisplayName("简单表达式 + 类型转换")
    void testSimpleExpression() {
        Map<String, Object> vars = Map.of("age", "18");
        Integer age = SpELHelper.parseExpression("T(Integer).parseInt(#age) + 2", vars, Integer.class);
        assertEquals(20, age);
    }

    /* ========================= root 对象 ========================= */

    @Test
    @DisplayName("使用 root 对象访问属性")
    void testRootObject() {
        User user = new User("alice", 20);
        Map<String, Object> vars = Map.of("bonus", 5);
        Integer result = SpELHelper.parseExpression("age + #bonus", user, vars, Integer.class);
        assertEquals(25, result);
    }

    /* ========================= 模板渲染 ========================= */

    @Test
    @DisplayName("模板渲染：字符串拼接 + 计算")
    void testTemplate() {
        Map<String, Object> vars = Map.of("price", 100, "count", 2);
        String txt = SpELHelper.parseTemplate("订单金额：#{#price * #count} 元", vars);
        assertEquals("订单金额：200 元", txt);
    }

    @Test
    @DisplayName("模板渲染：支持 root 对象")
    void testTemplateWithRoot() {
        Order order = new Order(199.9);
        Map<String, Object> vars = Map.of("rate", 0.8);
        String txt = SpELHelper.parseTemplate("折后价：#{T(java.lang.Math).round(amount * #rate)} 元", order, vars);
        assertEquals("折后价：160 元", txt);
    }

    /* ========================= 自定义函数 ========================= */

    @Test
    @DisplayName("注册自定义静态函数并在表达式中调用")
    void testCustomFunction() throws NoSuchMethodException {
        Method isEmpty = StringUtils.class.getDeclaredMethod("isEmpty", CharSequence.class);
        EvaluationContext ctx = new StandardEvaluationContext();
        SpELHelper.registerFunction(ctx, "isEmpty", isEmpty);

        Map<String, Object> vars = Map.of("str", "");
        Boolean result = SpELHelper.parseExpression(ctx, "#isEmpty(#str)", vars, Boolean.class);
        assertTrue(result);
    }

    /* ========================= 缓存验证 ========================= */

    @Test
    @DisplayName("同一表达式只编译一次（缓存命中）")
    @SuppressWarnings("unchecked")
    void testCache() throws NoSuchFieldException, IllegalAccessException {
        String expr = "1 + 1";
        // 第一次会触发 Counter 里的 parse
        Integer v1 = SpELHelper.parseExpression(expr, Map.of(), Integer.class);
        assertEquals(2, v1);

        Field field = SpELHelper.class.getDeclaredField("EXPRESSION_CACHE");
        field.setAccessible(true);
        Object expressionCache = field.get(null);
        assertInstanceOf(Cache.class, expressionCache);

        assertNotNull(((Cache<@NonNull String, @NonNull Expression>) expressionCache).getIfPresent(expr));
    }

    /* ========================= 异常场景 ========================= */

    @Test
    @DisplayName("表达式语法错误应抛异常")
    void testInvalidExpression() {
        assertThrows(Exception.class,
                () -> SpELHelper.parseExpression("1 + ", Map.of(), Object.class));
    }


    /* ========================= 辅助类 ========================= */

    record User(String name, int age) {

    }

    record Order(double amount) {

    }

    static class StringUtils {
        public static boolean isEmpty(CharSequence cs) {
            return cs == null || cs.isEmpty();
        }
    }
}