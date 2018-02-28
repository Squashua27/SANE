package com.sane.router.UI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sane.router.R;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.Datagram;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.support.BootLoader;
import com.sane.router.support.FrameLogger;
import com.sane.router.support.ParentActivity;
import com.sane.router.support.Utilities;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages the sniffer side of the User Interfaces,
 * observes BootLoader & FrameLogger
 *
 * @author Joshua Johnston
 */
public class SnifferUI implements Observer
{
    //Fields
    private Activity parentActivity; //instance of the parent
    private Context context; //instance retrieved from activity's getBaseContext()
    private FrameLogger frameLogger; //instance of singleton FrameLogger class
    private ListView frameListView; //attached to the list of frames (top) ListView
    private TextView protocolTextView; //'' protocol description (middle) TextView
    private TextView byteDumpTextView; //'' the hex & ascii dump (bottom) TextView
    private SnifferFrameListAdapter adapter; //instance of the private class

    //Methods
    public SnifferUI() {} //empty constructor

    /**
     * sets up adaptor, ListView and TextViews
     */
    private void connectWidgets()
    {
        frameListView = parentActivity.findViewById(R.id.packetList);
        protocolTextView = parentActivity.findViewById(R.id.protocolDescription);
        byteDumpTextView = parentActivity.findViewById(R.id.hexDescription);

        adapter = new SnifferFrameListAdapter(context, frameLogger.getFrameList());
        frameListView.setAdapter(adapter);
        frameListView.setOnItemClickListener(showClickedFrame);

    }

    private AdapterView.OnItemClickListener showClickedFrame = new AdapterView.OnItemClickListener()
    {
        @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            Datagram frameToShow = frameLogger.getFrameList().get(i);//get frame to show

            Log.i(Constants.LOG_TAG, "\n \n \nSniffer Frame clicked"
                    + " - Displaying frame on Sniffer UI: \n"
                    + frameToShow.toProtocolExplanationString() + " \n \n");

            protocolTextView.setText(frameToShow.toProtocolExplanationString());
            byteDumpTextView.setText(Utilities.eightBytesPerLine(frameToShow.toHexString()));
        }
    };

    /**
     * The necessary method of the Observer interface, constructs class at the
     * prompting of the boot loader, updates UI when triggered by the frame logger
     *
     * @param observable - the observable triggering the update
     * @param o - an optional passed object
     */
    public void update(Observable observable, Object o)
    {
        if (observable instanceof BootLoader)
        {
            parentActivity = ParentActivity.getParentActivity(); //get parent reference
            context = parentActivity.getBaseContext(); //get context
            frameLogger = FrameLogger.getInstance(); //get frameLogger instance
            frameLogger.addObserver(this); //begin observing the frame logger
            connectWidgets(); //instantiates and connects UI objects
        }
        else if (observable instanceof FrameLogger)
            adapter.notifyDataSetChanged();
    }

    //Private classes of SnifferUI
    /**
     * A holder class that holds widgets (views) that make up a single
     * row in the sniffer top window
     */
    private static class ViewHolder
    {
        TextView packetNumber;
        TextView packetSummaryString;
    }

    /**
     * private adapter to display numbered rows from a ListView
     * object containing all frames transmitted or received,
     * instantiated above, note the constructor passes context and frameList
     */
    private class SnifferFrameListAdapter extends ArrayAdapter<LL2PFrame>
    {
        private ArrayList<LL2PFrame> frameList;//the ArrayList displayed by ListView rows.

        /*
        *  The constructor is passed the context and the arrayList.
        *  the arrayList is assigned to the local variable so its contents can be
        *  adapted to the listView.
        */
        public SnifferFrameListAdapter(Context context, ArrayList<LL2PFrame> frames)
        {
            super(context, 0, frames);
            frameList = frames;
        }
        /**
         * Here is where the work is performed to adapt a specific row in the arrayList to
         * a row on the screen.
         * @param position  - position in the array we're working with
         * @param convertView - a row View that passed in – has a view to use or a null object
         * @param parent - the main view that contains the rows.  Note that is is the ListView object.
         * @return View - the returned view
         */
        @Override public View getView(int position, View convertView, ViewGroup parent)
        {
            // First retrieve a frame object from the arrayList at the position we're working on
            LL2PFrame ll2PFrame = getItem(position);
            // declare a viewHolder - this simply is a single object to hold a two widgets
            ViewHolder viewHolder;

            /**
             * If convertView is null then we didn't get a recycled View, we have to create from scratch.
             * We do that here.
             */
            if (convertView == null)
            {
                // inflate the view defined in the layout xml file using an inflater we create here.
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.sniffer_frame_summary_row_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.packetNumber = (TextView) convertView.findViewById(R.id.snifferFrameNumberTextView);
                viewHolder.packetSummaryString = (TextView) convertView.findViewById(R.id.snifferItemTextView);
                convertView.setTag(viewHolder);
            } else {viewHolder = (ViewHolder) convertView.getTag();}

            viewHolder.packetNumber.setText(Integer.toString(position));
            viewHolder.packetSummaryString.setText(frameList.get(position).toSummaryString());
            return convertView;
        }

    }
}
