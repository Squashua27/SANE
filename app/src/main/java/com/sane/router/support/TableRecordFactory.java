package com.sane.router.support;

import com.sane.router.networks.Constants;
import com.sane.router.networks.tableRecords.AdjacencyRecord;
import com.sane.router.networks.tableRecords.Record;
import com.sane.router.networks.tableRecords.TableRecord;

import java.net.InetAddress;

/**
 * A factory to create Table Record Objects
 *
 * @author Joshua Johnston
 */
public class TableRecordFactory implements Factory
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
            return new Record();
        else //if (type == Constants.ADJACENCY_RECORD)
            return new AdjacencyRecord(Integer.valueOf(data), Constants.IP_ADDRESS);
    }
}
