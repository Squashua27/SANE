package com.sane.router.network.datagram;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.DatagramPayloadField;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.support.Utilities;
import com.sane.router.support.factories.HeaderFieldFactory;

/**
 * The Lab Layer 3 Protocol Datagram Class
 *        _______________________________________________
 * offset|___0___|___1___|___2___|___3___|___4___|___5___|
 *  0x00 |_Src_LL3_Addrs_|_Dst_LL3_Addrs_|______Type_____|
 *  0x06 |______ID_______|__TTL__|                       |
 *  0x0C |                                               |
 *  0x12 |          Payload (no set length)              |
 *  0x18 |                                               |
 *  0x2E |                                _______________|
 *  0x34 |_______________________________|____Checksum___|
 *
 *  @author Joshua Johnston
 */
public class LL3PDatagram implements Datagram
{
    //Fields
    private LL3PAddressField source;           //the Datagram's Layer 3 source address
    private LL3PAddressField destination;     //the Datagram's Layer 3 destination address
    private int type;                        //the LL3P type, only 0x8001 is valid
    private int identifier;                 //contains an identifier, value 0-65, 535
    private int ttl;                       //Time To Live, single byte, value 0-15
    private DatagramPayloadField payload; //the datagram's payload
    private String checksum;             //place-holder, 4 hex chars (2 bytes)

    //Methods
    public LL3PDatagram(String data)
    {
        HeaderFieldFactory factory = HeaderFieldFactory.getInstance(); //ref for field construction

        source = factory.getItem
                (Constants.LL3P_SOURCE_ADDRESS_FIELD,
                data.substring(2*Constants.LL3P_SOURCE_ADDRESS_OFFSET,
                2*(Constants.LL3P_SOURCE_ADDRESS_OFFSET + Constants.LL3P_ADDRESS_LENGTH)));

        destination = factory.getItem
                (Constants.LL3P_DEST_ADDRESS_FIELD,
                data.substring(2*Constants.LL3P_DEST_ADDRESS_OFFSET,
                2*(Constants.LL3P_DEST_ADDRESS_OFFSET + Constants.LL3P_ADDRESS_LENGTH)));

        type = Integer.valueOf(data.substring(2*Constants.LL3P_TYPE_FIELD_OFFSET,
                2*(Constants.LL3P_TYPE_FIELD_OFFSET + Constants.LL3P_TYPE_FIELD_LENGTH)),16);

        identifier = Integer.valueOf(data.substring(2*Constants.LL3P_ID_FIELD_OFFSET,
                2*(Constants.LL3P_ID_FIELD_OFFSET + Constants.LL3P_ID_FIELD_LENGTH)),16);

        ttl = Integer.valueOf(data.substring(2*Constants.LL3P_TTL_FIELD_OFFSET,
                2*(Constants.LL3P_TTL_FIELD_OFFSET + Constants.LL3P_TTL_FIELD_LENGTH)),16);

        payload = factory.getItem
                (type, data.substring(2*Constants.LL3P_PAYLOAD_FIELD_OFFSET,
                data.length() - 2*Constants.LL3P_CHECKSUM_FIELD_LENGTH));

        checksum = data.substring(data.length() - 2*Constants.LL3P_CHECKSUM_FIELD_LENGTH);
    }
    public LL3PDatagram(String message, int dest)
    {
        HeaderFieldFactory factory = HeaderFieldFactory.getInstance(); //ref for field construction

        source = factory.getItem(Constants.LL3P_SOURCE_ADDRESS_FIELD, Constants.LL3P_ADDRESS);

        destination = factory.getItem
                (Constants.LL3P_DEST_ADDRESS_FIELD, Utilities.padHexString
                (Integer.toHexString(dest),Constants.LL3P_ADDRESS_LENGTH));

        type = Constants.LL2P_TYPE_LL3P;

        identifier = 0xAAAA;

        ttl = Constants.LL3P_DATAGRAM_TTL;

        payload = factory.getItem(type, message);

        checksum = "CCCC";
    }

    /**
     * increments datagram TTL by 1
     */
    public void decrementTTL() {
        ttl++;
    }
    public boolean isExpired(){
        return (ttl > 15);
    }

    //Getters
    public int getSource() {
        return source.getLL3PAddress();
    }
    public int getDestination() {
        return destination.getLL3PAddress();
    }
    public int getDestinationNetwork(){
        return destination.getNetworkAddress();
    }
    public int getType() {
        return type;
    }
    public int getID() {
        return identifier;
    }
    public int getTTL() {
        return ttl;
    }
    public DatagramPayloadField getPayloadField(){
        return payload;
    }
    public Datagram getPayload() {
        return payload.getPayload();
    }
    public String getChecksum() {
        return checksum;
    }

    //To-String Methods
    private String sourceToTransmissionString(){
        return source.toTransmissionString();
    }
    private String destinationToTransmissionString(){
        return destination.toTransmissionString();
    }
    private String typeToTransmissionString(){
        return Utilities.padHexString(Integer.toHexString(type), Constants.LL3P_TYPE_FIELD_LENGTH);
}
    private String identifierToTransmissionString(){
        return Utilities.padHexString(Integer.toHexString(identifier), Constants.LL3P_ID_FIELD_LENGTH);
    }
    private String ttlToTransmissionString(){
        return Utilities.padHexString(Integer.toHexString(ttl), Constants.LL3P_TTL_FIELD_LENGTH);
    }
    private String payloadToTransmissionString(){
        return payload.toTransmissionString();
    }
    private String checksumToTransmissionString(){
        return Utilities.padHexString(checksum, Constants.LL3P_CHECKSUM_FIELD_LENGTH);
    }

    //Interface Implementation
    @Override public String toHexString()
    {
        return toTransmissionString();
    }
    @Override public String toProtocolExplanationString()
    {
        return "LL3P Source: 0x" + sourceToTransmissionString().toUpperCase() + " \n"
                + "LL3P Dest: 0x" + destinationToTransmissionString().toUpperCase() + " \n"
                + "Type: 0x" + typeToTransmissionString() + " \n"
                + "ID: " + identifierToTransmissionString() + " \n"
                + "TTL: " + ttl + " \n"
                + "Payload: \n" + payloadToTransmissionString() + " \n"
                + "Checksum: " + checksumToTransmissionString().toUpperCase();
    }
    @Override public String toSummaryString()
    {
        return    " Type: 0x" + typeToTransmissionString()
                + "   Source: 0x" + sourceToTransmissionString().toUpperCase()
                + "   Dest: 0x" + destinationToTransmissionString().toUpperCase();
    }
    @Override public String toTransmissionString()
    {
        return sourceToTransmissionString()
                + destinationToTransmissionString()
                + typeToTransmissionString()
                + identifierToTransmissionString()
                + ttlToTransmissionString()
                + payloadToTransmissionString()
                + checksumToTransmissionString();
    }
}
