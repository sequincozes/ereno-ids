/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstractClassification;

public class GeneralParameters {

    public static boolean CSV = false;
    public static String ALL_IN_ONE_FILE = "/home/silvio/datasets/Full_SV_2020/full_compilation.csv";
    public static String OUTPUT = "outputs/";

    // Run Settings
    public static ClassifierExtended[] CLASSIFIERS_FOREACH = {
        GenericClassifiers.RANDOM_TREE,
        GenericClassifiers.J48,
        GenericClassifiers.REP_TREE,
        GenericClassifiers.NAIVE_BAYES,
        GenericClassifiers.RANDOM_FOREST
    };// KNN está fora

    public static ClassifierExtended SINGLE_CLASSIFIER_MODE = GenericClassifiers.REP_TREE;
    public static final boolean VERBOSO = true;

    public static final int TOTAL_FEATURES = 18;
    public static final boolean NORMALIZE = false;

    public static int[] ALL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

    public static int[] FEATURE_SELECTION = FeatureSubsets.WSN_GBDT;

}
