/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.subsets;

/**
 * @author silvio
 */
public class SWATFeatures extends FeatureSubsets {

    public SWATFeatures() {
        super(SWAT_RCL_FULL, SWAT_RCL_GR, SWAT_RCL_I);
    }

    // Standard FeatureSubsets RCLSs
    public static final int[] SWAT_RCL_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    public static final int[] SWAT_RCL_GR = new int[]{ /*substituir pelo RCL do GR */};

    /* IWSSR RCL*/
    public static final int[] RCL_IWSSR_NaiveBayes = new int[]{};
    public static final int[] RCL_IWSSR_RandomTree = new int[]{};
    public static final int[] RCL_IWSSR_J48 = new int[]{};
    public static final int[] RCL_IWSSR_RandomForest = new int[]{};
    public static final int[] RCL_IWSSR_RepTree = new int[]{};

    public static final int[][] SWAT_RCL_I = new int[][]{ /*substituir pelo RCL do GR */};


}
