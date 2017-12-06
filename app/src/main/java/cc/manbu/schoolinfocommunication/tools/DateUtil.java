package cc.manbu.schoolinfocommunication.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.manbu.schoolinfocommunication.R;
import cc.manbu.schoolinfocommunication.config.ManbuApplication;


public class DateUtil {

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public enum TimeSpanMode {
		Year, Month, Date, Hours, Minute, Second
	}

	public static String getPadDateHour(Date start, Date end) {
		try {
			long quot = start.getTime() - end.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
			return String.valueOf(quot);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static String getCurrentDateString() {
		return getDateString(getCurrentDate());
	}

	public static Date getBeforeDate(Date date) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		Date dBefore = calendar.getTime(); // 得到前一天的时间
		return dBefore;
	}

	public static String getBeforeString(Date date) {
		return getDateString(getBeforeDate(date));
	}
	
	public static String getDateString(Date date) {
		if (date == null) {
			return "";
		}
		return format.format(date);
	}

	public static Date getNextDate(Date date) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为后一天
		int nextDay = calendar.get(Calendar.DAY_OF_MONTH);
		Date dBefore = calendar.getTime(); // 得到后一天的时间
		calendar.setTimeInMillis(System.currentTimeMillis());
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
	    if(nextDay>curDay){
	    	dBefore = calendar.getTime();
	    }
		return dBefore;
	}

	public static String getNextDateString(Date date) {
		return getDateString(getNextDate(date));
	}

	

	public static String getShortDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null) {
			return "";
		}
		return format.format(date);
	}

	public static String getDateString(Date date, String formate) {
		SimpleDateFormat format = new SimpleDateFormat(formate);
		if (date == null) {
			return "";
		}
		return format.format(date);
	}

	public static String getTimeSpan(Date beforDate, Date afterDate,
									 TimeSpanMode mode) {
		Long t1 = null;
		Long t2 = null;
		String res = null;
		try {
			t1 = format.parse(format.format(afterDate)).getTime();
			t2 = format.parse(format.format(beforDate)).getTime();
		} catch (ParseException e) {

			e.printStackTrace();
		}

		int hours = (int) ((t1 - t2) / 3600000);
		int minutes = (int) (((t1 - t2) / 1000 - hours * 3600) / 60);
		int second = (int) ((t1 - t2) / 1000 - hours * 3600 - minutes * 60);
		if (mode == TimeSpanMode.Date) {
			res = (hours == 0 ? "" : (hours + "小时"));
		}
		if (mode == TimeSpanMode.Minute) {
			res = (hours == 0 ? "" : (hours + "小时"))
					+ (minutes == 0 ? "" : (minutes + "分钟"));
		}
		if (mode == TimeSpanMode.Second) {
			res = (hours == 0 ? "" : (hours + "小时"))
					+ (minutes == 0 ? "" : (minutes + "分钟"))
					+ (second == 0 ? "" : (minutes + "秒"));
		}

		return res;
	}

	public static Date getDate(long value) {
		try {
			long mi = Long.parseLong(value + "");
			return new Date(mi);
		} catch (Exception e) {
			return null;
		}

	}

	public static Date getDate(String value) {
		try {
			return format.parse(value);
		} catch (ParseException e) {

			e.printStackTrace();
			return null;
		}
	}
	public static String format(String format, Date date) {
		if(date == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date);
	}
	public static String format(Date date) {
		if(date == null){
			return "";
		}
		Date now = new Date(System.currentTimeMillis());
		long time = now.getTime()-date.getTime();
		int dDays = getDateSpace(date,now);
		if(dDays==0){
			String ago_sec = ManbuApplication.getInstance().getResources().getString(R.string.ago_sec);
			if(time<=1000){
				return "1"+ago_sec;
			}
			if(time<60*1000){
				return Math.round(time/1000)+ago_sec;
			}
			if(time>=60*1000 && time<60*1000*60){
				return Math.round(time/(60*1000))+ManbuApplication.getInstance().getResources().getString(R.string.ago_min);
			}
			if(time>=60*1000*60 && time<60*1000*60*24){
				return Math.round(time/(60*1000*60))+ManbuApplication.getInstance().getResources().getString(R.string.ago_hour);
			}
		}
		if(dDays>0){
			int dMonths = getMonthSpace(date,now);
			if(dMonths==0){
				return dDays+ManbuApplication.getInstance().getResources().getString(R.string.ago_day);
			}
			if(dMonths>0){
				int dYears = getYearSpace(date,now);
				if(dYears==0){
					return dMonths+ManbuApplication.getInstance().getResources().getString(R.string.ago_mouth);
				}
				if(dYears>0){
					return dMonths+ManbuApplication.getInstance().getResources().getString(R.string.ago_year);
				}
			}
		}
		return "";
	}
	
	public static int getDateSpace(Date date1, Date date2){
        int result = 0;
        Calendar calst = Calendar.getInstance();;
        Calendar caled = Calendar.getInstance();
        calst.setTime(date1);
        caled.setTime(date2);
         //设置时间为0时   
         calst.set(Calendar.HOUR_OF_DAY, 0);
         calst.set(Calendar.MINUTE, 0);
         calst.set(Calendar.SECOND, 0);
         caled.set(Calendar.HOUR_OF_DAY, 0);
         caled.set(Calendar.MINUTE, 0);
         caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数   
         int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;   
        return days;   
    }
	
	/**
	 * 
	 * @author Carlos
	 * @updateTime 2015年3月10日 上午2:34:52
	 * @param date1
	 * @param date2
	 * @return 
	 */
	 public static int getMonthSpace(Date date1, Date date2){
	        int result = 0;
	        Calendar c1 = Calendar.getInstance();
	        Calendar c2 = Calendar.getInstance();
	        c1.setTime(date1);
	        c2.setTime(date2);
	        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
	        return result;

	    }
	 
	 public static int getYearSpace(Date date1, Date date2){
	        int result = 0;
	        Calendar c1 = Calendar.getInstance();
	        Calendar c2 = Calendar.getInstance();
	        c1.setTime(date1);
	        c2.setTime(date2);
	        result = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
	        return result;

	    }
	public static Date parse(String format, String date) {
		if(date == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
//			Log.e("DateUtil.parse", e.getMessage());
			return null;
		}
	}
	public static String getLocalWeekday(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch(week){
		case 1:
			str = "日";
			break;
		case 2:
			str = "一";
			break;
		case 3:
			str = "二";
			break;
		case 4:
			str = "三";
			break;
		case 5:
			str = "四";
			break;
		case 6:
			str = "五";
			break;
		case 7:
			str = "六";
			break;
		}
		return "星期"+str;
	}
	public static int getAge(Date birthday){
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth 
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth 
				age--;
			}
		}
		return age;
		}
}
