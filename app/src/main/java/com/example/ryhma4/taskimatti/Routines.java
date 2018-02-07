package com.example.ryhma4.taskimatti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Routines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
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
                startActivity(new Intent(Routines.this, ContentActivity.class));
                Toast.makeText(Routines.this, "Main", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(Routines.this, Settings.class));
                Toast.makeText(Routines.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_setTasks:
                startActivity(new Intent(Routines.this, SetTasks.class));
                Toast.makeText(Routines.this, "Set Tasks", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_routines:
                Toast.makeText(Routines.this, "Already in Routines", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
