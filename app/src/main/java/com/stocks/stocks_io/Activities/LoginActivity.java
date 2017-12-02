package com.stocks.stocks_io.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stocks.stocks_io.Data.Endpoints;
import com.stocks.stocks_io.Model.UsersModel;
import com.stocks.stocks_io.POJO.BaseMessage;
import com.stocks.stocks_io.POJO.LoginResponse;
import com.stocks.stocks_io.POJO.Results;
import com.stocks.stocks_io.R;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.stocks.stocks_io.Utils.NoMoreJavaKt.getAndSaveSymbols;

public class LoginActivity extends AppCompatActivity {
    private String TAG = LoginActivity.class.getSimpleName();

    private static final boolean DEBUG = true;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TextView authView;
    public TextInputEditText emailInput;
    public TextInputEditText passwordInput;
    public FloatingActionButton continueButton;
    public Switch authSwitch;
    public TextView switchText;
    public TextInputEditText firstNameInput;
    public TextInputEditText lastNameInput;
    public LinearLayout registerDetails;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        // The noose looks a little more enticing every day
        authView = findViewById(R.id.auth_title_view);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        continueButton = findViewById(R.id.continue_button);
        authSwitch = findViewById(R.id.auth_switch);
        switchText = findViewById(R.id.switch_text);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        registerDetails = findViewById(R.id.register_details);

        sharedPreferences = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (realm.where(Results.class).findFirst() == null) getAndSaveSymbols();

        if (sharedPreferences.getBoolean(getString(R.string.user_logged_in), false)) {
            Intent intent = new Intent(this, StocksActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        switchText.setOnClickListener(v -> authSwitch.setChecked(!authSwitch.isChecked()));
        authSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                registerDetails.setVisibility(View.GONE);
                authView.setText(getString(R.string.login));
                switchText.setText(getString(R.string.register));
            } else {
                registerDetails.setVisibility(View.VISIBLE);
                authView.setText(getString(R.string.register));
                switchText.setText(getString(R.string.login));
            }
        });

        continueButton.setOnClickListener(v -> {
            String email, password;
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();

            if (email.equals("")) {
                Toast.makeText(this, "Please include an email", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.equals("")) {
                Toast.makeText(this, "Please include a password", Toast.LENGTH_LONG).show();
                return;
            }

            if (!authSwitch.isChecked()) {
                loginUser(email, password);
            } else {
                String firstName, lastName;
                firstName = firstNameInput.getText().toString();
                lastName = lastNameInput.getText().toString();

                if (firstName.equals("")) {
                    Toast.makeText(this, "Please include a first name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.equals("")) {
                    Toast.makeText(this, "Please include a last name", Toast.LENGTH_LONG).show();
                    return;
                }
                registerUser(firstName, lastName, email, password);
            }
        });

    }

    public void registerUser(String firstName, String lastName, String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.DEVBASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

        UsersModel model = retrofit.create(UsersModel.class);
        Call<BaseMessage> registerUser = model.registerUser(firstName, lastName, email, password);
        registerUser.enqueue(new Callback<BaseMessage>() {
            @Override
            public void onResponse(@NonNull Call<BaseMessage> call, @NonNull Response<BaseMessage> response) {
                Log.wtf(TAG, "Response code: " + response.code());
                if (!response.isSuccessful()) {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {

                    }
                    Log.wtf(TAG, "Response error body: " + errorBody);
                    return;
                }
                Log.wtf(TAG, "Response body: " + response.body().getMessage());
                loginUser(email, password);
            }

            @Override
            public void onFailure(@NonNull Call<BaseMessage> call, @NonNull Throwable t) {
                Log.wtf(TAG, t.getMessage());
            }
        });
    }

    public void loginUser(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.DEVBASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

        UsersModel model = retrofit.create(UsersModel.class);

        Call<LoginResponse> loginUser = model.loginUser(email, password);
        loginUser.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    Log.wtf(TAG, "Response code: " + response.code());
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {

                    }
                    Log.wtf(TAG, "Response code: " + errorBody);
                    return;
                }

                Log.wtf(TAG, "Response token: " + response.body().getToken());
                Log.wtf(TAG, "Response id: " + response.body().getUserId());

                editor.putBoolean(getString(R.string.user_logged_in), true).apply();
                editor.putString(getString(R.string.user_email), email).apply();
                editor.putString(getString(R.string.token), response.body().getToken()).apply();
                Intent intent = new Intent(LoginActivity.this, StocksActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.wtf(TAG, t.getMessage());
            }
        });
    }
}
