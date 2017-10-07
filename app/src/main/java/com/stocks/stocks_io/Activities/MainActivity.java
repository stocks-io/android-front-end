package com.stocks.stocks_io.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.squareup.moshi.Moshi;
import com.stocks.stocks_io.Data.Endpoints;
import com.stocks.stocks_io.Model.UsersModel;
import com.stocks.stocks_io.POJO.BaseMessage;
import com.stocks.stocks_io.POJO.LoginResponse;
import com.stocks.stocks_io.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private Moshi moshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moshi = new Moshi.Builder().build();

        loginUser();
    }

    public void registerUser() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.BASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

        UsersModel model = retrofit.create(UsersModel.class);
        Call<BaseMessage> registerUser = model.registerUser("pizza dog", "1234", "darrien", "glasser", "pizza@outlook.com");
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

    public void loginUser() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Endpoints.BASEURL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build();

        UsersModel model = retrofit.create(UsersModel.class);

        Call<LoginResponse> loginUser = model.loginUser("pizza dog", "1234");
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
