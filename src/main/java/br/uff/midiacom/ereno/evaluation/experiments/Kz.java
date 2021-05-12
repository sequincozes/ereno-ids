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
public class Kz {

    public static void main(String[] args) throws Exception {
        // Manual Run
        GeneralParameters.CSV = true;


        GenericClassifiers.allCustom = new ClassifierExtended[]{
                GenericClassifiers.J48,
                GenericClassifiers.RANDOM_TREE,
                GenericClassifiers.REP_TREE
        };


        GeneralParameters.DATASET = GeneralParameters.WSN_NORMAL_GRAYHOLE;
        System.out.println(GeneralParameters.DATASET);
        CrossValidation.printFolds(WsnFeatures.WSN_GR);
        System.out.println("OneR");
        CrossValidation.printFolds(WsnFeatures.WSN_OneR);
        System.out.println("IG");
        CrossValidation.printFolds(WsnFeatures.WSN_IG);
    }


}
