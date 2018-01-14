package com.sane.router.support;

/**
 * Created by Joshua Johnston on 1/13/2018.
 */

//Generic_Exception_Class____________________
public class LabException extends Exception
{
    private static final long serialVersionUID = 1L;

    public LabException(String errorMessage)//constructor
    {
        super (errorMessage);
    }
}
