package com.antariksh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.antariksh.helper.customAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoDisplay extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    ArrayList<String>url,name;
    private DatabaseReference dbRef;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    public  String YOUTUBE_API_KEY = "AIzaSyAydKjPqb2sKwfCTY7Trf-dz0gF9i3NcCs";
    public static  String VIDEO_ID = "";
    public  static  YouTubePlayer yplayer;
    private ListView lv;
    //public String videourl[]={"bzSTpdcs-EI","lpdRqn6xwiM"};
    //public String videoDetail[]={"Channa Mereya...","Aye dil hai muskil..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        dbRef= FirebaseDatabase.getInstance().getReference().child("Video");
        url=new ArrayList<>();
        name=new ArrayList<>();
        lv= (ListView) findViewById(R.id.list);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VIDEO_ID = url.get(position);
                yplayer.cueVideo(VIDEO_ID);

            }
        });




        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                   // Toast.makeText(getApplicationContext(),""+snapshot.child("vid_url").getValue(),Toast.LENGTH_LONG).show();
                    name.add(snapshot.getKey());
                    url.add(snapshot.child("vid_url").getValue().toString());
                }
                VIDEO_ID=url.get(0);
                lv.setAdapter(new customAdapter(VideoDisplay.this,url,name));
                youTubeView.initialize(YOUTUBE_API_KEY, VideoDisplay.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            yplayer=youTubePlayer;
            youTubePlayer.cueVideo(VIDEO_ID); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}
