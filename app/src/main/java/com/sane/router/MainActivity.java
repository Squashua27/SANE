package com.sane.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sane.router.UI.UIManager;
import com.sane.router.UI.dialogs.AddAdjacencyDialog;
import com.sane.router.networks.Constants;
import com.sane.router.support.BootLoader;

/**
 * the main activity
 *
 * @author Joshua Johnston
 */
public class MainActivity extends AppCompatActivity implements AddAdjacencyDialog.AdjacencyPairListener
{
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BootLoader(this); //loads classes to begin router operation
    }

    /**
     * override of the standard Java method - executed when the options menu is created
     *
     * @param menu - the created options menu
     */
    @Override public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * override of the standard Java method - executes when an options menu item is selected
     *
     * @param item - the menu item selected
     */
    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.showIPAddress)
        {
            UIManager.getInstance().displayMessage("Your IP address is "+ Constants.IP_ADDRESS);
        }
        else if (item.getItemId() == R.id.addAdjacency)
        {
            AddAdjacencyDialog dialog = new AddAdjacencyDialog();
            dialog.show(getFragmentManager(), "add_adjacency");
        }
        return super.onOptionsItemSelected(item);
    }
    public void onFinishedEditDialog(String string1, String string2)
    {
        //ToDo: call LL1Daemon's addAdjacency method
    }
}
