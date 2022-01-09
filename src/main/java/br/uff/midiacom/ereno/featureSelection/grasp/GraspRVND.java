/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp;

import br.uff.midiacom.ereno.featureSelection.grasp.localsearch.LocalSearches;
import br.uff.midiacom.ereno.outputManager.FirebaseOutput;
import br.uff.midiacom.ereno.outputManager.LocalOutput;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author silvio
 */
public class GraspRVND extends Grasp {

    public GraspSolution run(int[] rcl, String methodChoosen, String dataset) throws Exception {
        outputManager = new FirebaseOutput().initialize(methodChoosen, dataset);
        this.beginTime = System.currentTimeMillis();
        outputManager.writeBeginTime();

        /* RCL Baseada no Critério OneR */
        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = constructSolution(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));
        System.out.println("%%%% solucaoInicial: " + getBestGlobalSolution().getEvaluation().getF1Score());
        System.out.println("%%%% bestGlobal: " + getBestGlobalSolution().getEvaluation().getF1Score());

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.doRVND(initialSolution, this);
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

            iterationNumber = ++iterationNumber;
            System.out.println("############# ITERATION (" + iterationNumber + ") #############");

            GraspSolution reconstructedSoluction = reconstructSolution(initialSolution);

            // Avalia Solução
            avaliar(reconstructedSoluction);

            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.doRVND(reconstructedSoluction, this);
            if (reconstructedSoluction.isBetterThan(getBestGlobalSolution(),criteriaMetric)) {
                setBestGlobalSolution(reconstructedSoluction.newClone(false));
                noImprovements = 0;
            } else {
                noImprovements++;
            }

            currentTime = System.currentTimeMillis() - beginTime;
            System.out.println("######### Fim ITERAÇÂO (" + iterationNumber + " / Current Time:" + (currentTime / 1000 / 60) + "min) - F1Score:" + String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())));// " PROVA: " + ValidacaoCICIDS2017.executar(bestGlobal.getArrayFeaturesSelecionadas()).getAcuracia()));
            outputManager.writeIteration(new Iteration(getBestGlobalSolution().getF1Score(), getBestGlobalSolution().getFeatureSet(), iterationNumber, noImprovements, numberEvaluation, (currentTime / 1000 / 60) + "min"));

        }
        return getBestGlobalSolution();
    }

    public GraspSolution run(int[] rcl, String methodChoosen) throws Exception {
        outputManager = new FirebaseOutput().initialize(methodChoosen, "thesis");
        this.beginTime = System.currentTimeMillis();
        outputManager.writeBeginTime();

        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = constructSolution(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));
        System.out.println("%%%% solucaoInicial: " + getBestGlobalSolution().getEvaluation().getF1Score());
        System.out.println("%%%% bestGlobal: " + getBestGlobalSolution().getEvaluation().getF1Score());

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.doRVND(initialSolution, this);
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

            iterationNumber = ++iterationNumber;
            System.out.println("############# ITERATION (" + iterationNumber + ") #############");

            GraspSolution reconstructedSoluction = reconstructSolution(initialSolution);

            // Avalia Solução
            avaliar(reconstructedSoluction);

            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.doRVND(reconstructedSoluction, this);
            if (reconstructedSoluction.isBetterThan(getBestGlobalSolution(),criteriaMetric)) {
                setBestGlobalSolution(reconstructedSoluction.newClone(false));
                noImprovements = 0;
            } else {
                noImprovements++;
            }

            currentTime = System.currentTimeMillis() - beginTime;
            System.out.println("######### Fim ITERAÇÂO (" + iterationNumber + " / Current Time:" + (currentTime / 1000 / 60) + "min) - F1Score:" + String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())));// " PROVA: " + ValidacaoCICIDS2017.executar(bestGlobal.getArrayFeaturesSelecionadas()).getAcuracia()));
            outputManager.writeIteration(new Iteration(getBestGlobalSolution().getF1Score(), getBestGlobalSolution().getFeatureSet(), iterationNumber, noImprovements, numberEvaluation, (currentTime / 1000 / 60) + "min"));

        }
        return getBestGlobalSolution();
    }

}
