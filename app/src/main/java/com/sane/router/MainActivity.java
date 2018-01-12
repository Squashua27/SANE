package com.sane.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sane.router.support.Bootloader;

// Comment test
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Bootloader(this);
    }
}
