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
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

public class ThesisMicroservice {

    // java -jar thesis.jar [dataset.csv] [features] [classifier]
    // java -jar thesis.jar [train.csv] [test.csv] [features] [classifier]
    public static void main(String[] args) throws Exception {
        System.out.println("usage: java -jar thesis.jar [dataset.csv] [features] [classifier] OR \n" +
                "java -jar thesis.jar [train.csv] [test.csv] [features] [classifier]");
        // Maldormed call
        if (args.length != 4 && args.length != 3) {
            System.out.println("features = {F-GRASP, I-GRASP, IWSSR, GOOSE, GOOSESV, GOOSESV++, FULL}");
            System.out.println("classifiers = {1=RANDOM_TREE, 2=J48, 3=REP_TREE, 4=NAIVE_BAYES, 5=RANDOM_FOREST}");
            System.exit(1);
        } else if (args.length == 3) {
            System.out.println("Entered filter selection mode.");
            // Seting up the dataset
            GeneralParameters.DATASET = args[0];

            // Seting up the features
            String fs = args[1].toLowerCase();

            // Seting up the classifier
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.parseInt(args[2]) - 1];

            switch (fs) {
                case "i-grasp":
                    System.exit(1);
                    System.err.println("Not implemented yet.");
                    ((GraspRVND) new GraspRVND().setupGraspMicroservice(Integer.parseInt(args[2]) - 1)).run(FeatureSubsets.iwssrGoosePlusPlusAndSvPlusPlus, "grasp_rvnd", GeneralParameters.DATASET);
                    break;
                case "iwssr":
                    runIWSSR();
                    break;
                case "f-grasp":
                    ((GraspRVND) new GraspRVND().setupGraspMicroservice(Integer.parseInt(args[2]) - 1)).run(FeatureSubsets.goosePlusPlusAndSvPlusPlus, "grasp_rvnd", GeneralParameters.DATASET);
                    break;
            }
        } else if (args.length == 4) {
            // Seting up the dataset
            String train = args[0];
            String test = args[1];

            // Seting up the classifier
            GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.parseInt(args[3]) - 1];

            // Seting up the features
            String fs = args[2].toLowerCase();
            switch (fs) {
                case "goose":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.goose;
                    break;
                case "goosesv":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.gooseAndSv;
                    break;
                case "goosesv++":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.gooseAndSvPlusPlus;
                    break;
                case "full":
                    GeneralParameters.FEATURE_SELECTION = FeatureSubsets.goosePlusPlusAndSvPlusPlus;
                    break;
            }

            runSingleEvaluation(train, test);
        }

    }

    private static void runSingleEvaluation(String trainData, String testData) throws Exception {
        System.out.println("Classifier: "+GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName()+" - FS: "+Arrays.toString(GeneralParameters.FEATURE_SELECTION));
        GeneralParameters.DATASET = trainData;
        Instances train = br.uff.midiacom.ereno.abstractclassification.Util.loadAndFilterSingleFile(false);
        train.setClassIndex(train.numAttributes() - 1);

        GeneralParameters.DATASET = testData;
        Instances test = br.uff.midiacom.ereno.abstractclassification.Util.loadAndFilterSingleFile(false);
        test.setClassIndex(test.numAttributes() - 1);

        System.out.println("CONFUSION MATRIX");
        GenericResultado result = GenericEvaluation.runSingleClassifier(train, test);
        for (int classIndex = 0; classIndex < result.getConfusionMatrix().length; classIndex++) {
            System.out.print("EXPECTED: " + classIndex + ";RESULT:;");
            for (int expectedIndex = 0; expectedIndex < result.getConfusionMatrix().length; expectedIndex++) {
                System.out.print(result.getConfusionMatrix()[classIndex][expectedIndex] + ";");
            }
            System.out.println("");
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
