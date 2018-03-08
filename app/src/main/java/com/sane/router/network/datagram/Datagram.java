package com.sane.router.network.datagram;

/**
 * Interface to be implemented by all Datagram classes
 *
 * @author Joshua Johnston
 */
public interface Datagram
{
    /**
     * Returns datagram field contents as a string
     *
     * @return String - datagram contents as a string
     */
    @Override String toString();
    /**
     * Returns Datagram field contents as a hex string - often used by snifferUI
     *
     * @return String - datagram contents as a hex string
     */
    String toHexString();
    /**
     * Returns a string containing a complete description of all Datagram Fields,
     * also displays encapsulated datagrams as applicable
     *
     * @return String - datagram explanation as a string
     */
    String toProtocolExplanationString();
    /**
     * Returns a single-line summary of a datagram's top-level protocol,
     * used by Sniffer UI to display datagram short summaries
     *
     * @return String - top-level protocol summary as a string
     */
    String toSummaryString();
    /**
     * Returns a string representing a datagram in LAB format ready for transmission,
     * lab format: all numbers in appropriately padded hex, all strings readable ascii
     *
     * @return String - complete datagram ready for transmission
     */
    String toTransmissionString();
}