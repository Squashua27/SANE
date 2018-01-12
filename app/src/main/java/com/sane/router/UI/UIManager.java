package com.sane.router.UI;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Joshua Johnston on 1/12/2018.
 */

public class UIManager extends Observable//<-- Should be observer, but won't compile?
{
    private static final UIManager ourInstance = new UIManager();

    public static UIManager getInstance() {return ourInstance;}

    //constructor, currently empty
    private UIManager()
    {

    }

    public void update(Observable observable, Object object)
    {

    }
}
