package dateValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

public class dateValid {
	
//	public static boolean isValidDateFormat(String date) {
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        try {
//        	YearMonth ym = YearMonth.parse(date, format);
//        	int day = Integer.parseInt(date.substring(8, 10));
//            int maxDays = ym.lengthOfMonth();
//            return day <= maxDays;
//        } catch (DateTimeParseException e) {
//            return false;
//        }
//    }
//	
//	public static boolean isValidDate(String date, String min, String max) {
//		if(isValidDateFormat(date) && isValidDateFormat(min) && isValidDateFormat(max)) {
//			DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-DD");
//			LocalDate d = LocalDate.parse(date, format);
//			LocalDate minDate = LocalDate.parse(min, format);
//			LocalDate maxDate = LocalDate.parse(max, format);
//			return d.isBefore(maxDate) && d.isAfter(minDate);
//		}
//		return false;
//	}
	
//	public static boolean isValidDateFormat(Date date) {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        format.setLenient(false);
//
//        String dateString = format.format(date);
//
//        try {
//            format.parse(dateString);
//            return true;
//        } catch (java.text.ParseException e) {
//            return false;
//        }
//    }
	
	public static String dateParser(Date date) {
		//Date date = new Date(95, 3, 1); // April 1, 1995

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
        String dateString = format.format(date);
        return dateString; // Output: 1995-04-01
        } catch (Exception e){
        	return null;
        }

	}
	
	
	public static boolean isValidDateFormat(String date) {
		//String date = d.parse(null); 
      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      try {
      	YearMonth ym = YearMonth.parse(date, format);
      	int day = Integer.parseInt(date.substring(8, 10));
          int maxDays = ym.lengthOfMonth();
          return day <= maxDays;
      } catch (DateTimeParseException e) {
          return false;
      }
  }
	
//	public static boolean isValidDate(Date date) {
//		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//	    try {
//	        date = formatter.parse("11/12/2011");
//	        //System.out.println("Date is : " + date);
//	        return true;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
	
	
//	public static String parseDateToString(Date d) {
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String date = format.format(d);
//		return date;
//	}
//	
//	public static String dateToString(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        return format.format(date);
//    }
	
//	public static Date convertToDate(String dateString) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            return format.parse(dateString);
//        } catch (Exception e) {
//            return null;
//        }
//    }
	
	public static String convertToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(date);
        } catch (Exception e) {
            return null; 
        }
    }
	
	public static boolean isValidDate(Date date, Date min, Date max) {
		return date.before(max) && date.after(min);
	}

//	public static void main(String[] args) {
////		System.out.println(isValidDateFormat("31-04-2022"));
////		System.out.println(isValidDateFormat("01-01-1990"));
////		System.out.println(isValidDateFormat("30-04-2022"));
//		System.out.println(dateParser(new Date(30, 04, 2022)));
//		System.out.println(isValidDateFormat(new Date(202, 04, 31)));
//		System.out.println(isValidDateFormat(new Date(30, 04, 2022)));
//		System.out.println(isValidDateFormat(new Date(2022, 13, 12)));
//		System.out.println(isValidDateFormat(new Date(2022, 04, 30)));
//		//System.out.println(isValidDate("31-04-2022", "01-01-1990", "12-12-2022"));
//	}
	
	public static void main(String[] args) {
		Date date = new Date(1995-1900, 4-1, 30);  
		Date date1 = new Date(1995-1900, 4-1, 31);  
		Date date2 = new Date(1995-1900, 4-1, 32);  
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
//        String strDate = dateFormat.format(date);  
//        System.out.println("Converted String: " + strDate);  
//		System.out.println(isValidDate1(date));
//		System.out.println(isValidDate1(date1));
//		System.out.println(isValidDate1(date2));
		System.out.println(isValidDate(date1, date, date2));
		//System.out.println(dateToString(date));
		System.out.println(convertToDate("1995-05-31"));
	}
}
