package com.newEra.strangers.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newEra.strangers.chat.memory_cache.InMemoryCache;
import com.newEra.strangers.chat.model.PendingMessageStatus;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.util.List;
import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.activity.FullscreenActivity;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.MessageStatus;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.Keys;
import com.newEra.strangers.chat.socket.AllSocketEvents;
import com.newEra.strangers.chat.thread_util.CustomThreadPoolInstance;
import com.newEra.strangers.chat.util.Constants;
import com.newEra.strangers.chat.util.MethodUtil;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class FriendMessageAdapter extends Adapter {
    private Activity activity;
    private List<StMessage> stMessageList;

    private class ImageReceivedHolder extends ViewHolder {
        private ImageView ivImage;
        private ImageView ivLoad;
        private TextView tvMsgTime;

        public ImageReceivedHolder(View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.image_message);
            this.ivLoad = (ImageView) view.findViewById(R.id.load_image);
            this.tvMsgTime = (TextView) view.findViewById(R.id.msg_time);
        }
    }

    private class ImageSentHolder extends ViewHolder {
        private ImageView ivDelivered;
        private ImageView ivImage;
        private ImageView ivSeen;
        private ImageView ivSent;
        private TextView tvMsgTime;

        public ImageSentHolder(View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.image_message);
            this.ivSent = (ImageView) view.findViewById(R.id.sent);
            this.ivDelivered = (ImageView) view.findViewById(R.id.delivered);
            this.ivSeen = (ImageView) view.findViewById(R.id.seen);
            this.tvMsgTime = (TextView) view.findViewById(R.id.msg_time);
        }
    }

    private class ReceivedMessageHolder extends ViewHolder {
        private EmojiconTextView tvMsg;
        private TextView tvMsgTime;

        public ReceivedMessageHolder(View view) {
            super(view);
            this.tvMsg =  view.findViewById(R.id.text_message);
            this.tvMsgTime = (TextView) view.findViewById(R.id.msg_time);
        }
    }

    private class SentMessageHolder extends ViewHolder {
        private ImageView ivDelivered;
        private ImageView ivSeen;
        private ImageView ivSent;
        private TextView tvMsg;
        private TextView tvMsgTime;

        public SentMessageHolder(View view) {
            super(view);
            this.tvMsg = (TextView) view.findViewById(R.id.text_message);
            this.ivSent = (ImageView) view.findViewById(R.id.sent);
            this.ivDelivered = (ImageView) view.findViewById(R.id.delivered);
            this.ivSeen = (ImageView) view.findViewById(R.id.seen);
            this.tvMsgTime = (TextView) view.findViewById(R.id.msg_time);
        }
    }

    public FriendMessageAdapter(List<StMessage> stMessageList, Activity activity) {
        this.stMessageList = stMessageList;
        this.activity = activity;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new SentMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_msg_sent, parent, false));
            case 1:
                return new ReceivedMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_msg_received, parent, false));
            case 3:
                return new ImageReceivedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_img_received, parent, false));
            case 4:
                return new ImageSentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_img_sent, parent, false));
            default:
                return null;
        }
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { final StMessage stMessage = this.stMessageList.get(position);
        if (stMessage.getMsgType() == 1 && stMessage.getStatus().equals(MessageStatus.RECEIVED)) {
            stMessage.setStatus(MessageStatus.SEEN);
            DbHandlerInstance.dbHandler.updateMessageStatus(stMessage.getMsgId(), stMessage.getStatus());
            PendingMessageStatus pendingMessageStatus = new PendingMessageStatus();
            pendingMessageStatus.setMessageId(stMessage.getMsgId());
            pendingMessageStatus.setSenderId(InMemoryCache.myself.getUserId());
            pendingMessageStatus.setReceiverId(stMessage.getUserId());
            pendingMessageStatus.setStatus(MessageStatus.SEEN);
            AllSocketEvents.triggerFriendMessageStatus(pendingMessageStatus);
        }
        try {
            if (stMessage.getMsgType() == 0) {
                SentMessageHolder sentMessageHolder = (SentMessageHolder) holder;
                sentMessageHolder.tvMsg.setText(stMessage.getMsg());
                setMsgStatus(sentMessageHolder.ivSent, sentMessageHolder.ivDelivered, sentMessageHolder.ivSeen, stMessage.getStatus());
                sentMessageHolder.tvMsgTime.setText(MethodUtil.getMsgTimeString(stMessage.getMsgTime()));
            }
            if (stMessage.getMsgType() == 1) {
                ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
                receivedMessageHolder.tvMsg.setText(stMessage.getMsg());
                receivedMessageHolder.tvMsgTime.setText(MethodUtil.getMsgTimeString(stMessage.getMsgTime()));
            }
            if (stMessage.getMsgType() == 4) {
                ImageSentHolder imageSentHolder = (ImageSentHolder) holder;
                Image image = stMessage.getLocalImage();
                Picasso.get().load(image.getUrl()).placeholder(imageSentHolder.ivImage.getDrawable()).resize(550, (image.getHeight() * 550) / image.getWidth()).centerCrop().into(imageSentHolder.ivImage, new Callback() {
                    /* class strangers.chat.adapter.FriendMessageAdapter.AnonymousClass1 */

                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        DbHandlerInstance.dbHandler.deleteMessagesByMsgId(stMessage.getMsgId());
                        FriendMessageAdapter.this.stMessageList.remove(stMessage);
                    }
                });
                setMsgStatus(imageSentHolder.ivSent, imageSentHolder.ivDelivered, imageSentHolder.ivSeen, stMessage.getStatus());
                imageSentHolder.tvMsgTime.setText(MethodUtil.getMsgTimeString(stMessage.getMsgTime()));
                imageSentHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                    /* class strangers.chat.adapter.FriendMessageAdapter.AnonymousClass2 */

                    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
                     method: ClspMth{android.content.Intent.putExtra(java.lang.String, boolean):android.content.Intent}
                     arg types: [java.lang.String, int]
                     candidates:
                      ClspMth{android.content.Intent.putExtra(java.lang.String, int):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.String[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, int[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, double):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, char):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, boolean[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, byte):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, android.os.Bundle):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, float):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.CharSequence[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.CharSequence):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, long[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, long):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, short):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, android.os.Parcelable[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, java.io.Serializable):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, double[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, android.os.Parcelable):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, float[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, byte[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, java.lang.String):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, short[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, char[]):android.content.Intent}
                      ClspMth{android.content.Intent.putExtra(java.lang.String, boolean):android.content.Intent} */
                    @SuppressLint("WrongConstant")
                    public void onClick(View v) {
                        String imagePath = stMessage.getLocalImage().getUrl();
                        if (imagePath.length() > 0) {
                            Intent intent = new Intent(FriendMessageAdapter.this.activity, FullscreenActivity.class);
                            intent.putExtra(Keys.IMAGE_URL, imagePath);
                            intent.putExtra("width", stMessage.getLocalImage().getWidth());
                            intent.putExtra("height", stMessage.getLocalImage().getHeight());
                            intent.putExtra(Keys.IS_LOCAL, true);
                            intent.addFlags(268435456);
                            FriendMessageAdapter.this.activity.startActivity(intent);
                        }
                    }
                });
            }
            if (stMessage.getMsgType() == 3) {
                final ImageReceivedHolder imageReceivedHolder = (ImageReceivedHolder) holder;
                Image image2 = stMessage.getLocalImage();
                if (image2.getUrl().length() > 0) {
                    imageReceivedHolder.ivLoad.setVisibility(View.GONE);
                    Picasso.get().load(image2.getUrl()).placeholder(imageReceivedHolder.ivImage.getDrawable()).resize(550, (image2.getHeight() * 550) / image2.getWidth()).centerCrop().into(imageReceivedHolder.ivImage, new Callback() {
                        /* class strangers.chat.adapter.FriendMessageAdapter.AnonymousClass3 */

                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            DbHandlerInstance.dbHandler.deleteMessagesByMsgId(stMessage.getMsgId());
                            FriendMessageAdapter.this.stMessageList.remove(stMessage);
                        }
                    });
                } else {
                    if (stMessage.isImageLoding()) {
                        imageReceivedHolder.ivLoad.setVisibility(View.GONE);
                    } else {
                        imageReceivedHolder.ivLoad.setVisibility(View.VISIBLE);
                    }
                    Image image3 = stMessage.getThumbnailUrl();
                    Picasso.get().load(image3.getUrl()).placeholder(imageReceivedHolder.ivImage.getDrawable()).resize(550, (image3.getHeight() * 550) / image3.getWidth()).centerCrop().into(imageReceivedHolder.ivImage);
                }
                imageReceivedHolder.tvMsgTime.setText(MethodUtil.getMsgTimeString(stMessage.getMsgTime()));
                imageReceivedHolder.ivLoad.setOnClickListener(new View.OnClickListener() {
                    /* class strangers.chat.adapter.FriendMessageAdapter.AnonymousClass4 */

                    public void onClick(View v) {
                        FriendMessageAdapter.this.updateImageView(v, imageReceivedHolder, stMessage);
                    }
                });
                imageReceivedHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                    /* class strangers.chat.adapter.FriendMessageAdapter.AnonymousClass5 */

                    @SuppressLint("WrongConstant")
                    public void onClick(View v) {
                        String imagePath = stMessage.getLocalImage().getUrl();
                        if (imagePath.length() > 0) {
                            Intent intent = new Intent(FriendMessageAdapter.this.activity, FullscreenActivity.class);
                            intent.putExtra(Keys.IMAGE_URL, imagePath);
                            intent.putExtra("width", stMessage.getLocalImage().getWidth());
                            intent.putExtra("height", stMessage.getLocalImage().getHeight());
                            intent.addFlags(268435456);
                            FriendMessageAdapter.this.activity.startActivity(intent);
                            return;
                        }
                        FriendMessageAdapter.this.updateImageView(v, imageReceivedHolder, stMessage);
                    }
                });
            }
        } catch (Exception e) {
        }

    }
    public void updateImageView(final View v, ImageReceivedHolder imageReceivedHolder, final StMessage stMessage) {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            imageReceivedHolder.ivLoad.setVisibility(View.GONE);
            stMessage.setImageLoding(true);
            CustomThreadPoolInstance.threadPoolExecutor.submitTaskAndLeave(new Runnable() {
                /* class strangers.chat.adapter.FriendMessageAdapter.AnonymousClass6 */

                public void run() {
                    try {
                        String localUriStr = FileProvider.getUriForFile(v.getContext(), Constants.FILE_PROVIDER_AUTHORITY, MethodUtil.saveBitmap(BitmapFactory.decodeStream(new URL(stMessage.getOriginalUrl().getUrl()).openConnection().getInputStream()), 3, stMessage.getMsgId() + ".png")).toString();
                        if (DbHandlerInstance.dbHandler.updateLocalImagePath(stMessage.getMsgId(), localUriStr)) {
                            stMessage.getLocalImage().setUrl(localUriStr);
                            stMessage.getLocalImage().setHeight(stMessage.getOriginalUrl().getHeight());
                            stMessage.getLocalImage().setWidth(stMessage.getOriginalUrl().getWidth());
                            stMessage.setStatus(MessageStatus.SEEN);
                            DbHandlerInstance.dbHandler.updateMessageStatus(stMessage.getMsgId(), stMessage.getStatus());
                            PendingMessageStatus pendingMessageStatus = new PendingMessageStatus();
                            pendingMessageStatus.setMessageId(stMessage.getMsgId());
                            pendingMessageStatus.setSenderId(InMemoryCache.myself.getUserId());
                            pendingMessageStatus.setReceiverId(stMessage.getUserId());
                            pendingMessageStatus.setStatus(MessageStatus.SEEN);
                            AllSocketEvents.triggerFriendMessageStatus(pendingMessageStatus);
                        }
                    } catch (Exception e) {
                        stMessage.setImageLoding(false);
                    }
                }
            });
            return;
        }
        ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }


    public int getItemCount() {
        return this.stMessageList.size();
    }

    public List<StMessage> getStMessageList() {
        return this.stMessageList;
    }

    public int getItemViewType(int position) {
        return ((StMessage) this.stMessageList.get(position)).getMsgType();
    }

    private void setMsgStatus(ImageView ivSent, ImageView ivDelivered, ImageView ivSeen, String msgStatus) {
        if (msgStatus.equals(MessageStatus.SENT)) {
            ivSent.setVisibility(View.VISIBLE);
            ivDelivered.setVisibility(View.GONE);
            ivSeen.setVisibility(View.GONE);
        } else if (msgStatus.equals(MessageStatus.DELIVERED)) {
            ivSent.setVisibility(View.GONE);
            ivDelivered.setVisibility(View.VISIBLE);
            ivSeen.setVisibility(View.GONE);
        } else if (msgStatus.equals(MessageStatus.SEEN)) {
            ivSent.setVisibility(View.GONE);
            ivDelivered.setVisibility(View.GONE);
            ivSeen.setVisibility(View.VISIBLE);
        } else {
            ivSent.setVisibility(View.GONE);
            ivDelivered.setVisibility(View.GONE);
            ivSeen.setVisibility(View.GONE);
        }
    }
}
