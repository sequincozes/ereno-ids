/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.abstractclassification.Util;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspVND;
import br.uff.midiacom.ereno.featureSelection.grasp.sbseg2022.SBSeGrasp;
import br.uff.midiacom.ereno.featureSelection.subsets.FeatureSubsets;

import java.util.ArrayList;

/**
 * @author silvio
 */
public class IWSSr implements NeighborhoodStructure {

    GraspSolution bestLocal;
    Grasp grasp;
    SBSeGrasp sbSeGrasp;
    int remIterations = 1000000;
    int remNoImprovements = 1000000;
    public ArrayList<Integer> fullList;

    public static void main(String[] args) throws Exception {
        Grasp graspVnd = new GraspVND();
        GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[Integer.parseInt(args[1])];
        GeneralParameters.DATASET = args[0];
//        GeneralParameters.SINGLE_CLASSIFIER_MODE = GeneralParameters.CLASSIFIERS_FOREACH[1];
//        GeneralParameters.DATASET ="C:\\datasets\\uc01\\train.csv";// GeneralParameters.SWAT30pct;
        graspVnd.allInstances = Util.loadSingleFile(true);
        IWSSr iwssr = new IWSSr(graspVnd);
        iwssr.fullList = new ArrayList<>();

        int[] rcl = new int[graspVnd.allInstances.get(0).numAttributes() - 1]; //@TODO quando usar isso, atribuir uma RCL
        for (int feature = 0; feature < graspVnd.allInstances.get(0).numAttributes() - 1; feature++) {
            iwssr.fullList.add(feature + 1);
            rcl[feature] = feature + 1;
        }

        ArrayList<Integer> seedFS = new ArrayList<>();

        seedFS.add(iwssr.fullList.remove(rcl.length - 1));
        GraspSolution seedSolution = new GraspSolution(seedFS, iwssr.fullList);
        seedSolution = graspVnd.avaliar(seedSolution);

        iwssr.run(seedSolution);

    }

    public IWSSr(Grasp grasp) {
        this.grasp = grasp;
    }

    public IWSSr(SBSeGrasp grasp) {
        this.sbSeGrasp = grasp;
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

    public GraspSolution performSwapMovimentSBSeg(GraspSolution reference, int rclFeatureIndex, int solutionFeatureIndex, boolean sbseg2022) throws Exception {
        reference.deselectFeature(solutionFeatureIndex);
        reference.selectFeature(rclFeatureIndex);
        remIterations = remIterations - 1;
        reference = sbSeGrasp.externalEvaluation(reference);
        return reference;
    }

    public GraspSolution performAddMovimentSBSeg(GraspSolution reference, int rclFeatureIndex, boolean sbseg2022) throws Exception {
        reference.selectFeature(rclFeatureIndex);
        remIterations = remIterations - 1;
        reference = sbSeGrasp.externalEvaluation(reference);
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
                    System.out.println("!# Novel Best Local: " + swap.getFeatureSet() + " reached " + swap.getF1Score() + " and passed " + bestLocal.getFeatureSet() + " with " + bestLocal.getF1Score());
                    bestLocal = swap.newClone(false);
                    grasp.setBestGlobalSolution(bestLocal.newClone(false));
                }
            }

            GraspSolution add = performAddMoviment(beforeIncrement.newClone(true), rclIndex);
            if (add.isBetterThan(bestLocal, grasp.criteriaMetric)) {
                System.out.println("!# Novel Best Local: " + add.getFeatureSet() + " reached " + add.getF1Score() + " and passed " + bestLocal.getFeatureSet() + " with " + bestLocal.getF1Score());
                bestLocal = add.newClone(false);
                grasp.setBestGlobalSolution(bestLocal.newClone(false));
            }
        }
        restoreRCL(bestLocal);

        return bestLocal;
    }

    @Override
    public GraspSolution runSBSeGrasp(GraspSolution seed) throws Exception {
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
                GraspSolution swap = performSwapMovimentSBSeg(beforeIncrement.newClone(true), rclIndex, solutionIndex, true);
                if (swap.isBetterThan(bestLocal, grasp.criteriaMetric)) {
                    System.out.println("!# Novel Best Local: " + swap.getFeatureSet() + " reached " + swap.getF1Score() + " and passed " + bestLocal.getFeatureSet() + " with " + bestLocal.getF1Score());
                    bestLocal = swap.newClone(false);
                    grasp.setBestGlobalSolution(bestLocal.newClone(false));
                }
            }

            GraspSolution add = performAddMovimentSBSeg(beforeIncrement.newClone(true), rclIndex, true);
            if (add.isBetterThan(bestLocal, grasp.criteriaMetric)) {
                System.out.println("!# Novel Best Local: " + add.getFeatureSet() + " reached " + add.getF1Score() + " and passed " + bestLocal.getFeatureSet() + " with " + bestLocal.getF1Score());
                bestLocal = add.newClone(false);
                sbSeGrasp.setBestGlobalSolution(bestLocal.newClone(false));
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

