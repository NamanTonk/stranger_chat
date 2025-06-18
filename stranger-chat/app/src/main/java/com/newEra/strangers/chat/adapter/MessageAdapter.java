package com.newEra.strangers.chat.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;
import java.util.List;
import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.activity.FullscreenActivity;
import com.newEra.strangers.chat.model.Image;
import com.newEra.strangers.chat.model.Keys;
import com.newEra.strangers.chat.model.Message;

public class MessageAdapter extends Adapter {
    private Context context;
    private List<Message> messages;
    private RecyclerView recyclerView;

    private class ImageReceivedHolder extends ViewHolder {
        private ImageView ivImage;
        private ImageView ivLoad;

        public ImageReceivedHolder(View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.image_message);
            this.ivLoad = (ImageView) view.findViewById(R.id.load_image);
        }
    }

    private class ImageSentHolder extends ViewHolder {
        private ImageView ivImage;

        public ImageSentHolder(View view) {
            super(view);
            this.ivImage =  view.findViewById(R.id.image_message);
        }
    }

    private class ReceivedMessageHolder extends ViewHolder {
        private TextView tvMsg;

        public ReceivedMessageHolder(View view) {
            super(view);
            this.tvMsg =  view.findViewById(R.id.text_message);
            tvMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clipData = ClipData.newPlainText(,messages.get(getLayoutPosition()).getText());
                   clipboardManager.setText(messages.get(getLayoutPosition()).getText());
                   Toast.makeText(v.getContext(),"Text Copy",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    private class SentMessageHolder extends ViewHolder {
        private TextView tvMsg;

        public SentMessageHolder(View view) {
            super(view);
            this.tvMsg =  view.findViewById(R.id.text_message);
            tvMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(messages.get(getLayoutPosition()).getText());
                    Toast.makeText(v.getContext(),"Text Copy",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    public MessageAdapter(List<Message> messages, Context context, RecyclerView recyclerView) {
        this.messages = messages;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new SentMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false));
            case 1:
                return new ReceivedMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false));
            case 3:
                return new ImageReceivedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_received, parent, false));
            case 4:
                return new ImageSentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_sent, parent, false));
            default:
                return null;
        }
    }

    public int getItemViewType(int position) {
        return ((Message) this.messages.get(position)).getType();
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image;
        final Message message = (Message) this.messages.get(position);
        if (message.getType() == 1) {
            ((ReceivedMessageHolder) holder).tvMsg.setText(message.getText());
        }
        if (message.getType() == 0) {
            ((SentMessageHolder) holder).tvMsg.setText(message.getText());
        }
        if (message.getType() == 4) {
            ImageSentHolder imageSentHolder = (ImageSentHolder) holder;
            image = message.getImage().getOriginal();
            Picasso.get().load(image.getUrl()).placeholder(imageSentHolder.ivImage.getDrawable()).resize(550, (image.getHeight() * 550) / image.getWidth()).centerCrop().into(imageSentHolder.ivImage);
            if (!message.isImageScrolled()) {
                this.recyclerView.scrollToPosition(getItemCount() - 1);
                message.setImageScrolled(true);
            }
            imageSentHolder.ivImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MessageAdapter.this.context, FullscreenActivity.class);
                    intent.putExtra(Keys.IMAGE_URL, message.getImage().getOriginal().getUrl());
                    intent.putExtra(Keys.WIDTH, message.getImage().getOriginal().getWidth());
                    intent.putExtra(Keys.HEIGHT, message.getImage().getOriginal().getHeight());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MessageAdapter.this.context.startActivity(intent);
                }
            });
        }
        if (message.getType() == 3) {
            final ImageReceivedHolder imageReceivedHolder = (ImageReceivedHolder) holder;
            if (message.isLoaded()) {
                image = message.getImage().getOriginal();
            } else {
                image = message.getImage().getThumbnail();
            }
            Picasso.get().load(image.getUrl()).placeholder(imageReceivedHolder.ivImage.getDrawable()).resize(550, (image.getHeight() * 550) / image.getWidth()).centerCrop().into(imageReceivedHolder.ivImage, new EmptyCallback() {
                public void onSuccess() {
                    super.onSuccess();
                    if (message.isLoaded()) {
                        imageReceivedHolder.ivLoad.setVisibility(View.GONE);
                    } else {
                        imageReceivedHolder.ivLoad.setVisibility(View.VISIBLE);
                    }
                    if (!message.isImageScrolled()) {
                        MessageAdapter.this.recyclerView.scrollToPosition(MessageAdapter.this.getItemCount() - 1);
                        message.setImageScrolled(true);
                    }
                }
            });
            imageReceivedHolder.ivLoad.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Picasso.get().load(message.getImage().getOriginal().getUrl()).placeholder(imageReceivedHolder.ivImage.getDrawable()).resize(550, (message.getImage().getOriginal().getHeight() * 550) / message.getImage().getOriginal().getWidth()).centerCrop().into(imageReceivedHolder.ivImage, new EmptyCallback() {
                        public void onSuccess() {
                            super.onSuccess();
                            imageReceivedHolder.ivLoad.setVisibility(View.GONE);
                            message.setLoaded(true);
                            if (!message.isImageScrolled()) {
                                MessageAdapter.this.recyclerView.scrollToPosition(MessageAdapter.this.getItemCount() - 1);
                                message.setImageScrolled(true);
                            }
                        }
                    });
                }
            });
            imageReceivedHolder.ivImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (message.isLoaded()) {
                        Intent intent = new Intent(MessageAdapter.this.context, FullscreenActivity.class);
                        intent.putExtra(Keys.IMAGE_URL, message.getImage().getOriginal().getUrl());
                        intent.putExtra(Keys.WIDTH, message.getImage().getOriginal().getWidth());
                        intent.putExtra(Keys.HEIGHT, message.getImage().getOriginal().getHeight());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MessageAdapter.this.context.startActivity(intent);
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.messages.size();
    }

    public void addElementAndUpdate(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public List<Message> getMessages() {
        return this.messages;
    }
}
