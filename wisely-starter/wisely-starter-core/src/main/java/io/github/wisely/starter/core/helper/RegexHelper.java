package io.github.wisely.starter.core.helper;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * RegexHelper
 * 正则工具类
 */
@UtilityClass
public class RegexHelper {

    // Empty
    public static final Pattern emptyRegex = Pattern.compile("");
    // 数字
    public static final Pattern numberRegex = Pattern.compile("^-?[0-9]+$");
    // money
    public static final Pattern moneyRegex = Pattern.compile("^[1-9][0-9]*(\\.[0-9]{1,2})?$");

    // 字母
    public static final Pattern onlyLetterRegex = Pattern.compile("^[a-zA-Z]+$");
    // 包含数字以及横线
    public static final Pattern onlyCharAndNumberRegex = Pattern.compile("^[a-zA-Z0-9]+([-_][a-zA-Z0-9]+)*$");
    // 同上在加中文
    public static final Pattern onlyCharAndNumberAndChRegex = Pattern.compile("^[a-zA-Z0-9一-龥\\s]+([-_][a-zA-Z0-9一-龥\\s]+)*$");
    // 邮箱
    public static final Pattern emailRegex = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.[a-z]+([-.][a-z]+)*$");
    // 手机号
    public static final Pattern mobileRegex = Pattern.compile("^1\\d{10}$", Pattern.CASE_INSENSITIVE);
    // 图片后缀
    public static final Pattern imagePostfixRegex = Pattern.compile("\\.(?:GIF|JPG|JPEG|BMP|PNG)$", Pattern.CASE_INSENSITIVE);
    // 网址
    public static final Pattern urlRegex = Pattern.compile("^http://([\\w-]+(\\.[\\w-/]+)+)+$");
    // 标签
    public static final Pattern tagRegex = Pattern.compile("^\\s*(([a-z0-9A-Z\\u4e00-\\u9fa5]{2,8})\\s+){0,2}([a-z0-9A-Z\\u4e00-\\u9fa5]{2,8}\\s*)?$");
    // 统一社会信用代码，即企业代码
    public static final Pattern UnifiedSocialCreditCodeRegex = Pattern.compile("^[A-Z0-9]{2}[0-9]{6}[A-Z0-9]{10}$");
    // 身份证号 尾号如果是x必须大写，前端处理
    public static final Pattern idCardRegex = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|Xx)$");
    // 邮编 6位数字
    public static final Pattern postCodeRegex = Pattern.compile("^\\d{6}$");
    // 行政区划代码
    //    县级以上行政区划代码由6位阿拉伯数字组成
    //    第一位数字：1～8
    //    第二位数字：1~7
    //    第三到六位数字：0~9
    public static final Pattern areaNoRegex = Pattern.compile("^[1-8][1-7]\\d{4}$");
    // 经度  -180.0～+180.0（整数部分为0～180，必须输入1到5位小数）
    public static final Pattern longitudeRegex = Pattern.compile("^([1][3-9]|[2-9]\\d{1}|1[0-2][0-9]|13[0-5])(\\.\\d{1,6})?$");
    // 纬度  -90.0～+90.0（整数部分为0～90，必须输入1到5位小数）
    public static final Pattern latitudeRegex = Pattern.compile("^([3-9]|[1-4][0-9]|5[0-3])(\\.\\d{1,6})?$");

    // 中文
    public static Pattern chineseRegex = Pattern.compile("[一-龥]+");
    // URL 图片缩放尺寸
    public static Pattern urlWidth = Pattern.compile(".+!(\\d+)$");
    public static Pattern urlHeight = Pattern.compile(".+!h(\\d+)$");
    public static Pattern urlWidthAndHeight = Pattern.compile(".+!(\\d+)x(\\d+)$");
    // 占位符
    public static Pattern placeholder = Pattern.compile("\\$\\{([^{}]+)}");


    /**
     * 正则校验
     *
     * @param regex 正则表达式
     * @param str   待校验字符串
     * @return true 符合 false 不符合
     */
    public static boolean matches(String regex, String str) {
        return Pattern.matches(regex, str);
    }


    /**
     * <b>功能：</b>验证字符串str是否只包含英文,数字和"-_"
     *
     * @param str 待验证字符串
     * @return true    是只包含英文,数字和"-_" false	不是只包含英文,数字和"-_"
     */
    public static boolean onlyCharAndNumber(String str) {
        return RegexHelper.onlyCharAndNumberRegex.matcher(str).find();
    }


    /**
     * <b>功能：</b>验证字符串str是否只包含英文,数字,中文,空格和"-_"
     *
     * @param str 待验证字符串
     * @return true    是只包含英文,数字,中文和"-_" false	不是只包含英文,数字,中文和"-_"
     */
    public static boolean onlyCharAndNumberAndCh(String str) {
        return RegexHelper.onlyCharAndNumberAndChRegex.matcher(str).find();
    }

    /**
     * 只包含字母
     *
     * @param str 待验证字符串
     * @return true    是只包含字母 false	不是只包含字母
     */
    public static boolean onlyLetter(String str) {
        return RegexHelper.onlyLetterRegex.matcher(str).find();
    }


    /**
     * <b>功能：</b>验证字符串email是否符合email格式
     *
     * @param email 待验证邮件地址
     * @return true    符合 false	不符合
     */
    public static boolean isEmail(String email) {
        return StringHelper.isNotBlank(email) && RegexHelper.emailRegex.matcher(email).find();
    }

    /**
     * <b>功能：</b>验证字符串url是否符合url格式
     *
     * @param url 待验证URL地址
     * @return true    符合 false	不符合
     */
    public static boolean isUrl(String url) {
        return StringHelper.isNotBlank(url) && RegexHelper.urlRegex.matcher(url).find();
    }


    /**
     * <b>功能：</b>验证字串是否手机号
     *
     * @param mobile 待验证手机号
     * @return true    符合 false	不符合
     */
    public static boolean isMobile(String mobile) {
        return StringHelper.isNotBlank(mobile) && RegexHelper.mobileRegex.matcher(mobile).find();
    }

    /**
     * <b>功能：</b>验证字串图片文件是否属于限定的某中类型格式
     *
     * @param filepath 待验证图片名称
     * @return true    符合 false	不符合
     */
    public static boolean isImageFile(String filepath) {
        return StringHelper.isNotBlank(filepath) && RegexHelper.imagePostfixRegex.matcher(filepath).find();
    }

    /**
     * <b>功能：</b>验证字符串number是否为整数
     *
     * @param number 待验证字符串
     * @return true    是整数 false	不是整数
     */
    public static boolean isNumber(String number) {
        return StringHelper.isNotBlank(number) && RegexHelper.numberRegex.matcher(number).find();
    }

    /**
     * <b>功能：</b>验证字符串tags是否符合 Tags标签 正确格式 (aaa ddd ccc)
     *
     * @param tags 待验证字符串
     * @return true|false 符合标签格式 true 否则false
     */
    public static boolean tags(String tags) {
        return StringHelper.isNotBlank(tags) && RegexHelper.tagRegex.matcher(tags).find();
    }

}
