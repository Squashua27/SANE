package com.sane.router.UI;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.sane.router.networks.Constants;
import com.sane.router.networks.daemons.LL1Daemon;
import com.sane.router.networks.datagram.LL2PFrame;
import com.sane.router.networks.table.TableInterface;
import com.sane.router.networks.table.tableRecords.AdjacencyRecord;

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
    public AdjacencyTableUI(Activity activity, int table,
                            TableInterface tableInterface,
                            LL1Daemon tableManager)
    {
        super(activity, table, tableInterface);//the super-class constructor
        myPersonalDemon = tableManager;//The demon manages the table - one hell of a butler
        listViewWidget.setOnItemClickListener(sendEchoRequest);
        listViewWidget.setOnItemLongClickListener(removeAdjacency);
    }

    private AdapterView.OnItemClickListener sendEchoRequest = new AdapterView.OnItemClickListener()
    {
        @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            AdjacencyRecord recordToSend = (AdjacencyRecord) recordList.get(i); //get record to send
            LL2PFrame echoRequest = new LL2PFrame //create echo request LL2P frame
                    (recordToSend.getLL2PAddressAsTransmissionString()
                    + Constants.LL2P_ADDRESS
                    + Constants.LL2P_TYPE_ECHO_REPLY_HEX
                    + "(Echo request payload)" + "aCRC");

            myPersonalDemon.sendFrame(echoRequest);

            Log.i(Constants.LOG_TAG, "\n Adjacency Record clicked - Sending echo Request: "
                    + echoRequest.toProtocolExplanationString() + "\n ");
        }
    };

    private AdapterView.OnItemLongClickListener removeAdjacency = new AdapterView.OnItemLongClickListener() {
        @Override public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            return false;
        }
    };
}
