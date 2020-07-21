/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.outputManager;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.NonNull;
import br.uff.midiacom.ereno.outputManager.model.Detail;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import br.uff.midiacom.ereno.outputManager.model.Error;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silvio
 */
public class FirebaseOutput implements OutputManager {

    private DatabaseReference mDatabase;
    private String graspMethod;
    private String experimentName;

    @Override
    public OutputManager initialize(String graspMethod) {
        this.experimentName = GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName() + "_" + ManagementFactory.getRuntimeMXBean().getName().replace(".", "_");
        this.graspMethod = graspMethod;
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("ereno-9326b-firebase-adminsdk-n4abg-a77d35e070.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://ereno-9326b.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            serviceAccount.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FirebaseOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FirebaseOutput.class.getName()).log(Level.SEVERE, null, ex);

        }
        return this;

    }

    @Override
    public void writeDetail(Detail detail) {
        final DatabaseReference detailReference = mDatabase.child(graspMethod).child(experimentName).child("details");
        detailReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                detailReference.child(String.valueOf(dataSnapshot.getChildrenCount())).setValueAsync(detail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro ao salvar detalhe: " + databaseError);
            }
        });
    }

    @Override
    public void writeIteration(Iteration iteration) {
        final DatabaseReference iterationReference = mDatabase.child(graspMethod).child(experimentName).child("iterations");
        iterationReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                iterationReference.child(String.valueOf(dataSnapshot.getChildrenCount())).setValueAsync(iteration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro ao salvar detalhe: " + databaseError);
            }
        });
    }

    @Override
    public void writeError(Error error) {
        final DatabaseReference errorReference = mDatabase.child(graspMethod).child(experimentName).child("errors");
        errorReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                errorReference.child(String.valueOf(dataSnapshot.getChildrenCount())).setValueAsync(error);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro ao salvar detalhe: " + databaseError);
            }
        });
    }

}
