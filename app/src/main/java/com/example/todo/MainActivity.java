package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;
    List<User> userList;
    TextView day,time;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Looks like you're offline!")
                    .setMessage("Please,Connect to the internet")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        }


        userList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        recyclerView = findViewById(R.id.recycler);
//        listView = findViewById(R.id.Listview);

        day = findViewById(R.id.day);
        time = findViewById(R.id.time);

        long t =  System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        String Newday = dateFormat.format(t);
        String NewTime = simpleDateFormat.format(t);
        day.setText(Newday);
        time.setText(NewTime);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                   User user = snapshot.getValue(User.class);
//                    String id = user.getTask();
//                    String log = "my message";
//                    Log.d(log,id + "");
                    userList.add(user);
                }

                 // code for recycler view adaptor
//                Adaptor adaptor = new Adaptor(getApplicationContext(),userList);
//                listView.setAdapter(adaptor);
                 RecyclerViewAdapter adapter = new RecyclerViewAdapter(userList);
                 RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                 recyclerView.setLayoutManager(layoutManager);
                 recyclerView.setItemAnimator(new DefaultItemAnimator());
                 recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), (CharSequence) databaseError.toException(),Toast.LENGTH_LONG).show();
            }
        });



//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Delete");
//                builder.setMessage("This task is removed permenently");
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String t = userList.get(position).getTask();
//                        String id = userList.get(position).getId();
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
//    //                  String key = databaseReference.getKey();
//    //                  String tag = "this is a key";
//    //                  Log.d(tag,key);
//                            databaseReference.removeValue();
//    //                 Toast.makeText(getApplicationContext(),t,Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                final AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.additem) {
            Intent intent = new Intent(MainActivity.this,AddData.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
