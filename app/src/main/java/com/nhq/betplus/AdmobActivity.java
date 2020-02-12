package com.nhq.betplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdmobActivity extends BaseActivity implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardedVideoAd;
    boolean isRewarded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admob);
        Log.d("Statee", "ad onCreate");

        MobileAds.initialize(this, "ca-app-pub-8968999347793078~9650487482");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        //my own key
/*        mRewardedVideoAd.loadAd("ca-app-pub-8968999347793078/4398160809",
                new AdRequest.Builder().build());*/

        //default key (android)
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
        // Reward the user
        isRewarded = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Intent intent = new Intent();
        if (isRewarded) {
            setResult(RESULT_OK, intent);
        }
        else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        TextView textView = findViewById(R.id.inform);
        textView.setText("Không tải được quảng cáo, vui lòng kiểm tra kết nối Internet");
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }

    @Override
    public void onResume() {
        Log.d("Statee", "ad onResume");
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("Statee", "ad onPause");
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("Statee", "ad onDestroy");
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
