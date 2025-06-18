package com.newEra.strangers.chat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.newEra.strangers.chat.database.model.MessageStatus;
import com.newEra.strangers.chat.model.MessagePayload;
import com.newEra.strangers.chat.model.MessageStatusWrapper;
import com.newEra.strangers.chat.model.PendingMessageStatus;
import com.newEra.strangers.chat.network.NewCustomOkHttp;
import com.newEra.strangers.chat.util.NewUtil;
import com.newEra.strangers.chat.util.StaticControll;
import com.newEra.strangers.chat.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.comparators.ConversationComparator;
import com.newEra.strangers.chat.comparators.SortOrder;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.model.StUser;
//import com.newEra.strangers.chat.network.CustomOkHttp;
import com.newEra.strangers.chat.network.NetworkChecker;
import com.newEra.strangers.chat.socket.AllSocketEvents;
import com.newEra.strangers.chat.socket.SocketUtil;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;
import com.newEra.strangers.chat.util.Constants;
import com.newEra.strangers.chat.util.MethodUtil;

public class MainActivity extends AppCompatActivity {
    private View adViewBottom;
    private View adViewTop;
    private Button btnSave;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;
    private RelativeLayout count_chat_layout;
    private Button btnStart;
    private Button btnUnseenMsgCount;
    private EditText etNickName;
    private File file = null;
    private ImageView ivFriends;
    private ImageView ivProfilePic;
    private ProgressBar progressBar, image_Progress;
    private SharedPreferences.Editor sharedPreferences;
    private Toolbar toolbar;
    private View view_click;
    private Boolean isChanging = false;
    private Boolean chatStartAds = false;
    private Boolean openFriendActivityAds = false;
    private Boolean btnSaveAds = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("ChatAPP", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        new StaticControll().getInstance();
        adRequest = com.newEra.strangers.chat.util.AdRequest.getRequest();
        interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/1134551681");
        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/2518890622");
        interstitialAd.loadAd(adRequest);
        InMemoryCache.context = getApplicationContext();
        this.btnStart = findViewById(R.id.btn_start);
        this.btnSave = findViewById(R.id.save);
        this.count_chat_layout = findViewById(R.id.count_chat_layout);
        this.adViewTop = findViewById(R.id.adViewTop);
        this.adViewBottom = findViewById(R.id.adViewBottom);
        this.ivProfilePic = findViewById(R.id.profile_url);
        this.view_click = findViewById(R.id.view_click);
        this.etNickName = findViewById(R.id.et_nickname);
        MainActivity.this.etNickName.setEnabled(false);

        this.ivFriends = findViewById(R.id.friends);
        this.progressBar = findViewById(R.id.progressBar);
        this.image_Progress = findViewById(R.id.image_Progress);
        this.btnUnseenMsgCount = findViewById(R.id.unseen_msg_count);
        this.btnSave.setVisibility(View.GONE);
        this.btnUnseenMsgCount.setVisibility(View.GONE);
        DbHandlerInstance.initializeDbHandler(getApplicationContext());
        if (!sharedPreferences.getBoolean("FirstTimeLoad", false))
            alertDialogForPrivacy();
        if (new NetworkChecker().haveNetworkConnection(getApplicationContext())) {
            if (InMemoryCache.myself != null) {
                try {
                    SocketUtil.initializeAndConnect();
                    AllSocketEvents.listenConnectedEvent();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                updateProfileChanges(null);
            }
        }
        this.toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.btnStart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                    chatStartAds = true;
                }else {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), MessageActivity.class));
                }

//                if (interstitialAd.isLoaded() && new StaticControll().getCurrentMonth() >= 3)
//                    interstitialAd.show();
            }
        });
        this.ivFriends.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                    openFriendActivityAds = true;
                }else {
                    if (InMemoryCache.myself != null) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ConversationActivity.class));
                    }
                }
            }
        });
        this.ivProfilePic.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                count_chat_layout.setVisibility(View.GONE);
                MainActivity.this.pickImageFromGallery();
                MainActivity.this.btnStart.setVisibility(View.GONE);
                MainActivity.this.btnSave.setVisibility(View.VISIBLE);
            }
        });
        this.view_click.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.etNickName.setEnabled(true);
                MainActivity.this.btnStart.setVisibility(View.GONE);
                MainActivity.this.view_click.setVisibility(View.GONE);
                MainActivity.this.btnSave.setVisibility(View.VISIBLE);
                isChanging = true;
                etNickName.requestFocus();
                openKeybaord();
            }
        });
        this.btnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                hideKeyBoard();

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    btnSaveAds = true;
                } else {
                    new SaveDataTask().execute();
                }

            }
        });
        refreshUnSeenMsgCount();
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
                public void run() {
                    MethodUtil.saveProfileImagesAsync(MainActivity.this.getApplicationContext());
                }
            });
        }
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

                if (chatStartAds) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), MessageActivity.class));
                    chatStartAds = false;
                } else if (openFriendActivityAds) {
                    if (InMemoryCache.myself != null) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), ConversationActivity.class));
                    }
                    openFriendActivityAds = false;
                } else if (btnSaveAds) {
                    new SaveDataTask().execute();
                    btnSaveAds = false;
                }
                LoadAdd();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("CHAT_STARNGER_ADS", i + "");
            }
        });
        fetchPendingMessagesAsync();
        fetchPendingMessageStatusesAsync();
        refreshUnSeenMsgCount();

    }

    private void openKeybaord() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                toolbar.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }
    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    public void LoadAdd() {
        interstitialAd.loadAd(adRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == Constants.GALLERY_IMAGE && data != null) {
            try {
                if (data.getData() != null) {
                    this.file = MethodUtil.getFileFromBitmap(getApplicationContext(), MethodUtil.getResizedBitmap(BitmapFactory.decodeStream(new BufferedInputStream(getApplicationContext().getContentResolver().openInputStream(data.getData())))));
//                    MainActivity.this.image_Progress.setVisibility(View.VISIBLE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data == null) {
            MainActivity.this.btnStart.setVisibility(View.VISIBLE);
            MainActivity.this.btnSave.setVisibility(View.GONE);
            MainActivity.this.count_chat_layout.setVisibility(View.VISIBLE);
        }
    }

    private void pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.GALLERY_IMAGE);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    private void alertDialogForBlockedSelf() {
        if (InMemoryCache.accountResponse != null && InMemoryCache.accountResponse.isBlocked() && InMemoryCache.accountResponse.getBlockedDto() != null && StringUtil.isNotEmptyStr(InMemoryCache.accountResponse.getBlockedDto().getHeading()) && StringUtil.isNotEmptyStr(InMemoryCache.accountResponse.getBlockedDto().getBody1()) && StringUtil.isNotEmptyStr(InMemoryCache.accountResponse.getBlockedDto().getBody2())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(InMemoryCache.accountResponse.getBlockedDto().getHeading());
            builder.setMessage(InMemoryCache.accountResponse.getBlockedDto().getBody1() + "\n" + InMemoryCache.accountResponse.getBlockedDto().getBody2());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                /* class strangers.chat.activity.MainActivity.AnonymousClass15 */

                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            });
            builder.create().show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateProfileChanges(this.file);


    }


    private void updateProfileChanges(final File file) {
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.etNickName.setText(InMemoryCache.myself.getNickName());
                    if (file == null) {
                        Picasso.get().load(InMemoryCache.myself.getProfileImageUrl()).resize(500, 500).centerCrop().into(MainActivity.this.ivProfilePic);
                    } else if (file != null) {
                        Picasso.get().load(file).resize(500, 500).centerCrop().into(MainActivity.this.ivProfilePic);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    protected void onDestroy() {
        if (SocketUtil.socket != null && SocketUtil.socket.connected()) {
            SocketUtil.socket.disconnect();
        }
        super.onDestroy();
    }


    private void refreshUnSeenMsgCount() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            /* class strangers.chat.activity.MainActivity.AnonymousClass11 */

            public void run() {
                int noOfUnseenMsg = 0;
                try {
                    for (StConversation stConversation : InMemoryCache.friendsConversationList) {
                        noOfUnseenMsg += MethodUtil.getNoOfUnseenMsgs(stConversation.getStMessageList());
                    }
                    final int count = noOfUnseenMsg;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        /* class strangers.chat.activity.MainActivity.AnonymousClass11.AnonymousClass1 */

                        public void run() {
                            if (count > 0) {
                                MainActivity.this.btnUnseenMsgCount.setText(String.valueOf(count));
                                MainActivity.this.btnUnseenMsgCount.setVisibility(View.VISIBLE);
                                return;
                            }
                            MainActivity.this.btnUnseenMsgCount.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                } finally {
                    handler.postDelayed(this, 1500);
                }
            }
        });
    }

    private void alertDialogForPrivacy() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Important Thing");
        builder.setMessage("For Kind Your Information -> Never share your or someone else's personal information such as phone number, email and location. There's no responsibility from our side if something goes wrong  with you.\nEnjoy Stranger Chat App And Take Care \n\nThankYou");
        builder.setCancelable(false);
        builder.setPositiveButton("close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences = getSharedPreferences("ChatAPP", MODE_PRIVATE).edit();
                sharedPreferences.putBoolean("FirstTimeLoad", true);
                sharedPreferences.apply();
            }

        });
        builder.create().show();

    }

    private void fetchPendingMessageStatusesAsync() {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            /* class strangers.chat.activity.MainActivity.AnonymousClass17 */

            public void run() {
                List<PendingMessageStatus> pendingMessageStatuses;
                if (InMemoryCache.myself != null && (pendingMessageStatuses = NewCustomOkHttp.getPendingMessageStatuses(InMemoryCache.myself.getUserId())) != null && pendingMessageStatuses.size() > 0) {
                    for (PendingMessageStatus pendingMessageStatus : pendingMessageStatuses) {
                        NewUtil.updateMsgStatusInConversation(pendingMessageStatus);
                    }
                }
            }
        });
    }

    private void fetchPendingMessagesAsync() {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            /* class strangers.chat.activity.MainActivity.AnonymousClass16 */

            public void run() {
                List<MessagePayload> messagePayloadList;
                StUser stUser;
                if (InMemoryCache.myself != null && (messagePayloadList = NewCustomOkHttp.getPendingMessages(InMemoryCache.myself.getUserId())) != null && messagePayloadList.size() > 0) {
                    List<PendingMessageStatus> pendingMessageStatusList = new ArrayList<>();
                    for (MessagePayload messagePayload : messagePayloadList) {
                        StMessage stMessage = NewUtil.convertMessagePayloadToStMessage(messagePayload, MessageStatus.RECEIVED);
                        PendingMessageStatus pendingMessageStatus = new PendingMessageStatus();
                        pendingMessageStatus.setSenderId(InMemoryCache.myself.getUserId());
                        pendingMessageStatus.setReceiverId(stMessage.getUserId());
                        pendingMessageStatus.setMessageId(stMessage.getMsgId());
                        pendingMessageStatus.setStatus(MessageStatus.DELIVERED);
                        if (NewUtil.isUserOnline(stMessage.getUserId())) {
                            AllSocketEvents.triggerFriendMessageStatus(pendingMessageStatus);
                        } else {
                            pendingMessageStatusList.add(pendingMessageStatus);
                        }
                        if (!NewUtil.isUserExists(InMemoryCache.friendsConversationList, stMessage.getUserId()) && !NewUtil.isUserExists(InMemoryCache.blockedConversationList, stMessage.getUserId()) && (stUser = NewCustomOkHttp.addFriend(InMemoryCache.myself.getUserId(), stMessage.getUserId())) != null && !stUser.isBlocked()) {
                            InMemoryCache.friendsConversationList.add(new StConversation(stUser, new ArrayList()));
                            Collections.sort(InMemoryCache.friendsConversationList, new ConversationComparator(SortOrder.DESCENDING));
                        }
                        NewUtil.addMsgInConversationAndSave(stMessage);
                    }
                    if (pendingMessageStatusList.size() > 0) {
                        MessageStatusWrapper statusWrapper = new MessageStatusWrapper();
                        statusWrapper.setPendingMessageStatusList(pendingMessageStatusList);
                        NewCustomOkHttp.savePendingMessageStatusBulk(statusWrapper);
                    }
                }
            }
        });
    }

    /**
     * Save Profile Picture
     **/
    class SaveDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.btnSave.setVisibility(View.GONE);
            MainActivity.this.image_Progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.this.runOnUiThread(new Runnable() {
                /* class strangers.chat.activity.EditMyProfileActivity.AnonymousClass5.AnonymousClass4 */
                public void run() {
                    Log.i("UPDATE", "UI Case");
                    MainActivity.this.image_Progress.setVisibility(View.GONE);
                    count_chat_layout.setVisibility(View.VISIBLE);
                    MainActivity.this.btnStart.setVisibility(View.VISIBLE);
                    MainActivity.this.etNickName.setEnabled(false);
                    MainActivity.this.btnSave.setVisibility(View.GONE);
                    MainActivity.this.view_click.setVisibility(View.VISIBLE);
                    if (file != null) {
                        Picasso.get().load(file).resize(500, 500).centerCrop().into(MainActivity.this.ivProfilePic);
                    }
                }
            });

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String name = etNickName.getText().toString() == null ? "Stranger:007" : etNickName.getText().toString();
            if (name != null)
                if (StringUtil.isNotEmptyStr(name)) {
                    InMemoryCache.myself.setNickName(name);
                }
            if (MainActivity.this.file == null) {
                CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
                    public void run() {
                        Log.i("UPDATE", "Null File");
                        NewCustomOkHttp.saveUserDataWithoutImage(InMemoryCache.myself);
                    }
                });
            } else {

                Log.i("UPDATE", "BC");

                CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndWaitToComplete(new Runnable() {
                    /* class strangers.chat.activity.EditMyProfileActivity.AnonymousClass5.AnonymousClass3 */
                    public void run() {
                        String newProfileUrl = NewCustomOkHttp.saveUserDataWithImage(InMemoryCache.myself, MainActivity.this.file);
                        if (StringUtil.isNotEmptyStr(newProfileUrl)) {
                            InMemoryCache.myself.setProfileImageUrl(newProfileUrl);
                        }


                    }
                });
            }
            return null;
        }
    }
}

