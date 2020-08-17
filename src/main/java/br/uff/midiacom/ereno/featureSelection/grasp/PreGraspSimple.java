/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp;

import br.uff.midiacom.ereno.evaluation.GraspMetrics;
import br.uff.midiacom.ereno.featureSelection.grasp.localsearch.LocalSearches;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructure;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructures;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.PreGraspIWSSr;
import br.uff.midiacom.ereno.outputManager.FirebaseOutput;
import java.util.ArrayList;

/**
 *
 * @author sequi
 */
public class PreGraspSimple extends PreGrasp { //@TODO: Create abstract GRASP

    public GraspSolution runGraspSimple(int[] rcl, String methodChoosen, NeighborhoodStructures selectedNeighborhood, String dataset) throws Exception {
        outputManager = new FirebaseOutput().initialize(methodChoosen, dataset);
        this.beginTime = System.currentTimeMillis();
        outputManager.writeBeginTime();
        GraspMetrics criteria = GraspMetrics.F1SCORE;
        NeighborhoodStructure neighborhood = new PreGraspIWSSr(this);

        /* Add all rcl features to RCL */
        ArrayList<Integer> RCL = buildCustomRCL(rcl);

        // Solução Inicial Factível
        GraspSolution initialSolution = buildIWSSSolucaoInicial(RCL);
        initialSolution = avaliar(initialSolution);
        setBestGlobalSolution(initialSolution.newClone(false));

        /* Gera uma solução vizinha igual ou melhor */
        initialSolution = LocalSearches.buscaLocal(initialSolution, neighborhood, new GraspSimple());
        if (initialSolution.isBetterThan(getBestGlobalSolution(),criteria)) {
            setBestGlobalSolution(initialSolution.newClone(false));
        }

        return getBestGlobalSolution();
    }

}
