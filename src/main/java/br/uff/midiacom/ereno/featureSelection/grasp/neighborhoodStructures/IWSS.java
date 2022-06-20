/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.externalClassifier.SBSeGrasp;

import java.util.ArrayList;

/**
 *
 * @author silvio Incremental Wrapper-Based Subset Selection
 */
public class IWSS implements NeighborhoodStructure {

    Grasp grasp;
    SBSeGrasp sbSeGrasp;
    GraspSolution bestLocal;
    ArrayList<Integer> fullList;

    public IWSS(Grasp grasp) {
        this.grasp = grasp;
    }
    public IWSS(SBSeGrasp sbSeGrasp) {
        this.sbSeGrasp = sbSeGrasp;
    }

    /* Build solution sequentially 
        - from empty set, feature by feature 
        - up to end RCL or features lenght achieved
     */
    @Override
    public GraspSolution run(GraspSolution seed) throws Exception {
        System.out.println("Running IWSS:");
        bestLocal = seed.newClone(false); // initialization

        // Starts with a empty set
        while (bestLocal.getNumSelectedFeatures() > 0) {
            bestLocal.deselectFeature(0); //remove first, send to end of RCL
        }

        fullList = bestLocal.copyRCLFeatures();

        for (int rclIndex = bestLocal.getNumRCLFeatures() - 1; rclIndex >= 0; --rclIndex) {
            System.out.println("IWSS >>> adding " + bestLocal.getRCLfeatures().get(rclIndex) + " > to set " + bestLocal.getFeatureSet() + "(Acc: " + bestLocal.getAccuracy()+ "), (F1: " + bestLocal.getF1Score() + ")");

            if (grasp.currentTime >= grasp.maxTime) {
                return bestLocal;
            }
            GraspSolution beforeIncrement = bestLocal.newClone(false);
            GraspSolution add = performAddMoviment(beforeIncrement.newClone(true), rclIndex);
            if (add.isBetterThan(bestLocal,  grasp.criteriaMetric)) {
                bestLocal = add.newClone(false);
            }
        }

        restoreRCL(bestLocal);
        return bestLocal;
    }

    @Override
    public GraspSolution runSBSeGrasp(GraspSolution seed) throws Exception {
        System.out.println("Running IWSS:");
        bestLocal = seed.newClone(false); // initialization

        // Starts with a empty set
        while (bestLocal.getNumSelectedFeatures() > 0) {
            bestLocal.deselectFeature(0); //remove first, send to end of RCL
        }

        fullList = bestLocal.copyRCLFeatures();

        for (int rclIndex = bestLocal.getNumRCLFeatures() - 1; rclIndex >= 0; --rclIndex) {
            System.out.println("IWSS >>> adding " + bestLocal.getRCLfeatures().get(rclIndex) + " > to set " + bestLocal.getFeatureSet() + "(Acc: " + bestLocal.getAccuracy()+ "), (F1: " + bestLocal.getF1Score() + ")");

            if (sbSeGrasp.currentTime >= sbSeGrasp.maxTime) {
                return bestLocal;
            }
            GraspSolution beforeIncrement = bestLocal.newClone(false);
            GraspSolution add = performAddMovimentSBSeGrasp(beforeIncrement.newClone(true), rclIndex);
            if (add.isBetterThan(bestLocal,  sbSeGrasp.criteriaMetric)) {
                bestLocal = add.newClone(false);
            }
        }

        restoreRCL(bestLocal);
        return bestLocal;
    }
    public GraspSolution performAddMoviment(GraspSolution reference, int rclFeatureIndex) throws Exception {
        reference.selectFeature(rclFeatureIndex);
        reference = grasp.avaliar(reference);
        return reference;
    }
    public GraspSolution performAddMovimentSBSeGrasp(GraspSolution reference, int rclFeatureIndex) throws Exception {
        reference.selectFeature(rclFeatureIndex);
        reference = sbSeGrasp.externalEvaluation(reference);
        return reference;
    }

    private void restoreRCL(GraspSolution bestLocal) {
        for (int feature : fullList) {
            if (!bestLocal.getSelectedFeatures().contains(feature) && !bestLocal.getRCLfeatures().contains(feature)) {
                bestLocal.addFeatureRCL(feature);
            }
        }
    }

}
