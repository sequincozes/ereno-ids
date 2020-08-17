/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.FeatureSubsets;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import java.util.ArrayList;
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class CrossValidation {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = false;

        Util.getResultAverage(setupAndRun(5, 5, GenericClassifiers.J48, new int[]{1, 6, 10, 16, 24, 25, 33, 34, 39, 44, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 67, 68, 72, 75})).printResults();

        /*System.out.println("CICIDS GR");
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_TREE, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.J48, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.REP_TREE, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.NAIVE_BAYES, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_FOREST, FeatureSubsets.CICIDS_GR)).printResults();
         */
        //System.out.println("CICIDS IG/OneR");
        //Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_TREE, FeatureSubsets.RCL_CICIDS_IG)).printResults();
        //Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.J48, FeatureSubsets.RCL_CICIDS_IG)).printResults();
        //Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.REP_TREE, FeatureSubsets.RCL_CICIDS_IG)).printResults();
        //Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.NAIVE_BAYES, FeatureSubsets.WSN_GR)).printResults();
        //Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_FOREST, FeatureSubsets.RCL_CICIDS_IG)).printResults();
        // System.exit(0);
        //Util.printAverageResults(runMultipleClassifier(5, 7)); ancienttulip189
    }

    public static GenericResultado[] setupAndRun(int folds, int seed, ClassifierExtended classifier, int[] fs) throws Exception {
        GeneralParameters.SINGLE_CLASSIFIER_MODE = classifier;
        GeneralParameters.FEATURE_SELECTION = fs;
        Instances allInstances = Util.loadAndFilterSingleFile(false); // mudar para o grasp
        GenericResultado[] result = runSingleClassifier(allInstances, folds, seed);
        return result;
    }

    public static void justRun(int folds, int seed) throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        Instances allInstances = Util.loadAndFilterSingleFile(true); // mudar para o grasp
        allInstances = Util.copyAndFilter(allInstances, true);
        runSingleClassifier(allInstances, folds, seed);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static GenericResultado[] runSingleClassifier(Instances allInstances, int totalFolds, int seed) throws Exception {
        //System.out.println("allInstances: " + allInstances.size());
        //System.out.println("Last: " + allInstances.get(0).toString());
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        GenericResultado[] resultsCompilation = new GenericResultado[totalFolds];
        float totalTrain = 0;
        float totalTest = 0;

        long begin = System.currentTimeMillis();
        for (int fold = 0; fold < totalFolds; fold++) {
            /* Tempo de treinamento */
            long beginTimestamp = System.nanoTime();
            Instances train = randData.trainCV(totalFolds, fold, rand);
            long trainigTimestamp = System.nanoTime();
            totalTrain = totalTrain + ((trainigTimestamp - beginTimestamp) / train.size());
            //System.out.println("Total Time:" + (System.currentTimeMillis() - begin));

            /* Tempo de teste */
            // long begin = System.currentTimeMillis();
            Instances test = randData.testCV(totalFolds, fold);
            long evaluationTimestamp = System.nanoTime();
            totalTest = totalTest + ((evaluationTimestamp - trainigTimestamp) / test.size());
            resultsCompilation[fold] = GenericEvaluation.runSingleClassifier(train, test);
        }
        long end = System.currentTimeMillis();

        totalTrain = (totalTrain / totalFolds);
        totalTest = (totalTest / totalFolds);
        //totalTrain = 1 / ((totalTrain) / 1000000 / 5);
        //totalTest =  1 / ((totalTest) / 1000000 / 5);
        System.out.println(allInstances.get(0).numAttributes() + ";" + totalTrain + ";" + totalTest + ";" + (end - begin) / totalFolds);

        return resultsCompilation;
    }

    public static GenericResultado[][] runMultipleClassifier(int totalFolds, int seed) throws Exception {
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
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_GRASPP_BITFLIP_RandomForest_5;
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

    private static int[] getArray(ArrayList<Integer> fs) {
        int[] array = new int[fs.size()];
        for (int i = 0; i < fs.size(); i++) {
            array[i] = fs.get(i);
        }
        return array;
    }

}
