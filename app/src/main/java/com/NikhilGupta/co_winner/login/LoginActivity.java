package com.NikhilGupta.co_winner.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.NikhilGupta.co_winner.MainActivity;
import com.NikhilGupta.co_winner.receivers.NetworkBroadcastReceiver;
import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.databinding.ActivityLoginBinding;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;
import com.NikhilGupta.co_winner.retrofit.RetrofitHelper;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static final String REGEX_PHONE_MOBILE = "[6789]{1}[0-9]{9}";
    private static final String TAG = "Test";
    public ActivityLoginBinding binding;
    private RequestInterface requestInterface;

    private String mTxnId;
    private String mOTP;
    private String mOTPEncoded;
    private String mToken;
    CountDownTimer mTimer;

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
                if (mTimer != null) {
                    mTimer.cancel();
                    binding.resendText.setVisibility(View.INVISIBLE);
                    binding.tvTimer.setVisibility(View.INVISIBLE);
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

        binding.resend.setOnClickListener(v -> {
            getOtpBT.setEnabled(true);
            getOtpBT.performClick();
        });
    }

    public void getOtp(View view) {
        binding.otpLabel.setVisibility(View.VISIBLE);
        binding.otp.setVisibility(View.VISIBLE);
        binding.textView.setVisibility(View.INVISIBLE);
        binding.resend.setVisibility(View.INVISIBLE);
        binding.getOtp.setEnabled(false);

        String mMobileNo = binding.mobile.getText().toString();
        Log.d(TAG, "mMobileNo: " + mMobileNo);
        if (!mMobileNo.matches(REGEX_PHONE_MOBILE)) {
            binding.mobile.setError("Please enter valid Mobile no.");
            binding.mobile.requestFocus();
        } else {
            requestOtp(mMobileNo);
        }

    }

    private void startTimer() { // options: run on new thread, make it asynchronous, use AsyncTask, use AsyncTaskLoader Or user Executor. Or Pass the timer to async
        Log.d(TAG, "startTimer: control is here");
        binding.textView.setVisibility(View.VISIBLE);
        binding.resend.setVisibility(View.INVISIBLE);
        binding.resendText.setVisibility(View.VISIBLE);
        binding.tvTimer.setVisibility(View.VISIBLE);

        mTimer = new CountDownTimer(180000, 1000)
        {
            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText(String.format(getString(R.string.resend_otp), " - " + (millisUntilFinished / 1000) + " sec"));
            }

            public void onFinish()
            {
                binding.resend.setVisibility(View.VISIBLE);
                binding.resendText.setVisibility(View.INVISIBLE);
                binding.tvTimer.setVisibility(View.INVISIBLE);
                if (binding.mobile.getText().toString().length() == 10)
                    binding.getOtp.setEnabled(true);
            }
        };
        mTimer.start();
    }

    private void requestOtp(String mMobileNo) {
        requestInterface = RetrofitHelper.getInstance().create(RequestInterface.class);

        Map<String, String> mobileMap = new HashMap<>();
        mobileMap.put("mobile", mMobileNo);

        Call<SaveTxnId> otpCall = requestInterface.getOtp(mobileMap);
        otpCall.enqueue(new Callback<SaveTxnId>() {
            @Override
            public void onResponse(@NonNull Call<SaveTxnId> call, @NonNull retrofit2.Response<SaveTxnId> response) {

                switch (response.code()) {
                    case 200:
//                        saveTxnId = new SaveTxnId(response.body().txnId);
                        mTxnId = response.body().txnId;
                        Log.d(TAG, "txnId: >\n" + mTxnId);
                        Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                        startTimer(); // probably network transaction and timer is updating on same Ui thread hence collapsing
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
            }

            @Override
            public void onFailure(@NonNull Call<SaveTxnId> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        binding.verify.setEnabled(false);

        mOTP = binding.otp.getText().toString();
        Log.d(TAG, "mOTP: " + mOTP);
        encodeOtp();
        Log.d(TAG, "mOTPEncoded: " + mOTPEncoded);

//        mTxnId = saveTxnId.getTxnId();
        Map<String, String> verifyMap = new HashMap<>();
        verifyMap.put("otp", mOTPEncoded);
        verifyMap.put("txnId", mTxnId);

        Call<UserToken> verifyCall = requestInterface.submitData(verifyMap);
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
                        Log.d(TAG, "onResponse: " + response);
                        Log.d(TAG, "saving token in sharedPrefs: "+mToken);
                        SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mToken",mToken);
                        editor.putLong("lastLogin", System.currentTimeMillis());
                        editor.apply();
                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle("Login Successful!");
                        alertDialog.setMessage("This app uses temporary login session valid for 5 minutes");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Got it",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finishLoginActivity();
                                    }
                                });
                        alertDialog.show();

//                        UserToken userToken = new UserToken(mToken);
                        // BTW later we'll try to call the calling activity by writing finish()
//                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
                    intent.putExtra("userToken",mToken);
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
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finishLoginActivity() {
        finish();
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