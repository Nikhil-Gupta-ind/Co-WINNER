package com.NikhilGupta.co_winner.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.NikhilGupta.co_winner.MainActivity;
import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.CentersActivity;
import com.NikhilGupta.co_winner.login.LoginActivity;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    // Checks No Connection/Back Online
    FrameLayout networkStatus;
    TextView networkLabel;
    Context ActivityContext;

    public NetworkBroadcastReceiver(CentersActivity centersActivity) {
        networkStatus = centersActivity.findViewById(R.id.network_status);
        networkLabel = centersActivity.findViewById(R.id.network_label);
        ActivityContext = centersActivity;
    }

    public NetworkBroadcastReceiver(LoginActivity loginActivity) {
        networkStatus = loginActivity.findViewById(R.id.network_status);
        networkLabel = loginActivity.findViewById(R.id.network_label);
        ActivityContext = loginActivity;
    }

    public NetworkBroadcastReceiver(MainActivity mainActivity) {
        networkStatus = mainActivity.findViewById(R.id.network_status);
        networkLabel = mainActivity.findViewById(R.id.network_label);
        ActivityContext = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            networkStatus.setBackgroundColor(ActivityContext.getResources().getColor(R.color.teal_200));
            networkLabel.setText(ActivityContext.getResources().getString(R.string.back_online));
            networkStatus.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> networkStatus.setVisibility(View.GONE), 3000);
        } else {
            networkStatus.setBackgroundColor(ActivityContext.getResources().getColor(R.color.black));
            networkLabel.setText(ActivityContext.getResources().getString(R.string.no_connection));
            networkStatus.setVisibility(View.VISIBLE);
        }

//        if (context instanceof LoginActivity) getOtp button enabled false
    }
}