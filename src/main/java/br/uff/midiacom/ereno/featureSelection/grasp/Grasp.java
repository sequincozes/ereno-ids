/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp;

import br.uff.midiacom.ereno.evaluation.experiments.FeatureSelectionExperiments;
import br.uff.midiacom.ereno.featureSelection.subsets.*;
import br.uff.midiacom.ereno.outputManager.OutputManager;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.evaluation.GraspMetrics;
import br.uff.midiacom.ereno.evaluation.TimeAnalysis;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;
import br.uff.midiacom.ereno.outputManager.model.Detail;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import weka.core.Instances;

/**
 * @author sequi
 */
public abstract class Grasp {

    public static Instances allInstances;

    public static GraspMetrics criteriaMetric = GraspMetrics.F1SCORE;// GraspMetrics.F1SCORE;

    public int maxTime = 24 * 60 * 60 * 1000; // quantidade total de iteracoes
    int maxIterations = 100000; // quantidade total de iteracoes
    public int maxNumberEvaluation = 100000; // quantidade total de avaliações (50*20)mc
    final int maxNoImprovement = 100000; // iteracoes sem melhorias consecutivas
    static int NUM_FEATURES = 5;
    //String method = "method";
    //String experimentIdentifier = "0000000";
    boolean localOutput = false;

    // Current Run Data
    int iterationNumber = 0;
    int noImprovements = 0;
    public long currentTime = 0;
    boolean printSelection = false;
    public int numberEvaluation = 0;
    OutputManager outputManager;
    private GraspSolution bestGlobalSolution;
    public int numBitFLipFeatures = 5;
    long beginTime = 0;

    /* java -jar grasp.jar 2 1 wsn >> full_rcl_2_1.txt&
         java -jar graspAcc.jar 2 2 wsn >> full_rcl_2_2.txt&
         java -jar graspAcc.jar 2 3 wsn >> full_rcl_2_3.txt&
         java -jar graspAcc.jar 2 4 wsn >> full_rcl_2_4.txt&
         java -jar graspAcc.jar 2 5 wsn >> full_rcl_2_5.txt&*/

    //         java -jar graspAcc.jar GR-G-VND 2 wsn >> full_rcl_2_2.txt&
    public static void main(String[] args) throws IOException, Exception {
        args = new String[]{"GR-G-BF", "2", "all_in_one_wsn"};
        /* TEMPORARIO, LEVA PARA O FS*/
        if (false) {
            FeatureSelectionExperiments.main(args);
        } else {
            /* FIM TEMPORARIO, LEVA PARA O FS*/

            //   args = new String[]{"2", "4", "wsn", "5"};
            boolean pregrasp = false;
            boolean timeAnalysis = false;
            if (timeAnalysis) {
                TimeAnalysis.main(args);
            } else if (pregrasp) {
                PreGrasp.main(args);
            } else {
//            enableMicroservicesOld(args);
                enableMicroservices(args);
            }
        }

    }

    public static void setupStandaloneGrasp(String classifier) throws Exception {
        GeneralParameters.DATASET = "/home/silvio/datasets/wsn-ds/all/all_in_one.csv";
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.J48;
        allInstances = Util.loadSingleFile(true);
    }

    private static void enableMicroservices(String[] args) throws Exception {

        String graspAlgoritm = args[0];
        int classifier = Integer.valueOf(args[1]) - 1;
        switch (args.length) {
            case 4:
                GeneralParameters.DATASET = args[2] + ".csv";
                GeneralParameters.FOLDS = Integer.valueOf(args[3]);
                showOptions(graspAlgoritm, classifier, args[2]);
                break;
            case 3:
                graspAlgoritm = args[0];
                classifier = Integer.valueOf(args[1]) - 1;
                GeneralParameters.DATASET = args[2] + ".csv";
                showOptions(graspAlgoritm, classifier, args[2]);
                break;
            case 2:
                showOptions(graspAlgoritm, classifier, args[2]); // bug aqui, so existem 2. Corrigir se gor usar
                break;
            case 0:
                System.out.println("Please select the experimentation type:");
                System.out.println("GR-G-BF, GR-G-VND, GR-G-RVND, F-G-VND, F-G-RVND, I-G-VND");
                // System.out.println("(4) Single Classifier");
                //System.out.println("(5) Multiple Classifier");
                Scanner t = new Scanner(System.in);
                graspAlgoritm = t.nextLine();
                System.out.println("Selected algo: " + graspAlgoritm);
                System.out.println("Please enter the dataset.csv name:");
                // System.out.println("(4) Single Classifier");
                //System.out.println("(5) Multiple Classifier");

                Scanner t2 = new Scanner(System.in);
                String dataset = t2.nextLine();

                classifier = -1;
                showOptions(graspAlgoritm, classifier, dataset);

                break;
            default:
                System.out.println("Usage: grasp.jar [grasp_mode classifier] OR [grasp_mode classifier dataset (without .csv)]");
                System.out.println("Alternative Usage: grasp.jar grasp_mode param2 (params are optional, but if exists, must be two)");

                break;
        }
    }

    // This works by setupping grasp method index, but RCL should be hardcoded
    private static void enableMicroservicesOld(String[] args) throws Exception {
        int graspAlgoritm;
        int classifier;
        switch (args.length) {
            case 4:
                graspAlgoritm = Integer.valueOf(args[0]);
                classifier = Integer.valueOf(args[1]) - 1;
                GeneralParameters.DATASET = args[2] + ".csv";
                GeneralParameters.FOLDS = Integer.valueOf(args[3]);
                showOptionsOld(graspAlgoritm, classifier, args[2]);
                break;
            case 3:
                graspAlgoritm = Integer.valueOf(args[0]);
                classifier = Integer.valueOf(args[1]) - 1;
                GeneralParameters.DATASET = args[2] + ".csv";
                showOptionsOld(graspAlgoritm, classifier, args[2]);
                break;
            case 2:
                graspAlgoritm = Integer.valueOf(args[0]);
                classifier = Integer.valueOf(args[1]) - 1;
                showOptionsOld(graspAlgoritm, classifier, args[2]); // bug aqui, so existem 2. Corrigir se gor usar
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
                System.out.println("Selected algo: " + graspAlgoritm);
                System.out.println("Please enter the dataset.csv name:");
                // System.out.println("(4) Single Classifier");
                //System.out.println("(5) Multiple Classifier");

                Scanner t2 = new Scanner(System.in);
                String dataset = t2.nextLine();

                classifier = -1;
                showOptionsOld(graspAlgoritm, classifier, dataset);

                break;
            default:
                System.out.println("Usage: grasp.jar [grasp_mode classifier] OR [grasp_mode classifier dataset (without .csv)]");
                System.out.println("Alternative Usage: grasp.jar grasp_mode param2 (params are optional, but if exists, must be two)");

                break;
        }
    }

    public Grasp setupGraspMicroservice(int choosenClassifierIndex) throws Exception {
        try {
            allInstances = Util.loadSingleFile(true);
        } catch (NullPointerException e) {
            downloadDatabase();
        }
        if (choosenClassifierIndex < 0) {
            System.out.println("Dataset: " + GeneralParameters.DATASET);
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

    public void setBestGlobalSolution(GraspSolution bestGlobalSolution) throws IOException {
        //writeDetails("EVALUATION [" + numberEvaluation + "] - NEW BEST GLOBAL: " + bestGlobalSolution.getFeaturesAndPerformance());
        this.bestGlobalSolution = bestGlobalSolution;
        outputManager.writeBestImprovement(new Detail(bestGlobalSolution.getF1Score(), bestGlobalSolution.getFeatureSet(), numberEvaluation, bestGlobalSolution.getEvaluation().getTime()));
    }

    public ArrayList<Integer> buildCustomRCL(int[] RCL) throws Exception {
        ArrayList<Integer> candidates = new ArrayList<>();
        //System.out.print("RCL: {");
        for (int i = 0; i < RCL.length; i++) {
            candidates.add(RCL[i]);
            /*            System.out.print(RCL[i]);
            if (i < RCL.length - 1) {
                System.out.print(",");
            } else {
                System.out.println("}");
            }
             */
        }

        return candidates;
    }

    public GraspSolution reconstructSolution(GraspSolution initialSolution) {
        ArrayList<Integer> fullRCL = initialSolution.copyRCLFeatures();
        fullRCL.addAll(initialSolution.copySolutionFeatures());
        System.out.println("RCL: " + fullRCL);
        /* Seleciona as features das primeiras N posicoes como solução inicial*/
        GraspSolution solution = new GraspSolution();
        Random r = new Random(fullRCL.size());
        while (solution.getArrayFeaturesSelecionadas().length < NUM_FEATURES) {
            solution.addFeature(fullRCL.remove(r.nextInt(fullRCL.size())));
        }

        // As demais features irão compor a RCL_flip 
        while (fullRCL.size() > 0) {
            solution.addFeatureFlip(fullRCL.remove(0));
        }
        System.out.println("Solução Inicial: " + solution.getFeatureSet());
        System.out.println("RCL Restante: " + solution.getRCLfeatures());

        return solution;
    }

    public GraspSolution constructSolution(ArrayList<Integer> fullRCL) {

        System.out.println("RCL: " + fullRCL);
        /* Seleciona as features das primeiras N posicoes como solução inicial*/
        GraspSolution solution = new GraspSolution();
        Random r = new Random(fullRCL.size());
        while (solution.getArrayFeaturesSelecionadas().length < NUM_FEATURES) {
            solution.addFeature(fullRCL.remove(r.nextInt(fullRCL.size())));
        }

        // As demais features irão compor a RCL_flip 
        while (fullRCL.size() > 0) {
            solution.addFeatureFlip(fullRCL.remove(0));
        }
        System.out.println("Solução Inicial: " + solution.getFeatureSet());
        System.out.println("RCL Restante: " + solution.getRCLfeatures());

        return solution;
    }

    public GraspSolution avaliar(GraspSolution solution) throws Exception {
        boolean debug = false;
        if (debug) {
            solution.setEvaluation(new GenericResultado(ThreadLocalRandom.current().nextInt(10, 100)));
            return solution;
        } else {
            solution.setCurrentTimeSeconds((beginTime / 1000) - (System.currentTimeMillis() / 1000));
            if (GeneralParameters.DEBUG_MODE) {
                System.out.println("Dataset: " + GeneralParameters.DATASET);
                System.out.println("Classifier: " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName());
                System.out.println("Folds: " + GeneralParameters.FOLDS);
                System.out.println("Seed: " + GeneralParameters.GRASP_SEED);
            }

            GeneralParameters.FEATURE_SELECTION = solution.getArrayFeaturesSelecionadas();
            GenericResultado[] resultado = CrossValidation.runSingleClassifier(Util.copyAndFilter(allInstances, printSelection), GeneralParameters.FOLDS, GeneralParameters.GRASP_SEED);
            solution.setEvaluation(Util.getResultAverage(resultado));
            String avaliacao = "AVALIAÇÃO"
                    + "(" + numberEvaluation++ + ") - Selected: "
                    + Arrays.toString(solution.getArrayFeaturesSelecionadas())
                    + " F1Score > " + solution.getEvaluation().getF1Score()
                    + "(acc: " + solution.getEvaluation().getAcuracia();

            System.out.println(avaliacao);
            if (GeneralParameters.DEBUG_MODE) {

                if (!(solution.getEvaluation().getF1Score() > 0)) {
                    System.err.print("Problema na F1 Score: " + solution.getEvaluation().getF1Score());
                    System.exit(0);
                }
            }
            if (outputManager != null)
                outputManager.writeDetail(new Detail(solution.getF1Score(), solution.getFeatureSet(), numberEvaluation, solution.getCurrentTimeSeconds()));

            return solution;
        }
    }

    public void downloadDatabase() throws InterruptedException, IOException, Exception {
        System.out.println("Database File '" + GeneralParameters.DATASET + "' not found. Press (D) for download it or (C) for cancel.");
        Scanner download = new Scanner(System.in);
        if (download.next().equals("D") || download.next().equals("d")) {
            System.out.println("Downloading...");
            String downloadString = "wget -O " + GeneralParameters.DATASET + ".zip -b https://drive.google.com/uc?id=1jbC6AyjXmyPVtq2kML5_RWLqrqKqfZ-m&export=download";
            String unzipString = "unzip " + GeneralParameters.DATASET + ".zip ";
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

    @Deprecated
    private static void showOptionsOld(int graspAlgoritm, int classifier, String dataset) throws Exception {
        if (dataset.contains("WSN") || dataset.contains("wsn")) {
//            FeatureSubsets.RCL = FeatureSubsets.RCL_WSN_IWSSR[classifier];
            FeatureSubsets.RCL = FeatureSubsets.WSN_FULL;

        } else if (dataset.contains("KDD") || dataset.contains("kdd")) {
//            FeatureSubsets.RCL = FeatureSubsets.RCL_KDD_IWSSR[classifier];
            FeatureSubsets.RCL = FeatureSubsets.KDD_FULL;
//            FeatureSubsets.RCL = KddFeatures.IG_RCL20;

        } else if (dataset.contains("CICIDS") || dataset.contains("cicids")) {
//            FeatureSubsets.RCL = FeatureSubsets.RCL_CICIDS_IWSSR[classifier];
            FeatureSubsets.RCL = CicidsFeatures.RCL_GR;

        } else {
            System.out.println("Invalid dataset name. Must contains kdd, wsn, or cicids.");
            System.exit(1);
        }

        switch (graspAlgoritm) {
            case 1:
                ((GraspSimple) new GraspSimple().setupGraspMicroservice(classifier)).run(FeatureSubsets.RCL, "grasp", NeighborhoodStructures.BIT_FLIP, dataset);
                break;
            case 2:
                ((GraspVND) new GraspVND().setupGraspMicroservice(classifier)).run(FeatureSubsets.RCL, "grasp_vnd", dataset);
                break;
            case 3:
                ((GraspRVND) new GraspRVND().setupGraspMicroservice(classifier)).run(FeatureSubsets.RCL, "grasp_rvnd", dataset);
                break;
            case 4:
                System.out.println("Not implemented yet.");
                break;
            case 5:
                System.out.println("Not implemented yet.");
                break;
        }
    }

    private static void showOptions(String graspAlgoritm, int classifier, String dataset) throws Exception {
        FeatureSubsets featureSubsets = null;
        if (dataset.toLowerCase().contains("wsn")) {
            featureSubsets = new WsnFeatures();
        } else if (dataset.toLowerCase().contains("kdd")) {
            featureSubsets = new KddFeatures();
        } else if (dataset.toLowerCase().contains("cicids")) {
            featureSubsets = new CicidsFeatures();
        } else if (dataset.toLowerCase().contains("swat")) {
            featureSubsets = new SWATFeatures();

        } else {
            System.out.println("Invalid dataset name. Must contains kdd, wsn, or cicids.");
            System.exit(1);
        }

        if (graspAlgoritm.equals(GeneralParameters.GRASP_METHOD[0])) { // "GR-G-BF"
            FeatureSubsets.RCL = featureSubsets.RCL_GR; //não sei se é usado, mas pra nao dar problema eu estou
            ((GraspSimple) new GraspSimple()
                    .setupGraspMicroservice(classifier))
                    .run(FeatureSubsets.RCL, graspAlgoritm, NeighborhoodStructures.BIT_FLIP, dataset);

        } else if (graspAlgoritm.equals(GeneralParameters.GRASP_METHOD[1])) { // "GR-G-VND"
            FeatureSubsets.RCL = featureSubsets.RCL_GR;
//            System.out.println("featureSubsets.RCL_GR: "+featureSubsets.RCL_GR);
//            System.out.println("FeatureSubsets.RCL: "+FeatureSubsets.RCL);
            ((GraspVND) new GraspVND()
                    .setupGraspMicroservice(classifier))
                    .run(FeatureSubsets.RCL, graspAlgoritm, dataset);

        } else if (graspAlgoritm.equals(GeneralParameters.GRASP_METHOD[2])) { // "GR-G-RVND"
            FeatureSubsets.RCL = featureSubsets.RCL_GR;
            ((GraspRVND) new GraspRVND()
                    .setupGraspMicroservice(classifier))
                    .run(FeatureSubsets.RCL, graspAlgoritm, dataset);

        } else if (graspAlgoritm.equals(GeneralParameters.GRASP_METHOD[3])) { // "F-G-VND"
            FeatureSubsets.RCL = featureSubsets.RCL_FULL;
            ((GraspVND) new GraspVND()
                    .setupGraspMicroservice(classifier))
                    .run(FeatureSubsets.RCL, graspAlgoritm, dataset);

        } else if (graspAlgoritm.equals(GeneralParameters.GRASP_METHOD[4])) { // "F-G-RVND"
            FeatureSubsets.RCL = featureSubsets.RCL_FULL;
            ((GraspRVND) new GraspRVND()
                    .setupGraspMicroservice(classifier))
                    .run(FeatureSubsets.RCL, graspAlgoritm, dataset);

        } else if (graspAlgoritm.equals(GeneralParameters.GRASP_METHOD[5])) { // "I-G-VND"
            FeatureSubsets.RCL = featureSubsets.RCL_I[classifier];
            ((GraspVND) new GraspVND()
                    .setupGraspMicroservice(classifier))
                    .run(FeatureSubsets.RCL, graspAlgoritm, dataset);

        } else {
            System.out.println("Opção inválida: " + graspAlgoritm);
        }

    }

}
