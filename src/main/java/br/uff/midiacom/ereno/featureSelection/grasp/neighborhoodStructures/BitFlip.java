/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author silvio
 */
public class BitFlip implements NeighborhoodStructure {

    GraspSolution bestLocal;
    Grasp grasp;
    int remLSIterations = 20;
    int remLSNoImprovements = 10;

    public BitFlip(Grasp grasp) {
        this.grasp = grasp;
    }

    private GraspSolution performSingleMoviment(GraspSolution reference) throws Exception {
        GraspSolution neighborSolution = reference.newClone(true);
        Random r = new Random();
        int rem = r.nextInt(neighborSolution.getNumSelectedFeatures() - 1);
        int add = r.nextInt(neighborSolution.getNumRCLFeatures() - 1);
        //System.out.println("RCL: " + neighborSolution.getNumRCLFeatures() + "/sel: " + neighborSolution.getNumSelectedFeatures() + " (add:" + add + " rem:" + rem + ")");
        neighborSolution.replaceFeature(
                r.nextInt(rem + 1),
                r.nextInt(add + 1));
        neighborSolution = grasp.avaliar(neighborSolution);
        return neighborSolution;
    }

    @Override
    public GraspSolution run(GraspSolution reference) throws Exception {
        bestLocal = reference.newClone(false); // initialization

        while (--remLSIterations > 0 && --remLSNoImprovements > 0) {
            GraspSolution neighborSolution = performSingleMoviment(reference);
            if (neighborSolution.isBetterThan(bestLocal)) {
                bestLocal = neighborSolution.newClone(false);
                remLSNoImprovements = 10;
            }
        }
        remLSIterations = 20;
        remLSNoImprovements = 10;
        return bestLocal;
    }

}
