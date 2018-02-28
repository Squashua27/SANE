package com.sane.router.support.factories;

import android.util.Log;

import com.sane.router.networks.Constants;
import com.sane.router.networks.datagramFields.CRCField;
import com.sane.router.networks.datagramFields.DatagramPayloadField;
import com.sane.router.networks.datagramFields.LL2PAddressField;
import com.sane.router.networks.datagramFields.LL2PTypeField;
import com.sane.router.networks.datagramFields.HeaderField;
import com.sane.router.networks.datagramFields.LL3PAddressField;

/**
 * A factory to create Header Field Objects
 *
 * @author Joshua Johnston
 */
public class HeaderFieldFactory implements Factory<HeaderField, String>
{
    //Singleton Implementation
    private static final HeaderFieldFactory ourInstance = new HeaderFieldFactory();
    public static HeaderFieldFactory getInstance() {return ourInstance;}
    private HeaderFieldFactory(){}

    /**
     * Interface factory method to create Header Field Objects
     *
     * @param type - the type to create (tracked as arbitrary Constants)
     * @param data - the data used to create the Header Field
     *
     * @return <U extends HeaderField> - a nonspecific HeaderField created
     */
    public <U extends HeaderField> U getItem(int type, String data)
    {
        if (type == Constants.LL2P_DEST_ADDRESS_FIELD)
            return (U) new LL2PAddressField(data, false);
        else if (type == Constants.LL2P_SOURCE_ADDRESS_FIELD)
            return (U) new LL2PAddressField(data, true);
        else if (type == Constants.LL3P_DEST_ADDRESS_FIELD)
            return (U) new LL3PAddressField(data, false);
        else if (type == Constants.LL3P_SOURCE_ADDRESS_FIELD)
            return (U) new LL3PAddressField(data, true);
        else if (type == Constants.LL2P_TYPE_FIELD)
            return (U) new LL2PTypeField(data);
        else if (type == Constants.LL2P_PAYLOAD_FIELD)
            return (U) new DatagramPayloadField(data);
        else if (type == Constants.CRC_FIELD)
            return (U) new CRCField(data);
        else
            Log.e(Constants.LOG_TAG, "Error creating HeaderField");
        return null;
    }
}
