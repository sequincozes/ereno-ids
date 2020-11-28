/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class CrossValidation {

    public static void printFoldResults(int[] features, int onlyThis, boolean printConfusionMatrix) throws Exception {
        System.out.print(GenericClassifiers.all[onlyThis].getClassifierName() + ";");

        // Compute for all folds
        GenericResultado[] results = setupAndRun(5, 7, GenericClassifiers.all[onlyThis], features);

        // Just print
        for (int i = 0; i < GeneralParameters.FOLDS; i++) {
            try {
                System.out.print("fold[" + i + "];" + results[i].getF1Score() + ";");
            } catch (Exception ex) {
                Logger.getLogger(CrossValidation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("");

        // Just print matrix
        if (printConfusionMatrix) {
            int numFold = 0;
            for (GenericResultado foldResult : results) {
                System.out.println("Fold: " + numFold++);
                //Para cada fold, mostrar matriz
                for (int classIndex = 0; classIndex < foldResult.getConfusionMatrix().length; classIndex++) {
                    System.out.print("Esperado: " + classIndex + ";resultados:;");
                    for (int expectedIndex = 0; expectedIndex < foldResult.getConfusionMatrix().length; expectedIndex++) {
                        System.out.print(foldResult.getConfusionMatrix()[classIndex][expectedIndex] + ";");
                    }
                    System.out.println("");
                }
            }
        }
    }

    public static GenericResultado[] setupAndRun(int folds, int seed, ClassifierExtended classifier, int[] fs) throws Exception {
        GeneralParameters.SINGLE_CLASSIFIER_MODE = classifier;
        GeneralParameters.FEATURE_SELECTION = fs;
        Instances allInstances = Util.loadAndFilterSingleFile(false); // mudar para o grasp
        GenericResultado[] result = runSingleClassifier(allInstances, folds, seed);

        return result;
    }

    public static void justRun(int folds, int seed) throws Exception {
        System.out.println("--- All classifiers");
        long time = System.currentTimeMillis();
        Instances allInstances = Util.loadAndFilterSingleFile(true); // mudar para o grasp
        allInstances = Util.copyAndFilter(allInstances, true);
        runSingleClassifier(allInstances, folds, seed);
        System.out.println("--- END classifiers (" + (System.currentTimeMillis() - time) + ")");
    }

    public static GenericResultado[] runSingleClassifier(Instances allInstances, int totalFolds, int seed) throws Exception {
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        GenericResultado[] resultsCompilation = new GenericResultado[totalFolds];
        for (int fold = 0; fold < totalFolds; fold++) {
            Instances train = randData.trainCV(totalFolds, fold, rand);
            Instances test = randData.testCV(totalFolds, fold);
            resultsCompilation[fold] = GenericEvaluation.runSingleClassifier(train, test);

            /* APAGAR */
            //System.out.println("GR_G_VND - " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName()                    + " - " + Arrays.toString(GeneralParameters.FEATURE_SELECTION));

            /*  ATÉ AQUI */
            if (GeneralParameters.DEBUG_MODE) {

                System.out.println("runSingleClassifier - F1:" + resultsCompilation[fold].getF1Score());
                System.out.println("runSingleClassifier - Acc:" + resultsCompilation[fold].getAcuracia());
                System.out.println("runSingleClassifier - Precision:" + resultsCompilation[fold].getPrecision());
                System.out.println("runSingleClassifier - Recall:" + resultsCompilation[fold].getRecall());
                System.out.println("runSingleClassifier - VN:" + resultsCompilation[fold].VN);
                System.out.println("runSingleClassifier - VP:" + resultsCompilation[fold].VP);
                System.out.println("runSingleClassifier - FN:" + resultsCompilation[fold].FN);
                System.out.println("runSingleClassifier - FP:" + resultsCompilation[fold].FP);
                System.out.println("-----");
            }
        }

        //totalTest = (totalTest / totalFolds);
        //  System.out.println(allInstances.get(0).numAttributes() + ";" + totalTrain + ";" + totalTest + ";" + (end - begin) / totalFolds);
        return resultsCompilation;
    }

    public static GenericResultado[][] runMultipleClassifier(int totalFolds, int seed) throws Exception {
        Instances allInstances = Util.loadAndFilterSingleFile(true);
        Random rand = new Random(seed);   // create seeded number generator
        Instances randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator

        GenericResultado[][] resultsCompilation = new GenericResultado[totalFolds][GeneralParameters.CLASSIFIERS_FOREACH.length];
        System.out.print("--- FOLD: ");
        for (int fold = 0; fold < totalFolds; fold++) {
            System.out.print("[" + fold + "]");
            Instances train = randData.trainCV(totalFolds, fold, rand);
            Instances test = randData.testCV(totalFolds, fold);
            GenericResultado[] classifiersResults = GenericEvaluation.runMultipleClassifier(train, test);
            int classifierIndex = 0;
            for (GenericResultado classifierResult : classifiersResults) {
                resultsCompilation[fold][classifierIndex++] = classifierResult;
            }
        }
        System.out.println();

        return resultsCompilation;
    }

    private static int[] getArray(ArrayList<Integer> fs) {
        int[] array = new int[fs.size()];
        for (int i = 0; i < fs.size(); i++) {
            array[i] = fs.get(i);
        }
        return array;
    }

    public static void printFolds(int[][] features) throws Exception {
        int numFolds = 5;
        // Compute all folds, for each classifier
        GenericResultado[][] allClassifiers = new GenericResultado[GenericClassifiers.all.length][];
        for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
            System.out.print(GenericClassifiers.all[classifierIndex].getClassifierName() + ";");
            GenericResultado[] foldResults = setupAndRun(numFolds, 7, GenericClassifiers.all[classifierIndex], features[classifierIndex]);
            allClassifiers[classifierIndex] = foldResults;
        }

        //Just print  
        for (int classifierIndex = 0; classifierIndex < GenericClassifiers.all.length; classifierIndex++) {
            System.out.print(GenericClassifiers.all[classifierIndex].getClassifierName() + ";");
            for (int fold = 0; fold < GeneralParameters.FOLDS; fold++) {
                try {
                    System.out.print(allClassifiers[classifierIndex][fold].getF1Score() + ";");
                } catch (Exception ex) {
                    Logger.getLogger(CrossValidation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("");
        }
    }
}
