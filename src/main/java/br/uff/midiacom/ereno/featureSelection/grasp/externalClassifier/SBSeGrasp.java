/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.externalClassifier;

import br.uff.midiacom.ereno.abstractclassification.*;
import br.uff.midiacom.ereno.evaluation.GraspMetrics;
import br.uff.midiacom.ereno.featureSelection.grasp.*;
import br.uff.midiacom.ereno.featureSelection.grasp.localsearch.LocalSearches;
import br.uff.midiacom.ereno.outputManager.FirebaseOutput;
import br.uff.midiacom.ereno.outputManager.LocalOutput;
import br.uff.midiacom.ereno.outputManager.OutputManager;
import br.uff.midiacom.ereno.outputManager.model.Detail;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author sequi
 */
public class SBSeGrasp {

    public SBSeGrasp() {

    }

    public static Instances allInstances;

    public static GraspMetrics criteriaMetric = GraspMetrics.F1SCORE;// GraspMetrics.F1SCORE;

    public int maxTime = 24 * 60 * 60 * 1000; // quantidade total de iteracoes
    int maxIterations = 100; // quantidade total de iteracoes
    public int maxNumberEvaluation = 1000; // quantidade total de avaliações (50*20)mc
    final int maxNoImprovement = 100; // iteracoes sem melhorias consecutivas
    static int NUM_FEATURES = 1;
    //String method = "method";
    //String experimentIdentifier = "0000000";
    boolean localOutput = false;

    // Current Run Data
    int iterationNumber = 0;
    int noImprovements = 0;
    public long currentTime = 0;
    boolean printSelection = false;
    public int numberEvaluation = 0;
    OutputManager outputManager;
    private GraspSolution bestGlobalSolution;
    public int numBitFLipFeatures = 5;
    long beginTime = 0;
    static boolean FIXED_LENGTH = false;

    public static void main(String[] args) throws Exception {
        int[] rcl = new int[]{1, 2, 3, 4, 5, 6, 7, 8};

        if (args.length > 0) {
            try {
                NUM_FEATURES = Integer.parseInt(args[0]);
                FIXED_LENGTH = true;
            } catch (Exception e) {
                System.err.println("Expected argument: number of features (or anything)");
                System.exit(1);
            }
        } else {
            FIXED_LENGTH = false;
            NUM_FEATURES = new Random(System.currentTimeMillis()).nextInt(rcl.length);
        }
        SBSeGrasp sbSeGrasp = new SBSeGrasp();
        sbSeGrasp.runSbSeGrasp(rcl, "GRASP-VND", "NSL-KDD-NORMALIZED");
    }


    public GraspSolution getBestGlobalSolution() {
        return bestGlobalSolution;
    }

    public void setBestGlobalSolution(GraspSolution bestGlobalSolution) throws IOException {
        this.bestGlobalSolution = bestGlobalSolution;
        outputManager.writeBestImprovement(new Detail(bestGlobalSolution.getF1Score(), bestGlobalSolution.getFeatureSet(), numberEvaluation, bestGlobalSolution.getEvaluation().getTime()));
    }

    public ArrayList<Integer> buildCustomRCL(int[] RCL) throws Exception {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < RCL.length; i++) {
            candidates.add(RCL[i]);
        }

        return candidates;
    }

    public GraspSolution reconstructSolution(GraspSolution initialSolution) {
        ArrayList<Integer> fullRCL = initialSolution.copyRCLFeatures();
        fullRCL.addAll(initialSolution.copySolutionFeatures());
        System.out.println("RCL: " + fullRCL);
        /* Seleciona as features das primeiras N posicoes como solução inicial*/
        GraspSolution solution = new GraspSolution();
        Random r = new Random(System.currentTimeMillis());
        if (!FIXED_LENGTH) {
            NUM_FEATURES = r.nextInt(fullRCL.size());
        }
        while (solution.getArrayFeaturesSelecionadas().length < NUM_FEATURES) {
            solution.addFeature(fullRCL.remove(r.nextInt(fullRCL.size())));
        }

        // As demais features irão compor a RCL_flip 
        while (fullRCL.size() > 0) {
            solution.addFeatureFlip(fullRCL.remove(0));
        }
        System.out.println("Solução Inicial: " + solution.getFeatureSet());
        System.out.println("RCL Restante: " + solution.getRCLfeatures());

        return solution;
    }

    public GraspSolution constructSolution(ArrayList<Integer> fullRCL) {

        System.out.println("RCL: " + fullRCL);
        /* Seleciona as features das primeiras N posicoes como solução inicial*/
        GraspSolution solution = new GraspSolution();
        Random r = new Random(System.currentTimeMillis());
        while (solution.getArrayFeaturesSelecionadas().length < NUM_FEATURES) {
            solution.addFeature(fullRCL.remove(r.nextInt(fullRCL.size())));
        }

        // As demais features irão compor a RCL_flip 
        while (fullRCL.size() > 0) {
            solution.addFeatureFlip(fullRCL.remove(0));
        }
        System.out.println("Solução Inicial: " + solution.getFeatureSet());
        System.out.println("RCL Restante: " + solution.getRCLfeatures());

        return solution;
    }

    public GraspSolution externalEvaluation(GraspSolution solution) throws Exception {
        solution.setCurrentTimeSeconds((beginTime / 1000) - (System.currentTimeMillis() / 1000));
        if (GeneralParameters.DEBUG_MODE) {
            System.out.println("Dataset: " + GeneralParameters.DATASET);
            System.out.println("Classifier: " + GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName());
            System.out.println("Folds: " + GeneralParameters.FOLDS);
            System.out.println("Seed: " + GeneralParameters.GRASP_SEED);
        }
        GeneralParameters.FEATURE_SELECTION = solution.getArrayFeaturesSelecionadas();

        try {
            float f1Score = ExternalClassifier.externalEvaluation(Arrays.toString(GeneralParameters.FEATURE_SELECTION));
            solution.setEvaluation(f1Score);
            System.err.println("F1Score: " + f1Score);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("It is necessary to set TRAIN and TEST datasets when cross-validation is off.");
            System.err.println("Solution: " + solution);
            System.err.println("Evaluation: " + solution.getEvaluation());
            System.exit(1);
        }

        if (getBestGlobalSolution() == null || getBestGlobalSolution().equals(null)) {
            solution.setEvaluation(0);
            String avaliacao = "EV;" + numberEvaluation++ + ";" + solution.getEvaluation().getF1Score() + ";" + Arrays.toString(solution.getArrayFeaturesSelecionadas()) + ";(max: not yet)";
            System.out.println(avaliacao);
        } else {
            String avaliacao = "EV;" + numberEvaluation++ + ";" + solution.getEvaluation().getF1Score() + ";" + Arrays.toString(solution.getArrayFeaturesSelecionadas()) + ";(max: " + getBestGlobalSolution().getF1Score() + " -> " + getBestGlobalSolution().getFeatureSet() + ")";
            System.out.println(avaliacao);
        }

        if (GeneralParameters.DEBUG_MODE) {

            if (!(solution.getEvaluation().getF1Score() > 0)) {
                System.err.print("Problema na F1 Score: " + solution.getEvaluation().getF1Score());
                System.exit(0);
            }
        }
        if (outputManager != null)
            outputManager.writeDetail(new Detail(solution.getF1Score(), solution.getFeatureSet(), numberEvaluation, solution.getCurrentTimeSeconds()));

        return solution;
    }

    public GraspSolution runSbSeGrasp(int[] rcl, String methodChoosen, String dataset) throws Exception {
        outputManager = new FirebaseOutput().initialize(methodChoosen, dataset);
        this.beginTime = System.currentTimeMillis();
        outputManager.writeBeginTime();

        System.out.println("######### ITERATION (" + iterationNumber + ") #############");
        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = constructSolution(RCL);
        initialSolution = externalEvaluation(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.doVNDSBSeGrasp(initialSolution, this);
        iterationNumber++;
        if (initialSolution.isBetterThan(getBestGlobalSolution(), criteriaMetric)) {
            setBestGlobalSolution(initialSolution.newClone(false));
        }

        if (localOutput) {
            LocalOutput localOutputManager = (LocalOutput) outputManager;
            localOutputManager.writeHeaders();
        }
        while (iterationNumber < this.maxIterations && noImprovements < this.maxNoImprovement && currentTime < maxTime) {
            if (numberEvaluation >= maxNumberEvaluation) {
                return getBestGlobalSolution();
            }
            iterationNumber++;
            System.out.println("######### ITERATION (" + iterationNumber + ") #############");

            GraspSolution reconstructedSoluction = reconstructSolution(initialSolution);
            externalEvaluation(reconstructedSoluction);

            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.doVNDSBSeGrasp(reconstructedSoluction, this);
            if (reconstructedSoluction.isBetterThan(getBestGlobalSolution(), criteriaMetric)) {
                setBestGlobalSolution(reconstructedSoluction.newClone(false));
                System.out.println("GLOBAL IMPROVEMENT: " + Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas()) + " = " + getBestGlobalSolution().getEvaluation().getF1Score());
                noImprovements = 0;
            } else {
                System.out.print("Sem melhoras: " + noImprovements + " atualizado para ");
                noImprovements++;
                System.out.println("Sem melhoras: " + noImprovements);
            }
            currentTime = System.currentTimeMillis() - beginTime;
            System.out.println("######### Fim ITERAÇÂO (" + iterationNumber + " / Current Time:" + (currentTime / 1000 / 60) + "min) - F1Score:" + String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())));
            outputManager.writeIteration(new Iteration(getBestGlobalSolution().getF1Score(), getBestGlobalSolution().getFeatureSet(), iterationNumber, noImprovements, numberEvaluation, (currentTime / 1000 / 60) + "min"));
        }
        return getBestGlobalSolution();
    }
}
