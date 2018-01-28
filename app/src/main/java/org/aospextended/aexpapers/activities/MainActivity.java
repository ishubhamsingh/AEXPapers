package org.aospextended.aexpapers.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.dm.wallpaper.board.activities.WallpaperBoardActivity;
import com.dm.wallpaper.board.activities.configurations.ActivityConfiguration;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.aospextended.aexpapers.Constants;
import org.aospextended.aexpapers.RequestInterface;
import org.aospextended.aexpapers.licenses.License;
import org.aospextended.aexpapers.models.ServerRequest;
import org.aospextended.aexpapers.models.ServerResponse;
import org.aospextended.aexpapers.models.StatsData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends WallpaperBoardActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private SharedPreferences pref;
    private CompositeDisposable mCompositeDisposable;
    @NonNull
    @Override
    public ActivityConfiguration onInit() {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        pref=getSharedPreferences("aexStatsPrefs", Context.MODE_PRIVATE);
        mCompositeDisposable = new CompositeDisposable();
        if (!(pref.getString(Constants.LAST_BUILD_DATE, "null").equals(Constants.KEY_BUILD_DATE) && !pref.getBoolean(Constants.IS_FIRST_LAUNCH, true))) {
            pushStats();
        }
        return new ActivityConfiguration()
                .setLicenseCheckerEnabled(License.isLicenseCheckerEnabled())
                .setLicenseKey(License.getLicenseKey())
                .setRandomString(License.getRandomString())
                .setDonationProductsId(License.getDonationProductsId());
    }

    private void pushStats() {
        //Anonymous Stats

        if (!TextUtils.isEmpty(SystemProperties.get(Constants.KEY_DEVICE))) { //Push only if installed ROM is AEX
            RequestInterface requestInterface = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RequestInterface.class);

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
            mCompositeDisposable.add(requestInterface.operation(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            Log.d(Constants.TAG, "This ain't AEX!");
        }

    }

    private void handleResponse(ServerResponse resp) {

        if (resp.getResult().equals(Constants.SUCCESS)) {

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.IS_FIRST_LAUNCH, false);
            editor.putString(Constants.LAST_BUILD_DATE, Constants.KEY_BUILD_DATE);
            editor.apply();
            Log.d(Constants.TAG, "push successful");

        } else {
            Log.d(Constants.TAG, resp.getMessage());
        }

    }

    private void handleError(Throwable error) {

        Log.d(Constants.TAG, error.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

}
