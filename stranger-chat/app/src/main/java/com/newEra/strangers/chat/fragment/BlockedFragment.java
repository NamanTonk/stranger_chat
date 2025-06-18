package com.newEra.strangers.chat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.adapter.ConversationAdapter;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;

import java.util.Objects;

public class BlockedFragment extends Fragment {
    private ConversationAdapter conversationAdapter;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        interstitialAd = new InterstitialAd(Objects.requireNonNull(getContext()));
        adRequest = com.newEra.strangers.chat.util.AdRequest.getRequest();
//        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/8834499185");
        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/6074992250");
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(adRequest);
            }


        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
        this.conversationAdapter = new ConversationAdapter(InMemoryCache.blockedConversationList, view.getContext());
        recyclerView.setAdapter(this.conversationAdapter);
        if (interstitialAd.isLoaded())
            interstitialAd.show();
        return view;
    }

    public ConversationAdapter getConversationAdapter() {
        return this.conversationAdapter;
    }
}
