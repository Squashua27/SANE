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
 * that recur at after a regular time interval
 *
 * @author Joshua Johnston
 */
public class Scheduler implements Observer
{
    //Singleton Implementation
    private static final Scheduler ourInstance = new Scheduler();
    public static Scheduler getInstance() {
        return ourInstance;
    }
    private Scheduler(){}

    //Fields
    private ScheduledThreadPoolExecutor threadManager;//manages pool of threads
    private ARPDaemon arpDaemon; //for private access to ARP services
    //private RPDaemon lrpDeamon; //TODO: creaate this class
    private TableUI tableUI; //private tableUI reference to keep tables updated

    //Methods

    /**
     * Required method of the Observer Class, Ensures non-operation until bootLoader prompt
     *
     * @param observable - The observed object
     * @param object     - An object passed in by the triggering observable
     */
    @Override public void update(Observable observable, Object object)
    {
        threadManager = new ScheduledThreadPoolExecutor(Constants.THREAD_COUNT);
        arpDaemon = ARPDaemon.getInstance();
        tableUI = UIManager.getInstance().getTableUI();

        threadManager.scheduleAtFixedRate(tableUI, Constants.ROUTER_BOOT_TIME, Constants.UI_UPDATE_INTERVAL, TimeUnit.SECONDS);
        threadManager.scheduleAtFixedRate(arpDaemon, Constants.ROUTER_BOOT_TIME, Constants.ARP_UPDATE_INTERVAL, TimeUnit.SECONDS);
    }
}
