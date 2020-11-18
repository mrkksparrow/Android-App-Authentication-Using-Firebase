package com.example.sparrow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity{

    private EditText xname, xnumber;
    private Button search, logout;
    private ListView xlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},PackageManager.PERMISSION_GRANTED);

        xname = (EditText) findViewById(R.id.name);
        xnumber = (EditText) findViewById(R.id.number);
        xlist = (ListView) findViewById(R.id.list);

        search = (Button) findViewById(R.id.get);
        logout = (Button) findViewById(R.id.logout);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE},"DISPLAY_NAME = '" + xname.getText().toString()+"'",null, null);
                    cursor.moveToFirst();
                    xnumber.setText(cursor.getString(0));
                }
                catch (Exception e){
                    e.printStackTrace();
                    xnumber.setText("Not Found");
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, MainActivity.class));
            }
        });

    }
    @Override
    protected void onStart() {
        get();
        super.onStart();
    }
    public void get(){
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null, null,null);
        startManagingCursor(cursor);

        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,cursor, from,to);
        xlist.setAdapter(simpleCursorAdapter);
        xlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    @Override
    public void onBackPressed() {

    }
}

/**/
