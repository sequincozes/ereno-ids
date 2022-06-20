package br.uff.midiacom.ereno.featureSelection.grasp.externalClassifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExternalClassifier {
    public static void main(String[] args) throws IOException {
        String features = "1,5";
        System.out.println(externalEvaluation(features));
    }

    public static Float externalEvaluation(String intFeatures) throws IOException {

//        if ("a".contains("a")) {
//            float fakeF1Score = new Random(System.currentTimeMillis()).nextInt(90);
//            System.out.println(fakeF1Score);
//            return fakeF1Score+10;
//        }
        String literalFeatures = intFeatures
                .replace("1", "duration")
                .replace("2", "protocol_type")
                .replace("3", "service")
                .replace("4", "src_bytes")
                .replace("5", "dst_bytes")
                .replace("6", "land")
                .replace("7", "wrong_fragment")
                .replace("8", "count")
                .replace("[", "")
                .replace("]", "")
                .replace(", ", ",");


        Runtime rt = Runtime.getRuntime();
        String command = "./run_treino_teste normalizados/TreinoNorm0100.csv normalizados/TesteNorm0100.csv " + literalFeatures;
//        String command = "/sysroot/home/silvio/IdeaProjects/ereno/LightKNN/run_treino_teste /sysroot/home/silvio/IdeaProjects/ereno/LightKNN/normalizados/TreinoNorm0100.csv /sysroot/home/silvio/IdeaProjects/ereno/LightKNN/normalizados/TesteNorm0100.csv " + literalFeatures;
        Process proc = rt.exec(command);

//        Process proc = rt.exec("./run_treino_teste normalizados/TreinoNorm0100.csv normalizados/TesteNorm0100.csv protocol_type,flag");
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        String s = null;
        boolean printing = false;
        while ((s = stdInput.readLine()) != null) {
            if (s.contains("Resultados para teste")) {
                printing = true;
            }
            if (printing) {
                if (s.contains("F1Score")) {
                    String f1Str = s.split(" ")[1].replace(",", "");
                    return Float.valueOf(f1Str);
                }
            }
        }

        // Read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            System.err.println(s);
        }
        System.err.println("command: " + command);
        return null;
    }

    public static Float internallKNNEvaluation(String intFeatures) throws IOException {

//        if ("a".contains("a")) {
//            float fakeF1Score = new Random(System.currentTimeMillis()).nextInt(90);
//            System.out.println(fakeF1Score);
//            return fakeF1Score+10;
//        }
        String literalFeatures = intFeatures
                .replace("1", "duration")
                .replace("2", "protocol_type")
                .replace("3", "service")
                .replace("4", "src_bytes")
                .replace("5", "dst_bytes")
                .replace("6", "land")
                .replace("7", "wrong_fragment")
                .replace("8", "count")
                .replace("[", "")
                .replace("]", "")
                .replace(", ", ",");


        Runtime rt = Runtime.getRuntime();
        String command = "./run_treino_teste normalizados/TreinoNorm0100.csv normalizados/TesteNorm0100.csv " + literalFeatures;
//        String command = "/sysroot/home/silvio/IdeaProjects/ereno/LightKNN/run_treino_teste /sysroot/home/silvio/IdeaProjects/ereno/LightKNN/normalizados/TreinoNorm0100.csv /sysroot/home/silvio/IdeaProjects/ereno/LightKNN/normalizados/TesteNorm0100.csv " + literalFeatures;
        Process proc = rt.exec(command);

//        Process proc = rt.exec("./run_treino_teste normalizados/TreinoNorm0100.csv normalizados/TesteNorm0100.csv protocol_type,flag");
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        String s = null;
        boolean printing = false;
        while ((s = stdInput.readLine()) != null) {
            if (s.contains("Resultados para teste")) {
                printing = true;
            }
            if (printing) {
                if (s.contains("F1Score")) {
                    String f1Str = s.split(" ")[1].replace(",", "");
                    return Float.valueOf(f1Str);
                }
            }
        }

        // Read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            System.err.println(s);
        }
        System.err.println("command: " + command);
        return null;
    }
}
