/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.crossvalidation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.FeatureSubsets;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class CrossValidation {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = false;

        /*System.out.println("CICIDS GR");
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_TREE, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.J48, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.REP_TREE, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.NAIVE_BAYES, FeatureSubsets.CICIDS_GR)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_FOREST, FeatureSubsets.CICIDS_GR)).printResults();
        */
        System.out.println("CICIDS IG/OneR");
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_TREE, FeatureSubsets.CICIDS_IG_RCL)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.J48, FeatureSubsets.CICIDS_IG_RCL)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.REP_TREE, FeatureSubsets.CICIDS_IG_RCL)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.NAIVE_BAYES, FeatureSubsets.CICIDS_IG_RCL)).printResults();
        Util.getResultAverage(setupAndRun(5, 7, GenericClassifiers.RANDOM_FOREST, FeatureSubsets.CICIDS_IG_RCL)).printResults();
        


        System.exit(0);

        //Util.printAverageResults(runMultipleClassifier(5, 7));
    }

    public static GenericResultado[] setupAndRun(int folds, int seed, ClassifierExtended classifier, int[] fs) throws Exception {
        GeneralParameters.SINGLE_CLASSIFIER_MODE = classifier;
        GeneralParameters.FEATURE_SELECTION = fs;
        Instances allInstances = Util.loadAndFilterSingleFile(false); // mudar para o grasp
        //allInstances = Util.copyAndFilter(allInstances, true);
        return runSingleClassifier(allInstances, folds, seed);
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