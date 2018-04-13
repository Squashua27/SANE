package com.sane.router.support;

import android.app.Activity;
import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.daemons.ARPDaemon;
import com.sane.router.network.daemons.LL1Daemon;
import com.sane.router.network.daemons.LL2Daemon;
import com.sane.router.network.daemons.LL3Daemon;
import com.sane.router.network.daemons.LRPDaemon;
import com.sane.router.network.daemons.Scheduler;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.network.datagram.LL3PDatagram;
import com.sane.router.network.datagram.LRPPacket;

import java.util.Observable;

/**
 * Creates Classes, notifies classes when to begin operations (Boots router)
 *
 * @author Joshua Johnston
 */
public class BootLoader extends Observable
{
    //Methods
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
        //Instantiate Router Classes
        ParentActivity.setParentActivity(parentActivity);
        UIManager uiManager = UIManager.getInstance();

        //Create Observer List
        addObserver(uiManager);
        addObserver(FrameLogger.getInstance());
        addObserver(LL1Daemon.getInstance());
        addObserver(LL2Daemon.getInstance());
        addObserver(LL3Daemon.getInstance());
        addObserver(ARPDaemon.getInstance());
        addObserver(LRPDaemon.getInstance());
        addObserver(uiManager.getTableUI());
        addObserver(uiManager.getSnifferUI());
        addObserver(Scheduler.getInstance());

        //Notify Observers
        setChanged();//notify Java of change
        notifyObservers();//trigger update method in observers

        //Output
        uiManager.displayMessage(Constants.ROUTER_NAME + " is up and running!");

        //Testing
        test(); //Test of BootLoader and other early classes
        //ARPDaemon.getInstance().testARP(); //Test of ARP structures and methods
    }
    /**
     * Runs various tests for debugging and quality control
     */
    private void test()
    {
        //Test: Lab 3
        /**
        //Test: Create an LL2P frame, tests Classes and Header Factory
        LL2PFrame packet = new LL2PFrame("F1A5C0B1A5ED8008(text datagram)CCCC");
        Log.i(Constants.LOG_TAG," \n \n"+packet.toProtocolExplanationString()+" \n \n");

        //Test: Create an adjacency record, tests Classes and Table Record Factory
        String addressString = Constants.LL2P_ADDRESS + Constants.IP_ADDRESS;

        AdjacencyRecord record = TableRecordFactory.getInstance().getItem
                (Constants.ADJACENCY_RECORD, addressString);

        Log.i(Constants.LOG_TAG, record.toString());
        Log.i(Constants.LOG_TAG, " \n" + record.toString() + " \n \n");

        //Test: Lab 4
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
        Log.i(Constants.LOG_TAG, "\n\nTesting with mirror...\n\n");
        Log.i(Constants.LOG_TAG, "\n\nJust kidding... Only talking to myself...\n\n");
        myPersonalDemon.addAdjacency("112233","10.30.92.154");
        LL2PFrame frame = new LL2PFrame("112233B1A5ED8008(text datagram)CCCC");
        //Send a packet to the mirror:
        myPersonalDemon.sendFrame(frame);



        //Test: Test Routing and Forwarding Table
        Log.i(Constants.LOG_TAG, " \n \nTesting Routing Table... \n \n");
        RoutingTable routingTable = new RoutingTable();
        RoutingTable forwardingTable = new RoutingTable();

        Log.i(Constants.LOG_TAG, " \n \n1.  Adding a route... \n \n");
        routingTable.addNewRoute(new RoutingRecord(1,2,3));
        routingTable.expireRecords(5); //make sure route is not immediately expired
//table should contain a record
        Log.i(Constants.LOG_TAG, " \n \n2.  Waiting for record to expire... \n \n");
        routingTable.expireRecords(2); //this should expire the route
//table should be empty (if you waited 2 seconds in debugging)
        Log.i(Constants.LOG_TAG, " \n \n3.  Check: Newer route added over faster route... \n \n");
        routingTable.addNewRoute(new RoutingRecord(1,3,1));
        routingTable.addNewRoute(new RoutingRecord(1,4,1));
//table should contain only one record
        routingTable.clear();
        Log.i(Constants.LOG_TAG, " \n \n4.  Check: getBestRoutes() returns single best route for each network... \n \n");

        routingTable.addNewRoute(new RoutingRecord(1,4,5));
        routingTable.addNewRoute(new RoutingRecord(1,3,7));
        routingTable.addNewRoute(new RoutingRecord(1,5,6));

        routingTable.addNewRoute(new RoutingRecord(2,8,1));
        routingTable.addNewRoute(new RoutingRecord(2,7,2));
        routingTable.addNewRoute(new RoutingRecord(2,6,7));

        routingTable.getBestRoutes();
//next hop 7 should be the best next hop for both networks.

        Log.i(Constants.LOG_TAG, " \n \n5.  Check: Re-adding an existing route updates TTL... \n \n");
        routingTable.addNewRoute(new RoutingRecord(1,4,5));
//RoutingRecord(1,4,5) should be the only freshly created record
        routingTable.clear();
        Log.i(Constants.LOG_TAG, " \n \n6.  Check: Removing all routes from one source... \n \n");
        routingTable.addNewRoute(new RoutingRecord(4,1,2));
        routingTable.addNewRoute(new RoutingRecord(4,3,4));
        routingTable.addNewRoute(new RoutingRecord(4,5,6));

        routingTable.addNewRoute(new RoutingRecord(5,1,2));
        routingTable.addNewRoute(new RoutingRecord(5,3,4));
        routingTable.addNewRoute(new RoutingRecord(5,5,6));

        routingTable.addNewRoute(new RoutingRecord(6,1,2));
        routingTable.addNewRoute(new RoutingRecord(6,3,4));
        routingTable.addNewRoute(new RoutingRecord(6,5,6));

        routingTable.removeRoutesFrom(5);
//Should return 3 routes from net 4, and 3 from 6

        Log.i(Constants.LOG_TAG, " \n \n7.  Check: Get best routes for forwarding table... \n \n");
        routingTable.clear();
        routingTable.addNewRoute(new RoutingRecord(10,2,7));
        routingTable.addNewRoute(new RoutingRecord(10,3,4));
        routingTable.addNewRoute(new RoutingRecord(10,5,6));

        routingTable.addNewRoute(new RoutingRecord(11,9,2));
        routingTable.addNewRoute(new RoutingRecord(11,4,7));
        routingTable.addNewRoute(new RoutingRecord(11,6,6));

        routingTable.addNewRoute(new RoutingRecord(12,6,2));
        routingTable.addNewRoute(new RoutingRecord(12,9,4));
        routingTable.addNewRoute(new RoutingRecord(12,5,7));

        forwardingTable.addRoutes(routingTable.getBestRoutes());
//Forwarding table should contain 3 records, next hops should all be 7

        Log.i(Constants.LOG_TAG, " \n \n8.  Check: Add more, better routes to routing table, update forwarding table... \n \n");
        routingTable.addNewRoute(new RoutingRecord(10,1,4));
        routingTable.addNewRoute(new RoutingRecord(11,2,4));
        routingTable.addNewRoute(new RoutingRecord(12,3,4));

        forwardingTable.addRoutes(routingTable.getBestRoutes());
        routingTable.clear();
//Now all forwarding table routes go through next hop: 4
*/

        /**
        //LAB_11_TESTING______________________________
        //LL1Daemon.getInstance().addAdjacency(Constants.LL2P_ADDRESS, Constants.IP_ADDRESS);
        LL1Daemon.getInstance().addAdjacency("f00d1e", "10.30.11.11");

        //LRPDaemon.getInstance().receiveNewLRP("09011205010900".getBytes(),100);
        LRPDaemon.getInstance().processLRPPacket(new LRPPacket("0a01010102"), Integer.valueOf("f00d1e",16));

        //Test: LL3PDatagram
        LL3PDatagram testPacket = new LL3PDatagram("0901"+"0101"+"8001"+"AAAA"+"07"+"01234567"+"CCCC");
        Log.i(Constants.LOG_TAG," \n \nProtocol explanation: \n" + testPacket.toProtocolExplanationString());
        Log.i(Constants.LOG_TAG," \n \nPacket Summary: \n" + testPacket.toSummaryString());
        Log.i(Constants.LOG_TAG," \n \nTransmission String: \n" + testPacket.toTransmissionString());

        //Test: LL3 (and LL2) Daemon
        LL3Daemon.getInstance().sendToNextHop(testPacket);

        LL3Daemon.getInstance().sendMessage("Taxation is theft.", Integer.valueOf("0101", 16));

        //Test: Receiving an LL3 Datagram
        LL3PDatagram packetNotForMe = new LL3PDatagram("0901"+"0101"+"8001"+"AAAA"+"07"+"This packet does not belong to me."+"CCCC");
        LL3PDatagram packetForMe = new LL3PDatagram("0101"+"0901"+"8001"+"AAAA"+"07"+"Packet GET!"+"CCCC");

        //LL2PFrame frameNotForMe = LL2Daemon.getInstance().processLL2PFrame(packetNotForMe);

        LL3Daemon.getInstance().sendToNextHop(packetNotForMe);
        LL3Daemon.getInstance().sendToNextHop(packetForMe);
        */
    }
}