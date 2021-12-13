/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericClassifiers;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspVND;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSSr;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author vagne
 */
public class RodaExperimento {

    // vini.jar -f dataset.csv 40 gr
    // vini.jar -c 
    // vini.jar -
    public static void main(String[] args) throws Exception {

        GraspSolution bestLocal;
        Grasp grasp;
        int remIterations = 1000000;
        int remNoImprovements = 1000000;
        ArrayList<Integer> fullList;
        args = new String[]{"-c", "teste.csv", "4", "2"};
        System.out.println("Tecle -h ou --help para abrir o menu de opções.");

        switch (args[0]) {
            case "-h":
                System.out.println("-h --help to options menu");
                System.out.println("-f  [Para aplicar o filtro. Requisitos: Dataset, Features, Método. \n "
                        + "Exemplo de uso:  -f dataset.csv 40 gr]");
                System.out.println("-c  [Para classificar. Requisitos: {Dataset, Fold, Classificador}]"
                        + "                // - Tecle 0 para RANDOM_TREE\n"
                        + "                // - Tecle 1 para J48\n"
                        + "                // - Tecle 2 para REP_TREE\n"
                        + "                // - Tecle 3 para NAIVE_BAYES\n"
                        + "                // - Tecle 4 para RANDOM_FOREST");
                System.out.println("-i  [Para rodar IWSSr. Requisitos: {Dataset, Classificador}"
                        + "Exemplo de uso: -i dataset.csv 1]");
                System.exit(0);
            case "--help":
                System.out.println("-h --help to options menu");
                System.out.println("-e  [Run ExperimentoVinicius.java class]");
                System.out.println("-i  [Run IWSSr filter]");
                System.exit(0);
                break;
            case "-f":
                GeneralParameters.DATASET = args[1];
                if (args[3].toLowerCase().equalsIgnoreCase("GR")) {
                    FeatureRanking.avaliarESelecionarFromGeneralParamter(Integer.parseInt(args[2]), FeatureRanking.METODO.GR, false);
                } else if (args[3].toLowerCase().equalsIgnoreCase("IG")) {
                    FeatureRanking.avaliarESelecionarFromGeneralParamter(Integer.parseInt(args[2]), FeatureRanking.METODO.IG, false);
                } else if (args[3].toLowerCase().equalsIgnoreCase("OneR")) {
                    FeatureRanking.avaliarESelecionarFromGeneralParamter(Integer.parseInt(args[2]), FeatureRanking.METODO.OneR, false);
                }
                break;
            case "-c":
                GeneralParameters.DATASET = args[1];
                GeneralParameters.FOLDS = Integer.valueOf(args[2]);
                GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.valueOf(args[3])]; // Passando a posição do classificador.
                // Para classificar usando um filtro
                // CrossValidation.runAndPrintFoldResults(top5, 2, false);
                // Para classificar sem utilizar filtros.
                CrossValidation.runAndPrintFoldResults(false); // false significa que não vai exibir a matriz confusão.
            case "-i":
                GeneralParameters.DATASET = args[1];
                runIWSSr(args);
                break;
        }
    }

    public static void runIWSSr(String[] args) throws Exception {
        Grasp graspVnd = new GraspVND();
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.parseInt(args[2])];
        //        GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[1];
        //        GeneralParameters.DATASET ="C:\\datasets\\uc01\\train.csv";// GeneralParameters.SWAT30pct;
        graspVnd.allInstances = Util.loadSingleFile(true);
        IWSSr iwssr = new IWSSr(graspVnd);
        iwssr.fullList = new ArrayList<>();

        int[] rcl = new int[graspVnd.allInstances.get(0).numAttributes() - 1]; //@TODO quando usar isso, atribuir uma RCL
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

    public static void runExperimentosVinicius(String[] args) throws Exception {
        GeneralParameters.CSV = true;

        //12, 26, 4, 25, 39, 30, 38, 6, 29, 5, 3, 37, 33, 34, 35, 31, 28, 27, 8, 41, 32, 23, 10, 36, 13, 2, 15, 11, 19, 40, 16, 21, 1, 17, 7, 24, 14, 22, 9
        int[] completo = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        int[] top5 = new int[]{12, 26, 4, 25, 39};
        int[] top10 = new int[]{12, 26, 4, 25, 39, 30, 38, 6, 29, 5};

        // Para aplicar o filtro
        FeatureRanking.avaliarESelecionarFromGeneralParamter(40, FeatureRanking.METODO.GR, false);

        // Para classificar usando um filtro
        // CrossValidation.runAndPrintFoldResults(top5, 2, false);
        // Para classificar sem utilizar filtros.
        CrossValidation.runAndPrintFoldResults(top5, 2, false);
    }

}
