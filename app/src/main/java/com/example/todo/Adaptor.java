package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class Adaptor extends ArrayAdapter<User>{

    Context context;
    List<User> users;

    public Adaptor(Context context, List<User> users){
        super(context,R.layout.list_layout,users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_layout,null);

       TextView name = convertView.findViewById(R.id.taskName);
       TextView time = convertView.findViewById(R.id.taskTime);

       User user = users.get(position);

       name.setText(user.getTask());
       time.setText(user.getTime());

        return convertView;
    }
}
