/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import static br.uff.midiacom.ereno.evaluation.CrossValidation.runSingleClassifier;
import static br.uff.midiacom.ereno.evaluation.CrossValidation.setupAndRun;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class Repeticao {

    // GR_G_BF, GR_G_VND, GR_G_RVND, I_G_VND, FULL, GR, IG, OneR
    public static void main(String[] args) throws Exception {
        // Classificar   
        GeneralParameters.DATASET = GeneralParameters.WSN_2CLASS;
//        System.out.println("GR_G_BF");
//        print(run(Setup.GR_G_BF));
//
        System.out.println("GR_G_VND");
        print(run(Setup.GR_G_VND));

        System.out.println("GR_G_RVND");
        print(run(Setup.GR_G_RVND));
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

            // Imprimir Folds
            for (int fold = 0; fold < GeneralParameters.FOLDS; fold++) {
                strResults = strResults.concat(classifiersAndFolds[classifierIndex][fold].getF1Score() + ";");
            }
            System.out.println(strResults);
        }

    }

    public enum Setup {
        GR_G_BF, GR_G_VND, GR_G_RVND, I_G_VND, FULL, GR, IG, OneR
    }

    public static GenericResultado[][] run(Setup setup) throws Exception {
        GenericResultado[][] classifiersAndFolds = new GenericResultado[GenericClassifiers.all.length][GeneralParameters.FOLDS];

        Instances dataset = Util.loadSingleFile(false);
        Instances filteredDataset;
        switch (setup) {
            case GR_G_BF:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_BF[classifierIndex];
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, false);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }
                break;
            case GR_G_VND:

                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_VND[classifierIndex].clone();
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, false);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }

                break;
            case GR_G_RVND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.GR_G_RVND[classifierIndex];
                    filteredDataset = Util.copyAndFilter(dataset, false);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }

                break;
            case I_G_VND:
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    GeneralParameters.FEATURE_SELECTION = WsnFeatures.I_G_VND[classifierIndex].clone();
                    filteredDataset = Util.copyAndFilter(dataset, false);
                    // Classifica
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }

                break;
            case FULL:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_FULL.clone();
                filteredDataset = Util.copyAndFilter(dataset, false);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }
                break;
            case GR:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_GR.clone();
                filteredDataset = Util.copyAndFilter(dataset, false);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }
                break;

            case IG:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_IG.clone();
                filteredDataset = Util.copyAndFilter(dataset, false);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }
                break;

            case OneR:
                GeneralParameters.FEATURE_SELECTION = WsnFeatures.WSN_OneR.clone();
                filteredDataset = Util.copyAndFilter(dataset, false);
                for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
                    GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.all[classifierIndex];
                    classifiersAndFolds[classifierIndex] = runSingleClassifier(filteredDataset, GeneralParameters.FOLDS, 7);
                }
                break;
        }
        return classifiersAndFolds;
    }

}
