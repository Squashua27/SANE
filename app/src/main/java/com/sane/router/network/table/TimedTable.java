package com.sane.router.network.table;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.table.tableRecords.Record;
import com.sane.router.network.table.tableRecords.TableRecord;
import com.sane.router.support.LabException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An extension of the Table Class providing additional functionality
 * to allow for the handling of time-sensitive Table Records
 *
 * @author Joshua Johnston
 */
public class TimedTable extends Table
{
    //Methods
    public TimedTable(){super();}//Constructor

    /**
     * Removes (expires) all table entries older than a given maximum age
     *
     * @param maxAge - max allowable record age in seconds
     * @return removedRecordList - the list of expired, removed records
     */
    public List<Record> expireRecords(int maxAge)
    {
        List<Record> removedRecordList = Collections.synchronizedList(new ArrayList<Record>());
        Record targetRecord;

        Iterator<Record> recordIterator = table.iterator();//Iterator to create list to remove
        while (recordIterator.hasNext())
        {
            targetRecord = recordIterator.next();
            if (targetRecord.getAgeInSeconds() > maxAge)
                removedRecordList.add(targetRecord);//add target to list of removed records
        }

        for (TableRecord recordToRemove: removedRecordList)
            table.remove(recordToRemove);//remove target

        return removedRecordList;
    }

    /**
     * Updates the age of the record matching a given key,
     * making the record 0 seconds old (Record.lastTimeTouched = 0)
     *
     * @param key - table element identifier, LL2P address for ARP Records
     */
    public boolean touch(Integer key)
    {
        Iterator<Record> recordIterator = table.iterator();
        Record targetRecord;
        while (recordIterator.hasNext())
        {
            targetRecord = recordIterator.next();
            if (key == targetRecord.getKey())
            {
                targetRecord.updateTime();//touches the record
                Log.i(Constants.LOG_TAG, "\nRecord found and touched...\n\n");
                return true;//record appropriately touched, exit loop and method
            }
        }
        //If no record matching the passed key is found, throw exception:
        new LabException("Failed to perform TimedTable.touch(key)");
        Log.i(Constants.LOG_TAG, "\nRecord touching failure...\n\n");
        return false;
    }
}
