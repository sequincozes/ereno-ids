/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.localsearch;

import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.BitFlip;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSS;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.IWSSr;
import br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructure;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author silvio
 */
public class LocalSearches {

    public static GraspSolution doVND(GraspSolution semente, Grasp grasp) throws Exception {

        ArrayList<NeighborhoodStructure> neighborhoodStructures = new ArrayList<>();
        neighborhoodStructures.add(new IWSSr(grasp));
        System.out.println("Adding IWWSr...");
        neighborhoodStructures.add(new IWSS(grasp));
        System.out.println("Adding IWWS...");
        boolean includedBF = false;
        if (grasp.numBitFLipFeatures > 0 && !includedBF) {
            neighborhoodStructures.add(new BitFlip(grasp));
            System.out.println("Adding BitFlip...");
        }
        GraspSolution bestLocal = semente.newClone(false);
        for (int i = 0; i < neighborhoodStructures.size(); i++) {
            GraspSolution nova = buscaLocal(semente, neighborhoodStructures.get(i), grasp);
            System.out.println("**** Selected bestLocal: " + bestLocal.getFeatureSet() + "(" + bestLocal.getF1Score() + "), RCL:" + bestLocal.getRCLfeatures());
            System.out.println("Running structure: " + i);
            System.out.println("**** Selected nova: " + nova.getFeatureSet() + "(" + nova.getF1Score()+ "), RCL:" + nova.getRCLfeatures());

            if (nova.isBetterThan(bestLocal, grasp.criteriaMetric)) {
                bestLocal = nova.newClone(false);
            }
            grasp.numBitFLipFeatures = bestLocal.getNumSelectedFeatures();
            if (grasp.numBitFLipFeatures > 0 && !includedBF) {
                includedBF = true;
                neighborhoodStructures.add(new BitFlip(grasp));
                System.out.println("Adding BitFlip...");
            }
        }
        return bestLocal;
    }

    public static GraspSolution doRVND(GraspSolution semente, Grasp grasp) throws Exception {
        // Inicializar T
        ArrayList<NeighborhoodStructure> neighborhoodStructures = new ArrayList<>();
        neighborhoodStructures.add(new IWSSr(grasp));
        System.out.println("Adding IWWSr...");
        neighborhoodStructures.add(new IWSS(grasp));
        System.out.println("Adding IWWS...");
        if (grasp.numBitFLipFeatures > 0) {
            neighborhoodStructures.add(new BitFlip(grasp));
            System.out.println("Adding BitFlip...");
        }
        int min = 0;
        int max = neighborhoodStructures.size();

        GraspSolution melhor = semente.newClone(false);

        boolean firstIteration = true;
        while (neighborhoodStructures.size() > 0) {
            int randomNum = 0;
            try {
                randomNum = ThreadLocalRandom.current().nextInt(min, max);
                System.out.println("Running structure: " + randomNum);
                GraspSolution nova = buscaLocal(semente.newClone(false), neighborhoodStructures.get(randomNum), grasp);
                System.out.println("**** Selected melhor: " + melhor.getFeatureSet() + "(" + melhor.getF1Score()+ "), RCL:" + melhor.getRCLfeatures());
                System.out.println("**** Selected nova: " + nova.getFeatureSet() + "(" + nova.getF1Score()+ "), RCL:" + nova.getRCLfeatures());
                if (nova.isBetterThan(melhor, grasp.criteriaMetric)) {
                    if (firstIteration) {
                        firstIteration = false;
                        neighborhoodStructures.remove(randomNum);
                        System.out.println("REMOVE(" + randomNum + ") - T: " + neighborhoodStructures.size());
                        max = neighborhoodStructures.size();
                        grasp.numBitFLipFeatures = nova.getNumSelectedFeatures();

                    } else {
                        System.out.println("RESET");
                        melhor = nova.newClone(false);
                        neighborhoodStructures = new ArrayList<>();
                        neighborhoodStructures.add(new IWSSr(grasp));
                        System.out.println("Adding IWWSr...");
                        neighborhoodStructures.add(new IWSS(grasp));
                        System.out.println("Adding IWWS...");
                        if (grasp.numBitFLipFeatures > 0) {
                            neighborhoodStructures.add(new BitFlip(grasp));
                            System.out.println("Adding BitFlip...");
                        }
                        max = neighborhoodStructures.size();
                    }
                    grasp.numBitFLipFeatures = melhor.getNumSelectedFeatures();
                } else {
                    neighborhoodStructures.remove(randomNum);
                    System.out.println("REMOVE(" + randomNum + ") - T: " + neighborhoodStructures.size());
                    max = neighborhoodStructures.size();
                    grasp.numBitFLipFeatures = melhor.getNumSelectedFeatures();
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Sorteou numero invalido, cancelando a rodada:" + e.getLocalizedMessage());
            }
        }

        return melhor;
    }

    public static GraspSolution buscaLocal(GraspSolution solution, NeighborhoodStructure neigborhood,Grasp grasp) throws Exception {
        GraspSolution bestLocal = neigborhood.run(solution);
        if (bestLocal.isBetterThan(solution, grasp.criteriaMetric)) {
            return bestLocal;
        } else {
            return solution;
        }

    }
}
