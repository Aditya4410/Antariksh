package com.antariksh.helper;

/**
 * Created by Aditya Singh on 30-04-2017.
 */

public class QuizQuestion {
    String q,o1,o2,o3,o4,ans;

    public QuizQuestion(String q, String o1, String o2, String o3, String o4,String ans) {
        this.q = q;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
        this.o4 = o4;
        this.ans=ans;


    }
    public String getAns()
    {
        return ans;
    }

    public String getQ() {
        return q;
    }

    public String getO1() {
        return o1;
    }

    public String getO2() {
        return o2;
    }

    public String getO3() {
        return o3;
    }

    public String getO4() {
        return o4;
    }
}
