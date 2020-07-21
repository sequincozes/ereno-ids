/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.legacy.substation;

import br.uff.midiacom.ereno.abstractclassification.ClassifierExtended;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.output.prediction.CSV;
import weka.clusterers.SimpleKMeans;
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

    public static void main(String[] args) throws Exception {
        allInstances = Util.loadAndFilter(true);
//
//        semClustering();

//        System.out.println("classifier;VP;VN;FP;FN;acuracy");
//        for (ClassifierExtended classififer : Parameters.CLASSIFIERS_FOREACH) {
        ClassifierExtended classififer = Parameters.CLASSIFIERS_FOREACH[3];
        selectedClassifier = classififer.getClassifier();
        GenericResultado r = testaEssaGalera(classififer.getClassifierName());
        String acuracia = String.valueOf(r.getAcuracia()).replace(".", ",");

        if (CSV) {
            System.out.println(
                    classififer.getClassifierName() + ";"
                    + r.getVP() + ";"
                    + r.getVN() + ";"
                    + r.getFP() + ";"
                    + r.getFN() + ";"
                    + acuracia
            );
        } else {
//                System.out.println("VP: " + r.getVP() + "  ->  acc:" + r.getAcuracia());
        }
//        }

    }

    public static GenericResultado testaEssaGalera(String descricao) throws Exception {
        selectedClassifier.buildClassifier(allInstances[0]);
        System.out.println("---------");
        System.out.println(descricao);
        // Resultados
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;
        int VP = 0;
        int VN = 0;
        int FP = 0;
        int FN = 0;
        long time = System.nanoTime();
        // Validação de ataques
//        System.out.println(" *** Ataques *** ");
        for (int i = 0; i < allInstances[2].size(); i++) {
            Instance testando = allInstances[2].instance(i);
            for (int k = 0; k < 164; k++) {
                double antes = System.nanoTime();
                double res1 = selectedClassifier.classifyInstance(testando);
                double depois = (System.nanoTime() - antes) / 1000;
                System.out.println(depois);

                if (res1 == testando.classValue()) {
                    VP = VP + 1;
//                System.out.println("Detectou: " + testando);
                } else {
                    FN = FN + 1;
                }
            }
        }

        // Validação de normais
//        System.out.println(" *** Normais *** ");
        for (int i = 0; i < allInstances[1].size(); i++) {
            Instance testando2 = allInstances[1].instance(i);
            for (int k = 0; k < 500; k++) {
                double antes = System.nanoTime();
                double res2 = selectedClassifier.classifyInstance(testando2);
                double depoisN = (System.nanoTime() - antes);
                double depoisM = (System.nanoTime() - antes) / 1000;
                double depoisMS = (System.nanoTime() - antes) / 1000000;
                System.out.println(depoisM);

                if (res2 == testando2.classValue()) {
                    VN = VN + 1;
                } else {
                    FP = FP + 1;
//                System.out.println("Confundiu: " + testando2 + "("+testando2.classValue()+") com " + res2);

                }
            }
        }
        long timeEnd = (System.nanoTime() - time) / 1000;

        try {

            acuracia = Float.valueOf(((VP + VN)) * 100) / Float.valueOf((VP + VN + FP + FN));
            txDec = Float.valueOf((VP * 100)) / Float.valueOf((VP + FN));  // Sensitividade ou Taxa de Detecção
            txAFal = Float.valueOf((FP * 100)) / Float.valueOf((VN + FP)); // Especificade ou Taxa de Alarmes Falsos    
        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + VP + " + " + VN + ") * 100) / (" + VP + " + " + VN + "+ " + FP + "+" + FN + "))");
        }
        //Resultado r = new Resultado(descricao, VP, FN, VN, FP, acuracia, txDec, txAFal, timeEnd);
        GenericResultado r = new GenericResultado(descricao, VP, FN, VN, FP, timeEnd);
        return r;

    }

}
