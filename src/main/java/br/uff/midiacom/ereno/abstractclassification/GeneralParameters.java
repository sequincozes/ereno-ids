/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.abstractclassification;

import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;

public class GeneralParameters {

    public static boolean DEBUG_MODE = false;

    public static boolean CSV = false;
    public static String WSN_DATASET = "all_in_one_wsn.csv";
    public static String WSN_DATASET_MINI = "all_mini_one_wsn.csv";
    public static String TESTE_DATASET = "all_in_one_wsn.csv";
    public static String CICIDS_DATASET = "all_in_one_cicids.csv";
    public static String KDD_DATASET = "all_in_one_kdd.csv";
    public static String CONSISTENCY_DATASET = "/home/silvio/datasets/Full_SV_2020/"
            + "consistency_v2/uc00_to_uc05_fullgoose_multiclass.csv";

    public static String DATASET = CONSISTENCY_DATASET;

    public static String WSN_5CLASS = "/home/silvio/datasets/wsn-ds/WSN-DS-5class.csv";
    public static String WSN_2CLASS = "/home/silvio/datasets/wsn-ds/WSN-DS-2class.csv";

    // GRASP
    public static String OUTPUT = "outputs/";
    public static int FOLDS = 5;
    public static int GRASP_SEED = 5;

    // Run Settings java 
//  java -jar preGrasp.jar all_in_one_WSN.csv 4 >> pregrasp_WSN_RANDOM_FOREST.txt
    public static String[] DATASETS_FOREACH = {
        CICIDS_DATASET,
        KDD_DATASET,
        WSN_DATASET
    };

    public static int[][] DATASETS_FEATURES_FOREACH = {
        FeatureSubsets.CICIDS_FULL,
        FeatureSubsets.KDD_FULL,
        FeatureSubsets.WSN_FULL
    };

    public static ClassifierExtended[] CLASSIFIERS_FOREACH = {
        GenericClassifiers.RANDOM_TREE,
        GenericClassifiers.J48,
        GenericClassifiers.REP_TREE,
        GenericClassifiers.NAIVE_BAYES,
        GenericClassifiers.RANDOM_FOREST
    };// KNN está fora

    public static ClassifierExtended SINGLE_CLASSIFIER_MODE = GenericClassifiers.RANDOM_FOREST;
    public static final boolean VERBOSO = true;

    public static final boolean NORMALIZE = false;

    public static int[] FEATURE_SELECTION = {};// FeatureSubsets.RCL_CICIDS_IG;
    static int NUM_CLASSES = 6;

    public static int getTotalFeatures() {
        return FEATURE_SELECTION.length;
    }

}
