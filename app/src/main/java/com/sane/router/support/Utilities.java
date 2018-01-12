package com.sane.router.support;

/**
 * Created by Joshua Johnston on 1/12/2018.
 */

public class Utilities
{
    //
    static public String padHexString(String string, int byteCount)
    {
        while (string.length() < byteCount*2)
        {
            String.format("%01d", string);
        }
        return string;
    }
}
