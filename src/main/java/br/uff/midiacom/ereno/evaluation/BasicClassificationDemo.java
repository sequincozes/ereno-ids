package br.uff.midiacom.ereno.evaluation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.Util;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instance;
import weka.core.Instances;

public class BasicClassificationDemo {
    public static void main(String[] args) throws Exception {
        // Select one classifier for the evaluation
        ClassifierExtended classifier = new ClassifierExtended(new NaiveBayes(), "NaiveBayes");

        // Select a subset of features (or the full set)
        int features[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

        // Read training dataset and train classifier
        String train = "train.arff";
        Instances trainingDataset = setup(train, classifier, features);
        trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);
        classifier.getClassifier().buildClassifier(trainingDataset); // training the classifier

        // Read testing dataset and test classifier
        String test = "test.arff";
        Instances testingDataset = setup(test, classifier, features);
        testingDataset.setClassIndex(testingDataset.numAttributes() - 1);

        // Exemplo
        Instance instancia = testingDataset.get(0);
        System.out.println("Classe esperada: " + instancia.classValue());
        System.out.println("Classe resultante: " + classifier.getClassifier().classifyInstance(instancia));


    }

    public static Instances setup(String dataset, ClassifierExtended classifier, int[] fullFeatures) throws Exception {
        GeneralParameters.DATASET = dataset;
        GeneralParameters.SINGLE_CLASSIFIER_MODE = classifier;
        GeneralParameters.FEATURE_SELECTION = fullFeatures;
        return Util.loadSingleFile(false); // mudar para o grasp
    }
}
