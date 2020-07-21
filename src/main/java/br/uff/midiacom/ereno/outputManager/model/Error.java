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
public class Error {

    public String message;

    public Error() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Error(String message) {
        this.message = message;
    }

}
