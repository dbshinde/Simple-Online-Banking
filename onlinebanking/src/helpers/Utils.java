package helpers;

public class Utils {
	
	public static <T> boolean isNotNullAndEmpty(T e){
 	   boolean isNotNUll=false;
 	   if((e!=null) && !(e.toString().isEmpty()) && !(e.toString().equalsIgnoreCase("null"))){
			isNotNUll=true;
 	   }
	  return isNotNUll;
 	}
}
