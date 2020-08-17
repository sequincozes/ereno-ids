/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.abstractclassification;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author sequi
 */
public class GenericResultado {

    String Cx;
    float VP, FN, VN, FP;
    //long Time;
    double acuracia, recall, precision, f1Score;
    double cpuLoad, memoryLoad;
    double varianceTime, stdDvTime, loConfIntTime, hiConfIntTime;
    ArrayList<Long> detailedClassificationTime;
    private long nanotime;
    private int testDatasetSize;
    float avgTime;
    long Time;

    public void setAvgTime(float avgTime) {
        this.avgTime = avgTime;
    }

    public float getAvgTime() {
        return avgTime;
    }
    public int getTestDatasetSize() {
        return testDatasetSize;
    }

    public long getNanotime() {
        return nanotime;
    }

    public long getMicrotime() {
        return nanotime / 1000;
    }

    public long getMilitime() {
        return getMicrotime() / 1000;
    }

    public long getSectime() {
        return getMilitime() / 1000;
    }

    public GenericResultado(double acuracia) {
        this.acuracia = acuracia;
    }

    public GenericResultado(GenericResultado evaluation) {
        this.Cx = evaluation.getCx();
        this.VP = evaluation.getVP();
        this.FN = evaluation.getFN();
        this.VN = evaluation.getVN();
        this.FP = evaluation.getFP();
        this.Time = evaluation.getTime();
        this.acuracia = evaluation.getAcuracia();
        this.recall = evaluation.getRecall();
        this.precision = evaluation.getPrecision();
        this.f1Score = evaluation.getF1Score();
        this.cpuLoad = evaluation.getCpuLoad();
        this.memoryLoad = evaluation.getMemoryLoad();
    }

    GenericResultado() {

    }

    GenericResultado(String cx, float VP, float FN, float VN, float FP, float avgTime, double acuracia, double recall, double f1score, double varianceTime, double stdDvTime, double loConfIntTime, double hiConfIntTime) {
        this.Cx = cx;
        this.VP = VP;
        this.FN = FN;
        this.VN = VN;
        this.FP = FP;
        this.avgTime = avgTime;
        this.acuracia = acuracia;
        this.recall = recall;
        this.f1Score = f1score;
        this.varianceTime = varianceTime;
        this.stdDvTime = stdDvTime;
        this.loConfIntTime = loConfIntTime;
        this.hiConfIntTime = hiConfIntTime;
    }

    public int[][] getConfusionMatrix() {
        return confusionMatrix;
    }
    public int[][] confusionMatrix = new int[6][6];

    public GenericResultado(String Cx, float VP, float FN, float VN, float FP, long Time, double acuracia, double txDet, double txAFal, double cpuLoad, double memoryLoad) {
        this.Cx = Cx;
        this.VP = VP;
        this.FN = FN;
        this.VN = VN;
        this.FP = FP;
        this.Time = Time;
        this.acuracia = acuracia;
        this.recall = txDet;
        this.precision = txAFal;
        this.cpuLoad = cpuLoad;
        this.memoryLoad = memoryLoad;
    }

    public GenericResultado(String descricao, float VP, float FN, float VN, float FP, double acuracia, double txDet, double txAFal, double f1score) {
        this.Cx = descricao;
        this.VP = VP;
        this.FN = FN;
        this.VN = VN;
        this.FP = FP;
        this.acuracia = acuracia;
        this.recall = txDet;
        this.precision = txAFal;
        this.f1Score = f1score;
    }

    public GenericResultado(String descricao, float VP, float FN, float VN, float FP, long time) {
        this.Cx = descricao;
        this.VP = VP;
        this.FN = FN;
        this.VN = VN;
        this.FP = FP;
        this.nanotime = time;
        recalcular();
    }

    public GenericResultado(String descricao, float VP, float FN, float VN, float FP, long time, int[][] confusionMatrix) {
        this.Cx = descricao;
        this.VP = VP;
        this.FN = FN;
        this.VN = VN;
        this.FP = FP;
        this.Time = time;
        this.confusionMatrix = confusionMatrix;
        recalcular();
    }

    public GenericResultado(String descricao, float VP, float FN, float VN, float FP) {
        this.Cx = descricao;
        this.VP = VP;
        this.FN = FN;
        this.VN = VN;
        this.FP = FP;
    }

    public GenericResultado recalcular() {
        this.acuracia = Float.valueOf(((getVP() + getVN()) * 100) / (getVP() + getVN() + getFP() + getFN())) / 100;
        this.recall = Float.valueOf((getVP() * 100) / (getVP() + getFN())) / 100;
        this.precision = Float.valueOf((getVP() * 100) / (getVP() + getFP())) / 100;
        this.f1Score = Float.valueOf((float) (2 * (recall * precision))) / ((recall + precision));
        return this;
    }

    public String getCx() {
        return Cx;
    }

    public void setCx(String Cx) {
        this.Cx = Cx;
    }

    public float getVP() {
        return VP;
    }

    public void setVP(float VP) {
        this.VP = VP;
    }

    public float getFN() {
        return FN;
    }

    public void setFN(float FN) {
        this.FN = FN;
    }

    public float getVN() {
        return VN;
    }

    public void setVN(float VN) {
        this.VN = VN;
    }

    public float getFP() {
        return FP;
    }

    public void setFP(float FP) {
        this.FP = FP;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long Time) {
        this.Time = Time;
    }

    public double getAcuracia() {
        String acc = String.valueOf(acuracia);
        if (acc.length() > 6) {
            acc = acc.substring(0, 7);
        }
        acuracia = Double.valueOf(acc);
        return acuracia;
    }

    public void printResults(String nome, boolean persistOnDisk) throws IOException {
        System.out.print(nome + "|");
        double cpu = getCpuLoad();
        double memory = getMemoryLoad();

        String cpuS = "";
        String memoryS = "";

        try {
            cpuS = String.valueOf(cpu).substring(0, 4);
        } catch (Exception e) {
            System.out.println("Errinho: " + e.getLocalizedMessage());
        }

        try {
            memory = memory / 1000;
            memoryS = String.valueOf(memory).substring(0, 4) + "K";
        } catch (Exception e) {
            System.out.println("Errinho: " + e.getLocalizedMessage());
        }

//        System.out.print("CPU: (" + cpuS + "%), Memória: " + memoryS + " Tempo: " + getTime() + ", "
        System.out.print("Acc: " + acuracia);
        try {
            System.out.print(" Tx. VP: " + String.valueOf(recall).substring(0, 7).replace(".", ","));
            System.out.print(" Tx. FP: " + String.valueOf(precision).substring(0, 7).replace(".", ","));
        } catch (Exception e) {
            System.out.print(" Tx. VP: " + String.valueOf(recall).replace(".", ","));
            System.out.print(" Tx. FP: " + String.valueOf(precision).replace(".", ","));
        }
        System.out.print(" (VN: " + VN);
        System.out.print(" VP: " + VP);
        System.out.print(" FN: " + FN);
        System.out.println(" FP: " + FP + ")");
        if (persistOnDisk) {
            FileWriter arq = new FileWriter("d:\\resultados.txt", true);
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.append(Cx + "| CPU: " + cpuS + "%|, Memória: " + memoryS + " |Tempo: " + getTime() + "|, "
                    + "Acc: " + acuracia + "%s" + ""
                    + " (VN: " + VN + " VP: " + VP + " FN: " + FN + " FP: " + FP + ")\r\n");
            arq.close();
        }
    }

    public void printResultsHeader() {
        System.out.println("\nClassifier;Accuracy;Precision;Recall;F1Score;VP;VN;FP;FN;time");
    }

    public void printResults() {
        recalcular();
        System.out.println(
                getCx() + ";"
                + getAcuracia() + ";"
                + getPrecision() + ";"
                + getRecall() + ";"
                + getF1Score() + ";"
                + getVP() + ";"
                + getVN() + ";"
                + getFP() + ";"
                + getFN() + ";"
                + getTime()
        );
    }

    public void printDetailedResults() {
        recalcular();
        System.out.println(
                getCx() + ";"
                + getAcuracia() + ";"
                + getPrecision() + ";"
                + getRecall() + ";"
                + getF1Score() + ";"
                + getVP() + ";"
                + getVN() + ";"
                + getFP() + ";"
                + getFN() + ";"
                + getTime() + ";"
                + stdDvTime + ";"
                + hiConfIntTime + ";"
                + loConfIntTime + ";"
        );
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public void setMemoryLoad(double memoryLoad) {
        this.memoryLoad = memoryLoad;
    }

    public double getMemoryLoad() {
        return memoryLoad;
    }

    public void printIterations(String nome, String dirietorioParaGravar) throws IOException {
        System.out.print("CLASSIFICADOR" + "	");
        System.out.print("ACURÁCIA" + "	");
        System.out.print("DETECÇÃO" + "	");
        System.out.print("ALARMES FALSOS" + "	");

        FileWriter arq = new FileWriter(dirietorioParaGravar + ":\\resultados_" + nome + "_.txt", true);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.append(nome + "	"
                + acuracia + "	"
                + (VP / (VP + FN)) + "	"
                + (FP / (FP + VN)) + "	"
                + "\r\n");
        arq.close();
    }

    public double getRecall() {
        return recall;
    }

    public double getF1Score() {
        return f1Score;
    }

    public void setRecall(double taxaDeteccao) {
        this.recall = taxaDeteccao;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double taxaAlarmeFalsos) {
        this.precision = taxaAlarmeFalsos;
    }

    public void printDetailedTime() {
        System.out.println(
                getCx() + ";"
                + getAvgTime() + ";"
                + hiConfIntTime + ";"
                + loConfIntTime + ";"
        );
    }

    public void printDetailedTime(String details) {
        System.out.println(
                 details + ";"
                + getAvgTime()+ ";"
                + hiConfIntTime + ";"
                + loConfIntTime + ";"
        );
    }

    public void setTestSize(int size) {
        this.testDatasetSize = size;
    }
}
