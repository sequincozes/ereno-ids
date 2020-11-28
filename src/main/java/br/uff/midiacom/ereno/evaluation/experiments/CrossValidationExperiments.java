/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.subsets.CicidsFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.FullConsistencySubset;
import br.uff.midiacom.ereno.featureSelection.subsets.KddFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;

/**
 *
 * @author silvio
 */
public class CrossValidationExperiments {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = true;
        fullConsistencyV2CrossValidation();
//        wsnCrossValidation();
    }

    public static void wsnCrossValidation() throws Exception {
        boolean printConfusionMatrix = false;
        System.out.println("--- WSN_FULL");
        CrossValidation.printFoldResults(WsnFeatures.WSN_IG, 0, printConfusionMatrix);
        CrossValidation.printFoldResults(WsnFeatures.WSN_IG, 1, printConfusionMatrix);
        CrossValidation.printFoldResults(WsnFeatures.WSN_IG, 2, printConfusionMatrix);
        CrossValidation.printFoldResults(WsnFeatures.WSN_IG, 3, printConfusionMatrix);
        CrossValidation.printFoldResults(WsnFeatures.WSN_IG, 4, printConfusionMatrix);
    }

    private static void fullConsistencyV2CrossValidation() throws Exception {

        boolean printConfusionMatrix = true;
        System.out.println("--- FULL_GOOSE");

        CrossValidation.printFoldResults(FullConsistencySubset.FULL, 0, printConfusionMatrix);
        CrossValidation.printFoldResults(FullConsistencySubset.FULL, 1, printConfusionMatrix);
        CrossValidation.printFoldResults(FullConsistencySubset.FULL, 2, printConfusionMatrix);
        CrossValidation.printFoldResults(FullConsistencySubset.FULL, 3, printConfusionMatrix);
        CrossValidation.printFoldResults(FullConsistencySubset.FULL, 4, printConfusionMatrix);
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
