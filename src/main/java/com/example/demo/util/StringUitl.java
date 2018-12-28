package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUitl {
	private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";
	
	public static String removeChinese(String str) {
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(str);
        return mat.replaceAll("");
	}
}
