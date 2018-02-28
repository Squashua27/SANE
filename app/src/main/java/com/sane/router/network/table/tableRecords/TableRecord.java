package com.sane.router.network.table.tableRecords;

/**
 * Interface defining table record specifications,
 * requirements of any class in keeping of records or tables
 *
 * @author Joshua Johnston
 */
public interface TableRecord
{
    /**
     * All table classes must keep and be prepared to return a key
     *
     * @return int - the key, an integer value
     */
    int getKey();
    /**
     * Returns the time in seconds since the table was last referenced,
     * if not applicable to the table in question, returns "0"
     *
     * @return int - the time in seconds since last table access
     */
    int getAgeInSeconds();
}
