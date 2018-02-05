package com.sane.router.networks.table;

import com.sane.router.networks.tableRecords.Record;
import com.sane.router.networks.tableRecords.TableRecord;

import java.util.List;

/**
 * Interface regulating the operations of the table class object,
 * a structure that will be composed of record class objects
 *
 * @author Joshua Johnston
 */
public interface TableInterface
{
    //Interface Methods
    /**
     * Returns complete Table contents as a list of Records
     *
     * @return List<TableRecord> - a list of table records
     */
    public List<Record> getTableAsList();
    /**
     * Adds a Record to the Table, throws exception on failure
     *
     * @param recordToAdd - Record to be added
     *
     * @return TableRecord - the Record added
     */
    public Record addItem(Record recordToAdd);
    /**
     * Returns a Record matching the specified item, throws exception on failure
     *
     * @param recordToGet - the Record to search for and return
     *
     * @return TableRecord - the found Record to be returned
     */
    public Record getItem(Record recordToGet);
    /**
     * Removes from table and returns a record matching a given key
     *
     * @param recordKey - key of the record to be removed
     *
     * @return TableRecord -  the removed record, null if key not found
     */
    public Record removeItem(Integer recordKey);
    /**
     * Finds and returns a record matching the given record key,
     * throws an exception if matching key is not found
     *
     * @param recordKey - the key of the record to get
     *
     * @return TableRecord - the gotten TableRecord
     */
    public Record getItem(Integer recordKey);
    /**
     * Clears a Table, removes all Records
     */
    public void clear();
}
