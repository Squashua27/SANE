package com.sane.router.support;

/**
 * a generic subtype of the generic Exception class, will handle thrown exceptions
 *
 * @author Joshua Johnston
 */
public class LabException extends Exception
{
    //Fields
    private static final long serialVersionUID = 1L;
    /**
     * Exception constructor, produces an error message
     *
     * @param errorMessage - tbe error message
     */
    //Methods
    public LabException(String errorMessage)//constructor
    {
        super (errorMessage);
    }
}