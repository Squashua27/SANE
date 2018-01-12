package com.sane.router.support;

import android.app.Activity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Joshua Johnston on 1/11/2018.
 */

//Creates classes, notifies classes when to begin operations
public class Bootloader extends Observable
{
    //METHODS

    //constructor, calls "bootRouter" (and test for testing purposes)
    public Bootloader(Activity parentActivity)
    {
        bootRouter(parentActivity);
        test();
    }

    //instantiates other Router classes, adds them as observers, and notifies them to operate
    private void bootRouter(Activity parentActivity)
    {
        //instantiate ParentActivity class? No?
        ParentActivity.setParentActivity(parentActivity);
        //
    }

    //runs testing for debugging and quality control
    private void test()
    {
        //run tests
    }
}

