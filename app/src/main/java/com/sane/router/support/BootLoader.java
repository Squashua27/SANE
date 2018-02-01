package com.sane.router.support;

import android.app.Activity;
import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.networks.Constants;
import com.sane.router.networks.datagram.LL2PFrame;

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
        LL2PFrame packet = new LL2PFrame("B1A5EDF1A5C08008(text datagram)aCRC");

        UIManager.getInstance().displayMessage(packet.toProtocolExplanationString(),999);
        Log.i(Constants.LOG_TAG, packet.toProtocolExplanationString());
    }
}

