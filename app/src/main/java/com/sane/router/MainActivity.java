package com.sane.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sane.router.support.BootLoader;

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
}
