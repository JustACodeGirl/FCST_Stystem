/**
 * StringUtils.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package com.ovt.sale.fcst.common.utils;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

/**
 * StringUtils
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[API] 1.0
 */
public class StringUtils
{
    public static final String CSV_SEPARATOR = ",";

    public static final String SLASH = "/";

    public static final String UNDER_LINE = "_";

    public static final String QUESTION = "?";

    public static final String AND = "&";

    public static final String EQUAL = "=";

    public static final String NEW_LINE = "\n";

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String SPACE = "space";
    
    public static final String FLOW = "flow";
    
    public static final String SINGLE_QUOTE = "\'";
    
    public static final String SQL_BETWEEN = " BETWEEN ";
    
    public static final String SQL_UPDATE = " UPDATE ";
    
    public static final String SQL_SET = " SET ";
    
    public static final String SQL_AND = " AND ";

    public static final String SQL_WHERE = " WHERE ";

    public static final String SQL_EQUAL = " = ";
    
    public static final String SQL_SELECT = " SELECT * FROM ";
    
	private static Object initLock = new Object();

    public static boolean isBlank(Object str)
    {
        return str == null || str.toString().trim().length() == 0;
    }

    public static boolean isNotBlank(Object str)
    {
        return !isBlank(str);
    }

    public static String trim(String str)
    {
        return str == null ? null : str.trim();
    }

    public static boolean equals(String str1, String str2)
    {
        return str1 != null && str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2)
    {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * get CSV string from the collections. use comma as separator. blank items
     * will be skipped.
     * 
     * @param values Collection
     * @return StringBuffer
     */
    public static String getCSV(Collection<?> values)
    {
        return getCSV(values, CSV_SEPARATOR, true);
    }

    public static String getCSV(Collection<?> values, boolean warpSingleQutoa)
    {
        return getCSV(values, CSV_SEPARATOR, true, warpSingleQutoa).toString();
    }

    /**
     * convert the collection into CSV.
     * 
     * @param values
     * @param separators
     * @param ignoreBlank if true blank entries will be skipped
     * 
     * @return StringBuffer
     */
    public static String getCSV(Collection<?> values, String separators,
            boolean ignoreBlank)
    {
        return getCSV(values, separators, ignoreBlank, false).toString();
    }

    private static StringBuffer getCSV(Collection<?> values, String separators,
            boolean ignoreBlank, boolean warpSingleQutoa)
    {
        StringBuffer sb = new StringBuffer();
        if (values == null || values.size() == 0)
        {
            return sb;
        }

        for (Iterator<?> it = values.iterator(); it.hasNext();)
        {
            String value = it.next().toString();
            if (ignoreBlank && isBlank(value))
            {
                continue;
            }

            if (sb.length() > 0)
            {
                sb.append(separators);
            }
            sb.append(warpSingleQutoa ? addSingleQuote(value) : value);
        }

        return sb;
    }

    private static String addSingleQuote(String strSource)
    {
        if (strSource == null)
        {
            strSource = "";
        }
        return "'" + strSource + "'";
    }

    public static String formatNumber(double num, int maxFractionDigits)
    {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(maxFractionDigits);
        nf.setGroupingUsed(false);
        return nf.format(num);
    }

    /**
     * convert '2016-03-05' to web time format '2016/3/5'.
     * 
     * @param timeTitle
     * @return
     */
    public static String toWebTimeFormat(String timeTitle)
    {
        StringBuffer webTimeTitle = new StringBuffer();
        String[] timePart = timeTitle.split("-");
        webTimeTitle.append(timePart[0]);
        for (int i = 1; i < timePart.length; i++)
        {
            webTimeTitle.append("/");
            if (timePart[i].indexOf("0") == 0)
            {
                webTimeTitle.append(timePart[i].replace("0", ""));
            }
            else
            {
                webTimeTitle.append(timePart[i]);
            }
        }
        return webTimeTitle.toString();
    }
    
    public static final String randomString(int length) 
    {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					numbersAndLetters = "023456789abcdefghijkmnopqrstuvwxyz"
							.toCharArray();
				}
			}
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen
					.nextInt(numbersAndLetters.length - 1)];
		}
		return new String(randBuffer);
	}
    
    /**
	 * 字符串替换，将 source 中的 oldString 全部换成 newString
	 * 
	 * @param source 源字符串
	 * @param oldString 老的字符串
	 * @param newString 新的字符串
	 * @return 替换后的字符串
	 */
	public static String replace(String source, String oldString,
			String newString) {
		if (source != null) {
			StringBuffer output = new StringBuffer();
			int lengthOfSource = source.length(); // 源字符串长度
			int lengthOfOld = oldString.length(); // 老字符串长度

			int posStart = 0; // 开始搜索位置
			int pos; // 搜索到老字符串的位置

			while ((pos = source.indexOf(oldString, posStart)) >= 0) {
				output.append(source.substring(posStart, pos));

				output.append(newString);
				posStart = pos + lengthOfOld;
			}

			if (posStart < lengthOfSource) {
				output.append(source.substring(posStart));
			}

			return output.toString();
		}
		return "";
	}
}
