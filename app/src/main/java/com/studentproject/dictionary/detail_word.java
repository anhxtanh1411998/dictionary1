package com.studentproject.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class detail_word extends AppCompatActivity {
    private TextView detailWord;
    private TextView detailContent;
    private TextView detailLink;
    private WebView detailWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_word);
        Init();
        Intent intent = getIntent();
        detailWord.setText(intent.getStringExtra("WORD"));
        detailContent.setText(intent.getStringExtra("CONTENT"));
        String link = intent.getStringExtra("LINK");
        SpannableString content = new SpannableString( link);
        content.setSpan(new UnderlineSpan(), 0,  link.length(), 0);
        detailLink.setText(content);
        detailLink.setHighlightColor(getResources().getColor(R.color.colorAccent));
        //detailLink.setText(intent.getStringExtra("LINK"));
        detailWeb.setWebViewClient(new MyWebViewClient(detailLink));
        detailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUrl();
            }
        });

    }

    private void goUrl() {
        String url = detailLink.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter url", Toast.LENGTH_SHORT).show();
            return;
        }
        detailWeb.getSettings().setLoadsImagesAutomatically(true);
        detailWeb.getSettings().setJavaScriptEnabled(true);
        detailWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        detailWeb.loadUrl(url);
    }


    private void showStaticContent() {
        String staticContent = "<h2>Select web page</h2>"
                + "<ul><li><a href='http://eclipse.org'>Eclipse</a></li>"
                + "<li><a href='http://google.com'>Google</a></li></ul>";
        detailWeb.loadData(staticContent, "text/html", "UTF-8");
    }

    private void Init() {
        detailWord = findViewById(R.id.detail_word);
        detailContent = findViewById(R.id.detail_content);
        detailLink = findViewById(R.id.detail_link);
        detailWeb = findViewById(R.id.web_display);
        detailLink.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
