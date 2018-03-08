package com.sane.router.network.tableRecords;

import com.sane.router.support.Utilities;

/**
 * The base class of all table Class objects,
 * implements the TableRecord interface
 *
 * @author Joshua Johnston
 */
public class Record implements TableRecord
{
    //Fields
    private int lastTimeTouched;

    //Methods
    /**
     * A constructor, simply initializes time last touched to the current time
     */
    public Record()
    {
        updateTime();
    }
    /**
     * Sets time last touched to the current time
     */
    public void updateTime()
    {
        lastTimeTouched = Utilities.getTimeInSeconds();
    }
    /**
     * Makes a comparison between records.
     * If this record is greater: positive result
     * If this record is less: negative result
     *
     * @param record - the record to compare subject to
     *
     * @return int - the integer difference between two tables
     */
    public int compareTo(Record record)
    {
        return getKey() - record.getKey();
    }

    //Interface Implementation (see interface for method documentation)
    public int getKey()
    {
        return 0;
    }
    public int getAgeInSeconds()
    {
        int temp = Utilities.getTimeInSeconds() - lastTimeTouched;
        return Utilities.getTimeInSeconds() - lastTimeTouched;
    }
}
