package com.sane.router.network.daemons;

import com.sane.router.network.Constants;
import com.sane.router.network.table.RoutingTable;
import com.sane.router.network.table.TimedTable;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.support.BootLoader;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Routing Protocol Daemon, handles the methods and updating of Routing Records
 *
 * @author Joshua Johnston
 */
public class LRPDaemon extends Observable implements Observer, Runnable
{
    //Fields
    RoutingTable routingTable;

    //Singleton Implementation
    private static final LRPDaemon ourInstance = new LRPDaemon();
    public static LRPDaemon getInstance() {
        return ourInstance;
    }
    private LRPDaemon() {routingTable = new RoutingTable();
    }

    //Interface Implementation
    @Override public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)
        {
            routingTable = new RoutingTable();
        }
    }
    /**
     * Definitive method of the Runnable Interface, removes expired records at set interval
     */
    @Override public void run()
    {
        List<Record> removedRecords = routingTable.expireRoutes(Constants.LRP_RECORD_TTL);
        if (!removedRecords.isEmpty())
            notifyObservers(removedRecords);
    }
}
