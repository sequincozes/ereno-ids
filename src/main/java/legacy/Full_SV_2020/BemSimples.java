/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package legacy.Full_SV_2020;

import AbstractClassification.GenericResultado;
import AbstractClassification.ClassifierExtended;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author silvio
 */
public class BemSimples {

    static Instances[] allInstances;
    static Classifier selectedClassifier = Parameters.NAIVE_BAYES.getClassifier();
    static boolean rawOutput = false;
    static boolean debug = false;
    static boolean CSV = false;
    static boolean SIMPLE = false;
    static boolean ERROR = true;
    static double normalClass;

    public static void main(String[] args) throws Exception {
        allInstances = Util.loadAndFilter(false, 10); // a cada 10 instancias, uma é treino
        normalClass = allInstances[0].get(0).classValue();
        for (ClassifierExtended classififer : Parameters.CLASSIFIERS_FOREACH) {
            // ClassifierExtended classififer = Parameters.CLASSIFIERS_FOREACH[4];

            selectedClassifier = classififer.getClassifier();
            GenericResultado r = testaEssaGalera(classififer.getClassifierName(), false);
            String acuracia = String.valueOf(r.getAcuracia()).replace(".", ",");

            if (CSV) {
                System.out.println(
                        classififer.getClassifierName() + ";"
                        + r.getVP() + ";"
                        + r.getVN() + ";"
                        + r.getFP() + ";"
                        + r.getFN() + ";"
                        + acuracia.replace(",", ".") + ";"
                        + r.getTime()
                );
            } else if (SIMPLE) {
                System.out.println("Classificador: " + classififer.getClassifierName() + " -> " + String.valueOf(r.getAcuracia()).substring(0, 5) + "%");
            } else if (ERROR) {

            }
        }
    }

    public static GenericResultado testaEssaGalera(String descricao, boolean timeTest) throws Exception {
        selectedClassifier.buildClassifier(allInstances[0]);
        if (timeTest) {
            System.out.println("---------");
            System.out.println(descricao);
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

        for (int i = 0; i < allInstances[1].size(); i++) {
            Instance testando = allInstances[1].instance(i);
            //for (int k = 0; k < 164; k++) {

            long antes = System.nanoTime();
            double res1 = selectedClassifier.classifyInstance(testando);
            long depois = (System.nanoTime() - antes); // / 1000;
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
                String str = testando.toStringMaxDecimalDigits(10).substring(0, 3).replace("0,", "0").replace("1,", "0.1");
                float pos = Float.valueOf(str) * 10;
                int posi = (int) pos;

                if (res1 == normalClass) {
                    FN = FN + 1;
                } else {
                    FP = FP + 1;
                    //System.out.println("testando: " + testando);
                    vectorPosErrors[posi] = vectorPosErrors[posi] + 1;

                }
                //  break;
            }
            //}
        }
        if (ERROR) {
            System.out.println("Results");
            int ll = 0;
            // for (int qtd : vectorPosErrors) {
            //     System.out.println(ll++ + ">" + qtd);
            //}
        }

        try {
            acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
            txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
            txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos    
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
        }
//        Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal, cumulativo / (VP + VN + FP + FN));
        GenericResultado r = new GenericResultado(descricao, VP, FN, VN, FP, cumulativo);
        return r;

    }

}
