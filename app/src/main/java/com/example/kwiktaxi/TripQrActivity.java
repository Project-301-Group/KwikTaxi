package com.example.kwiktaxi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Button;
import android.graphics.pdf.PdfDocument;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.content.ContentValues;
import android.net.Uri;
import java.io.OutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TripQrActivity extends AppCompatActivity {

    private Bitmap bmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_qr);

        String base64 = getIntent().getStringExtra("qr_base64");
        ImageView iv = findViewById(R.id.ivQr);
        Button btnDownloadPdf = findViewById(R.id.btnDownloadPdf);
        if (base64 != null) {
            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            iv.setImageBitmap(bmp);
        }

        btnDownloadPdf.setOnClickListener(v -> saveQrAsPdf());
    }

    private void saveQrAsPdf() {
        if (bmp == null) return;
        PdfDocument doc = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bmp.getWidth() + 40, bmp.getHeight() + 80, 1).create();
        PdfDocument.Page page = doc.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawColor(0xFFFFFFFF);
        canvas.drawBitmap(bmp, 20, 40, paint);
        doc.finishPage(page);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "trip_qr_" + System.currentTimeMillis() + ".pdf");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/");
        Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
        if (uri != null) {
            try (OutputStream os = getContentResolver().openOutputStream(uri)) {
                doc.writeTo(os);
            } catch (IOException e) {
                // ignore
            }
        }
        doc.close();
    }
}


