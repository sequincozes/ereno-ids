/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.outputManager.model.Detail;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.internal.NonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author silvio
 */
public class FirebaseOutputDiscovery {

    private DatabaseReference mDatabase;
    private String[] graspMethods = {"grasp", "grasp_vnd", "grasp_rvnd"};
    private String graspMethod;// = graspMethods[0];

    private String[] datasets = {"wsn", "kdd", "cicids"};
    private String dataset;// = datasets[0];

    private String[] classifiers = {"J48", "NaiveBayes", "RandomForest", "RandomTree", "REPTree"};
    private String classifier;// = classifiers[0];

    private String[] hosts = {"cluster01", "cluster04", "cluster05"};
    private String host;// = hosts[0];

    String pid;
    private boolean alreadyPrintedHeader = false;
    int currentIterationNumber = 1;
    boolean offlinemode = false;
    boolean debug = false;
    boolean printReference = false;

    public static void main(String[] args) {
        FirebaseOutputDiscovery fb = new FirebaseOutputDiscovery();
//        fb.initialize(fb.graspMethod, fb.dataset);
//        fb.discoverPIDs(fb.classifier, fb.host);
        fb.initialize();
//        fb.discoveryAll();
        fb.discoverPIDsComplete("all_in_one_cicids", "grasp");
        while (true) {
        }
    }

    @Deprecated
    public void discoveryAll() {
        //kdd,wsn, cicids
        for (String currentMethod : graspMethods) {
            this.graspMethod = currentMethod;
            for (String currentDataset : datasets) {
                this.dataset = currentDataset;
                for (String currentClassifier : classifiers) {
                    this.classifier = currentClassifier;
                    for (String currentHost : hosts) {
                        this.host = currentHost;
                        System.out.println("Will discover to: " + dataset + "/" + graspMethod + "/" + classifier + "/" + host);
//                        discoverPIDs(classifier, host);
                    }
                }
            }
        }

        //- grasp
        //- grasp_rvnd
        //- grasp_vnd
        //-- J48
        //-- NaiveBayes
        //-- RandomForest
        //-- RandomTree
        //-- REPTree
        //---- cluster01
        //---- cluster04
        //---- cluster05
        //------ <PID> []
        //-------- begin_time
        //-------- last_evaluation
        //-------- iterations
        //----------- 0 []
        //----------- N [N].currentTime
        //----------- N [N].noImprovments
        //i = N - noImprovments
        //[i].currentTime
    }

    public FirebaseOutputDiscovery initialize() {
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
                mDatabase = FirebaseDatabase.getInstance().getReference();
                if (printReference)
                    System.out.println("mDatabase:" + mDatabase);
                serviceAccount.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FirebaseOutputDiscovery.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FirebaseOutputDiscovery.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return this;

    }

    public void discoverPIDsComplete(String datasetToDiscover, String graspMethodToDiscover) {
        this.graspMethod = graspMethodToDiscover;
        this.dataset = datasetToDiscover;

        final DatabaseReference iterationsReference = mDatabase.child(datasetToDiscover).child(graspMethodToDiscover);//.child(Classifier).child(host);//.child(pid).child("iterations");//.child("1").child("details").child("1884");
        iterationsReference.addValueEventListener(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                          if (printReference)
                                                              System.out.println("Method Ref: " + dataSnapshot.getRef());
                                                          for (DataSnapshot classifierRef : dataSnapshot.getChildren()) {
                                                              classifier = classifierRef.getKey();
                                                              if (printReference)
                                                                  System.out.println("classifierRef: " + classifierRef.getRef());
                                                              for (DataSnapshot hostRef : classifierRef.getChildren()) {
                                                                  host = hostRef.getKey();
                                                                  if (printReference)
                                                                      System.out.println("hostRef: " + hostRef.getRef());
                                                                  String pids = "Found " + hostRef.getChildrenCount() + " PIDs on " + host + ": [";
                                                                  for (DataSnapshot pidRef : hostRef.getChildren()) {
                                                                      pid = pidRef.getKey();

                                                                      if (printReference)
                                                                          System.out.println("pidRef: " + pidRef.getRef());
                                                                      pids = pids.concat(pidRef.getKey() + ",");
                                                                      if (pidRef.getChildrenCount() > 1) {
                                                                          ArrayList<Iteration> iterations = new ArrayList<>();
                                                                          for (DataSnapshot itRef : pidRef.child("iterations").getChildren()) {
                                                                              if (printReference)
                                                                                  System.out.println("itRef: " + itRef.getRef());

                                                                              try {
                                                                                  Iteration it = new Iteration();

                                                                                  try {
                                                                                      long numberEval = itRef.child("numberEvaluation").getValue(Long.class);
                                                                                      it.setNumberEvaluation((int) numberEval);
                                                                                  } catch (Exception e) {
                                                                                      it.setNumberEvaluation(-1);
                                                                                  }

                                                                                  try {
                                                                                      long noImprovments = itRef.child("noImprovments").getValue(Long.class);
                                                                                      it.setNoImprovments((int) noImprovments);
                                                                                  } catch (Exception e) {
                                                                                      it.setNoImprovments(-1);
                                                                                  }

                                                                                  try {
                                                                                      long iterationNumber = itRef.child("iterationNumber").getValue(Long.class);
                                                                                      it.setIterationNumber((int) iterationNumber);
                                                                                  } catch (Exception e) {
                                                                                      it.setIterationNumber(-1);
                                                                                  }

                                                                                  try {
                                                                                      it.setCurrentTime((String) itRef.child("currentTime").getValue(String.class));
                                                                                  } catch (Exception e) {
                                                                                      it.setCurrentTime(String.valueOf(-1));
                                                                                  }
                                                                                  try {
                                                                                      it.setAccuracy((String) itRef.child("accuracy").getValue(String.class));
                                                                                  } catch (Exception e) {
                                                                                      it.setAccuracy(String.valueOf(-1));
                                                                                  }

                                                                                  try {
                                                                                      it.setSubset((String) itRef.child("subset").getValue(String.class));
                                                                                  } catch (Exception e) {
                                                                                      it.setSubset("{}");
                                                                                  }

                                                                                  iterations.add(it);
                                                                              } catch (Exception e) {
                                                                                  e.printStackTrace();
                                                                              }
                                                                          }
                                                                          printBesIteration((ArrayList<Iteration>) iterations.clone());
                                                                      } else {
//                                                                          System.out.println("No complete iterations. " + dataSnapshot.getRef());
                                                                          Detail detail = (Detail) dataSnapshot.child("details").getValue();
                                                                          ArrayList<Detail> details = new ArrayList<>();
                                                                          details.add(detail);
//                                                                          printBestDetail(details);
                                                                      }
                                                                  }
                                                                  pids = pids.concat("]");
                                                                  pids = pids.replace(",]", "]");
                                                                  if (debug) System.out.println(pids);
                                                              }
                                                          }
                                                      }

                                                      @Override
                                                      public void onCancelled(@NonNull DatabaseError databaseError) {
                                                          System.out.println("Erro ao salvar detalhe: " + databaseError);
                                                      }

                                                  }
        );

    }

    private void printBesIteration(ArrayList<Iteration> iterations) {
        Iteration lastIt = iterations.get(iterations.size() - 1);
        int lastImprovment = lastIt.getIterationNumber() - lastIt.noImprovments;
//        System.out.println("Its:"+iterations.size() +", Last:"+ lastImprovment+" = "+lastIt.getIterationNumber()+ " - "+ lastIt.noImprovments);
        Iteration improvedInt = iterations.get(lastImprovment);
//        System.out.println("Time: "+improvedInt.getCurrentTime());
        if (!alreadyPrintedHeader) {
            System.out.println("dataset;graspMethod;host;classifier;pid;lastImprovment;improvmentTime;lastTime");
            alreadyPrintedHeader = true;
        }

        try {
            if (improvedInt.getCurrentTime() == null) {
                if (iterations.size() > 0) {
                    improvedInt = iterations.get(lastImprovment + 1);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }

        if(improvedInt.getIterationNumber()>-1)
        System.out.println(dataset + ";" + graspMethod + ";" + host + ";" + classifier + ";" + pid + ";" +
                lastImprovment + "(" + improvedInt.getIterationNumber() + ")/" + (iterations.size() - 1) + ";" +
                improvedInt.getCurrentTime() + ";" + lastIt.getCurrentTime() + ";" + improvedInt.getSubset() + ";" + improvedInt.getAccuracy());


//        improvedInt.print();
//        System.out.println("Last improvmnet: "+lastImprovment);

//        System.out.println(improvedInt.getCurrentTime() + ";" + improvedInt.accuracy + ";" + improvedInt.iterationNumber + ";" + improvedInt.noImprovments + ";" + improvedInt.numberEvaluation + ";" + improvedInt.subset);
    }

    //@TODO: testar o código de cima, se nao funcionar, programar algo assim:
    //kdd
    //wsn
    //cicids
    //- grasp
    //- grasp_rvnd
    //- grasp_vnd
    //-- J48
    //-- NaiveBayes
    //-- RandomForest
    //-- RandomTree
    //-- REPTree
    //---- cluster01
    //---- cluster04
    //---- cluster05
    //------ <PID> []
    //-------- begin_time
    //-------- last_evaluation
    //-------- iterations
    //----------- 0 []
    //----------- N [N].currentTime
    //----------- N [N].noImprovments
    //i = N - noImprovments
    //[i].currentTime
}
