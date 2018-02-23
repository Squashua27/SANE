package com.sane.router.networks.datagram;

/**
 * Interface to be implemented by all datagrams
 *
 * @author Joshua Johnston
 */
public interface Datagram
{
    /**
     * Returns datagram field contents as string suitable for transmission
     *
     * @return String - datagram contents as string
     */
    @Override String toString();
    /**
     * Returns datagram field contents as hex string - often used by snifferUI
     *
     * @return String - datagram contents as hex string
     */
    String toHexString();
    /**
     * Returns a string containing a complete description of all datagram fields,
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
