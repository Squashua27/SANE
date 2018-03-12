package com.sane.router.network.datagram;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.network.datagramFields.LRPCount;
import com.sane.router.network.datagramFields.LRPSequenceNumber;
import com.sane.router.network.datagramFields.NetworkDistancePair;
import com.sane.router.support.factories.HeaderFieldFactory;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public LRPPacket(String data)//Constructor used when deconstructing a received packet
    {
        factory = HeaderFieldFactory.getInstance();

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
    public LRPPacket(int ll3p, int seqNum, int cnt, List<NetworkDistancePair> pairs)
    {
        factory = HeaderFieldFactory.getInstance();

        sourceLL3P = factory.getItem
                (Constants.LL3P_SOURCE_ADDRESS_FIELD,
                Integer.toHexString(ll3p));

        sequenceNumber = factory.getItem
                (Constants.LRP_SEQUENCE_NUMBER,
                Integer.toHexString(seqNum));

        count = factory.getItem(Constants.LRP_COUNT, Integer.toHexString(cnt));

        for (NetworkDistancePair route : pairs)
            routes.add((NetworkDistancePair) factory.getItem
                    (Constants.NETWORK_DISTANCE_PAIR,
                    route.toTransmissionString()));
    }//Constructor used when creating an packet by field value to send to another router
    public LL3PAddressField getSourceLL3P(){return sourceLL3P;} //typical getter
    public LRPSequenceNumber getSequenceNumber(){return sequenceNumber;} //typical getter
    public LRPCount getCount(){return count;} //typical getter
    public List<NetworkDistancePair> getRoutes(){return routes;} //typical getter
    public int getRouteCount(){ return routes.size(); }
    /**
     * Gets the LRPPacket as a byte array suitable for transmission
     *
     * @return byte[] - the transmission string as a byte array
     */
    public byte[] getBytes() { return toTransmissionString().getBytes(); }

    //Interface Implementation
    @Override public String toHexString()
    {
        String hexString = sourceLL3P.toHexString()
                + sequenceNumber.toHexString()
                + count.toHexString();

        for (NetworkDistancePair route : routes)
            hexString += route.toHexString();

        return hexString;

    }
    @Override public String toProtocolExplanationString()
    {
        String explanation = "Source LLP: " + sourceLL3P.toHexString()
                + " \nSequence Number: " + sequenceNumber.toTransmissionString()
                + " \nCount: " + count.toTransmissionString() + " \nRoutes: ";

        for (NetworkDistancePair route : routes)
            explanation += route.toTransmissionString() + " \n        ";

        return explanation;
    }
    @Override public String toSummaryString()
    {
        String summary = "Source LLP: " + sourceLL3P.toHexString()
                + " \nSequence Number: " + sequenceNumber.toTransmissionString()
                + " \nCount: " + count.toTransmissionString() + " \nRoutes: ";

        for (NetworkDistancePair route : routes)
            summary += route.toTransmissionString() + " \n        ";

        return summary;
    }
    @Override public String toTransmissionString()
    {
        String packet = sourceLL3P.toTransmissionString()
                + sequenceNumber.toTransmissionString()
                + count.toTransmissionString();

        for (NetworkDistancePair route : routes)
            packet += route.toTransmissionString();

        return packet;
    }
}
