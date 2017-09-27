package org.aospextended.aexpapers.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import com.dm.wallpaper.board.activities.WallpaperBoardActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.aospextended.aexpapers.Constants;
import org.aospextended.aexpapers.RequestInterface;
import org.aospextended.aexpapers.licenses.License;
import org.aospextended.aexpapers.models.ServerRequest;
import org.aospextended.aexpapers.models.ServerResponse;
import org.aospextended.aexpapers.models.StatsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends WallpaperBoardActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initMainActivity(savedInstanceState,
                License.isLicenseCheckerEnabled(),
                License.getRandomString(),
                License.getLicenseKey(),
                License.getDonationProductsId());

        pref = getSharedPreferences("aexStatsPrefs", Context.MODE_PRIVATE);

        if (!(pref.getString(Constants.LAST_VERSION, "null").equals(Constants.KEY_VERSION) && !pref.getBoolean(Constants.IS_FIRST_LAUNCH, true))) {
            pushStats();
        }


    }

    private void pushStats() {
        //Anonymous Stats

        if (!TextUtils.isEmpty(SystemProperties.get(Constants.KEY_DEVICE))) { //Push only if installed ROM is AEX
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestInterface requestInterface = retrofit.create(RequestInterface.class);

            StatsData stats = new StatsData();
            stats.setDevice(stats.getDevice());
            stats.setModel(stats.getModel());
            stats.setVersion(stats.getVersion());
            stats.setBuildType(stats.getBuildType());
            stats.setCountryCode(stats.getCountryCode(getApplicationContext()));
            stats.setBuildDate(stats.getBuildDate());
            ServerRequest request = new ServerRequest();
            request.setOperation(Constants.PUSH_OPERATION);
            request.setStats(stats);
            Call<ServerResponse> response = requestInterface.operation(request);

            response.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                    ServerResponse resp = response.body();
                    if (resp.getResult().equals(Constants.SUCCESS)) {

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Constants.IS_FIRST_LAUNCH, false);
                        editor.putString(Constants.LAST_VERSION, Constants.KEY_VERSION);
                        editor.apply();
                        Log.d(Constants.TAG, "push successful");

                    } else {
                        Log.d(Constants.TAG, resp.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    StatsData stats = new StatsData();
                    Log.d(Constants.TAG, "push failed");

                }
            });
        } else {
            Log.d(Constants.TAG, "This ain't AEX!");
        }

    }
}
