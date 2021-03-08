package com.qcit.location.selector.libary.utils;

import com.qcit.location.selector.libary.models.City;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.List;

public class PingYinUtil {

    public static List<City> transferList(List<City> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                exchangeNameOrder(j, list);
            }
        }
        return list;
    }

    public static void exchangeNameOrder(int j, List<City> list) {
        String namePinYin1 = list.get(j).getPinyin();
        String namePinYin2 = list.get(j + 1).getPinyin();

        int size = namePinYin1.length() >= namePinYin2.length() ? namePinYin2.length() : namePinYin1.length();
        for (int i = 0; i < size; i++) {
            if (!list.get(j).getBelongLetter().equals(list.get(j + 1).getBelongLetter())) {
                break;
            }
            char jc = namePinYin1.charAt(i);
            char jcNext = namePinYin2.charAt(i);
            if (jc < jcNext) {//A在B之前就不用比较了
                break;
            }
            if (jc > jcNext) {//A在B之后就直接交换,让A在前面B在后面
                City nameBean = list.get(j);
                list.set(j, list.get(j + 1));
                list.set(j + 1, nameBean);
                break;
            }
            //如果AB一样就继续比较后面的字母
        }
    }

    public static List<City> shortPinyin(List<City> citys) {
        return transferList(citys);
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     * @return
     */
    public static List<City> getPingYins(List<City> citys) {
        for (int j = 0; j < citys.size(); j++) {
            String inputString = citys.get(j).name;
            HanyuPinyinOutputFormat format = new
                    HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);

            char[] input = inputString.trim().toCharArray();
            String output = "";

            try {
                for (int i = 0; i < input.length; i++) {
                    if (java.lang.Character.toString(input[i]).
                            matches("[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.
                                toHanyuPinyinStringArray(input[i],
                                        format);
                        output += temp[0];
                    } else
                        output += java.lang.Character.toString(
                                input[i]).toLowerCase();
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            citys.get(j).setPinyin(output);
            citys.get(j).setBelongLetter(citys.get(j).getPinyin().charAt(0)+"");
        }
        return citys;
    }
}