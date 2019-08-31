package com.example.todo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class AddData extends AppCompatActivity {
    EditText editText;
    Button button;
    String task,time;
    String id;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        editText = findViewById(R.id.additem);
        button = findViewById(R.id.addBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 task = editText.getText().toString();

                if(!TextUtils.isEmpty(task)){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("Tasks");

                     id = databaseReference.push().getKey();

                    long currenttime = System.currentTimeMillis();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    time = simpleDateFormat.format(currenttime);

                    // add data to the DB
                    User user = new User();
                    user.setId(id);
                    user.setTask(task);
                    user.setTime(time);
                    databaseReference.child(id).setValue(user);

                    //Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_LONG).show();

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Intent intent = new Intent(AddData.this,MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(AddData.this, 0, intent, 0);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification mNotification = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mNotification = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Todo App")
                                .setContentText(user.getTask()  + " added in database")
                                .setSmallIcon(R.drawable.logo)
                                .setSound(soundUri)
                                .setContentIntent(pIntent)
                                .setVibrate(new long[]{ 1000, 1000})
                                .build();
                    }

                    mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(0, mNotification);

                      editText.setText("");
                }else{
                    editText.setError("Task Required");
                    //Toast.makeText(getApplicationContext(),"Please,Enter Task",Toast.LENGTH_LONG).show();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

    }

    public void back(View view){
        Intent intent = new Intent(AddData.this,MainActivity.class);
        startActivity(intent);
    }
}
