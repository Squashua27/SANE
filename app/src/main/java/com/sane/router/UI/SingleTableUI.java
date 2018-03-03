package com.sane.router.UI;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sane.router.network.table.Table;
import com.sane.router.network.table.TableInterface;
import com.sane.router.network.table.tableRecords.Record;

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
    protected Activity parentActivity; //reference to the parent for context
    protected Table table; //the definitive table, the class's namesake
    protected List<Record> recordList; //list of displayed records
    protected ListView listViewWidget; //refers to on-screen ListView
    private ArrayAdapter arrayAdapter; //adapts the table for the ListView widget

    //Methods

    /**
     * Single Table UI Constructor, constructs Table management objects and connects
     * them to their corresponding Table User Interface widgets, keeps connected objects
     * updated
     *
     * @param activity - the parent activity, provides context
     * @param tableListViewID - identifies the ListView widget that displays the table
     * @param tableToDisplay - the table itself
     */
    public SingleTableUI(Activity activity, int tableListViewID,
                         TableInterface tableToDisplay)
    {
        parentActivity = activity; //get passed object
        table = (Table) tableToDisplay; //get passed object
        arrayAdapter = new ArrayAdapter(parentActivity.getBaseContext(),//synchronizing object
               android.R.layout.simple_list_item_1, table.getTableAsList());

        listViewWidget = (ListView)parentActivity.findViewById(tableListViewID);//on-screen table
        listViewWidget.setAdapter(arrayAdapter); // tell the widget its adapter

        recordList = table.getTableAsList();//retrieve the list from the table object

        table.addObserver(this);//Tells the table to observe (this o) for calls to update
    }

    /**
     * Required method of the observer class, called by table object upon
     * change to table data
     *
     * @param observable - the observed object
     * @param object - an object passed by the triggering observed object
     */
    @Override public void update(Observable observable, Object object) {updateView();}

    /**
     * Reloads the array and updates the on-screen table
     */
    public void updateView()
    {
        parentActivity.runOnUiThread(new Runnable()
        {// a mini-Runnable class’s run method:
            @Override public void run(){arrayAdapter.notifyDataSetChanged();}
        });

    }
}
