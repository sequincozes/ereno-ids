/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package legacy.Full_SV_2020;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import legacy.substation.FeatureAvaliada;
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

    public static Instances[] loadAndFilter(boolean printSelection, int trainProportion) throws Exception {

        Instances allInstances = new Instances(Util.readDataFile(Parameters.ALL_IN_ONE_FILE));
        System.out.println("All instances: "+allInstances.size());
   

        Instances train = new Instances(allInstances, 0, 1);
        Instances test = new Instances(allInstances, 1, 2);
        train.remove(0);
        test.remove(0);
        
        int k = 0;
        for (Instance i : allInstances) {
            if (k == trainProportion) {
                train.add(i);
                k = 0;
            } else {
                test.add(i);
                k = k+1;
            }
        }

        System.out.println("Train: "+train.size());
        System.out.println("Teste: "+test.size());
     

        if (Parameters.NORMALIZE) {
            train = normalizar(train);
            test = normalizar(test);
        }

        if (Parameters.FEATURE_SELECTION.length > 0) {
            test = Util.applyFilterKeep(test);
            train = Util.applyFilterKeep(train);

            if (printSelection) {
                System.out.print(Arrays.toString(Parameters.FEATURE_SELECTION) + " - ");
                System.out.println("train: " + train.numAttributes());
                System.out.print("test: " + test.numAttributes());
            }
            test.setClassIndex(test.numAttributes() - 1);
            train.setClassIndex(train.numAttributes() - 1);

        }

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
