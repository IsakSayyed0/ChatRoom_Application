package com.isaksayyed.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {


    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    FirebaseAuth auth;
    DatabaseReference reference;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        setupDisplayname();

        //Firebase
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);


        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMessage();
                return true;
            }
        });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }



    public void setupDisplayname(){

        SharedPreferences preferences = getSharedPreferences(RegisterActivity.CHAT_PREFS,MODE_PRIVATE);
        mDisplayName = preferences.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
        if (mDisplayName == null) mDisplayName="Anonymous";

    }

    private void sendMessage() {

        Toast.makeText(this, "Sending Chat", Toast.LENGTH_LONG).show();


        String input = mInputText.getText().toString();
        if (!input.equals("")){
            InstantMessage chat = new InstantMessage(input,mDisplayName);
            reference.child("messages").push().setValue(chat);
            mInputText.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatListAdapter = new ChatListAdapter(this,reference,mDisplayName);
        mChatListView.setAdapter(chatListAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        chatListAdapter.cleanUp();
    }

}
