package com.sane.router.network.datagram;
import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.CRCField;
import com.sane.router.network.datagramFields.DatagramPayloadField;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.datagramFields.LL2PTypeField;
import com.sane.router.support.factories.HeaderFieldFactory;

/**
 * The Lab Layer 2 Protocol frame Class
 *        _______________________________________________
 * offset|___0___|___1___|___2___|___3___|___4___|___5___|
 *  0x00 |___Dest_MAC_Address____|__Source_MAC_Address___|
 *  0x06 |__Type_Field___|                               |
 *  0x0C |                                               |
 *  0x12 |          Payload (no set length)              |
 *  0x18 |                                               |
 *  0x2E |                                _______________|
 *  0x34 |_______________________________|___CRC_Field___|
 *
 * @author Joshua Johnston
 */
public class LL2PFrame implements Datagram
{
    //Fields
    private LL2PAddressField destinationAddress;
    private LL2PAddressField sourceAddress;
    private LL2PTypeField type;
    private DatagramPayloadField payload;
    private CRCField crc;

    //Methods
    /**
     * Default constructor with no arguments, for testing
     *
     * @return LL2PFrame frame - a frame of the LL2P
     */
    public LL2PFrame()
    {
        makeFrame("F1A5C0B1A5ED8008(text datagram)CCCC");
    }
    /**
     * The constructor generally used to make an LL2PFrame
     *
     * @return LL2PFrame frame - a frame of the LL2P
     */
    public LL2PFrame(String frame)
    {
        Log.i(Constants.LOG_TAG, "\n\nConstructing a frame with default text payload...\n\n");
        makeFrame(frame);
        makePayloadField(frame);
    }
    /**
     * Constructor to frame a preconstructed datagram
     *
     * @param frame - The data with with to construct a frame
     * @param packet - The packet to frame
     */
    public LL2PFrame(String frame, Datagram packet)
    {
        Log.i(Constants.LOG_TAG, "\n\nConstructing a frame with around input packet...\n\n");
        makeFrame(frame);
        payload = new DatagramPayloadField(packet);
    }
    /**
     * Method of the constructor, constructs the simpler fields - all but the payload
     *
     * @param data - The data with which to construct the frame
     */
    private void makeFrame(String data)
    {
        destinationAddress = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_DEST_ADDRESS_FIELD,
                data.substring(2*Constants.LL2P_DEST_ADDRESS_OFFSET,
                2*Constants.LL2P_DEST_ADDRESS_OFFSET + 2*Constants.LL2P_ADDRESS_LENGTH));

        sourceAddress = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_SOURCE_ADDRESS_FIELD,
                data.substring(2*Constants.LL2P_SOURCE_ADDRESS_OFFSET,
                2*Constants.LL2P_SOURCE_ADDRESS_OFFSET + 2*Constants.LL2P_ADDRESS_LENGTH));

        type = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_TYPE_FIELD,
                data.substring(2*Constants.LL2P_TYPE_FIELD_OFFSET,
                2*Constants.LL2P_TYPE_FIELD_OFFSET + 2*Constants.LL2P_TYPE_FIELD_LENGTH));

        crc = HeaderFieldFactory.getInstance().getItem
                (Constants.CRC_FIELD,
                data.substring(data.length() - Constants.LL2P_CRC_FIELD_LENGTH*2));
    }
    /**
     * Constructs and populates the payload field when the constructor is given a string
     *
     * @return DatagramPayloadField - the payload of a datagram
     */
    private DatagramPayloadField makePayloadField(String frame)
    {
        String data = frame.substring
                (2*Constants.LL2P_PAYLOAD_OFFSET,
                frame.length() - 2*Constants.LL2P_CRC_FIELD_LENGTH);

        if (type.toTransmissionString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REQUEST_HEX)
                || type.toTransmissionString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REPLY_HEX))
        {
            //TODO: How do I know whether I'm sending a reply or request?
            payload = HeaderFieldFactory.getInstance().getItem(Constants.LL3P_SOURCE_DATAGRAM_PAYLOAD_FIELD,data);
        }
        else
            payload = HeaderFieldFactory.getInstance().getItem(Constants.LL2P_TEXT_PAYLOAD_FIELD,data);

        return payload;
    }

    //Getters
    public LL2PAddressField getDestinationAddress()
    {
        return destinationAddress;
    }
    public LL2PAddressField getSourceAddress()
    {
        return sourceAddress;
    }
    public LL2PTypeField getType()
    {
        return type;
    }
    public DatagramPayloadField getPayloadField()
    {
        return payload;
    }
    public CRCField getCrc()
    {
        return crc;
    }

    //Interface Implementation (see interface for function descriptions)
    @Override public String toString()
    {
        return destinationAddress.toTransmissionString()
                + sourceAddress.toTransmissionString()
                + type.toTransmissionString()
                + payload.toTransmissionString()
                + crc.toTransmissionString();
    }
    public String toHexString()
    {
        return destinationAddress.toHexString()
                + sourceAddress.toHexString()
                + type.toHexString()
                + payload.toHexString()
                + crc.toHexString();
    }
    public String toProtocolExplanationString()
    {
        return " " + destinationAddress.explainSelf() + "\n "
                + sourceAddress.explainSelf() + "\n "
                + type.explainSelf() + "\n "
                + payload.explainSelf() + "\n "
                + crc.explainSelf();
    }
    public String toSummaryString()
    {
        return    " Type: 0x" + type.toTransmissionString().toUpperCase()
                + "   Source: 0x" + sourceAddress.toTransmissionString().toUpperCase()
                + "   Dest: 0x" + destinationAddress.toTransmissionString().toUpperCase();
    }
    public String toTransmissionString()
    {
        return destinationAddress.toTransmissionString()
                + sourceAddress.toTransmissionString()
                + type.toTransmissionString()
                + payload.toTransmissionString()
                + crc.toTransmissionString();
    }
}
