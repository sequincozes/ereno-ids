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
public class GraspVND extends Grasp {

    public GraspVND() {
        super();
    }

    public GraspSolution runGraspVND(int[] rcl, String methodChoosen, String dataset) throws Exception {
        outputManager = new FirebaseOutput().initialize(methodChoosen, dataset);
        this.beginTime = System.currentTimeMillis();
        outputManager.writeBeginTime();

        System.out.println("######### ITERATION (" + iterationNumber + ") #############");

        /* RCL Baseada no Critério OneR */
        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = constructSolution(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.doVND(initialSolution, this);
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
            avaliar(reconstructedSoluction);

            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.doVND(reconstructedSoluction, this);
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
