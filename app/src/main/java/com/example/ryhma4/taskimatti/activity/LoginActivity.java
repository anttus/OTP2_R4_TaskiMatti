package com.example.ryhma4.taskimatti.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryhma4.taskimatti.R;
import com.example.ryhma4.taskimatti.Controller.Database;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Locale;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String TAG_GOOGLE = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private EditText inputEmail;
    private EditText inputPassword;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private Database db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_main);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_login_test))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        AppCompatButton btnLogin = findViewById(R.id.btnLogin);
        AppCompatButton btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        TextView btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(buttonListener);
        btnLoginGoogle.setOnClickListener(buttonListener);
        btnSignUp.setOnClickListener(buttonListener);

        mAuth = FirebaseAuth.getInstance();
        db = new Database();

    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId() /*to get clicked view id**/) {
                case R.id.btnLogin:
                    signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
                    break;
                case R.id.btnLoginGoogle:
                    signInGoogle();
                    break;
                case R.id.btnSignUp:
                    createAccount(inputEmail.getText().toString(), inputPassword.getText().toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    /**
     * Create account with email and password
     * @param email User's email address
     * @param password User's password
     */
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) { return; }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            db.userExists(user);

                            sendEmailVerification();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
        // [END create_user_with_email]
    }

    /**
     * Sign in to the app with Google's account that's stored on the device
     * @param acct The Google account that is logged in on the device
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_GOOGLE, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.userExists(user);

                            launchMainActivity();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_GOOGLE, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            //nackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Sign in with email and password
     * @param email User's email address
     * @param password User's password
     */
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, R.string.auth_success,
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            launchMainActivity();

                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.w(TAG_GOOGLE, "Attempting Google sign in" + Integer.toString(requestCode));
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.w(TAG_GOOGLE, "Try started...");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.w(TAG_GOOGLE, "account = ");

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_GOOGLE, "Google " + R.string.auth_failed, e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getResources().getString(R.string.error_field_required_short));
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getResources().getString(R.string.error_field_required_short));
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

    private void sendEmailVerification() {
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    R.string.action_verification_sent + " " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    R.string.action_verification_failed + " ",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    /**
     * Updating the UI after signing up or signing in.
     * Works as the "Smart lock" so that after signing in the app is already signed in when opening it
     * @param user The instance of current Firebase user
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            if (!user.isEmailVerified()) {
                Toast.makeText(LoginActivity.this,
                        R.string.action_verify_email,
                        Toast.LENGTH_SHORT).show();
            } else {
                launchMainActivity();
            }
        } else {
            Toast.makeText(LoginActivity.this,
                    R.string.action_signed_out,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
