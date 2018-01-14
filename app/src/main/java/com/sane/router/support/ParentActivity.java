package com.sane.router.support;

import android.app.Activity;

/**
 * Created by Joshua Johnston on 1/12/2018.
 */

//Parent_activity_reference_manager____________________
public class ParentActivity
{
    private static Activity parentActivity; //the reference to the parent activity

    public static Activity getParentActivity() //typical get method
    {
        return parentActivity;
    }

    public static void setParentActivity(Activity newParentActivity) //typical set method
    {
        parentActivity = newParentActivity;
    }
}
