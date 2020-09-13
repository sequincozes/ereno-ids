/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class TimeAnalysis {

    public static void main(String[] args) throws Exception {
        String dataset = GeneralParameters.DATASETS_FOREACH[0];
        int[] allFeatures = GeneralParameters.DATASETS_FEATURES_FOREACH[0];
        ClassifierExtended classifier =GenericClassifiers.NAIVE_BAYES;// GeneralParameters.CLASSIFIERS_FOREACH[0];
        int folds = 5;//Integer.valueOf(args[2]);
        
        if (false) { //Automatic
            System.out.println("Usage: java -jar timeanalysis.jar [datasetIndex] [classifierIndex] [nFolds]");
            dataset = GeneralParameters.DATASETS_FOREACH[Integer.valueOf(args[0])];
            allFeatures = GeneralParameters.DATASETS_FEATURES_FOREACH[Integer.valueOf(args[0])];
            classifier = GeneralParameters.CLASSIFIERS_FOREACH[Integer.valueOf(args[3])];
            folds = Integer.valueOf(args[2]);
        }
        
        GeneralParameters.CSV = false;
        boolean debug = false;
        boolean printSelection = false;
        int deadline = 0;

        Instances allInstances = setup(dataset, classifier, allFeatures);
        ArrayList<Integer> fs = new ArrayList<>();
        for (int i = 1; i <= allFeatures.length; i++) {
            fs.add(i);
        }

        for (int i = allFeatures.length; i > 0; i--) {
            GenericResultado[] results = run(allInstances, folds, 5, deadline, Util.getArray(fs), printSelection);
            GenericResultado aggregatedResults = Util.getResultAverageDetailed(results, debug);
            aggregatedResults.printDetailedTime(String.valueOf(fs.size()));
            fs.remove(0);
        }
    }

    public static Instances setup(String dataset, ClassifierExtended classifier, int[] fullFeatures) throws Exception {
        GeneralParameters.DATASET = dataset;
        GeneralParameters.SINGLE_CLASSIFIER_MODE = classifier;
        GeneralParameters.FEATURE_SELECTION = fullFeatures;
        return Util.loadSingleFile(false); // mudar para o grasp
    }

    public static GenericResultado[] run(Instances instances, int folds, int seed, long deadline, int[] fs, boolean printSelection) throws Exception {
        GeneralParameters.FEATURE_SELECTION = fs;
        System.out.println("fs: "+Arrays.toString(fs));
        Instances filtered = Util.copyAndFilter(instances, printSelection);
        if (deadline > 0) {
            return runSingleClassifierWithDeadline(filtered, folds, seed, deadline);
        } else {
            return runSingleClassifier(filtered, folds, seed);
        }
    }

    public static GenericResultado[] runSingleClassifier(Instances allInstances, int totalFolds, int seed) throws Exception {
        /* Inicializações */
        GenericResultado[] resultsCompilation = new GenericResultado[totalFolds];

        /* Particionar em @TotalFolds */
        Random rand = new Random(seed);
        Instances randData = new Instances(allInstances);
        randData.randomize(rand);

        /* Medir tempo total para a classificação de cada fold */
        // long begin = System.currentTimeMillis();
        for (int fold = 0; fold < totalFolds; fold++) { // Vai ter uma fold que o conjunto de treino e teste são idênticos
            Instances train = randData.trainCV(totalFolds, 1, rand); // Treinar uma só vez e não contabilizar
            Instances test = randData.testCV(totalFolds, fold);
            resultsCompilation[fold] = GenericEvaluation.runSingleClassifier(train, test);
        }
        //long end = System.currentTimeMillis();
        ///System.out.println(allInstances.get(0).numAttributes() + /*";" + totalTrain + ";" + totalTest +*/ ";" + (end - begin) / totalFolds);
        return resultsCompilation;
    }

    public static GenericResultado[] runSingleClassifierWithDeadline(Instances allInstances, int totalFolds, int seed, long deadline) throws Exception {
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        GenericResultado[] resultsCompilation = new GenericResultado[totalFolds];
        float totalTrain = 0;
        float totalTest = 0;

        long begin = System.currentTimeMillis();

        long beginTimestamp = System.nanoTime();
        Instances train = randData.trainCV(totalFolds, 1, rand);
        long trainigTimestamp = System.nanoTime();
        totalTrain = totalTrain + ((trainigTimestamp - beginTimestamp) / train.size());
        //System.out.println("Total Time:" + (System.currentTimeMillis() - begin));

        /* Tempo de teste */
        // long begin = System.currentTimeMillis();
        Instances test = randData.testCV(totalFolds, 1);
        long evaluationTimestamp = System.nanoTime();
        totalTest = totalTest + ((evaluationTimestamp - trainigTimestamp) / test.size());
        GenericEvaluation.runSingleClassifierWithDeadline(train, test, 1000 * 1000 * 1000);

        long end = System.currentTimeMillis();

        totalTrain = (totalTrain / totalFolds);
        totalTest = (totalTest / totalFolds);
        //totalTrain = 1 / ((totalTrain) / 1000000 / 5);
        //totalTest =  1 / ((totalTest) / 1000000 / 5);

        //System.out.println(allInstances.get(0).numAttributes() + ";" + totalTrain + ";" + totalTest + ";" + (end - begin) / totalFolds);
        return resultsCompilation;
    }

}
