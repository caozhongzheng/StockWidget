/**  
 * Utils class
 * RK_ID: RK_STOCK
 * @author wgz
 * @date 2010-04-27
 */
package com.lenovo.leos.stocks;

import java.text.DecimalFormat;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.format.Time;
import android.text.format.DateUtils;
import android.text.format.DateFormat;
import android.content.Context;

public class Utils { 
	public static String replaceBlank(String str){   
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");    
		Matcher m = p.matcher(str);   
		String after = m.replaceAll("");  		
		return after;		
	} 
	  
	public static String formatSize(String strSize) {
        String suffix = null;
        double size = new Double(strSize).doubleValue();
        // add KB or MB suffix if size is greater than 1K or 1M
        if (size >= Math.pow(10, 6)) {
            suffix = "M";
            size /= Math.pow(10, 6);
        }
        
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        
        String result = formatter.format(Math.round(size*100)/100.0);
                
        if (suffix != null) {
            result = result + suffix;
        }
        return result;
    }
	
	public static String formatDoubleSize(String strSize) {
       
		String result = "";
		
		if (strSize.indexOf(".") >= 0) {
			strSize = strSize+"00";
			DecimalFormat formatter = new DecimalFormat();			
		    formatter.setGroupingSize(3);		    
		    result = formatter.format(Math.round((new Double(strSize).doubleValue()) * 100) / 100.0);
		} else {
			result = strSize+".00";
		}
		return result;
    }
	
	public static String formatFloatSize(String strSize) {	       
		String result = "";
		strSize = strSize.split("%")[0];
		
		if (strSize.indexOf(".") >= 0) {
			strSize = strSize + "00";
			DecimalFormat formatter = new DecimalFormat();			
		    formatter.setGroupingSize(3);		    
		    result = formatter.format(Math.round((new Double(strSize).doubleValue()) * 100) / 100.0);
		} else {
			result = strSize +".00";
		}
		return result+"%";
    }
	
	public static String formatTime(Date strTime,Context context) {
		SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		
		String dateStr = chineseDateFormat.format(strTime);
		long tmptime = strTime.getTime();
	
	    Time calendartime = new Time();
	    calendartime.set(tmptime);
	   
	    int flags = DateUtils.FORMAT_SHOW_TIME ;
        if (DateFormat.is24HourFormat(context)) {
     	    flags |= DateUtils.FORMAT_24HOUR;
        } 
        String timestr = DateUtils.formatDateTime(context, tmptime, flags);
       
		return dateStr + " " + timestr + ":" + String.valueOf(strTime.getSeconds());
	}
	
	/**
	 * To constraint threads' number
	 * @param array
	 * @param maxNum
	 * @return
	 */
	public static List<String[]> splitByMaxNum(String[] array, int maxNum) {
		int memberNum;
		List<String[]> ret = new ArrayList<String[]>();
		
		if (array == null || maxNum == 0 || array.length == 0) {
			return null;
		}
		int len = array.length;
		int quotient = len / maxNum;
		int remainder = len % maxNum;
		int groupNum = Math.min(maxNum, len);
		
		for (int i = 0; i < groupNum; i++) {
			memberNum = i < remainder ? (quotient + 1) : quotient;
			String[] retSub = new String[memberNum];
			for (int j = 0; j < memberNum; j++) {
				int op = j * groupNum + i;
				retSub[j] = array[op];
			}			
			ret.add(retSub);
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
}
