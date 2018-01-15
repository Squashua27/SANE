package com.sane.router.support;

/**
 * universal utilities for general use
 *
 * @author Joshua Johnston
 */
public class Utilities
{
    //pads zeros to the beginning of string to return a string of length byteCount

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
            String.format("%01d", string); //pads a zero
        }
        return string;// return zero-padded string
    }
}
