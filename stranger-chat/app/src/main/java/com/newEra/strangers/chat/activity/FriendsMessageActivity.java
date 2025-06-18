package com.newEra.strangers.chat.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.newEra.strangers.chat.network.NewCustomOkHttp;
import com.newEra.strangers.chat.util.NewUtil;
import com.newEra.strangers.chat.util.StaticControll;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Collections;
import java.util.Date;

import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.adapter.FriendMessageAdapter;
import com.newEra.strangers.chat.comparators.ConversationComparator;
import com.newEra.strangers.chat.comparators.SortOrder;
import com.newEra.strangers.chat.comparators.StMessageComparator;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.MessageStatus;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.Keys;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.socket.AllSocketEvents;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;
import com.newEra.strangers.chat.util.Constants;
import com.newEra.strangers.chat.util.IntentExtrasKeys;
import com.newEra.strangers.chat.util.MethodUtil;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class FriendsMessageActivity extends AppCompatActivity {
    private View adViewBottom;
    private Button btnOnlineStatus;
    private ImageView btnSend;
    private File cameraImageFile = null;
    private Uri cameraImageUri = null;
    private Editor editor;
    private InterstitialAd interstitialAd;
    private EmojIconActions emojicon;
    // private LinearLayout fbAdViewContainer;
    private StConversation friendConversation = null;
    private LinearLayout inputBoxLayout;
    private ImageView ivActionMenu;
    private ImageView ivBack;
    private ImageView ivBrowse;
    private ImageView ivCamera;
    private ImageView ivEmoji;
    private ImageView ivLogo;
    private EmojiconEditText etMessage;
    private FriendMessageAdapter mAdapter;
    private FrameLayout rootView;
    private AdRequest adRequest;
    private Boolean ivActionMenuads = false;
    private Boolean ivLogoAds = false;
    private RecyclerView rvMessage;
    private AdView adView2;
    private Toolbar toolbar;
    private TextView tvBarTitle;
    private TextView tvTypingStatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_message);
        adRequest = com.newEra.strangers.chat.util.AdRequest.getRequest();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/1397380645");
//        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(adRequest);
        String userId = getIntent().getStringExtra(IntentExtrasKeys.USER_ID);
        if (userId == null || userId.length() == 0) {
            finish();
        } else {
            for (StConversation stConversation : InMemoryCache.friendsConversationList) {
                if (userId.equals(stConversation.getStUser().getUserId())) {
                    this.friendConversation = stConversation;
                    break;
                }
            }
            if (this.friendConversation == null) {
                for (StConversation stConversation2 : InMemoryCache.blockedConversationList) {
                    if (userId.equals(stConversation2.getStUser().getUserId())) {
                        this.friendConversation = stConversation2;
                        break;
                    }
                }
            }
            if (this.friendConversation == null || this.friendConversation.getStUser() == null) {
                finish();
            }
        }
        this.rvMessage = findViewById(R.id.rv_message);
        this.etMessage = findViewById(R.id.et_messages);
        this.btnSend = findViewById(R.id.btn_send_messeage_activitys);
        this.ivEmoji = (ImageView) findViewById(R.id.ic_cutes);
        this.rootView = findViewById(R.id.root_view_frame);
        this.adView2 = findViewById(R.id.adView2);
        this.inputBoxLayout = findViewById(R.id.input_box_layout);
        this.toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        this.ivBack = (ImageView) findViewById(R.id.back);
        this.ivActionMenu = (ImageView) findViewById(R.id.action_menu);
        this.tvBarTitle = (TextView) findViewById(R.id.toolbar_title);
        this.tvTypingStatus = (TextView) findViewById(R.id.toolbar_typing_status);
        this.btnOnlineStatus = (Button) findViewById(R.id.online_status);
        this.ivBrowse = (ImageView) findViewById(R.id.image_browse);
        this.ivCamera = (ImageView) findViewById(R.id.image_camera);
        this.ivLogo = (ImageView) findViewById(R.id.logo);
        this.adViewBottom = findViewById(R.id.adViewBottom);
        // this.fbAdViewContainer =  findViewById(R.id.fb_top_banner);
        adView2.loadAd(adRequest);
        adView2.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("Naman", i + "");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adView2.loadAd(adRequest);


            }
        });
        emojicon = new EmojIconActions(FriendsMessageActivity.this, rootView, etMessage, ivEmoji);

        CharSequence nickName = this.friendConversation.getStUser().getNickName();
        TextView textView = this.tvBarTitle;
        if (nickName == null) {
            nickName = "stranger";
        }
        textView.setText(nickName);
        if (this.friendConversation.isOnline()) {
            this.btnOnlineStatus.setVisibility(View.VISIBLE);
        } else {
            this.btnOnlineStatus.setVisibility(View.INVISIBLE);
        }
        ivEmoji.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);
                emojicon.ShowEmojIcon();
                emojicon.setUseSystemEmoji(true);
                etMessage.setUseSystemDefault(true);
            }
        });
        this.tvTypingStatus.setText(getToolBarOnlineStatusText());
        Picasso.get().load(this.friendConversation.getStUser().getProfileImageUrl()).placeholder(this.ivLogo.getDrawable()).into(this.ivLogo);
        this.ivLogo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    ivLogoAds = true;
                } else {
                    String imagePath = FriendsMessageActivity.this.friendConversation.getStUser().getProfileImageUrl();
                    if (imagePath.length() > 0) {
                        Intent intent = new Intent(v.getContext(), FullscreenActivity.class);
                        intent.putExtra(Keys.IMAGE_URL, imagePath);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                    }
                }

            }
        });
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (this.friendConversation.getStUser().isBlocked()) {
            this.inputBoxLayout.setVisibility(View.INVISIBLE);
            this.adViewBottom.setVisibility(View.VISIBLE);
        } else {
            this.inputBoxLayout.setVisibility(View.VISIBLE);
            this.adViewBottom.setVisibility(View.GONE);
        }
        LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.rvMessage.setLayoutManager(layoutManager);
        this.rvMessage.setItemAnimator(new DefaultItemAnimator());
        this.mAdapter = new FriendMessageAdapter(this.friendConversation.getStMessageList(), this);
        this.rvMessage.setAdapter(this.mAdapter);
        this.rvMessage.scrollToPosition(this.mAdapter.getItemCount() - 1);
        this.btnSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //  String message = FriendsMessageActivity.this.etMessage.getText().toString();
                String message = etMessage.getText().toString();
                if (message != null && message.length() > 0) {
                    String msgId = MethodUtil.generateMsgId();
                    StMessage stMessage = new StMessage();
                    stMessage.setMsgId(msgId);
                    stMessage.setUserId(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
                    stMessage.setMsg(message);
                    stMessage.setMsgType(0);
                    stMessage.setStatus(MessageStatus.PENDING);
                    stMessage.setMsgTime(new Date().getTime());
                    stMessage.setLocalImage(new Image("", 0, 0));
                    stMessage.setThumbnailUrl(new Image("", 0, 0));
                    stMessage.setOriginalUrl(new Image("", 0, 0));
                    AllSocketEvents.triggerSendFriendMsg(stMessage, FriendsMessageActivity.this.friendConversation.getStUser());
                    FriendsMessageActivity.this.etMessage.setText("");
                }

            }
        });
        this.ivBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FriendsMessageActivity.this.finish();
            }
        });
        this.ivActionMenu.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded() && new StaticControll().getInstance().getCurrentMonth() >= 3) {
                    interstitialAd.show();
                    ivActionMenuads = true;
                } else {
                    FriendsMessageActivity.this.showAlertDialogMenu(FriendsMessageActivity.this);
                }
            }
        });
        this.ivBrowse.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FriendsMessageActivity.this.pickImageFromGallery();
            }
        });
        this.ivCamera.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FriendsMessageActivity.this.pickImageFromCamera();
            }
        });
        ivEmoji.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);
                emojicon.ShowEmojIcon();
                emojicon.setUseSystemEmoji(true);
                etMessage.setUseSystemDefault(true);
            }
        });
        registerTypingEvent();
        refreshConversationList(this.mAdapter, layoutManager, this.rvMessage);
        sendPendingMsgAgain();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (ivActionMenuads) {
                    ivActionMenu.performClick();
                    ivActionMenuads = false;
                } else if (ivLogoAds) {
                    ivLogo.performClick();
                    ivLogoAds = false;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AdRequest();
                    }
                }, 2500);
            }
        });
    }

    public void AdRequest() {
        interstitialAd.loadAd(adRequest);
    }

    private void registerTypingEvent() {
        this.etMessage.addTextChangedListener(new TextWatcher() {
            private Runnable runnable_EditTextWatcher = new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AllSocketEvents.triggerFriendTypingEnd(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
                    thread = null;
                }
            };
            private Thread thread;

            public void onTextChanged(CharSequence ss, int start, int before, int count) {
                AllSocketEvents.triggerFriendTypingStart(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                FriendsMessageActivity.this.rvMessage.scrollToPosition(FriendsMessageActivity.this.mAdapter.getItemCount() - 1);
            }

            public void afterTextChanged(Editable ss) {
                if (this.thread == null) {
                    this.thread = new Thread(this.runnable_EditTextWatcher);
                    this.thread.start();
                }
            }
        });

    }


    private AlertDialog getDialogForImageUpload(String localUri, int width, int height, Bitmap bitmap) {
        View view = getLayoutInflater().inflate(R.layout.dialog_image_send, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_send);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.dialog_layout);
        int h = (height * 550) / width;
        imageView.setImageBitmap(bitmap);
        Picasso.get().load(localUri).placeholder(imageView.getDrawable()).resize(550, h).centerCrop().into(imageView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final String str = localUri;
        final Bitmap bitmap2 = bitmap;
        final int i = width;
        final int i2 = height;
        builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                if (str != null) {
//                    try {
//                        Bitmap bitmapToUpload = MethodUtil.getResizedBitmap(bitmap2);
//                        StMessage stMessage = FriendsMessageActivity.this.saveImageMsg(i, i2, bitmapToUpload);
//                        if (stMessage != null) {
//                            File fileToUpload = MethodUtil.getFileFromBitmap(FriendsMessageActivity.this.getApplicationContext(), bitmapToUpload);
//                            if (fileToUpload != null) {
//                                NewCustomOkHttp.uploadFriendChatImage(FriendsMessageActivity.this.getApplicationContext(), fileToUpload, stMessage.getMsgId(), InMemoryCache.myself.getUserId(), stMessage.getUserId(), Long.valueOf(stMessage.getMsgTime()));
//                            }
//                        }
//                    } catch (Exception e) {
//                    }
//                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = null;
        try {
            return builder.create();
        } catch (Exception e) {
            return alertDialog;
        }
    }

    private void pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.GALLERY_IMAGE);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    private void pickImageFromCamera() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            this.cameraImageFile = MethodUtil.createCameraTempImageFile(getApplicationContext());
            if (cameraIntent.resolveActivity(getPackageManager()) != null && this.cameraImageFile != null) {
                try {
                    this.cameraImageUri = FileProvider.getUriForFile(getApplicationContext(), Constants.FILE_PROVIDER_AUTHORITY, this.cameraImageFile);
                    if (VERSION.SDK_INT <= 21) {
                        cameraIntent.setClipData(ClipData.newRawUri("", this.cameraImageUri));
                        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    cameraIntent.putExtra("output", this.cameraImageUri);
                    startActivityForResult(cameraIntent, Constants.CAMERA_IMAGE);
                    return;
                } catch (Exception e) {
                    return;
                }
            }
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    private StMessage saveImageMsg(int width, int height, Bitmap bitmap) {
        try {
            StMessage stMessage = new StMessage();
            stMessage.setMsgId(MethodUtil.generateMsgId());
            stMessage.setUserId(this.friendConversation.getStUser().getUserId());
            stMessage.setMsg("");
            stMessage.setMsgType(4);
            stMessage.setStatus(MessageStatus.PENDING);
            stMessage.setMsgTime(new Date().getTime());
            stMessage.setLocalImage(new Image(FileProvider.getUriForFile(getApplicationContext(), Constants.FILE_PROVIDER_AUTHORITY, MethodUtil.saveBitmap(bitmap, 4, stMessage.getMsgId() + ".png")).toString(), width, height));
            stMessage.setThumbnailUrl(new Image("", width, height));
            stMessage.setOriginalUrl(new Image("", width, height));
            this.friendConversation.getStMessageList().add(stMessage);
            DbHandlerInstance.dbHandler.addMessage(stMessage);
            Collections.sort(this.friendConversation.getStMessageList(), new StMessageComparator(SortOrder.ASCENDING));
            Collections.sort(InMemoryCache.friendsConversationList, new ConversationComparator(SortOrder.DESCENDING));
            return stMessage;
        } catch (Exception e) {
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == Constants.GALLERY_IMAGE) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    bitmap = BitmapFactory.decodeStream(new BufferedInputStream(getApplicationContext().getContentResolver().openInputStream(uri)));
                    if (bitmap != null) {
                        getDialogForImageUpload(uri.toString(), bitmap.getWidth(), bitmap.getHeight(), bitmap).show();
                    }
                }
            } catch (Exception e) {
            }
        }
        if (requestCode == Constants.CAMERA_IMAGE && resultCode == -1) {
            try {
                if (this.cameraImageUri != null && this.cameraImageFile != null) {
                    bitmap = BitmapFactory.decodeStream(new BufferedInputStream(getApplicationContext().getContentResolver().openInputStream(this.cameraImageUri)));
                    if (bitmap != null) {
                        getDialogForImageUpload(this.cameraImageUri.toString(), bitmap.getWidth(), bitmap.getHeight(), bitmap).show();
                    }
                }
            } catch (Exception e2) {
            }
        }
    }

    private void refreshConversationList(final FriendMessageAdapter friendMessageAdapter, final LayoutManager layoutManager, final RecyclerView recyclerView) {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            public void run() {
                while (!FriendsMessageActivity.this.isFinishing()) {
                    try {
                        if (System.currentTimeMillis() % 1500 == 0) {
                            FriendsMessageActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    FriendsMessageActivity.this.tvTypingStatus.setText(FriendsMessageActivity.this.getToolBarOnlineStatusText());
                                    if (FriendsMessageActivity.this.friendConversation.isOnline()) {
                                        FriendsMessageActivity.this.btnOnlineStatus.setVisibility(View.VISIBLE);
                                    } else {
                                        FriendsMessageActivity.this.btnOnlineStatus.setVisibility(View.INVISIBLE);
                                    }
                                    friendMessageAdapter.notifyDataSetChanged();
                                    int position = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                                    if (position >= 0 && position < FriendsMessageActivity.this.mAdapter.getStMessageList().size()) {
                                        int msgType = ((StMessage) FriendsMessageActivity.this.mAdapter.getStMessageList().get(position)).getMsgType();
                                        if (position != FriendsMessageActivity.this.mAdapter.getItemCount() - 2) {
                                            return;
                                        }
                                        if (msgType == 0 || msgType == 1) {
                                            recyclerView.scrollToPosition(FriendsMessageActivity.this.mAdapter.getItemCount() - 1);
                                        }
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

    private String getToolBarOnlineStatusText() {
        if (this.friendConversation.isTyping()) {
            return "typing...";
        }
        if (this.friendConversation.isOnline()) {
            return "online";
        }
        if (this.friendConversation.getStUser().getLastOnlineAt() > 0) {
            return MethodUtil.getLastOnlineAtStr(this.friendConversation.getStUser().getLastOnlineAt());
        }
        return "offline";
    }

    private void sendPendingMsgAgain() {
//        if (new NetworkChecker().haveNetworkConnection(this)) {
//            for (final StMessage stMessage : this.friendConversation.getStMessageList()) {
//                if (stMessage.getMsgType() == 0 && stMessage.getStatus().equals(MessageStatus.PENDING)) {
//                    AllSocketEvents.triggerSendFriendTextMsg(stMessage.getMsgId(), InMemoryCache.myself.getUserId(), stMessage.getUserId(), stMessage.getMsg(), stMessage.getMsgTime());
//                }
//                if (stMessage.getMsgType() == 4 && stMessage.getStatus().equals(MessageStatus.PENDING)) {
//                    CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
//                        public void run() {
//                            try {
//                                String uri = stMessage.getLocalImage().getUrl();
//                                if (uri != null && uri.length() > 0) {
//                                    File fileToUpload = MethodUtil.getFileFromBitmap(FriendsMessageActivity.this.getApplicationContext(), BitmapFactory.decodeStream(new BufferedInputStream(FriendsMessageActivity.this.getContentResolver().openInputStream(Uri.parse(stMessage.getLocalImage().getUrl())))));
//                                    if (fileToUpload != null) {
//                                        CustomOkHttp.uploadFriendChatImage(FriendsMessageActivity.this.getApplicationContext(), fileToUpload, stMessage.getMsgId(), InMemoryCache.myself.getUserId(), stMessage.getUserId(), Long.valueOf(stMessage.getMsgTime()));
//                                    }
//                                }
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//                }
//            }
//        }
    }

    private void showAlertDialogMenu(Context context) {
        View view = getLayoutInflater().inflate(R.layout.alert_box_friend_chat_menu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        Button btnClearChat = (Button) view.findViewById(R.id.clear_chat);
        Button btnDelete = (Button) view.findViewById(R.id.delete);
        Button btnBlock = (Button) view.findViewById(R.id.block);
        Button btnUnBlock = (Button) view.findViewById(R.id.unblock);
        if (this.friendConversation.getStUser().isBlocked()) {
            btnBlock.setVisibility(View.GONE);
            btnUnBlock.setVisibility(View.VISIBLE);
        } else {
            btnBlock.setVisibility(View.VISIBLE);
            btnUnBlock.setVisibility(View.GONE);
        }
        btnClearChat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FriendsMessageActivity.this.showAlertDialogForConfirmation(FriendsMessageActivity.this, 0, FriendsMessageActivity.this.friendConversation.getStUser().getNickName());
                alertDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FriendsMessageActivity.this.showAlertDialogForConfirmation(FriendsMessageActivity.this, 1, FriendsMessageActivity.this.friendConversation.getStUser().getNickName());
                alertDialog.dismiss();
            }
        });
        btnBlock.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NewCustomOkHttp.blockUnblockFriend(InMemoryCache.myself.getUserId(), FriendsMessageActivity.this.friendConversation.getStUser().getUserId(), true).booleanValue()) {
                    NewUtil.moveBlockedFriend(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
                }
                FriendsMessageActivity.this.finish();
            }
        });
        btnUnBlock.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NewCustomOkHttp.blockUnblockFriend(InMemoryCache.myself.getUserId(), FriendsMessageActivity.this.friendConversation.getStUser().getUserId(), false).booleanValue()) {
                    NewUtil.moveUnBlockedFriend(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
                }
                FriendsMessageActivity.this.finish();

            }
        });
        alertDialog.show();
    }

    private void showAlertDialogForConfirmation(Context context, final int action, String friendName) {
        String msg = "Are you sure";
        String btnText = "Ok";
        if (action == 0) {
            msg = "Are you sure you want to clear your chat history with " + friendName + "?";
            btnText = "Clear Chat";
        } else if (action == 1) {
            if (friendName == null) {
                friendName = "friend";
            }
            msg = "Are you sure you want to remove " + friendName + " from your friend list?";
            btnText = "Remove";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (action == 0) {
                    FriendsMessageActivity.this.friendConversation.getStMessageList().removeAll(FriendsMessageActivity.this.friendConversation.getStMessageList());
                    DbHandlerInstance.dbHandler.deleteMessagesByUserId(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
                } else if (action == 1) {
                    if (NewCustomOkHttp.removeFriend(InMemoryCache.myself.getUserId(), FriendsMessageActivity.this.friendConversation.getStUser().getUserId()).booleanValue()) {
                        NewUtil.deleteConversation(FriendsMessageActivity.this.friendConversation.getStUser().getUserId());
                    }
                    FriendsMessageActivity.this.finish();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
