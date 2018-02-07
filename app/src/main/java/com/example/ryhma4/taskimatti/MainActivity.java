package com.example.ryhma4.taskimatti;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_create_routine);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.action_main:
                startActivity(new Intent(this, MainActivity.class));
                setContentView(R.layout.activity_main);
                Toast.makeText(this, "Main", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                setContentView(R.layout.activity_settings);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_setTasks:
                startActivity(new Intent(this, SetTasks.class));
                setContentView(R.layout.activity_set_tasks);
                Toast.makeText(this, "Already in Set Tasks", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_routines:
                startActivity(new Intent(this, Routines.class));
                setContentView(R.layout.activity_routines);
                Toast.makeText(this, "Routines", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_signOut:
                LoginActivity la = new LoginActivity();
                la.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
