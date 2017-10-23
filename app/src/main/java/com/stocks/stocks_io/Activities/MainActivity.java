package com.stocks.stocks_io.Activities;

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

import com.squareup.moshi.Moshi;
import com.stocks.stocks_io.Data.Endpoints;
import com.stocks.stocks_io.Model.UsersModel;
import com.stocks.stocks_io.POJO.BaseMessage;
import com.stocks.stocks_io.POJO.LoginResponse;
import com.stocks.stocks_io.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private Moshi moshi;

    @BindView(R.id.auth_title_view)
    public TextView authView;

    @BindView(R.id.email_input)
    public TextInputEditText emailInput;

    @BindView(R.id.password_input)
    public TextInputEditText passwordInput;

    @BindView(R.id.continue_button)
    public FloatingActionButton continueButton;

    @BindView(R.id.auth_switch)
    public Switch authSwitch;

    @BindView(R.id.switch_text)
    public TextView switchText;

    @BindView(R.id.first_name_input)
    public TextInputEditText firstNameInput;

    @BindView(R.id.last_name_input)
    public TextInputEditText lastnameInput;

    @BindView(R.id.register_details)
    public LinearLayout registerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

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
            loginUser(email, password);
        });
    }

    public void registerUser() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.BASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

        UsersModel model = retrofit.create(UsersModel.class);
        Call<BaseMessage> registerUser = model.registerUser("Dave", "Machado", "d@glasser.com", "pizzaDog");
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
            }

            @Override
            public void onFailure(@NonNull Call<BaseMessage> call, @NonNull Throwable t) {
                Log.wtf(TAG, t.getMessage());
            }
        });
    }

    public void loginUser(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.BASEURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
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
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.wtf(TAG, t.getMessage());
            }
        });
    }
}
