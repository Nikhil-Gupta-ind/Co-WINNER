package com.NikhilGupta.co_winner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NikhilGupta.co_winner.centerlocator.CentersActivity;
import com.NikhilGupta.co_winner.centerlocator.CentersByLocationActivity;
import com.NikhilGupta.co_winner.databinding.ActivityMainBinding;
import com.NikhilGupta.co_winner.login.LoginActivity;
import com.NikhilGupta.co_winner.receivers.NetworkBroadcastReceiver;
import com.NikhilGupta.co_winner.retrofit.RequestInterface;
import com.NikhilGupta.co_winner.retrofit.RetrofitHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// TODO (1) BaseActivity to have receivers and view common in all activities
// TODO (2) Application class to have permissions and services common to all
public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 88;
    final static String TAG = "Test";
    private NetworkBroadcastReceiver networkBroadcastReceiver;
    RequestInterface requestInterface;

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    RelativeLayout bottomSheetRL;
    ActivityMainBinding binding;
    Animation animation, animation2, animation3, animation4;

    SharedPreferences sharedPreferences;
    static String mToken;
    Toast toast;
    int flag = 0; // flag for ref id field drawer
    File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startAnimation();

        bottomSheetRL = findViewById(R.id.idRLSheet);
        sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);

        getBearerToken();

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        binding.centerLocator.setOnClickListener(v -> startActivity(new Intent(this, CentersActivity.class)));

        binding.certificate.setOnClickListener(v -> {
            getBearerToken();
            // First check if the user token != null
            if (mToken == null) {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else {
                /*if (toast!=null) toast.cancel();
                toast = Toast.makeText(MainActivity.this, "Coming Up!", Toast.LENGTH_SHORT);
                toast.show();*/

                if (flag == 0) {
                    flag = 1;
                    binding.cardView3.setVisibility(View.VISIBLE);
                    animation4 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.atg_four);
                    binding.cardView3.setAnimation(animation4);
                } else {
                    flag = 0;
                    animation3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.atg_three);
                    binding.cardView3.setAnimation(animation3);
                    new Handler().postDelayed(() -> binding.cardView3.setVisibility(View.INVISIBLE), 800);
                }
            }
        });

        binding.download.setOnClickListener(v -> {
            getBearerToken();
            if (mToken != null) {

                if (binding.referenceId.getText().toString().trim().length() < 14) {
                    Toast.makeText(this, "Invalid ID", Toast.LENGTH_SHORT).show();
                } else {
                    setupPermissions();
                }
            } else {
                flag = 0;
                animation3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.atg_three);
                binding.cardView3.setAnimation(animation3);
                new Handler().postDelayed(() -> binding.cardView3.setVisibility(View.INVISIBLE), 800);
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }

        });

        binding.more.setOnClickListener(v -> {
            startActivity(new Intent(this, CentersByLocationActivity.class));
        });

        binding.more2.setOnClickListener(v -> {
            startActivity(new Intent(this, WebviewActivity.class));
//            if (toast != null) toast.cancel();
//            toast = Toast.makeText(this, "Coming Soon! in new updates", Toast.LENGTH_SHORT);
//            toast.show();
        });

        binding.button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // PRESSED
                    binding.button.setBackgroundResource(R.drawable.bg_btn_bordered);
                    binding.button.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // RELEASED
                    binding.button.setBackgroundResource(R.drawable.bg_btn);
                    binding.button.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    break;
            }
            return false;
        });

        binding.button.setOnClickListener(v -> {
            String textMessage = "https://github.com/Nikhil-Gupta-ind/jCloud";
            // Create the text message with a string.
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
            shareIntent.setType("text/plain");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Try to invoke the intent.
            try {
                startActivity(Intent.createChooser(shareIntent, "Share Co-WINNER App"));
            } catch (ActivityNotFoundException e) {
                // Define what your app should do if no activity can handle the intent.
            }
        });
    }

    /**
     * An important step here to prefix 'Bearer ' to mToken
     */
    private void getBearerToken() {
        String oldToken = sharedPreferences.getString("mToken", null);
        if (oldToken != null) {
            long lastLogin = sharedPreferences.getLong("lastLogin", 0);
            if (System.currentTimeMillis() - lastLogin > 300000) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mToken",null);
                editor.putLong("lastLogin", 0);
                editor.apply();
            } else {
                mToken = "Bearer " + oldToken;
                // change menu item name on the basis of token
                Log.d(TAG, "Added bearer to sharedPrefs: \n" + mToken);
            }
        } else {
            mToken = null;
        }
    }

    private void setupPermissions() {
        // If we don't have the record audio permission...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                String[] permissionsWeNeed = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissionsWeNeed, MY_PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            // Otherwise, permissions were granted and we are ready to go!
            startDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    startDownload();
                } else {
                    Toast.makeText(this, "Permission for audio not granted. Visualizer can't run.", Toast.LENGTH_LONG).show();
                    finish();
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    private void startDownload() {
        String mReferenceId = binding.referenceId.getText().toString();
        requestInterface = RetrofitHelper.getInstance().create(RequestInterface.class);
        Call<ResponseBody> pdfCall = requestInterface.downloadPdf(mToken, mReferenceId);
        pdfCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                switch (response.code()) {
                    case 200:
                        Toast.makeText(MainActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        InputStream inputStream = response.body().byteStream();
                        Log.d("Test", "onResponse1: " + response + "\n" + inputStream.toString());
//                      inputStream.toString()=>  buffer((buffer(ResponseBodySource(okhttp3.internal.http2.Http2Stream$FramingSource@7905aaf)))).inputStream()

                        // Create the pdf file
                        String filename = "Vaccine certificate CoWINNER " + System.currentTimeMillis()/1000 + ".pdf";
                        pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

                        FileOutputStream fileOutputStream;
                        try {
                            pdfFile.createNewFile();
                            fileOutputStream = new FileOutputStream(pdfFile);

//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            int read;
                            byte[] data = new byte[32768];

                            while ((read = inputStream.read(data, 0, data.length)) != -1) {
//                                byteArrayOutputStream.write(data, 0, read);
                                fileOutputStream.write(data, 0, read);
                            }
//                            return byteArrayOutputStream.toByteArray(); // if in a fun
                            fileOutputStream.close();
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: Download successful!");

                        /*// DownloadManager
//                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
                        DownloadManager.Request request = new DownloadManager.Request();
                        request.setTitle(filename);
                        request.setMimeType("application/pdf");
                        request.allowScanningByMediaScanner();
                        request.setAllowedOverMetered(true);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);*/

                        // Show an option to open pdf
                        Uri uri = FileProvider.getUriForFile(MainActivity.this,"com.NikhilGupta.co_winner"+".provider",pdfFile);
                        Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenIntent.setDataAndType(uri,"application/pdf");
                        pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    startActivity(intent);
                        Snackbar.make(binding.mainLayout, "Download Successful!", Snackbar.LENGTH_LONG)
                                .setAction("OPEN", view -> {
                                    // open pdf
                                    try {
                                        startActivity(Intent.createChooser(pdfOpenIntent, "Choose an application to open with:"));
                                    } catch (ActivityNotFoundException e) {
                                        // Define what your app should do if no activity can handle the intent.
                                        Toast.makeText(MainActivity.this, "No app found to open pdf", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_blue_bright ))
                                .show();

                        // Notification
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, pdfOpenIntent, PendingIntent.FLAG_IMMUTABLE);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "CHANNEL_ID")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(filename)
                                .setContentText("Download complete. Tap to open")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, builder.build());

                        break;

                    case 400:
                        Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        break;

                    case 401:
                        Toast.makeText(MainActivity.this, "Unauthorised access", Toast.LENGTH_SHORT).show();
                        break;

                    case 500:
                        Toast.makeText(MainActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
                Log.d(TAG, "onResponse2: " + response + "\nresponse.body = \n" + response.body());
//                Log.d(TAG, "onResponse3: " + response.headers());
                try {
                    response.body().close();
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: " + e.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_log);
        if (sharedPreferences.getString("mToken", null) != null) {
            menuItem.setTitle(R.string.logout);
        } else {
            menuItem.setTitle(R.string.login);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_log && item.getTitle().equals("Logout")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mToken", null);
            editor.apply();
            item.setTitle(R.string.login);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            // hide the ref id
            flag = 0;
            binding.referenceId.setText("");
            binding.cardView3.setVisibility(View.INVISIBLE);
            return true;
        } else if (item.getItemId() == R.id.action_log && item.getTitle().equals("Login")) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (item.getItemId() == R.id.about){
            displayBottomSheet(); return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void startAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.atg);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.atg_two);
//        animation3 = AnimationUtils.loadAnimation(this, R.anim.atg_three);
        //Pass Animation
        binding.cardView2.setAnimation(animation);
        binding.title.setAnimation(animation);
        binding.subTitle.setAnimation(animation);
        binding.button.setAnimation(animation2);
    }

    private void displayBottomSheet(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView link = layout.findViewById(R.id.github_link);
//        link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://github.com/Nikhil-Gupta-ind?tab=repositories";
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                /*if (intent.resolveActivity(getPackageManager()) != null){
//                    startActivity(intent);
//                }*/
//                startActivity(intent);
//            }
//        });
    }

    private void startDownload2() {
        String mReferenceId = binding.referenceId.getText().toString();
        URL searchUrl = buildUrl(mReferenceId);
        Log.d(TAG, "startDownload: " + searchUrl.toString());
        new downloadTask().execute(searchUrl);
    }

    public static URL buildUrl(String referenceId) {
        Uri builtUri = Uri.parse("https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download").buildUpon()
                .appendQueryParameter("beneficiary_reference_id", referenceId)
//                .appendQueryParameter("Authorization", mToken)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException urlException) {
            urlException.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static class downloadTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            try {
                result = getResponseFromHttpUrl(url);
                Log.d(TAG, "doInBackground: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                Log.d(TAG, "onPostExecute: " + s);
            } else {
                Log.d(TAG, "onPostExecute: " + s);
            }
        }
    }
}