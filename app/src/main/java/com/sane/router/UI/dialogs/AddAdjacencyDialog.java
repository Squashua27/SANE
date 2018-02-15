package com.sane.router.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sane.router.R;
import com.sane.router.networks.Constants;
import com.sane.router.networks.daemons.LL1Daemon;
import com.sane.router.networks.datagramFields.LL2PAddressField;
import com.sane.router.networks.table.tableRecords.AdjacencyRecord;
import com.sane.router.support.factories.TableRecordFactory;

/**
 * Dialog providing an interface for user to add adjacency records
 *
 * @author Joshua Johnston
 */
public class AddAdjacencyDialog extends DialogFragment
{
    //Fields
    private EditText IPAddressEditText; //on-screen widget holding user-entered IP address
    private EditText LL2PAddressEditText;//on-screen widget holding user-entered LL2P address
    private Button addAdjacencyButton;//button to add adjacency, closes dialog
    private Button cancelButton;//button to close dialog

    public interface AdjacencyPairListener
    {
        void onFinishedEditDialog(String ipAddress, String ll2PAddress);
    }

    //Methods
    /**
     * The constructor: empty
     */
    public AddAdjacencyDialog()
    {
        Log.i(Constants.LOG_TAG,"\n \n Called AddAdjacencyDialog constructor... \n ");
    }

    /**
     * An Override of the required method - executed on creation of a view,
     * inflates a view and ties layout fields to corresponding Java classes
     *
     * @param inflater - the inflater object to inflate the view
     * @param container - the container
     * @param savedInstanceState - a saved instance of the state
     * @return View - returns the created view
     */
    @Nullable @Override public View onCreateView
            (LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        Log.i(Constants.LOG_TAG,"\n \n Got inside AddAdjacencyDialog.onCreateView... \n ");

        final View rootView = inflater.inflate //inflates view
                (R.layout.add_adjacency, container, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Adjacency Dialog");
        builder.create();

        //Connect XML layout file widgets to Java class objects
        LL2PAddressEditText = rootView.findViewById(R.id.LL2PEditText);
        IPAddressEditText = rootView.findViewById(R.id.IPEditText);
        addAdjacencyButton = rootView.findViewById(R.id.addAdjacencyButton);
        cancelButton = rootView.findViewById(R.id.cancelButton);

        /**
         * Event handler for a click on the Add Adjacency button,
         * works with the LL1 Daemon to add an adjacency, then dismisses dialog
         */
        addAdjacencyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                Log.i(Constants.LOG_TAG,"\n \n Add Adjacency Button clicked... \n ");

                LL1Daemon.getInstance().addAdjacency(LL2PAddressEditText.getText().toString(),
                        IPAddressEditText.getText().toString());

                Log.i(Constants.LOG_TAG, " \nAdjacency Table: "
                        + LL1Daemon.getInstance().getAdjacencyTable().toString() + " \n \n");
                dismiss();
            }
        });

        /**
         * Event handler for a click on the cancel button, dismisses dialog
         */
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                Log.i(Constants.LOG_TAG,"\n \n Add Adjacency Dialog canceled... \n");
                dismiss();
            }
        });

        return rootView;
    }

    /**
     * Override of the required method - executed on dialog creation, calls constructor
     *
     * @param savedInstanceState - a saved instance of the state
     * @return Dialog - the created Dialog
     */
    @Override public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Log.i(Constants.LOG_TAG,"\n \n Creating AddAdjacency Dialog... \n ");

        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Add Adjacency Dialog");
        //builder.create();
        return super.onCreateDialog(savedInstanceState);
    }
}
