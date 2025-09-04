package com.wisely.starter.core.data.helper;

import com.wisely.starter.core.data.eum.*;
import com.wisely.starter.core.data.eum.YearMonth;
import com.wisely.starter.core.exception.eum.CommonExceptionEnum;
import com.wisely.starter.core.helper.StringHelper;
import com.wisely.starter.core.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * org.apache.commons.lang3.time.DateUtils
 * 基础上扩展 LocalDateTime
 */
@Slf4j
public class DateHelper extends DateUtils {

    private final static ConcurrentHashMap<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap<>();


    private static DateTimeFormatter getFormatter(String pattern) {
        if (!FORMATTER_MAP.containsKey(pattern)) {
            FORMATTER_MAP.putIfAbsent(pattern, DateTimeFormatter.ofPattern(pattern));
        }
        return FORMATTER_MAP.get(pattern);
    }


    /**
     * Date转换为LocalDateTime
     *
     * @param date Date类型对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        if (ValidHelper.isNull(date)) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time LocalDateTime类型日期对象
     * @return Date对象
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        if (ValidHelper.isNull(time)) {
            return null;
        }
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取指定日期的毫秒
     *
     * @param time LocalDateTime类型日期对象
     * @return 日期对象的毫秒数
     */
    public static Long getMilliByTime(LocalDateTime time) {
        if (ValidHelper.isNull(time)) {
            return 0L;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time LocalDateTime类型日期对象
     * @return 日期对象的秒数
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        if (ValidHelper.isNull(time)) {
            return null;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time LocalDateTime类型日期对象
     * @return 格式化后的时间字符串
     */
    public static String format(LocalDateTime time) {
        return format(time, Common.DATETIME.getPattern());
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time    LocalDateTime类型日期对象
     * @param pattern 格式化字符串
     * @return 格式化后的时间字符串
     */
    public static String format(LocalDateTime time, String pattern) {
        if (ValidHelper.isNull(time) || StringHelper.isBlank(pattern)) {
            return null;
        }
        return time.format(getFormatter(pattern));
    }


    /**
     * 获取指定时间的指定格式
     * 默认的时间格式化 yyyy-MM-dd HH:mm:ss
     *
     * @param time Date类型日期对象
     * @return 格式化后的时间字符串
     */
    public static String format(Date time) {
        return format(time, Common.DATETIME.getPattern());
    }


    /**
     * 获取指定时间的指定格式
     *
     * @param time    Date类型日期对象
     * @param pattern 格式化字符串
     * @return 格式化后的时间字符串
     */
    public static String format(Date time, String pattern) {
        if (ValidHelper.isNull(time) || StringHelper.isBlank(pattern)) {
            return null;
        }

        LocalDateTime localDateTime = convertDateToLDT(time);
        return localDateTime == null ? null : localDateTime.format(getFormatter(pattern));
    }


    /**
     * 日期格式字符串转换为其他格式
     *
     * @param timeStr 时间字符串
     * @param pattern 格式化字符串
     * @return 格式化后的时间字符串
     */
    public static String format(String timeStr, String pattern) {
        return format(getDate(timeStr), pattern);
    }

    /**
     * 获取当前时间的指定格式
     * yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的指定格式字符串
     */
    public static String formatNow() {
        return formatNow(Common.DATETIME.getPattern());
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern 格式化字符串
     * @return 当前时间的指定格式字符串
     */
    public static String formatNow(String pattern) {
        if (ValidHelper.isNull(pattern)) {
            return null;
        }
        return format(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值
     *
     * @param time   LocalDateTime类型日期对象
     * @param number 单位数量
     * @param field  ChronoUnit.*
     * @return 计算后的日期对象
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        if (ValidHelper.isNull(time) || ValidHelper.isNull(field)) {
            return null;
        }
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值
     *
     * @param time   LocalDateTime类型日期对象
     * @param number 单位数量
     * @param field  ChronoUnit.*
     * @return 计算后的日期对象
     */
    public static LocalDateTime minus(LocalDateTime time, long number, TemporalUnit field) {
        if (ValidHelper.isNull(time) || ValidHelper.isNull(field)) {
            return null;
        }
        return time.minus(number, field);
    }


    /**
     * 获取两个日期的差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param field     ChronoUnit.* 单位(年月日时分秒)
     * @return 开始/结束时间相隔的指定单位数量
     */
    public static long between(Date startTime, Date endTime, ChronoUnit field) {
        return between(convertDateToLDT(startTime), convertDateToLDT(endTime), field);
    }

    /**
     * 获取两个日期的差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param field     ChronoUnit.* 单位(年月日时分秒)
     * @return 开始/结束时间相隔的指定单位数量
     */
    public static long between(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {

        if (ValidHelper.isNull(startTime) || ValidHelper.isNull(endTime) || ValidHelper.isNull(field)) {
            return 0L;
        }

        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12L + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    private final static String[] FORMAT_PATTERNS = new String[]{

            // —————————— 常见完整时间（优先级最高）——————————

            Common.DATETIME.getPattern(),            // 0 标准格式，最常见
            Common.DATETIME_SLASH.getPattern(),      // 1 斜杠分隔（常见于日志、Windows系统）
            Common.DATETIME_MIN.getPattern(),        // 2 常见（无秒）
            Common.DATETIME_SLASH_MIN.getPattern(),  // 3 斜杠年月日时分秒
            Common.DATE.getPattern(),                // 4 仅日期，非常常见
            Common.DATE_SLASH.getPattern(),          // 5

            // —————————— 紧凑格式（无分隔符）——————————

            Compact.DATETIME.getPattern(),            // 6 常用于文件名、交易流水
            Compact.DATETIME_MIN.getPattern(),        // 7 日期事件（到分钟）
            DateOnly.COMPACT.getPattern(),            // 8 常见（如统计日期）

            // —————————— 带毫秒的时间（放后面，避免干扰无毫秒格式）——————————
            Common.DATETIME_MILLIS.getPattern(),      // 9 毫秒（标准）
            Common.DATETIME_SLASH_MILLIS.getPattern(),// 10 毫秒（斜杠）
            Compact.DATETIME_MILLIS.getPattern(),     // 11 紧凑带毫秒

            // —————————— ISO8601 / RFC3339 标准格式（含T/Z）——————————
            ISO8601.BASIC.getPattern(),               // 12 ISO 标准（如JSON输出）
            ISO8601.MS.getPattern(),                  // 13 ISO 带毫秒
            ISO8601.Z.getPattern(),                   // 14 带时区偏移（如 +0800）
            ISO8601.FULL.getPattern(),                // 15 推荐的 ISO8601 完整格式

            // —————————— 年月格式 ——————————
            YearMonth.DASH.getPattern(),              // 16
            YearMonth.SLASH.getPattern(),             // 19
            YearMonth.COMPACT.getPattern(),           // 20

            // —————————— 仅时间格式（必须放最后！避免 "1230" 被误认为 HHmm）——————————
            TimeOnly.COLON.getPattern(),              // 21
            TimeOnly.COMPACT.getPattern(),            // 22
            TimeOnly.MINUTE.getPattern(),             // 23
    };


    /**
     * 获取日期对象
     *
     * @param value    对象
     * @param patterns 格式化字符串
     * @return Date对象
     */
    public static Date getDate(Object value, String... patterns) {

        if (ValidHelper.isNull(value)) {
            return null;
        }

        switch (value) {
            case Date date -> {
                return date;
            }
            case LocalDate localDate -> {
                return convertLDTToDate(localDate.atStartOfDay());
            }
            case LocalTime localTime -> {
                return convertLDTToDate(localTime.atDate(LocalDate.now()));
            }
            case LocalDateTime localDateTime -> {
                return convertLDTToDate(localDateTime);
            }
            case String s when ValidHelper.isNotEmpty(patterns) -> {
                try {
                    return parseDate(s, patterns);
                } catch (ParseException e) {
                    log.error("LocalDateTimeHelper.getDate error:", e);
                }
            }
            default -> {
            }
        }

        return null;
    }


    /**
     * obj转日期
     *
     * @param value 对象
     * @return Date对象
     */
    public static Date getDate(Object value) {
        return getDate(value, FORMAT_PATTERNS);
    }


    /**
     * 获取日期对象
     *
     * @param value 对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime getLocalDateTime(Object value) {
        return getLocalDateTime(value, FORMAT_PATTERNS);
    }

    /**
     * 获取日期对象
     *
     * @param value 对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime getLocalDateTime(Object value, String... pattern) {

        if (ValidHelper.isNull(value)) {
            return null;
        }

        switch (value) {
            case LocalDate localDate -> {
                return localDate.atStartOfDay();
            }
            case LocalTime localTime -> {
                return localTime.atDate(LocalDate.now());
            }
            case LocalDateTime localDateTime -> {
                return localDateTime;
            }
            case Date date -> {
                return convertDateToLDT(date);
            }
            default -> {
                return convertDateToLDT(getDate(value, pattern));
            }
        }
    }


    /**
     * 验证字符串date是否符合Date格式
     *
     * @param date 待验证日期
     * @return true-符合 false-不符合
     */
    public static boolean isDate(Object date) {
        if (date instanceof Date) {
            return true;
        }
        return getDate(date) != null;
    }


    /**
     * 获取当前年度第一天
     *
     * @return LocalDateTime对象，本年度第一天
     */
    public static LocalDateTime yearStart() {
        return yearStart(LocalDateTime.now());
    }

    /**
     * 获取指定时间年度第一天
     *
     * @param localDateTime 指定时间
     * @return LocalDateTime对象,指定时间的年度第一天
     */
    public static LocalDateTime yearStart(LocalDateTime localDateTime) {
        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(localDateTime, "localDateTime");
        return LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear())), LocalTime.MIN);
    }


    /**
     * 获取本年度结束时间
     *
     * @return LocalDateTime对象，本年度最后一天
     */
    public static LocalDateTime yearEnd() {
        return yearEnd(LocalDateTime.now());
    }

    /**
     * 获取指定时间年度结束时间
     *
     * @param localDateTime 指定时间
     * @return LocalDateTime对象,指定时间的年度最后一天
     */
    public static LocalDateTime yearEnd(LocalDateTime localDateTime) {
        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(localDateTime, "localDateTime");
        return LocalDateTime.of(LocalDate.from(localDateTime.with(TemporalAdjusters.lastDayOfYear())), LocalTime.MAX);
    }

    /**
     * 获取指定日期所在季度
     *
     * @param localDateTime 指定时间
     * @return 指定时间的季度 1-4
     */
    public static Integer quarter(LocalDateTime localDateTime) {
        return localDateTime == null ? null : ((localDateTime.getMonthValue() - 1) / 3 + 3) % 4 + 1;
    }

    /**
     * 获取本季度开始时间
     *
     * @return 本季度的开始时间
     */
    public static LocalDateTime quarterStart() {
        return quarterStart(LocalDateTime.now());
    }

    /**
     * 获取指定季度开始时间
     *
     * @param localDateTime 指定时间
     * @return 指定时间的季度开始时间
     */
    public static LocalDateTime quarterStart(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        Month month = localDateTime.getMonth();
        return LocalDateTime.of(LocalDate.of(localDateTime.getYear(), month.firstMonthOfQuarter(), 1), LocalTime.MIN);
    }


    /**
     * 获取本季度结束时间
     *
     * @return 本季度的结束时间
     */
    public static LocalDateTime quarterEnd() {
        return quarterEnd(LocalDateTime.now());
    }

    /**
     * 获取指定季度结束时间
     *
     * @param localDateTime 指定时间
     * @return 指定时间的季度结束时间
     */
    public static LocalDateTime quarterEnd(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        Month endMonthOfQuarter = localDateTime.getMonth().firstMonthOfQuarter().plus(2);
        LocalDate localDate = LocalDate.of(localDateTime.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(localDateTime.toLocalDate().isLeapYear()));
        return LocalDateTime.of(localDate, LocalTime.MAX);
    }

    /**
     * 获取本月第一天
     *
     * @return 本月第一天
     */
    public static LocalDateTime monthStart() {
        return monthStart(LocalDateTime.now());
    }

    /**
     * 获取指定月份第一天
     *
     * @param localDateTime 指定时间
     * @return 指定月份第一天
     */
    public static LocalDateTime monthStart(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(LocalDate.from(localDateTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
    }

    /**
     * 当前月份最后一天
     *
     * @return 本月最后一天
     */
    public static LocalDateTime monthEnd() {
        return monthEnd(LocalDateTime.now());
    }

    /**
     * 获取指定月份最后一天
     *
     * @param localDateTime 指定时间
     * @return 指定月份最后一天
     */
    public static LocalDateTime monthEnd(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(LocalDate.from(localDateTime.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);
    }


    /**
     * 获取指定周开始时间
     *
     * @return 本周第一天
     */
    public static LocalDateTime weekStart() {
        return weekStart(LocalDateTime.now());
    }

    /**
     * 获取指定周开始时间
     *
     * @param localDateTime 指定时间
     * @return 指定周第一天
     */
    public static LocalDateTime weekStart(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN).with(DayOfWeek.MONDAY);
    }


    /**
     * 获取本周结束时间
     *
     * @return 本周最后一天
     */
    public static LocalDateTime weekEnd() {
        return weekEnd(LocalDateTime.now());
    }

    /**
     * 获取指定周结束时间
     *
     * @param localDateTime 指定时间
     * @return 指定周最后一天
     */
    public static LocalDateTime weekEnd(LocalDateTime localDateTime) {

        if (localDateTime == null) {
            return null;
        }

        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX).with(DayOfWeek.SUNDAY);
    }

    /**
     * 获取本周几对应的日期
     *
     * @param dayOfWeek DayOfWeek.MONDAY
     *                  DayOfWeek.SUNDAY
     * @return 本周几对应的日期
     */
    public static LocalDateTime weekDay(DayOfWeek dayOfWeek) {
        return weekDay(LocalDateTime.now(), dayOfWeek);
    }

    /**
     * 获取指定日期的周几日期
     *
     * @param localDateTime 指定时间
     * @param dayOfWeek     DayOfWeek.MONDAY
     *                      DayOfWeek.SUNDAY
     * @return 指定日期所在周的周几日期
     */
    public static LocalDateTime weekDay(LocalDateTime localDateTime, DayOfWeek dayOfWeek) {
        return localDateTime == null ? null : localDateTime.with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }


    /**
     * 获取今天的开始
     *
     * @return 今天开始时间
     */
    public static LocalDateTime dayStart() {
        return dayStart(LocalDateTime.now());
    }

    /**
     * 获取一天的开始
     *
     * @param localDateTime 指定时间
     * @return 一天的开始
     */
    public static LocalDateTime dayStart(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }


    /**
     * 获取一天的开始（字符串）
     *
     * @param dateObj 日期对象
     * @param pattern 格式化字符串
     * @return 指定天开始时间的字符串
     */
    public static String dayStart(Object dateObj, String pattern) {
        return ValidHelper.isNull(dateObj) ? null : dayStart(getLocalDateTime(dateObj)).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取一天的结束
     *
     * @return 今天结束时间
     */
    public static LocalDateTime dayEnd() {
        return dayEnd(LocalDateTime.now());
    }

    /**
     * 获取一天的结束
     *
     * @param localDateTime 指定时间
     * @return 一天的结束
     */
    public static LocalDateTime dayEnd(LocalDateTime localDateTime) {
        return localDateTime == null ? null : LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }

    /**
     * 获取一天的结束（字符串）
     *
     * @param dateObj 日期对象
     * @param pattern 格式化字符串
     * @return 指定天结束时间的字符串
     */
    public static String dayEnd(Object dateObj, String pattern) {
        return ValidHelper.isNull(dateObj) ? null : dayEnd(getLocalDateTime(dateObj)).format(DateTimeFormatter.ofPattern(pattern));
    }

    private DateHelper() {
        throw new UnsupportedOperationException("DateHelper 不允许实例化");
    }
}

