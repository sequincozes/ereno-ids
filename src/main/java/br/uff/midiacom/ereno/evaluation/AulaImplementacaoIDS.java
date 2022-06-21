package br.uff.midiacom.ereno.evaluation;

import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AulaImplementacaoIDS {
    public static void main(String args[]) throws Exception {
        Instances datasetTreinamento = lerDataset("treinamento.arff");
        Instances datasetTestes = lerDataset("teste.arff");
        J48 classificador = construirJ48(datasetTreinamento);

        // Exemplo de classificação da primeira amostra de teste
        Instance amostra = datasetTreinamento.get(0);
        testarInstancia(classificador, amostra);
        double resultado = testarInstancia(classificador, datasetTestes.get(0));

        // Exemplo de processamento de resultado
        System.out.println("Classe esperada: " + amostra.classValue());
        System.out.println("Classe predita pelo J48: " + resultado);
    }

    private static double testarInstancia(J48 classificador, Instance amostra) throws Exception {
        return classificador.classifyInstance(amostra);
    }

    private static J48 construirJ48(Instances treinamento) throws Exception {
        J48 classificador = new J48();
        classificador.buildClassifier(treinamento);
        return classificador;
    }

    public static Instances lerDataset(String dataset) throws IOException {
        FileReader fr = new FileReader(dataset);
        BufferedReader br = new BufferedReader(fr);
        Instances datasetInstances = new Instances(br);
        datasetInstances.setClassIndex(datasetInstances.numAttributes() - 1);
        return datasetInstances;
    }

}
