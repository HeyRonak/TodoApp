package com.example.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>{

    List<User> userList;
    int position;
    public RecyclerViewAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout,viewGroup,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
          position = i;
         final User user = userList.get(i);
         myHolder.task.setText(user.getTask());
         myHolder.time.setText(user.getTime());

         myHolder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(final View view) {
                   final String id = user.getId();
                 AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                 builder.setTitle("Delete");
                 builder.setMessage("This task is removed permenently");
                 builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
                         databaseReference.removeValue();
                         Toast.makeText(view.getContext(),"Data Removed Successfully",Toast.LENGTH_SHORT).show();
                     }
                 });
                 builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                     }
                 });
                  AlertDialog alertDialog = builder.create();
                  alertDialog.show();
             }
         });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView task,time;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.taskName);
            time = itemView.findViewById(R.id.taskTime);
        }
    }
}
