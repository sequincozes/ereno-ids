/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation.experiments;

/**
 *
 * @author vagne
 */
public class RodaExperimento {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    System.out.println("-h --help to options menu");
                    System.out.println("-e --experiments [Run ExperimentoVinicius.java class]");
                    System.out.println("-f --filter [Run IWSSr filter]");
                    System.out.println("-t --temp [Time verify unit]");
                    System.out.println("-fo --folds [Folds size [DEFAULT= 7]]");
                    System.out.println("-d --dataset [Change dataset [DEFAULT= KDD_DATASET]");

                    System.exit(0);
                case "--help":
                    System.out.println("-h --help to options menu");
                    System.out.println("-e --experiments [Run ExperimentoVinicius.java class]");
                    System.out.println("-f --filter [Run IWSSr filter]");
                    System.out.println("-t --temp [Time verify unit]");
                    System.out.println("-fo --folds [Folds size [DEFAULT= 7]]");
                    System.out.println("-d --dataset [Change dataset [DEFAULT= KDD_DATASET]");
                    System.exit(0);
                    break;
                case "-e":
                    file = args[i + 1];
                    //rodaExperimentoVinicius;
                    break;
                case "--experiments":
                    //rodaExperimentoVinicius;
                    break;
                case "-f":
                    //rodaFiltroIWSSr;
                    break;
                case "--filter":
                    //rodaFiltroIWSSr;
                    break;
                case "-t":
                    //verificaUnidadeTempo;
                    break;
                case "--temp":
                    //verificaUnidadeTempo;
                    break;
                case "-fo":
                    //altera GeneralParameters.FOLDS;
                    break;
                case "--folds":
                    //altera GeneralParameters.FOLDS;
                    break;
                case "-t":
                    //altera GeneralParameters.KDD_DATASET;
                    break;
                case "--temp":
                    //altera GeneralParameters.KDD_DATASET;
                    break;
            }
            i = i + 1;
        }
    }
    
    
}
