package com.example.ryhma4.taskimatti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_main:
                startActivity(new Intent(Settings.this, ContentActivity.class));
                Toast.makeText(Settings.this, "Main", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(Settings.this, "Already in Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_setTasks:
                startActivity(new Intent(Settings.this, SetTasks.class));
                Toast.makeText(Settings.this, "Set Tasks", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_routines:
                startActivity(new Intent(Settings.this, Routines.class));
                Toast.makeText(Settings.this, "Routines", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
