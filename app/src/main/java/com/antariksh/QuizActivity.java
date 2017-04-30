package com.antariksh;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements Animation.AnimationListener{
    Context context;
    TextView textViewStart;
    ImageView imageViewQuiz,imageViewGo;
    Animation an,an2,an3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textViewStart= (TextView) findViewById(R.id.textstart);
        imageViewGo= (ImageView) findViewById(R.id.buttonGo);
        imageViewQuiz= (ImageView) findViewById(R.id.quiz);

        an= AnimationUtils.loadAnimation(this,R.anim.fadeinfast);
        an2= AnimationUtils.loadAnimation(this,R.anim.fadeinfast);
        an3= AnimationUtils.loadAnimation(this,R.anim.fadeinfast);
        textViewStart.setVisibility(View.VISIBLE);
        textViewStart.setAnimation(an);
        an.setAnimationListener(this);
        an2.setAnimationListener(this);
        an3.setAnimationListener(this);
        imageViewGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(QuizActivity.this,RunQuiz.class));
            }
        });



    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation==an)
        {
            imageViewQuiz.setVisibility(View.VISIBLE);
            imageViewQuiz.setAnimation(an2);
        }
        if(animation==an2)
        {
            imageViewGo.setVisibility(View.VISIBLE);
            imageViewGo.setAnimation(an3);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
