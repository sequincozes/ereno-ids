/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.featureSelection.grasp.Grasp;
import br.uff.midiacom.featureSelection.grasp.GraspSolution;
import java.util.Arrays;

/**
 *
 * @author silvio
 */
public class IWSSr implements NeighborhoodStructure {

    GraspSolution bestLocal;
    Grasp grasp;
    int remIterations = 20;
    int remNoImprovements = 10;

    public IWSSr(Grasp grasp) {
        this.grasp = grasp;
    }

    public GraspSolution performSwapMoviment(GraspSolution reference, int rclFeatureIndex, int solutionFeatureIndex) throws Exception {
        reference.deselectFeature(solutionFeatureIndex);
        reference.selectFeature(rclFeatureIndex);
        remIterations = remIterations - 1;
        reference = grasp.avaliar(reference);
        return reference;
    }

    public GraspSolution performAddMoviment(GraspSolution reference, int rclFeatureIndex) throws Exception {
        reference.selectFeature(rclFeatureIndex);
        remIterations = remIterations - 1;
        reference = grasp.avaliar(reference);
        return reference;
    }

    @Override
    public GraspSolution run(GraspSolution seed) throws Exception {
        bestLocal = seed.newClone(true); // initialization
        int lenght = seed.getNumSelectedFeatures();
        System.out.println("Seed:" + Arrays.toString(seed.getArrayFeaturesSelecionadas()));
        while (bestLocal.getNumSelectedFeatures() > 0) {
            bestLocal.deselectFeature(0);
        }

        // Initial solution
        bestLocal.selectFeature(0);
        bestLocal = grasp.avaliar(bestLocal);

        for (int rclIndex = 0; rclIndex < bestLocal.getNumRCLFeatures(); rclIndex++) {
            GraspSolution beforeIncrement = bestLocal.newClone(false);
            for (int solutionIndex = 0; solutionIndex < bestLocal.getNumSelectedFeatures(); solutionIndex++) {
                // inverte e avalia
                if (remIterations == 0 || remNoImprovements == 0) {
                    return bestLocal;
                }
                GraspSolution swap = performSwapMoviment(beforeIncrement.newClone(true), rclIndex, solutionIndex);
                if (swap.isBetterThan(bestLocal)) {
                    bestLocal = swap.newClone(false);
                    remNoImprovements = 10;
                    System.out.println("Melhorou com swap: " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()));
                }
            }
            if (bestLocal.getNumSelectedFeatures() < lenght) {
                if (remIterations == 0 || remNoImprovements == 0) {
                    return bestLocal;
                }
                GraspSolution add = performAddMoviment(beforeIncrement.newClone(true), rclIndex);
                if (add.isEqualOrBetterThan(bestLocal)) {
                    System.out.println("Melhorou com add: " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()));
                    System.out.println("Melhorou com add");
                    bestLocal = add.newClone(false);
                }
            } else {
                return bestLocal;
            }

        }
        return bestLocal;
    }

}
