package com.sane.router.network.datagram;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.network.datagramFields.LRPCount;
import com.sane.router.network.datagramFields.LRPSequenceNumber;
import com.sane.router.network.datagramFields.NetworkDistancePair;
import com.sane.router.support.factories.HeaderFieldFactory;

import java.util.List;

/**
 * A Lab Routing Protocol Datagram:
 *        _______________________________________________
 * offset|_____0_____|_____1_____|_____2_____|_____3_____|
 *  0x00 |______Source_LL3P______|Seq#_|Count|/\/\/\/\/\/|
 *  0x03 |___Net_#1__|__Dist_#1__|___Net_#2__|__Dist_#2__|
 *  0x07 |___Net_#3__|__Dist_#3__|___Net_#4__|__Dist_#4__|
 *  0x0B |___Net_#5__|__Dist_#5__|___Net_#6__|__Dist_#6__|
 *  0X0F |___Net_#7__|__Dist_#7__|...max_15_Net/Dist_Pairs
 *
 * @author Joshua Johnston
 */
public class LRPPacket implements Datagram
{
    //Fields
    private LL3PAddressField sourceLL3P;//LL3P address of source router
    private LRPSequenceNumber sequenceNumber;//Datagram's place in a sequence (max 15)
    private LRPCount count;//Number of hops from packet source
    private List<NetworkDistancePair> routes;//update list of network distance pairs

    private HeaderFieldFactory factory;//factory for creation  of header fields

    //Methods
    public LRPPacket(String data)//Constructor
    {
        sourceLL3P = factory.getItem
                (2*Constants.LL3P_SOURCE_ADDRESS_FIELD,
                data.substring(2*Constants.LL3P_SOURCE_OFFSET,
                2*Constants.LL3P_ADDRESS_LENGTH));

        sequenceNumber = factory.getItem
                (2*Constants.LRP_SEQUENCE_NUMBER,
                data.substring(2*Constants.LL3P_SEQ_AND_COUNT_OFFSET,
                2*Constants.LL3P_SEQ_AND_COUNT_OFFSET+1));

        count = factory.getItem
                (2*Constants.LRP_COUNT,
                data.substring(2*Constants.LL3P_SEQ_AND_COUNT_OFFSET+1,
                2*Constants.LL3P_SEQ_AND_COUNT_OFFSET+2));

        int length = data.length() - 2*Constants.LL3P_LIST_OFFSET;
        int pairCount = length/4;

        for (int pairIndex = 0; pairIndex < pairCount; pairIndex++)
        {
            routes.add( (NetworkDistancePair) factory.getItem
                    (Constants.NETWORK_DISTANCE_PAIR,
                    data.substring(2*Constants.LL3P_LIST_OFFSET+2*pairIndex*Constants.LL3P_ADDRESS_LENGTH,
                    2*Constants.LL3P_LIST_OFFSET+2*(pairIndex*Constants.LL3P_ADDRESS_LENGTH+1))));
        }
    }

    //Interface Implementation

    @Override public String toHexString()
    {
        return null;
    }
    @Override
    public String toProtocolExplanationString()
    {
        return null;
    }
    @Override public String toSummaryString()
    {
        return null;
    }
    @Override public String toTransmissionString()
    {
        return null;
    }
}
