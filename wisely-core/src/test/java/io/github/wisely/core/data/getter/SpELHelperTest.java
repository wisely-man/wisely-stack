package io.github.wisely.core.data.getter;

import io.github.wisely.core.spring.helper.SpELHelper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SpELHelper 工具类的单元测试
 */
public class SpELHelperTest {

    // 测试用的根对象
    private TestUser user;
    // 测试用的变量集合
    private Map<String, Object> variables;

    /**
     * 在每个测试方法执行前初始化数据
     */
    @BeforeEach
    void setUp() {
        // 初始化根对象
        user = new TestUser();
        user.setName("Alice");
        user.setAge(30);
        user.setSalary(new BigDecimal("8000.00"));

        // 初始化变量集合
        variables = new HashMap<>();
        variables.put("discount", 0.9); // 折扣率 90%
        variables.put("bonus", new BigDecimal("1000")); // 奖金
        variables.put("taxRate", 0.1); // 税率 10%
    }

    /**
     * 测试简单表达式解析（不带根对象，仅使用变量）
     */
    @Test
    void testParseExpressionWithVariablesOnly() {
        // 场景1: 计算折扣后的价格
        String expression1 = "#discount * 100";
        BigDecimal result1 = SpELHelper.parseExpression(expression1, null, variables, BigDecimal.class);
        assertEquals(new BigDecimal("90.0"), result1);

        // 场景2: 计算奖金加上固定津贴
        String expression2 = "#bonus + 500";
        BigDecimal result2 = SpELHelper.parseExpression(expression2, null, variables, BigDecimal.class);
        assertEquals(new BigDecimal("1500"), result2);

        // 场景3: 判断税率是否大于某个值
        String expression3 = "#taxRate > 0.05";
        Boolean result3 = SpELHelper.parseExpression(expression3, null, variables, Boolean.class);
        assertTrue(result3);
    }

    /**
     * 测试简单表达式解析（使用根对象）
     */
    @Test
    void testParseExpressionWithRootObject() {
        // 场景1: 直接访问根对象属性
        String expression1 = "name";
        String result1 = SpELHelper.parseExpression(expression1, user, null, String.class);
        assertEquals("Alice", result1);

        // 场景2: 访问根对象的嵌套属性或方法 (age 是属性)
        String expression2 = "age + 5";
        Integer result2 = SpELHelper.parseExpression(expression2, user, null, Integer.class);
        assertEquals(35, result2);

        // 场景3: 对根对象的数值属性进行计算
        String expression3 = "salary.multiply(1.1)"; // 薪水上涨10%
        BigDecimal result3 = SpELHelper.parseExpression(expression3, user, null, BigDecimal.class);
        assertEquals(0, new BigDecimal("8800.00").compareTo(result3));
    }

    /**
     * 测试简单表达式解析（同时使用根对象和变量）
     */
    @Test
    void testParseExpressionWithRootAndVariables() {
        // 场景: 计算用户的实际收入 = (基本工资 + 奖金) * 折扣率 - 税
        String expression = "(salary + #bonus) * #discount - salary * #taxRate";
        BigDecimal result = SpELHelper.parseExpression(expression, user, variables, BigDecimal.class);
        // 计算过程: (8000 + 1000) * 0.9 - 8000 * 0.1 = 9000 * 0.9 - 800 = 8100 - 800 = 7300
        assertEquals(0, new BigDecimal("7300.00").compareTo(result));
    }

    /**
     * 测试模板解析（Template），结果总是字符串
     */
    @Test
    void testParseTemplate() {
        // 场景1: 使用变量的模板
        String template1 = "Hello #{#discount * 100}% user! Your bonus is #{#bonus}.";
        String result1 = SpELHelper.parseTemplate(template1, variables);
        assertEquals("Hello 90.0% user! Your bonus is 1000.", result1);

        // 场景2: 使用根对象的模板
        String template2 = "Welcome, #{name}! You are #{age} years old.";
        String result2 = SpELHelper.parseTemplate(template2, user, null);
        assertEquals("Welcome, Alice! You are 30 years old.", result2);

        // 场景3: 混合使用根对象和变量的模板
        String template3 = "#{name}'s net salary after bonus and tax: #{(salary + #bonus) * (1 - #taxRate)}";
        String result3 = SpELHelper.parseTemplate(template3, user, variables);
        // 计算: (8000 + 1000) * (1 - 0.1) = 9000 * 0.9 = 8100
        assertEquals("Alice's net salary after bonus and tax: 8100.000", result3);
    }

    /**
     * 测试注册自定义函数
     */
    @Test
    void testRegisterFunction() throws NoSuchMethodException {
        StandardEvaluationContext context = new StandardEvaluationContext(user);
        // 将当前测试类中的静态方法注册为SpEL函数
        Method customMethod = SpELHelperTest.class.getDeclaredMethod("customToUpper", String.class);
        SpELHelper.registerFunction(context, "toUpper", customMethod);

        // 使用注册的函数
        String expression = "#toUpper(name)";
        String result = SpELHelper.parseExpression(context, expression, null, String.class);
        assertEquals("ALICE", result);
    }

    /**
     * 自定义的辅助方法，用于测试 registerFunction
     */
    public static String customToUpper(String str) {
        return str != null ? str.toUpperCase() : null;
    }

    /**
     * 一个简单的POJO类，用于作为SpEL表达式的根对象
     */
    @Setter
    @Getter
    public static class TestUser {
        // Getters and Setters
        private String name;
        private Integer age;
        private BigDecimal salary;

    }
}