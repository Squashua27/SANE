package com.sane.router.support;

import com.sane.router.networks.daemons.LL1Daemon;
import com.sane.router.networks.datagram.LL2PFrame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Keeps a list of LL2PFrames, updates SnifferIU upon changes
 *
 * @author Joshua Johnston
 */
public class FrameLogger extends Observable implements Observer
{
    //Fields
    private ArrayList<LL2PFrame> frameList;

    //Singleton Implementation
    private static final FrameLogger ourInstance = new FrameLogger();
    public static FrameLogger getInstance() {
        return ourInstance;
    }
    private FrameLogger()
    {
        frameList = new ArrayList<LL2PFrame>();
    }

    //Methods
    public ArrayList<LL2PFrame> getFrameList(){return frameList;}//standard getter

    /**
     * The necessary Observer method, becomes Observable on bootLoader prompt,
     * notifies own Observers of change upon receiving & adding LL2P frame
     *
     * @param observable - the observed object
     * @param object - the optional passed object
     */
    public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)
            addObserver(LL1Daemon.getInstance());
        if (object instanceof LL2PFrame)
        {
            frameList.add((LL2PFrame)object);
            setChanged();
            notifyObservers();
        }
    }

}
