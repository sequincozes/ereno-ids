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
public class ClassifierResult {

    public Detail[] accuracy;
    public Iteration[] subset;
    public String classifier;

    public ClassifierResult(Detail[] accuracy, Iteration[] subset, String classifier) {
        this.accuracy = accuracy;
        this.subset = subset;
        this.classifier = classifier;
    }

    public ClassifierResult() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

   

}
