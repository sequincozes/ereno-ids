/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import br.uff.midiacom.ereno.legacy.substation.FeatureAvaliada;

import java.util.Random;

import static br.uff.midiacom.ereno.legacy.substation.Parameters.NORMALIZE;
import static br.uff.midiacom.ereno.legacy.substation.Util.readDataFile;

import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.OneRAttributeEval;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericCleaner;

/**
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

    public static void justRank(String file, METODO metodo, int starFromIndex) throws Exception {
        System.out.println("Método: " + metodo + "/ dataset: " + file);
        Instances instances = new Instances(readDataFile(file));
        instances.setClassIndex(instances.numAttributes() - 1);
        System.out.println("instances attrs " + instances.numAttributes());
        if (NORMALIZE) {
            instances = normalizar(instances);
        }
        FeatureAvaliada[] allFeatures = new FeatureAvaliada[instances.numAttributes() - 1];
        switch (metodo) {
            case IG:
                System.out.println("IG:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    try {
                        allFeatures[i] = new FeatureAvaliada(calcularaIG(instances, i), i + 1);
                        if (CSV) {
                            System.out.println("IG;" + allFeatures[i].indiceFeature + ";" + instances.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                        } else {
                            System.out.println("IG: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + instances.attribute(i).name() + ")");
                        }
                    } catch (java.lang.IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Relief:
                System.out.println("Relief:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularReliefF(instances, i), i + 1);
                }
                break;
            case GR:
                System.out.println("GR:");
                if (GeneralParameters.NUMERIC_CLEANNER) {
                    NumericCleaner decimals = new NumericCleaner();                         // new instance of filter
                    decimals.setInputFormat(instances);
                    decimals.setDecimals(5);
                    instances = Filter.useFilter(instances, decimals);   // apply filter
                }
                for (int i = starFromIndex; i < instances.numAttributes() - 1; i++) {

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
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    try {
                        allFeatures[i] = new FeatureAvaliada(calcularOneRAttributeEval(instances, i), i + 1);
                    } catch (Exception e) {
                        System.out.println("Erro ao avaliar Feature " + i + " " + e.getMessage() + ", set OneR value to 0.");
                        allFeatures[i] = new FeatureAvaliada(0, i + 1);
                    }
                }
                break;
            default:
                System.out.println("Método incorreto.");
        }

        Util.quickSort(allFeatures, 0, allFeatures.length - 1);
        int i = 0;

        for (FeatureAvaliada feature : allFeatures) {
            System.out.println(feature.getIndiceFeature() + "-" + feature.getValorFeature());
        }
    }

    public static enum METODO {
        GR, IG, Relief, OneR;
    }

    ;

    public static void main(String[] args) throws Exception {
        System.out.println("WSN OneR");
        justRank(GeneralParameters.SWAT30pct, METODO.OneR);

    }

    public static void justMergeAndRank(String file1, String file2, METODO metodo) throws Exception {
        System.out.println("Método: " + metodo);
        Instances instances1 = new Instances(readDataFile(file1));
        Instances instances = new Instances(readDataFile(file2));
        instances.addAll(instances1);

        instances.setClassIndex(instances.numAttributes() - 1);
        if (NORMALIZE) {
            instances = normalizar(instances);
        }
        FeatureAvaliada[] allFeatures = new FeatureAvaliada[instances.numAttributes() - 1];
        switch (metodo) {
            case IG:
                System.out.println("IG:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    try {
                        allFeatures[i] = new FeatureAvaliada(calcularaIG(instances, i), i + 1);
                        if (CSV) {
                            System.out.println("IG;" + allFeatures[i].indiceFeature + ";" + instances.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                        } else {
                            System.out.println("IG: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + instances.attribute(i).name() + ")");
                        }
                    } catch (java.lang.IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Relief:
                System.out.println("Relief:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularReliefF(instances, i), i + 1);
                }
                break;
            case GR:
                System.out.println("GR:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
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
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularOneRAttributeEval(instances, i), i + 1);
                }
                break;
            default:
                System.out.println("Método incorreto.");
        }

        Util.quickSort(allFeatures, 0, allFeatures.length - 1);
        int i = 0;

        for (FeatureAvaliada feature : allFeatures) {
            System.out.println(feature.getIndiceFeature() + "-" + feature.getValorFeature());
        }
    }

    public static void justMergeAndRank(Instances instances, METODO metodo) throws Exception {
        System.out.println("Método: " + metodo);

        instances.setClassIndex(instances.numAttributes() - 1);
        if (NORMALIZE) {
            instances = normalizar(instances);
        }
        FeatureAvaliada[] allFeatures = new FeatureAvaliada[instances.numAttributes() - 1];
        switch (metodo) {
            case IG:
                System.out.println("IG:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    try {
                        allFeatures[i] = new FeatureAvaliada(calcularaIG(instances, i), i + 1);
                        if (CSV) {
                            System.out.println("IG;" + allFeatures[i].indiceFeature + ";" + instances.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                        } else {
                            System.out.println("IG: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + instances.attribute(i).name() + ")");
                        }
                    } catch (java.lang.IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Relief:
                System.out.println("Relief:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularReliefF(instances, i), i + 1);
                }
                break;
            case GR:
                System.out.println("GR:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
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
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularOneRAttributeEval(instances, i), i + 1);
                }
                break;
            default:
                System.out.println("Método incorreto.");
        }

        Util.quickSort(allFeatures, 0, allFeatures.length - 1);
        int i = 0;

        for (FeatureAvaliada feature : allFeatures) {
            System.out.println(feature.getIndiceFeature() + "-" + feature.getValorFeature());
        }
    }

    public static void justRank(String file, METODO metodo) throws Exception {
        System.out.println("Método: " + metodo + "/ dataset: " + file);
        Instances instances = new Instances(readDataFile(file));
        instances.setClassIndex(instances.numAttributes() - 1);
//        instances.deleteAttributeAt(42);
//        instances.deleteAttributeAt(41);
//        instances.deleteAttributeAt(40);
//        instances.deleteAttributeAt(39);
//        instances.deleteAttributeAt(38);
//        instances.deleteAttributeAt(37);
//        instances.deleteAttributeAt(36);
//        instances.deleteAttributeAt(35);
//        instances.deleteAttributeAt(34);
//        instances.deleteAttributeAt(33);
//        instances.deleteAttributeAt(32);
//        instances.deleteAttributeAt(31);
//        instances.deleteAttributeAt(30);
//        instances.deleteAttributeAt(29);
//        instances.deleteAttributeAt(28);
//        instances.deleteAttributeAt(27);
//        instances.deleteAttributeAt(26);
//        instances.deleteAttributeAt(25);
//        instances.deleteAttributeAt(24);
//        instances.deleteAttributeAt(23);
//        instances.deleteAttributeAt(22);
//        instances.deleteAttributeAt(21);
//        instances.deleteAttributeAt(20);
//        instances.deleteAttributeAt(19);
//        instances.deleteAttributeAt(18);
//        instances.deleteAttributeAt(17);
//        instances.deleteAttributeAt(16);
//        instances.deleteAttributeAt(15);
//        instances.deleteAttributeAt(14);
//        instances.deleteAttributeAt(13);
//        instances.deleteAttributeAt(12);
//        instances.deleteAttributeAt(11);
//        instances.deleteAttributeAt(10); // A nominal attribute (vsmA) cannot have duplicate labels ('(-0.088808--0.088808]').
//        instances.deleteAttributeAt(9);
//        instances.deleteAttributeAt(8);
//        instances.deleteAttributeAt(7); // A nominal attribute (vsbA) cannot have duplicate labels ('(0.094344-0.094344]').
//        instances.deleteAttributeAt(6); // A nominal attribute (ismC) cannot have duplicate labels ('(0.062934-0.062934]').
//        instances.deleteAttributeAt(5); // A nominal attribute (ismB) cannot have duplicate labels ('(-0.000051--0.000051]').
//        instances.deleteAttributeAt(4); // A nominal attribute (ismA) cannot have duplicate labels ('(-0.000023--0.000023]').
//        instances.deleteAttributeAt(3); //  A nominal attribute (isbC) cannot have duplicate labels ('(-0.064013--0.064013]').
//        instances.deleteAttributeAt(2);
//        instances.deleteAttributeAt(1);
//        instances.deleteAttributeAt(0);
        System.out.println("instances attrs " + instances.numAttributes());
        if (NORMALIZE) {
            instances = normalizar(instances);
        }
        FeatureAvaliada[] allFeatures = new FeatureAvaliada[instances.numAttributes() - 1];
        switch (metodo) {
            case IG:
                System.out.println("IG:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    try {
                        allFeatures[i] = new FeatureAvaliada(calcularaIG(instances, i), i + 1);
                        if (CSV) {
                            System.out.println("IG;" + allFeatures[i].indiceFeature + ";" + instances.attribute(i).name() + ";" + allFeatures[i].valorFeature);
                        } else {
                            System.out.println("IG: [" + allFeatures[i].indiceFeature + "] Ganho: " + allFeatures[i].valorFeature + " (" + instances.attribute(i).name() + ")");
                        }
                    } catch (java.lang.IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Relief:
                System.out.println("Relief:");
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    allFeatures[i] = new FeatureAvaliada(calcularReliefF(instances, i), i + 1);
                }
                break;
            case GR:
                System.out.println("GR:");
                if (GeneralParameters.NUMERIC_CLEANNER) {
                    NumericCleaner decimals = new NumericCleaner();                         // new instance of filter
                    decimals.setInputFormat(instances);
                    decimals.setDecimals(5);
                    instances = Filter.useFilter(instances, decimals);   // apply filter
                }
                for (int i = 0; i < instances.numAttributes() - 1; i++) {

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
                for (int i = 0; i < instances.numAttributes() - 1; i++) {
                    try {
                        allFeatures[i] = new FeatureAvaliada(calcularOneRAttributeEval(instances, i), i + 1);
                    } catch (Exception e) {
                        System.out.println("Erro ao avaliar Feature " + i + " " + e.getMessage() + ", set OneR value to 0.");
                        allFeatures[i] = new FeatureAvaliada(0, i + 1);
                    }
                }
                break;
            default:
                System.out.println("Método incorreto.");
        }

        Util.quickSort(allFeatures, 0, allFeatures.length - 1);
        int i = 0;

        for (FeatureAvaliada feature : allFeatures) {
            System.out.println(feature.getIndiceFeature() + "-" + feature.getValorFeature());
        }
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

    public static FeatureAvaliada[] avaliarESelecionarFromGeneralParamter(int featuresSelecionar, METODO metodo, boolean debug) throws Exception {
        System.out.println("Método: " + metodo);
        Instances instances = new Instances(readDataFile(GeneralParameters.DATASET));
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


}
