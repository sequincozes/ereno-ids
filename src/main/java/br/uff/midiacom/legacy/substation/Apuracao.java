/**
 * Classe	Normal	Back	Land	Neptune	Pod	Smurf	Teardrop Treino	19455	440	5	21440
 * 52	56158	195 Teste	77823	1763	16	85761	212	224633	784 Total	97278	2203	21
 * 107201	264	280791	979
 */
package br.uff.midiacom.legacy.substation;

//import WSN_DS.ValidacaoWSN;
//import CICIDS2017.ValidacaoWSN;
import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class Apuracao {

    boolean medirCpu = true;
    boolean debug = false;
    float VP = 0;
    float FP = 0;
    float VN = 0;
    float FN = 0;

    double cpuLoad = 0;
    double memoryLoad = 0;

    String attackFolder;
    String normalsFile;
    String ataquesFile;
    int expectedAttacks;
    int expectedNormals;
    ArrayList<Classifier> classifiers;
    String trainFile;
    Instances trainInstances;
    Instances testAttackInstances;
    Instances testNormalInstances;
    boolean showProgress;
    boolean normals;
    boolean attacks;
    long tempo = 0;
    int features;

    public Apuracao(
            int expectedAttacks,
            int expectedNormals,
            String attackType,
            boolean showProgress,
            String diretorio,
            String trainFile,
            String ataquesFile,
            String normalsFile,
            int features
    ) {
        this.expectedAttacks = expectedAttacks;
        this.expectedNormals = expectedNormals;
        this.showProgress = showProgress;
        this.trainFile = diretorio + attackType + "/"+  trainFile;
        this.normalsFile = diretorio + attackType + "/"+ normalsFile;
        this.ataquesFile = diretorio + attackType + "/"+ ataquesFile;
        this.features = features;

    }

    public void loadAndFilter(int filter[], boolean keep) throws Exception {
        BufferedReader datafileTrain = readDataFile(getTrainFile());
        BufferedReader datafileTestAttack = readDataFile(getAtaquesFile());
        BufferedReader datafileTestNormal = readDataFile(getNormalsFile());

        trainInstances = new Instances(datafileTrain);
        testAttackInstances = new Instances(datafileTestAttack);
        testNormalInstances = new Instances(datafileTestNormal);

        //Filtros
        if (filter.length > 0) {
            if (keep) {
                testAttackInstances = applyFilterKeep(testAttackInstances, filter, getFeatures());
                trainInstances = applyFilterKeep(trainInstances, filter, getFeatures());
                testNormalInstances = applyFilterKeep(testNormalInstances, filter, getFeatures());
            } else {
                testAttackInstances = applyFilterDelete(testAttackInstances, filter);
                trainInstances = applyFilterDelete(trainInstances, filter);
                testNormalInstances = applyFilterDelete(testNormalInstances, filter);
            }
//            trainInstances = ValidacaoWSN.mormalizar(trainInstances);
//            testAttackInstances = ValidacaoWSN.mormalizar(testAttackInstances);
//            testNormalInstances = ValidacaoWSN.mormalizar(testNormalInstances);

        }
        System.out.print("[" + (trainInstances.numAttributes() - 1) + " Train Features] - ");
        System.out.print("[" + (testAttackInstances.numAttributes() - 1) + " Test Features] - ");
        System.out.print(Arrays.toString(filter) + " - ");
        trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
        testAttackInstances.setClassIndex(testAttackInstances.numAttributes() - 1);
        testNormalInstances.setClassIndex(testNormalInstances.numAttributes() - 1);
    }

    public void testInstanceAndRetroFeed(Classifier classifier, int pos, boolean isAttack, boolean retrofeed) throws Exception {

        if (isAttack) {
            Instance instance_i = testAttackInstances.instance(pos);
            double class1 = classifier.classifyInstance(instance_i);
            if (retrofeed) {
                instance_i.setClassValue(class1);
                trainInstances.add(instance_i);
            }
            if (class1 == 0.0) { // O classificador diz que nao é ataque
                setFN(getFN() + 1);         // Tráfego Ataque = Não detectado
                if (debug) {
                    System.err.println("[" + pos + "] FN: (" + class1 + ") deveria ser (1+ ataque)");
                }
            } else {
                setVP(getVP() + 1);         // Tráfego Ataque = Detectado
                if (debug) {
                    System.err.println("[" + pos + "] VP: (" + class1 + ") realmente é (1+ ataque)");
                }
            }
          
        } else {
            Instance instance_i = testNormalInstances.instance(pos);
            double class1 = classifier.classifyInstance(instance_i);
            if (retrofeed) {
                instance_i.setClassValue(class1);
                trainInstances.add(instance_i);
            }
            if (class1 == 0.0) {
                setVN(getVN() + 1);         // Tráfego Normal = Não detectado
                if (debug) {
                    System.err.println("[" + pos + "] VN: (" + class1 + ") realmente é (0 - normal)");
                }
            } else {
                setFP(getFP() + 1);         // Tráfego Normal = Detecção Incorreta
                if (debug) {
                    System.err.println("[" + pos + "] FP: (" + class1 + ") deveria ser (0 -normal)");
                }

            }
           }

    }

    public void testInstance(Classifier classifier, int pos, boolean isAttack) throws Exception {

        if (isAttack) {

            double class1 = classifier.classifyInstance(testAttackInstances.instance(pos));
            if (class1 == 0.0) { // O classificador diz que nao é ataque
                setFN(getFN() + 1);         // Tráfego Ataque = Não detectado
                if (debug) {
                    System.err.println("[" + pos + "] FN: (" + class1 + ") deveria ser (1+ ataque)");
                }
            } else {
                setVP(getVP() + 1);         // Tráfego Ataque = Detectado
                if (debug) {
                    System.err.println("[" + pos + "] VP: (" + class1 + ") realmente é (1+ ataque)");
                }
            }
            double cpuLoadAtual = 0;//CpuLoad.getProcessCpuLoad();
            double memoryLoadAtual = 0;//CpuLoad.getProccessMemory();

            if (getCpuLoad() > 0 && medirCpu) {
                double peso = (1 / (getFP() + getVN() + getFN() + getVP()));
                double pesoAtual = 1 - peso;
                double mediaCPU = (cpuLoadAtual * peso) + (getCpuLoad() * pesoAtual);
                double mediaMemory = (memoryLoadAtual * peso) + (getMemoryLoad() * pesoAtual);
                setCpuLoad(mediaCPU);
                setMemoryLoad(mediaMemory);

            } else {
                setCpuLoad(cpuLoadAtual);
                setMemoryLoad(memoryLoadAtual);
            }

        } else {
            double class1 = classifier.classifyInstance(testNormalInstances.instance(pos));
            if (class1 == 0.0) {
                setVN(getVN() + 1);         // Tráfego Normal = Não detectado
                if (debug) {
                    System.err.println("[" + pos + "] VN: (" + class1 + ") realmente é (0 - normal)");
                }
            } else {
                setFP(getFP() + 1);         // Tráfego Normal = Detecção Incorreta
                if (debug) {
                    System.err.println("[" + pos + "] FP: (" + class1 + ") deveria ser (0 -normal)");
                }

            }
            double cpuLoadAtual =0;// CpuLoad.getProcessCpuLoad();
            double memoryLoadAtual =0;// CpuLoad.getProccessMemory();

            if ((getCpuLoad() > 0) && medirCpu) {
                double peso = (1 / (getFP() + getVN() + getFN() + getVP()));
                double pesoAtual = 1 - peso;
                double mediaCPU = (cpuLoadAtual * peso) + (getCpuLoad() * pesoAtual);
                double mediaMemory = (memoryLoadAtual * peso) + (getMemoryLoad() * pesoAtual);
                setCpuLoad(mediaCPU);
                setMemoryLoad(mediaMemory);

            } else {
                setCpuLoad(cpuLoadAtual);
                setMemoryLoad(memoryLoadAtual);
            }
        }

    }

    public static void print(String txt) {
        System.out.println(txt);
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

    public void runWithNormals(Classifier classifier, String name) throws Exception {
        try {
            long inicioInicial = System.currentTimeMillis();
            long inicio = System.currentTimeMillis();
//            System.out.println(getDataTreined());
            classifier.buildClassifier(getDataTreined());
//            System.out.println("[Normal] Tempo de construção: " + ((long) (System.currentTimeMillis() - inicio)) + " ms");
            inicio = System.currentTimeMillis();
            long teste = System.currentTimeMillis();
            for (int i = 0; i < getExpectedNormals(); i++) {
                testInstanceAndRetroFeed(classifier, i, false, false);
            }
//            System.out.println("[Normal] Tempo de Classificação: " + (System.currentTimeMillis() - teste) + "ms");

            try {
//                System.out.println("[Normal] Velocidade de Testes: " + ((getExpectedNormals() / ((System.currentTimeMillis() - inicio) / 1000))) + " amostras/s");
            } catch (ArithmeticException e) {
//                System.out.println("[Normal] Velocidade de Testes: " + (getExpectedNormals() + "/" + ((System.currentTimeMillis() - inicio))) + " amostras/ms");

            }
            long time = (System.currentTimeMillis() - inicioInicial);
            setTempo(getTempo() + time);
//            exportResultsCSV(name, getTempo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runWithAttacks(Classifier classifier, String name) throws Exception {
        try {
            long inicioInicial = System.currentTimeMillis();
            long inicio = System.currentTimeMillis();
            classifier.buildClassifier(getDataTreined());
//            System.out.println("[Attack] Tempo de construção: " + ((long) (System.currentTimeMillis() - inicio)) + "ms");
            inicio = System.currentTimeMillis();
            long teste = System.currentTimeMillis();
            for (int i = 0; i < getExpectedAttacks(); i++) {
                testInstanceAndRetroFeed(classifier, i, true, false);
            }
//            System.out.println("[Attack] Tempo de Classificação: " + (System.currentTimeMillis() - teste) + "ms");

            try {
//                System.out.println("[Attack] Velocidade de Testes: " + (getExpectedAttacks() / ((System.currentTimeMillis() - inicio) / 1000)) + " amostras/s");
            } catch (ArithmeticException r) {
//                System.out.println("[Attack] Velocidade de Testes: " + getExpectedAttacks() + "/" + ((System.currentTimeMillis() - inicio)) + " amostras/ms");
            }
            long time = (System.currentTimeMillis() - inicioInicial);
            setTempo(getTempo() + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GenericResultado getResults(String name) {
//        System.out.println("TEMPO: " + getTempo());
        double acuracia = 0;
        double txDec = 0;
        double txAFal = 0;

        try {
            acuracia = Float.valueOf(((getVP() + getVN()) * 100) / (getVP() + getVN() + getFP() + getFN()));
            // Sensitividade ou Taxa de Detecção
            txDec = Float.valueOf((getVP() * 100) / (getVP() + getFN()));
            // Especificade ou Taxa de Alarmes Falsos    
            txAFal = Float.valueOf((getFP() * 100) / (getVN() + getFP()));

        } catch (java.lang.ArithmeticException e) {
            System.out.println("Divisão por zero ((" + getVP() + " + " + getVN() + ") * 100) / (" + getVP() + " + " + getVN() + "+ " + getFP() + " getFN())");
        }
        GenericResultado resultado = new GenericResultado(name, getVP(), getFN(), getVN(), getFP(), getTempo(), acuracia, txDec, txAFal, getCpuLoad(), getMemoryLoad());
        setVN(0);
        setFN(0);
        setVP(0);
        setFP(0);
        return resultado;
    }

    public float getVP() {
        return VP;
    }

    public void setVP(float VP) {
        this.VP = VP;
    }

    public float getFP() {
        return FP;
    }

    public void setFP(float FP) {
        this.FP = FP;
    }

    public float getVN() {
        return VN;
    }

    public void setVN(float VN) {
        this.VN = VN;
    }

    public float getFN() {
        return FN;
    }

    public void setFN(float FN) {
        this.FN = FN;
    }

    public String getAttackFolder() {
        return attackFolder;
    }

    public String getNormalsFile() {
        return normalsFile;
    }

    public String getAtaquesFile() {
        return ataquesFile;
    }

    public int getExpectedAttacks() {
        return expectedAttacks;
    }

    public int getExpectedNormals() {
        return expectedNormals;
    }

    public ArrayList<Classifier> getClassifiers() {
        return classifiers;
    }

    public String getTrainFile() {
        return trainFile;
    }

    public Instances getDataTreined() {
        return trainInstances;
    }

    public void setDataTreined(Instances dataTreined) {
        this.trainInstances = dataTreined;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isNormals() {
        return normals;
    }

    public void setNormals(boolean normals) {
        this.normals = normals;
    }

    public boolean isAttacks() {
        return attacks;
    }

    public void setAttacks(boolean attacks) {
        this.attacks = attacks;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public static Instances applyFilterDelete(Instances instances, int[] featuresToDelete) {
        long timeForFiltering = System.currentTimeMillis();
//        System.out.print("Deletadas Features: ");
        for (int i = featuresToDelete.length - 1; i >= 0; i--) {
            //    System.out.print(featuresToDelete[i] + ",");
            instances.deleteAttributeAt(featuresToDelete[i]);
        }
//        System.out.println("}");
//        timeForFiltering = System.currentTimeMillis() - timeForFiltering;
        return instances;
    }

    public static Instances applyFilterKeep(Instances instances, int[] featuresToKeep, int totalFeatures) {
        int deletadas = 0;
        for (int i = totalFeatures; i > 0; i--) {
            boolean deletar = true;
            for (int j : featuresToKeep) {
                if (i == j) {
                    deletar = false;
//                    System.out.println("Manter [" + i + "]");
                    break;
                }
            }
            if (deletar) {
//                System.out.println("Deletando " + i);
                instances.deleteAttributeAt(i - 1);
                deletadas = deletadas + 1;
                if (deletadas == (totalFeatures - featuresToKeep.length)) {
                    break;
                }
            }
        }
//        System.out.println("Instancia: " + instances.get(0).toString());
        return instances;
    }

    public int getFeatures() {
        return features;
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public double getMemoryLoad() {
        return memoryLoad;
    }

    public void setMemoryLoad(double memoryLoad) {
        this.memoryLoad = memoryLoad;
    }
}
