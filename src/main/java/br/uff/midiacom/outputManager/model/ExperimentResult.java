/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.outputManager.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 *
 * @author silvio
 */
@IgnoreExtraProperties
public class ExperimentResult {

    public ClassifierResult[] results;
    String name;
    int numClassifiers;

    public ExperimentResult() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ExperimentResult(String name, int numClassifiers) {
        this.name = name;
        this.numClassifiers = numClassifiers;
        this.results = new ClassifierResult[numClassifiers];
    }
}
