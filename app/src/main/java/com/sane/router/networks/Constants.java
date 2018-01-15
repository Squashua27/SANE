package com.sane.router.networks;

import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * contains universal constants
 *
 * @author Joshua Johnston
 */
public class Constants
{
    // IP address stored here in dotted decimal notation
    public static final String IP_ADDRESS;
    // strings used in debugging messages or messages sent to the log file.
    public static final String ROUTER_NAME = "The Promised LAN";
    public static final String LOG_TAG = "The Promised LAN: ";

    /**
     * the following code snippet by Corbin Young,
     * an improvement upon the previous year's IP getting method
     */
    static
    {
        IP_ADDRESS = getLocalIpAddress();
        Log.i(Constants.LOG_TAG, "IP Address is "+IP_ADDRESS); // this will show up in the log file
    }

    /**
     * IP address getting method, credit: Prof Smith of Oklahoma Christian University
     *
     * getLocalIPAddress - this function goes through the network interfaces
     * looking for one thxat has a valid IP address.
     * Care must be taken to avoid a loopback address and IPv6 Addresses.
     *
     * @return - a string containing the IP address in dotted decimal notation.
     */
    private static String getLocalIpAddress()
    {
        //String address= null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}