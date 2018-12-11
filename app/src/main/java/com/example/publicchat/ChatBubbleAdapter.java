package com.example.publicchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatBubbleAdapter extends ArrayAdapter<Message> {

    private Context mContext;
    private int mResource;

    public ChatBubbleAdapter(Context context, int resource, ArrayList<Message> list) {
        super(context, resource, list);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the information
        User user = getItem(position).getUser();
        String message = getItem(position).getText();
        Date time = getItem(position).getTime();

        Message chatBubbleInfo = new Message(message, user, time);

        if(ChatRoomActivity.getCurrentUser().getID().equals(user.getID()))
            mResource = R.layout.current_user_chat_bubble;
        else mResource = R.layout.other_users_chat_bubble;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String formatTime = formatter.format(time);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.username12);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.message12);
        TextView tvTime = (TextView) convertView.findViewById(R.id.time12);

//        System.out.println("Id of user is: " + getChatRoomUser().getID());
        tvUsername.setText(user.getUsername());
        tvMessage.setText(message);
        tvTime.setText(formatTime);

        return convertView;
    }
}