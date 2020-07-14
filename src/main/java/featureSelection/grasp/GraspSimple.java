/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featureSelection.grasp;

import AbstractClassification.GeneralParameters;
import featureSelection.grasp.localsearch.LocalSearches;
import featureSelection.grasp.neighborhoodStructures.BitFlip;
import featureSelection.grasp.neighborhoodStructures.IWSS;
import featureSelection.grasp.neighborhoodStructures.IWSSr;
import featureSelection.grasp.neighborhoodStructures.NeighborhoodStructure;
import featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author sequi
 */
public class GraspSimple extends Grasp { //@TODO: Create abstract GRASP

    public GraspSolution runGraspSimple(int[] rcl, String methodChoosen, NeighborhoodStructures selectedNeighborhood) throws Exception {
        this.method = methodChoosen;
        generateExperimentIdentifier();

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
        GraspSolution initialSolution = buildSolucaoInicial(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.buscaLocal(initialSolution, neighborhood);
        if (initialSolution.isEqualOrBetterThan(getBestGlobalSolution())) {
            setBestGlobalSolution(initialSolution.newClone(false));
        }

        writeHeaders();

        while (iteration < this.maxIterations && noImprovements < this.maxNoImprovement) {
            System.out.println("IT: " + iteration + "/" + maxIterations + " .. noImp: " + noImprovements + "/" + maxNoImprovement);
            long time = System.currentTimeMillis();

            iteration = ++iteration;
            System.out.println("######### ITERATION (" + iteration + ") #############");

            GraspSolution reconstructedSoluction = initialSolution.reconstruirNewSolucao(NUM_FEATURES);

            // Avalia Solução
            avaliar(reconstructedSoluction);
            if (initialSolution.isBetterThan(getBestGlobalSolution())) {
                setBestGlobalSolution(initialSolution.newClone(false));
                noImprovements = 0;
            }
            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.buscaLocal(reconstructedSoluction, neighborhood);

            if (reconstructedSoluction.isEqualOrBetterThan(getBestGlobalSolution())) {
                setBestGlobalSolution(reconstructedSoluction.newClone(false));
                noImprovements = 0;
            } else {
                noImprovements = ++noImprovements;
            }

            if (String.valueOf(getBestGlobalSolution().getEvaluation().getAcuracia()).length() > 7) {
                System.out.println("######### Fim ITERAÇÂO (" + iteration + ") - Acc:" + String.valueOf(getBestGlobalSolution().getEvaluation().getAcuracia()).substring(0, 7) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())) + " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
            } else {
                System.out.println("######### Fim ITERAÇÂO (" + iteration + ") - Acc:" + String.valueOf(getBestGlobalSolution().getEvaluation().getAcuracia()) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())) + " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
            }
            writeIteration(iteration, getBestGlobalSolution().getEvaluation().getAcuracia(), getBestGlobalSolution().getEvaluation().getF1Score(), getBestGlobalSolution().getEvaluation().getPrecision(), getBestGlobalSolution().getEvaluation().getRecall(), (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())), " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
        }
        return getBestGlobalSolution();
    }

}
