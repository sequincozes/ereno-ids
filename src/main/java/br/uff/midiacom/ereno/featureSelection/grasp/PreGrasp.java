/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp;

import br.uff.midiacom.ereno.abstractclassification.FeatureSubsets;
import br.uff.midiacom.ereno.outputManager.OutputManager;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.crossvalidation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;
import br.uff.midiacom.ereno.outputManager.model.Detail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import weka.core.Instances;

/**
 *
 * @author sequi
 */
public abstract class PreGrasp {

    public static Instances allInstances;

    int maxTime = 24 * 60 * 60 * 1000; // quantidade total de iteracoes
    int maxIterations = 1; // quantidade total de iteracoes
    public int maxNumberEvaluation = 100000; // quantidade total de avaliações (50*20)
    final int maxNoImprovement = 100000; // iteracoes sem melhorias consecutivas
    static int NUM_FEATURES = 78;
    //String method = "method";
    //String experimentIdentifier = "0000000";
    boolean localOutput = false;

    // Current Run Data
    int iterationNumber = 0;
    int noImprovements = 0;
    long currentTime = 0;
    boolean printSelection = false;
    public int numberEvaluation = 0;
    OutputManager outputManager;
    boolean printEachEvaluation = false;
    long beginTime = 0;

    public static void main(String[] args) throws IOException, Exception {
        System.out.println("pregrasp.jar dataset classifier");
        String dataset = args[0];
        Integer classifier = Integer.valueOf(args[1]);
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[classifier-1];
        GeneralParameters.ALL_IN_ONE_FILE = dataset;//"/home/silvio/datasets/CICIDS2017/all_in_one/all_in_one_cicids2017.csv";       
        allInstances = Util.loadSingleFile(true);
        if (dataset.contains("CIC")) {
            new PreGraspSimple().runGraspSimple(FeatureSubsets.CICIDS_FULL, "grasp", NeighborhoodStructures.IWSS);
        } else if (dataset.contains("KDD")) {
            new PreGraspSimple().runGraspSimple(FeatureSubsets.KDD_FULL, "grasp", NeighborhoodStructures.IWSS);
        } else if (dataset.contains("WSN")) {
            new PreGraspSimple().runGraspSimple(FeatureSubsets.WSN_FULL, "grasp", NeighborhoodStructures.IWSS);
        }
        /*
        if (args.length == 2 && args[0].equals("all")) {
            String command = "java -jar grasp.jar " + args[1] + " 1 & "
                    + "java -jar grasp.jar " + args[1] + " 2 &"
                    + "java -jar grasp.jar " + args[1] + " 3 &"
                    + "java -jar grasp.jar " + args[1] + " 4 & "
                    + "java -jar grasp.jar " + args[1] + " 5 &";
            Runtime.getRuntime().exec(command);

        } else {
            enableMicroservices(args);
        }
         */
    }

    private static void enableMicroservices(String[] args) throws Exception {
        int graspAlgoritm;
        int classifier;
        switch (args.length) {
            case 4:
                graspAlgoritm = Integer.valueOf(args[0]);
                classifier = Integer.valueOf(args[1]) - 1;
                GeneralParameters.ALL_IN_ONE_FILE = args[2] + ".csv";
                GeneralParameters.FOLDS = Integer.valueOf(args[3]);
                showOptions(graspAlgoritm, classifier);
                break;
            case 3:
                graspAlgoritm = Integer.valueOf(args[0]);
                classifier = Integer.valueOf(args[1]) - 1;
                GeneralParameters.ALL_IN_ONE_FILE = args[2] + ".csv";
                showOptions(graspAlgoritm, classifier);
                break;
            case 2:
                graspAlgoritm = Integer.valueOf(args[0]);
                classifier = Integer.valueOf(args[1]) - 1;
                showOptions(graspAlgoritm, classifier);
                break;
            case 0:
                System.out.println("Please select the experimentation type:");
                System.out.println("(1) GRASP");
                System.out.println("(2) GRASP-VND");
                System.out.println("(3) GRASP-RVND");
                // System.out.println("(4) Single Classifier");
                //System.out.println("(5) Multiple Classifier");
                Scanner t = new Scanner(System.in);
                graspAlgoritm = t.nextInt();
                classifier = -1;
                showOptions(graspAlgoritm, classifier);

                break;
            default:
                System.out.println("Usage: grasp.jar [grasp_mode classifier] OR [grasp_mode classifier dataset (without .csv)]");
                System.out.println("Alternative Usage: grasp.jar grasp_mode param2 (params are optional, but if exists, must be two)");

                break;
        }
    }

    public PreGrasp setupGraspMicroservice(int choosenClassifierIndex) throws Exception {
        try {
            allInstances = Util.loadSingleFile(true);
        } catch (NullPointerException e) {
            downloadDatabase();
        }
        if (choosenClassifierIndex < 0) {
            System.out.println("Dataset: " + GeneralParameters.ALL_IN_ONE_FILE);
            System.out.println("Choose classifier:");
            System.out.println("(1) RANDOM TREE");
            System.out.println("(2) J48");
            System.out.println("(3) REP TREE");
            System.out.println("(4) NAIVE BAYES");
            System.out.println("(5) RANDOM FOREST");
            Scanner t = new Scanner(System.in);
            choosenClassifierIndex = t.nextInt() - 1;
        }
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[choosenClassifierIndex];
        return this;
    }

    public GraspSolution getBestGlobalSolution() {
        return bestGlobalSolution;
    }
    private GraspSolution bestGlobalSolution;

    public void setBestGlobalSolution(GraspSolution bestGlobalSolution) throws IOException {
        //writeDetails("EVALUATION [" + numberEvaluation + "] - NEW BEST GLOBAL: " + bestGlobalSolution.getFeaturesAndPerformance());
        this.bestGlobalSolution = bestGlobalSolution;
    }

    public ArrayList<Integer> buildCustomRCL(int[] RCL) throws Exception {
        ArrayList<Integer> candidates = new ArrayList<>();
        System.out.print("RCL: {");
        for (int i = 0; i < RCL.length; i++) {
            candidates.add(RCL[i]);
            System.out.print(RCL[i]);
            if (i < RCL.length - 1) {
                System.out.print(",");
            } else {
                System.out.println("}");
            }
        }
        return candidates;
    }

    public GraspSolution buildIWSSSolucaoInicial(ArrayList<Integer> RCL) {
        /* Seleciona as features das primeiras N posicoes como solução inicial*/
        GraspSolution solution = new GraspSolution();

        solution.addFeature(RCL.remove(0));

        /* As demais features irão compor a RCL_flip */
        while (RCL.size() > 0) {
            solution.addFeatureFlip(RCL.remove(RCL.size() - 1));
        }

        return solution;
    }

    public GraspSolution avaliar(GraspSolution solution) throws Exception {
        GeneralParameters.FEATURE_SELECTION = solution.getArrayFeaturesSelecionadas();
        GenericResultado[] resultado = CrossValidation.runSingleClassifier(Util.copyAndFilter(allInstances, printSelection), GeneralParameters.FOLDS, GeneralParameters.GRASP_SEED);
        solution.setEvaluation(Util.getResultAverage(resultado));
        String avaliacao = "AVALIAÇÃO " + "(" + numberEvaluation++ + ")" + Arrays.toString(solution.getArrayFeaturesSelecionadas()) + " > " + solution.getEvaluation().getAcuracia();
        if (printEachEvaluation) {
            System.out.println(avaliacao);
        }
        outputManager.writeDetail(new Detail(solution.getAccuracy(), solution.getFeatureSet(), numberEvaluation, solution.getEvaluation().getTime()));
        return solution;
    }

    public void downloadDatabase() throws InterruptedException, IOException, Exception {
        System.out.println("Database File '" + GeneralParameters.ALL_IN_ONE_FILE + "' not found. Press (D) for download it or (C) for cancel.");
        Scanner download = new Scanner(System.in);
        if (download.next().equals("D") || download.next().equals("d")) {
            System.out.println("Downloading...");
            String downloadString = "wget -O " + GeneralParameters.ALL_IN_ONE_FILE + ".zip -b https://drive.google.com/uc?id=1jbC6AyjXmyPVtq2kML5_RWLqrqKqfZ-m&export=download";
            String unzipString = "unzip " + GeneralParameters.ALL_IN_ONE_FILE + ".zip ";
            System.out.println("Executing Command: " + downloadString);
            Runtime.getRuntime().exec(downloadString);
            System.out.println("Please wait 30 seconds... ");
            Thread.sleep(30000);
            System.out.println("Executing Command: " + unzipString);
            Runtime.getRuntime().exec(unzipString);
            System.out.println("Please wait 5 seconds... ");
            Thread.sleep(5000);
            allInstances = Util.loadSingleFile(true);
        }

    }

    private static void showOptions(int graspAlgoritm, int classifier) throws Exception {
        switch (graspAlgoritm) {
            case 1:
                ((GraspSimple) new GraspSimple().setupGraspMicroservice(classifier)).runGraspSimple(FeatureSubsets.RCL, "grasp", NeighborhoodStructures.BIT_FLIP);
                break;
            case 2:
                ((GraspVND) new GraspVND().setupGraspMicroservice(classifier)).runGraspVND(FeatureSubsets.RCL, "grasp_vnd");
                break;
            case 3:
                ((GraspRVND) new GraspRVND().setupGraspMicroservice(classifier)).runGraspRVND(FeatureSubsets.RCL, "grasp_rvnd");
                break;
            case 4:
                System.out.println("Not implemented yet.");
                break;
            case 5:
                System.out.println("Not implemented yet.");
                break;
        }
    }

}
