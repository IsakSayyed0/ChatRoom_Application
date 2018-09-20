package com.isaksayyed.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mactivity;
    private DatabaseReference mDatabaseReference;
    private String displayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public ChatListAdapter(Activity activity, DatabaseReference ref,String name){
        mactivity = activity;
        displayName = name;
        mDatabaseReference = ref.child("messages");
        mDatabaseReference.addChildEventListener(childEventListener);
        mSnapshotList = new ArrayList<>();
    }

    static class viewHolder{

        TextView author;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot = mSnapshotList.get(i);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater inflater= (LayoutInflater) mactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row,viewGroup,false);
            final viewHolder holder = new viewHolder();
            holder.author = view.findViewById(R.id.author);
            holder.body = view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.author.getLayoutParams();
            view.setTag(holder);
        }

        final InstantMessage message =getItem(i);
        final viewHolder holder = (viewHolder) view.getTag();

        boolean isMe = message.getAuthor().equals(displayName);
        setChatRowAppearance(isMe,holder);

        String author = message.getAuthor();
        holder.author.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);
        return view;
    }

    private void setChatRowAppearance(boolean isItMe,viewHolder holder){

        if (isItMe){
            holder.params.gravity = Gravity.END;
            holder.author.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }
        else {
            holder.params.gravity = Gravity.START;
            holder.author.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.author.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanUp(){

        mDatabaseReference.removeEventListener(childEventListener);

    }
}
