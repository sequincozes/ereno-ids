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
    public int iteration;
    public long time;

    public Iteration() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Iteration(String accuracy, String subset, int iteration, long time) {
        this.accuracy = accuracy;
        this.subset = subset;
        this.iteration = iteration;
        this.time = time;
    }

}
