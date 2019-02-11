package in.co.sunrays.proj4.util;
/**
 * This class validates input data
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 */

import java.util.Date;

public class DataValidator {
	 public static boolean isNull(String val) {
	        if (val == null || val.trim().length() == 0) {
	            return true;
	            
	        } else {
	            return false;
	        }
	    }

	    /**
	     * Checks if value is NOT Null
	     * 
	     * @param val
	     * @return
	     */
	    public static boolean isNotNull(String val) {
	        return !isNull(val);
	    }

	    /**
	     * Checks if value is an Integer
	     * 
	     * @param val
	     * @return
	     */

	    public static boolean isInteger(String val) {

	        if (isNotNull(val)) {
	            try {
	                int i = Integer.parseInt(val);
	                return true;
	            } catch (NumberFormatException e) {
	                return false;
	            }

	        } else {
	            return false;
	        }
	    }

	    /**
	     * Checks if value is Long
	     * 
	     * @param val
	     * @return
	     */
	    public static boolean isLong(String val) {
	        if (isNotNull(val)) {
	            try {
	                long i = Long.parseLong(val);
	                return true;
	            } catch (NumberFormatException e) {
	                return false;
	            }

	        } else {
	            return false;
	        }
	    }

	    /**
	     * Checks if value is valid Email ID
	     * 
	     * @param val
	     * @return
	     */
	    public static boolean isEmail(String val) {

	        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	        if (isNotNull(val)) {
	            try {
	                return val.matches(emailreg);
	            } catch (NumberFormatException e) {
	                return false;
	            }

	        } else {
	            return false;
	        }
	    }

	    /**
	     * Checks if value is Date
	     * 
	     * @param val
	     * @return
	     */
	    public static boolean isDate(String val) {

	        Date d = null;
	        if (isNotNull(val)) {
	            d = DataUtility.getDate(val);
	        }
	        return d != null;
	    }

	    /**
	     * Test above methods
	     * 
	     * @param args
	     */
	    public static void main(String[] args) {

	        System.out.println("Not Null 2" + isNotNull("shanoo vishwakarma"));
	        System.out.println("Not Null 3" + isNotNull("j"));
	        System.out.println("Not Null 4" + isNull(null));

	        System.out.println("Is Int " + isInteger(null));
	        System.out.println("Is Int " + isInteger("1"));
	        System.out.println("Is Int " + isInteger("123"));
	        System.out.println("Is Int " + isNotNull("123"));
	        System.out.println("is email "+  isEmail("sanuvish11@gmail.com"));
	        System.out.println("DATE "+isDate("10/12/2002"));
	        System.out.println("long "+isLong("2318"));
	    }

	    
	}


