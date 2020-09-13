/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.exceptions.IncompleteFeatureSelection;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author silvio
 */
public class BitFlip implements NeighborhoodStructure {

    GraspSolution bestLocal;
    Grasp grasp;
    int remLSIterations = 50;
    int remLSNoImprovements = 20;

    public BitFlip(Grasp grasp) {
        this.grasp = grasp;
    }

    private GraspSolution performSingleMoviment(GraspSolution reference) throws IncompleteFeatureSelection, Exception {
        GraspSolution neighborSolution = reference.newClone(true);
        int rem = 0;
        int add = 0;
        if (neighborSolution.getNumSelectedFeatures() == 0 || neighborSolution.getNumRCLFeatures() == 0) {
            throw new IncompleteFeatureSelection("Selected features: " + neighborSolution.getNumSelectedFeatures() + ", RCL Size: " + neighborSolution.getNumRCLFeatures());
        } else {
            if (neighborSolution.getNumSelectedFeatures() == 1) {
                rem = 0;
            } else {
                rem = ThreadLocalRandom.current().nextInt(0, neighborSolution.getNumSelectedFeatures() - 1);
            }
            if (neighborSolution.getNumRCLFeatures() == 1) {
                add = 0;
            } else {
                add = ThreadLocalRandom.current().nextInt(0, neighborSolution.getNumRCLFeatures() - 1);
            }
        }

        neighborSolution.replaceFeature(rem, add);
        neighborSolution = grasp.avaliar(neighborSolution);
        return neighborSolution;
    }

    @Override
    public GraspSolution run(GraspSolution reference) throws Exception {
        System.out.println("Running BitFlip:");
        bestLocal = reference.newClone(false); // initialization

        while (--remLSIterations > 0 && --remLSNoImprovements > 0) {
            GraspSolution neighborSolution;
            try {
                neighborSolution = performSingleMoviment(reference);
                if (neighborSolution.isBetterThan(bestLocal,  grasp.criteriaMetric)) {
                    bestLocal = neighborSolution.newClone(false);
                    remLSNoImprovements = 10;
                }
            } catch (IncompleteFeatureSelection ex) {
                System.out.println("Cancelando run: " + ex);
                remLSIterations = 20;
                remLSNoImprovements = 10;
                return bestLocal;
            }

        }
        remLSIterations = 20;
        remLSNoImprovements = 10;
        return bestLocal;
    }

}
