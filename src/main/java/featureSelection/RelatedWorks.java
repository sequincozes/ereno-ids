package featureSelection;

import java.net.URLClassLoader;
import AbstractClassification.ClassifierExtended;
import legacy.sv2020_consistency.BemSimples;
import legacy.sv2020_consistency.Parameters;
import legacy.sv2020_consistency.Util;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.REPTree;
import weka.core.Instance;
import weka.core.Instances;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author silvio
 */
public class RelatedWorks extends BemSimples {

    public static void main(String[] args) throws Exception {
        com.intel.daal.algorithms.classifier.training.TrainingInput input;
        com.intel.daal.algorithms.gbt.classification.training.TrainingBatch bath;
        com.intel.daal.algorithms.gbt.classification.training.TrainingResult res;
        com.intel.daal.algorithms.gbt.classification.training.Parameter par;
        com.intel.daal.algorithms.gbt.training.SplitMethod s;
        
        
    }

    public static void adaboost() throws Exception {
        //Runtime.getRuntime().exec("export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64;");
        AdaBoostM1 classifier = new AdaBoostM1();
        String optionString = "-P 100 -S 2 -I 10 -W weka.classifiers.trees.REPTree"; //decision stumps as weak classifier
        //String optionString2 = "-W weka.classifiers.trees.REPTree"; //decision stumps as weak classifier
        classifier.setOptions(weka.core.Utils.splitOptions(optionString));
        classifier.setClassifier(new REPTree());
        ClassifierExtended AdaBoostM1Extended = new ClassifierExtended(true, classifier, "AdaBoostM1");
        Parameters.CLASSIFIERS_FOREACH = new ClassifierExtended[1];
        Parameters.CLASSIFIERS_FOREACH[0] = AdaBoostM1Extended;
        run("uc01_fullgoose_mini");
        System.out.println(classifier.getCapabilities());
        System.out.println(classifier.toString());
        System.out.println(classifier.getCapabilities().getAttributeCapabilities());
        System.out.println(classifier.getCapabilities().getAttributeCapabilities());

    }
}
