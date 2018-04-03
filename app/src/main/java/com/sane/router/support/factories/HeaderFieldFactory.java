package com.sane.router.support.factories;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.datagram.ARPDatagram;
import com.sane.router.network.datagram.LRPPacket;
import com.sane.router.network.datagram.TextDatagram;
import com.sane.router.network.datagramFields.CRCField;
import com.sane.router.network.datagramFields.DatagramPayloadField;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.datagramFields.LL2PTypeField;
import com.sane.router.network.datagramFields.HeaderField;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.network.datagramFields.LRPCount;
import com.sane.router.network.datagramFields.LRPSequenceNumber;
import com.sane.router.network.datagramFields.NetworkDistancePair;

/**
 * A factory to create Header Field Objects
 *
 * @author Joshua Johnston
 */
public class HeaderFieldFactory implements Factory<HeaderField, String>
{
    //Singleton Implementation
    private static final HeaderFieldFactory ourInstance = new HeaderFieldFactory();
    public static HeaderFieldFactory getInstance() {return ourInstance;}
    private HeaderFieldFactory(){}

    //Methods
    /**
     * Interface factory method to create Header Field Objects
     *
     * @param type - the type to create (tracked as arbitrary Constants)
     * @param data - the data used to create the Header Field
     * @return <U extends HeaderField> - a nonspecific HeaderField created
     */
    public <U extends HeaderField> U getItem(int type, String data)
    {
        Log.i(Constants.LOG_TAG, "\n\nGettin Type "+type+" HeaderField with data:"+data+"\n\n");
        if (type == Constants.LL2P_DEST_ADDRESS_FIELD)
            return (U) new LL2PAddressField(data, false);
        else if (type == Constants.LL2P_SOURCE_ADDRESS_FIELD)
            return (U) new LL2PAddressField(data, true);
        else if (type == Constants.LL3P_DEST_ADDRESS_FIELD)
            return (U) new LL3PAddressField(data, false);
        else if (type == Constants.LL3P_SOURCE_ADDRESS_FIELD)
            return (U) new LL3PAddressField(data, true);
        else if (type == Constants.LL2P_TYPE_FIELD)
            return (U) new LL2PTypeField(data);
        else if (type == Constants.LL2P_TEXT_PAYLOAD_FIELD)
            return (U) new DatagramPayloadField(data);
        else if (type == Constants.CRC_FIELD)
            return (U) new CRCField(data);
        else if (type == Constants.LRP_COUNT)
            return (U) new LRPCount(data);
        else if (type == Constants.LRP_SEQUENCE_NUMBER)
            return (U) new LRPSequenceNumber(data);
        else if (type == Constants.NETWORK_DISTANCE_PAIR)
            return (U) new NetworkDistancePair(data);

        else if (type == Constants.LL3P_DEST_DATAGRAM_PAYLOAD_FIELD)
            return (U) new DatagramPayloadField(new ARPDatagram(data, false));
        else if (type == Constants.LL3P_SOURCE_DATAGRAM_PAYLOAD_FIELD)
            return (U) new DatagramPayloadField(new ARPDatagram(data, true));
        else if (type == Constants.LL2P_TYPE_LRP)
            return (U) new DatagramPayloadField(new LRPPacket(data));
        else if (type == Constants.LL2P_TYPE_ECHO_REQUEST)
            return (U) new DatagramPayloadField(new TextDatagram(data));
        else if (type == Constants.LL2P_TYPE_ECHO_REPLY)
            return (U) new DatagramPayloadField(new TextDatagram(data));

        //else if (type == Constants.LL2P_TYPE_ARP_REQUEST)
        //    return (U) new DatagramPayloadField(new ARPDatagram(data, true));

            Log.e(Constants.LOG_TAG, "Error creating HeaderField");

        return null;
    }
}
