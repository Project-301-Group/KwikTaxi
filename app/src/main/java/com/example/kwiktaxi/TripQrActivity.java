package com.example.kwiktaxi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TripQrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_qr);

        String base64 = getIntent().getStringExtra("qr_base64");
        ImageView iv = findViewById(R.id.ivQr);
        if (base64 != null) {
            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            iv.setImageBitmap(bmp);
        }
    }
}


