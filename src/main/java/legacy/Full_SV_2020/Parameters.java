/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package legacy.Full_SV_2020;

import AbstractClassification.ClassifierExtended;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

public class Parameters {

    public static String ALL_IN_ONE_FILE = "/home/silvio/datasets/Full_SV_2020/full_compilation.csv";

    public static final ClassifierExtended RANDOM_TREE = new ClassifierExtended(true, new RandomTree(), "RandomTree");
    public static final ClassifierExtended RANDOM_FOREST = new ClassifierExtended(true, new RandomForest(), "RandomForest");
    public static final ClassifierExtended NAIVE_BAYES = new ClassifierExtended(true, new NaiveBayes(), "NaiveBayes");
    public static final ClassifierExtended REP_TREE = new ClassifierExtended(true, new REPTree(), "REPTree");
    public static final ClassifierExtended J48 = new ClassifierExtended(true, new J48(), "J48");
    public static final ClassifierExtended KNN = new ClassifierExtended(true, new IBk(), "KNN");

    // Run Settings
    public static final ClassifierExtended[] CLASSIFIERS_FOREACH = {RANDOM_TREE, J48, REP_TREE, NAIVE_BAYES, RANDOM_FOREST};// KNN está fora
    public static final int TOTAL_FEATURES = 13;
    public static final boolean NORMALIZE = true;

    static int[] ALL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    static int[] FEATURE_SELECTION = ALL;

}
