/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.featureSelection;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

public class Parameters {

    public static String LEGACY_TRAIN;
    public static String LEGACY_TEST;
    public static boolean VERBOSE = true;
    
    // Directory
    //public static final String DIRETORIO = "/home/silvio/datasets/Full_SV_2020/consistency_v2/";
    public static final String DIRETORIO = "/home/silvio/datasets/Full_SV_2020/consistency_v2/";
    public static final String NORMAL_CLASS = "uc00_fullgoose";

    // Files
    //public static final String NORMAL_CLASS = "uc00_fullgoose_multiclass";
    public static String MULTI_CLASS_UC01 = "uc01_fullgoose_multiclass";
    public static String MULTI_CLASS_UC02 = "uc02_fullgoose_multiclass";
    public static String MULTI_CLASS_UC03 = "uc03_fullgoose_multiclass";
    public static String MULTI_CLASS_UC04 = "uc04_fullgoose_multiclass";
    public static String MULTI_CLASS_UC05 = "uc05_fullgoose_multiclass";
    public static String MULTI_CLASS_UC06 = "uc06_fullgoose_multiclass";

//    public static final String NORMAL_CLASS = "uc00multmini";
//    public static String MULTI_CLASS_UC01 = "uc01multmini";
//    public static String MULTI_CLASS_UC02 = "uc02multmini";
//    public static String MULTI_CLASS_UC03 = "uc03multmini";
//    public static String MULTI_CLASS_UC04 = "uc04multmini";
//    public static String MULTI_CLASS_UC05 = "uc05multmini";
    public static String BINARTY_ATTACK_CLASS = "uc06_fullgoose";

    public static final String SEPARATOR = "/";

    static String getAttackFile() {
        return DIRETORIO + BINARTY_ATTACK_CLASS + ".csv";
    }

    public static final String NORMAL_FILE = DIRETORIO + NORMAL_CLASS + ".csv";

    public static final String MULTICLASS_FILES[] = {
        DIRETORIO + MULTI_CLASS_UC01 + ".csv",
        DIRETORIO + MULTI_CLASS_UC02 + ".csv",
        DIRETORIO + MULTI_CLASS_UC03 + ".csv",
        DIRETORIO + MULTI_CLASS_UC04 + ".csv",
        DIRETORIO + MULTI_CLASS_UC05 + ".csv",
        DIRETORIO + MULTI_CLASS_UC06 + ".csv"
    };

    public static final ClassifierExtended RANDOM_TREE = new ClassifierExtended(true, new RandomTree(), "RandomTree");
    public static final ClassifierExtended RANDOM_FOREST = new ClassifierExtended(true, new RandomForest(), "RandomForest");
    public static final ClassifierExtended NAIVE_BAYES = new ClassifierExtended(true, new NaiveBayes(), "NaiveBayes");
    public static final ClassifierExtended REP_TREE = new ClassifierExtended(true, new REPTree(), "REPTree");
    public static final ClassifierExtended J48 = new ClassifierExtended(true, new J48(), "J48");
    public static final ClassifierExtended KNN = new ClassifierExtended(true, new IBk(), "KNN");

    // Run Settings
    public static ClassifierExtended[] CLASSIFIERS_FOREACH = {REP_TREE, RANDOM_TREE, J48, RANDOM_FOREST, NAIVE_BAYES};// KNN está fora
    //public static final ClassifierExtended[] CLASSIFIERS_FOREACH = {REP_TREE};// KNN está fora
    public static final boolean NORMALIZE = false;

    static int[] susbtation1 = new int[]{2, 3, 4, 8, 9, 10, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34};
    static int[] susbtation2 = new int[]{5, 6, 7, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34};
    static int[] ambasGoose = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34};
    static int[] ambasSvGooseConsistency = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    static int[] gooseOnly = new int[]{2, 3, 4, 8, 9, 10, 15, 16, 17};
    static int[] injectionRelated = new int[]{19, 20, 21, 23, 25, 26, 28, 29, 31, 34};

    static int[] full = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};
    static int[] test1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, /*16,*/ 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};
    static int[] test2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, /*16,*/ 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, /*35,*/ 36, 37, 38, 39, 40, 41, /*42*/};

    static int[] FEATURE_SELECTION = test2;//ambas;
    public static final int TOTAL_FEATURES = FEATURE_SELECTION.length;

}
