package com.sane.router;

import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sane.router.UI.UIManager;
import com.sane.router.networks.Constants;
import com.sane.router.support.BootLoader;
import com.sane.router.support.Utilities;

/**
 * the main activity
 *
 * @author Joshua Johnston
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new BootLoader(this); //loads classes to begin router operation
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.showIPAddress)
        {
            //UIManager.raiseToast("Your IP address is "+ Constants.IP_ADDRESS);
            UIManager.getInstance().displayMessage("Your IP address is "+ Constants.IP_ADDRESS);
        }
        return super.onOptionsItemSelected(item);
    }
}
