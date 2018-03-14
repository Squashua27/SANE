package com.sane.router.UI;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sane.router.support.BootLoader;
import com.sane.router.support.ParentActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * manages User Interface and delegates tasks to lower level UI classes,
 * observes BootLoader class, begins regular operation after bootLoader gives the all-clear
 *
 * @author Joshua Johnston
 */
public class UIManager implements Observer
{
    private TableUI tableUI;//instance of the Table User Interface
    private SnifferUI snifferUI;//instance of the Sniffer User Interface
    private Activity parentActivity;//activity reference allowing context-dependent operations
    private Context context;//holds context for context-dependent operations

    //Singleton_Implementation____________________
    private static final UIManager uiManager = new UIManager();//creates single instance
    public static UIManager getInstance() {return uiManager;}//typical get method
    private UIManager()//Constructor, builds UI elements to Manage
    {
        tableUI = new TableUI();
        snifferUI = new SnifferUI();
    }
    //Methods____________________
    public TableUI getTableUI(){return tableUI;} //Standard getter
    public SnifferUI getSnifferUI(){return snifferUI;}//Standard getter
    /**
     * displays a message
     *
     * @param message - the message to be displayed, as a string
     */
    public void displayMessage(String message)//displays message
    {
        displayMessage(message, Toast.LENGTH_LONG);
    }
    /**
     * displays a message for a duration
     *
     * @param message the message to be displayed, as a string
     * @param duration duration of message to be displayed, int
     */
    public void displayMessage(String message, int duration)
    {
        Toast.makeText(context, message, duration).show();
    }
    /**
     * allows use of widgets, currently a place holder
     */
    private void setUpWidgets(){}
    //Interface Implementation
    /**
     * necessary method of Observer class, sets up UI operations after notified to do so
     *
     * @param observable - the observable object
     * @param object - the argument passed by the notifyObservers() method
     */
    public void update(Observable observable, Object object)//necessary method of Observer
    {
        if(observable instanceof BootLoader)//if update() called from BootLoader class
        {
            parentActivity = ParentActivity.getParentActivity();
            context = parentActivity.getBaseContext();
            setUpWidgets();
        }
    }
}
