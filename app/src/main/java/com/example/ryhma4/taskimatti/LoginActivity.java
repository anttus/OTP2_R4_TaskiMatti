package com.example.ryhma4.taskimatti;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by mikae on 30.1.2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.btnLogin) {
            TextView txtTest = (TextView)findViewById(R.id.txtTest);
            txtTest.setText("Login button success!");
        }
    }

    private void createAccount() {

    }

    private void signIn() {

    }

    private void signOut() {

    }

    private boolean validateForm() {
        return false;
    }

    private void updateUI(FirebaseUser user) {

    }
}
