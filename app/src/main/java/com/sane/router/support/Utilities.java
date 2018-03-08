package com.sane.router.support;

import java.util.Calendar;

import static java.lang.Math.min;

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
     * Formats a hex string for output to a hex dump display
     *
     * @param inputString - the input hex string
     * @return outputString - the output string
     */
    public static String eightBytesPerLine(String inputString)
    {
        int charCount = inputString.length();//total hex characters
        int lineCount = charCount/16 + 1;//total lines of output to make

        String outputString = "     0x:__0__1__2__3__4__5__6__7\n";//Label: column offset in bytes
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++)//line-by-line formatting
        {
            outputString += " 0x"//row offset in bytes
            + padHexString(Integer.toHexString(lineIndex*8),2) + "| ";

            int hexCount = min(charCount - 16*lineIndex, 16);//number of hex characters in line
            for (int hexIndex = 0; hexIndex < hexCount; hexIndex++)//formatting per hex char
            {
                outputString += inputString.charAt(16*lineIndex + hexIndex);//add hex char
                if (hexIndex % 2 == 1)
                    outputString += " ";//group hex pairs into bytes
            }
            for (int dif = (16 - hexCount + 1)/2 + 1; dif > 0; dif--)//formatting for last line
            outputString += "   "; //end of hex dump

            String hex = inputString.substring(16*lineIndex, 16*lineIndex + hexCount);
            StringBuilder ASCII = new StringBuilder();
            //formatting per ASCII char
            for (int i = 0; i < hex.length(); i+=2)
            {
                String str = hex.substring(i, i+2);
                if (32<=Integer.parseInt(str,16) && Integer.parseInt(str,16)<=126)
                    ASCII.append((char)Integer.parseInt(str, 16));
                else
                    ASCII.append(".");
            }
            outputString += ASCII.toString() + "\n";
        }
        return outputString;
    }
    /**
     * Returns the time in seconds passed since the base date in seconds
     *
     * @return time - time in seconds since base date in seconds
     */
    public static int getTimeInSeconds()
    {
        return (int) (Calendar.getInstance().getTimeInMillis()/1000-baseDateInSeconds);
    }
}