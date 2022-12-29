package com.example.progettoducange.Utils;

import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class Utils {
    // remove quotation marks and space within the string passed as parameter
    public static String CleanString(String str)
    {
        return str.replace("\"", "").replaceAll("[\\p{Ps}\\p{Pe}]", "").trim();
    }

    public static boolean CheckEmail(String email)
    {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern EMAIL_REGEX = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

        return EMAIL_REGEX.matcher(email).matches();
    }

    // Password must contain at least one digit [0-9].
    // Password must contain at least one lowercase Latin character [a-z].
    // Password must contain at least one uppercase Latin character [A-Z].
    // Password must contain at least one special character like ! @ # & ( ).
    // Password must contain a length of at least 8 characters and a maximum of 20 characters.
    public static boolean CheckPassword(String password)
    {
        String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern PASSWORD_REGEX = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

        return PASSWORD_REGEX.matcher(password).matches();
    }

    public static boolean isNumeric(String string) {
        int intValue;

        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }
}
