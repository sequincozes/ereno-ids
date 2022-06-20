package br.uff.midiacom.ereno.thesis;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspRVND;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspVND;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSSr;
import br.uff.midiacom.ereno.featureSelection.grasp.externalClassifier.SBSeGrasp;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;

import java.util.ArrayList;

import static br.uff.midiacom.ereno.abstractclassification.GeneralParameters.PRINT_CONFUSION_MATRIX;

public class ThesisMicroservice {

    // java -jar thesis.jar [train.csv] [test.csv] [features] [classifier]
    public static void main(String[] args) throws Exception {
        System.out.println("Configurado para o SBSeg na classe THesisMicroservice.java");
        SBSeGrasp.main(args);

        System.out.println("usage: java -jar thesis.jar [train.csv] [test.csv] [features] [classifier]");
        GeneralParameters.CROSS_VALIDATION = false;

        // Maldormed call
        if (args.length != 4) {
            // java -Xmx20g -jar thesis.jar all.csv goose-grasp 2 >> grasp-goose_08-janeir_cluster04.txt&
            System.err.println("usage: java -jar thesis.jar [train.csv] [test.csv] [features] [classifier]");
            System.out.println("features = {GOOSE-GRASP, GSV-GRASP, F-GRASP, I-GRASP, IWSSR, GOOSE, GOOSESV, GOOSESV++, FULL}");
            System.out.println("classifiers = {1=RANDOM_TREE, 2=J48, 3=REP_TREE, 4=NAIVE_BAYES, 5=RANDOM_FOREST}");
            System.exit(1);
        } else {
            // Seting up the dataset
            GeneralParameters.DATASET = null; // We do not use this parameter because the cross-validation is off.
            Util.loadTrainFile(args[0]);
            Util.loadTestFile(args[1]);

            // Seting up the features
            String fs = args[3].toLowerCase();

            // Seting up the classifier
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.parseInt(args[2]) - 1];

            switch (fs) {
                case "goose-grasp":
                    new GraspRVND().run(FeatureSubsets.goose, "grasp_rvnd");
                    break;
                case "gsv-grasp":
                    new GraspRVND().run(FeatureSubsets.gooseAndSv, "grasp_rvnd");
                    break;
                case "i-grasp":
                    System.exit(1);
                    System.err.println("Not implemented yet.");
                    new GraspRVND().run(FeatureSubsets.iwssrGoosePlusPlusAndSvPlusPlus, "grasp_rvnd");
                    break;
                case "iwssr":
                    runIWSSR();
                    break;
                case "f-grasp":
                    new GraspRVND().run(FeatureSubsets.goosePlusPlusAndSvPlusPlus, "grasp_rvnd");
                    break;
                case "goose":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.goose;
                    runSingleEvaluation();
                    break;
                case "goosesv":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.gooseAndSv;
                    runSingleEvaluation();
                    break;
                case "goosesv++":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.gooseAndSvPlusPlus;
                    runSingleEvaluation();
                    break;
                case "full":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.goosePlusPlusAndSvPlusPlus;
                    runSingleEvaluation();
                    break;
                case "custom":
                    runSingleEvaluation();
                    break;
            }
        }
    }

    private static void runSingleEvaluation() throws Exception {
        GeneralParameters.TRAIN = Util.applyFilterKeep(GeneralParameters.TRAIN);
        GeneralParameters.TEST = Util.applyFilterKeep(GeneralParameters.TEST);
        GenericResultado result = GenericEvaluation.runSingleClassifier(GeneralParameters.TRAIN, GeneralParameters.TEST);

        if (PRINT_CONFUSION_MATRIX) {
            System.out.println("CONFUSION MATRIX");
            for (int classIndex = 0; classIndex < result.getConfusionMatrix().length; classIndex++) {
                System.out.print("EXPECTED: " + classIndex + ";RESULT:;");
                for (int expectedIndex = 0; expectedIndex < result.getConfusionMatrix().length; expectedIndex++) {
                    System.out.print(result.getConfusionMatrix()[classIndex][expectedIndex] + ";");
                }
                System.out.println("");
            }
        }
    }

    private static void runIWSSR() throws Exception {
        Grasp graspVnd = new GraspVND();
        graspVnd.allInstances = Util.loadSingleFile(true);
        IWSSr iwssr = new IWSSr(graspVnd);
        iwssr.fullList = new ArrayList<>();

        int[] rcl = new int[graspVnd.allInstances.get(0).numAttributes() - 1];
        for (int feature = 0; feature < graspVnd.allInstances.get(0).numAttributes() - 1; feature++) {
            iwssr.fullList.add(feature + 1);
            rcl[feature] = feature + 1;
        }

        ArrayList<Integer> seedFS = new ArrayList<>();

        seedFS.add(iwssr.fullList.remove(rcl.length - 1));
        GraspSolution seedSolution = new GraspSolution(seedFS, iwssr.fullList);
        seedSolution = graspVnd.avaliar(seedSolution);

        iwssr.run(seedSolution);
    }

}
