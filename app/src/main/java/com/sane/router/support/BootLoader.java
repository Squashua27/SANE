package com.sane.router.support;

import android.app.Activity;
import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.networks.Constants;
import com.sane.router.networks.daemons.LL1Daemon;
import com.sane.router.networks.datagram.LL2PFrame;
import com.sane.router.networks.table.Table;
import com.sane.router.networks.table.tableRecords.AdjacencyRecord;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.Observable;

/**
 * creates classes, notifies classes when to begin operations
 *
 * @author Joshua Johnston
 */
public class BootLoader extends Observable
{
    //METHODS____________________
    /**
     * constructor, calls "bootRouter"
     *
     * @param parentActivity - activity reference allowing context-dependent operations
     */
    public BootLoader(Activity parentActivity)
    {
        bootRouter(parentActivity); //boots the router
    }

    /**
     * instantiates other Router classes, adds them as observers, and notifies them to operate
     *
     * @param parentActivity - activity reference allowing context-dependent operations
     */
    private void bootRouter(Activity parentActivity)
    {
        //Instantiate_Router_Classes____________________
        ParentActivity.setParentActivity(parentActivity);
        UIManager uiManager = UIManager.getInstance();

        //Create_Observer_List____________________
        addObserver(UIManager.getInstance());
        addObserver(FrameLogger.getInstance());
        addObserver(LL1Daemon.getInstance());
        addObserver(uiManager.getTableUI());

        //Misc_Router_Setup____________________
        //(place holder)

        //Notify_Observers____________________
        setChanged();//notify Java of change
        notifyObservers();//trigger update method in observers

        //Output____________________
        uiManager.displayMessage(Constants.ROUTER_NAME + " is up and running!");

        //Testing____________________
        test();
    }

    /**
     * Runs testing for debugging and quality control
     */
    private void test()
    {
        //Lab_3_Test____________________
        /**
        //Test: Create an LL2P frame, tests Classes and Header Factory
        LL2PFrame packet = new LL2PFrame("F1A5C0B1A5ED8008(text datagram)aCRC");
        Log.i(Constants.LOG_TAG," \n \n"+packet.toProtocolExplanationString()+" \n \n");

        //Test: Create an adjacency record, tests Classes and Table Record Factory
        String addressString = Constants.LL2P_ADDRESS + Constants.IP_ADDRESS;

        AdjacencyRecord record = TableRecordFactory.getInstance().getItem
                (Constants.ADJACENCY_RECORD, addressString);

        Log.i(Constants.LOG_TAG, record.toString());
        Log.i(Constants.LOG_TAG, " \n" + record.toString() + " \n \n");
         */

        //Lab_4_Test____________________
        //Test: Create various adjacency records
        Log.i(Constants.LOG_TAG,"\n \n Test: Adjacency Record Class~~~~~~~~~~~~~~~~~~~~\n ");
        AdjacencyRecord record0 = TableRecordFactory.getInstance().getItem
                (Constants.ADJACENCY_RECORD, Constants.LL2P_ADDRESS + Constants.IP_ADDRESS);
        Log.i(Constants.LOG_TAG, " \nrecord 0: " + record0.toString() + " \n");

        AdjacencyRecord record1 = TableRecordFactory.getInstance().getItem
                (Constants.ADJACENCY_RECORD, "ABCDEF" + "1.2.3.4");
        Log.i(Constants.LOG_TAG, " \nrecord 1: " + record1.toString() + " \n");

        AdjacencyRecord record2 = TableRecordFactory.getInstance().getItem
                (Constants.ADJACENCY_RECORD, "12D1E4" + "27.27.27.27");
        Log.i(Constants.LOG_TAG, " \nrecord 2: " + record2.toString() + " \n \n");

        //Test: Create an adjacency table, add and remove records
        Log.i(Constants.LOG_TAG,"\n \n Test: Adjacency Table Class~~~~~~~~~~~~~~~~~~~~");
        Table adjacencyTable = new Table();

        adjacencyTable.addItem(record0);
        Log.i(Constants.LOG_TAG, " \nAdjacency table, record0 added: "
                + adjacencyTable.toString() + " \n");

        adjacencyTable.addItem(record1);
        adjacencyTable.addItem(record2);
        Log.i(Constants.LOG_TAG, " \nAdjacency table, all records added: "
                + adjacencyTable.toString() + " \n");

        adjacencyTable.removeItem(record1);
        Log.i(Constants.LOG_TAG, " \nFull adjacency table, record1 removed: "
                + adjacencyTable.toString() + " \n");

        //Test: LL1Daemon
        Log.i(Constants.LOG_TAG,"\n \n Test: Lab Layer 1 Daemon Class~~~~~~~~~~~~~~~~~~~~");
        LL1Daemon myPersonalDemon = LL1Daemon.getInstance();

        myPersonalDemon.addAdjacency(Constants.LL2P_ADDRESS, Constants.IP_ADDRESS);//record0
        myPersonalDemon.addAdjacency("ABCDEF", "1.2.3.4");//record1
        myPersonalDemon.addAdjacency("12D1E4", "27.27.27.27");//record2

        Log.i(Constants.LOG_TAG, "\n \n Demon's table, all records added: "
                + myPersonalDemon.getAdjacencyTable().toString());

        myPersonalDemon.removeAdjacency("ABCDEF");
        Log.i(Constants.LOG_TAG, "\n \n Demon's table, record1 removed: "
                + myPersonalDemon.getAdjacencyTable().toString());

        //Test: Use the mirror
        myPersonalDemon.addAdjacency("112233","10.30.92.154");
        LL2PFrame frame = new LL2PFrame("112233B1A5ED8008(text datagram)aCRC");
        //Send a packet to the mirror:
        myPersonalDemon.sendFrame(frame);
    }
}

