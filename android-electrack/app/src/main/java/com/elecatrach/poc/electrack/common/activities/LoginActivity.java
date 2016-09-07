package com.elecatrach.poc.electrack.common.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.activities.AdminMainActivity;
import com.elecatrach.poc.electrack.client.activity.MainActivity;
import com.elecatrach.poc.electrack.client.gcm.RegistrationIntentService;
import com.elecatrach.poc.electrack.common.application.ConnectionDetector;
import com.elecatrach.poc.electrack.common.application.Const;
import com.elecatrach.poc.electrack.common.application.ProgressGenerator;
import com.elecatrach.poc.electrack.common.managers.PrefManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements ProgressGenerator
        .OnCompleteListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String GCM_TOKEN = "gcmToken";

    private EditText mEmailText;
    private EditText mPasswordText;
    TextView mSignUpTextView;
    ImageView logo_image;
    SharedPreferences preferences;

    ProgressGenerator progressGenerator;
    ActionProcessButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        mEmailText = (EditText) findViewById(R.id.input_email);
        mPasswordText = (EditText) findViewById(R.id.input_password);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        mSignUpTextView = (TextView) findViewById(R.id.link_signup);
        logo_image = (ImageView) findViewById(R.id.logo);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        progressGenerator = new ProgressGenerator(this);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        final ConnectionDetector connectionDetector = new ConnectionDetector(LoginActivity.this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) login();
                else
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...").setContentText("You are not connected to " +
                            "internet!").show();
            }
        });

        assert mSignUpTextView != null;
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AdminSignUpActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        progressGenerator.start(email, password, LoginActivity.this, btnSignIn);
        btnSignIn.setEnabled(false);
        mEmailText.setEnabled(false);
        mPasswordText.setEnabled(false);
        logo_image.setEnabled(false);
        mSignUpTextView.setEnabled(false);

    }

    private boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (email.isEmpty()) {
            mEmailText.setError("enter a valid user name");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            mPasswordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }
        return valid;

    }

    private void onLoginFailed() {
        Snackbar.make(btnSignIn, "Login Failed", Snackbar.LENGTH_LONG).show();
        btnSignIn.setEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onComplete(Boolean success) {
        if (!success) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        String token = pref.getString(GCM_TOKEN, null);

        if (token == null) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        PrefManager manager = new PrefManager(this);

        if (preferences.getBoolean("isUser", true)) {
            manager.setUserLoggedIn(true);
            manager.setAsAdmin(false);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            manager.setUserLoggedIn(true);
            manager.setAsAdmin(true);
            Intent intent = new Intent(this, AdminMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
}
