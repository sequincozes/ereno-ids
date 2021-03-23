/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSimple;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;
import weka.core.Instances;

/**
 * @author silvio
 */
public class ExtendedCIoT {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = true;
//        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_BLACKHOKE_GRASP_BITFLIP_J48_5;
//        GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.J48;

//        String normalFile = GeneralParameters.WSN_ATTACKS + "normal.csv";
//        String attackFile = GeneralParameters.WSN_ATTACKS + "blackhole.csv";
//        String classes[] = {normalFile, attackFile};
//        runWithExpertClassifiers(classes);


//        runWithExpertClassifiers();
//        runWithBinaryClassifiers();
        runWithExpertClassifiersSacanagem();
//        runWithBinaryMatrix();
    }

    public static void runWithExpertClassifiersSacanagem() throws Exception {
        System.out.println("--- Expert classifier analysis sacanagem");

        System.out.println("--- Expert classifier FLOODING");
        String forTrainOnly = GeneralParameters.WSN_ATTACKS + "WSN-DS-2class.csv";
        String forTestOnly = GeneralParameters.WSN_ATTACKS + "WSN-DS-5class.csv";
        String forTrainOnlys[] = new String[]{forTrainOnly};
        String forTestOnlys[] = new String[]{forTestOnly};

        for (int i = 0; i < 5; i++) {
            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Expert_TDMA[i];
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
            boolean printConfusionMatrix = true;
            Instances dataset = Util.loadAndFilter(forTrainOnlys, true);
            Instances datasetForTestOnly = Util.loadAndFilter(forTestOnlys, true);
            CrossValidation.runWithInstances(dataset, datasetForTestOnly, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);
        }

    }


    // Binary two-class analysis
    public static void runWithBinaryMatrix() throws Exception {
        String binaryFile = GeneralParameters.WSN_ATTACKS + "WSN-DS-2class.csv";
        String multiClassFile = GeneralParameters.WSN_ATTACKS + "WSN-DS-5class.csv";
        String[] classes = new String[]{binaryFile};
        String[] classesMulti = new String[]{multiClassFile};

        for (int i = 0; i < 5; i++) {
            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Binary[i];
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
            boolean printConfusionMatrix = true;
            Instances datasetBinary = Util.loadAndFilter(classes, true);
            Instances datasetMulticlass = Util.loadAndFilter(classesMulti, true);

            CrossValidation.runWithInstancesBinaryMatrix(datasetBinary, datasetMulticlass, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);

        }

    }

    public static void runWithExpertClassifiers() throws Exception {
        System.out.println("--- Expert classifier analysis");

//        System.out.println("--- Expert classifier GRAYHOLE");
//        String singleFile = GeneralParameters.WSN_ATTACKS + "normal_grayhole.csv";
//        String classes[] = new String[]{singleFile};
//        for (int i = 0; i < 5; i++) {
//            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Expert_Grayhole[i];
//            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
//            boolean printConfusionMatrix = false;
//            Instances dataset = Util.loadAndFilter(classes, true);
//            CrossValidation.runWithInstances(dataset, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);
//        }

        System.out.println("--- Expert classifier BLACKHOLE");
        String singleFile = GeneralParameters.WSN_ATTACKS + "normal_blackhole.csv";
        String classes[] = new String[]{singleFile};

        for (int i = 0; i < 5; i++) {
            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Expert_Blackhole[i];
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
            boolean printConfusionMatrix = false;
            Instances dataset = Util.loadAndFilter(classes, true);
            CrossValidation.runWithInstances(dataset, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);
        }

        System.out.println("--- Expert classifier FLOODING");
        singleFile = GeneralParameters.WSN_ATTACKS + "normal_flooding.csv";
        classes = new String[]{singleFile};

        for (int i = 0; i < 5; i++) {
            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Expert_Flooding[i];
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
            boolean printConfusionMatrix = false;
            Instances dataset = Util.loadAndFilter(classes, true);
            CrossValidation.runWithInstances(dataset, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);
        }
    }

    // Binary two-class analysis
    public static void runWithBinaryClassifiers() throws Exception {
        System.out.println("--- Binary classifier analysis");
        String singleFile = GeneralParameters.WSN_ATTACKS + "WSN-DS-2class.csv";
        String[] classes = new String[]{singleFile};
        for (int i = 0; i < 5; i++) {
            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Binary[i];
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
            boolean printConfusionMatrix = false;
            Instances dataset = Util.loadAndFilter(classes, true);
            CrossValidation.runWithInstances(dataset, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);
        }
    }

    // Multi-class analysis
    public static void runWithMulticlassClassifiers() throws Exception {
//        String singleFile = GeneralParameters.WSN_ATTACKS + "normal_tdma.csv";
        String multiClassFile = GeneralParameters.WSN_ATTACKS + "WSN-DS-5class.csv";

        String[] classes = new String[]{multiClassFile};
        System.out.println("--- Multiclass classifier analysis");
        for (int i = 0; i < 5; i++) {
            GeneralParameters.FEATURE_SELECTION = FeatureSubsets.Multiclass[i];
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.allCustom[i];
            boolean printConfusionMatrix = true;
            Instances dataset = Util.loadAndFilter(classes, true);
            CrossValidation.runWithInstances(dataset, GeneralParameters.FEATURE_SELECTION, GeneralParameters.SINGLE_CLASSIFIER_MODE, printConfusionMatrix);
        }
    }

}
