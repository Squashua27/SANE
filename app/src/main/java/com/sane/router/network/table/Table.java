package com.sane.router.network.table;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.TableRecord;
import com.sane.router.support.LabException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * A Table of TableRecords, implements TableInterface, extends Observable
 *
 * @author Joshua Johnston
 */
public class Table extends Observable implements TableInterface
{
    //Fields
    protected List<Record> table; //the class's definitive list

    //Methods
    /**
     * The constructor for a new empty list, creates a table such that it can contain
     * any one type of table record and is viable in a multi-threaded environment
     */
    public Table()
    {
        table = Collections.synchronizedList(new ArrayList<Record>());
    }
    @Override public synchronized String toString()
    {
        Iterator<Record> recordIterator = table.iterator();
        String returnString = new String();
        while (recordIterator.hasNext())
        {
            returnString += recordIterator.next().toString()+"\n ";
        }
        new LabException("Failed to perform Record.getItem(recordToGet)");
        return returnString;
    }
    /**
     * Notifies observers of a change, called by all methods that touch the table
     */
    public synchronized void updateDisplay()
    {
        setChanged();//notify Java of change
        notifyObservers();//trigger update method in observers
    }
    /**
     * Returns complete Table contents as a list of Records
     *
     * @return List<Record> - a list of table records
     */
    public synchronized List<Record> getTableAsList()
    {
        return table;
    }
    /**
     * Adds a TableRecord to the Table, throws exception on failure
     *
     * @param recordToAdd - TableRecord to be added
     *
     * @return TableRecord - the TableRecord added
     */
    public synchronized Record addItem(Record recordToAdd)
    {
        table.add(recordToAdd);
        updateDisplay();
        return recordToAdd;
    }
    /**
     * Returns a Record matching the specified item, throws exception on failure
     *
     * @param recordToGet - the TableRecord to search for and return
     *
     * @return Record - the found TableRecord to be returned
     */
    public synchronized Record getItem(Record recordToGet)
    {
        Iterator<Record> recordIterator = table.iterator();
        Record targetRecord;

        while (recordIterator.hasNext())
        {
            targetRecord = recordIterator.next();
            if (targetRecord == recordToGet)
                return targetRecord;
        }
        Log.i(Constants.LOG_TAG, "Failed to find record: " + recordToGet.toString());
        return null;
    }
    /**
     * Finds and returns a record matching the given record key,
     * throws an exception if matching key is not found
     *
     * @param recordKey - the key of the record to get
     *
     * @return Record - the gotten Record
     */
    public synchronized Record getItem(Integer recordKey)
    {
        Iterator<Record> recordIterator = table.iterator();
        Record targetRecord;

        while (recordIterator.hasNext())
        {
            targetRecord = recordIterator.next();
            if (targetRecord.getKey() == recordKey)
            {
                return targetRecord;
            }
        }
        Log.i(Constants.LOG_TAG, "Failed to perform Record.getItem(recordKey).");
        return null;
    }
    /**
     * Removes from table and returns a record matching a given key
     *
     * @param recordKey - key of the record to be removed
     *
     * @return Record -  the removed record, null if key not found
     */
    public synchronized Record removeItem(Integer recordKey)
    {
        Iterator<Record> recordIterator = table.iterator();
        Record targetRecord;
        while (recordIterator.hasNext())
        {
            targetRecord = recordIterator.next();
            if (targetRecord.getKey() == recordKey)
            {
                recordIterator.remove();
                updateDisplay();
                return targetRecord;
            }
        }
        new LabException("Failed to perform Record.removeItem(recordKey)");
        return null;
    }
    /**
     * Removes from table and returns a record matching a given record
     *
     * @param recordToRemove - key of the record to be removed
     *
     * @return Record -  the removed record, null if Record not found
     */
    public synchronized Record removeItem(TableRecord recordToRemove)
    {
            Iterator<Record> recordIterator = table.iterator();
            Record targetRecord;
            while (recordIterator.hasNext())
            {
                targetRecord = recordIterator.next();
                if (targetRecord == recordToRemove)
                {
                    recordIterator.remove();
                    updateDisplay();
                    return targetRecord;
                }
            }
            Log.i(Constants.LOG_TAG, "Failed to perform Record.removeItem(recordToRemove)");
            return null;
    }
    /**
     * Clears a Table, removes all TableRecords
     */
    public synchronized void clear()
    {
        table.clear();
        updateDisplay();
    }
}