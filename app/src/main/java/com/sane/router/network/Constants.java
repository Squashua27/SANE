package com.sane.router.network;

import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Contains universal constants
 *
 * @author Joshua Johnston
 */
public class Constants
{
    public static final String IP_ADDRESS;//IP address in dotted decimal notation
    public static final String LL2P_ADDRESS = "B1A5ED";
//    public static final int LL2P_ADDRESS_int = 127;
    public static final String LL3P_ADDRESS = "2727";
    public static final int UDP_PORT = 49999;//our chosen port for communication

    //used in debugging messages or messages sent to the log file.
    public static final String ROUTER_NAME = "The Promised LAN";
    public static final String LOG_TAG = "The Promised LAN: ";

    //Schedule Management (times in seconds)
    public static final int THREAD_COUNT = 2;
    public static final int ROUTER_BOOT_TIME = 4;
    public static final int UI_UPDATE_INTERVAL = 1;
    public static final int ARP_UPDATE_INTERVAL = 5;
    public static final int ARP_RECORD_TTL = 10;

    //Datagram Constants (lengths and offsets in bytes)
    //Lengths (in bytes)
    public static final int LL2P_ADDRESS_LENGTH = 3;
    public static final int LL2P_TYPE_FIELD_LENGTH = 2;
    public static final int LL2P_CRC_FIELD_LENGTH = 2;

    public static final int LL3P_ADDRESS_LENGTH = 2;
    public static final int LL3P_ADDRESS_NETWORK_LENGTH = 1;
    public static final int LL3P_ADDRESS_HOST_LENGTH = 1;

    //Offsets (in bytes)
    public static final int LL2P_DEST_ADDRESS_OFFSET = 0;
    public static final int LL2P_SOURCE_ADDRESS_OFFSET = 3;
    public static final int LL2P_TYPE_FIELD_OFFSET = 6;
    public static final int LL2P_PAYLOAD_OFFSET = 8;

    public static final int LL3P_ADDRESS_NETWORK_OFFSET = 0;
    public static final int LL3P_ADDRESS_HOST_OFFSET = 1;


    //LL2P Type Designations
    public static final int LL2P_TYPE_LL3P = 0x8001;
    public static final int LL2P_TYPE_RESERVED = 0x8002;
    public static final int LL2P_TYPE_LRP = 0x8003;
    public static final int LL2P_TYPE_ECHO_REQUEST = 0x8004;
    public static final int LL2P_TYPE_ECHO_REPLY = 0x8005;
    public static final int LL2P_TYPE_ARP_REQUEST = 0x8006;
    public static final int LL2P_TYPE_ARP_REPLY = 0x8007;
    public static final int LL2P_TYPE_TEXT = 0x8008;
    //string representations
    public static final String LL2P_TYPE_LL3P_HEX = "8001";
    public static final String LL2P_TYPE_RESERVED_HEX = "8002";
    public static final String LL2P_TYPE_LRP_HEX = "8003";
    public static final String LL2P_TYPE_ECHO_REQUEST_HEX = "8004";
    public static final String LL2P_TYPE_ECHO_REPLY_HEX = "8005";
    public static final String LL2P_TYPE_ARP_REQUEST_HEX = "8006";
    public static final String LL2P_TYPE_ARP_REPLY_HEX = "8007";
    public static final String LL2P_TYPE_TEXT_HEX = "8008";

    //Arbitrary (but consistent) integer values to refer to fields created in factory
    //Header Field Factory:
    public static final int LL2P_DEST_ADDRESS_FIELD = 0;
    public static final int LL2P_SOURCE_ADDRESS_FIELD = 1;
    public static final int LL3P_DEST_ADDRESS_FIELD = 2;
    public static final int LL3P_SOURCE_ADDRESS_FIELD = 3;
    public static final int LL2P_TYPE_FIELD = 4;
    public static final int LL2P_PAYLOAD_FIELD = 5;
    public static final int CRC_FIELD = 6;

    //Table Record Factory:
    public static final int RECORD = 0;
    public static final int ADJACENCY_RECORD = 1;
    public static final int ARP_RECORD = 2;

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