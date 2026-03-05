package com.example.googleadsdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

/**
 * Google Ads 单页 Demo
 * 
 * 展示三种广告类型：
 * 1. Banner 广告 - 横幅广告，固定在页面底部
 * 2. Interstitial 广告 - 插屏广告，全屏展示
 * 3. Rewarded 广告 - 激励视频广告，看完获得奖励
 * 
 * 注意：使用的是 Google 官方测试广告 ID
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoogleAdsDemo";
    
    // ============ 测试广告 ID（正式上线需替换为真实 ID）============
    // Banner 广告测试 ID
    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
    // 插屏广告测试 ID
    private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    // 激励视频广告测试 ID
    private static final String REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

    // UI 组件
    private AdView bannerAdView;
    private Button btnShowInterstitial;
    private Button btnShowRewarded;
    private TextView tvStatus;
    private TextView tvRewardCount;

    // 广告对象
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    // 奖励计数
    private int rewardCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI
        initViews();

        // 初始化 Mobile Ads SDK
        initMobileAds();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        bannerAdView = findViewById(R.id.adView_banner);
        btnShowInterstitial = findViewById(R.id.btn_show_interstitial);
        btnShowRewarded = findViewById(R.id.btn_show_rewarded);
        tvStatus = findViewById(R.id.tv_status);
        tvRewardCount = findViewById(R.id.tv_reward_count);

        // 插屏广告按钮
        btnShowInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialAd();
            }
        });

        // 激励视频按钮
        btnShowRewarded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardedAd();
            }
        });

        // 初始状态
        btnShowInterstitial.setEnabled(false);
        btnShowRewarded.setEnabled(false);
        updateRewardCount();
    }

    /**
     * 初始化 Google Mobile Ads SDK
     */
    private void initMobileAds() {
        updateStatus("正在初始化广告 SDK...");
        
        MobileAds.initialize(this, initializationStatus -> {
            Log.d(TAG, "Mobile Ads SDK 初始化完成");
            updateStatus("SDK 初始化完成，正在加载广告...");
            
            // SDK 初始化后加载各类广告
            loadBannerAd();
            loadInterstitialAd();
            loadRewardedAd();
        });
    }

    // ==================== Banner 广告 ====================

    /**
     * 加载 Banner 横幅广告
     */
    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
        Log.d(TAG, "Banner 广告开始加载");
    }

    // ==================== Interstitial 插屏广告 ====================

    /**
     * 加载插屏广告
     */
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, INTERSTITIAL_AD_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        btnShowInterstitial.setEnabled(true);
                        updateStatus("插屏广告已就绪");
                        Log.d(TAG, "插屏广告加载成功");
                        
                        // 设置全屏内容回调
                        setupInterstitialCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        interstitialAd = null;
                        btnShowInterstitial.setEnabled(false);
                        Log.e(TAG, "插屏广告加载失败: " + loadAdError.getMessage());
                        updateStatus("插屏广告加载失败: " + loadAdError.getMessage());
                    }
                });
    }

    /**
     * 设置插屏广告回调
     */
    private void setupInterstitialCallbacks() {
        if (interstitialAd == null) return;
        
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "插屏广告已关闭");
                interstitialAd = null;
                btnShowInterstitial.setEnabled(false);
                updateStatus("插屏广告已关闭，正在加载新广告...");
                // 广告关闭后重新加载
                loadInterstitialAd();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "插屏广告展示失败: " + adError.getMessage());
                interstitialAd = null;
                updateStatus("插屏广告展示失败");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "插屏广告正在展示");
                updateStatus("插屏广告正在展示");
            }
        });
    }

    /**
     * 展示插屏广告
     */
    private void showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            Toast.makeText(this, "插屏广告未就绪，请稍候", Toast.LENGTH_SHORT).show();
            loadInterstitialAd();
        }
    }

    // ==================== Rewarded 激励视频广告 ====================

    /**
     * 加载激励视频广告
     */
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, REWARDED_AD_UNIT_ID, adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        btnShowRewarded.setEnabled(true);
                        updateStatus("激励视频已就绪");
                        Log.d(TAG, "激励视频加载成功");
                        
                        // 设置全屏内容回调
                        setupRewardedCallbacks();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                        btnShowRewarded.setEnabled(false);
                        Log.e(TAG, "激励视频加载失败: " + loadAdError.getMessage());
                        updateStatus("激励视频加载失败: " + loadAdError.getMessage());
                    }
                });
    }

    /**
     * 设置激励视频回调
     */
    private void setupRewardedCallbacks() {
        if (rewardedAd == null) return;
        
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "激励视频已关闭");
                rewardedAd = null;
                btnShowRewarded.setEnabled(false);
                updateStatus("激励视频已关闭，正在加载新广告...");
                // 广告关闭后重新加载
                loadRewardedAd();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "激励视频展示失败: " + adError.getMessage());
                rewardedAd = null;
                updateStatus("激励视频展示失败");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "激励视频正在展示");
                updateStatus("激励视频正在展示");
            }
        });
    }

    /**
     * 展示激励视频广告
     */
    private void showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // 用户观看完视频，获得奖励
                    int amount = rewardItem.getAmount();
                    String type = rewardItem.getType();
                    
                    Log.d(TAG, "用户获得奖励: " + amount + " " + type);
                    
                    // 更新奖励计数
                    rewardCount += amount;
                    updateRewardCount();
                    
                    Toast.makeText(MainActivity.this, 
                            "🎉 恭喜获得 " + amount + " 个奖励！", 
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "激励视频未就绪，请稍候", Toast.LENGTH_SHORT).show();
            loadRewardedAd();
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 更新状态文本
     */
    private void updateStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvStatus.setText(status);
            }
        });
    }

    /**
     * 更新奖励计数显示
     */
    private void updateRewardCount() {
        tvRewardCount.setText("已获得奖励: " + rewardCount);
    }

    // ==================== 生命周期 ====================

    @Override
    protected void onPause() {
        if (bannerAdView != null) {
            bannerAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bannerAdView != null) {
            bannerAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (bannerAdView != null) {
            bannerAdView.destroy();
        }
        super.onDestroy();
    }
}
