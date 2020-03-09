package com.studentproject.dictionary;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity {
    private ListView listView;
    ImageButton VoiceSearch;
    private static final int REQUEST_CODE = 1000;
    CustomAdapter adapter;
    boolean isloading = false;
    DatabaseOpenHelper DB;
    View loading;
    MyHander hander;
    AutoCompleteTextView SearchItem;
    ArrayList<translate> listVocabulary = new ArrayList<>();
    List<String> listWord = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Toggle();
        setupDrawerContent(navigationView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_EngVn:
                DB = new DatabaseOpenHelper(MainActivity.this, "anh_viet", "anh_viet.db");
                listVocabulary = DB.getListmore();
                SearchItem.setText("");
                showListView();
                loadListView();
                callButtonVoice();
                Search();
                selectItemListViewEn_Vn();
                break;
            case R.id.nav_EngEng:
                Log.w("ket qua", "10");
                break;
            case R.id.nav_VnEng:
                DB = new DatabaseOpenHelper(MainActivity.this, "viet_anh", "viet_anh.db");
                listVocabulary = DB.getListmore();
                SearchItem.setText("");
                showListView();
                loadListView();
                callButtonVoice();
                Search();
                selectItemListViewVn_En();
                break;
            case R.id.nav_Favorites:
                Log.w("ket qua", "3");
                break;
            case R.id.nav_your_word:
                Log.w("ket qua", "4");
                break;
            case R.id.nav_share:
                Log.w("ket qua", "5");
                break;
            case R.id.nav_help:
                Log.w("ket qua", "6");
                break;
            default:
                Log.w("ket qua", "7");
                break;

        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    public void callButtonVoice() {
        VoiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchItem.setText("");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
                try {
                    startActivityForResult(intent, REQUEST_CODE);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                String Query = matches.get(0);
                SearchItem.setText(Query);
                // speak.setEnabled(false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Init() {
        listView = findViewById(R.id.listView);
        SearchItem = findViewById(R.id.sv_search);
        VoiceSearch = findViewById(R.id.bt_voice);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_myword);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nvView);
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void Toggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void Search() {
        SearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listWord = DB.getWord(s.toString());
                showAutoCompleteTextView(listWord);
            }

            @Override
            public void afterTextChanged(Editable s) {
                listVocabulary = DB.Search(s.toString());
                showListView();
            }
        });
    }

    public void showAutoCompleteTextView(List<String> list) {
        ArrayAdapter<String> adapterWord = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        SearchItem.setAdapter(adapterWord);
        SearchItem.setThreshold(1);
    }

    public void loadListView() {
        hander = new MyHander();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        loading = inflater.inflate(R.layout.load_view, null);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && isloading == false) {
                    listVocabulary = DB.getListmore();
                    Log.w("hello", "ga");
                    isloading = true;
                    Thread thread = new myThread();
                    thread.start();
                }
            }

        });

    }

    public void showListView() {
        adapter = new CustomAdapter(this, R.layout.display_iteam_listview, listVocabulary);
        listView.setAdapter(adapter);
    }

    private void selectItemListViewEn_Vn() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                translate translate = adapter.getItem(position);
                Log.w("Kết quả:", (position) + translate.getWord());
                Intent intent = new Intent(MainActivity.this, detail_word.class);
                intent.putExtra("WORD", translate.getWord());
                intent.putExtra("CONTENT", translate.getContent());
                String word = translate.getWord().replaceAll("\"", "");
                intent.putExtra("LINK", "http://tratu.soha.vn/dict/en_vn/" + word);
                startActivity(intent);
            }
        });

    }

    private void selectItemListViewVn_En() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                translate translate = adapter.getItem(position);
                Log.w("Kết quả:", (position) + translate.getWord());
                Intent intent = new Intent(MainActivity.this, detail_word.class);
                intent.putExtra("WORD", translate.getWord());
                intent.putExtra("CONTENT", translate.getContent());
                String word = translate.getWord().replaceAll("\"", "");
                intent.putExtra("LINK", "http://tratu.soha.vn/dict/vn_en/" + word);
                startActivity(intent);
            }
        });
    }

    public class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.w("ket qua", "100");
            switch (msg.what) {
                case 0:
                    listView.addFooterView(loading);
                    break;
                case 1:
                    adapter.addItemCustomAdapter((ArrayList<translate>) msg.obj);
                    listView.removeFooterView(loading);
                    isloading = false;
                    break;
            }
        }
    }

    public class myThread extends Thread {
        @Override
        public void run() {
            hander.sendEmptyMessage(0);
            ArrayList<translate> arrayList = listVocabulary;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = hander.obtainMessage(1, arrayList);
            hander.sendMessage(message);
        }

    }
}
