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

    public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)
            addObserver(LL1Daemon.getInstance());
        if (observable instanceof LL1Daemon)
        {
            frameList.add((LL2PFrame) object);
            setChanged();
            notifyObservers();
        }
    }

}
