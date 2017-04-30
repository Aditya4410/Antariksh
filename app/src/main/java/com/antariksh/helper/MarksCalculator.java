package com.antariksh.helper;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aditya Singh on 30-04-2017.
 */

public class MarksCalculator {
    static int totalMarks=0;
    static char selected='e';
    public void increaseMarks()
    {
        totalMarks++;
        Log.d("Current Marks: ",""+totalMarks);
    }

    public static void setTotalMarks(int totalMarks) {
        MarksCalculator.totalMarks = totalMarks;
    }

    public int getTotalMarks()
    {
        return totalMarks;
    }

    public char getSelected() {
        return selected;
    }

    public void setSelected(char selected) {
        MarksCalculator.selected = selected;
    }
}
