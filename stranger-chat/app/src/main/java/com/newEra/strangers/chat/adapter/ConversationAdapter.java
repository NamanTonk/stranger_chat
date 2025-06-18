package com.newEra.strangers.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.newEra.strangers.chat.util.StaticControll;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.activity.FriendsMessageActivity;
import com.newEra.strangers.chat.database.DbHandlerInstance;
import com.newEra.strangers.chat.database.model.StMessage;
import com.newEra.strangers.chat.model.StConversation;
import com.newEra.strangers.chat.util.IntentExtrasKeys;
import com.newEra.strangers.chat.util.MethodUtil;

public class ConversationAdapter extends Adapter<ConversationAdapter.ConversationHolder> {
    private Context context;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;
    private List<StConversation> stConversationList;

    public class ConversationHolder extends ViewHolder {
        private Button btnOnlineStatus;
        private ImageView ivProfileImage;
        private TextView tvLastMsg;
        private TextView tvLastMsgAt;
        private TextView tvName;

        public ConversationHolder(View itemView) {
            super(itemView);
            this.ivProfileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            this.tvName = (TextView) itemView.findViewById(R.id.name);
            this.tvLastMsg = (TextView) itemView.findViewById(R.id.last_message);
            this.btnOnlineStatus = (Button) itemView.findViewById(R.id.online_status);
            this.tvLastMsgAt = (TextView) itemView.findViewById(R.id.last_msg_at);
        }
    }

    public ConversationAdapter(List<StConversation> stConversationList, Context context) {
        this.stConversationList = stConversationList;
        this.context = context;
        interstitialAd = new InterstitialAd(context);
        adRequest = com.newEra.strangers.chat.util.AdRequest.getRequest();
        interstitialAd.setAdUnitId("ca-app-pub-3745255674091870/4406687362");
        interstitialAd.loadAd(adRequest);
    }

    @NonNull
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false));
    }

    public void onBindViewHolder(@NonNull final ConversationHolder holder, int position) {
        final StConversation stConversation = (StConversation) this.stConversationList.get(position);
        String msg = "";
        if (stConversation.getStMessageList().size() > 0) {
            StMessage lastMsg = (StMessage) stConversation.getStMessageList().get(stConversation.getStMessageList().size() - 1);
            if (lastMsg.getMsgType() == 4 || lastMsg.getMsgType() == 3) {
                msg = "IMAGE";
            } else {
                msg = ((StMessage) stConversation.getStMessageList().get(stConversation.getStMessageList().size() - 1)).getMsg();
            }
        }
        //http://api.makechums.com/image/strangers/profiles/omp6bipw2gl1.jpg
        //content://com.newEra.strangers.chat.file_provider/strangers/Strangers/Profile/10z3hetp7aa0agykeip60q32.png
        int noOfUnseenMsgs = MethodUtil.getNoOfUnseenMsgs(stConversation.getStMessageList());
        Picasso.get().load(stConversation.getStUser().getProfileImageUrl()).placeholder(holder.ivProfileImage.getDrawable()).into(holder.ivProfileImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                try {
                    DbHandlerInstance.dbHandler.deleteProfileImageById(stConversation.getStUser().getUserId());
                } catch (Exception e2) {
                }
            }
        });
        holder.tvName.setText(stConversation.getStUser().getNickName());
        if (msg.length() >= 35) {
            msg = msg.substring(0, 35) + "...";
        }
        holder.tvLastMsg.setText(msg);
        String lastMsgAt = "";
        if (stConversation.getStMessageList().size() > 0) {
            lastMsgAt = MethodUtil.getLastMsgTimeStr(((StMessage) stConversation.getStMessageList().get(stConversation.getStMessageList().size() - 1)).getMsgTime());
        }
        holder.tvLastMsgAt.setText(lastMsgAt);
        if (noOfUnseenMsgs > 0) {
            holder.tvLastMsg.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvLastMsg.setTypeface(null, Typeface.NORMAL);
        }
        if (stConversation.isOnline()) {
            holder.btnOnlineStatus.setVisibility(View.VISIBLE);
        } else {
            holder.btnOnlineStatus.setVisibility(View.GONE);
        }

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AdRequest();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("Naman",i+"");
            }
        });
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (interstitialAd.isLoaded() && new StaticControll().getInstance().getCurrentMonth() >= 3)
                    interstitialAd.show();
                Intent intent = new Intent(v.getContext(), FriendsMessageActivity.class);
                intent.putExtra(IntentExtrasKeys.USER_ID, stConversation.getStUser().getUserId());
                v.getContext().startActivity(intent);

            }
        });

    }

    public void AdRequest() {
        interstitialAd.loadAd(adRequest);
    }

    public int getItemCount() {
        return this.stConversationList.size();
    }
}
