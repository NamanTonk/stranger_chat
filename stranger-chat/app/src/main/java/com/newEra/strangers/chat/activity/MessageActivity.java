package com.newEra.strangers.chat.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.newEra.strangers.chat.api.Retro;
import com.newEra.strangers.chat.model.Keys;
import com.newEra.strangers.chat.model.StUser;
import com.newEra.strangers.chat.model.profile_Model;
import com.newEra.strangers.chat.network.NewCustomOkHttp;
import com.newEra.strangers.chat.util.BlurTransform;
import com.newEra.strangers.chat.util.NewUtil;
import com.newEra.strangers.chat.util.StaticControll;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.socket.emitter.Emitter;
import io.socket.emitter.Emitter.Listener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.adapter.MessageAdapter;
import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.ImageData;
import com.newEra.strangers.chat.model.Message;
import com.newEra.strangers.chat.socket.SocketUtil;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;
import com.newEra.strangers.chat.util.Constants;
import com.newEra.strangers.chat.util.CustomJsonParser;
import com.newEra.strangers.chat.util.MethodUtil;

public class MessageActivity extends AppCompatActivity implements Callback<profile_Model> {
    private ImageView btnSend;
    private File cameraImageFile = null;
    private Uri cameraImageUri = null;
    private EmojiconEditText etMessage;
    private ImageView ivBrowse;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest = new com.newEra.strangers.chat.util.AdRequest().getRequest();
    private CircleImageView profile_picture;
    private EmojIconActions emojicon;
    private ImageView ivCamera, logo;
    public Gson gson = new Gson();
    private ImageView ivCross;
    private ImageView ivEmoji;
    private MessageAdapter mAdapter;
    private FrameLayout rootView;
    private RecyclerView rvMessage;
    private LayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView tvBarTitle;
    private String profile_pic_Url;
    private Boolean profilePictureAds = false;
    private Boolean emojiKeyboardAds = false;
    private Boolean closeChatAds = false;
    private Boolean tvBrowseAds = false;
    private Boolean adFriendEndChatPositiveAds = false;
    private Boolean adFriendEndChatAdsNegativeAds = false;
    private Boolean leaveChatAds = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/9887139675");
        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/6074992250");
        interstitialAd.loadAd(adRequest);
        etMessage = findViewById(R.id.et_message);
        this.rvMessage = findViewById(R.id.rv_message);
        this.btnSend = findViewById(R.id.btn_send);
        this.profile_picture = findViewById(R.id.profile_picture);
        this.ivEmoji = findViewById(R.id.ic_cute);
        this.rootView = findViewById(R.id.root_view);
        this.toolbar = findViewById(R.id.my_toolbar);
        this.ivCross = findViewById(R.id.end_chat);
        this.logo = findViewById(R.id.bc_logo);
        this.tvBarTitle = findViewById(R.id.toolbar_title);
        this.ivBrowse = findViewById(R.id.image_browse);
        this.ivCamera = findViewById(R.id.image_camera);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        this.rvMessage.setLayoutManager(layoutManager);
        this.rvMessage.setItemAnimator(new DefaultItemAnimator());
        this.mAdapter = new MessageAdapter(new ArrayList(), getApplicationContext(), this.rvMessage);
        this.rvMessage.setAdapter(this.mAdapter);
        emojicon = new EmojIconActions(this, rootView, etMessage, ivEmoji);
        btnSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (message != null && message.length() > 0 && SocketUtil.socket != null) {
                    SocketUtil.socket.emit("send_message", message);
                    MessageActivity.this.mAdapter.addElementAndUpdate(new Message(0, message));
                    MessageActivity.this.etMessage.setText("");
                }
            }
        });
        profile_picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd.isLoaded() && new StaticControll().getInstance().getCurrentMonth() >= 3) {
                    interstitialAd.show();
                    profilePictureAds = true;
                } else {
                    startActivity(new Intent(MessageActivity.this, FullscreenActivity.class).putExtra(Keys.IMAGE_URL, profile_pic_Url));
                }
            }
        });
        ivEmoji.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    emojiKeyboardAds = true;
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);
                    emojicon.ShowEmojIcon();
                    emojicon.setUseSystemEmoji(true);
                    etMessage.setUseSystemDefault(true);
                }


            }
        });
        this.ivCross.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    if (InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID == null) {
                        MessageActivity.this.finish();
                    } else {
                        MessageActivity.this.alertDialogForEndChatByMe();
                    }
                }

            }
        });
        this.ivBrowse.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    tvBrowseAds = true;
                } else {
                    MessageActivity.this.pickImageFromGallery();
                }
            }
        });
        this.ivCamera.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MessageActivity.this.pickImageFromCamera();
            }
        });
        this.tvBarTitle.setText("waiting...");
        logo.setVisibility(View.GONE);
        if (SocketUtil.socket != null)
            startChat();
        else {
            try {
                SocketUtil.initializeAndConnect();
                startChat();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void startChat() {
        if (SocketUtil.socket != null) {
            getRoomAndRegisterStatusEvent();
            registerReceiveMessageEvent();
            listenTypingEvent();
            registerChatEndEvent();
            registerTypingEvent();
            registerImageEvent();
        }
        Retro.api().PROFILE_MODEL_CALL().enqueue(MessageActivity.this);
        interstitialAd.setAdListener(new AdListener() {
            /* private Boolean profilePictureAds = false;
    private Boolean emojiKeyboardAds = false;
    private Boolean closeChatAds = false;
    private Boolean tvBrowseAds = false;
    private Boolean adFriendEndChatPositiveAds = false;
    private Boolean adFriendEndChatAdsNegativeAds = false;*/
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (profilePictureAds) {
                    profile_picture.performClick();
                    profilePictureAds = false;
                } else if (emojiKeyboardAds) {
                    ivEmoji.performClick();
                    emojiKeyboardAds = false;
                } else if (closeChatAds) {
                    ivCross.performClick();
                    closeChatAds = false;
                } else if (tvBrowseAds) {
                    ivBrowse.performClick();
                    tvBrowseAds = false;
                } else if (adFriendEndChatAdsNegativeAds) {
                    StUser stUser = NewCustomOkHttp.addFriend(InMemoryCache.myself.getUserId(), InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID);
                    if (stUser != null) {
                        NewUtil.addFriend(stUser, MethodUtil.getStMsgFromMsgs(MessageActivity.this.mAdapter.getMessages(), stUser), true);
                    }
                    MessageActivity.this.endChat();
                    adFriendEndChatAdsNegativeAds = false;
                } else if (adFriendEndChatPositiveAds) {
                    MessageActivity.this.endChat();
                    adFriendEndChatPositiveAds = false;
                } else if (leaveChatAds) {
                    MessageActivity.this.endChat();
                    leaveChatAds = false;
                }
                AdRequest();

            }
        });
        refreshConversationList(this.mAdapter, layoutManager, this.rvMessage);

    }

    public void AdRequest() {
        interstitialAd.loadAd(adRequest);
    }

    private void getRoomAndRegisterStatusEvent() {
        SocketUtil.socket.emit("get_a_room", InMemoryCache.myself.getUserId(), InMemoryCache.myself.getNickName(), InMemoryCache.myself.getProfileImageUrl());
        SocketUtil.socket.on("room_status", new Listener() {
            public void call(Object... args) {
                final String unused = InMemoryCache.RANDOM_CHAT_ROOM_ID = args[0].toString();
                final String unused2 = InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID = args[1].toString();
                final String name = args[2] == null ? "Someone" : args[2].toString();
                final String profilePic = args[3] == null ? null : args[3].toString();
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        logo.setBackgroundResource(R.drawable.shape_online);
                        logo.setVisibility(View.VISIBLE);
                        MessageActivity.this.tvBarTitle.setText(name);
                    }
//
                });


            }
        });

    }

    private void registerReceiveMessageEvent() {
        SocketUtil.socket.on("receive_message", new Emitter.Listener() {
            public void call(final Object... args) {
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MessageActivity.this.mAdapter.addElementAndUpdate(new Message(1, args[0].toString()));
                    }
                });
            }
        });
    }

    private void listenTypingEvent() {
        SocketUtil.socket.on("listen_typing_start", new Emitter.Listener() {
            public void call(Object... args) {
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        logo.setVisibility(View.VISIBLE);
                        logo.setBackgroundResource(R.drawable.typing);
                        MessageActivity.this.tvBarTitle.setText(" Type...");


                    }
                });
            }
        });
        SocketUtil.socket.on("listen_typing_end", new Emitter.Listener() {
            public void call(Object... args) {
                MessageActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        logo.setBackgroundResource(R.drawable.shape_online);
                        logo.setVisibility(View.VISIBLE);
                        MessageActivity.this.tvBarTitle.setText("Online");


                    }
                });
            }
        });
    }

    private void registerChatEndEvent() {
        SocketUtil.socket.on("listen_chat_end", new Emitter.Listener() {
            public void call(Object... args) {
                if (!MessageActivity.this.isFinishing()) {
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            MessageActivity.this.alertDialogForEndChatByOtherUser();
                        }
                    });
                }
            }
        });
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
                    SocketUtil.socket.emit("trigger_typing_end", new Object[0]);
                    thread = null;
                }
            };
            private Thread thread;

            public void onTextChanged(CharSequence ss, int start, int before, int count) {
                SocketUtil.socket.emit("trigger_typing_start", new Object[0]);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MessageActivity.this.rvMessage.scrollToPosition(MessageActivity.this.mAdapter.getItemCount() - 1);
            }

            public void afterTextChanged(Editable ss) {
                if (this.thread == null) {
                    this.thread = new Thread(this.runnable_EditTextWatcher);
                    this.thread.start();
                }
            }
        });
    }

    private void registerImageEvent() {
        SocketUtil.socket.on("receive_image", new Listener() {
            public void call(Object... args) {
                final ImageData imageData = CustomJsonParser.getImageData(args[0].toString());
                if (imageData != null) {
                    MessageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            MessageActivity.this.mAdapter.addElementAndUpdate(new Message(3, imageData));
                        }
                    });
                }
            }
        });
    }

    private void endChat() {
        if (SocketUtil.socket != null && SocketUtil.socket.connected()) {
            SocketUtil.socket.emit("trigger_chat_end", new Object[0]);
        }
        InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID = null;
        finish();
    }

    private AlertDialog getDialogForImageUpload(String localUri, final int width, final int height, final Bitmap bitmap) {
        View view = getLayoutInflater().inflate(R.layout.dialog_image_send, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_send);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.dialog_layout);
        int h = (height * 550) / width;
        imageView.setImageBitmap(bitmap);
        Picasso.get().load(localUri).placeholder(imageView.getDrawable()).resize(550, h).centerCrop().into(imageView);
        Builder builder = new Builder(this);
        builder.setView(view);
        builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (InMemoryCache.RANDOM_CHAT_ROOM_ID != null) {
                    try {
                        Bitmap bitmapToUpload = MethodUtil.getResizedBitmap(bitmap);
                        final File file = MethodUtil.getFileFromBitmap(MessageActivity.this.getApplicationContext(), bitmapToUpload);
                        if (file != null) {
                            MessageActivity.this.addImageSentMessageAndUpdate(width, height, bitmapToUpload);
                            CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
                                /* class strangers.chat.activity.MessageActivity.AnonymousClass16.AnonymousClass1 */

                                public void run() {
                                    ImageData imageData = NewCustomOkHttp.uploadImage(file);
                                    if (imageData != null) {
                                        SocketUtil.socket.emit("send_image", MessageActivity.this.gson.toJson(imageData));
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == Constants.GALLERY_IMAGE) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    bitmap = BitmapFactory.decodeStream(new BufferedInputStream(getApplicationContext().getContentResolver().openInputStream(data.getData())));
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

    private Bitmap takeScreenshot(RelativeLayout layout) {
        View screenView = layout.getChildAt(0);
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void alertDialogForEndChatByMe() {
        Builder builder = new Builder(this);
        builder.setMessage("Are you sure you want to exit chat?");
        builder.setPositiveButton("Add as friend", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                StUser stUser = NewCustomOkHttp.addFriend(InMemoryCache.myself.getUserId(), InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID);
                if (stUser != null) {
                    NewUtil.addFriend(stUser, MethodUtil.getStMsgFromMsgs(MessageActivity.this.mAdapter.getMessages(), stUser), true);
                }
                MessageActivity.this.endChat();

            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    leaveChatAds = true;
                } else {
                    MessageActivity.this.endChat();

                }
            }
        });
        builder.setNeutralButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void alertDialogForEndChatByOtherUser() {

        Builder builder = new Builder(this);
        builder.setMessage("Chat ended by stranger, do you want to add as friend?");
        builder.setCancelable(false);
        builder.setPositiveButton("ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    adFriendEndChatPositiveAds = true;
                } else
                    MessageActivity.this.endChat();

            }
        });
//        builder.setPositiveButton("next stranger", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (interstitialAd.isLoaded())
//                    interstitialAd.show();
//                startChat();
//                mAdapter = new MessageAdapter(new ArrayList(), getApplicationContext(), rvMessage);
//                rvMessage.setAdapter(mAdapter);
//
//
//            }
//        });
        builder.setNegativeButton("add as friend", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    adFriendEndChatAdsNegativeAds = true;
                } else {
                    StUser stUser = NewCustomOkHttp.addFriend(InMemoryCache.myself.getUserId(), InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID);
                    if (stUser != null) {
                        NewUtil.addFriend(stUser, MethodUtil.getStMsgFromMsgs(MessageActivity.this.mAdapter.getMessages(), stUser), true);
                    }
                    MessageActivity.this.endChat();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        assert alertDialog != null;
        alertDialog.show();
    }

    public void onBackPressed() {
        if (InMemoryCache.RANDOM_CHAT_OTHER_PERSON_ID == null) {
            finish();
        } else {
            alertDialogForEndChatByMe();
        }
    }

    protected void onDestroy() {
        endChat();
        super.onDestroy();
    }

    private void addImageSentMessageAndUpdate(int width, int height, Bitmap bitmap) {
        try {
            Image image = new Image(FileProvider.getUriForFile(getApplicationContext(), Constants.FILE_PROVIDER_AUTHORITY, MethodUtil.saveBitmap(bitmap, 4, null)).toString(), width, height);
            ImageData imageData = new ImageData();
            imageData.setThumbnail(image);
            imageData.setOriginal(image);
            this.mAdapter.addElementAndUpdate(new Message(4, imageData));
        } catch (Exception e) {
        }
    }

    private void refreshConversationList(MessageAdapter messageAdapter, final LayoutManager layoutManager, final RecyclerView recyclerView) {
        CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
            public void run() {
                while (!MessageActivity.this.isFinishing()) {
                    try {
                        if (System.currentTimeMillis() % 1500 == 0) {
                            MessageActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    int position = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                                    if (position >= 0 && position < MessageActivity.this.mAdapter.getMessages().size()) {
                                        int msgType = ((Message) MessageActivity.this.mAdapter.getMessages().get(position)).getType();
                                        if (position != MessageActivity.this.mAdapter.getItemCount() - 2) {
                                            return;
                                        }
                                        if (msgType == 0 || msgType == 1) {
                                            recyclerView.scrollToPosition(MessageActivity.this.mAdapter.getItemCount() - 1);
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

    @Override
    public void onResponse(Call<profile_Model> call, Response<profile_Model> response) {
        if (response.body() != null && response.body().getResults() != null)
            Picasso.get().load(response.body().getResults().get(0).getPicture().getMedium()).transform(new BlurTransform(this)).into(profile_picture);
        profile_pic_Url = response.body().getResults().get(0).getPicture().getLarge();
    }

    @Override
    public void onFailure(Call<profile_Model> call, Throwable t) {
        t.printStackTrace();
    }
}
