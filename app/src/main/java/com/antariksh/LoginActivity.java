package com.antariksh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.antariksh.helper.SharedPrefManager;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private Button buttonLogin,buttonForgotPassword;
    private AwesomeValidation awesomeValidation;
    private LoginButton loginButtonfb;
    private CallbackManager callbackManager;
    private SharedPrefManager prefManager;
    private ProgressDialog progressDialog;
    private TextView textViewForgotPassword,newRegistration;
    private EditText editTextForgotPasswordEmail;

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        user = mAuth.getCurrentUser();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        prefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        newRegistration = (TextView) findViewById(R.id.newRegistration);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        buttonLogin= (Button) findViewById(R.id.buttonLogin);
        loginButtonfb= (LoginButton) findViewById(R.id.loginfb);
        callbackManager=CallbackManager.Factory.create();
        textViewForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.email_error);

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View v = getLayoutInflater().inflate(R.layout.forgotpassword,null);
                editTextForgotPasswordEmail= (EditText) v.findViewById(R.id.forgotPasswordEmail);
                buttonForgotPassword = (Button) v.findViewById(R.id.forgotPasswordButton);

                buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            mAuth.sendPasswordResetEmail(editTextForgotPasswordEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Password reset link has been sucessfully sent to your e-mail",Toast.LENGTH_LONG).show();
                                    builder.create().dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(getApplicationContext(),"Error sending e-mail",Toast.LENGTH_LONG).show();
                                }
                            });
                    }
                });
                builder.setView(v);
                builder.create().show();
            }
        });


        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate())
                {
                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString()).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            user=authResult.getUser();
                            startActivity(new Intent(LoginActivity.this,MasterActivity.class));
                            finish();

                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_LONG).show();
                }

            }
        });

        loginButtonfb.setReadPermissions("email","public_profile");
        loginButtonfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog.setTitle("Logging You in...");
                progressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Error: ",error.toString());
                Toast.makeText(getApplicationContext(),"error: "+error.toString(),Toast.LENGTH_LONG).show();

            }
        });
        newRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View v = getLayoutInflater().inflate(R.layout.newregistration,null);
                builder.setView(v);
                builder.show();

            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
       AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                        prefManager.setLoggedIn(true);
                    if (progressDialog!=null)
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,MasterActivity.class));
                        finish();
                        } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
        }
                });
    }

}
