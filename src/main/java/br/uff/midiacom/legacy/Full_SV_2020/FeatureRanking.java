/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.legacy.Full_SV_2020;

import br.uff.midiacom.legacy.substation.FeatureAvaliada;
import static br.uff.midiacom.legacy.substation.Parameters.NORMALIZE;
import static br.uff.midiacom.legacy.substation.Util.readDataFile;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.OneRAttributeEval;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author sequi
 */
public class FeatureRanking {

static boolean CSV = true;
    private static Instances normalizar(Instances instances) throws Exception {
        Normalize filter = new Normalize();
        filter.setInputFormat(instances);
        instances = Filter.useFilter(instances, filter);
        return instances;
    }

    public static enum METODO {
        GR, IG, Relief, OneR;
    };

    public static void main(String[] args) throws Exception {
//        avaliarESelecionar(14, METODO.OneR, false); 

    Parameters.ALL_IN_ONE_FILE  = "/home/silvio/datasets/wsn-ds/blackhole/train_5.csv";


        avaliarESelecionar(18, METODO.IG, false); 
        avaliarESelecionar(18, METODO.GR, false); 

    }

    public static FeatureAvaliada[] avaliarESelecionar(int featuresSelecionar, METODO metodo, boolean debug) throws Exception {
        System.out.println("Método: " + metodo);
        Instances instances = new Instances(readDataFile(Parameters.ALL_IN_ONE_FILE));
        instances.setClassIndex(instances.numAttributes() - 1);
        if (NORMALIZE) {
            instances = normalizar(instances);
        }
        FeatureAvaliada[] allFeatures = new FeatureAvaliada[instances.numAttributes()];
        switch (metodo) {
            case IG:
                System.out.println("IG:");
                for (int i = 0; i < instances.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularaIG(instances, i), i + 1);
                    if(CSV){
                        System.out.println("IG;" + allFeatures[i].indiceFeature+";"+instances.attribute(i).name()+";" + allFeatures[i].valorFeature);
                    } else {
                        System.out.println("IG: [" + allFeatures[i].indiceFeature+"] Ganho: " + allFeatures[i].valorFeature + " ("+instances.attribute(i).name()+ ")");                        
                    }
                }
                break;
            case Relief:
                System.out.println("Relief:");
                for (int i = 0; i < instances.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularReliefF(instances, i), i + 1);
                }
                break;
            case GR:
                System.out.println("GR:");
                for (int i = 0; i < instances.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularGainRatioAttributeEval(instances, i), i + 1);
                    if(CSV){
                        System.out.println("GR;" + allFeatures[i].indiceFeature+";"+instances.attribute(i).name()+";" + allFeatures[i].valorFeature);
                    } else {
                        System.out.println("GR: [" + allFeatures[i].indiceFeature+"] Ganho: " + allFeatures[i].valorFeature + " ("+instances.attribute(i).name()+ ")");                        
                    }

                }
                break;
            case OneR:
                System.out.println("OneR:");
                for (int i = 0; i < instances.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularOneRAttributeEval(instances, i), i + 1);
                }
                break;
            default:
                System.out.println("Método incorreto.");
        }

        Util.quickSort(allFeatures, 0, allFeatures.length - 1);
        FeatureAvaliada[] filter = new FeatureAvaliada[featuresSelecionar];
        int i = 0;

//        for (FeatureAvaliada feature : allFeatures) {
//            System.out.println(feature.getIndiceFeature() + "-" + feature.getValorFeature());
//        }
        for (int j = allFeatures.length; j > allFeatures.length - featuresSelecionar; j--) {
//            System.out.println(featuresSelecionar + "[" + i + "]" + "/[" + (j - 1) + "]" + allFeatures.length);
            filter[i++] = allFeatures[j - 1];
        }

        if (debug) {
            for (FeatureAvaliada filter1 : filter) {
                System.out.println(filter1.getIndiceFeature() + "-" + filter1.getValorFeature());
            }
        } else {
            System.out.print("{");
            for (FeatureAvaliada filter1 : filter) {
                System.out.print(filter1.getIndiceFeature() + ", ");
            }
            System.out.println("}");
        }

        return filter;
    }

    public static double calcularaIG(Instances instances, int featureIndice) throws Exception {
        InfoGainAttributeEval ase = new InfoGainAttributeEval();
        ase.buildEvaluator(instances);
        return ase.evaluateAttribute(featureIndice);
    }

    public static double calcularOneRAttributeEval(Instances instances, int featureIndice) throws Exception {
        OneRAttributeEval ase = new OneRAttributeEval();
        ase.buildEvaluator(instances);
        return ase.evaluateAttribute(featureIndice);
    }

    public static double calcularReliefF(Instances instances, int featureIndice) throws Exception {
        System.out.println("Chegou aq? 1");
        ReliefFAttributeEval ase = new ReliefFAttributeEval();
        System.out.println("Chegou aq? 2");
        instances.setClassIndex(instances.numAttributes() - 1);
        System.out.println("Chegou aq? 3");
        ase.buildEvaluator(instances);
        System.out.println("Chegou aq? 4");
        return ase.evaluateAttribute(featureIndice);
    }

    public static double calcularGainRatioAttributeEval(Instances instances, int featureIndice) throws Exception {
        GainRatioAttributeEval ase = new GainRatioAttributeEval();
        instances.setClassIndex(instances.numAttributes() - 1);
        ase.buildEvaluator(instances);
        return ase.evaluateAttribute(featureIndice);
    }

}
