package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.*;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSimple;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;

import java.util.ArrayList;


public class SBSeg2022 {
    public static void main(String[] args) throws Exception {

        System.out.println("Normal class: "+GeneralParameters.normalClass);
        runClassifier();
//        runGRASPFS();
//        runTime();
        System.out.println("Normal class: "+GeneralParameters.normalClass);
    }

    private static void runTime() throws Exception {
        ArrayList<float[]> times = new ArrayList();
        for (int i = 0; i < 1; i++) {
            times.add(runClassifier());
        }

        System.out.println("L-KNN;KNN");
        for (int i = 0; i < 1; i++) {
            System.out.println(times.get(i)[0]+";"+times.get(i)[1]);;
        }
    }


    private static void runGRASPFS() throws Exception {

        // Parametros para o GRASP normal
        GeneralParameters.DATASET = "/home/silvio/datasets/NSLKDD/KDDTrain";
        GeneralParameters.UNIFIED_DATASET = GeneralParameters.DATASET;
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.KNN;
        Grasp.allInstances = Util.loadSingleFile(true);

        // Parâmetros para o GRASP com classificador local
        GeneralParameters.TRAIN = Util.loadFile("/home/silvio/datasets/sbseg2022/TreinoNorm0100smurf.csv");
        GeneralParameters.TEST = Util.loadFile("/home/silvio/datasets/sbseg2022/TesteNorm0100smurf.csv");
        GeneralParameters.CROSS_VALIDATION = false;
        GeneralParameters.LOCAL_VALIDATION = true;

        // Rodar com ambos os parametros
        new GraspSimple().run(
                FeatureSubsets.KDD_FULL,
                "GRASP Simple",
                NeighborhoodStructures.IWSSR,
                GeneralParameters.UNIFIED_DATASET);
    }

    private static float[] runClassifier() throws Exception {
        String train = "/home/silvio/datasets/sbseg2022/5fModelo.csv";
        String test = "/home/silvio/datasets/sbseg2022/KDDTesteNorm.csv";
        Util.loadTrainFile(train);
        Util.loadTestFile(test);
        GeneralParameters.NUM_CLASSES = 2;
        GeneralParameters.FEATURE_SELECTION = new int[]{1, 2, 3, 4, 5}; // FeatureSubsets.KDD_FULL;
        GeneralParameters.TRAIN = Util.applyFilterKeep(GeneralParameters.TRAIN);
        GeneralParameters.TEST = Util.applyFilterKeep(GeneralParameters.TEST);
        GeneralParameters.CROSS_VALIDATION = false;

        System.out.println("accuracy;precision;recall;f1score;vp;vn;fp;fn;time");

        /* LKNN */
        long micro = System.currentTimeMillis();
        new LKNN().run(GeneralParameters.TRAIN, GeneralParameters.TEST).printResults();
        long lknn = ((System.currentTimeMillis() - micro));

        /* KNN */
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.KNN;
        long micro2 = System.currentTimeMillis();
        GenericEvaluation.runSingleClassifier(GeneralParameters.TRAIN, GeneralParameters.TEST);
        long knn = ((System.currentTimeMillis() - micro2));

//        GenericResultado result = GenericEvaluation.runSingleClassifierJ48(GeneralParameters.TRAIN, GeneralParameters.TEST);

//        GenericResultado result = GenericEvaluation.runSingleClassifierJ48(GeneralParameters.TRAIN, GeneralParameters.TEST);
//        System.out.println(System.currentTimeMillis() - time+"ms");
//        result.printResults();
        return new float[]{lknn,knn};
    }


}
