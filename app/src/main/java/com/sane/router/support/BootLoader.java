package com.sane.router.support;

import android.app.Activity;
import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.networks.Constants;
import com.sane.router.networks.datagram.LL2PFrame;
import com.sane.router.networks.tableRecords.AdjacencyRecord;
import com.sane.router.networks.tableRecords.TableRecord;

import java.util.Observable;

/**
 * creates classes, notifies classes when to begin operations
 *
 * @author Joshua Johnston
 */
public class BootLoader extends Observable
{
    //METHODS____________________
    /**
     * constructor, calls "bootRouter"
     *
     * @param parentActivity - activity reference allowing context-dependent operations
     */
    public BootLoader(Activity parentActivity)
    {
        bootRouter(parentActivity); //boots the router
    }

    /**
     * instantiates other Router classes, adds them as observers, and notifies them to operate
     *
     * @param parentActivity - activity reference allowing context-dependent operations
     */
    private void bootRouter(Activity parentActivity)
    {
        //Instantiate_Router_Classes____________________
        ParentActivity.setParentActivity(parentActivity);
        UIManager uiManager = UIManager.getInstance();

        //Create_Observer_List____________________
        addObserver(UIManager.getInstance());

        //Misc_Router_Setup____________________
        //(place holder)

        //Notify_Observers____________________
        setChanged();//notify Java of change
        notifyObservers();//calls update method in observers

        //Output____________________
        uiManager.displayMessage(Constants.ROUTER_NAME + " is up and running!");

        //Testing____________________
        test();
    }

    /**
     * Runs testing for debugging and quality control
     */
    private void test()
    {
        //Test: Create an LL2P frame, tests Classes and Header Factory
        LL2PFrame packet = new LL2PFrame("F1A5C0B1A5ED8008(text datagram)aCRC");
        Log.i(Constants.LOG_TAG," \n \n"+packet.toProtocolExplanationString()+" \n \n");

        //Test: Create an adjacency record, tests Classes and Table Record Factory
        String addressString = Constants.LL2P_ADDRESS + Constants.IP_ADDRESS;

        AdjacencyRecord record = TableRecordFactory.getInstance().getItem
                (Constants.ADJACENCY_RECORD, addressString);

        Log.i(Constants.LOG_TAG, record.toString());
        Log.i(Constants.LOG_TAG, " \n" + record.toString() + " \n \n");
    }
}

