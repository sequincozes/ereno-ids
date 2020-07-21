/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.legacy.sv2020_consistency;

import br.uff.midiacom.legacy.substation.FeatureAvaliada;
import java.util.Random;
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
//        System.out.println("UC01");
//        Parameters.BINARTY_ATTACK_CLASS = "uc01_fullgoose";
//        avaliarESelecionar(34, METODO.GR, false); 
//        
//        System.out.println("UC02");
//        Parameters.BINARTY_ATTACK_CLASS = "uc02_fullgoose";
//        avaliarESelecionar(34, METODO.GR, false); 
//        
//        System.out.println("UC03");
//        Parameters.BINARTY_ATTACK_CLASS = "uc03_fullgoose";
//        avaliarESelecionar(34, METODO.GR, false); 
//        
//        System.out.println("UC04");
//        Parameters.BINARTY_ATTACK_CLASS = "uc04_fullgoose";
//        avaliarESelecionar(34, METODO.GR, false); 
//        
//        System.out.println("UC05");
//        Parameters.BINARTY_ATTACK_CLASS = "uc05_fullgoose";
//        avaliarESelecionar(34, METODO.GR, false); 

        avaliarESelecionar(42, METODO.GR, false, 1000);

    }

    public static FeatureAvaliada[] avaliarESelecionar(int featuresSelecionar, METODO metodo, boolean debug) throws Exception {
        System.out.println("Método: " + metodo);
        Instances instances = new Instances(readDataFile(Parameters.NORMAL_FILE));
        instances.addAll(new Instances(readDataFile(Parameters.getAttackFile())));
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
                    if (CSV) {
                        System.out.println("IG;" + allFeatures[i].indiceFeature + ";" + instances.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                    } else {
                        System.out.println("IG: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + instances.attribute(i).name() + ")");
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
                    if (CSV) {
                        System.out.println("GR;" + allFeatures[i].indiceFeature + ";" + instances.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                    } else {
                        System.out.println("GR: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + instances.attribute(i).name() + ")");
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

    public static FeatureAvaliada[] avaliarESelecionar(int featuresSelecionar, METODO metodo, boolean debug, int numInstances) throws Exception {
        System.out.println("Método: " + metodo);
        Instances normal = new Instances(readDataFile(Parameters.NORMAL_FILE));
        Instances attack = new Instances(readDataFile(Parameters.getAttackFile()));

        Instances reduced = new Instances(normal, 0, 1);
        reduced.remove(0);

        for (int i = 0; i < (numInstances / 2); i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(attack.size() - 1);
            reduced.add(normal.get(randomNumber));
        }

        for (int i = 0; i < (numInstances / 2); i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(attack.size() - 1);
            reduced.add(attack.get(randomNumber));
        }

        reduced.setClassIndex(reduced.numAttributes() - 1);
        if (NORMALIZE) {
            reduced = normalizar(reduced);
        }
        FeatureAvaliada[] allFeatures = new FeatureAvaliada[reduced.numAttributes()];
        switch (metodo) {
            case IG:
                System.out.println("IG:");
                for (int i = 0; i < reduced.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularaIG(reduced, i), i + 1);
                    if (CSV) {
                        System.out.println("IG;" + allFeatures[i].indiceFeature + ";" + reduced.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                    } else {
                        System.out.println("IG: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + reduced.attribute(i).name() + ")");
                    }
                }
                break;
            case Relief:
                System.out.println("Relief:");
                for (int i = 0; i < reduced.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularReliefF(reduced, i), i + 1);
                }
                break;
            case GR:
                System.out.println("GR:");
                for (int i = 0; i < reduced.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularGainRatioAttributeEval(reduced, i), i + 1);
                    if (CSV) {
                        System.out.println("GR;" + allFeatures[i].indiceFeature + ";" + reduced.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                    } else {
                        System.out.println("GR: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + reduced.attribute(i).name() + ")");
                    }

                }
                break;
            case OneR:
                System.out.println("OneR:");
                for (int i = 0; i < reduced.numAttributes(); i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularOneRAttributeEval(reduced, i), i + 1);
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
