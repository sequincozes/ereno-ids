/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.FeatureSubsets;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import java.util.ArrayList;
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class TimeAnalysis {

    public static void main(String[] args) throws Exception {
        GeneralParameters.CSV = false;
        boolean debug = false;
        int deadline = 0;
        int numFeatuers = 18;

        Util.getResultAverageDetailed(setupAndRun(GeneralParameters.WSN_DATASET, 5, 5, GenericClassifiers.RANDOM_TREE, new int[]{1}, deadline), debug).printDetailedTime();
        Util.getResultAverageDetailed(setupAndRun(GeneralParameters.WSN_DATASET, 5, 5, GenericClassifiers.RANDOM_TREE, FeatureSubsets.WSN_FULL, deadline), debug).printDetailedTime();
        System.out.println("Discard before");
        //System.exit(0);

        ArrayList<Integer> fs = new ArrayList<>();
        for (int i = 1; i < numFeatuers; i++) {
            fs.add(i);
            Util.getResultAverageDetailed(setupAndRun(GeneralParameters.WSN_DATASET, 5, 5, GenericClassifiers.RANDOM_TREE, Util.getArray(fs), deadline), debug).printDetailedTime(String.valueOf(fs.size()));
        }

        System.out.println("Vai começar a remover...");
        
        for (int i = numFeatuers; i > 0; i--) {
            fs.remove(0);
            Util.getResultAverageDetailed(setupAndRun(GeneralParameters.WSN_DATASET, 5, 5, GenericClassifiers.RANDOM_TREE, Util.getArray(fs), deadline), debug).printDetailedTime(String.valueOf(fs.size()));
        }
    }

    public static GenericResultado[] setupAndRun(String dataset, int folds, int seed, ClassifierExtended classifier, int[] fs, int deadline) throws Exception {
        GeneralParameters.DATASET = dataset;
        GeneralParameters.SINGLE_CLASSIFIER_MODE = classifier;
        GeneralParameters.FEATURE_SELECTION = fs;
        Instances allInstances = Util.loadAndFilterSingleFile(false); // mudar para o grasp
        if (deadline > 0) {
            return runSingleClassifierWithDeadline(allInstances, folds, seed, deadline);
        } else {
            return runSingleClassifier(allInstances, folds, seed);
        }
    }

    public static GenericResultado[] runSingleClassifier(Instances allInstances, int totalFolds, int seed) throws Exception {
        /* Inicializações */
        GenericResultado[] resultsCompilation = new GenericResultado[totalFolds];
        float totalTrain = 0;
        float totalTest = 0;

        /* Particionar em @TotalFolds */
        Random rand = new Random(seed);
        Instances randData = new Instances(allInstances);
        randData.randomize(rand);

        /* Medir tempo total para a classificação de cada fold */
        long begin = System.currentTimeMillis();
        for (int fold = 0; fold < totalFolds; fold++) { // Vai ter uma fold que o conjunto de treino e teste são idênticos
            Instances train = randData.trainCV(totalFolds, 1, rand); // Treinar uma só vez e não contabilizar
            long previous = System.currentTimeMillis();
            Instances test = randData.testCV(totalFolds, fold);
            resultsCompilation[fold] = GenericEvaluation.runSingleClassifier(train, test);
            resultsCompilation[fold].setTime(System.currentTimeMillis() - previous);
        }
        long end = System.currentTimeMillis();
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
