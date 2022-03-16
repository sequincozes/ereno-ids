/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.GenericEvaluation;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.evaluation.CrossValidation;
import br.uff.midiacom.ereno.featureSelection.FeatureRanking;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspVND;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSSr;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;

import static br.uff.midiacom.ereno.featureSelection.Util.putHeaders;

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
//        args = new String[]{"-d", "wsnteste.csv", "5"};
//        System.out.println("Tecle -h ou --help para abrir o menu de opções.");
        if (args.length == 0) {
            showHelp();
        } else if (!(args[0].equals("-h") || args[0].equals("--help") || args[0].equals("-f") || args[0].equals("-c") ||
                args[0].equals("-i") || args[0].equals("-r") || args[0].equals("-d") || args[0].equals("-s"))) { // se não for nenhum desses aqui, chama o método showhelp
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
            case "-d": //divide dataset
                System.out.println("Selecionado a opção de dividir dataset em proporção de treino (o restante será utilizado para teste).");
                GeneralParameters.DATASET = args[1];
                reduceInstances(Integer.valueOf(args[2]));
                break;
            case "-s": // single fold
                System.out.printf("Selecionado a opção de teste sem validação cruzada.");
                GeneralParameters.TRAINING_DATASET = args[1];
                GeneralParameters.TESTING_DATASET = args[2];
                GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.valueOf(args[3])]; // Passando a posição do classificador.
                System.out.println("Selecionado o dataset de treino: " + GeneralParameters.TRAINING_DATASET);
                System.out.println("Selecionado o dataset de teste: " + GeneralParameters.TESTING_DATASET);
                System.out.println("Classificador: " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName());
                runWithoutCV();
                Runtime rtSingleFold = Runtime.getRuntime();
                // freeMemory = memória livre alocada atual, totalMemory = memória total alocada

                long usedMemorySingleFold = (rtSingleFold.totalMemory() - rtSingleFold.freeMemory());
                System.out.println("O classificador " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName() +
                        " utilizou " + usedMemorySingleFold + " bytes de memória.");
                break;
            default:
                showHelp();
        }
    }

    private static void showHelp() {
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-h or --help # Para abrir o menu de opções");
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-f  # Para aplicar o filtro. Uso: java -jar vini.jar -f [DATASET] [FEATURES] [MÉTODO] \n "
                + "                # Exemplo de uso: java -jar vini.jar -f dataset.csv 40 GR\n"
                + "                # GR\n"
                + "                # IG\n"
                + "                # Relief\n"
                + "                # OneR");
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-c  # Para classificar. Uso: java -jar vini.jar -c [DATASET] [FOLD] [CLASSIFICADOR] \n "
                + "                # Exemplo de uso: java -jar vini.jar -c dataset.csv 7 1\n"
                + "                # - 0 para RANDOM_TREE\n"
                + "                # - 1 para J48\n"
                + "                # - 2 para REP_TREE\n"
                + "                # - 3 para NAIVE_BAYES\n"
                + "                # - 4 para RANDOM_FOREST");
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-i  # Para rodar IWSSr. Uso: java-jar vini.jar -i [DATASET] [CLASSIFICADOR]\n"
                + "                # Exemplo de uso: java -jar vini.jar -i dataset.csv 1] \n"
                + "                # - 0 para RANDOM_TREE\n"
                + "                # - 1 para J48\n"
                + "                # - 2 para REP_TREE\n"
                + "                # - 3 para NAIVE_BAYES\n"
                + "                # - 4 para RANDOM_FOREST");
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-r  # Para reduzir o dataset. Uso: java-jar vini.jar -r [DATASET] [TOTALFOLDS] [FOLD] [SEED]\n"
                + "                # Exemplo de uso: java -jar vini.jar -r dataset.csv 10 1 7]");
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-d  # Para dividir um dataset em dois pedaços. Uso: java-jar vini.jar -r [DATASET] [PORÇÃO]\n"
                + "                # Exemplo de uso: java -jar vini.jar -d dataset.csv 2] \n "
                + "                # OBS:. O parâmetro PORÇÃO representa a proporção de treinamento. Exemplos: \n "
                + "                # Porção 2: representa 20% de treino e 80% de teste \n "
                + "                # Porção 3: representa 30% de treino e 70% de teste \n "
                + "                # Porção 5: representa 50% de treino e 60% de teste \n "
                + "                # Porção 10: representa 100% de treino.");
        System.out.println(" ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----");
        System.out.println("-s  # Para testar um dataset sem validaçao cruzada. Uso: java-jar vini.jar -s [TRAINING_DATASET] [TESTING_DATASET] [CLASSIFICADOR] \n"
                + "                # Exemplo de uso: java -jar vini.jar -s dataset_training.csv dataset_testing.csv 2\n"
                + "                # - 0 para RANDOM_TREE\n"
                + "                # - 1 para J48\n"
                + "                # - 2 para REP_TREE\n"
                + "                # - 3 para NAIVE_BAYES\n"
                + "                # - 4 para RANDOM_FOREST");
        System.exit(0);
    }

    public static void reduceInstances() throws IOException {
        Instances allInstances = br.uff.midiacom.ereno.featureSelection.Util.cutFold(GeneralParameters.TOTALFOLDS,
                GeneralParameters.FOLD, GeneralParameters.SEED, new String[]{
                        GeneralParameters.DATASET});
        System.out.println("Total folds: " + GeneralParameters.TOTALFOLDS);
        System.out.println("Fold: " + GeneralParameters.FOLD);
        System.out.println("Seed: " + GeneralParameters.SEED);
        System.out.println("Tamanho reduzido: " + allInstances.size());
        br.uff.midiacom.ereno.featureSelection.Util.writeInstancesToFile(allInstances, GeneralParameters.DATASET.replace(".csv",
                "_" + "totalfolds=" + GeneralParameters.TOTALFOLDS + "_fold=" + GeneralParameters.FOLD + "_seed=" + GeneralParameters.SEED + ".csv"));
    }

    public static void reduceInstances(int trainingPortion) throws IOException {
        putHeaders(GeneralParameters.DATASET, GeneralParameters.DATASET.replace(".csv",
                "_training.csv"));
        int i = 0;
        for (i = 0; i < trainingPortion; i++) {
            Instances allInstances = br.uff.midiacom.ereno.featureSelection.Util.cutFold(10,
                    i, GeneralParameters.SEED, new String[]{
                            GeneralParameters.DATASET});

            br.uff.midiacom.ereno.featureSelection.Util.writeInstancesToFileT(allInstances,
                    GeneralParameters.DATASET.replace(".csv",
                    "_training.csv"));
        }

        putHeaders(GeneralParameters.DATASET, GeneralParameters.DATASET.replace(".csv",
                "_testing.csv"));
        for (i = trainingPortion; i < 10; i++) { // sempre de 1 a 10.
            Instances allInstances = br.uff.midiacom.ereno.featureSelection.Util.cutFold(10,
                    i, GeneralParameters.SEED, new String[]{
                            GeneralParameters.DATASET});

            br.uff.midiacom.ereno.featureSelection.Util.writeInstancesToFileT(allInstances,
                    GeneralParameters.DATASET.replace(".csv",
                            "_testing.csv"));
        }
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

    private static void runWithoutCV() throws Exception {
        GeneralParameters.DATASET = GeneralParameters.TRAINING_DATASET;

        Instances train = br.uff.midiacom.ereno.abstractclassification.Util.loadSingleFile(false);
        train.setClassIndex(train.numAttributes() - 1);

        GeneralParameters.DATASET = GeneralParameters.TESTING_DATASET;
        Instances test = br.uff.midiacom.ereno.abstractclassification.Util.loadSingleFile(false);
        test.setClassIndex(test.numAttributes() - 1);

        GenericEvaluation.runSingleClassifier(train, test);
    }

}
