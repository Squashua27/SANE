package com.sane.router.support;

import android.app.Activity;

/**
 * Created by Joshua Johnston on 1/12/2018.
 */

//
public class ParentActivity
{
    private static Activity parentActivity;

    public static Activity getParentActivity()
    {
        return parentActivity;
    }

    public static void setParentActivity(Activity newParentActivity)
    {
        parentActivity = newParentActivity;
    }
}
