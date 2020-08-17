/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.outputManager.*;
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
public class FirebaseOutputProcessor {

    private DatabaseReference mDatabase;
    private String graspMethod;
    int currentIterationNumber = 1;
    boolean offlinemode = false;

    public static void main(String[] args) {
        FirebaseOutputProcessor fb = new FirebaseOutputProcessor();
        fb.initialize("grasp_rvnd", "kdd");
        fb.readDetails("RandomForest", "cluster05", "4478","0");
        //fb.readBestIteration("NaiveBayes", "cluster01", "30938");
        while (true) {}

    }

    public FirebaseOutputProcessor initialize(String graspMethod, String dataset) {
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
                mDatabase = FirebaseDatabase.getInstance().getReference().child(dataset).child(graspMethod);
                System.out.println("mDatabase:" + mDatabase);
                serviceAccount.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FirebaseOutputProcessor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FirebaseOutputProcessor.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return this;

    }

    public void readDetails(String Classifier, String host, String pid, String it) {
        final DatabaseReference iterationsReference = mDatabase.child(Classifier).child(host).child(pid).child("iterations").child(it).child("details");//.child("1884");
        System.out.println("iterationsReferences: " + iterationsReference);
        iterationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Data Change");
                if (dataSnapshot.getChildrenCount() > 1) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        //System.out.println("Aq: " + messageSnapshot);
                        Detail interaction = messageSnapshot.getValue(Detail.class);
                        //System.out.println("It: " + interaction.accuracy);
                        printBestDetail(interaction);
                    }
                } else if (dataSnapshot.getChildrenCount() == 0) {
                    System.out.println("No complete iterations. " + dataSnapshot.toString());
                    Detail detail = (Detail) dataSnapshot.child("details").getValue();
                    printBestDetail(detail);
                } else {
                    System.out.println("One complete iteration. " + dataSnapshot.toString());
                    Iteration iteratino = (Iteration) dataSnapshot.getValue();
                    printBestDetail(iteratino.details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro ao salvar detalhe: " + databaseError);
            }

        }
        );

    }

    public void readBestIteration(String Classifier, String host, String pid) {
        final DatabaseReference iterationsReference = mDatabase.child(Classifier).child(host).child(pid).child("iterations");//.child("1").child("details").child("1884");
        System.out.println("iterationsReferences: " + iterationsReference);
        iterationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Data Change");
                if (dataSnapshot.getChildrenCount() > 1) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        try {
                            //System.out.println("Aq: " + messageSnapshot);
                            Iteration interaction = messageSnapshot.getValue(Iteration.class);
                            //System.out.println("It: " + interaction.accuracy);
                            printIteration(interaction);
                        } catch (Exception e) {
                            System.out.println(e.getCause());
                        }
                    }
                } else if (dataSnapshot.getChildrenCount() == 0) {
                    System.out.println("No complete iterations. " + dataSnapshot.toString());
                    Detail detail = (Detail) dataSnapshot.child("details").getValue();
                    printBestDetail(detail);
                } else {
                    System.out.println("One complete iteration. " + dataSnapshot.toString());
                    Iteration iteratino = (Iteration) dataSnapshot.getValue();
                    printBestDetail(iteratino.details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro ao salvar detalhe: " + databaseError);
            }

        }
        );

    }

    private void printBestDetail(Detail child) {
        System.out.println(child.time + ";" + child.accuracy + ";" + child.evaluation + ";" + child.subset);
    }

    private void printIteration(Iteration iteratino) {
        System.out.println(iteratino.currentTime + ";" + iteratino.accuracy + ";" + iteratino.iterationNumber + ";" + iteratino.noImprovments + ";" + iteratino.numberEvaluation+ ";" + iteratino.subset);
    }
}
