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
     * @param inputString - the input string
     * @return outputString - the output string
     */
    public static String eightBytesPerLine(String inputString)
    {
        int charCount = inputString.length();
        int lineCount = charCount/16 + 1;
        String outputString = "";

        //formatting per line (technically, two lines)
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++)
        {
            //formatting hex dump line
            outputString += padHexString(Integer.toHexString(lineIndex*8),2)//offset of row
            + "   ";

            for (int charIndex = 0; charIndex < min(charCount - 16*lineIndex, 16); charIndex++)
            {
                //formatting per hex character
                outputString += inputString.charAt(16*lineIndex + charIndex);

                if (charIndex % 2 == 1)
                    outputString += " ";//add spaces to group characters into twos
            }
            outputString += "\n"; //end of hex dump line

            //formatting ASCII dump line
            if (lineIndex < lineCount - 1)
            //    outputString += Integer.parseInt(inputString.substring(lineIndex*16, lineIndex*16+15),16);
                outputString += inputString.substring(lineIndex*16, lineIndex*16+16)+"\n";
            else
            //    outputString += Integer.parseInt(inputString.substring(lineIndex*16),16);
                outputString += inputString.substring(lineIndex*16);
        }

        return outputString;
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
