package com.newEra.strangers.chat.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.adapter.ConversationAdapter;
import com.newEra.strangers.chat.adapter.ViewPagerAdapter;
import com.newEra.strangers.chat.fragment.BlockedFragment;
import com.newEra.strangers.chat.fragment.FriendsFragment;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;

public class ConversationActivity extends AppCompatActivity {
    private View adViewBottom;
    private BlockedFragment blockedFragment;
    private FriendsFragment friendsFragment;
    private ImageView ivCross;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private AdView adView;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;
    private Boolean ivCreossAds = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.ivCross = (ImageView) findViewById(R.id.cross);
        this.adViewBottom = findViewById(R.id.adViewBottom);
        adView = findViewById(R.id.adView3);
        adRequest = com.newEra.strangers.chat.util.AdRequest.getRequest();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/7634366379");
//        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(adRequest);
            }
        });
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (ivCreossAds) {
                    finish();
                    ivCreossAds = false;
                }
                adView.loadAd(adRequest);

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

        });
        this.ivCross.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ConversationActivity.this.finish();
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    ivCreossAds = true;
                } else finish();

            }
        });
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager(this.viewPager);
        this.tabLayout.setupWithViewPager(this.viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        refreshConversationList();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.friendsFragment = new FriendsFragment();
        this.blockedFragment = new BlockedFragment();
        adapter.addFragment(this.friendsFragment, "Friends");
        adapter.addFragment(this.blockedFragment, "Blocked");
        viewPager.setAdapter(adapter);
    }

    private void refreshConversationList() {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            public void run() {
                while (!ConversationActivity.this.isFinishing()) {
                    try {
                        if (System.currentTimeMillis() % 1500 == 0) {
                            ConversationActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    ConversationAdapter caf = ConversationActivity.this.friendsFragment.getConversationAdapter();
                                    ConversationAdapter cab = ConversationActivity.this.blockedFragment.getConversationAdapter();
                                    if (caf != null) {
                                        caf.notifyDataSetChanged();
                                    }
                                    if (cab != null) {
                                        cab.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }
}
