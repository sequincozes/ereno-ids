package br.uff.midiacom.ereno.outputManager.model;

import java.util.ArrayList;

public class Pid {
    String begin_time;
    String last_evaluation;
    ArrayList<Iteration> iterations;

    public Pid() {
    }

    public Pid(String begin_time, String last_evaluation, ArrayList<Iteration> iterations) {
        this.begin_time = begin_time;
        this.last_evaluation = last_evaluation;
        this.iterations = iterations;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getLast_evaluation() {
        return last_evaluation;
    }

    public void setLast_evaluation(String last_evaluation) {
        this.last_evaluation = last_evaluation;
    }

    public ArrayList<Iteration>getIterations() {
        return iterations;
    }

    public void setIterations(ArrayList<Iteration> iterations) {
        this.iterations = iterations;
    }
}
