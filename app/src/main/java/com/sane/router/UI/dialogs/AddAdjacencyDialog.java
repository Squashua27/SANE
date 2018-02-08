package com.sane.router.UI.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    public AddAdjacencyDialog(){}

    /**
     * An Override of the required method - executed on creation of a view
     *
     * @param inflater - the inflater object to inflate the view
     * @param container - the container
     * @param savedInstanceState - a saved instance of the state
     * @return View - returns the created view
     */
    @Nullable @Override public View onCreateView
            (LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        //ToDo: View rootView = inflater.inflate(R.layout.add_adjacency_layout, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Override of the required method - executed on dialog creation, calls constructor
     *
     * @param savedInstanceState - a saved instance of the state
     * @return Dialog - the created Dialog
     */
    @Override public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return super.onCreateDialog(savedInstanceState);
    }
}
