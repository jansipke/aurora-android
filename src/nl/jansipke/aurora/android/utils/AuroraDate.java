package nl.jansipke.aurora.android.utils;

import java.text.ParseException;
import java.util.Calendar;

public class AuroraDate {
	
	private static String[] months = {
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	private int year;
	private int month;
	private int day;
	
	public AuroraDate(int calendarPart, int ago) {
		Calendar date = Calendar.getInstance();
		date.add(calendarPart, -ago);
		this.year = date.get(Calendar.YEAR);
		this.month = date.get(Calendar.MONTH) + 1;
		this.day = date.get(Calendar.DAY_OF_MONTH);
	}
	
	public AuroraDate(int year, int month, int day) throws ParseException {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public AuroraDate(String timestamp) throws ParseException {
		if (timestamp == null || timestamp.length() != 10) {
			throw new ParseException("Timestamp needs to be 10 characters long, but is [" + timestamp + "]", 0);
		}
		this.year = new Integer(timestamp.substring(0, 4)).intValue();
		this.month = new Integer(timestamp.substring(5, 7)).intValue();
		this.day = new Integer(timestamp.substring(8, 10)).intValue();
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public String getMonthString() {
		return months[month - 1];
	}
	
	public String getMonthYearString() {
		return months[month - 1] + " " + year;
	}
	
	public String getYearMonthString() {
		StringBuffer sb = new StringBuffer(year + "-");
		if (month < 10) {
			sb.append("0");
		}
		sb.append(month);
		return sb.toString();
	}
	
	public boolean isWeekend() {
		Calendar date = Calendar.getInstance();
	    date.set(year, month - 1, day);
	    return (date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(year + "-");
		if (month < 10) {
			sb.append("0");
		}
		sb.append(month + "-");
		if (day < 10) {
			sb.append("0");
		}
		sb.append(day);
		return sb.toString();
	}
}
