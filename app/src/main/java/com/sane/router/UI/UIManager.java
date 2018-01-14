package com.sane.router.UI;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sane.router.support.ParentActivity;

import org.w3c.dom.Comment;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Joshua Johnston on 1/12/2018.
 */

//Manages_User_Interface_&_Delegates_Tasks_to_Lower_Level_UI_Classes____________________
public class UIManager implements Observer
{
    private Activity parentActivity;

    private Context context;

    //Singleton_Implementation____________________
    private static final UIManager ourInstance = new UIManager();//creates single instance

    private UIManager(){/*constructor stuff*/}//constructor, currently empty

    public static UIManager getInstance() {return ourInstance;}//typical get method

    //Methods____________________
    public void displayMessage(String message)//displays message
    {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void displayMessage(String message, int duration)//displays message for duration
    {
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private void setUpWidgets(){}//place holder, will be used to access on-screen widgets

    public void update(Observable observable, Object object)//necessary method of Observer
    {
        if(observable.hasChanged())
        {
            parentActivity = ParentActivity.getParentActivity();
            context = parentActivity.getBaseContext();
            setUpWidgets();
        }
    }
}
//____________________
