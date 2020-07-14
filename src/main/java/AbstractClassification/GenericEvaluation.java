/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstractClassification;

import static AbstractClassification.GenericClassifiers.NAIVE_BAYES;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class GenericEvaluation {

    static boolean rawOutput = false;
    static boolean debug = false;
    static boolean SIMPLE = false;
    static boolean ERROR = false;
    static double normalClass;

    public static GenericResultado[] runMultipleClassifier(Instances train, Instances test) throws Exception {
        GenericResultado[] results = new GenericResultado[GeneralParameters.CLASSIFIERS_FOREACH.length];
        int run = 0;
        for (ClassifierExtended classififer : GeneralParameters.CLASSIFIERS_FOREACH) {
            GenericResultado r = testaEssaGalera(classififer, train, test, false);
            results[run++] = r;
            if (GeneralParameters.CSV) {
                System.out.println(
                        classififer.getClassifierName() + ";"
                        + String.valueOf(r.getAcuracia()).replace(",", ".") + ";"
                        + String.valueOf(r.getPrecision()).replace(",", ".") + ";"
                        + String.valueOf(r.getRecall()).replace(",", ".") + ";"
                        + String.valueOf(r.getF1Score()).replace(",", ".") + ";"
                        + r.getVP() + ";"
                        + r.getVN() + ";"
                        + r.getFP() + ";"
                        + r.getFN() + ";"
                        + r.getTime()
                );
            } else if (SIMPLE) {
                System.out.println("Classificador: " + classififer.getClassifierName() + " -> " + String.valueOf(r.getAcuracia()).substring(0, 5) + "%");
            } else if (ERROR) {
                //WTF?
            }
        }
        return results;
    }

    public static GenericResultado[] TEMP(Instances train, Instances test) throws Exception {
        GenericResultado[] results = new GenericResultado[GeneralParameters.CLASSIFIERS_FOREACH.length];
        int run = 0;
        for (ClassifierExtended classififer : GeneralParameters.CLASSIFIERS_FOREACH) {
            GenericResultado r = testaEssaGalera(classififer, train, test, false);
            results[run++] = r;
            if (GeneralParameters.CSV) {
                System.out.println(
                        classififer.getClassifierName() + ";"
                        + String.valueOf(r.getAcuracia()).replace(",", ".") + ";"
                        + String.valueOf(r.getPrecision()).replace(",", ".") + ";"
                        + String.valueOf(r.getRecall()).replace(",", ".") + ";"
                        + String.valueOf(r.getF1Score()).replace(",", ".") + ";"
                        + r.getVP() + ";"
                        + r.getVN() + ";"
                        + r.getFP() + ";"
                        + r.getFN() + ";"
                        + r.getTime()
                );
            } else if (SIMPLE) {
                System.out.println("Classificador: " + classififer.getClassifierName() + " -> " + String.valueOf(r.getAcuracia()).substring(0, 5) + "%");
            } else if (ERROR) {
                //WTF?
            }
        }
        return results;
    }

    public static GenericResultado runSingleClassifier(Instances train, Instances test) throws Exception {
        ClassifierExtended classififer = GeneralParameters.SINGLE_CLASSIFIER_MODE;
        GenericResultado r = testaEssaGalera(classififer, train, test, false);

        if (GeneralParameters.CSV) {
            System.out.println(
                    r.Cx + ";"
                    + String.valueOf(r.getAcuracia()).replace(",", ".") + ";"
                    + String.valueOf(r.getPrecision()).replace(",", ".") + ";"
                    + String.valueOf(r.getRecall()).replace(",", ".") + ";"
                    + String.valueOf(r.getF1Score()).replace(",", ".") + ";"
                    + r.getVP() + ";"
                    + r.getVN() + ";"
                    + r.getFP() + ";"
                    + r.getFN() + ";"
                    + r.getTime()
            );
        } else if (SIMPLE) {
            System.out.println("Classificador: " + classififer.getClassifierName() + " -> " + String.valueOf(r.getAcuracia()).substring(0, 5) + "%");
        }

        return r;
    }

    private static GenericResultado testaEssaGalera(ClassifierExtended selectedClassifier, Instances train, Instances test, boolean timeTest) throws Exception {
        selectedClassifier.getClassifier().buildClassifier(train);
        if (timeTest) {
            System.out.println("---------");
            System.out.println(selectedClassifier.getClassifierName());
        }

        // Resultados
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;
        long time = System.nanoTime();
        long cumulativo = 0;
        int vectorPosErrors[] = new int[1000];

        for (int i = 0; i < test.size(); i++) {
            Instance testando = test.instance(i);
            long antes = System.nanoTime();
            double res1 = selectedClassifier.getClassifier().classifyInstance(testando);
            long depois = (System.nanoTime() - antes);
            if (timeTest) {
                System.out.println(depois);
            }
            cumulativo = cumulativo + depois;
            if (res1 == testando.classValue()) {
                if (res1 == normalClass) {
                    VN = VN + 1;
                } else {
                    VP = VP + 1;
                }
            } else {
                //  String str = testando.toStringMaxDecimalDigits(10).substring(0, 3).replace("0,", "0").replace("1,", "0.1");
                // float pos = Float.valueOf(str) * 10;
                // int posi = (int) pos;

                if (res1 == normalClass) {
                    FN = FN + 1;
                } else {
                    FP = FP + 1;
                    // vectorPosErrors[posi] = vectorPosErrors[posi] + 1;
                }
            }
        }
        if (ERROR) {
            System.out.println("Results");
            int ll = 0;
        }

        try {
            acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
            txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
            txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos    
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
        }
//        Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal, cumulativo / (VP + VN + FP + FN));
        GenericResultado r = new GenericResultado(selectedClassifier.getClassifierName(), VP, FN, VN, FP, cumulativo);
        return r;

    }

}
