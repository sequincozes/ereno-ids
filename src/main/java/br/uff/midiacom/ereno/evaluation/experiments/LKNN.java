package br.uff.midiacom.ereno.evaluation.experiments;

import br.uff.midiacom.ereno.abstractclassification.GenericResultado;
import br.uff.midiacom.ereno.abstractclassification.Util;
import weka.core.Instance;
import weka.core.Instances;

import static br.uff.midiacom.ereno.abstractclassification.GeneralParameters.normalClass;

public class LKNN {
    private int k = 3;
    private Manhhatan[] knn;
    private Instances trainingInstances;
    int tp, tn, fn, fp;
    boolean printDebug = true;
    boolean printDebugMoveBack = true;
    int stopI = -20;
    private long trainingNanotime;

    private long testingNanotime;

    public static void main(String[] args) throws Exception {
        LKNN lknn = new LKNN();
//        lknn.train(Util.loadFile("/home/silvio/datasets/sbseg2022/5fModelo.csv"));
//        lknn.test(Util.loadFile("/home/silvio/datasets/sbseg2022/5fTeste_single.csv"));
//        lknn.test(Util.loadFile("/home/silvio/datasets/sbseg2022/5fTeste.csv"));
        lknn.train(Util.loadFile("/home/silvio/datasets/sbseg2022/5fModelo_debug.csv"));
        lknn.test(Util.loadFile("/home/silvio/datasets/sbseg2022/5fTeste_debug.csv"));
        GenericResultado results = lknn.getResults();
        results.printResults();

        lknn.printVizinhos();
    }

    private void printVizinhos() {

        try {
            System.out.println("Teste - " +
                    ((int) knn[0].t.value(0)) +
                    "," + ((int) knn[0].t.value(1)) +
                    "," + ((int) knn[0].t.value(2)) +
                    "," + ((int) knn[0].t.value(3)) +
                    "," + ((int) knn[0].t.value(4)) +
                    ",[6th: " + ((int) knn[0].a.value(5)) +
                    "]");
        } catch (NullPointerException e) {
        }

        for (Manhhatan kn : knn) {
            if (kn != null) {
                System.out.println("Vizinho - " +
                        ((int) kn.a.value(0)) +
                        "," + ((int) kn.a.value(1)) +
                        "," + ((int) kn.a.value(2)) +
                        "," + ((int) kn.a.value(3)) +
                        "," + ((int) kn.a.value(4)) +
                        ",[6th: " + ((int) kn.a.value(5)) +
                        "] - Vote: " + kn.getClasse() +
                        " (Distance: " + kn.getDistancia_t_a_somada() + ")");
            }
        }
    }


    private void test(Instances testing) {

        long beginTestingNanotime = System.nanoTime();
        testing.setClassIndex(testing.numAttributes() - 1);
        System.out.println("Amostra;Resultado;Esperado");
        int i = 0;

        for (Instance t : testing) {
            double resultado = test(t);
            double esperado = t.classValue();

            String r = "";

            if (resultado == esperado) {
                if (resultado == normalClass) {
                    tn = tn + 1;
                    r = "tn";
                } else {
                    tp = tp + 1;
                    r = "tp";
                }
            } else { // bad prediction
                if (resultado == normalClass) {
                    fn = fn + 1;
                    r = "fn";
                } else {
                    fp = fp + 1;
                    r = "fp";
                }
            }


            if (printDebug) {
                System.out.println(i++ + " | " + resultado + " | " + esperado + " - (" + r + ")");

                if (i - 1 == stopI) {
                    break;
                }
            }
        }

        long endTestingNanotime = System.nanoTime();
        this.testingNanotime = endTestingNanotime - beginTestingNanotime;
    }

    /* Test Multiple */
    public GenericResultado run(Instances training, Instances testing) {
        train(training, k);
        test(testing);
        return getResults();
    }

    public void train(String trainingDataset, int k) {
        long beginTrainingNanotime = System.nanoTime();

        try {
            this.trainingInstances = Util.loadFile(trainingDataset);
            this.knn = new Manhhatan[k];//new Instances(trainingInstances, k);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.k = k;
        long endTrainingNanotime = System.nanoTime();
        this.trainingNanotime = endTrainingNanotime - beginTrainingNanotime;
    }

    public void train(String trainingDataset) {
        long beginTrainingNanotime = System.nanoTime();

        try {
            this.trainingInstances = Util.loadFile(trainingDataset);
            this.knn = new Manhhatan[k];//new Instances(trainingInstances, k);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        long endTrainingNanotime = System.nanoTime();
        this.trainingNanotime = endTrainingNanotime - beginTrainingNanotime;
    }

    public void train(Instances trainingInstances) {
        long beginTrainingNanotime = System.nanoTime();
        this.knn = new Manhhatan[k];
        trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
        this.trainingInstances = trainingInstances;
        long endTrainingNanotime = System.nanoTime();
        this.trainingNanotime = endTrainingNanotime - beginTrainingNanotime;
    }

    public void train(Instances trainingInstances, int k) {
        long beginTrainingNanotime = System.nanoTime();
        this.k = k;
        this.knn = new Manhhatan[k];//new Instances(trainingInstances, k);
        trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
        this.trainingInstances = trainingInstances;
        long endTrainingNanotime = System.nanoTime();
        this.trainingNanotime = endTrainingNanotime - beginTrainingNanotime;
    }

    public double test(Instance t) {
        for (Instance a : trainingInstances) {
            Manhhatan peer = new Manhhatan(a, t);
            updateKNN(peer);
        }
        return computeClass();
    }

    private double computeClass() {
        double qtdNormal = 0;
        double qtdAttack = 0;
        int i = 1;
        for (Manhhatan k_neighbor : knn) {
//            System.out.println("KNN class: [" + i++ + "] = " + k_neighbor.getClasse() + " / Distance: " + k_neighbor.distancia_t_a_somada);
            if (k_neighbor.getClasse() < 1) {
                qtdAttack = qtdAttack + 1;
            } else {
                qtdNormal = qtdNormal + 1;
            }
        }

//        System.out.println("qtdNormal: " + qtdNormal + "/ atk: " + qtdAttack);
        if (qtdNormal > qtdAttack) {
            return 1;
        } else {
            return 0;
        }
    }

    private void updateKNN(Manhhatan peer) {
//        for (int i = 0; i < k; i++) {
        for (int i = k - 1; i > 0; i--) {
            Manhhatan k_neighbor = knn[i];
            if (k_neighbor == null) {
                knn[i] = peer;
                System.out.println("Inicializou o knn[" + i + "] nulo com " + knn[i].a.value(0));
                break;
            } else if (peer.getDistancia_t_a_somada() < k_neighbor.getDistancia_t_a_somada()) {
                System.out.println("Vai movimentar o knn[" + i + "], pos a distância do par é menor");
//                System.out.println("Substiuição: K[" + i + "]: "
//                        + (int) peer.a.value(0) + "," +
//                        (int) peer.a.value(1) + "," +
//                        (int) peer.a.value(2) + "," +
//                        (int) peer.a.value(3) + "," +
//                        (int) peer.a.value(4) +
//                        " (d: " + peer.getDistancia_t_a_somada() + ")" + " substituiu " +
//                        (int) k_neighbor.a.value(0) +
//                        (int) k_neighbor.a.value(1) + "," +
//                        (int) k_neighbor.a.value(2) + "," +
//                        (int) k_neighbor.a.value(3) + "," +
//                        (int) k_neighbor.a.value(4) +
//                        " (d: " + k_neighbor.getDistancia_t_a_somada() + ")");
                moveBack(peer);
            }
        }
    }

    // Começa do fim para o início, se tiver nulo, põe lá
    private void moveBack(Manhhatan peer) {
        for (int i = k - 1; i > 0; i--) {
            // Se a posição estiver nula, preenche com o valor
            if (knn[i] == null) {
                knn[i] = peer;
                System.out.println("O valor foi para knn[" + (i) + "], que estava nulo.");
                break;
            }

            // Verifica se a posição é menor que o


        }
    }

    private void moveBack(int k, Manhhatan peer) {
        if (k > 0) {
            if (knn[k] == null) {
                knn[k] = new Manhhatan(peer);
                System.out.println("-----VIZINHOS k=" + k + " (inicialização) ------");
                printVizinhos();
            } else {
                knn[k] = new Manhhatan(peer);
                knn[k - 1] = new Manhhatan(knn[k]);
                System.out.println("-----VIZINHOS k=" + k + " (depois do moveBack) ------");
                printVizinhos();
                moveBack(k - 1, knn[k - 1]);
            }
            if (printDebugMoveBack) {
                System.out.println(" -- K[" + (k - 1) + "]: "
                        + (int) knn[k - 1].a.value(0) + "," +
                        (int) knn[k - 1].a.value(1) + "," +
                        (int) knn[k - 1].a.value(2) + "," +
                        (int) knn[k - 1].a.value(3) + "," +
                        (int) knn[k - 1].a.value(4) +
                        " (d: " + knn[k - 1].getDistancia_t_a_somada() + ")" + " substituiu " +
                        (int) knn[k].a.value(0) +
                        (int) knn[k].a.value(1) + "," +
                        (int) knn[k].a.value(2) + "," +
                        (int) knn[k].a.value(3) + "," +
                        (int) knn[k].a.value(4) +
                        " (d: " + knn[k].getDistancia_t_a_somada() + ")");
            }

        } else {
            knn[k] = new Manhhatan(peer);
            System.out.println("-----VIZINHOS (k = 0) ------");
            printVizinhos();
            System.out.println("--------------------");
        }
    }

    private class Manhhatan {
        private Instance a;
        private Instance t;


        private double classe;
        private double distancia_t_a_somada;

        public Manhhatan(Instance a, Instance t) {
            this.a = a;
            this.t = t;
            this.classe = new Integer((int) a.classValue());
//            System.out.println("Valor de classe = "+classe + " / porque classValue: "+a.classValue() + "sexto atributo: "+a.value(5));
            for (int i = 0; i < a.numAttributes() - 1; i++) {
                double distancia_t_a = a.value(i) - t.value(i);
                if (distancia_t_a < 0) {
                    distancia_t_a = distancia_t_a * -1;
                }
                distancia_t_a_somada = distancia_t_a_somada + distancia_t_a;
            }

        }

        public Manhhatan(Manhhatan manhhatan) {
            this.distancia_t_a_somada = manhhatan.getDistancia_t_a_somada();
            this.a = manhhatan.getA();
            this.classe = manhhatan.getClasse();
            this.t = manhhatan.getT();
        }

        public double getClasse() {
            return this.classe;
        }

        public double getDistancia_t_a_somada() {
            return distancia_t_a_somada;
        }

        public Instance getA() {
            return a;
        }

        public void setA(Instance a) {
            this.a = a;
        }

        public Instance getT() {
            return t;
        }

        public void setT(Instance t) {
            this.t = t;
        }

        public void setClasse(double classe) {
            this.classe = classe;
        }

        public void setDistancia_t_a_somada(double distancia_t_a_somada) {
            this.distancia_t_a_somada = distancia_t_a_somada;
        }

    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Manhhatan[] getKnn() {
        return knn;
    }

    public void setKnn(Manhhatan[] knn) {
        this.knn = knn;
    }

    public Instances getTrainingInstances() {
        return trainingInstances;
    }

    public void setTrainingInstances(Instances trainingInstances) {
        this.trainingInstances = trainingInstances;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public int getTn() {
        return tn;
    }

    public void setTn(int tn) {
        this.tn = tn;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public int getFp() {
        return fp;
    }

    public void setFp(int fp) {
        this.fp = fp;
    }

    public GenericResultado getResults() {
        int tp = getTp();
        int tn = getTn();
        int fp = getFp();
        int fn = getFn();
        float microtime = this.testingNanotime / 1000;
        return new GenericResultado("L-KNN", tp, fn, tn, fp, microtime);
    }

    private long testingNanotime() {
        return this.testingNanotime;
    }

    private long getTrainingNanotime() {
        return this.trainingNanotime;
    }


//    public Instances removeRedundances(Instances instances) {
//        for eac
//
//    }

}
