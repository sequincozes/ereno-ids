/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspVND;
import br.uff.midiacom.ereno.featureSelection.subsets.WsnFeatures;
import java.util.ArrayList;

/**
 *
 * @author silvio
 */
public class IWSSr implements NeighborhoodStructure {

    GraspSolution bestLocal;
    Grasp grasp;
    int remIterations = 1000000;
    int remNoImprovements = 1000000;
    ArrayList<Integer> fullList;

    public static void main(String[] args) throws Exception {
        Grasp graspVnd = new GraspVND();
        graspVnd.allInstances = Util.loadSingleFile(true);
        IWSSr iwssr = new IWSSr(graspVnd);
        iwssr.fullList = new ArrayList<>();

        int[] WSN_FULL = new int[]{18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int feature : WSN_FULL) {
            iwssr.fullList.add(feature);
        }

        ArrayList<Integer> seedFS = new ArrayList<>();

        seedFS.add(iwssr.fullList.remove(WSN_FULL.length - 1));
        GraspSolution seedSolution = new GraspSolution(seedFS, iwssr.fullList);
        seedSolution = graspVnd.avaliar(seedSolution);

        iwssr.run(seedSolution);

    }

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
        bestLocal = seed.newClone(false); // initialization

        while (bestLocal.getNumSelectedFeatures() > 0) {
            bestLocal.deselectFeature(0);
        }
        fullList = bestLocal.copyRCLFeatures();
        System.out.println("Running IWSSSr:");
        // Initial solution
        for (int rclIndex = bestLocal.getNumRCLFeatures() - 1; rclIndex >= 0; --rclIndex) {
            System.out.println("IWSSr >>> adding " + bestLocal.getRCLfeatures().get(rclIndex) + " > to set " + bestLocal.getFeatureSet() + "(Acc: " + bestLocal.getAccuracy() + ")0 (F1: " + bestLocal.getF1Score() + ") ");
            GraspSolution beforeIncrement = bestLocal.newClone(false);
            for (int solutionIndex = 0; solutionIndex < bestLocal.getNumSelectedFeatures(); solutionIndex++) {
                GraspSolution swap = performSwapMoviment(beforeIncrement.newClone(true), rclIndex, solutionIndex);
                if (swap.isBetterThan(bestLocal, grasp.criteriaMetric)) {
                    bestLocal = swap.newClone(false);
                }
            }

            GraspSolution add = performAddMoviment(beforeIncrement.newClone(true), rclIndex);
            if (add.isBetterThan(bestLocal, grasp.criteriaMetric)) {
                bestLocal = add.newClone(false);
            }
        }
        restoreRCL(bestLocal);

        return bestLocal;
    }

    private void restoreRCL(GraspSolution bestLocal) {
        for (int feature : fullList) {
            if (!bestLocal.getSelectedFeatures().contains(feature) && !bestLocal.getRCLfeatures().contains(feature)) {
                bestLocal.addFeatureRCL(feature);
            }
        }

    }
}

