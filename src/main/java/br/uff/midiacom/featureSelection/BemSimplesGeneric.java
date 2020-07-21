/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.featureSelection;

import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class BemSimplesGeneric {

    private static Instances[] allInstances;
    private static Classifier selectedClassifier = Parameters.NAIVE_BAYES.getClassifier();
    private static boolean rawOutput = false;
    private static boolean debug = false;
    private static boolean CSV = true;
    private static double normalClass;
    private static boolean printTime = false;
    private static boolean printConfusionMatrix = true;

    public static void main(String[] args) throws Exception {
        run("/home/silvio/datasets/wsn-ds/binario/binario_treino_1.csv","/home/silvio/datasets/wsn-ds/binario/ataque_e_normal_99_binario.csv");
    }

    public static void runMultiClass(String useCase) throws Exception {

        allInstances = Util.loadAndFilterMulticlass(true, 10, true); // a cada N instancias, uma é treino
        System.out.println("Size:" + allInstances[0].size());
        System.out.println("Instance:" + allInstances[0].get(0));
        System.out.println("Class:" + allInstances[0].get(0).classValue());
        normalClass = allInstances[0].get(0).classValue();

        if (CSV) {
            System.out.println(useCase + ";acuracia;precision;recall;f1score;VP;VN;FP;FN;time");
        }

        for (ClassifierExtended classififer : Parameters.CLASSIFIERS_FOREACH) {
            // ClassifierExtended classififer = Parameters.CLASSIFIERS_FOREACH[4];

            selectedClassifier = classififer.getClassifier();
            GenericResultado r = testaEssaGalera(classififer.getClassifierName());
            String acuracia = String.valueOf(r.getAcuracia()).replace(".", ",");
            String precision = String.valueOf(r.getPrecision()).replace(".", ",");
            String recall = String.valueOf(r.getRecall()).replace(".", ",");
            String f1score = String.valueOf(r.getF1Score()).replace(".", ",");

            if (CSV) {
                System.out.println(
                        classififer.getClassifierName() + ";"
                        + acuracia.replace(",", ".") + ";"
                        + precision.replace(",", ".") + ";"
                        + recall.replace(",", ".") + ";"
                        + f1score.replace(",", ".") + ";"
                        + r.getVP() + ";"
                        + r.getVN() + ";"
                        + r.getFP() + ";"
                        + r.getFN() + ";"
                        + r.getTime()
                );
            } else {
                System.out.println("VP: " + r.getVP() + "  ->  acc:" + r.getAcuracia());
            }

            if (printConfusionMatrix) {
                for (int i = 0; i < r.getConfusionMatrix().length; i++) {
                    System.out.print("Esperado: " + i + ";");
                    for (int j = 0; j < r.getConfusionMatrix().length; j++) {
                        System.out.print(r.getConfusionMatrix()[i][j] + ";");
                    }
                    System.out.println("");
                }
            }

        }
    }

     public static void run(String trainFile, String testfile) throws Exception {
        Parameters.LEGACY_TRAIN = trainFile;
        Parameters.LEGACY_TEST = testfile;
        Parameters.FEATURE_SELECTION = new int[]{8, 11, 13, 5, 12};
        Parameters.CLASSIFIERS_FOREACH = new ClassifierExtended[]
        {Parameters.J48};
        allInstances = Util.loadAndFilterLegacy(true); 

        normalClass = allInstances[0].get(0).classValue();

        if (CSV) {
            System.out.println(Parameters.FEATURE_SELECTION.toString()  + ";acuracia;precision;recall;f1score;VP;VN;FP;FN;time");
        }

        for (ClassifierExtended classififer : Parameters.CLASSIFIERS_FOREACH) {
            selectedClassifier = classififer.getClassifier();
            GenericResultado r = testaEssaGalera(classififer.getClassifierName());
            String acuracia = String.valueOf(r.getAcuracia()).replace(".", ",");
            String precision = String.valueOf(r.getPrecision()).replace(".", ",");
            String recall = String.valueOf(r.getRecall()).replace(".", ",");
            String f1score = String.valueOf(r.getF1Score()).replace(".", ",");

            if (CSV) {
                System.out.println(
                        classififer.getClassifierName() + ";"
                        + acuracia.replace(",", ".") + ";"
                        + precision.replace(",", ".") + ";"
                        + recall.replace(",", ".") + ";"
                        + f1score.replace(",", ".") + ";"
                        + r.getVP() + ";"
                        + r.getVN() + ";"
                        + r.getFP() + ";"
                        + r.getFN() + ";"
                        + r.getTime()
                );
            } else {
                System.out.println("VP: " + r.getVP() + "  ->  acc:" + r.getAcuracia());
            }
        }
    }

    
    public static void run(String useCase) throws Exception {
        Parameters.BINARTY_ATTACK_CLASS = useCase;

        allInstances = Util.loadAndFilter(true, 10, true); // a cada N instancias, uma é treino

        /**
         * ******************************
         *
         * for (int i = 0; i < allInstances[0].numAttributes(); i++) {
         * System.out.println(i+"]"+allInstances[0].attribute(i).name()); }
         *
         * ******************************
         */
        normalClass = allInstances[0].get(0).classValue();

        if (CSV) {
            System.out.println(useCase + ";acuracia;precision;recall;f1score;VP;VN;FP;FN;time");
        }

        for (ClassifierExtended classififer : Parameters.CLASSIFIERS_FOREACH) {
            selectedClassifier = classififer.getClassifier();
            GenericResultado r = testaEssaGalera(classififer.getClassifierName());
            String acuracia = String.valueOf(r.getAcuracia()).replace(".", ",");
            String precision = String.valueOf(r.getPrecision()).replace(".", ",");
            String recall = String.valueOf(r.getRecall()).replace(".", ",");
            String f1score = String.valueOf(r.getF1Score()).replace(".", ",");

            if (CSV) {
                System.out.println(
                        classififer.getClassifierName() + ";"
                        + acuracia.replace(",", ".") + ";"
                        + precision.replace(",", ".") + ";"
                        + recall.replace(",", ".") + ";"
                        + f1score.replace(",", ".") + ";"
                        + r.getVP() + ";"
                        + r.getVN() + ";"
                        + r.getFP() + ";"
                        + r.getFN() + ";"
                        + r.getTime()
                );
            } else {
                System.out.println("VP: " + r.getVP() + "  ->  acc:" + r.getAcuracia());
            }
        }
    }

    public static GenericResultado testaEssaGalera(String descricao) throws Exception {
        selectedClassifier.buildClassifier(allInstances[0]);
        if (printTime) {
            System.out.println("---------");
            System.out.println(descricao);
        }

        // Resultados
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;
        long time = System.nanoTime();
        long cumulativo = 0;
        int[] uc00 = {0, 0, 0, 0, 0, 0, 0};
        int[] uc01 = {0, 0, 0, 0, 0, 0, 0};
        int[] uc02 = {0, 0, 0, 0, 0, 0, 0};
        int[] uc03 = {0, 0, 0, 0, 0, 0, 0};
        int[] uc04 = {0, 0, 0, 0, 0, 0, 0};
        int[] uc05 = {0, 0, 0, 0, 0, 0, 0};
        int[] uc06 = {0, 0, 0, 0, 0, 0, 0};
        int confusionMatrix[][] = {uc00, uc01, uc02, uc03, uc04, uc05, uc06};

        for (int i = 0; i < allInstances[1].size(); i++) {
            Instance testando = allInstances[1].instance(i);

            long antes = System.nanoTime();
            double resultado = selectedClassifier.classifyInstance(testando);
            double esperado = testando.classValue();
            // Confusion matrix:
            confusionMatrix[(int) esperado][(int) resultado] = confusionMatrix[(int) esperado][(int) resultado] + 1;
            //System.out.println("confusionMatrix[" + (int) esperado + "][" + (int) resultado + "] = " + (confusionMatrix[(int) esperado][(int) resultado] + 1));
            long depois = (System.nanoTime() - antes); // / 1000;
            if (printTime) {
                System.out.println(depois);
            }
            cumulativo = cumulativo + depois;
            if (resultado == esperado) {
                if (resultado == normalClass) {
                    VN = VN + 1;
                } else {
                    VP = VP + 1;
                }
            } else {
                if (resultado == normalClass) {
                    FN = FN + 1;
                    //System.out.println("Esperado vs Resultado: " + esperado + " vs " + resultado);

                } else {
                    FP = FP + 1;
                    //System.out.println("Esperado vs Resultado: " + esperado + " vs " + resultado);

                }
            }
        }

        long timeEnd = (System.nanoTime() - time) / 1000;

        GenericResultado r = new GenericResultado(descricao, VP, FN, VN, FP, cumulativo / 1000000, confusionMatrix);
        return r;

    }

}
