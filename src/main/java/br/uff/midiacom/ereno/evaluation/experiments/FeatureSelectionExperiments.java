/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking.METODO;
import br.uff.midiacom.ereno.featureSelection.Util;
import br.uff.midiacom.ereno.featureSelection.subsets.CicidsFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;
import br.uff.midiacom.ereno.featureSelection.subsets.KddFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;
import weka.core.Instances;

import java.io.IOException;

/**
 * @author silvio
 */
public class FeatureSelectionExperiments {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = true;

//        args[0] = "/home/silvio/datasets/Full_SV_2021/consistency_v4/uc0"+uc+"/uc00_uc0"+uc+"_1s.csv";
//        args[0] = "/home/silvio/datasets/Full_SV_2021/consistency_v4/uc0" + uc + "/uc00_uc0" + uc + ".csv";
//        args[0] = GeneralParameters.WSN_DATASET;

        METODO method = null;
        System.out.println("Usage: java -jar featureRanking.jar [dataset] [method]");
        System.out.println("Example: java -jar featureRanking.jar uc00_uc01 GR");
        String uc = args[0];
        if (args[1].equalsIgnoreCase("IG")) {
            method = METODO.IG;
        } else if (args[1].equalsIgnoreCase("GR")) {
            method = METODO.GR;
        } else if (args[1].equalsIgnoreCase("OneR")) {
            method = METODO.OneR;
        } else {
            System.out.println("Method must be IG, GR, or OneR");
            System.exit(1);
        }
//        System.out.println("uc00_uc0" + uc + "_"+method.name());
        System.out.println(uc + "_"+method.name());
        GeneralParameters.DATASET = uc;
        FeatureRanking.justRank(GeneralParameters.DATASET, method);
    }


    private static void fullConsistencyV4CrossValidation() throws Exception {
        int[] fs = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43};
        CrossValidation.printFolds(fs);
    }

    public static void reduceInstances() throws IOException {
        Instances allInstances = Util.cutFold(10, 1, 7, new String[]{
                GeneralParameters.DATASET});
        System.out.println("Tamanho: " + allInstances.size());
        Util.writeInstancesToFile(allInstances, GeneralParameters.CONSISTENCY_DATASET.replace(".csv", "_10percent.csv"));
    }

    public static void wsnCrossValidation() throws Exception {
        boolean printConfusionMatrix = false;
        System.out.println("--- WSN_FULL");
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_IG, 0, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_IG, 1, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_IG, 2, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_IG, 3, printConfusionMatrix);
        CrossValidation.runAndPrintFoldResults(WsnFeatures.WSN_IG, 4, printConfusionMatrix);
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
