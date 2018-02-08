package com.sane.router.support.factories;

/**
 * Factory Interface to be implemented by all Factory Classes
 *
 * @author Joshua Johnston
 */
public interface Factory<T, S>
{
    /**
     * Gets the desired object of the specified type
     *
     * type - the type of object to be created (arbitrary integers in Constants)
     * data - data used to construct the object
     * object - the created and returned object
     *
     * @author Joshua Johnston
     */
    <U extends T> U getItem(int type, S data);
}
