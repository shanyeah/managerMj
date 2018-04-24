package com.imovie.mogic.web.common;

/**
 * Created by zhouxinshan on 2016/4/12.
 */
public final class CommonUtil {

    public static String getTextOrDefault(String src, String def) {
        if (null == src || src.isEmpty()) {
            return def;
        }
        return src;
    }

    /**
     * null or String.empty()
     */
    public static boolean isStringEmpty(String str) {
        if (null == str || str.isEmpty()) {
            return true;
        }
        return false;
    }

    /***/
    public static boolean isStringNotEmpty(String str) {
        return !isStringEmpty(str);
    }

    /**
     * 全角转换为半角
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 半角转换为全角
     */
    public static String toSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 输入框输入数字后自动填充
     *
     * @param input    输入的数字
     * @param increase 步长
     * @return 运算后的数字
     */
    public static int getIncreasedNumber(int input, int increase) {
        int result = 0;
        if (input <= increase) {
            result = increase;
        } else {
            result = input - (input % increase);
        }
        return result;
    }
}
