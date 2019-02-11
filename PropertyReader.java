package in.co.sunrays.proj4.util;

import java.util.ResourceBundle;

/**
 * Read the property values from application properties file using Resource
 * Bundle
 * 
 * @author Factory
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class PropertyReader {

    private static ResourceBundle rb = ResourceBundle
            .getBundle("in.co.sunrays.proj4.bundle.system");

    public static String getValue(String key) {

        String val = null;

        try {
            val = rb.getString(key);
        } catch (Exception e) {
            val = key;
        }

        return val;

    }

    /**
     * Gets String after placing param values
     * 
     * @param key
     * @param param
     * @return String
     */
    public static String getValue(String key, String param) {
        String msg = getValue(key);
        msg = msg.replace("{0}", param);
        return msg;
    }

    public static String getValue(String key, String[] params) {
        String msg = getValue(key);
        for (int i = 0; i < params.length; i++) {
            msg = msg.replace("{" + i + "}", params[i]);
        }
        return msg;
    }

  

    public static void main(String[] args) {
        String[] params = { "Roll No" };
        System.out.println(PropertyReader.getValue("error.require", params));
    }

}