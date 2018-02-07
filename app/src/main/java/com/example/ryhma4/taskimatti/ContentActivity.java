package com.example.ryhma4.taskimatti;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
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
                Toast.makeText(ContentActivity.this, "Already in Main", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(ContentActivity.this, Settings.class));
                Toast.makeText(ContentActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_setTasks:
                startActivity(new Intent(ContentActivity.this, SetTasks.class));
                Toast.makeText(ContentActivity.this, "Set Tasks", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_routines:
                startActivity(new Intent(ContentActivity.this, Routines.class));
                Toast.makeText(ContentActivity.this, "Routines", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
