/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.multithread;

import br.uff.midiacom.ereno.abstractclassification.FeatureSubsets;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import java.util.Random;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class Multithread {

    static Random rand;
    static Instances randData;
    static int currentFold = 1;
    static int totalFolds = 10;
    static GenericResultado[] resultsCompilation;

    public static void main(String[] args) throws Exception {
        GeneralParameters.ALL_IN_ONE_FILE = "/home/silvio/datasets/wsn-ds/all/all_in_one_mini.csv";
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GenericClassifiers.NAIVE_BAYES;
        GeneralParameters.FEATURE_SELECTION = FeatureSubsets.WSN_IG;

        Instances allInstances = Util.loadSingleFile(true);
        allInstances.setClassIndex(allInstances.numAttributes() - 1);
        Multithread m = new Multithread();
        m.runSingleClassifierMultithread(allInstances, totalFolds, currentFold);
    }

    public GenericResultado[] runSingleClassifierMultithread(Instances allInstances, int totalFolds, int seed) throws Exception {
        rand = new Random(10);   // create seeded number generator
        randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        resultsCompilation = new GenericResultado[totalFolds];

        for (int fold = 0; fold < totalFolds; fold++) {
            Thread object = new Thread(new ClassificationNanoservice());
            object.start();
        }

        return resultsCompilation;
    }

    public GenericResultado[] runSingleClassifierSinglethread(Instances allInstances, int totalFolds, int seed) throws Exception {
        rand = new Random(10);   // create seeded number generator
        randData = new Instances(allInstances);   // create copy of original data
        randData.randomize(rand);         // randomize data with number generator
        resultsCompilation = new GenericResultado[totalFolds];

        for (int fold = 0; fold < totalFolds; fold++) {
            classificationNanojob(currentFold++);
        }

        return resultsCompilation;
    }

    class ClassificationNanoservice implements Runnable {

        public void run() {
            try {
                classificationNanojob(currentFold++);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void classificationNanojob(int currentFold) throws Exception {
        //currentFold = currentFold + 1;
        Instances train = randData.trainCV(totalFolds, currentFold, rand);
        Instances test = randData.testCV(totalFolds, currentFold);
        resultsCompilation[currentFold] = GenericEvaluation.runSingleClassifier(train, test);
    }
}
