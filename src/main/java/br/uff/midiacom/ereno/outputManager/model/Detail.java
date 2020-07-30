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
public class Detail {

    public String accuracy;
    public String subset;
    public int evaluation;
    public long time;

    public Detail() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Detail(String accuracy, String subset, int evaluation, long time) {
        this.accuracy = accuracy;
        this.subset = subset;
        this.evaluation = evaluation;
        this.time = time;
    }
    
    public void print(){
        System.out.println(subset + " = "+accuracy);
    }

}
