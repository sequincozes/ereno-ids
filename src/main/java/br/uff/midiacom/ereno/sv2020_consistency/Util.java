/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.sv2020_consistency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import br.uff.midiacom.ereno.legacy.substation.FeatureAvaliada;
import static br.uff.midiacom.ereno.sv2020_consistency.Parameters.MULTICLASS_FILES;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author silvio
 */
public class Util {

    public static void quickSort(FeatureAvaliada[] vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separar(vetor, inicio, fim);
            quickSort(vetor, inicio, posicaoPivo - 1);
            quickSort(vetor, posicaoPivo + 1, fim);
        }
    }

    private static int separar(FeatureAvaliada[] vetor, int inicio, int fim) {
        FeatureAvaliada pivo = vetor[inicio];
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getValorFeature() <= pivo.getValorFeature()) {
                i++;
            } else if (pivo.getValorFeature() < vetor[f].getValorFeature()) {
                f--;
            } else {
                FeatureAvaliada troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = pivo;
        return f;
    }

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    public static Instances applyFilterKeep(Instances instances) {
        int[] fs = Parameters.FEATURE_SELECTION;
        Arrays.sort(fs);
        int deletadas = 0;
        for (int i = instances.numAttributes() - 1; i > 0; i--) {
            if (instances.numAttributes() <= fs.length) {
                System.err.println("O número de features precisa ser maior que o filtro.");
                return instances;
            }
            boolean deletar = true;
            for (int j : fs) {
                if (i == j) {
                    deletar = false;
//                    System.out.println("Manter [" + i + "]:" + instances.attribute(i));;
                }
            }
            if (deletar) {
                instances.deleteAttributeAt(i - 1);
            }
        }
        return instances;
    }

    private static Instances normalizar(Instances instances) throws Exception {
        Normalize filter = new Normalize();
        filter.setInputFormat(instances);
        instances = Filter.useFilter(instances, filter);
        return instances;
    }

    public static Instances[] loadAndFilter(boolean printSelection, int trainProportion, boolean balance) throws Exception {

        Instances normalInstances = new Instances(Util.readDataFile(Parameters.NORMAL_FILE));
        System.out.println("Normal: " + normalInstances.size());
        System.out.println("combinatedInstances(0)" + normalInstances.get(0));

        Instances attackInstances = new Instances(Util.readDataFile(Parameters.getAttackFile()));
        System.out.println("Attacks: " + attackInstances.size());

        int endNormal = normalInstances.size();

        Instances combinatedInstances = new Instances(normalInstances);
        combinatedInstances.addAll(attackInstances);
        System.out.println("combinatedInstances(0)" + combinatedInstances.get(0));
        System.out.println("combinatedInstances(end)" + combinatedInstances.get(combinatedInstances.size() - 1));
        Instances train = new Instances(combinatedInstances, 0, 1);
        Instances test = new Instances(combinatedInstances, 1, 2);

        train.remove(0);
        test.remove(0);

        int threshold = (int) (attackInstances.size() / trainProportion);
        System.out.println("Threshold: " + threshold);

        int index = 0;
        int k = 0; // when achieve K, put to train dataset

        System.out.println("Combinated Size: " + combinatedInstances.size());

        if (balance) {
            for (Instance i : combinatedInstances) {
                index++;
                if ((train.size() < threshold) || ((index > endNormal) && (train.size() < 2 * threshold))) {
                    if (k == trainProportion) {
                        train.add(i);
                        k = 0;
                    } else {
                        test.add(i); // add to test until reach k
                        k = k + 1;
                    }

                } else {
                    test.add(i); // add remaining only to test
                }
            }
        } else {
            k = 0;
            for (Instance i : combinatedInstances) {
                if (k == trainProportion) {
                    train.add(i);
                    k = 0;
                } else {
                    test.add(i); // add to test until reach k
                    k = k + 1;
                }
            }
        }

        System.out.println("Train: " + train.size());
        System.out.println("Test: " + test.size());

        if (Parameters.NORMALIZE) {
            train = normalizar(train);
            test = normalizar(test);
        }

        if (Parameters.FEATURE_SELECTION.length > 0) {
            test = Util.applyFilterKeep(test);
            train = Util.applyFilterKeep(train);

            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
            }
            test.setClassIndex(test.numAttributes() - 1);
            train.setClassIndex(train.numAttributes() - 1);

        }

        return new Instances[]{train, test};

    }

    public static Instances[] loadAndFilterMulticlass(boolean printSelection, int trainProportion, boolean balance) throws Exception {

        Instances normalInstances = new Instances(Util.readDataFile(Parameters.NORMAL_FILE));
        System.out.println("Normal: " + normalInstances.size());
        int endNormal = normalInstances.size();

        int attacks = 0;
        System.out.println("Number of classes: " + MULTICLASS_FILES.length);
        for (String file : MULTICLASS_FILES) {
            Instances multiclassInstances = new Instances(Util.readDataFile(file));
            normalInstances.addAll(multiclassInstances);
            attacks = multiclassInstances.size();
            System.out.println("Attacks: " + attacks);
        }
        Instances train = new Instances(normalInstances, 0, 1);
        Instances test = new Instances(normalInstances, 1, 2);

        train.remove(0);
        test.remove(0);

        int threshold = (int) (attacks / trainProportion);
        System.out.println("Threshold: " + threshold);

        int index = 0;
        int k = 0; // when achieve K, put to train dataset
        if (balance) {
            for (Instance i : normalInstances) {
                index++;
                if ((train.size() < threshold) || ((index > endNormal) && (train.size() < 2 * threshold))) {
                    if (k == trainProportion) {
                        train.add(i);
                        k = 0;
                    } else {
                        test.add(i); // add to test until reach k
                        k = k + 1;
                    }
                } else {
                    test.add(i); // add remaining only to test
                }
            }
        } else {
            for (Instance i : normalInstances) {
                if (k == trainProportion) {
                    train.add(i);
                    k = 0;
                } else {
                    test.add(i); // add to test until reach k
                    k = k + 1;
                }
            }
        }

        System.out.println("Train: " + train.size());
        System.out.println("Test: " + test.size());

        if (Parameters.NORMALIZE) {
            train = normalizar(train);
            test = normalizar(test);
        }

        if (Parameters.FEATURE_SELECTION.length > 0) {
            test = Util.applyFilterKeep(test);
            train = Util.applyFilterKeep(train);

            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
            }

        }
        test.setClassIndex(test.numAttributes() - 1);
        train.setClassIndex(train.numAttributes() - 1);
        return new Instances[]{train, test};

    }

    public static SimpleKMeans clusterData(Instances evaluation, int k) throws Exception {
        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setSeed(k);
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);
        kmeans.buildClusterer(evaluation);
        return kmeans;

    }

}
