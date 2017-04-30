package com.antariksh.helper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.antariksh.R;

/**
 * Created by Aditya Singh on 30-04-2017.
 */

public class FragQuizDisplay extends Fragment {
    TextView qstn, op1, op2, op3, op4;
    String ans;
    MarksCalculator marksCalc;
    CardView card1, card2, card3, card4;
    Button b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragquiz ,null);

        marksCalc= new MarksCalculator();
        qstn= (TextView) v.findViewById(R.id.qsn);
        op1= (TextView) v.findViewById(R.id.cardop1);
        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(op1);
                marksCalc.setSelected('A');

            }
        });
        op2= (TextView) v.findViewById(R.id.cardop2);
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(op2);
                marksCalc.setSelected('B');
            }
        });
        op3= (TextView) v.findViewById(R.id.cardop3);
        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(op3);
                marksCalc.setSelected('C');
            }
        });
        op4= (TextView) v.findViewById(R.id.cardop4);
        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(op4);
                marksCalc.setSelected('D');
            }
        });
        Bundle b = getArguments();
        qstn.setText(b.getString("Q"));
        op1.setText(b.getString("A"));
        op2.setText(b.getString("B"));
        op3.setText(b.getString("C"));
        op4.setText(b.getString("D"));
        ans=(b.getString("Ans"));
        return v;
    }

    public void setSelected(TextView t)
    {
        if (t==op1)
        {
            op2.setBackgroundColor(getResources().getColor(R.color.background));
            op2.setTextColor(getResources().getColor(R.color.white));
            op3.setBackgroundColor(getResources().getColor(R.color.background));
            op3.setTextColor(getResources().getColor(R.color.white));
            op4.setBackgroundColor(getResources().getColor(R.color.background));
            op4.setTextColor(getResources().getColor(R.color.white));

        }
        else if(t==op2)
        {
            op1.setBackgroundColor(getResources().getColor(R.color.background));
            op1.setTextColor(getResources().getColor(R.color.white));
            op3.setBackgroundColor(getResources().getColor(R.color.background));
            op3.setTextColor(getResources().getColor(R.color.white));
            op4.setBackgroundColor(getResources().getColor(R.color.background));
            op4.setTextColor(getResources().getColor(R.color.white));
        }
        else if (t==op3)
        {
            op1.setBackgroundColor(getResources().getColor(R.color.background));
            op1.setTextColor(getResources().getColor(R.color.white));
            op2.setBackgroundColor(getResources().getColor(R.color.background));
            op2.setTextColor(getResources().getColor(R.color.white));
            op4.setBackgroundColor(getResources().getColor(R.color.background));
            op4.setTextColor(getResources().getColor(R.color.white));

        }
        else
        {
            op1.setBackgroundColor(getResources().getColor(R.color.background));
            op1.setTextColor(getResources().getColor(R.color.white));
            op2.setBackgroundColor(getResources().getColor(R.color.background));
            op2.setTextColor(getResources().getColor(R.color.white));
            op3.setBackgroundColor(getResources().getColor(R.color.background));
            op3.setTextColor(getResources().getColor(R.color.white));
        }
        t.setBackgroundColor(getResources().getColor(R.color.white));
        t.setTextColor(getResources().getColor(R.color.background));
    }
}