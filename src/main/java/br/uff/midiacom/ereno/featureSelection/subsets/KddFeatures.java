/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.subsets;

/**
 * @author silvio
 */
public class KddFeatures extends FeatureSubsets {

    public KddFeatures() {
        super(KDD_RCL_FULL, KDD_RCL_GR, KDD_RCL_I);
    }

    public static final int[] RCL_IWSSR_NaiveBayes = new int[]{3, 4, 8, 9, 11, 13, 14, 15, 18, 20, 21, 41};
    public static final int[] RCL_IWSSR_RandomTree = new int[]{3, 4, 5, 6, 12, 14, 17, 22, 23, 26, 27, 29, 30, 40, 41};
    public static final int[] RCL_IWSSR_J48 = new int[]{1, 3, 4, 5, 6, 7, 12, 13, 15, 17, 19, 20, 24, 25, 27, 29, 30, 31, 32, 33, 41};
    public static final int[] RCL_IWSSR_RandomForest = new int[]{1, 3, 4, 5, 6, 12, 16, 23, 30, 34, 37, 38, 39, 40, 41};
    public static final int[] RCL_IWSSR_RepTree = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 12, 16, 17, 18, 19, 20, 21, 24, 25, 26, 27, 30, 32, 34, 37, 40, 41};
    public static final int[] RCL_IWSSR[] = {RCL_IWSSR_RandomTree, RCL_IWSSR_J48, RCL_IWSSR_RepTree, RCL_IWSSR_NaiveBayes, RCL_IWSSR_RandomForest};

    public static final int[] IWSS_VND_NaiveBayes = new int[]{21, 13, 8, 4, 3};
    public static final int[] IWSS_VND_RandomTree = new int[]{41, 40, 30, 26, 23, 12, 6, 5, 4, 3};
    public static final int[] IWSS_VND_J48 = new int[]{17, 1, 3, 5, 6, 12, 13, 19, 24, 25, 27, 29, 30, 31, 32, 33, 41, 4, 15};
    public static final int[] IWSS_VND_RandomForest = new int[]{34, 1, 3, 4, 5, 6, 12, 23, 30, 37, 38, 39, 40, 16};
    public static final int[] IWSS_VND_RepTree = new int[]{40, 1, 3, 4, 5, 6, 12, 17, 18, 19, 24, 25, 27, 30, 32, 34, 37, 16};
    public static final int[][] I_G_VND = {IWSS_VND_RandomTree, IWSS_VND_J48, IWSS_VND_RepTree, IWSS_VND_NaiveBayes, IWSS_VND_RandomForest};

    //Every GR means IG. Changing to make a standard.
    public static final int[] IG_GRASP_RandomTree = new int[]{26, 6, 23, 17, 3};
    public static final int[] IG_GRASP_J48 = new int[]{19, 32, 12, 5, 3};
    public static final int[] IG_GRASP_RepTree = new int[]{20, 3, 30, 7, 5};
    public static final int[] IG_GRASP_NaiveBayes = new int[]{14, 3, 21, 11, 4};
    public static final int[] IG_GRASP_RandomForest = new int[]{16, 40, 3, 34, 5};
    public static final int[] IG_GRASP[] = {IG_GRASP_RandomTree, IG_GRASP_J48, IG_GRASP_RepTree, IG_GRASP_NaiveBayes, IG_GRASP_RandomForest};

    public static final int[] IG_VND_NaiveBayes = new int[]{21, 13, 8, 4, 3};
    public static final int[] IG_VND_RandomTree = new int[]{41, 40, 30, 26, 23, 12, 6, 5, 4, 3};
    public static final int[] IG_VND_J48 = new int[]{17, 1, 3, 5, 6, 12, 13, 19, 24, 25, 27, 29, 30, 31, 32, 33, 41, 4, 15};
    public static final int[] IG_VND_RandomForest = new int[]{34, 1, 3, 4, 5, 6, 12, 23, 30, 37, 38, 39, 40, 16};
    public static final int[] IG_VND_RepTree = new int[]{40, 1, 3, 4, 5, 6, 12, 17, 18, 19, 24, 25, 27, 30, 32, 34, 37, 16};
    public static final int[] IG_VND[] = {IG_VND_RandomTree, IG_VND_J48, IG_VND_RepTree, IG_VND_NaiveBayes, IG_VND_RandomForest};

    public static final int[] IG_RVND_NaiveBayes = new int[]{21, 13, 8, 4, 3};
    public static final int[] IG_RVND_RandomTree = new int[]{41, 40, 30, 26, 23, 12, 6, 5, 4, 3};
    public static final int[] IG_RVND_J48 = new int[]{17, 1, 3, 5, 6, 12, 13, 19, 24, 25, 27, 29, 30, 31, 32, 33, 41, 4, 15};
    public static final int[] IG_RVND_RandomForest = new int[]{1, 3, 4, 5, 6, 12, 23, 30, 37, 38, 40, 41};
    public static final int[] IG_RVND_RepTree = new int[]{40, 1, 3, 4, 5, 6, 12, 17, 18, 19, 24, 25, 27, 30, 32, 34, 37, 16};
    public static final int[][] IG_RVND = {IG_RVND_RandomTree, IG_RVND_J48, IG_RVND_RepTree, IG_RVND_NaiveBayes, IG_RVND_RandomForest};

    public static final int[] FULL_RCL_VND_RandomTree = new int[]{41, 40, 38, 37, 35, 34, 31, 22, 13, 12, 6, 5, 4, 3, 2, 1};
    public static final int[] FULL_RCL_VND_J48 = new int[]{41, 37, 36, 35, 33, 26, 24, 23, 22, 12, 10, 7, 6, 5, 4, 3, 2};
    public static final int[] FULL_RCL_VND_RepTree = new int[]{8, 41, 40, 38, 36, 35, 26, 24, 23, 12, 11, 10, 6, 5, 3, 2, 1};
    public static final int[] FULL_RCL_VND_NaiveBayes = new int[]{8, 39, 37, 36, 27, 24, 17, 16, 12, 3};
    public static final int[] FULL_RCL_VND_RandomForest = new int[]{8, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28};
    public static final int[] FULL_RCL_VND[] = {FULL_RCL_VND_RandomTree, FULL_RCL_VND_J48, FULL_RCL_VND_RepTree, FULL_RCL_VND_NaiveBayes, FULL_RCL_VND_RandomForest};

    public static final int[] FULL_RCL_RVND_NaiveBayes = new int[]{41, 40, 38, 37, 35, 34, 31, 22, 13, 12, 6, 5, 4, 3, 2, 1};
    public static final int[] FULL_RCL_RVND_RandomTree = new int[]{8, 37, 36, 35, 33, 26, 24, 23, 22, 12, 10, 7, 6, 5, 4, 3, 2};
    public static final int[] FULL_RCL_RVND_J48 = new int[]{8, 41, 40, 38, 36, 35, 26, 24, 23, 12, 11, 10, 6, 5, 3, 2, 1};
    public static final int[] FULL_RCL_RVND_RandomForest = new int[]{8, 39, 36, 27, 24, 17, 16, 12, 3, 2};
    public static final int[] FULL_RCL_RVND_RepTree = new int[]{8, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 26, 24, 22, 18, 17, 13, 12, 10, 6, 5, 4, 3, 1};
    public static final int[] FULL_RCL_RVND[] = {FULL_RCL_RVND_RandomTree, FULL_RCL_RVND_J48, FULL_RCL_RVND_RepTree, FULL_RCL_RVND_NaiveBayes, FULL_RCL_RVND_RandomForest};

    public static final int[] IG5 = new int[]{5, 6, 3, 4, 30};
    public static final int[] GR5 = new int[]{12, 26, 4, 25, 39};
    public static final int[] OneR5 = new int[]{5, 6, 3, 4, 30};

    // Standard FeatureSubsets RCLSs
    public static final int[] KDD_RCL_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41};
    public static final int[] KDD_RCL_GR = new int[]{5, 6, 3, 4, 30, 29, 33, 34, 35, 12, 23, 38, 25, 39, 26, 37, 32, 36, 31, 41};
    public static final int[][] KDD_RCL_I = RCL_IWSSR;
}
