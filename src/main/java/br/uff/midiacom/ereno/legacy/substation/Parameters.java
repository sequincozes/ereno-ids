/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.legacy.substation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
//import weka.classifiers.trees.NBTree;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

/**
 *
 * @author silvio
 */
public class Parameters {
//    public static final String DIRETORIO = "/home/silvio/datasets/wsn-ds/";
//    public static final String ataque = DIRETORIO + "flooding/";

    public static final String DIRETORIO = "/home/silvio/datasets/";
//    public static final String ataque = DIRETORIO + "SV2020/";
    public static final String ataque = DIRETORIO + "GOOSExSV/";

    public static final String SEPARATOR = "/";
//    public static final String FILE_TRAIN = DIRETORIO + SEPARATOR + "10_train_files" + SEPARATOR + "compilado_train.csv"; //treino_binario_1000_1000
//    public static final String FILE_EVALUATION = DIRETORIO + SEPARATOR + "10_evaluation_files" + SEPARATOR + "compilado_evaluation.csv"; //ataque_binario_10k
//    public static final String FILE_TEST = DIRETORIO + SEPARATOR + "80_test_files" + SEPARATOR + "compilado_test.csv"; //ataque_binario_10k

    static final String TRAIN_FILE = ataque + "train_5.csv";
    static final String TEST_FILE = ataque + "test_95.csv";
    static final String NORMAL_FILE = ataque + "normal_test_95.csv";
    public static final ClassifierExtended RANDOM_TREE = new ClassifierExtended(true, new RandomTree(), "RandomTree");
    public static final ClassifierExtended RANDOM_FOREST = new ClassifierExtended(true, new RandomForest(), "RandomForest");
    public static final ClassifierExtended NAIVE_BAYES = new ClassifierExtended(true, new NaiveBayes(), "NaiveBayes");
    public static final ClassifierExtended REP_TREE = new ClassifierExtended(true, new REPTree(), "REPTree");
    public static final ClassifierExtended J48 = new ClassifierExtended(true, new J48(), "J48");
    public static final ClassifierExtended KNN = new ClassifierExtended(true, new IBk(), "KNN");
//    public static final ClassifierExtended NBTREE = new ClassifierExtended(true, new NBTree(), "NBTree");

    // Run Settings
    public static final ClassifierExtended[] CLASSIFIERS_FOREACH = {RANDOM_TREE, J48, RANDOM_FOREST, REP_TREE, NAIVE_BAYES};// KNN está fora
    public static final boolean TEST_NORMALS = false;
    public static final boolean TEST_ATTACKS = true;
    public static final int TOTAL_FEATURES = 10;
    public static final boolean NORMALIZE = false;

    //Selected by statoc
    static int[] STATIC_FEATURE_SELECTION = new int[]{1,2,3,4,5,6,7,8,9,10};
    static int[] STATIC_FEATURE_SELECTION_2 = new int[]{4,7,9,10};
    
    
    static int[] OneR5WSN = new int[]{18, 7, 6, 1, 4};
    static int[] IG5WSN = new int[]{18, 6, 3, 7, 13};
    static int[] GR5WSN = new int[]{3, 6, 10, 9, 13};
    static int[] OneR18WSN = new int[]{18, 7, 6, 1, 4, 2, 3, 5, 10, 11, 12, 13, 15, 16, 17, 14, 9, 8};//
    static int[] IG18WSN = new int[]{18, 6, 3, 7, 13, 12, 8, 17, 5, 10, 9, 14, 4, 1, 11, 15, 16, 2};
    static int[] GR18WSN = new int[]{3, 6, 10, 9, 13, 18, 7, 12, 8, 17, 5, 11, 15, 16, 14, 4, 1, 2};
    static int[] OneR10WSN = new int[]{18, 7, 6, 1, 4, 2, 3, 5, 10, 11};//, 12, 13, 15, 16, 17, 14, 9, 8};//
    static int[] IG10WSN = new int[]{18, 6, 3, 7, 13, 12, 8, 17, 5, 10};//, 9, 14, 4, 1, 11, 15, 16, 2};
    static int[] GR10WSN = new int[]{3, 6, 10, 9, 13, 18, 7, 12, 8, 17};//, 5, 11, 15, 16, 14, 4, 1, 2};

    static int[] FEATURE_SELECTION = STATIC_FEATURE_SELECTION_2;

}
