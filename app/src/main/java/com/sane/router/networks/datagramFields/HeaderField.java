package com.sane.router.networks.datagramFields;

/**
 * Interface providing translation of header fields to various text formats
 *
 * @author Joshua Johnston
 */
public interface HeaderField
{
    /**
     * returns a header field's contents as a string
     *
     * @return String - the string value of a given header field
     */
    String toString();
    /**
     * returns a header field's contents as a transmission string
     *
     * @return String - segment of transmission string corresponding to a given header field
     */
    String toTransmissionString();
    /**
     * returns a header field's contents as a hex string
     *
     * @return String - the hex string value of a given header field
     */
    String toHexString();
    /**
     * asks a field to explain itself
     *
     * @return String - the explanation of a given header field
     */
    String explainSelf();
    /**
     * returns a header field's contents as an ASCII string
     *
     * @return String - the ASCII string value of a given header field
     */
    String toASCIIString();
}
