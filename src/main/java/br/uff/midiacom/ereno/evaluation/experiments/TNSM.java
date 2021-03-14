/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;

import static br.uff.midiacom.ereno.evaluation.CrossValidation.runSingleClassifier;
import static br.uff.midiacom.ereno.evaluation.CrossValidation.setupAndRun;

import br.uff.midiacom.ereno.featureSelection.subsets.CicidsFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.KddFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;
import weka.core.Instances;

/**
 * @author silvio
 */
public class TNSM {

    private static boolean simplePrint = true;

    // GR_G_BF, GR_G_VND, GR_G_RVND, I_G_VND, FULL, GR, IG, OneR
    public static void main(String[] args) throws Exception {
        GeneralParameters.DATASET = GeneralParameters.CICIDS_DATASET;
        GeneralParameters.NORMALIZE = true;
        GeneralParameters.PRINT_SELECTION = false;

        GeneralParameters.EVALUATION_SEED = 5;
        System.out.println("CICIDS GR_G_RVND (seed 5)");
        print(runWithCICIDS(Setup.GR_G_RVND)); //1, 4, 36, 40, 49, 62, 63, 67, 68

        GeneralParameters.EVALUATION_SEED = 7;
        System.out.println("CICIDS GR_G_RVND (seed 7)");
        print(runWithCICIDS(Setup.GR_G_RVND));
//
//
//        System.out.println("CICIDS F_G_VND");
//        print(runWithCICIDS(Setup.F_G_VND));
//
//        System.out.println("CICIDS F_G_RVND");
//        print(runWithCICIDS(Setup.F_G_RVND));


        // Classificar
//        GeneralParameters.DATASET = GeneralParameters.KDD_DATASET;

//        System.out.println("KDD F_G_VND");
//        print(runWithKDD(Setup.F_G_VND));

//        System.out.println("KDD F_G_RVND");
//        print(runWithKDD(Setup.F_G_RVND));


//
//        System.out.println("FULL");
//        print(run(Setup.FULL));
//
//        System.out.println("IG");
//        print(run(Setup.IG));

//        System.out.println("GR");
//        print(run(Setup.GR));

//        System.out.println("OneR");
//        print(run(Setup.OneR));
//
//        System.out.println("I_G_VND");
//        print(run(Setup.I_G_VND));


    }

    private static void print(GenericResultado[][] classifiersAndFolds) {
        for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
            // Imprimir Detalhes
            GenericResultado results = Util.getResultAverage(classifiersAndFolds[classifierIndex]);
            String strResults = results.printResultsGetString();

            if (simplePrint) {
                System.out.println(results.getCx() + " - F1: " + results.getF1Score() + " / Acc: " + results.getAcuracia());
            } else {
                // Imprimir Folds
                for (int fold = 0; fold < GeneralParameters.FOLDS; fold++) {
                    strResults = strResults.concat(classifiersAndFolds[classifierIndex][fold].getF1Score() + ";");
                }
                System.out.println(strResults);

            }
        }

    }

    public enum Setup {
        GR_G_BF, GR_G_VND, GR_G_RVND, I_G_VND, FULL, GR, IG, OneR, F_G_VND, F_G_RVND
    }

    public static GenericResultado[][] runWithKDD(Setup setup) throws Exception {
        GenericResultado[][] classifiersAndFolds = new GenericResultado[GenericClassifiers.all.length][GeneralParameters.FOLDS];

        Instances dataset = Util.loadSingleFile(false);
        Instances filteredDataset;
        switch (setup) {
            case F_G_VND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = KddFeatures.FULL_RCL_VND[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, false);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
            case F_G_RVND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = KddFeatures.FULL_RCL_RVND[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, false);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
            case GR_G_BF:
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.FEATURE_SELECTION = KddFeatures.GR_G_BF[classifierIndex];
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;
            case GR_G_VND:

//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_VND[classifierIndex].clone();
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }

                break;
            case GR_G_RVND:
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_RVND[classifierIndex];
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }

                break;
            case I_G_VND:
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.I_G_VND[classifierIndex].clone();
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }

                break;
            case FULL:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_FULL.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
//                break;
            case GR:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_GR.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;

            case IG:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_IG.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;

            case OneR:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_OneR.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;
        }
        return classifiersAndFolds;
    }

    public static GenericResultado[][] runWithCICIDS(Setup setup) throws Exception {
        GenericResultado[][] classifiersAndFolds = new GenericResultado[GenericClassifiers.all.length][GeneralParameters.FOLDS];

        Instances dataset = Util.loadSingleFile(false);
        Instances filteredDataset;
        switch (setup) {
            case F_G_VND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = CicidsFeatures.GRASP_VND_FULL_RCL_F1[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);

                }
                break;
            case F_G_RVND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = CicidsFeatures.GRASP_RVND_FULL_RCL_F1[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
            case GR_G_BF:
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.FEATURE_SELECTION = KddFeatures.GR_G_BF[classifierIndex];
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;
            case GR_G_VND:

//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_VND[classifierIndex].clone();
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }

                break;
            case GR_G_RVND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = CicidsFeatures.GR_G_RVND[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);

                }
                break;
            case I_G_VND:
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.I_G_VND[classifierIndex].clone();
//                    filteredDataset = Util.copyAndFilter(dataset, false);
//                    // Classifica
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }

                break;
            case FULL:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = CicidsFeatures.CICIDS_FULL;
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
            case GR:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_GR.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;

            case IG:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_IG.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;

            case OneR:
//                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_OneR.clone();
//                filteredDataset = Util.copyAndFilter(dataset, false);
//                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
//                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
//                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
//                }
                break;
        }
        return classifiersAndFolds;
    }


    public static GenericResultado[][] runWithWSN(Setup setup) throws Exception {
        GenericResultado[][] classifiersAndFolds = new GenericResultado[GenericClassifiers.all.length][GeneralParameters.FOLDS];

        Instances dataset = Util.loadSingleFile(false);
        Instances filteredDataset;
        switch (setup) {
            case GR_G_BF:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_BF[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
            case GR_G_VND:

                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_VND[classifierIndex].clone();
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }

                break;
            case GR_G_RVND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_RVND[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }

                break;
            case I_G_VND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.I_G_VND[classifierIndex].clone();
                    filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }

                break;
            case FULL:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_FULL.clone();
                filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
            case GR:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_GR.clone();
                filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;

            case IG:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_IG.clone();
                filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;

            case OneR:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_OneR.clone();
                filteredDataset = Util.copyAndFilter(dataset, GeneralParameters.PRINT_SELECTION);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, GeneralParameters.EVALUATION_SEED);
                }
                break;
        }
        return classifiersAndFolds;
    }

}
