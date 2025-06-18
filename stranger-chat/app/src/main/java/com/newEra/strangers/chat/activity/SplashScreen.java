package com.newEra.strangers.chat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.comparators.ConversationComparator;
import com.newEra.strangers.chat.comparators.SortOrder;
import com.newEra.strangers.chat.comparators.StMessageComparator;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.GetAccountResponse;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.model.StUser;
import com.newEra.strangers.chat.model.ad.AdsResponse;
import com.newEra.strangers.chat.network.NetworkChecker;
import com.newEra.strangers.chat.network.NewCustomOkHttp;
import com.newEra.strangers.chat.socket.AllSocketEvents;
import com.newEra.strangers.chat.socket.SocketUtil;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;
import com.newEra.strangers.chat.util.IntentExtrasKeys;
import com.newEra.strangers.chat.util.NewUtil;
import com.newEra.strangers.chat.util.StaticControll;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

public class SplashScreen extends Activity {
    private boolean resetAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        this.resetAccount = getIntent().getBooleanExtra(IntentExtrasKeys.RESET_ACCOUNT, false);
        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
        new StaticControll().getInstance().setCurrentMonth(month);
        syncWithServerAsnyc();
        MobileAds.initialize(this, initializationStatus -> {
        });

//       new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashScreen.this, MainActivity.class));
//                finish();
//            }
//        }, 1000);


    }

    private void syncWithServerAsnyc() {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            public void run() {
                syncWithServer();
            }
        });
    }

    private void syncWithServer() {
        if (new NetworkChecker().haveNetworkConnection(getApplicationContext())) {
            Callable<Object> callableFetchData = new Callable<Object>() {
                /* class strangers.chat.activity.StartupActivity.AnonymousClass4 */

                public Object call() throws Exception {
                    fetchDataAndPrepareData();
                    return null;
                }
            };
            List<Callable<Object>> callableList = new ArrayList<>();
            callableList.add(callableFetchData);
            CustomThreadPoolInstance.threadPoolExecutor.executeAndWaitToComplete(callableList);
            if (InMemoryCache.myself != null) {
                if (SocketUtil.socket == null) {
                    try {
                        SocketUtil.initializeAndConnect();
                        AllSocketEvents.listenConnectedEvent();
                    } catch (URISyntaxException e) {
                    }
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                runOnUiThread(new Runnable() {
                    /* class strangers.chat.activity.StartupActivity.AnonymousClass6 */

                    public void run() {
                       alertDialogForNetwork();
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                /* class strangers.chat.activity.StartupActivity.AnonymousClass7 */

                public void run() {
                    alertDialogForNetwork();
                }
            });
        }

    }



    public void alertDialogForNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unable to connect. Please check internet connection and retry!");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            /* class strangers.chat.activity.StartupActivity.AnonymousClass9 */

            public void onClick(DialogInterface dialog, int which) {
                syncWithServerAsync();
            }
        });
        builder.setNegativeButton("exit", new DialogInterface.OnClickListener() {
            /* class strangers.chat.activity.StartupActivity.AnonymousClass10 */

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        if (!isFinishing()) {
            try {
                builder.create().show();
            } catch (Exception e) {
            }
        }
    }
    public void syncWithServerAsync() {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            /* class strangers.chat.activity.StartupActivity.AnonymousClass1 */

            public void run() {
                syncWithServer();
            }
        });
    }
    public void fetchDataAndPrepareData() {
        List<StUser> friendList;
        GetAccountResponse getAccountResponse = NewCustomOkHttp.fetchDataFromApi(Settings.Secure.getString(getContentResolver(), "android_id"), this.resetAccount);
        if (getAccountResponse != null) {
            InMemoryCache.accountResponse = getAccountResponse;
            InMemoryCache.myself = new StUser();
            InMemoryCache.myself.setUserId(getAccountResponse.getUserId());
            InMemoryCache.myself.setNickName(getAccountResponse.getNickName());
            InMemoryCache.myself.setProfileImageUrl(getAccountResponse.getProfileUrl());
            InMemoryCache.myself.setDeviceId(getAccountResponse.getDeviceId());
            InMemoryCache.myself.setFbAccountId(getAccountResponse.getFbAccountId());
            InMemoryCache.myself.setGender(getAccountResponse.getGender());
            InMemoryCache.myself.setBirthday(getAccountResponse.getBirthday());
            InMemoryCache.myself.setLivesIn(getAccountResponse.getLivesIn());
            InMemoryCache.myself.setInterests(getAccountResponse.getInterests());
            InMemoryCache.myself.setAbout(getAccountResponse.getDescription());
            if (getAccountResponse.getFriendList() == null) {
                friendList = new ArrayList<>();
            } else {
                friendList = getAccountResponse.getFriendList();
            }
            if (this.resetAccount) {
                for (StConversation stConversation : InMemoryCache.friendsConversationList) {
                    DbHandlerInstance.dbHandler.deleteMessagesByUserId(stConversation.getStUser().getUserId());
                }
                for (StConversation stConversation2 : InMemoryCache.blockedConversationList) {
                    DbHandlerInstance.dbHandler.deleteMessagesByUserId(stConversation2.getStUser().getUserId());
                }
                InMemoryCache.friendsConversationList.clear();
                InMemoryCache.blockedConversationList.clear();
            }
            for (StUser friend : friendList) {
                List<StMessage> stMessageList = DbHandlerInstance.dbHandler.getMessagesByUserId(friend.getUserId());
                if (stMessageList == null) {
                    stMessageList = new ArrayList<>();
                }
                if (friend.isBlocked()) {
                    if (!NewUtil.isUserExists(InMemoryCache.blockedConversationList, friend.getUserId())) {
                        Collections.sort(stMessageList, new StMessageComparator(SortOrder.ASCENDING));
                        InMemoryCache.blockedConversationList.add(new StConversation(friend, stMessageList));
                    }
                } else if (!NewUtil.isUserExists(InMemoryCache.friendsConversationList, friend.getUserId())) {
                    Collections.sort(stMessageList, new StMessageComparator(SortOrder.ASCENDING));
                    InMemoryCache.friendsConversationList.add(new StConversation(friend, stMessageList));
                }
            }
            Collections.sort(InMemoryCache.friendsConversationList, new ConversationComparator(SortOrder.DESCENDING));
            Collections.sort(InMemoryCache.blockedConversationList, new ConversationComparator(SortOrder.DESCENDING));
        }
    }


}
