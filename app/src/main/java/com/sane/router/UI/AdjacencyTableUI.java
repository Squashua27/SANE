package com.sane.router.UI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.sane.router.network.Constants;
import com.sane.router.network.daemons.LL1Daemon;
import com.sane.router.network.daemons.LL2Daemon;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.network.table.TableInterface;
import com.sane.router.network.tableRecords.AdjacencyRecord;

/**
 * An extension of the SingleTableUI Class to handle the unique purpose
 * of the Adjacency Table
 *
 * @author Joshua Johnston
 */
public class AdjacencyTableUI extends SingleTableUI
{
    //Fields
    private LL1Daemon myPersonalDemon;//reference to the LL1 Daemon

    //Methods
    /**
     * A constructor, constructs Adjacency Table User Interface
     *
     * @param activity - the parent activity, passed to super constructor to provide context
     * @param table - the definitive table of a Single Table User Interface
     * @param tableListViewID - reference to the GUI ListView -the user's view of the table
     * @param tableManager - object to manage the instance of the table
     */
    public AdjacencyTableUI(Activity activity, int table,
                            TableInterface tableListViewID,
                            LL1Daemon tableManager)
    {
        super(activity, table, tableListViewID);//the super-class constructor
        myPersonalDemon = tableManager;//The demon manages the table - one hell of a butler
        listViewWidget.setOnItemClickListener(sendEchoRequest);
        listViewWidget.setOnItemLongClickListener(removeAdjacency);
    }

    /**
     * When an Adjacency Table entry it clicked,
     * transmits an LL2P echo request packet with the entry's addresses
     */
    private AdapterView.OnItemClickListener sendEchoRequest = new AdapterView.OnItemClickListener()
    {
        @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            AdjacencyRecord recordToSend = (AdjacencyRecord) recordList.get(i); //get record to send
            LL2PFrame echoRequest = new LL2PFrame //create echo request LL2P frame
                    (recordToSend.getLL2PAddressAsTransmissionString()
                    + Constants.LL2P_ADDRESS
                    + Constants.LL2P_TYPE_ECHO_REQUEST_HEX
                    + "(Echo request payload)" + "CCCC");

            LL2Daemon.getInstance().sendEchoRequest(recordToSend.getLL2PAddressAsTransmissionString());

            Log.i(Constants.LOG_TAG, "\n \n \nAdjacency Record clicked"
                    + " - Sending echo Request: \n"
                    + echoRequest.toProtocolExplanationString() + " \n \n");
        }
    };
    /**
     * Allows a user to remove an Adjacency Record by long clicking
     * on the User Interface Adjacency Table
     */
    private AdapterView.OnItemLongClickListener removeAdjacency = new AdapterView.OnItemLongClickListener()
    {
        @Override public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            AdjacencyRecord recordToRemove = (AdjacencyRecord) recordList.get(i);
            myPersonalDemon.removeAdjacency(recordToRemove);//removes the selected adjacency

            Log.i(Constants.LOG_TAG, " \n \nAdjacency Record long-clicked"
                    + " - Adjacency Record Removed:   "
                    + recordToRemove.toString() + " \n \n");

            return false;
        }
    };
}
