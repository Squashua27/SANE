package com.sane.router.network.table.tableRecords;

/**
 * The base class of all table classes,
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
        lastTimeTouched = Math.round(System.currentTimeMillis()/1000L);
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
        return Math.round(System.currentTimeMillis()/1000L) - lastTimeTouched;
    }
}
