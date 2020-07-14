/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package featureSelection.grasp;

import featureSelection.grasp.localsearch.LocalSearches;
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

    public GraspSolution runGraspVND(int[] rcl, String methodChoosen) throws Exception {
        this.method = methodChoosen;
        generateExperimentIdentifier();
        
        int iteration = 1;
        int noImprovement = 0;
        System.out.println("######### ITERATION (" + iteration + ") #############");

        /* RCL Baseada no Critério OneR */
        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = buildSolucaoInicial(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.doVND(initialSolution, this);
        iteration = iteration++;
        if (initialSolution.isBetterThan(getBestGlobalSolution())) {
            setBestGlobalSolution(initialSolution.newClone(false));
        }

        writeHeaders();
        while (iteration < this.maxIterations && noImprovement < this.maxNoImprovement) {
            iteration = ++iteration;
            System.out.println("######### ITERATION (" + iteration + ") #############");
            long time = System.currentTimeMillis();

            GraspSolution reconstructedSoluction = initialSolution.reconstruirNewSolucao(NUM_FEATURES);
            avaliar(reconstructedSoluction);

            // Busca por Ótimo Local
            reconstructedSoluction = LocalSearches.doVND(reconstructedSoluction, this);
            if (reconstructedSoluction.isEqualOrBetterThan(getBestGlobalSolution())) {
                setBestGlobalSolution(reconstructedSoluction.newClone(false));
                System.out.println("GLOBAL IMPROVEMENT: " + Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas()) + " = " + getBestGlobalSolution().getEvaluation().getAcuracia());
                noImprovement = 0;
            } else {
                noImprovement = ++noImprovement;
            }
            System.out.println("######### Fim ITERAÇÂO (" + iteration + ") - Acc:" + String.valueOf(getBestGlobalSolution().getEvaluation().getAcuracia()).substring(0, 7) + "% - Conjunto = " + (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())));// " PROVA: " + ValidacaoCICIDS2017.executar(bestGlobal.getArrayFeaturesSelecionadas()).getAcuracia()));
            writeIteration(iteration, getBestGlobalSolution().getEvaluation().getAcuracia(), getBestGlobalSolution().getEvaluation().getF1Score(), getBestGlobalSolution().getEvaluation().getPrecision(), getBestGlobalSolution().getEvaluation().getRecall(), (Arrays.toString(getBestGlobalSolution().getArrayFeaturesSelecionadas())), " Iteration time: " + (System.currentTimeMillis() - time) / 1000 + " seconds.");
        }
        return getBestGlobalSolution();
    }

}
