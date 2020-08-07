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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    int currentIterationNumber = 1;
    boolean offlinemode = false;

    @Override
    public OutputManager initialize(String graspMethod, String dataset) {
        this.experimentName = GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName() + "_" + ManagementFactory.getRuntimeMXBean().getName().replace(".", "_");
        this.graspMethod = graspMethod;
        if (!offlinemode) {
            FileInputStream serviceAccount = null;
            try {
                serviceAccount = new FileInputStream("ereno-9326b-firebase-adminsdk-n4abg-a77d35e070.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://ereno-9326b.firebaseio.com")
                        .build();
                FirebaseApp.initializeApp(options);
                System.out.println("dataset");
                mDatabase = FirebaseDatabase.getInstance().getReference().child(dataset);
                System.out.println("mDatabase:"+mDatabase);
                serviceAccount.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FirebaseOutput.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FirebaseOutput.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return this;

    }

    @Override
    public void writeDetail(Detail detail) {
        if (!offlinemode) {
            final DatabaseReference pathReference = (getPath(experimentName));

            final DatabaseReference iterationReference = pathReference
                    .child("iterations")
                    .child(String.valueOf(currentIterationNumber - 1));
            final DatabaseReference detailsReference = iterationReference.child("details").child(String.valueOf(detail.evaluation));

            System.out.println("detailsReference: " + detailsReference);

            detailsReference.setValue(detail, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("DatabaseError: " + de.getMessage());
                    System.out.println("DatabaseReference: " + dr.toString());
                }
            });

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            pathReference.child("last_evaluation").setValue(dtf.format(now), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("DatabaseError: " + de.getMessage());
                    System.out.println("DatabaseReference: " + dr.toString());
                }
            });
        } else {
            detail.print();
        }
    }

    @Override
    public void writeIteration(Iteration iteration) {
        if (!offlinemode) {

            currentIterationNumber = iteration.iterationNumber;
            final DatabaseReference iterationReference = (getPath(experimentName))
                    .child("iterations")
                    .child(String.valueOf(currentIterationNumber));

            System.out.println("iterationReference: " + iterationReference);

            iterationReference.setValue(iteration, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("DatabaseError: " + de.getMessage());
                    System.out.println("DatabaseReference: " + dr.toString());
                }
            });
        } else {
            iteration.print();
        }
    }

    @Override
    public void writeError(Error error) {
        if (!offlinemode) {

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
        } else {
            System.out.println(error.message);
        }
    }

    private DatabaseReference getPath(String experimentName) {
        String[] classifierAndRest = experimentName.split("_");
        String classifier = classifierAndRest[0];
        String rest = classifierAndRest[1];
        String[] pidAndHost = rest.split("@");
        String pid = pidAndHost[0];
        String host = pidAndHost[1];
        return mDatabase.child(graspMethod).child(classifier).child(host).child(pid);
    }

    @Override
    public void writeBeginTime() {
        if (!offlinemode) {
            final DatabaseReference pathReference = (getPath(experimentName));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            pathReference.child("begin_time").setValue(dtf.format(now), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr) {
                    System.out.println("DatabaseError: " + de.getMessage());
                    System.out.println("DatabaseReference: " + dr.toString());
                }
            });
        }
    }
}
