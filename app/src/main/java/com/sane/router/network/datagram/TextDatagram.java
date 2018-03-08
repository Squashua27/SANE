package com.sane.router.network.datagram;

/**
 * A type of datagram containing only text
 *
 * @author Joshua Johnston
 */
public class TextDatagram implements Datagram
{
    //Fields
    private String payload;

    //Methods
    /**
     * Typical constructor method, creates a Text datagram given text
     *
     * @param text - the contents of the Text Datagram to construct
     */
    public TextDatagram(String text)
    {
        payload = text;
    }
    /**
     * Typical constructor method, creates a Text datagram with a default String
     */
    public TextDatagram()
    {
        payload = "(Text Datagram)";
    } //the default Text Datagram
    public String getPayload() //Typical get method
    {
        return payload;
    }
    public String setTextPayload(String text) //Typical set method
    {
        payload = text;
        return null;
    }

    //Interface Implementation (See Interface for method documentation)
    @Override public String toString()
    { return payload; }
    public String toHexString()
    { return payload; }
    public String toProtocolExplanationString()
    { return "Payload: " + payload; }
    public String toSummaryString()
    { return payload; }
    public String toTransmissionString()
    { return payload; }
}
