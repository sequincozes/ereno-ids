/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.featureSelection.grasp;

import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sequi
 */
public class GraspSolution {

    private final ArrayList<Integer> solutionFeatures;
    private final ArrayList<Integer> RCLfeatures;
    private GenericResultado evaluation;
    private boolean VERBOSE = false;

    public void example(String[] args) {
        ArrayList<Integer> featuresSelecionadas = new ArrayList<>();
        featuresSelecionadas.add(4);
        featuresSelecionadas.add(5);
        featuresSelecionadas.add(6);
        featuresSelecionadas.add(7);
        featuresSelecionadas.add(8);

        ArrayList<Integer> featuresRCL = new ArrayList<>();
        featuresRCL.add(1);
        featuresRCL.add(2);
        featuresRCL.add(3);
        featuresRCL.add(4);
        featuresRCL.add(5);
        featuresRCL.add(6);
        featuresRCL.add(7);
        featuresRCL.add(8);
        featuresRCL.add(9);
        featuresRCL.add(10);
        featuresRCL.add(11);
        featuresRCL.add(12);
        featuresRCL.add(13);
        featuresRCL.add(14);
        featuresRCL.add(15);
        featuresRCL.add(16);
        featuresRCL.add(17);
        featuresRCL.add(18);

        System.out.print("-- Semente: ");
        GraspSolution s = new GraspSolution(featuresSelecionadas, featuresRCL);
        GraspSolution s1 = new GraspSolution(featuresSelecionadas, featuresRCL);
        System.out.println("Acurácia: " + s.getEvaluation().getAcuracia());

        //long tempoi = System.currentTimeMillis();
        //s = s.IWSSNewSolution();
        //long tempo1 = System.currentTimeMillis();
        //s1 = s1.IWSSrNewSolution();
        //System.out.println("Acurácia: " + s.getAcuracia() + " | tempo: " + (tempo1 - tempoi));
        //System.out.println("Acurácia: " + s1.getAcuracia() + " | tempo: " + (System.currentTimeMillis() - tempo1));
    }

    public GraspSolution(ArrayList<Integer> featuresSelecionadas, ArrayList<Integer> featuresRCL) {
        this.solutionFeatures = featuresSelecionadas;
        this.RCLfeatures = featuresRCL;
        //@TODO: Criar método de avaliação
/*        try {
            Resultado res = ValidacaoWSN.executar(getArrayFeaturesSelecionadas());
            this.acuracia = res.getAcuracia();
            this.taxa_detecao = res.getTaxaDeteccao();
            this.taxa_falsos_positivos = res.getTaxaAlarmeFalsos();

        } catch (Exception ex) {
            Logger.getLogger(GraspSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
    }

    public GraspSolution() {
        this.solutionFeatures = new ArrayList<>();
        this.RCLfeatures = new ArrayList<>();
    }

    private GraspSolution(ArrayList<Integer> featuresSelecionadasAux, ArrayList<Integer> featuresRCL, GenericResultado evaluation) {
        this.solutionFeatures = featuresSelecionadasAux;
        this.RCLfeatures = featuresRCL;
        this.evaluation = new GenericResultado(evaluation);
    }

    public ArrayList<Integer> copySolutionFeatures() {
        return new ArrayList(solutionFeatures);
    }

    public int[] getArrayFeaturesSelecionadas() {
        int[] arrayFS = new int[solutionFeatures.size()];
        for (int i = 0; i < arrayFS.length; i++) {
            arrayFS[i] = solutionFeatures.get(i);
        }
        return arrayFS;
    }

    public int[] getArrayrcl() {
        int[] arrayFS = new int[RCLfeatures.size()];
        for (int i = 0; i < arrayFS.length; i++) {
            arrayFS[i] = RCLfeatures.get(i);
        }
        return arrayFS;
    }

    public boolean isBetterThan(GraspSolution solucao) {
        return this.evaluation.getAcuracia() > solucao.getEvaluation().getAcuracia();
    }

    //@TODO: Add support to multiple metrics
    public boolean isEqualOrBetterThan(GraspSolution solucao) {
        return this.evaluation.getAcuracia() >= solucao.getEvaluation().getAcuracia();
    }

    public void printSelection(String selecaoName) {
        //System.out.print("Seleção [" + selecaoName + "]: {");
        boolean printComma = false;
        for (int feature : solutionFeatures) {
            if (!printComma) {
                printComma = true;
                System.out.print(feature);
            } else {
                System.out.print(",");
                System.out.print(feature);
            }
        }
        System.out.print("} - ");
    }

    public GraspSolution reconstruirNewSolucao(int num_features) {
        ArrayList<Integer> featuresSelecionadasAux = new ArrayList<>(solutionFeatures);
        ArrayList<Integer> featuresRCLAux = new ArrayList<>(RCLfeatures);;

        Random r = new Random();
        while (featuresSelecionadasAux.size() > 0) {
            int toRemove = featuresSelecionadasAux.remove(0);
            featuresRCLAux.add(toRemove); //devolve a RCL
        }

        while (featuresSelecionadasAux.size() < num_features) {
            int sorteio = r.nextInt(featuresRCLAux.size());
            int featureSorteada = featuresRCLAux.remove(sorteio);
//            System.out.println("Sorteio[" + sorteio + "]: " + featureSorteada);
            if (!featuresSelecionadasAux.contains(featureSorteada)) {
//                System.out.println("A feature " + featureSorteada + " não está em " + s.featuresSelecionadas);
                featuresSelecionadasAux.add(featureSorteada);
            } else {
//                System.out.println("A feature " + featureSorteada + " JÁ está em " + s.featuresSelecionadas);
            }
        }
        GraspSolution newer = new GraspSolution(featuresSelecionadasAux, featuresRCLAux);
        return newer;
    }

    public GraspSolution newClone(boolean resetMetrics) {
        ArrayList<Integer> featuresSelecionadasAux = new ArrayList<>(solutionFeatures);
        ArrayList<Integer> getBitFlipSolution = new ArrayList<>(RCLfeatures);
        if (resetMetrics) {
            return new GraspSolution(featuresSelecionadasAux, getBitFlipSolution);
        }
        return new GraspSolution(featuresSelecionadasAux, RCLfeatures, evaluation);
    }

    public void addFeature(Integer fature) {
        solutionFeatures.add(fature);
    }

    public void addFeatureFlip(Integer fature) {
        RCLfeatures.add(fature);
    }

    public void printRCL() {
        System.out.print("RCL: {");
        boolean printComma = false;
        for (int feature : RCLfeatures) {
            if (!printComma) {
                printComma = true;
                System.out.print(feature);
            } else {
                System.out.print(",");
                System.out.print(feature);
            }
        }
        System.out.print("} - ");
    }

    public ArrayList<Integer> copyRCLFeatures() {
        return new ArrayList(RCLfeatures);
    }

    public int getNumSelectedFeatures() {
        return this.solutionFeatures.size();
    }

    public void deselectFeature(int removeIndex) {
        RCLfeatures.add(solutionFeatures.remove(removeIndex));
    }

    public int getNumRCLFeatures() {
        return this.RCLfeatures.size();
    }

    public void selectFeature(int selectIndex) {
        solutionFeatures.add(RCLfeatures.remove(selectIndex));
    }

    public void replaceFeature(int solIndex, int rclFeature) {
        int deselected = solutionFeatures.remove(solIndex);
        int selected = RCLfeatures.remove(rclFeature);

        solutionFeatures.add(selected);
        RCLfeatures.add(deselected);

    }

    public GenericResultado getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(GenericResultado evaluation) {
        this.evaluation = evaluation;
    }

    public boolean isVERBOSE() {
        return VERBOSE;
    }

    public void setVERBOSE(boolean VERBOSE) {
        this.VERBOSE = VERBOSE;
    }

    public ArrayList<Integer> getRCLfeatures() {
        return RCLfeatures;
    }

    public ArrayList<Integer> getSelectedFeatures() {
        return solutionFeatures;
    }

    public void addFeatureRCL(int feature) {
        RCLfeatures.add(feature);
    }

    public String getFeaturesAndPerformance() {
        return Arrays.toString(getArrayFeaturesSelecionadas()) + " - Acc: " + getEvaluation().getAcuracia();
    }
    public String getFeatureSet() {
        return Arrays.toString(getArrayFeaturesSelecionadas());
    } public String getAccuracy() {
        return String.valueOf(getEvaluation().getAcuracia());
    }
}
