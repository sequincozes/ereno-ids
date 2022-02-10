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
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author vagne
 */
public class RodaExperimento {


    public static void main(String[] args) throws Exception {

//        GraspSolution bestLocal;
//        Grasp grasp;
//        int remIterations = 1000000;
//        int remNoImprovements = 1000000;
//        ArrayList<Integer> fullList;
//        args = new String[]{"-r", "wsnteste.csv", "5", "1", "7"};
//        System.out.println("Tecle -h ou --help para abrir o menu de opções.");
        if (args.length == 0) {
            showHelp();
        } else if (!(args[0].equals("-h") || args[0].equals("--help") || args[0].equals("-f") || args[0].equals("-c") || args[0].equals("-i"))) { // se não for nenhum desses aqui, chama o método showhelp
            showHelp();
        }
        switch (args[0]) {
            case "-h":
                showHelp();
                break;
            case "--help":
                showHelp();
                break;
            case "-f":
                System.out.println("Selecionado a opção de seleção de features.");
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
                System.out.println("Selecionado a opção de classificação.");
                GeneralParameters.DATASET = args[1];
                GeneralParameters.FOLDS = Integer.valueOf(args[2]);
                GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.valueOf(args[3])]; // Passando a posição do classificador.
                System.out.println("Selecionado o dataset: " + GeneralParameters.DATASET);
                System.out.println("Número de folds: " + GeneralParameters.FOLDS);
                System.out.println("Classificador: " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName());
                // Para classificar usando um filtro
                // CrossValidation.runAndPrintFoldResults(top5, 2, false);
                // Para classificar sem utilizar filtros.
                CrossValidation.runAndPrintFoldResults(false); // false significa que não vai exibir a matriz confusão.
//                System.gc();
                System.out.println("Resultados: [Classifier, Acuracia, Precision, Recall, F1Score, VP, VN, FP, FN, Test Time, Training Time, Features]");
                Runtime rt = Runtime.getRuntime();
                // freeMemory = memória livre alocada atual, totalMemory = memória total alocada
//                long usedMemory = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024; // converte em MB
                long usedMemory = (rt.totalMemory() - rt.freeMemory());
                System.out.println("O classificador " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName() +
                        " utilizou " + usedMemory + " bytes de memória.");
                break;
            case "-i":
                System.out.println("Selecionado a opção de filtro com IWSSr.");
                GeneralParameters.DATASET = args[1];
                GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.parseInt(args[2])];
                runIWSSr();
                break;
            case "-r":
                System.out.println("Selecionado a opção de reduzir o dataset");
                GeneralParameters.DATASET = args[1];
                GeneralParameters.TOTALFOLDS = Integer.valueOf(args[2]);
                GeneralParameters.FOLDS = Integer.valueOf(args[3]);
                GeneralParameters.SEED = Integer.valueOf(args[4]);
                reduceInstances();
                break;
            default:
                showHelp();
        }
    }

    private static void showHelp() {
        System.out.println("-h or --help # Para abrir o menu de opções");
        System.out.println("-f  # Para aplicar o filtro. Uso: java -jar vini.jar -f [DATASET] [FEATURES] [MÉTODO] \n "
                + "                # Exemplo de uso: java -jar vini.jar -f dataset.csv 40 GR\n"
                + "                # GR\n"
                + "                # IG\n"
                + "                # Relief\n"
                + "                # OneR\n");
        System.out.println("-c  # Para classificar. Uso: java -jar vini.jar -c [DATASET] [FOLD] [CLASSIFICADOR] \n "
                + "                # Exemplo de uso: java -jar vini.jar -c dataset.csv 7 1\n"
                + "                # - 0 para RANDOM_TREE\n"
                + "                # - 1 para J48\n"
                + "                # - 2 para REP_TREE\n"
                + "                # - 3 para NAIVE_BAYES\n"
                + "                # - 4 para RANDOM_FOREST");
        System.out.println("-i  # Para rodar IWSSr. Uso: java-jar vini.jar -i [DATASET] [CLASSIFICADOR]\n"
                + "                # Exemplo de uso: java -jar vini.jar -i dataset.csv 1] \n"
                + "                # - 0 para RANDOM_TREE\n"
                + "                # - 1 para J48\n"
                + "                # - 2 para REP_TREE\n"
                + "                # - 3 para NAIVE_BAYES\n"
                + "                # - 4 para RANDOM_FOREST");
        System.out.println("-r  # Para reduzir o dataset. Uso: java-jar vini.jar -r [DATASET] [TOTALFOLDS] [FOLDS] [SEED]\n"
                + "                # Exemplo de uso: java -jar vini.jar -r dataset.csv 10 1 7]");
    }

    public static void reduceInstances() throws IOException {
        Instances allInstances = br.uff.midiacom.ereno.featureSelection.Util.cutFold(GeneralParameters.TOTALFOLDS,
                GeneralParameters.FOLDS, GeneralParameters.SEED, new String[]{
                        GeneralParameters.DATASET});
        System.out.println("Total folds: " + GeneralParameters.TOTALFOLDS);
        System.out.println("Folds: " + GeneralParameters.FOLDS);
        System.out.println("Seed: " + GeneralParameters.SEED);
        System.out.println("Tamanho reduzido: " + allInstances.size());
        br.uff.midiacom.ereno.featureSelection.Util.writeInstancesToFile(allInstances, GeneralParameters.DATASET.replace(".csv",
                "_" + "totalfolds=" + GeneralParameters.TOTALFOLDS + "_folds=" + GeneralParameters.FOLDS + "_seed=" + GeneralParameters.SEED + ".csv"));
    }


    public static void runIWSSr() throws Exception {
        Grasp graspVnd = new GraspVND();
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
