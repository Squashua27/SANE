package com.sane.router.support;

/**
 * Created by Joshua Johnston on 1/12/2018.
 */

//General_Purpose_Utilities_for_General_Use____________________
public class Utilities
{
    //pads zeros to the beginning of string to return a string of length byteCount
    static public String padHexString(String string, int byteCount)
    {
        while (string.length() < byteCount*2)
        {
            String.format("%01d", string); //pads a zero
        }
        return string;
    }
}
