/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.grasp.neighborhoodStructures;

import br.uff.midiacom.ereno.featureSelection.Parameters;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.featureSelection.grasp.GraspSolution;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author silvio Incremental Wrapper-Based Subset Selection
 */
public class IWSS implements NeighborhoodStructure {

    Grasp grasp;
    GraspSolution bestLocal;
    ArrayList<Integer> fullList;
    boolean completionMode = false;
    int lenght;
    int remIterations = 20;
    int remNoImprovements = 10;

    public IWSS(Grasp grasp) {
        this.grasp = grasp;
    }

    /* Build solution sequentially 
        - from empty set, feature by feature 
        - up to end RCL or features lenght achieved
     */
    @Override
    public GraspSolution run(GraspSolution seed) throws Exception {
//        System.out.println("----- ENTROU NO IWSS");
        lenght = seed.getNumSelectedFeatures();
        bestLocal = seed.newClone(false); // initialization

        // Starts with a empty set
        while (bestLocal.getNumSelectedFeatures() > 0) {
            bestLocal.deselectFeature(0); //remove first, send to end of RCL
        }

        fullList = bestLocal.copyRCLFeatures();

        try {
            while (bestLocal.getNumSelectedFeatures() < lenght) {
                if (completionMode) {
                    return bestLocal;
                } else {
                    if (remIterations <= 0 || remNoImprovements <= 0) {
                        return bestLocal;
                    }
                    GraspSolution neighborSolution = performSingleMoviment(bestLocal);
                    if (neighborSolution.isBetterThan(bestLocal)) {
                        bestLocal = neighborSolution.newClone(false);
                        remNoImprovements = 10;
//                        System.out.println("IWSS - LOCAL IMPROVEMENT: " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()) + " = " + bestLocal.getEvaluation().getAcuracia());
                    }
                }
            }
        } catch (IncompleteFeatureSelection e) {
            System.err.println("Warning! " + e.getLocalizedMessage() + "- best is " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()) + " Accuracy: " + bestLocal.getEvaluation().getAcuracia());
            restoreRCL(bestLocal);
            return bestLocal;
        }
        restoreRCL(bestLocal);
        return bestLocal;
    }

    public GraspSolution performSingleMoviment(GraspSolution reference) throws Exception, IncompleteFeatureSelection {

        GraspSolution neighborSolution = reference.newClone(false);
        if (neighborSolution.getRCLfeatures().size() > 0) {
            neighborSolution.selectFeature(neighborSolution.getRCLfeatures().size() - 1);//Seed selected first, then RCL
            neighborSolution = grasp.avaliar(neighborSolution);
            remIterations = remIterations - 1;
        } else {
            System.out.println("Completou");
            if (completionMode == false) {
                completionMode = true;
                restoreRCL(bestLocal);
            } else {
                throw new IncompleteFeatureSelection("All RCL features provides worst solution.");
            }
        }
        return neighborSolution;
    }

    private void restoreRCL(GraspSolution bestLocal) {
        for (int feature : fullList) {
            if (!bestLocal.getSelectedFeatures().contains(feature)) {
                bestLocal.addFeatureRCL(feature);
            }
        }
    }

}
