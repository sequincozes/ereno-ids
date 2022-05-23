package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.*;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSimple;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;


public class SBSeg2022 {
    public static void main(String[] args) throws Exception {
        String train = "/home/silvio/datasets/sbseg2022/train.csv";
        String test = "/home/silvio/datasets/sbseg2022/test.csv";
        GeneralParameters.NUM_CLASSES = 2;
        GeneralParameters.FEATURE_SELECTION = new int[]{2, 4};//new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        GeneralParameters.CROSS_VALIDATION = false;
        Util.loadTrainFile(train);
        Util.loadTestFile(test);

        GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.KNN;
        GeneralParameters.TRAIN = Util.applyFilterKeep(GeneralParameters.TRAIN);
        GeneralParameters.TEST = Util.applyFilterKeep(GeneralParameters.TEST);
        GenericResultado result = GenericEvaluation.runSingleClassifierJ48(GeneralParameters.TRAIN, GeneralParameters.TEST);
        result.printResults();

//        GeneralParameters.UNIFIED_DATASET = "/home/silvio/datasets/sbseg2022/unified.csv";
//
//        GraspSimple grasp = new GraspSimple();
//        grasp.run(GeneralParameters.FEATURE_SELECTION,
//                "GRASP Simple",
//                NeighborhoodStructures.IWSSR,
//                GeneralParameters.UNIFIED_DATASET
//        );

    }

}
