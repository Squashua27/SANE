package com.sane.router.networks.datagram;
import com.sane.router.networks.Constants;
import com.sane.router.networks.datagramFields.CRC;
import com.sane.router.networks.datagramFields.DatagramPayloadField;
import com.sane.router.networks.datagramFields.LL2PAddressField;
import com.sane.router.networks.datagramFields.LL2PTypeField;

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
    private CRC crc;

    //Methods
    /**
     * A constructor with no arguments, for testing
     *
     * @return LL2PFrame frame - a frame of the LL2P
     */
    public LL2PFrame()
    {
        destinationAddress = new LL2PAddressField("CAFCAF", false);
        sourceAddress = new LL2PAddressField(Constants.LL2P_ADDRESS, false);
        type = new LL2PTypeField(Constants.LL2P_TYPE_TEXT);
        payload = new DatagramPayloadField("Datagram payload");
        crc = new CRC(type.toTransmissionString());
    }
    /**
     * The constructor, generally used to make an LL2PFrame
     *
     * @return LL2PFrame frame - a frame of the LL2P
     */
    public LL2PFrame(String frame)
    {
        destinationAddress = new LL2PAddressField(frame.substring
                (2*Constants.LL2P_DEST_ADDRESS_OFFSET,
                2*Constants.LL2P_DEST_ADDRESS_OFFSET + 2*Constants.LL2P_ADDRESS_LENGTH),
                false);

        sourceAddress = new LL2PAddressField(frame.substring
                (2*Constants.LL2P_SOURCE_ADDRESS_OFFSET,
                2*Constants.LL2P_SOURCE_ADDRESS_OFFSET + 2*Constants.LL2P_ADDRESS_LENGTH-1),
                true);

        type = new LL2PTypeField(frame.substring
                (2*Constants.LL2P_TYPE_FIELD_OFFSET,
                2*Constants.LL2P_TYPE_FIELD_OFFSET + 2*Constants.LL2P_TYPE_FIELD_LENGTH-1));

        payload = new DatagramPayloadField(frame.substring(2*Constants.LL2P_PAYLOAD_OFFSET,
                frame.length() - 2*Constants.LL2P_CRC_FIELD_LENGTH-1));

        crc = new CRC(frame.substring
                (frame.length() - 2*Constants.LL2P_SOURCE_ADDRESS_OFFSET));
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
    public DatagramPayloadField getPayload()
    {
        return payload;
    }
    public CRC getCrc()
    {
        return crc;
    }

    //Interface Implementation (see interface for function descriptions)
    @Override
    public String toString()
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
        return destinationAddress.explainSelf() + "\n"
                + sourceAddress.explainSelf() + "\n"
                + type.explainSelf() + "\n"
                + payload.explainSelf() + "\n"
                + crc.explainSelf();
    }
    public String toSummaryString()
    {
        return    "type: 0x" + type.toTransmissionString()
                + "  source: 0x" + sourceAddress.toTransmissionString()
                + "  dest: 0x" + destinationAddress.toTransmissionString();
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
