package com.lljjcoder.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 作者：liji on 2018/1/22 10:30
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public class PinYinUtils {
    //pinyin4j格式类
    private HanyuPinyinOutputFormat format = null;
    //拼音字符串数组
    private String[] pinyin;

    //通过构造方法进行初始化
    public PinYinUtils() {

        format = new HanyuPinyinOutputFormat();
        /*
         * 设置需要转换的拼音格式
         * 以天为例
         * HanyuPinyinToneType.WITHOUT_TONE 转换为tian
         * HanyuPinyinToneType.WITH_TONE_MARK 转换为tian1
         * HanyuPinyinVCharType.WITH_U_UNICODE 转换为tiān
         *
         */
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        pinyin = null;
    }

    /**
     * 对单个字进行转换
     *
     * @param pinYinStr 需转换的汉字字符串
     * @return 拼音字符串数组
     */
    public String getCharPinYin(char pinYinStr) {

        try {
            //执行转换
            pinyin = PinyinHelper.toHanyuPinyinStringArray(pinYinStr, format);

        } catch (BadHanyuPinyinOutputFormatCombination e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //pinyin4j规则，当转换的符串不是汉字，就返回null
        if (pinyin == null) {
            return null;
        }

        //多音字会返回一个多音字拼音的数组，pinyiin4j并不能有效判断该字的读音
        return pinyin[0];
    }

    /**
     * 对单个字进行转换
     *
     * @param pinYinStr
     * @return
     */
    public String getStringPinYin(String pinYinStr) {
        StringBuffer sb = new StringBuffer();
        String tempStr = null;
        //循环字符串
        for (int i = 0; i < pinYinStr.length(); i++) {

            tempStr = this.getCharPinYin(pinYinStr.charAt(i));
            if (tempStr == null) {
                //非汉字直接拼接
                sb.append(pinYinStr.charAt(i));
            } else {
                sb.append(tempStr);
            }
        }

        return sb.toString();

    }

}
