/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking.METODO;
import br.uff.midiacom.ereno.featureSelection.Util;
import br.uff.midiacom.ereno.featureSelection.subsets.CicidsFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;
import br.uff.midiacom.ereno.featureSelection.subsets.KddFeatures;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;
import weka.core.Instances;

import java.io.IOException;

/**
 * @author silvio
 */
public class ExperimentosVinicius {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = true;
        GeneralParameters.DATASET = GeneralParameters.KDD_DATASET;
        GeneralParameters.FOLDS = 5 ;

//12, 26, 4, 25, 39, 30, 38, 6, 29, 5, 3, 37, 33, 34, 35, 31, 28, 27, 8, 41, 32, 23, 10, 36, 13, 2, 15, 11, 19, 40, 16, 21, 1, 17, 7, 24, 14, 22, 9
        int[] completo = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        int[] top5 = new int[]{12, 26, 4, 25, 39};
        int[] top10 = new int[]{12, 26, 4, 25, 39, 30, 38, 6, 29, 5};


        // Para aplicar o filtro
//        FeatureRanking.avaliarESelecionarFromGeneralParamter(40, FeatureRanking.METODO.GR, false);

        // Para classificar
        CrossValidation.runAndPrintFoldResults(top5, 2, false);
    }
}
