package com.NikhilGupta.co_winner.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.NikhilGupta.co_winner.MainActivity;
import com.NikhilGupta.co_winner.NetworkBroadcastReceiver;
import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.databinding.ActivityLoginBinding;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Test";
    public ActivityLoginBinding binding;
    private APIInterface apiInterface;

    private String mTxnId;
    private String mOTP;
    private String mOTPEncoded;
    private String mToken;
    private SaveTxnId saveTxnId;
    private UserToken userToken;

    NetworkBroadcastReceiver networkBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final EditText mobileET = binding.mobile;
        final EditText otpET = binding.otp;
        final Button getOtpBT = binding.getOtp;
        final Button verifyBT = binding.verify;
        final ProgressBar loadingPB = binding.loading;

        // Enable Get OTP button when there's text to send
        mobileET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    binding.mLabel.setVisibility(View.VISIBLE);
                    getOtpBT.setEnabled(charSequence.toString().trim().length() == 10);
                } else {
                    getOtpBT.setEnabled(false);
                    binding.mLabel.setVisibility(View.INVISIBLE);

                    binding.otpLabel.setVisibility(View.GONE);
                    otpET.setVisibility(View.GONE);
                    otpET.getText().clear();
                    binding.textView.setVisibility(View.GONE);
                    binding.resend.setVisibility(View.GONE);
                    verifyBT.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // Enable Verify button when there's text to send
        otpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verifyBT.setEnabled(charSequence.toString().trim().length() == 6);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void getOtp(View view) {
        binding.otpLabel.setVisibility(View.VISIBLE);
        binding.otp.setVisibility(View.VISIBLE);
        binding.textView.setVisibility(View.VISIBLE);
        binding.resend.setVisibility(View.VISIBLE);
        binding.getOtp.setEnabled(false);

        String mMobileNo = binding.mobile.getText().toString();
        Log.d(TAG, "mMobileNo: " + mMobileNo);
        requestOtp(mMobileNo);

        Toast.makeText(this, "OTP Sent", Toast.LENGTH_SHORT).show();
    }


    private void requestOtp(String mMobileNo) {
        apiInterface = MyRetro.getInstance().create(APIInterface.class);

        Map<String, String> mobileMap = new HashMap<>();
        mobileMap.put("mobile", mMobileNo);

        Call<SaveTxnId> otpCall = apiInterface.getOtp(mobileMap);
        otpCall.enqueue(new Callback<SaveTxnId>() {
            @Override
            public void onResponse(@NonNull Call<SaveTxnId> call, @NonNull retrofit2.Response<SaveTxnId> response) {

                switch (response.code()) {
                    case 200:
//                        saveTxnId = new SaveTxnId(response.body().txnId);
                        mTxnId = response.body().txnId;
                        Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                        break;

                    case 400:
                        Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        break;

                    case 401:
                        Toast.makeText(LoginActivity.this, "Unauthorised access", Toast.LENGTH_SHORT).show();
                        break;

                    case 500:
                        Toast.makeText(LoginActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;

                }
                /*if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body());
//                    saveTxnId = new SaveTxnId(response.body().txnId);
                    mTxnId = response.body().txnId;
                    Log.d(TAG, "txnId: >\n" + mTxnId);
//                    7ea75a3e-caf4-4a8c-971c-b56f83ec339b
                    Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onResponse: Something is wrong" + response);
                }*/
            }

            @Override
            public void onFailure(@NonNull Call<SaveTxnId> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private void encodeOtp() {
        if (mOTP != null) {
            try {
                mOTPEncoded = Hash256.toHexString(Hash256.getSHA(mOTP));
            } catch (NoSuchAlgorithmException e) {
                Log.d(TAG, "encodeOtp: Exception thrown for incorrect algorithm: " + e);
            }
        }
    }

    public void verify(View view) {
        binding.loading.setVisibility(View.VISIBLE);

        mOTP = binding.otp.getText().toString();
        Log.d(TAG, "mOTP: " + mOTP);
        encodeOtp();
        Log.d(TAG, "mOTPEncoded: " + mOTPEncoded);

//        mTxnId = saveTxnId.getTxnId();
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("otp", mOTPEncoded);
        verifyMap.put("txnId", mTxnId);

        Call<UserToken> verifyCall = apiInterface.submitData(verifyMap);
        verifyCall.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(@NonNull Call<UserToken> call, @NonNull retrofit2.Response<UserToken> response) {
                switch (response.code()) {
                    case 200:
                        binding.networkStatus.setBackgroundColor(getResources().getColor(R.color.green_800));
                        binding.networkLabel.setText(getResources().getString(R.string.code_200));
                        binding.networkStatus.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);

                        mToken = response.body().token;
                        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mToken",mToken);
                        editor.apply();

                        String readToken = sharedPreferences.getString("mToken", null);
                        if (readToken != null){
                            Toast.makeText(LoginActivity.this, ""+readToken, Toast.LENGTH_SHORT).show();
                        }

//                        UserToken userToken = new UserToken(mToken);
                        // BTW later we'll try to call the calling activity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        break;

                    case 400:
                        binding.networkStatus.setBackgroundColor(getResources().getColor(R.color.orange_800));
                        binding.networkLabel.setText(getResources().getString(R.string.code_400));
                        binding.networkStatus.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);
                        break;

                    case 401:
                        binding.networkStatus.setBackgroundColor(getResources().getColor(R.color.red_800));
                        binding.networkLabel.setText(getResources().getString(R.string.code_401));
                        binding.networkStatus.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);
                        break;

                    case 500:
                        binding.networkStatus.setBackgroundColor(getResources().getColor(R.color.black));
                        binding.networkLabel.setText(getResources().getString(R.string.code_500));
                        binding.networkStatus.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);
                        break;

                    default:
                        break;

                }
                /*if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response);
//                    Response{protocol=h2, code=200, message=, url=https://cdn-api.co-vin.in/api/v2/auth/public/confirmOTP}
                    Log.d(TAG, "onResponse1: " + response.body());
//                    com.NikhilGupta.co_winner.login.UserToken@7258cf0
                    Toast.makeText(LoginActivity.this, "Successful!", Toast.LENGTH_SHORT).show();

                    mToken = response.body().token;
                    UserToken userToken = new UserToken(mToken);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("usertoken",mToken);
//                    startActivity(intent);
                } else {
                    Log.d(TAG, "onResponse2: Something is wrong\n" + response);
                    Log.d(TAG, "onResponse3: Invalid OTP code=" + response.code());
//                    Response{protocol=h2, code=400, message=, url=https://cdn-api.co-vin.in/api/v2/auth/public/confirmOTP}
                    Log.d(TAG, "onResponse4: "+response.body());
//                    onResponse3: null
                }
                Log.d(TAG, "onResponse5: "+response.code());
                try {
                    Log.d(TAG, "Verify onResponse0: " + response.body().token);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: "+e);
//                    java.lang.NullPointerException: Attempt to read from field 'java.lang.String com.NikhilGupta.co_winner.login.UserToken.token' on a null object reference
                }*/

            }

            @Override
            public void onFailure(@NonNull Call<UserToken> call, @NonNull Throwable t) {

            }
        });
    }


    private void requestOtp2() {
        String url = "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        textView.setText("Response: " + response.toString());
                        Log.d(TAG, "onResponse: " + response.toString());
                    }
                },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d(TAG, "onErrorResponse: Error");
                            }
                        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Log.d(TAG, "getParams: params passed");
                Map<String, String> params = new HashMap<>();
                params.put("mobile", "9876543210");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                Log.d(TAG, "getHeaders: Headers given");
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Access the RequestQueue through your singleton class.
//        SaveTxnId.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void requestOtp1() {
        String requestUrl = "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP";
        /*String mPOST = "/api/v2/auth/public/generateOTP";
        String mHost = "cdn-api.co-vin.in";
        String maccept = "application/json";
        String mContent_Type = "application/json";
        String mContent_length = "23"; // int
        try {
            JSONObject jsonBody = new JSONObject("{\"mobile\":\"9876543210\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        StringRequest postRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Map<String, String> params = new HashMap<>();
                params.put("mobile", "9876543210");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
//        SaveTxnId.getInstance().addToRequestQueue(postRequest);
//        [ ] https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP 0xd03517e5 NORMAL null
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkBroadcastReceiver = new NetworkBroadcastReceiver(this);
        registerReceiver(networkBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkBroadcastReceiver);
    }

}