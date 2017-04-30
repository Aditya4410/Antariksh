package com.antariksh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antariksh.helper.FragQuizDisplay;
import com.antariksh.helper.MarksCalculator;
import com.antariksh.helper.QuizQuestion;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RunQuiz extends AppCompatActivity {

    DatabaseReference myRef;
    ArrayList<QuizQuestion> aList;
    FragQuizDisplay quizDisplay;
    Button buttonNext;
    MarksCalculator marksCalc;
    TextView totalQuestion,correctQuestion,questionNum;
    int i=1;
    ProgressDialog pd;
    Button buttonFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_quiz);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading your quiz...");
       // pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();
        aList=new ArrayList<>();
        marksCalc= new MarksCalculator();
        marksCalc.setSelected('e');
        marksCalc.setTotalMarks(0);
        questionNum= (TextView) findViewById(R.id.textQuestionNum);
        quizDisplay= new FragQuizDisplay();
        buttonNext= (Button) findViewById(R.id.nextQuestion);
        myRef= FirebaseDatabase.getInstance().getReference().child("Quiz");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    QuizQuestion question= new QuizQuestion(snapshot.getKey(),snapshot.child("options").child("A").getValue().toString(),snapshot.child("options").child("B").getValue().toString(),snapshot.child("options").child("C").getValue().toString(),snapshot.child("options").child("D").getValue().toString(),snapshot.child("Answer").getValue().toString());
                    aList.add(question);
                }
                publishData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonNext.getText().equals("SUBMIT"))
                {
                    if(aList.get(i-1).getAns().charAt(0)==marksCalc.getSelected())
                    {
                        marksCalc.increaseMarks();

                    }

                    AlertDialog.Builder builder= new AlertDialog.Builder(RunQuiz.this);
                    View v = getLayoutInflater().inflate(R.layout.resultshow,null);

                    totalQuestion = (TextView) v.findViewById(R.id.total);
                    correctQuestion= (TextView) v.findViewById(R.id.correct);
                    buttonFinish= (Button) v.findViewById(R.id.finish);
                    buttonFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(RunQuiz.this,MasterActivity.class));
                            finish();
                        }
                    });

                    totalQuestion.setText(""+aList.size());
                    correctQuestion.setText(""+marksCalc.getTotalMarks());
                    List<PieEntry> pi=new ArrayList<PieEntry>();
                    pi.add(new PieEntry(marksCalc.getTotalMarks()));
                    pi.add(new PieEntry(aList.size()-marksCalc.getTotalMarks()));
                    PieDataSet pidataset=new PieDataSet(pi,"Graph of Score");
                    pidataset.setColors(getColor(R.color.correct),getColor(R.color.wrong));
                    pidataset.setSliceSpace(2);
                    pidataset.setValueTextSize(30);


                    PieData data=new PieData(pidataset);
                    com.github.mikephil.charting.charts.PieChart mychart= (com.github.mikephil.charting.charts.PieChart) v.findViewById(R.id.piechart);
                    mychart.setCenterText(""+(5*marksCalc.getTotalMarks()));
                    mychart.setHoleRadius(50f);
                    mychart.animateXY(2000,2000);
                    mychart.setTransparentCircleAlpha(0);
                    mychart.setData(data);
                    mychart.invalidate();
                    builder.setCancelable(false);

                    builder.setView(v);
                    builder.create().show();



                }
                if (i<aList.size()) {


                    if(aList.get(i-1).getAns().charAt(0)==marksCalc.getSelected())
                    {
                        marksCalc.increaseMarks();

                    }

                    questionNum.setText("Question: "+(i+1));




                    FragQuizDisplay newQ = new FragQuizDisplay();
                    Bundle b = new Bundle();
                    b.putString("Q", aList.get(i).getQ());
                    b.putString("A", aList.get(i).getO1());
                    b.putString("B", aList.get(i).getO2());
                    b.putString("C", aList.get(i).getO3());
                    b.putString("D", aList.get(i).getO4());
                    b.putString("Ans",aList.get(i).getAns());
                    newQ.setArguments(b);
                    Slide slidein = new Slide();
                    Slide slideout = new Slide();
                    slidein.setDuration(500);
                    slideout.setDuration(500);
                    slidein.setSlideEdge(Gravity.RIGHT);
                    slideout.setSlideEdge(Gravity.LEFT);
                    newQ.setExitTransition(slideout);
                    newQ.setEnterTransition(slidein);
                    newQ.setExitTransition(slideout);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //quizDisplay.setData(aList.get(0).getQ(),aList.get(0).getO1(),aList.get(0).getO2(),aList.get(0).getO3(),aList.get(0).getO4());
                    ft.replace(R.id.frame, newQ);
                    ft.commit();
                    if (i==aList.size()-1)
                    {
                        buttonNext.setText("SUBMIT");
                    }
                    i++;
                }

            }
        });


    }
    public void publishData()
    {
        pd.dismiss();
        buttonNext.setVisibility(View.VISIBLE);
        questionNum.setText("Question: 1");
        Bundle b = new Bundle();
        b.putString("Q",aList.get(0).getQ());
        b.putString("A",aList.get(0).getO1());
        b.putString("B",aList.get(0).getO2());
        b.putString("C",aList.get(0).getO3());
        b.putString("D",aList.get(0).getO4());
        b.putString("Ans",aList.get(0).getAns());
        quizDisplay.setArguments(b);
        Slide slidein = new Slide();
        Slide slideout = new Slide();
        slidein.setDuration(500);
        slideout.setDuration(500);
        slidein.setSlideEdge(Gravity.RIGHT);
        slideout.setSlideEdge(Gravity.LEFT);
        quizDisplay.setEnterTransition(slidein);
        quizDisplay.setExitTransition(slideout);
        //quizDisplay.setData(aList.get(0).getQ(),aList.get(0).getO1(),aList.get(0).getO2(),aList.get(0).getO3(),aList.get(0).getO4());
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame,quizDisplay);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
