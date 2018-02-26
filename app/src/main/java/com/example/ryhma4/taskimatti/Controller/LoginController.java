//package com.example.ryhma4.taskimatti.Controller;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.ryhma4.taskimatti.R;
//import com.example.ryhma4.taskimatti.activity.LoginActivity;
//import com.example.ryhma4.taskimatti.database.Database;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//
///**
// * Created by anttu on 26.2.2018.
// */
//
//public class LoginController extends AppCompatActivity {
//    private LoginActivity login;
//    private static final String TAG = "EmailPassword";
//    private static final String TAG_GOOGLE = "GoogleActivity";
//    private static final int RC_SIGN_IN = 9001;
//
//    private GoogleSignInClient mGoogleSignInClient;
//
//    private FirebaseAuth mAuth;
//    private Database db;
//
//    public LoginController(LoginActivity login, FirebaseAuth mAuth, GoogleSignInClient mGoogleSignInClient) {
//        Log.wtf("CTRL", "Controller initialized");
//        this.login = login;
//        Log.wtf("CTRL", "Login done...");
////
////        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                .requestIdToken(getString(R.string.google_login_test))
////                .requestEmail()
////                .build();
////
////        Log.wtf("CTRL", "GSO done...");
////
////        mGoogleSignInClient = GoogleSignIn.getClient(login, gso);
//        this.mGoogleSignInClient = mGoogleSignInClient;
//        Log.wtf("CTRL", "GoogleSingIn done...");
//
//        this.mAuth = mAuth;
//        Log.wtf("CTRL", "mAuth...");
//        db = new Database();
//        Log.wtf("CTRL", "Controller initialization done.");
//    }
//
//    public void googleSignIn() {
//        Log.wtf("GSIG", "Started sign in");
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        Log.wtf("GSIG", "Started activity");
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            Log.w(TAG_GOOGLE, "Attempting Google sign in" + Integer.toString(requestCode));
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                Log.w(TAG_GOOGLE, "Try started...");
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.w(TAG_GOOGLE, "account = ");
//
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG_GOOGLE, "Google sign in failed", e);
//                // [START_EXCLUDE]
//                login.updateUI(null);
//                // [END_EXCLUDE]
//            }
//        }
//    }
//
//
//    public void signIn(String email, String password) {
//        Log.d(TAG, "signIn:" + email);
//
//        if (!login.validateForm()) {
//            return;
//        }
//
//        // [START sign_in_with_email]
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            Toast.makeText(login, "Kirjautuminen onnistui.",
//                                    Toast.LENGTH_SHORT).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            login.launchMainActivity();
//
//                            login.updateUI(user);
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(login, "Kirjautuminen epäonnistui.",
//                                    Toast.LENGTH_SHORT).show();
//                            login.updateUI(null);
//                        }
//                    }
//                });
//        // [END sign_in_with_email]
//    }
//
//    public void createAccount( String email, String password) {
//        Log.d(TAG, "createAccount:" + email);
//        if (!login.validateForm()) { return; }
//
//        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            db.userExists(user);
//
//                            sendEmailVerification();
//                            login.updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(login, "Authentication failed",
//                                    Toast.LENGTH_SHORT).show();
//                            login.updateUI(null);
//                        }
//
//                    }
//                });
//        // [END create_user_with_email]
//    }
//
//    private void firebaseAuthWithGoogle (GoogleSignInAccount acct) {
//        Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG_GOOGLE, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            db.userExists(user);
//
//                            login.launchMainActivity();
//
//                            login.updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG_GOOGLE, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(login, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //nackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            login.updateUI(null);
//                        }
//                    }
//                });
//    }
//
//    private void sendEmailVerification() {
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        if (task.isSuccessful()) {
//                            Toast.makeText(login,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(login,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        // [END_EXCLUDE]
//                    }
//                });
//        // [END send_email_verification]
//    }
//
//    public void signOut() {
//        mAuth.signOut();
//        login.updateUI(null);
//    }
//}
