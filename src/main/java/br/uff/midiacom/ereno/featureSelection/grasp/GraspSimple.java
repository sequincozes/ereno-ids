/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp;

import br.uff.midiacom.ereno.featureSelection.grasp.localsearch.LocalSearches;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.BitFlip;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSS;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSSr;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructure;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;
import br.uff.midiacom.ereno.outputManager.FirebaseOutput;
import br.uff.midiacom.ereno.outputManager.LocalOutput;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author sequi
 */
public class GraspSimple extends Grasp { //@TODO: Create abstract GRASP

    public GraspSolution runGraspSimple(int[] rcl, String methodChoosen, NeighborhoodStructures selectedNeighborhood, String dataset) throws Exception {
        System.out.println("Wellcome to GRASP!");
        outputManager = new FirebaseOutput().initialize(methodChoosen, dataset);
        this.beginTime = System.currentTimeMillis();
        outputManager.writeBeginTime();
        
        NeighborhoodStructure neighborhood = null;
        if (null != selectedNeighborhood) {
            switch (selectedNeighborhood) {
                case BIT_FLIP:
                    neighborhood = new BitFlip(this);
                    break;
                case IWSS:
                    neighborhood = new IWSS(this);
                    break;
                case IWSSR:
                    neighborhood = new IWSSr(this);
                    break;
                default:
                    break;
            }
        }

        /* RCL Baseada no Critério OneR */
        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = constructSolution(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.buscaLocal(initialSolution, neighborhood, this);
        if (initialSolution.isBetterThan(getBestGlobalSolution(),criteriaMetric)) {
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
            System.out.println("IT: " + iterationNumber + "/" + maxIterations + " .. noImp: " + noImprovements + "/" + maxNoImprovement);
            long time = System.currentTimeMillis();

            iterationNumber = ++iterationNumber;
            System.out.println("######### ITERATION (" + iterationNumber + ") #############");

            GraspSolution reconstructedSoluction = reconstructSolution(initialSolution);

            // Avalia Solução
            avaliar(reconstructedSoluction);
            if (initialSolution.isBetterThan(getBestGlobalSolution(),criteriaMetric)) {
                setBestGlobalSolution(initialSolution.newClone(false));
                noImprovements = 0;
            }
            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.buscaLocal(reconstructedSoluction, neighborhood, this);

            if (reconstructedSoluction.isBetterThan(getBestGlobalSolution(),criteriaMetric)) {
                setBestGlobalSolution(reconstructedSoluction.newClone(false));
                noImprovements = 0;
            } else {
                noImprovements = ++noImprovements;
            }

            if (String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()).length() > 7) {
                System.out.println("######### Fim ITERAÇÂO (" + iterationNumber + ") - F1Score:" + String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()).substring(0, 7) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())) + " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
            } else {
                System.out.println("######### Fim ITERAÇÂO (" + iterationNumber + ") - F1Score:" + String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())) + " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
            }
            //    writeIteration(iteration, getBestGlobalSolution().getEvaluation().getAcuracia(), getBestGlobalSolution().getEvaluation().getF1Score(), getBestGlobalSolution().getEvaluation().getPrecision(), getBestGlobalSolution().getEvaluation().getRecall(), (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())), " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
            currentTime = System.currentTimeMillis() - beginTime;
            System.out.println("######### Fim ITERAÇÂO (" + iterationNumber + " / Current Time:" + (currentTime / 1000 / 60) + "min) - F1Score:" + String.valueOf(getBestGlobalSolution().getEvaluation().getF1Score()) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())));// " PROVA: " + ValidacaoCICIDS2017.executar(bestGlobal.getArrayFeaturesSelecionadas()).getAcuracia()));
            outputManager.writeIteration(new Iteration(getBestGlobalSolution().getF1Score(), getBestGlobalSolution().getFeatureSet(), iterationNumber, noImprovements, numberEvaluation, (currentTime / 1000 / 60) + "min"));

            //   Thread.sleep(3000);
            //   System.exit(0);
        }
        return getBestGlobalSolution();
    }

}
