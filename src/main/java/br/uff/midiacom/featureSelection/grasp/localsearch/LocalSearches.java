/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.featureSelection.grasp.localsearch;

import br.uff.midiacom.featureSelection.grasp.Grasp;
import br.uff.midiacom.featureSelection.grasp.GraspRVND;
import br.uff.midiacom.featureSelection.grasp.GraspSimple;
import br.uff.midiacom.featureSelection.grasp.GraspSolution;
import br.uff.midiacom.featureSelection.grasp.GraspVND;
import br.uff.midiacom.featureSelection.grasp.neighborhoodStructures.BitFlip;
import br.uff.midiacom.featureSelection.grasp.neighborhoodStructures.IWSS;
import br.uff.midiacom.featureSelection.grasp.neighborhoodStructures.IWSSr;
import br.uff.midiacom.featureSelection.grasp.neighborhoodStructures.NeighborhoodStructure;
import static java.lang.Integer.max;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author silvio
 */
public class LocalSearches {

    public static GraspSolution doVND(GraspSolution semente, Grasp grasp) throws Exception {
        ArrayList<NeighborhoodStructure> neighborhoodStructures = new ArrayList<>();
        neighborhoodStructures.add(new BitFlip(grasp));
        neighborhoodStructures.add(new IWSS(grasp));
        neighborhoodStructures.add(new IWSSr(grasp));
        GraspSolution bestLocal = semente.newClone(false);
        for (int i = 0; i < 3; i++) {
            System.out.println("-> BEGIN| Estrutura de Vizinhança: " + i);
            long tempoInicial = System.currentTimeMillis();
            GraspSolution nova = buscaLocal(semente, neighborhoodStructures.get(i));
            long tempoFinal = System.currentTimeMillis() - tempoInicial;
            System.out.println("-> END| Estrutura de Vizinhança: " + i + "[" + tempoFinal + "]");
            if (nova.isEqualOrBetterThan(bestLocal)) {
                bestLocal = nova.newClone(false);
                System.out.println("LOCAL IMPROVEMENT: " + Arrays.toString(bestLocal.getArrayFeaturesSelecionadas()) + " = " + bestLocal.getEvaluation().getAcuracia());
            }
        }
        return bestLocal;
    }

    public static GraspSolution doRVND(GraspSolution semente, Grasp grasp) throws Exception {
        // Inicializar T
        ArrayList<NeighborhoodStructure> neighborhoodStructures = new ArrayList<>();
        neighborhoodStructures.add(new BitFlip(grasp));
        neighborhoodStructures.add(new IWSS(grasp));
        neighborhoodStructures.add(new IWSSr(grasp));
        int min = 0;
        int max = neighborhoodStructures.size();

        GraspSolution melhor = semente.newClone(false);

        while (neighborhoodStructures.size() > 0) {
            int randomNum = 0;
            try {
                randomNum = ThreadLocalRandom.current().nextInt(min, max);

                GraspSolution nova = buscaLocal(semente, neighborhoodStructures.get(randomNum));
                System.out.println("Melhor/Semente:" + Arrays.toString(melhor.getArrayFeaturesSelecionadas()) + "/" + melhor.getEvaluation().getAcuracia());
                System.out.println("Nova:" + Arrays.toString(nova.getArrayFeaturesSelecionadas()) + "/" + nova.getEvaluation().getAcuracia());
                if (nova.isBetterThan(melhor)) {
                    melhor = nova.newClone(false);
                    neighborhoodStructures = new ArrayList<>();
                    neighborhoodStructures.add(new BitFlip(grasp));
                    neighborhoodStructures.add(new IWSS(grasp));
                    neighborhoodStructures.add(new IWSSr(grasp));
                } else {
                    neighborhoodStructures.remove(randomNum);
                    System.out.println("REMOVE(" + randomNum + ") - T: " + neighborhoodStructures.size());
                    max = neighborhoodStructures.size();
                }
            } catch (Exception e) {
                System.out.println("Sorteou numero invalido, cancelando a rodada.");
            }
        }

        return melhor;
    }

    public static GraspSolution buscaLocal(GraspSolution solution, NeighborhoodStructure neigborhood) throws Exception {
        GraspSolution bestLocal = neigborhood.run(solution);
        if (bestLocal.isBetterThan(solution)) {
            return bestLocal;
        } else {
            return solution;
        }

    }
}
