/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.abstractclassification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import br.uff.midiacom.ereno.legacy.substation.FeatureAvaliada;
import java.util.ArrayList;
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

    public static double normalClass;

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
        int[] fs = GeneralParameters.FEATURE_SELECTION;
        Arrays.sort(fs);
        for (int i = instances.numAttributes() - 1; i > 0; i--) {
            if (instances.numAttributes() <= fs.length) {
                System.err.println("O número de features precisa ser maior que o filtro.");
                System.out.println("FS: " + Arrays.toString(fs) + " - Instance[0]: " + instances.get(0).toString());
                System.exit(1);
                return instances;
            }
            boolean deletar = true;
            for (int j : fs) {
                if (i == j) {
                    deletar = false;
                }
            }
            if (deletar) {
                instances.deleteAttributeAt(i - 1);
            }
        }

        return instances;
    }

    public static Instances copyAndFilter(Instances ref, boolean printSelection) {
        Instances allInstances = new Instances(ref);
        if (GeneralParameters.FEATURE_SELECTION.length > 0) {
            Util.applyFilterKeep(allInstances);
            if (printSelection) {
                System.out.println(Arrays.toString(GeneralParameters.FEATURE_SELECTION) + " - " + allInstances.numAttributes() + " attributes in fact.");
            }
        }
        allInstances.setClassIndex(allInstances.numAttributes() - 1);
        normalClass = allInstances.get(0).classValue();
        return allInstances;
    }

    private static Instances normalizar(Instances instances) throws Exception {
        Normalize filter = new Normalize();
        filter.setInputFormat(instances);
        instances = Filter.useFilter(instances, filter);
        return instances;
    }

    public static Instances[] loadAndFilter(boolean printSelection, int trainProportion) throws Exception {

        Instances allInstances = new Instances(Util.readDataFile(GeneralParameters.DATASET));
        System.out.println("All instances: " + allInstances.size());
        normalClass = allInstances.get(0).classValue();

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
                k = k + 1;
            }
        }

        System.out.println("Train: " + train.size());
        System.out.println("Teste: " + test.size());

        if (GeneralParameters.NORMALIZE) {
            train = normalizar(train);
            test = normalizar(test);
        }

        if (GeneralParameters.FEATURE_SELECTION.length > 0) {
            test = Util.applyFilterKeep(test);
            train = Util.applyFilterKeep(train);

            if (printSelection) {
                System.out.print(Arrays.toString(GeneralParameters.FEATURE_SELECTION) + " - ");
                System.out.println("train: " + train.numAttributes());
                System.out.print("test: " + test.numAttributes());
            }
            test.setClassIndex(test.numAttributes() - 1);
            train.setClassIndex(train.numAttributes() - 1);

        }

        return new Instances[]{train, test};

    }

    public static Instances loadAndFilterSingleFile(boolean printSelection) throws Exception {

        Instances allInstances = new Instances(Util.readDataFile(GeneralParameters.DATASET));
        if (printSelection) {
            System.out.println("All instances: " + allInstances.size());
        }
        if (GeneralParameters.NORMALIZE) {
            allInstances = normalizar(allInstances);
        }

        if (GeneralParameters.FEATURE_SELECTION.length > 0) {
            allInstances = Util.applyFilterKeep(allInstances);
            if (printSelection) {
                System.out.print(Arrays.toString(GeneralParameters.FEATURE_SELECTION) + " - ");
                System.out.println("All instances: " + allInstances.numAttributes());
            }
        }
        allInstances.setClassIndex(allInstances.numAttributes() - 1);
        normalClass = allInstances.get(0).classValue();
        return allInstances;

    }

    public static Instances loadSingleFile(boolean printSelection) throws Exception {
        Instances allInstances = new Instances(Util.readDataFile(GeneralParameters.DATASET));
        System.out.println("All instances: " + allInstances.size());

        if (GeneralParameters.NORMALIZE) {
            allInstances = normalizar(allInstances);
        }

        return allInstances;

    }

    public static SimpleKMeans clusterData(Instances evaluation, int k) throws Exception {
        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setSeed(k);
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(k);
        kmeans.buildClusterer(evaluation);
        return kmeans;

    }

    public static int[] getArray(ArrayList<Integer> fs) {
        int[] array = new int[fs.size()];
        for (int i = 0; i < fs.size(); i++) {
            array[i] = fs.get(i);
        }
        return array;
    }

    public static GenericResultado getResultAverage(GenericResultado[] results) {
        float VP = 0;
        float FN = 0;
        float VN = 0;
        float FP = 0;;
        long Time = 0;
        double acuracia = 0;
        double recall = 0;
        double precision = 0;
        double f1score = 0;

        try {
            for (GenericResultado res : results) {
                VP = VP + (res.getVP() / results.length);
                FN = FN + (res.getFN() / results.length);
                VN = VN + (res.getVN() / results.length);
                FP = FP + (res.getFP() / results.length);
                Time = Time + (res.getTime() / results.length);
                acuracia = acuracia + (res.getAcuracia() / results.length);
                recall = recall + (res.getRecall() / results.length);
                precision = precision + (res.getPrecision() / results.length);
                f1score = f1score + (res.getF1Score() / results.length);
            }
        } catch (NullPointerException e) {
            return new GenericResultado();
        }
        return new GenericResultado(results[0].getCx(), VP, FN, VN, FP, Time, acuracia, recall, recall, f1score, recall);
    }

    public static GenericResultado getResultAverageDetailed(GenericResultado[] results, boolean debug) {
        float VP = 0;
        float FN = 0;
        float VN = 0;
        float FP = 0;
        long avgTime = 0;
        double acuracia = 0;
        double recall = 0;
        double precision = 0;
        double f1score = 0;
        long[] times = new long[results.length];
        double stdDvTime = 0;
        double varianceTime = 0;
        double loConfIntTime = 0;
        double hiConfIntTime = 0;
        try {
            int pos = 0;
            for (GenericResultado res : results) {
                long individualTime = res.getMicrotime();
                if (debug) {
                    System.out.println("Individual Time: " + individualTime + " Microsseconds");
                }
                times[pos++] = individualTime;
                VP = VP + (res.getVP() / results.length);
                FN = FN + (res.getFN() / results.length);
                VN = VN + (res.getVN() / results.length);
                FP = FP + (res.getFP() / results.length);
                avgTime = avgTime + (individualTime / results.length);
                acuracia = acuracia + (res.getAcuracia() / results.length);
                recall = recall + (res.getRecall() / results.length);
                precision = precision + (res.getPrecision() / results.length);
                f1score = f1score + (res.getF1Score() / results.length);
            }

            //int MAXN = 100000;
            //int n = 0;
            //double[] x = new double[MAXN];
            // second pass: compute sample variance
            double xxbar = 0.0;
            for (int i = 0; i < times.length; i++) {
                xxbar += (times[i] - avgTime) * (times[i] - avgTime);
            }
            varianceTime = xxbar / (times.length - 1);
            stdDvTime = Math.sqrt(varianceTime);
            double raizDeN = Math.sqrt(times.length);
            loConfIntTime = avgTime - 1.96 * stdDvTime / raizDeN;
            hiConfIntTime = avgTime + 1.96 * stdDvTime / raizDeN;

            if (debug) {
                // print results
                System.out.println("average          = " + avgTime);
                System.out.println("sample variance  = " + varianceTime);
                System.out.println("sample stddev    = " + stdDvTime);
                System.out.println("95% approximate confidence interval");
                System.out.println("[ " + loConfIntTime + ", " + hiConfIntTime + " ]");
            }

        } catch (NullPointerException e) {
            //  System.out.println("Null ");
            return new GenericResultado();
        }
        return new GenericResultado(results[0].getCx(), VP, FN, VN, FP, avgTime, acuracia, recall, f1score, varianceTime, stdDvTime, loConfIntTime, hiConfIntTime);
    }

    //resultsCompilation[fold][classifierIndex++]
    /*
        @return average per fold, not per classifier 
     */
    public static void printAverageResults(GenericResultado[][] foldClassifierResults) {
        int classifierCount = foldClassifierResults[0].length;
        int foldCount = foldClassifierResults.length;
        System.out.println("Printing average from " + classifierCount + " classifiers with " + foldCount + " folds");
        GenericResultado[] classifierResultsAvg = new GenericResultado[classifierCount];

        for (int foldIndex = 0; foldIndex < foldCount; foldIndex++) {
            for (int classifierIndex = 0; classifierIndex < foldClassifierResults[0].length; classifierIndex++) {
                if (foldIndex == 0) { //Initialize classifier aggregation
                    classifierResultsAvg[classifierIndex] = new GenericResultado(foldClassifierResults[foldIndex][classifierIndex].Cx, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                }

                classifierResultsAvg[classifierIndex].VP = classifierResultsAvg[classifierIndex].VP + (foldClassifierResults[foldIndex][classifierIndex].getVP() / foldCount);
                classifierResultsAvg[classifierIndex].VN = classifierResultsAvg[classifierIndex].VN + (foldClassifierResults[foldIndex][classifierIndex].getVN() / foldCount);
                classifierResultsAvg[classifierIndex].FP = classifierResultsAvg[classifierIndex].FP + (foldClassifierResults[foldIndex][classifierIndex].getFP() / foldCount);
                classifierResultsAvg[classifierIndex].FN = classifierResultsAvg[classifierIndex].FN + (foldClassifierResults[foldIndex][classifierIndex].getFN() / foldCount);
                classifierResultsAvg[classifierIndex].Time = classifierResultsAvg[classifierIndex].Time + (foldClassifierResults[foldIndex][classifierIndex].getTime() / foldCount);
                classifierResultsAvg[classifierIndex].acuracia = classifierResultsAvg[classifierIndex].acuracia + (foldClassifierResults[foldIndex][classifierIndex].getAcuracia() / foldCount);
                classifierResultsAvg[classifierIndex].recall = classifierResultsAvg[classifierIndex].recall + (foldClassifierResults[foldIndex][classifierIndex].getRecall() / foldCount);
                classifierResultsAvg[classifierIndex].precision = classifierResultsAvg[classifierIndex].precision + (foldClassifierResults[foldIndex][classifierIndex].getPrecision() / foldCount);
                classifierResultsAvg[classifierIndex].f1Score = classifierResultsAvg[classifierIndex].f1Score + (foldClassifierResults[foldIndex][classifierIndex].getF1Score() / foldCount);
            }
        }
        boolean header = false;
        for (GenericResultado classifierRes : classifierResultsAvg) {
            if (!header) {
                classifierRes.printResultsHeader();
                header = true;
            }
            classifierRes.printResults();
        }

    }

}
