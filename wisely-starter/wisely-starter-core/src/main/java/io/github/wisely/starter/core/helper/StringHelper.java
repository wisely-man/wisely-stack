package io.github.wisely.starter.core.helper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.github.wisely.starter.core.data.getter.MapProxy;
import io.github.wisely.starter.core.data.helper.DataHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * org.apache.commons.lang3.StringUtils
 */
public class StringHelper extends StringUtils {

    /**
     * 字符串分割成Set<String>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的字符串Set集合
     */
    public static Set<String> splitToSet(String str, String pattern) {
        return splitToStream(str, pattern).collect(Collectors.toSet());
    }


    /**
     * 字符串分割成List<String>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的字符串List集合
     */
    public static List<String> splitToList(String str, String pattern) {
        if (isBlank(str)) {
            return Lists.newArrayList();
        }
        return Splitter.on(pattern).trimResults().omitEmptyStrings().splitToList(str);
    }

    /**
     * 字符串分割成Stream<String>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的字符串集合Stream
     */
    public static Stream<String> splitToStream(String str, String pattern) {
        if (isBlank(str)) {
            return Stream.empty();
        }
        return Splitter.on(pattern).splitToStream(str);
    }


    /**
     * 字符串分割成Set<Integer>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的Integer类型的Set集合
     */
    public static Set<Integer> splitToIntSet(String str, String pattern) {
        return splitToIntStream(str, pattern).collect(Collectors.toSet());
    }


    /**
     * 字符串分割成List<Integer>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的Integer类型的List集合
     */
    public static List<Integer> splitToIntList(String str, String pattern) {
        if (isBlank(str)) {
            return Lists.newArrayList();
        }
        return Splitter.on(pattern).trimResults().omitEmptyStrings()
                .splitToList(str).stream()
                .map(s -> DataHelper.getInt(s, 0))
                .collect(Collectors.toList());
    }

    /**
     * 字符串分割成Stream<Integer>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的Integer类型的Stream
     */
    public static Stream<Integer> splitToIntStream(String str, String pattern) {
        if (isBlank(str)) {
            return Stream.empty();
        }
        return Splitter.on(pattern).splitToStream(str).map(s -> DataHelper.getInt(s, 0)).toList().stream();
    }


    /**
     * 字符串分割成Set<Long>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的Long类型的Set集合
     */
    public static Set<Long> splitToLongSet(String str, String pattern) {
        return splitToLongStream(str, pattern).collect(Collectors.toSet());
    }


    /**
     * 字符串分割成List<Long>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的Long类型的List集合
     */
    public static List<Long> splitToLongList(String str, String pattern) {
        if (isBlank(str)) {
            return Lists.newArrayList();
        }
        return Splitter.on(pattern).trimResults().omitEmptyStrings()
                .splitToList(str).stream()
                .map(s -> DataHelper.getLong(s, 0L))
                .collect(Collectors.toList());
    }

    /**
     * 字符串分割成Stream<Long>
     *
     * @param str     源字符串
     * @param pattern 分隔符
     * @return 被分割后的Long类型的Stream
     */
    public static Stream<Long> splitToLongStream(String str, String pattern) {
        if (isBlank(str)) {
            return Stream.empty();
        }
        return Splitter.on(pattern).splitToStream(str)
                .map(s -> DataHelper.getLong(s, 0L))
                .toList()
                .stream();
    }


    /**
     * 替换占位符${?}为指定内容
     *
     * @param source 源字符串
     * @param params 替换参数
     * @return 替换后的字符串
     */
    public static String evaluate(String source, Map<String, Object> params) {

        if (isBlank(source)) {
            return source;
        }
        Matcher matcher = RegexHelper.placeholder.matcher(source);
        MapProxy<String, Object> mapProxy = MapProxy.proxy(params);
        while (matcher.find()) {
            source = source.replace(matcher.group(0), mapProxy.getString(matcher.group(1)));
        }
        return source;
    }

    /**
     * 模板参数替换
     *
     * @param data   需要替换的字符串
     * @param params 替换参数
     * @return 替换后的字符串
     */
    public static String templateVariableReplace(String data, Map<String, String> params) {

        if (isBlank(data) || ValidHelper.isEmpty(params)) {
            return data;
        }

        MapProxy<String, String> mapProxy = MapProxy.proxy(params);
        for (String key : mapProxy.keySet()) {
            data = Strings.CS.replace(data, "${" + key + "}", mapProxy.getString(key));
        }
        return data;
    }


    /**
     * 消息格式化
     *
     * @param message  消息模板
     * @param patterns 替换参数
     * @return 格式化后的消息
     */
    public static String format(String message, Object... patterns) {
        String[] arr = new String[0];
        if (ValidHelper.isNotEmpty(patterns)) {
            arr = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                arr[i] = DataHelper.getString(patterns[i], ""); // null数据处理
            }
        }
        return MessageFormat.format(message, (Object[]) arr);
    }

    /**
     * 字符串不相等比较
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return boolean
     */
    public static boolean notEquals(CharSequence cs1, CharSequence cs2) {
        return !Strings.CS.equals(cs1, cs2);
    }

    /**
     * 字符串不相等比较 (忽略大小写)
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return boolean
     */
    public static boolean notEqualsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return !Strings.CI.equals(cs1, cs2);
    }

    /**
     * 字符串不包含指定内容
     *
     * @param target 目标数据
     * @param search 匹配内容
     * @return boolean
     */
    public static boolean notContains(CharSequence target, CharSequence search) {
        return !Strings.CS.contains(target, search);
    }

    /**
     * 字符串不包含指定内容 (忽略大小写)
     *
     * @param target 目标数据
     * @param search 匹配内容
     * @return boolean
     */
    public static boolean notContainsIgnoreCase(CharSequence target, CharSequence search) {
        return !Strings.CI.contains(target, search);
    }


    /**
     * 过滤blank元素并拼接
     *
     * @param separator 分隔符
     * @param values    内容
     * @return 拼接后的字符串
     */
    public static String joinSkipBlank(final String separator, String... values) {
        return Joiner.on(separator).skipNulls().join(values);
    }


    /**
     * 过滤blank元素并拼接
     *
     * @param values    集合
     * @param separator 分隔符
     * @return 拼接后的字符串
     */
    public static String joinSkipBlank(Collection<String> values, final String separator) {
        return Joiner.on(separator).skipNulls().join(values);
    }


    /**
     * 将字符串编码为Unicode形式。该方法会将字符串中的每个字符转换成对应的Unicode编码，
     * 并以字符串形式进行返回。
     *
     * @param str 被编码的字符串。如果输入为空（null或空字符串），则直接返回原字符串。
     * @return Unicode字符串。返回的字符串中，每个字符都会被替换为其Unicode编码的形式，
     * 形如"\\uXXXX"，其中XXXX是字符的16进制Unicode编码。
     */
    public static String stringToUnicode(String str) {
        // 检查输入字符串是否为空，若为空则直接返回原字符串
        if (isEmpty(str)) {
            return str;
        }

        // 初始化StringBuilder以存储转换后的Unicode字符串
        final int len = str.length(); // 获取输入字符串的长度
        final StringBuilder unicode = new StringBuilder(str.length() * 6); // 预估容量，每个字符编码后可能占用6个字符长度
        char c;
        // 遍历输入字符串中的每个字符，进行Unicode编码转换
        for (int i = 0; i < len; i++) {
            c = str.charAt(i); // 获取当前字符
            unicode.append(toUnicodeHex(c)); // 将当前字符转换为Unicode编码，并追加到StringBuilder中
        }
        return unicode.toString(); // 返回转换后的Unicode字符串
    }


    public static char[] ALPHABETS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 将字符转换为Unicode十六进制字符串。
     *
     * @param ch 要转换的字符。
     * @return 转换后的Unicode十六进制字符串，以反斜杠u开头，后面跟着四位十六进制数。
     */
    public static String toUnicodeHex(char ch) {
        // 通过位移和与操作，将字符转换为四位十六进制数，并使用alphabets数组转换为字符
        return "\\u" +//
                ALPHABETS[(ch >> 12) & 15] +// 获取字符的第12到第15位
                ALPHABETS[(ch >> 8) & 15] +// 获取字符的第8到第11位
                ALPHABETS[(ch >> 4) & 15] +// 获取字符的第4到第7位
                ALPHABETS[(ch) & 15]; // 获取字符的第0到第3位
    }

    /**
     * Unicode字符串转为普通字符串<br>
     * Unicode字符串的表现方式为：\\uXXXX
     *
     * @param unicode Unicode字符串
     * @return 普通字符串
     */
    public static String unicodeToString(String unicode) {
        if (isBlank(unicode)) {
            return unicode;
        }

        final int len = unicode.length();
        StringBuilder sb = new StringBuilder(len);
        int i;
        int pos = 0;
        while ((i = Strings.CI.indexOf(unicode, "\\u", pos)) != -1) {
            sb.append(unicode, pos, i);//写入Unicode符之前的部分
            pos = i;
            if (i + 5 < len) {
                char c;
                try {
                    c = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
                    sb.append(c);
                    pos = i + 6;//跳过整个Unicode符
                } catch (NumberFormatException e) {
                    //非法Unicode符，跳过
                    sb.append(unicode, pos, i + 2);//写入"\\u"
                    pos = i + 2;
                }
            } else {
                //非Unicode符，结束
                break;
            }
        }

        if (pos < len) {
            sb.append(unicode, pos, len);
        }
        return sb.toString();
    }


    /**
     * 判断字符串（trim 后）长度是否在指定范围内
     *
     * @param text 字符串
     * @param min  最小长度（包含）
     * @param max  最大长度（包含）
     * @return true if length in [min, max]
     */
    public static boolean isLength(String text, int min, int max) {
        if (text == null) {
            return false;
        }
        String trimmed = StringHelper.trim(text);
        if (trimmed == null) {
            return false;
        }
        int len = trimmed.length();
        return len >= min && len <= max;
    }

    private StringHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

}
