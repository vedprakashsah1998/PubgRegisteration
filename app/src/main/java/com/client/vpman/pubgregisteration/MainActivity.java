package com.client.vpman.pubgregisteration;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.firebase.client.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    Firebase firebase;


    DatabaseReference databaseReference;
    public static final String Database_Path = "Tournament_detail";
    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";

    private DemoFragmentStateAdapter adapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.pager);
        adapter = new DemoFragmentStateAdapter(getSupportFragmentManager());
        BottomNavigationView bottomNav = findViewById(R.id.bottombar);
        mViewPager.setAdapter(adapter);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.create:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.tournament:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.register:
                        mViewPager.setCurrentItem(2);
                        break;
                   /* case R.id.bot_photographers:
                        mViewPager.setCurrentItem(3);
                        break;*/
                   /* case R.id.bot_painters:
                        mViewPager.setCurrentItem(4);
                        break;*/
                    default:
                        mViewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });




        Firebase.setAndroidContext(MainActivity.this);



        firebase = new Firebase(Firebase_Server_URL);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

    }
}
