package com.antariksh;

import android.content.Intent;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.antariksh.helper.SharedPrefManager;

public class EntryScreen extends AppCompatActivity {
    private ImageView imageViewTitle,imageViewRocket;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_entry_screen);
     /*   Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));*/
        prefManager = new SharedPrefManager(this);
        Animation an= AnimationUtils.loadAnimation(getBaseContext(),R.anim.entrybottom);
        Animation an2= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadein);
        imageViewTitle= (ImageView) findViewById(R.id.title);
        imageViewRocket= (ImageView) findViewById(R.id.rocket);
        imageViewRocket.setAnimation(an);
        an2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (prefManager.isLoggedIn())
                startActivity(new Intent(EntryScreen.this,MasterActivity.class));
                else
                startActivity(new Intent(EntryScreen.this,LoginActivity.class));
                finishAfterTransition();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageViewTitle.setAnimation(an2);

    }
}
