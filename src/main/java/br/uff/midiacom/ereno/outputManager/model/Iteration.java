/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.outputManager.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 *
 * @author silvio
 */
@IgnoreExtraProperties
public class Iteration {

    public String accuracy;
    public String subset;
    public int iterationNumber;
    public int noImprovments;
    public String currentTime;
    public Detail details;
    public int numberEvaluation;

    public Iteration() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Iteration(String accuracy, String subset, int iteration, int noImprovments, int numberEvaluation, String currentTime) {
        this.accuracy = accuracy;
        this.subset = subset;
        this.iterationNumber = iteration;
        this.currentTime = currentTime;
        this.noImprovments = noImprovments;
        this.numberEvaluation = numberEvaluation;
    }

}
