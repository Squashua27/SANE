package com.sane.router.support;

import android.app.Activity;

import com.sane.router.UI.UIManager;
import com.sane.router.networks.Constants;

import java.util.Observable;

/**
 * Created by Joshua Johnston on 1/11/2018.
 */

//creates classes, notifies classes when to begin operations
public class Bootloader extends Observable
{
    //METHODS

    //constructor, calls "bootRouter" (and test)
    public Bootloader(Activity parentActivity)
    {
        bootRouter(parentActivity);
        notifyObservers();
        test();
    }

    //instantiates other Router classes, adds them as observers, and notifies them to operate
    private void bootRouter(Activity parentActivity)
    {
        //Instantiate_Router_Classes____________________
        //instantiate ParentActivity class?
        ParentActivity.setParentActivity(parentActivity);
        UIManager UImanager = UIManager.getInstance();

        //Create_Observer_List____________________
        addObserver(UIManager.getInstance());

        //Misc_Router_Setup____________________
        //(place holder)

        //Notify_Observers____________________
        setChanged();//notify Java of change
        notifyObservers();//calls update method in observers

        //Output____________________
        UImanager.displayMessage(Constants.ROUTER_NAME + " is up and running!");
    }

    //runs testing for debugging and quality control
    private void test()
    {
        //run tests
    }
}

