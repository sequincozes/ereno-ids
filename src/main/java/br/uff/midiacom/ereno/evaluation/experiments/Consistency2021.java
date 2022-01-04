/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.*;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking.METODO;
import br.uff.midiacom.ereno.featureSelection.Util;
import br.uff.midiacom.ereno.featureSelection.subsets.*;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import java.awt.*;
import java.io.IOException;

/**
 * @author silvio
 */
public class Consistency2021 {

    public static void main(String[] args) throws Exception {
        System.out.println("Teste");
//        runWithoutCV();
        //    startRecursivo();
//        if (args.length > 0) {
//            microserviceCrossValidation(args);
//        } else {
//            manual();
//        }

//        runWithoutCV(FeatureSubsets.CONSISTENCY_GOOSEFeatures);
        runWithoutCV(new int[]{});

//        long before3 = System.currentTimeMillis();
//        runWithoutCV(FeatureSubsets.gooseAndSvPlusPlus);
//        System.out.println("Total Time (seconds):" + ((System.currentTimeMillis()-before3)/1000));

//        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/train.csv";
//        reduceInstances(10,2);
//        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/test.csv";
//        reduceInstances(10,3);
    }

    private static void runWithoutCV() throws Exception {
//        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/train1pct.csv";
        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/train.csv";

        Instances train = br.uff.midiacom.ereno.abstractclassification.Util.loadSingleFile(false);
        train.setClassIndex(train.numAttributes() - 1);

        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/test.csv";
//        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/test1pct.csv";
        Instances test = br.uff.midiacom.ereno.abstractclassification.Util.loadSingleFile(false);
        test.setClassIndex(test.numAttributes() - 1);

        GenericEvaluation.runSingleClassifierJ48(train, test);
    }

    private static void runWithoutCV(int[] filter) throws Exception {
        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/train.csv";
        GeneralParameters.FEATURE_SELECTION = filter;
        Instances train = br.uff.midiacom.ereno.abstractclassification.Util.loadAndFilterSingleFile(false);
        train.setClassIndex(train.numAttributes() - 1);

        GeneralParameters.DATASET = "/home/silvio/datasets/Full_SV_2021/consistency_v7/10pct/all_ucs/test.csv";
        Instances test = br.uff.midiacom.ereno.abstractclassification.Util.loadAndFilterSingleFile(false);
        test.setClassIndex(test.numAttributes() - 1);

        GenericResultado result = GenericEvaluation.runSingleClassifierJ48(train, test);
        for (int classIndex = 0; classIndex < result.getConfusionMatrix().length; classIndex++) {
            System.out.print("Esperado: " + classIndex + ";resultados:;");
            for (int expectedIndex = 0; expectedIndex < result.getConfusionMatrix().length; expectedIndex++) {
                System.out.print(result.getConfusionMatrix()[classIndex][expectedIndex] + ";");
            }
            System.out.println("");
        }
    }

    public static void showTree(J48 j48) throws Exception {
        // display classifier
        final javax.swing.JFrame jf =
                new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(500, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
                j48.graph(),
                new PlaceNode2());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });

        jf.setVisible(true);
        tv.fitToScreen();
    }

    private static void microserviceValidation(String[] args) throws Exception {
        GeneralParameters.FEATURE_SELECTION = ConsistencySubsets.UC0[Integer.parseInt(args[1])];
        GeneralParameters.DATASET = GeneralParameters.CONSISTENCYV5_DATASET[Integer.parseInt(args[1])];
        System.out.println(GeneralParameters.DATASET);

        if (args[0].equalsIgnoreCase("fs")) {
            FeatureRanking.justRank(GeneralParameters.DATASET, METODO.GR, Integer.parseInt(args[2]));
        } else {
            CrossValidation.printFolds(ConsistencySubsets.UC01RankedIG);
        }
    }

    private static void microserviceCrossValidation(String[] args) throws Exception {
        GeneralParameters.FEATURE_SELECTION = ConsistencySubsets.UC0[Integer.parseInt(args[1])];
        GeneralParameters.DATASET = GeneralParameters.CONSISTENCYV5_DATASET[Integer.parseInt(args[1])];
        System.out.println(GeneralParameters.DATASET);
        if (args[0].equalsIgnoreCase("fs")) {
            FeatureRanking.justRank(GeneralParameters.DATASET, METODO.GR, Integer.parseInt(args[2]));
        } else {
            CrossValidation.printFolds(ConsistencySubsets.UC01RankedIG);
        }
    }

    private static void manual() throws Exception {
        System.out.println("START FEATURE SELECTION");

        for (int i = 1; i < ConsistencySubsets.UC0.length; i++) {
            GeneralParameters.FEATURE_SELECTION = ConsistencySubsets.UC0[i];
            GeneralParameters.DATASET = GeneralParameters.CONSISTENCYV4_DATASET[i].replace("_v4", "_v5");
            System.out.println(GeneralParameters.DATASET);
            //        CrossValidation.printFolds(ConsistencySubsets.UC01RankedIG);
            FeatureRanking.justRank(GeneralParameters.DATASET, METODO.GR);
        }
        System.out.println("START CROSSVALIDATION");
        for (int i = 1; i < ConsistencySubsets.UC0.length; i++) {
            GeneralParameters.FEATURE_SELECTION = ConsistencySubsets.UC0[i];
            GeneralParameters.DATASET = GeneralParameters.CONSISTENCYV4_DATASET[i].replace("_v4", "_v5");
            System.out.println(GeneralParameters.DATASET);
            CrossValidation.printFolds(ConsistencySubsets.UC01RankedIG);
//            FeatureRanking.justRank(GeneralParameters.DATASET, METODO.GR);
        }
    }

    private static void startRecursivo() throws Exception {
        GeneralParameters.CSV = true;
        GeneralParameters.DATASET = GeneralParameters.CONSISTENCYV4_DATASET_UC01_10percent;
        ;
        System.out.println(GeneralParameters.DATASET);

        for (int i = 0; i < ConsistencySubsets.UC01RankedIG.length; i++) {
            System.out.print(ConsistencySubsets.UC01RankedIG[i]);
            if (i < ConsistencySubsets.UC01RankedIG.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println("");
//        CrossValidation.printFolds(ConsistencySubsets.UC01RankedIG);
        recursivo(ConsistencySubsets.UC01RankedIG);

    }

    static void recursivo(int[] fs) throws Exception {
        CrossValidation.printFolds(fs);
        int[] reducedFS = new int[fs.length - 1];
        for (int i = 1; i < fs.length; i++) {
            reducedFS[i - 1] = fs[i];
            System.out.print(fs[i]);
            if (i < fs.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println("");
        if (reducedFS.length > 1) {
            recursivo(reducedFS);
        }
    }


    public static void reduceInstances(int totalFolds, int fold) throws IOException {
        Instances allInstances = Util.cutFold(totalFolds, fold, 7, new String[]{GeneralParameters.DATASET});
        Util.writeInstancesToFile(allInstances, GeneralParameters.DATASET.replace(".csv", "_" + totalFolds + "folds-" + fold + ".csv"));
        System.out.println("Tamanho: " + allInstances.size());
    }

    public static void SWATCrossValidation() throws Exception {
        int[] completo = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        int[] top5 = new int[]{12, 26, 4, 25, 39};
        CrossValidation.runAndPrintFoldResults(new int[]{12, 26, 4, 25, 39}, 2, false);

    }

    public static void wsnCrossValidation() throws Exception {
        boolean printConfusionMatrix = false;
        System.out.println("--- WSN_FULL WSN_BLACKHOLE_GR_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_GR_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_GR_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_GR_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_BLACKHOLE_IG_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_IG_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_IG_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_IG_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_BLACKHOLE_ONER_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_ONER_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_ONER_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_BLACKHOLE_ONER_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_GRAYHOLE_GR_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_GR_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_GR_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_GR_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_GRAYHOLE_IG_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_IG_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_IG_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_IG_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_GRAYHOLE_ONER_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_ONER_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_ONER_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_ONER_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_FLOODING_GR_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_GR_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_GR_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_GR_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_FLOODING_IG_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_IG_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_IG_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_GRAYHOLE_IG_5, 2, printConfusionMatrix);

        System.out.println("--- WSN_FULL WSN_FLOODING_ONER_5");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_FLOODING_ONER_5, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_FLOODING_ONER_5, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_FLOODING_ONER_5, 2, printConfusionMatrix);

    }

    private static void fullConsistencyV3CrossValidation(int classifierIndex) throws Exception {
        /*@attribute @class@ {normal, random_replay, inverse_replay, masquerade_fake_fault, masquerade_fake_normal, injection, poisoned_high_rate}*/
        boolean printConfusionMatrix = false;
//        CrossValidation.runFastFirstFold(FullConsistencySubset.FULL, classifierIndex, printConfusionMatrix);

        FeatureRanking.justRank(GeneralParameters.CONSISTENCY_DATASET_1percent, METODO.IG);
//        FeatureRanking.justMergeAndRank(allInstances, METODO.IG);
//        CrossValidation.printFoldResults(FullConsistencySubset.FULL, 1, printConfusionMatrix);
//        FeatureRanking.justRank(GeneralParameters.CONSISTENCY_DATASET, METODO.OneR);
//        FeatureRanking.justRank(GeneralParameters.CONSISTENCY_DATASET, METODO.GR);
    }

    public void otherTestes() throws Exception {

        if (false) { // eu tava fazendo esse acho

            GeneralParameters.DATASET = GeneralParameters.KDD_DATASET;
            System.out.println("--- (KDD) GR-G-RVND");
            CrossValidation.printFolds(KddFeatures.IG_RVND);

            GeneralParameters.DATASET = GeneralParameters.CICIDS_DATASET;
            System.out.println("--- (CICIDS) GR-G-RVND");
            CrossValidation.printFolds(CicidsFeatures.GRASP_RVND_IG);
        }

        if (false) { // experimentos que faltam com wsn + grasp IMPROVED
            GeneralParameters.DATASET = GeneralParameters.WSN_DATASET;
            System.out.println("--- (WSN) GR-G-VND");
            CrossValidation.printFolds(WsnFeatures.I_G_VND);
            System.out.println("--- (WSN) GR-G-RVND");
            CrossValidation.printFolds(WsnFeatures.GR_G_RVND);

        }

//        System.out.println("--- WSN GRASP");
//        printFolds(WsnFeatures.WSN_GRASPP_BITFLIP);
//        System.out.println("--- WSN FULL");
//        printFolds(new int[][]{WsnFeatures.WSN_FULL, WsnFeatures.WSN_FULL, WsnFeatures.WSN_FULL, WsnFeatures.WSN_FULL, WsnFeatures.WSN_FULL});
//        System.out.println("--- WSN Filter");
        //printFolds(new int[][]{WsnFeatures.WSN_GRAYHOLE_IG_5, WsnFeatures.WSN_GRAYHOLE_IG_5, WsnFeatures.WSN_GRAYHOLE_IG_5, WsnFeatures.WSN_GRAYHOLE_IG_5, WsnFeatures.WSN_GRAYHOLE_IG_5});
        GeneralParameters.DATASET = GeneralParameters.WSN_DATASET;

//
    }

    public static void runIgWsn() throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_IG;
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static void runGrWsn() throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GR;
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static void runVndWsn() throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.J48};
        //GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPVND_J48;
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static void runGraspWsn() throws Exception {
        long time = System.currentTimeMillis();

        /* System.out.println("--- J48");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASP_BITFLIP_J48;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.J48};
        time = System.currentTimeMillis();
        runMultipleClassifier();
        System.out.println("--- END J48 (" + (System.currentTimeMillis() - time) + ")");

        
        System.out.println("--- Naive Bayes");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPP_BITFLIP_NaiveBayes;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.NAIVE_BAYES};
        time = System.currentTimeMillis();
        runMultipleClassifier();
        System.out.println("--- END Naive Bayes (" + (System.currentTimeMillis() - time) + ")");

        System.out.println("--- RANDOM_TREE");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPP_BITFLIP_RandomTree;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_TREE};
        time = System.currentTimeMillis();
        runMultipleClassifier();
        System.out.println("--- END RANDOM_TREE (" + (System.currentTimeMillis() - time) + ")");

        System.out.println("--- REP_TREE");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPP_BITFLIP_RepTree;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.REP_TREE};
        time = System.currentTimeMillis();
        runMultipleClassifier();
        System.out.println("--- END REP_TREE (" + (System.currentTimeMillis() - time) + ")");
         */
        System.out.println("--- RANDOM_FOREST WSN_GRASPP_BITFLIP_RandomForest");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPP_BITFLIP_RandomForest_5;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);

        System.out.println("--- RANDOM_FOREST WSN_IG");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_IG;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);
        System.out.println("--- END RANDOM_FOREST (" + (System.currentTimeMillis() - time) + ")");

        System.out.println("--- RANDOM_FOREST WSN_GR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GR;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);
        System.out.println("--- END RANDOM_FOREST (" + (System.currentTimeMillis() - time) + ")");

        System.out.println("--- RANDOM_FOREST WSN_GBDT");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GBDT;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        CrossValidation.runMultipleClassifier(10, 10);
        System.out.println("--- END RANDOM_FOREST (" + (System.currentTimeMillis() - time) + ")");

    }

}
