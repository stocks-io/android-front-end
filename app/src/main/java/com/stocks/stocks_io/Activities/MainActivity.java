package com.stocks.stocks_io.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stocks.stocks_io.Data.Endpoints;
import com.stocks.stocks_io.Model.UsersModel;
import com.stocks.stocks_io.POJO.BaseMessage;
import com.stocks.stocks_io.POJO.BaseUserInfo;
import com.stocks.stocks_io.POJO.FullUserInfo;
import com.stocks.stocks_io.POJO.LoginResponse;
import com.stocks.stocks_io.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new GsonBuilder().create();
        loginUser();
    }

    public void registerUser() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        UsersModel model = retrofit.create(UsersModel.class);
        FullUserInfo fu = new FullUserInfo("dgdg", "1234", "darrien", "glasser", "pizza@outlook.com");
        Call<BaseMessage> registerUser = model.registerUser(fu);
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
                    Log.wtf(TAG, "Response code: " + errorBody);
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

    public void loginUser() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        UsersModel model = retrofit.create(UsersModel.class);

        BaseUserInfo bubu = new BaseUserInfo("dgdg", "1234");
        Call<LoginResponse> loginUser = model.loginUser(bubu);
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

                Log.wtf(TAG, "Response body: " + response.body().getToken());
                Log.wtf(TAG, "Response body: " + response.body().getUserId());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.wtf(TAG, t.getMessage());
            }
        });
    }
}
