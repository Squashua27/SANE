package com.sane.router.UI;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sane.router.networks.table.Table;
import com.sane.router.networks.table.TableInterface;
import com.sane.router.networks.table.tableRecords.Record;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages a single table on the User Interface
 *
 * @author Joshua Johnston
 */
public class SingleTableUI implements Observer
{
    //Fields
    protected Activity parentActivity;
    protected Table table;
    protected List<Record> recordList; //list of displayed records
    protected ListView listViewWidget; //refers to on-screen ListView
    private ArrayAdapter arrayAdapter; //adapts the table for the ListView widget

    //Methods
    public SingleTableUI(Activity activity, int tableListViewID,
                         TableInterface tableToDisplay)
    {
        parentActivity = activity;
        table = (Table) tableToDisplay;
        arrayAdapter = new ArrayAdapter(parentActivity.getBaseContext(),
               android.R.layout.simple_list_item_1, table.getTableAsList());

        listViewWidget = (ListView) parentActivity.findViewById(tableListViewID);

        listViewWidget.setAdapter(arrayAdapter); // tell the widget its adapter

        recordList = table.getTableAsList();

        table.addObserver(this);
    }

    /**
     * Required method of the observer class, called by table object upon
     * change to table data
     *
     * @param observable - the observed object
     * @param object - an object passed by the triggering observed object
     */
    @Override public void update(Observable observable, Object object)
    {
        updateView();
    }

    /**
     * Reloads the array and updates the on-screen table
     */
    public void updateView()
    {
        parentActivity.runOnUiThread(new Runnable()
        {
            @Override public void run()// this is a mini-Runnable class’s run method!
            {
                arrayAdapter.notifyDataSetChanged();// notify the OS of change
            }
        });

    }
}
