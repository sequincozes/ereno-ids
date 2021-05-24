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
//        GeneralParameters.DATASET = "/home/silvio/datasets/wsn-ds/clustering2021/normal_grayhole_80percent_train.csv";
//        reduceInstances();
//        GeneralParameters.DATASET = "/home/silvio/datasets/wsn-ds/clustering2021/normal_flooding_80percent_train.csv";
//        reduceInstances();
//        GeneralParameters.DATASET = "/home/silvio/datasets/wsn-ds/clustering2021/normal_blackhole_80percent_train.csv";
//        reduceInstances();


        // Manual Run
//        GeneralParameters.CSV = true;
//        GenericClassifiers.allCustom = new ClassifierExtended[]{
//                GenericClassifiers.J48,
//                GenericClassifiers.RANDOM_TREE,
//                GenericClassifiers.REP_TREE
//        };
//
//
        GeneralParameters.DATASET = GeneralParameters.WSN_NORMAL_BLACKHOLE;
        System.out.println(GeneralParameters.DATASET);
//        CrossValidation.printFolds(WsnFeatures.WSN_GR);
     reduceInstances(5,0);
//        reduceInstances(5,2);
//        reduceInstances(5,3);
//        reduceInstances(5,4);
//

//        System.out.println("OneR");
//        CrossValidation.printFolds(WsnFeatures.WSN_OneR);
//        System.out.println("IG");
//        CrossValidation.printFolds(WsnFeatures.WSN_IG);
    }

    public static void reduceInstances(int totalFolds, int fold) throws IOException {
        Instances allInstances = Util.cutFold(totalFolds, fold, 7, new String[]{GeneralParameters.DATASET});
        Util.writeInstancesToFile(allInstances, GeneralParameters.DATASET.replace(".csv", "_"+totalFolds+"folds-"+fold+".csv"));        System.out.println("Tamanho: " + allInstances.size());
    }

}
