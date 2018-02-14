package com.sane.router.UI;

import android.app.Activity;
import android.view.View;

import com.sane.router.networks.daemons.LL1Daemon;
import com.sane.router.networks.table.TableInterface;

/**
 * Created by Joshua Johnston on 2/14/2018.
 */

public class AdjacencyTableUI extends SingleTableUI
{
    //Fields
    private LL1Daemon myPersonalDemon;//reference to the LL1 Daemon

    //Methods
    public AdjacencyTableUI(Activity activity, int table,
                            TableInterface tableInterface, LL1Daemon tableManager)
    {
        super(activity, table, tableInterface);//the super-class constructor
        myPersonalDemon = tableManager;//The demon manages the table - one hell of a butler
    }

    //private View.OnItemClickListener sendEchoRequest(){}

    //private View.OnItemLongClickListener removeAdjacency(){}
}
