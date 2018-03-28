package com.sane.router.network.daemons;

import com.sane.router.UI.TableUI;
import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Creates an object responsible for the scheduling of tasks
 * that occur at a regular time interval, spins off these tasks as threads
 *
 * @author Joshua Johnston
 */
public class Scheduler implements Observer
{
    //Fields
    private ScheduledThreadPoolExecutor threadManager;//manages pool of threads
    private ARPDaemon arpDaemon; //for private knowledge of ARP activity
    private LRPDaemon lrpDeamon; //for private knowledge of LRP activity
    private TableUI tableUI; //private tableUI reference to keep tables updated

    //Singleton Implementation
    private static final Scheduler ourInstance = new Scheduler();
    public static Scheduler getInstance(){return ourInstance;}
    private Scheduler(){} //empty constructor

    //Interface Implementation
    /**
     * Required method of the Observer Interface, Ensures non-operation until bootLoader prompt,
     * spins off schedule-controlled threads
     *
     * @param observable - The observed object
     * @param object - An object passed in by the triggering observable
     */
    @Override public void update(Observable observable, Object object)
    {
        threadManager = new ScheduledThreadPoolExecutor(Constants.THREAD_COUNT);
        arpDaemon = ARPDaemon.getInstance();
        tableUI = UIManager.getInstance().getTableUI();

        threadManager.scheduleAtFixedRate(tableUI, Constants.ROUTER_BOOT_TIME, Constants.UI_UPDATE_INTERVAL, TimeUnit.SECONDS);
        threadManager.scheduleAtFixedRate(arpDaemon, Constants.ROUTER_BOOT_TIME, Constants.ARP_UPDATE_INTERVAL, TimeUnit.SECONDS);

        //threadManager.scheduleAtFixedRate(lrpDeamon, Constants.ROUTER_BOOT_TIME, Constants.LRP_UPDATE_INTERVAL, TimeUnit.SECONDS);
    }
}