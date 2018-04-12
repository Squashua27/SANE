package com.sane.router.network.datagram;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.DatagramPayloadField;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.support.Utilities;

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
    private String checkSum;             //place-holder, 4 hex chars (2 bytes)

    //Methods
    /**
     * increments datagram TTL by 1
     */
    public void incrementTTL() {
        this.ttl++;
    }

    //Getters
    public int getSource() {
        return source.getLL3PAddress();
    }
    public int getDestination() {
        return destination.getLL3PAddress();
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
    public String getCheckSum() {
        return checkSum;
    }

    //To-String Methods
    private String sourceToTransmissionString(){
        return source.toTransmissionString();
    }
    private String destinationToTransmissionString(){
        return destination.toTransmissionString();
    }
    private String typeToTransmissionString(){
        return Utilities.padHexString(Integer.toHexString(type), Constants.LL3P_TYPE_LENGTH);
}
    private String identifierToTransmissionString(){
        return Utilities.padHexString(Integer.toHexString(identifier), Constants.LL3P_ID_LENGTH);
    }
    private String ttlToTransmissionString(){
        return Utilities.padHexString(Integer.toHexString(ttl), Constants.LL3P_TTL_LENGTH);
    }
    private String payloadToTransmissionString(){
        return payload.toTransmissionString();
    }
    private String checkSumToTransmissionString(){
        return Utilities.padHexString(checkSum, Constants.LL3P_CHECKSUM_LENGTH);
    }

    //Interface Implementation
    @Override public String toHexString()
    {
        return toTransmissionString();
    }
    @Override public String toProtocolExplanationString()
    {
        return "LL3P Source: " +
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1
                + 1;
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
