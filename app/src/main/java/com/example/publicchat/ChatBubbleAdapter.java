package com.example.publicchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatBubbleAdapter extends RecyclerView.Adapter<ChatBubbleAdapter.ViewHolder> {

    private ArrayList<Message> chat;
    private Message m;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText, time, username;

        public ViewHolder(View view){
            super(view);
            messageText = (TextView) view.findViewById(R.id.message12);
            time = (TextView) view.findViewById(R.id.time12);
            username = (TextView) view.findViewById(R.id.username12);
        }
    }

    public ChatBubbleAdapter(ArrayList<Message> chat){
        this.chat = chat;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position){
        m = chat.get(position);
        holder.messageText.setText(m.getText());

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String formattedTime = formatter.format(m.getTime());
        holder.time.setText(formattedTime);


        if(!CurrentUser.getInstance().getUsername().equals(m.getUser()))
            holder.username.setText(m.getUser());
    }

    @Override
    public int getItemCount(){
        return chat.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        if(viewType == 0) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_users_chat_bubble, parent, false);
            return new ViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.current_user_chat_bubble, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(CurrentUser.getInstance().getUsername().equals(chat.get(position).getUser()))
            return 1;
        else return 0;
    }
}