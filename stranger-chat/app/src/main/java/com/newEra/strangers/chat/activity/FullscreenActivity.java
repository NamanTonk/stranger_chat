package com.newEra.strangers.chat.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.newEra.strangers.chat.views.TouchImageView;
import com.squareup.picasso.Picasso;
import com.newEra.strangers.chat.R;
import com.newEra.strangers.chat.model.Keys;

public class FullscreenActivity extends AppCompatActivity {
    private TouchImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }
        this.imageView = findViewById(R.id.fullscreen_image);
        String url = getIntent().getStringExtra(Keys.IMAGE_URL);
        int width = getIntent().getIntExtra(Keys.WIDTH, 550);
        int height = getIntent().getIntExtra(Keys.HEIGHT, 550);
        if (url == null || url.length() < 1) {
            finish();
        }
        Picasso.get().load(Uri.parse(url)).into(this.imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
