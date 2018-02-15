package com.sane.router.UI;

import android.app.Activity;
import android.content.Context;

import com.sane.router.R;
import com.sane.router.networks.daemons.LL1Daemon;
import com.sane.router.support.BootLoader;
import com.sane.router.support.ParentActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * The class of Table UI objects, creates and manages four singleTableUI objects
 *
 * @author Joshua Johnston
 */
public class TableUI implements Runnable, Observer
{
    //Fields
    private AdjacencyTableUI adjacencyTableUI;
    //private SingleTableUI arpTableUI;
    //private SingleTableUI routingTable;
    //private SingleTableUI forwardingUI;

    //Methods

    /**
     * Empty constructor, UI objects are built after the router is booted
     */
    public void TableUI(){}

    /**
     * Runs once per second to keep the table current, currently empty
     */
    @Override public void run()
    {
    }

    @Override public void update(Observable observable, Object o)
    {
        if (observable instanceof BootLoader)
        {
            Activity activity = ParentActivity.getParentActivity();
            Context context = activity.getBaseContext();

            adjacencyTableUI = new AdjacencyTableUI(activity,
                    R.id.adjacencyTable,
                    LL1Daemon.getInstance().getAdjacencyTable(),
                    LL1Daemon.getInstance());
        }
    }
}
