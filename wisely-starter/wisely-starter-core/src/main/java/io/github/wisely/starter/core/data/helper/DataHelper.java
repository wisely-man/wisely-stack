package io.github.wisely.starter.core.data.helper;

import io.github.wisely.starter.core.helper.StringHelper;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * DataHelper
 * 数据处理工具类
 */
@Slf4j
public class DataHelper {

    /**
     * 百
     */
    public static BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * 千
     */
    public static BigDecimal THOUSAND = new BigDecimal("1000");

    /**
     * 万
     */
    public static BigDecimal TEN_THOUSAND = new BigDecimal("10000");


    /* ==================== 基础类型转换 ==================== */

    public static Boolean getBoolean(Object value, Boolean defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Boolean) return (Boolean) value;
        String str = value.toString().trim();
        if ("true".equalsIgnoreCase(str)) return Boolean.TRUE;
        if ("false".equalsIgnoreCase(str)) return Boolean.FALSE;
        if ("on".equalsIgnoreCase(str)) return Boolean.TRUE;
        if ("off".equalsIgnoreCase(str)) return Boolean.FALSE;
        return defaultValue;
    }

    public static Byte getByte(Object value, Byte defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.byteValue() : defaultValue;
    }

    public static Character getCharacter(Object value, Character defaultValue) {
        if (value == null) return defaultValue;
        String str = value.toString();
        return str.isEmpty() ? defaultValue : str.charAt(0);
    }

    public static Short getShort(Object value, Short defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.shortValue() : defaultValue;
    }

    public static Integer getInt(Object value, Integer defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.intValue() : defaultValue;
    }

    public static Long getLong(Object value, Long defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.longValue() : defaultValue;
    }

    public static Float getFloat(Object value, Float defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.floatValue() : defaultValue;
    }

    public static Double getDouble(Object value, Double defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.doubleValue() : defaultValue;
    }

    public static BigInteger getBigInt(Object value, BigInteger defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd.toBigInteger() : defaultValue;
    }

    public static BigDecimal getBigDecimal(Object value, BigDecimal defaultValue) {
        BigDecimal bd = toBigDecimal(value);
        return bd != null ? bd : defaultValue;
    }

    public static String getString(Object value, String defaultValue) {
        return value == null ? defaultValue : value.toString();
    }

    /* ==================== 集合类型转换 ==================== */

    public static HashSet<?> getHashSet(Object value) {
        if (value instanceof Collection) {
            return new HashSet<>((Collection<?>) value);
        }
        return new HashSet<>();
    }

    public static TreeSet<?> getTreeSet(Object value) {
        if (value instanceof Collection) {
            return new TreeSet<>((Collection<?>) value);
        }
        return new TreeSet<>();
    }

    public static ArrayList<?> getArrayList(Object value) {
        if (value instanceof Collection) {
            return new ArrayList<>((Collection<?>) value);
        }
        return new ArrayList<>();
    }

    public static LinkedList<?> getLinkedList(Object value) {
        if (value instanceof Collection) {
            return new LinkedList<>((Collection<?>) value);
        }
        return new LinkedList<>();
    }

    /* ==================== 内部工具方法 ==================== */

    /**
     * 把任意对象安全地转换成 BigDecimal。
     * <p>
     * 支持：Number、String（可带千分位）。
     * <p>
     * 若需支持本地化/自定义格式，可在此扩展 DecimalFormat。
     */

    private static BigDecimal toBigDecimal(Object value) {

        switch (value) {
            case null -> {
                return null;
            }
            case BigDecimal bigDecimal -> {
                return bigDecimal;
            }
            case Number number -> {
                return new BigDecimal(number.toString());
            }
            case String s -> {
                String str = s.trim();
                if (str.isEmpty()) {
                    return null;
                }

                /* 普通解析优先（性能更好） */
                try {
                    return new BigDecimal(str);
                } catch (NumberFormatException ignore) {
                    /* 尝试用 DecimalFormat 解析带千分位等格式 */
                }

                try {
                    DecimalFormat df = new DecimalFormat();
                    df.setParseBigDecimal(true);
                    return (BigDecimal) df.parse(str);
                } catch (ParseException ex) {
                    return null;
                }
            }
            default -> {
            }
        }
        return null;
    }


    /**
     * BigDecimal 相加
     *
     * @param b1 加数1
     * @param b2 加数2
     * @return 和
     */
    public static BigDecimal add(Object b1, Object b2) {
        return getBigDecimal(b1, BigDecimal.ZERO).add(getBigDecimal(b2, BigDecimal.ZERO));
    }

    /**
     * BigDecimal 相减
     *
     * @param minuend    被减数
     * @param subtrahend 减数
     * @return 差
     */
    public static BigDecimal subtract(Object minuend, Object subtrahend) {
        return getBigDecimal(subtrahend, BigDecimal.ZERO).subtract(getBigDecimal(minuend, BigDecimal.ZERO));
    }

    /**
     * BigDecimal 乘法
     *
     * @param b1 因数1
     * @param b2 因数2
     * @return 乘积
     */
    public static BigDecimal multiply(Object b1, Object b2) {
        return getBigDecimal(b1, BigDecimal.ZERO).multiply(getBigDecimal(b2, BigDecimal.ZERO));
    }

    /**
     * dividend / divisor
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    小数位精度
     * @return 商
     */
    public static BigDecimal divide(Object dividend, Object divisor, int scale) {

        BigDecimal dividendBig = getBigDecimal(dividend, BigDecimal.ZERO);
        BigDecimal divisorBig = getBigDecimal(divisor, BigDecimal.ZERO);

        if (dividendBig.compareTo(BigDecimal.ZERO) < 1 || divisorBig.compareTo(BigDecimal.ZERO) < 1) {
            return BigDecimal.ZERO;
        } else {
            return dividendBig.divide(divisorBig, scale, RoundingMode.HALF_UP);
        }
    }

    /**
     * BigDecimal 百分比
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 百分比
     */
    public static BigDecimal percent(Object numerator, Object denominator) {
        return divide(getBigDecimal(numerator, BigDecimal.ZERO).multiply(HUNDRED), denominator, 2);
    }

    /**
     * 数值格式化
     *
     * @param num 数值
     * @return 格式化后的字符串
     */
    public static String getDecimalString(Object num) {
        return format(num, "#,###.0", Locale.getDefault());
    }


    /**
     * 使用指定模式和 Locale 格式化数字
     *
     * @param number  数字
     * @param pattern 模式
     * @param locale  地区
     * @return 格式化字符串
     */
    public static String format(Object number, String pattern, Locale locale) {
        BigDecimal t = toBigDecimal(number);
        DecimalFormat df = new DecimalFormat(pattern);
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(locale));
        return df.format(t);
    }


    /**
     * 解析格式化字符串为数字（支持千分位、自定义模式）
     *
     * @param text    字符串
     * @param pattern 对应的格式模式
     * @return BigDecimal 精确值，失败返回 null
     */
    public static BigDecimal parse(String text, String pattern) {
        return parse(text, pattern, Locale.getDefault());
    }

    /**
     * 解析格式化字符串为数字（支持千分位、自定义模式），指定 Locale
     *
     * @param text    字符串
     * @param pattern 格式模式
     * @param locale  地区
     * @return BigDecimal，失败返回 null
     */
    public static BigDecimal parse(String text, String pattern, Locale locale) {
        if (text == null || text.trim().isEmpty() || pattern == null || pattern.isEmpty()) {
            return null;
        }

        try {
            DecimalFormat df = new DecimalFormat(pattern);
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(locale));
            // 解析为 Number，再转 BigDecimal
            Number number = df.parse(text.trim());
            return new BigDecimal(number.toString());
        } catch (ParseException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * 使用默认数字格式解析（自动识别千分位、Locale 相关格式）
     *
     * @param text   字符串
     * @param locale 地区
     * @return BigDecimal，失败返回 null
     */
    public static BigDecimal parseGeneral(String text, Locale locale) {

        if (StringHelper.isBlank(text)) {
            return null;
        }

        try {
            NumberFormat nf = NumberFormat.getInstance(locale);
            Number number = nf.parse(text.trim());
            return new BigDecimal(number.toString());
        } catch (ParseException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * 使用货币格式格式化
     *
     * @param number 数字
     * @param locale 地区（如 Locale.CHINA）
     * @return 货币格式字符串，如 "¥1,234.56"
     */
    @Nonnull
    public static String formatCurrency(Object number, Locale locale) {
        if (number == null || locale == null) {
            return "";
        }

        NumberFormat cf = NumberFormat.getCurrencyInstance(locale);

        return numberFormat(number, cf);
    }


    /**
     * 使用百分比格式格式化（自动乘以 100）
     *
     * @param number         数字（0.85 表示 85%）
     * @param fractionDigits 小数位数
     * @return 百分比格式字符串，如 "85.0%"
     */
    @Nonnull
    public static String formatPercent(Object number, int fractionDigits) {

        if (number == null) {
            return "";
        }

        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumFractionDigits(fractionDigits);
        pf.setMaximumFractionDigits(fractionDigits);

        return numberFormat(number, pf);
    }


    /**
     * 使用指定的 NumberFormat 格式化
     *
     * @param number       数字
     * @param numberFormat NumberFormat 实例
     * @return 格式化字符串
     */
    @Nonnull
    private static String numberFormat(Object number, NumberFormat numberFormat) {
        if (number instanceof Number n) {
            return numberFormat.format(n);
        }

        String str = number.toString().trim();
        if (str.isEmpty()) {
            return "";
        }

        try {
            BigDecimal bd = new BigDecimal(str);
            return numberFormat.format(bd);
        } catch (NumberFormatException e) {
            return "";
        }
    }


    private DataHelper() {
        throw new UnsupportedOperationException("DataHelper cannot be instantiated");
    }
}
