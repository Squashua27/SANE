package com.sane.router.support;

import android.os.AsyncTask;
import android.util.Log;

import com.sane.router.network.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * A class that exists to get IP addresses
 *
 * @author Joshua Johnston (exceptions noted)
 */
public class IPAddressGetter
{
    //Fields
    InetAddress returnAddress;

    //Singleton Implementation
    private static IPAddressGetter ourInstance = new IPAddressGetter();
    public static IPAddressGetter getInstance() {return ourInstance;}
    private IPAddressGetter(){}

    //Methods
    /**
     * Provides a generic method to get the IP address of a URL name,
     * will work with a numeric IP address
     *
     * Can't use "getByName("www.oc.edu")" method on the UI thread,
     * calls an AsynchTask private class to do that on a new thread
     *
     * Because the main method goes on before the answer is returned by the remote nameserver,
     * the method returns a null pointer.
     *
     * "get()" method of the AsyncTask forces it to wait
     *
     * "get()" method returns the object from the return of the doInBackground method
     * UNLESS you have an onPostExecute() method in the class
     *
     * "onPostExecute()" removed from private class, "get()" waits for thread completion
     *
     * @param hostname - the name of the host to retrieve an Inet Address from
     * @return InetAddress - the returned InetAddress
     *
     * @author Pat Smith, Professor at Oklahoma Christian University
     */
    public InetAddress getInetAddress(String hostname)
    {
        // create the object to manage the thread.  “ThreadedNameServer” is a private class below
        ThreadedNameServer thread = new ThreadedNameServer();

        // spin off the thread. Log a message to that effect.
        thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hostname);
        Log.i(Constants.LOG_TAG, " -- Started Thread. Getting ready to wait for results.");
        // use the get method to wait on the thread before proceeding. Also, don't include
        // an "onPostExecute()" method in the private AsyncTask object.
        try
        {
            returnAddress = thread.get(2000, TimeUnit.MILLISECONDS);
            Log.d(Constants.LOG_TAG, " -- Results received.  return address = "+returnAddress.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnAddress;
    }

    /**
     * A private threaded task that posts a request to the local nameserver to resolve the given
     * address. The router IP address should always be numeric.  However, if you had a name in a
     * local nameserver, it would translate the name to an IP address and return the InetAddress.
     *
     * The AsynkTask<…> signature expects a String (IP address), has no progress indicator,
     * and returns an InetAddress object.
     *
     * @author Pat Smith, professor of Oklahoma Christian University
     */
    private class ThreadedNameServer extends AsyncTask<String, Void, InetAddress> {

        /*
     *   The doInBackground is where the work is done. All of this is simply
    *    to call the getByName off the UI thread!
         */
        @Override
        protected InetAddress doInBackground(String... name) {
            InetAddress address = null;
            try {

                address = InetAddress.getByName(name[0]);
            } catch (UnknownHostException e){
                e.printStackTrace();
            }
            return address;
        }
    }
}
