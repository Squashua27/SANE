package com.sane.router.networks.table;

import com.sane.router.networks.tableRecords.TableRecord;

import java.util.List;

/**
 * Interface regulating the operations of the table class object,
 * a structure that will be composed of table record class objects
 *
 * @author Joshua Johnston
 */
public interface TableInterface
{
    //Interface Methods
    /**
     * Returns complete Table contents as a list of TableRecords
     *
     * @return List<TableRecord> - a list of table records
     */
    public List<TableRecord> getTableAsList();
    /**
     * Adds a TableRecord to the Table, throws exception on failure
     *
     * @param recordToAdd - TableRecord to be added
     *
     * @return TableRecord - the TableRecord added
     */
    public TableRecord addItem(TableRecord recordToAdd);
    /**
     * Returns a TableRecord matching the specified item, throws exception on failure
     *
     * @param recordToGet - the TableRecord to search for and return
     *
     * @return TableRecord - the found TableRecord to be returned
     */
    public TableRecord getItem(TableRecord recordToGet);
    /**
     * Removes from table and returns a record matching a given key
     *
     * @param recordKey - key of the record to be removed
     *
     * @return TableRecord -  the removed record, null if key not found
     */
    public TableRecord removeItem(Integer recordKey);
    /**
     * Finds and returns a record matching the given record key,
     * throws an exception if matching key is not found
     *
     * @param recordKey - the key of the record to get
     *
     * @return TableRecord - the gotten TableRecord
     */
    public TableRecord getItem(Integer recordKey);
    /**
     * Clears a Table, removes all TableRecords
     */
    public void clear();
}
