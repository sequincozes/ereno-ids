/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossvalidation;

import AbstractClassification.ClassifierExtended;
import AbstractClassification.FeatureSubsets;
import AbstractClassification.GeneralParameters;
import AbstractClassification.GenericClassifiers;
import AbstractClassification.GenericEvaluation;
import AbstractClassification.GenericResultado;
import AbstractClassification.Util;
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class CrossValidation {

    public static void main(String[] args) throws Exception {
        //      GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.NAIVE_BAYES;
//        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GeneralParameters.SINGLE_CLASSIFIER_MODE};
//        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_IG;

        GeneralParameters.CSV = false;
        System.out.println("------BLACKGOLE IG");
        GeneralParameters.ALL_IN_ONE_FILE = "/home/silvio/datasets/wsn-ds/blackhole/all_in_one_blackhole.csv";
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_IG;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------BLACKGOLE GR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_GR;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------BLACKGOLE OneR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_ONER;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------GRAYHOLE IG");
        GeneralParameters.ALL_IN_ONE_FILE = "/home/silvio/datasets/wsn-ds/grayhole/all_in_one_grayhole.csv";
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_IG;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------GRAYHOLE GR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_GR;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------GRAYHOLE OneR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_ONER;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------FLOODING IG");
        GeneralParameters.ALL_IN_ONE_FILE = "/home/silvio/datasets/wsn-ds/flooding/all_in_one_flooding.csv";
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_IG;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------FLOODING GR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_GR;
        Util.printAverageResults(runMultipleClassifier(7, 5));

        System.out.println("------FLOODING OneR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOLE_ONER;
        Util.printAverageResults(runMultipleClassifier(7, 5));

    }

    public static void justRun(int folds) throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        Instances allInstances = Util.loadAndFilterSingleFile(true); // mudar para o grasp
        allInstances = Util.copyAndFilter(allInstances, true);
        runSingleClassifier(allInstances, folds, 10);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static GenericResultado[] runSingleClassifier(Instances allInstances, int totalFolds, int seed) throws Exception {
        //System.out.println("Last: " + allInstances.get(0).toString());
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator

        GenericResultado[] resultsCompilation = new GenericResultado[totalFolds];
        for (int fold = 0; fold < totalFolds; fold++) {
            Instances train = randData.trainCV(totalFolds, fold, rand);
            Instances test = randData.testCV(totalFolds, fold);
            resultsCompilation[fold] = GenericEvaluation.runSingleClassifier(train, test);
        }

        return resultsCompilation;
    }

    public static GenericResultado[][] runMultipleClassifier(int seed, int totalFolds) throws Exception {
        Instances allInstances = Util.loadAndFilterSingleFile(true);
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator

        GenericResultado[][] resultsCompilation = new GenericResultado[totalFolds][GeneralParameters.CLASSIFIERS_FOREACH.length];
        System.out.print("--- FOLD: ");
        for (int fold = 0; fold < totalFolds; fold++) {
            System.out.print("[" + fold + "]");

            Instances train = randData.trainCV(totalFolds, fold, rand);
            Instances test = randData.testCV(totalFolds, fold);
            GenericResultado[] classifiersResults = GenericEvaluation.runMultipleClassifier(train, test);
            int classifierIndex = 0;
            for (GenericResultado classifierResult : classifiersResults) {
                resultsCompilation[fold][classifierIndex++] = classifierResult;
            }
        }
        System.out.println();

        return resultsCompilation;
    }

    public static void runIgWsn() throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_IG;
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static void runGrWsn() throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GR;
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static void runVndWsn() throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.J48};
        //GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPVND_J48;
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);
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
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPP_BITFLIP_RandomForest;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);

        System.out.println("--- RANDOM_FOREST WSN_IG");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_IG;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);
        System.out.println("--- END RANDOM_FOREST (" + (System.currentTimeMillis() - time) + ")");

        System.out.println("--- RANDOM_FOREST WSN_GR");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GR;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);
        System.out.println("--- END RANDOM_FOREST (" + (System.currentTimeMillis() - time) + ")");

        System.out.println("--- RANDOM_FOREST WSN_GBDT");
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GBDT;
        GeneralParameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]{GenericClassifiers.RANDOM_FOREST};
        time = System.currentTimeMillis();
        runMultipleClassifier(10, 10);
        System.out.println("--- END RANDOM_FOREST (" + (System.currentTimeMillis() - time) + ")");

    }

}
