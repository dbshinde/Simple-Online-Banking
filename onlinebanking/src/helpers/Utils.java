package helpers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	
	public static <T> boolean isNotNullAndEmpty(T e){
 	   boolean isNotNUll=false;
 	   if((e!=null) && !(e.toString().isEmpty()) && !(e.toString().equalsIgnoreCase("null"))){
			isNotNUll=true;
 	   }
	  return isNotNUll;
 	}
	
	public static Date getDate(String date) throws ParseException 
	{
		SimpleDateFormat datetimeFormatter = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();  
		cal.setTime(datetimeFormatter.parse(date));
		return cal.getTime();
	}
	
	public static Timestamp getDateTimeStamp(String date) throws ParseException 
	{
		return new Timestamp(getDate(date).getTime());
	}
}
