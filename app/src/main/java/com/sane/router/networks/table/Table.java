package com.sane.router.networks.table;

import com.sane.router.networks.table.tableRecords.Record;
import com.sane.router.networks.table.tableRecords.TableRecord;
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
    @Override public String toString()
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
     * The constructor for a new empty list, creates a table such that it can contain
     * any one type of table record and is viable in a multi-threaded environment
     */
    public Table()
    {
        table = Collections.synchronizedList(new ArrayList<Record>());
    }
    public void updateDisplay()
    {
        setChanged();//notify Java of change
        notifyObservers();//trigger update method in observers
    }
    //Interface Implementation
    /**
     * Returns complete Table contents as a list of Records
     *
     * @return List<Record> - a list of table records
     */
    public List<Record> getTableAsList()
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
    public Record addItem(Record recordToAdd)
    {
        if (!table.add(recordToAdd))
            new LabException("Failed to perform Record.addItem(recordToAdd)");
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
    public Record getItem(Record recordToGet)
    {
        Iterator<Record> recordIterator = table.iterator();
        Record targetRecord;

        while (recordIterator.hasNext())
        {
            targetRecord = recordIterator.next();
            if (targetRecord == recordToGet)
                return targetRecord;
        }
        new LabException("Failed to perform Record.getItem(recordToGet)");
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
    public Record getItem(Integer recordKey)
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
        new LabException("Failed to perform Record.getItem(recordKey)");
        return null;
    }
    /**
     * Removes from table and returns a record matching a given key
     *
     * @param recordKey - key of the record to be removed
     *
     * @return Record -  the removed record, null if key not found
     */
    public Record removeItem(Integer recordKey)
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
    public Record removeItem(Record recordToRemove)
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
        new LabException("Failed to perform Record.removeItem(recordToRemove)");
        return null;
    }
    /**
     * Clears a Table, removes all TableRecords
     */
    public void clear()
    {
        table.clear();
    }
}
