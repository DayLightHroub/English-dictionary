package com.example.husseinjehadalhroub.englishwords.MyTools;

import android.annotation.TargetApi;
import android.os.Build;

public class StringTools {
    private StringTools() {

    }

//    public static String convertToLegalExpression(String text) {
//        String newText;
//        newText = "" + Character.toUpperCase(text.charAt(0));
//        int length = text.length();
//        for (int i = 1; i < length; i++) {
//            newText += Character.toLowerCase(text.charAt(i));
//        }
//        return newText;
//    }

    public static String convertToSearchExpression(String text) {
        String newText = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            newText += Character.toLowerCase(text.charAt(i));
        }

        return newText;
    }

    public static String captilizeFirstLatter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isEnglish(String text) {
        return Character.isLowerCase(text.charAt(0));
    }

    public static String formatDot(String text){
        if (text.charAt(text.length() - 1) == '.')
            return text;
        else
           return (text + '.');
    }


}
