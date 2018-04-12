package com.sane.router.network.daemons;

import com.sane.router.support.BootLoader;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Joshua Johnston on 4/12/2018.
 */

public class LL3Daemon implements Observer
{
    //Fields
    private ARPDaemon arpDemon; //reference to interact with Address Resolution Protocol
    private LL2Daemon ll2Demon; //reference to Layer 2 for transmission of packets
    private LRPDaemon lrpDemon; //reference to obtain next hop for forwarding

    //Singleton Implementation
    private static final LL3Daemon ourInstance = new LL3Daemon();
    public static LL3Daemon getInstance() {
        return ourInstance;
    }
    private LL3Daemon() {} //(empty constructor)

    //Interface Implementation
    /**
     * Methods of the Observer Interface, connects to cooperating daemons upon bootLoader signal
     *
     * @param observable - the observed object
     * @param object - the object optionally passed by the triggering observable
     */
    @Override public void update(Observable observable, Object object)
    {
        arpDemon = ARPDaemon.getInstance();
        ll2Demon = LL2Daemon.getInstance();
        lrpDemon = LRPDaemon.getInstance();
    }
}
