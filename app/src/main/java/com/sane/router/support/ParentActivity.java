package com.sane.router.support;

import android.app.Activity;

/**
 * This class manages reference to the parent activity.  Without this reference
 * to the parent activity, other classes cannot perform context-dependent operations.
 *
 * @author Joshua Johnston
 */
public class ParentActivity
{
    //Fields
    private static Activity parentActivity; //the reference to the parent activity

    //Methods
    /**
     * typical get method, returns parentActivity
     *
     * @return parentActivity - reference to the parent activity
     */
    public static Activity getParentActivity()
    {
        return parentActivity;
    }
    /**
     * typical set method, sets parentActivity
     *
     * @param newParentActivity - new reference to current parent activity
     */
    public static void setParentActivity(Activity newParentActivity)
    {
        parentActivity = newParentActivity;
    }
}
