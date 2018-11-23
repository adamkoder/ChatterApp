package com.example.publicchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatBubbleAdapter extends ArrayAdapter<ChatBubbleInfo> {

    private Context mContext;
    private int mResource;

    public ChatBubbleAdapter(Context context, int resource, ArrayList<ChatBubbleInfo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the information
        String username = getItem(position).getUsername();
        String message = getItem(position).getMessage();

        ChatBubbleInfo chatBubbleInfo = new ChatBubbleInfo(username, message);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvUsername = (TextView) convertView.findViewById(R.id.username12);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.message12);

        tvUsername.setText(username);
        tvMessage.setText(message);

        return convertView;
    }
}
