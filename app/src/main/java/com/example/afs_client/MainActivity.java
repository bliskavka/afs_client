package com.example.afs_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements BluetoothStatusListener {

    BluetoothManager manager;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Menu menu;

    private final int DISCONNECT_BUTTON = 2;
    private final int RECONNECT_BUTTON = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setSubtitle(getString(R.string.connecting_subtitle));

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new ViewPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.monitor_tab);
                            break;
                        case 1:
                            tab.setText(R.string.stats_tab);
                            break;
                        case 2:
                            tab.setText(R.string.log_tab);
                            break;
                    }
                }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.main_menu, menu);
        menu.getItem(DISCONNECT_BUTTON).setVisible(false);
        menu.getItem(RECONNECT_BUTTON).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getSupportActionBar().setSubtitle(getString(R.string.connecting_subtitle));
            manager.connect();
        }
        if (item.getItemId() == R.id.action_disconnect) manager.disconnect();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (manager == null){
                manager = BluetoothManager.getInstance(this);
                manager.setBluetoothStatusListener(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
        manager.attach();
    }

    @Override
    public void onStop() {
        manager.detach();
        super.onStop();
    }

    @Override
    public void onBluetoothDisabled() {
        Toast.makeText(this, R.string.bt_disabled_toast, Toast.LENGTH_LONG).show();
        getSupportActionBar().setSubtitle(getString(R.string.not_connected_subtitle));
    }

    @Override
    public void onConnected() {
        Toast.makeText(this, R.string.connected_toast, Toast.LENGTH_SHORT).show();
        getSupportActionBar().setSubtitle(getString(R.string.connected_subtitle));
        menu.getItem(DISCONNECT_BUTTON).setVisible(true);
        menu.getItem(RECONNECT_BUTTON).setVisible(false);
    }

    @Override
    public void onConnectionFailed() {
        Toast.makeText(this, R.string.con_failed_toast, Toast.LENGTH_LONG).show();
        getSupportActionBar().setSubtitle(getString(R.string.not_connected_subtitle));
        if (menu != null) {
            menu.getItem(DISCONNECT_BUTTON).setVisible(false);
            menu.getItem(RECONNECT_BUTTON).setVisible(true);
        }
    }

    @Override
    public void onConnectionLost() {
        Toast.makeText(this, R.string.con_lost_toast, Toast.LENGTH_LONG).show();
        getSupportActionBar().setSubtitle(getString(R.string.not_connected_subtitle));
        menu.getItem(DISCONNECT_BUTTON).setVisible(false);
        menu.getItem(RECONNECT_BUTTON).setVisible(true);
    }

}
