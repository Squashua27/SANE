package com.sane.router.support.factories;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.table.tableRecords.AdjacencyRecord;
import com.sane.router.network.table.tableRecords.Record;
import com.sane.router.network.table.tableRecords.TableRecord;

/**
 * A factory to create Table Record Objects
 *
 * @author Joshua Johnston
 */
public class TableRecordFactory implements Factory<TableRecord, String>
{
    private static final TableRecordFactory ourInstance = new TableRecordFactory();
    public static TableRecordFactory getInstance() {
        return ourInstance;
    }
    private TableRecordFactory(){}

    /**
     * Interface factory method to create Header Field Objects
     *
     * @param type - the type to create (tracked as arbitrary Constants)
     * @param data - the data used to create a Table Record
     *
     * @return <U extends TableRecord> - nonspecific created TableRecord
     */
    public <U extends TableRecord> U getItem(int type, String data)
    {
        if (type == Constants.RECORD)
            return (U) new Record();
        else if (type == Constants.ADJACENCY_RECORD)
            return (U) new AdjacencyRecord(data);
        else
        Log.e(Constants.LOG_TAG, "Error creating TableRecord");
        return null;
    }

}
