package com.sane.router.network.datagram;
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
        makeFrame("F1A5C0B1A5ED8008(text datagram)CC");
    }
    /**
     * The constructor generally used to make an LL2PFrame
     *
     * @return LL2PFrame frame - a frame of the LL2P
     */
    public LL2PFrame(String frame)
    {
        makeFrame(frame);
    }
    /**
     * Method of the constructor, general and default
     *
     * @param frame - a frame of the LL2P
     */
    private void makeFrame(String frame)
    {
        destinationAddress = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_DEST_ADDRESS_FIELD,
                frame.substring(2*Constants.LL2P_DEST_ADDRESS_OFFSET,
                2*Constants.LL2P_DEST_ADDRESS_OFFSET + 2*Constants.LL2P_ADDRESS_LENGTH));

        sourceAddress = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_SOURCE_ADDRESS_FIELD,
                frame.substring(2*Constants.LL2P_SOURCE_ADDRESS_OFFSET,
                2*Constants.LL2P_SOURCE_ADDRESS_OFFSET + 2*Constants.LL2P_ADDRESS_LENGTH));

        type = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_TYPE_FIELD,
                frame.substring(2*Constants.LL2P_TYPE_FIELD_OFFSET,
                2*Constants.LL2P_TYPE_FIELD_OFFSET + 2*Constants.LL2P_TYPE_FIELD_LENGTH));

        payload = HeaderFieldFactory.getInstance().getItem
                (Constants.LL2P_PAYLOAD_FIELD,
                frame.substring(2*Constants.LL2P_PAYLOAD_OFFSET,
                frame.length() - Constants.LL2P_CRC_FIELD_LENGTH));

        crc = HeaderFieldFactory.getInstance().getItem
                (Constants.CRC_FIELD,
                frame.substring(frame.length() - Constants.LL2P_CRC_FIELD_LENGTH));
    }
    /**
     * Makes a datagram payload field
     *
     * @return DatagramPayloadField - the payload of a datagram
     */
    private DatagramPayloadField makePayloadField()
    {
        return new DatagramPayloadField(new LL2PFrame("frame"));
    }
    //Getters (We know what these do.)
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
