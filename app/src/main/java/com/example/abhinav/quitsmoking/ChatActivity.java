package com.example.abhinav.quitsmoking;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.abhinav.quitsmoking.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.format.DateFormat.format;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

   private EditText messageText;
   private ListView messageList;
   private Button send;
   private FirebaseListAdapter<ChatMessage> adapter;
    private User.UserResult user1;
   private int option_tag=0;
   private static boolean activityVisible;
    //uri to store file

    //firebase objects
    private DatabaseReference dbref;


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatActivity.activityPaused();
    }



    public void send(View v){

       /* if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals("Admin")){

            send_admin();
        }

        else{

            send_user();
        }

        */

       send_user(messageText.getText().toString());
       messageText.setText("");




    }

    public void send_user(String msg){

        if(!msg.startsWith(" ")&&!msg.isEmpty()) {

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Chats")
                    .push()
                    .setValue(new ChatMessage(msg, user1.getUsername()), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {

                                Log.i("event", "success");
                            } else {

                                Log.i("event", "failed");


                            }

                        }

                    });

        }




    }



    public void displayMessages(){

         dbref= FirebaseDatabase.getInstance().getReference();

         DatabaseReference myref;

        Query query;






        /*if(getIntent().getStringExtra("Uid")!=null) {


            myref = dbref.child("Chats").child(getIntent().getStringExtra("Uid"));

        }

        else{



            myref = dbref.child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());



        }

        */

        myref = dbref.child("Chats");







        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ChatActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.setTitle("Loading messages");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();





        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue()==null){

                    progressDoalog.dismiss();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error",databaseError.toString());

            }
        });

        query= myref.orderByChild("messageTime").limitToLast(20);





        adapter=new FirebaseListAdapter<ChatMessage>(ChatActivity.this,ChatMessage.class,R.layout.message,query) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void populateView(View v, final ChatMessage model, int position) {

                String user=model.getMessageUser();

                final TextView messagetext=(TextView)v.findViewById(R.id.message_text);
                TextView timeText=(TextView)v.findViewById(R.id.message_time);
                TextView messageUser=(TextView)v.findViewById(R.id.messageUser);
                ImageView image=(ImageView)v.findViewById(R.id.image);




                CardView cardView=(CardView)v.findViewById(R.id.cardView);


                RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)cardView.getLayoutParams();
                RelativeLayout.LayoutParams rparams=(RelativeLayout.LayoutParams)timeText.getLayoutParams();

                params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                rparams.removeRule(RelativeLayout.BELOW);




                if (user.equals(user1.getUsername())) {



                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


                    Log.i("user",user+" "+position);

                    messageUser.setTextColor(getResources().getColor(android.R.color.holo_green_light));






                }

                else{






                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);




                    Log.i("bot",user+" "+position);

                    messageUser.setTextColor(getResources().getColor(android.R.color.holo_blue_light));


                }

                cardView.setLayoutParams(params);

                    messagetext.setVisibility(View.VISIBLE);
                    messagetext.setText(model.getMessageText());



                    image.setVisibility(View.INVISIBLE);
                    rparams.addRule(RelativeLayout.BELOW,R.id.message_text);












                messageUser.setText(model.getMessageUser());
                timeText.setText(format("HH:mm",model.getMessageTime()));




            }

        };

        messageList.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // the first time you get here, hide the progress bar
                progressDoalog.dismiss();

            }



            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });











    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send=(Button)findViewById(R.id.sendButton);
        user1=User.UserResult.getUserResult();








        messageText=(EditText)findViewById(R.id.messageText);

        messageList=(ListView)findViewById(R.id.messageList);
        displayMessages();


    }







}
