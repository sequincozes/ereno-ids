/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.outputManager;

import br.uff.midiacom.ereno.abstractclassification.GeneralParameters;
import static br.uff.midiacom.ereno.abstractclassification.GeneralParameters.OUTPUT;
import br.uff.midiacom.ereno.featureSelection.grasp.Grasp;
import br.uff.midiacom.ereno.outputManager.model.Detail;
import br.uff.midiacom.ereno.outputManager.model.Iteration;
import br.uff.midiacom.ereno.outputManager.model.Error;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silvio
 */
public class LocalOutput implements OutputManager {

    String experimentIdentifier;
    private String experimentName;
    private String graspMethod;

    public void writeDetails(String mensagem) throws IOException {
        FileWriter arq;

        arq = new FileWriter(getOutputDetailsLocation() + ".txt", true);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.append(mensagem + "\r\n");
        arq.close();

    }

    public void writeHeaders() throws IOException {
        FileWriter arq;
        arq = new FileWriter(getOutputIterationsLocation() + ".txt", true);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.append(graspMethod + "	" + "acuracia" + "	" + "f1score" + "	" + "PRECISION" + "	" + "recall" + "	" + "selecao" + "\r\n");
        arq.close();

    }

    public void writeIteration(int iteracao, double acuracia, double f1score, double precision, double recall, String selecao, String time) {
        try {
            FileWriter arq;
            arq = new FileWriter(getOutputIterationsLocation() + ".txt", true);
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.append(graspMethod + "(" + iteracao + ")"
                    + "	" + acuracia
                    + "	" + f1score
                    + "	" + precision
                    + "	" + recall
                    + "	" + selecao
                    + "	" + time
                    + "	" + "\r\n");
            arq.close();
        } catch (IOException ex) {
            Logger.getLogger(Grasp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getOutputDetailsLocation() throws IOException {
        String detDir = "/details/";
        Runtime.getRuntime().exec("mkdir " + OUTPUT);
        Runtime.getRuntime().exec("mkdir " + OUTPUT + graspMethod);
        Runtime.getRuntime().exec("mkdir " + OUTPUT + graspMethod + detDir);
        String fullDir = OUTPUT + graspMethod + detDir + experimentIdentifier;
        return fullDir;
    }

    public String getOutputIterationsLocation() throws IOException {
        String itDir = "/iterations/";
        Runtime.getRuntime().exec("mkdir " + OUTPUT);
        Runtime.getRuntime().exec("mkdir " + OUTPUT + graspMethod);
        Runtime.getRuntime().exec("mkdir " + OUTPUT + graspMethod + itDir);
        return OUTPUT + graspMethod + itDir + experimentIdentifier;
    }

    @Override
    public void writeError(Error error) {
        System.out.println(error.message);
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        FileWriter arq;
        try {
            arq = new FileWriter("outputs/" + pid + "_error" + ".txt", true);

            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.append(error.message + "\r\n");
            arq.close();
        } catch (IOException ex) {
            Logger.getLogger(LocalOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void writeDetail(Detail detail) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeIteration(Iteration iteration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutputManager initialize(String graspMethod) {
        this.graspMethod = graspMethod;
        this.experimentName = GeneralParameters.SINGLE_CLASSIFIER_MODE.getClassifierName() + "_" + ManagementFactory.getRuntimeMXBean().getName();
        try {
            Runtime.getRuntime().exec("mkdir " + OUTPUT);
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp/details");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp/iterations");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp_vnd");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp_vnd/details");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp_vnd/iterations");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp_rvnd");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp_rvnd/details");
            Runtime.getRuntime().exec("mkdir " + OUTPUT + "grasp_rvnd/iterations");
        } catch (IOException ex) {
            Logger.getLogger(LocalOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    @Override
    public void writeBeginTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
