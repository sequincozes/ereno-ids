/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.PreGrasp;

/**
 *
 * @author silvio Incremental Wrapper-Based Subset Selection
 */
public class PreGraspIWSSr implements NeighborhoodStructure {

    GraspSolution bestLocal;
    PreGrasp grasp;
    int remIterations = 20000;
    int remNoImprovements = 10000;

    public PreGraspIWSSr(PreGrasp grasp) {
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
        bestLocal = seed.newClone(false); // initialization
        int lenght = seed.getNumSelectedFeatures();
        while (bestLocal.getNumSelectedFeatures() > 0) {
            bestLocal.deselectFeature(0);
        }

        // Initial solution
        //System.out.println("A");
        // bestLocal.selectFeature(bestLocal.getNumRCLFeatures()-1);
        //bestLocal = grasp.avaliar(bestLocal);
        for (int rclIndex = bestLocal.getNumRCLFeatures() - 1; rclIndex > 0; --rclIndex) {
            System.out.println(">>> " + bestLocal.getRCLfeatures().get(rclIndex) + " > "+bestLocal.getFeatureSet());
            GraspSolution beforeIncrement = bestLocal.newClone(false);
            for (int solutionIndex = 0; solutionIndex < bestLocal.getNumSelectedFeatures(); solutionIndex++) {
                //  System.out.println("swap(" + bestLocal.getRCLfeatures().get(rclIndex) + "," + bestLocal.getArrayFeaturesSelecionadas()[solutionIndex] + ")");
                GraspSolution swap = performSwapMoviment(beforeIncrement.newClone(true), rclIndex, solutionIndex);
                if (swap.isBetterThan(bestLocal,  Grasp.criteriaMetric)) {
                    bestLocal = swap.newClone(false);
                    //System.out.println("Melhorou com swap: " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()));
                }
            }

            //System.out.println("add(" + bestLocal.getRCLfeatures().get(rclIndex) + ")");
            GraspSolution add = performAddMoviment(beforeIncrement.newClone(true), rclIndex);
            if (add.isBetterThan(bestLocal,  Grasp.criteriaMetric)) {
                //System.out.println("Melhorou com add: " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()));
                bestLocal = add.newClone(false);
            }
        }
        return bestLocal;
    }

    @Override
    public GraspSolution runSBSeGrasp(GraspSolution seed) throws Exception {
       System.err.println("PreGraspIWSSr não é esperado com SBSeg2022.");
        System.exit(0);
        return null;
    }


}
