package com.sane.router.support;

import java.util.Calendar;

/**
 * universal utilities for general use
 *
 * @author Joshua Johnston
 */
public class Utilities
{
    //Fields
    public static long baseDateInSeconds = Calendar.getInstance().getTimeInMillis()/1000;

    //Methods
    /**
     * zero pads a hex string, returns s string of length byteCount
     *
     * @param string - the hex string to be padded (from the front) with zeros
     * @param byteCount - the integer length (in bytes) to pad the string to
     * @return string - the input string, returned after being padded
     */
    public static String padHexString(String string, int byteCount)
    {
        while (string.length() < byteCount*2)//while string is not of expected return length
        {
            string = "0" + string;//pads a zero
        }
        return string;// return zero-padded string
    }
    /**
     * Returns the time in seconds passed since the base date in seconds
     *
     * @return time - time in seconds since base date in seconds
     */
    public int getTimeInSeconds()
    {
        return (int) (Calendar.getInstance().getTimeInMillis()/1000-baseDateInSeconds);
    }
}
