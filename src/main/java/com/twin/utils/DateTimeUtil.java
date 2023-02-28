package com.twin.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtil {
    public final static String PATTERN_LONG = "yyyy-MM-dd HH:mm:ss";

    public final static String PATTERN_LONG2 = "yyyyMMdd HH:mm:ss";

    public final static String PATTERN_A = "yyyyMM";

    public final static String PATTERN_B = "yyyyMMdd";

    public final static String PATTERN_C = "yyyy-MM-dd";

    public static final String PATTERN_D = "yyMMdd";

    public final static String PATTERN_ZH = "yyyy年MM月dd日";

    public final static String PATTERN_ZH_MD = "MM月dd日";

    public final static String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    private final static SimpleDateFormat DEFAULT_FORMAT = new SimpleDateFormat(PATTERN_B);

    private static final ThreadLocal<Calendar> DATE_LOCAL = new ThreadLocal<Calendar>();

    static {
        DATE_LOCAL.set(Calendar.getInstance());
    }

    public static String formatDateDefault(Date date) {
        return DEFAULT_FORMAT.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date    需要格式化的 日期对象
     * @param pattern 日期样式
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date 需要格式化的 日期对象
     */
    public static String formatDate(Date date) {
        return formatDate(date, PATTERN_LONG);
    }

    public static String formatDate() {
        return formatDate(new Date(), PATTERN_LONG);
    }

    /**
     * 格式化日期
     *
     * @param day     相对当前时间的日期偏移,-1:日期向前偏移一天/1:日期向后偏移一天
     * @param pattern 日期样式
     */
    public static String formatDate(int day, String pattern) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        return formatDate(c.getTime(), pattern);
    }

    /**
     * 计算相对偏移日期午夜的秒数
     *
     * @param day 相对当前时间的日期偏移,-1:日期向前偏移一天/1:日期向后偏移一天
     */
    public static int getExpiration(int day) {
        Calendar c = Calendar.getInstance();
        long midnight = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_MONTH, day);
        return (int) ((c.getTimeInMillis() - midnight) / 1000);
    }

    /**
     * 返回日期年月日整型，如20140318
     */
    public static int getYearMonthDay(Calendar calendar) {
        return calendar.get(Calendar.YEAR) * 10000
                + (calendar.get(Calendar.MONTH) + 1) * 100
                + calendar.get(Calendar.DATE);
    }

    /**
     * 获取日期的年期，如201401， addPeriodCount月份偏移量
     */
    public static int getYearPeriod(Date date, int addPeriodCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addPeriodCount);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        return y * 100 + m;
    }

    /**
     * 匹配字符串首次出现的yyyy-mm-dd hh:mm:ss格式的时间
     *
     * @param str
     * @return
     */
    public static String getTime(String str) {
        String reg = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        Pattern MobilePhonePattern = Pattern.compile(reg);
        Matcher m = MobilePhonePattern.matcher(str);
        while (m.find()) {
            return m.group().trim();
        }
        return null;
    }

    /**
     * 把字符串按格式转换成Date对象
     *
     * @param text
     * @param pattern
     * @return
     */
    public static Date parse(String text, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 默认用yyyy-MM-dd HH:mm:ss的格式进行转换
     *
     * @param text
     * @return
     */
    public static Date parse(String text) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_LONG);
        try {
            return format.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取 当前时间
     *
     * @return
     */
    @Deprecated
    public static Date getNow() {
        return DATE_LOCAL.get().getTime();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 判断日期是否为今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        Date now = getNow();
        return formatDate(now, PATTERN_B).equals(formatDate(date, PATTERN_B));
    }

    /**
     * 判断日期是否为昨天
     *
     * @param date
     * @return
     */
    public static boolean isYesterday(Date date) {
        if (date == null) {
            return false;
        }

        Date now = getNow();
        return formatDate(DateUtils.addDays(now, -1), PATTERN_B).equals(formatDate(date, PATTERN_B));
    }

    /**
     * 计算两个日期之间相差日期天数(如:2014-10-09与2014-10-10相差一天)
     *
     * @param sd
     * @param ed
     */
    public static int diffDateDay(Date sd, Date ed) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sd);
        int sDay = calendar.get(Calendar.DAY_OF_YEAR);
        int sYear = calendar.get(Calendar.YEAR);

        calendar.setTime(ed);
        int eDay = calendar.get(Calendar.DAY_OF_YEAR);
        int eYear = calendar.get(Calendar.YEAR);

        // 每年的天数
        int oneYearDays = 365;
        if ((sYear % 4 == 0 && sYear % 100 != 0) || sYear % 400 == 0) {
            // 润年366天
            oneYearDays = 366;
        }

        return (eYear - sYear) * oneYearDays + eDay - sDay;
    }

    public static Date toLocalDate(Date GMT) throws ParseException {
        String localDateStr = formatDate(GMT);
        return parse(localDateStr);
    }

    public static Date getStartTime(Date date) {
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    public static Date getEndTime(Date date) {
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        today.set(Calendar.MILLISECOND, 999);
        return today.getTime();
    }

    public static Date getDayStartTime(int offset) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, offset);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    public static String getMinStartTime(int offset) {
        Date date = new Date();
        long time = date.getTime();
        date.setTime(time + (60 * offset * 1000));
        return formatDate(date);
    }

    public static String getHourStartTime(int offset) {
        Date date = now();
        long time = date.getTime();
        date.setTime(time + (60 * 60 * offset * 1000));
        return formatDate(date);
    }

    public static Date getMonthStartTime(int offset) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH, offset);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    public static Date getDayEndTime(int offset) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.add(Calendar.DAY_OF_MONTH, offset);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static Date getHourTime(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(timestamp));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTodayHourDate(int hour) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, hour);
        todayEnd.set(Calendar.MINUTE, 0);
        todayEnd.set(Calendar.SECOND, 0);
        todayEnd.set(Calendar.MILLISECOND, 0);
        return todayEnd.getTime();
    }

    public static int getHourOfDay() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.HOUR_OF_DAY);
    }

}
