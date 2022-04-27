package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;

public class UtilVinicius {

    public static void showMemory(String task) {
        Runtime rt = Runtime.getRuntime();
        // freeMemory = memória livre alocada atual, totalMemory = memória total alocada
//                long usedMemory = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024; // converte em MB
        long usedMemory = (rt.totalMemory() - rt.freeMemory());
        System.out.println("O classificador " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName() +
                " utilizou " + usedMemory + " bytes de memória ("+ usedMemory / 1024 / 1024 +" MB) " + task + ".");
    }
}
