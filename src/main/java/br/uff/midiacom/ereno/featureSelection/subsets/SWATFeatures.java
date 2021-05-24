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
    public static final int[] SWAT_FULL = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    public static final int[] SWAT_GR = {34, 43, 31, 5, 14};
    public static final int[] SWAT_IG = {6, 27, 35, 41, 45};
    public static final int[] SWAT_OneR = {41, 29, 39, 40, 19};

    public static final int[] IWSSr_RandomTree = {36, 35, 33, 31, 15, 8, 6, 5, 3};
    public static final int[] IWSSr_J48 = {38, 37, 35, 29, 27, 26, 25, 23, 7, 6, 4};
    public static final int[] IWSSr_REPTree = {37, 35, 27, 21, 20, 9, 8, 6, 5, 4, 3};
    public static final int[] IWSSr_NaiveBayes = {42, 39, 5};
    public static final int[] IWSSr_RandomForest = {48, 38, 37, 35, 27, 25, 15, 10, 8, 7, 6, 5, 4, 3, 2, 1};

    public static final int[] GR_G_BF_RandomTree = {19, 39, 25, 4, 6};
    public static final int[] GR_G_BF_J48 = {19, 39, 13, 40, 29};
    public static final int[] GR_G_BF_REPTree = {19, 39, 23, 4, 6};
    public static final int[] GR_G_BF_NaiveBayes = {19, 39, 41, 40, 43};
    public static final int[] GR_G_BF_RandomForest = {19, 39, 13, 4, 29};
    public static final int[][] GR_G_BF = {GR_G_BF_RandomTree, GR_G_BF_J48, GR_G_BF_REPTree, GR_G_BF_NaiveBayes, GR_G_BF_RandomForest};

    public static final int[] GR_G_VND_RandomTree = {29, 13, 5, 21, 6, 46, 16, 25};
    public static final int[] GR_G_VND_J48 = {29, 4, 19, 5, 25, 21, 6, 39, 10, 13};
    public static final int[] GR_G_VND_REPTree = {4, 10, 19, 39, 6, 13, 29, 21, 25, 23, 5};
    public static final int[] GR_G_VND_NaiveBayes = {4, 46, 25, 42, 23};
    public static final int[] GR_G_VND_RandomForest = {4, 19, 39, 46, 6, 29, 21, 25};
    public static final int[][] GR_G_VND = {GR_G_VND_RandomTree, GR_G_VND_J48, GR_G_VND_REPTree, GR_G_VND_NaiveBayes, GR_G_VND_RandomForest};

    public static final int[] GR_G_RVND_RandomTree = {29, 13, 5, 21, 6, 46, 16, 25};
    public static final int[] GR_G_RVND_J48 = {29, 4, 19, 5, 25, 21, 6, 39, 10, 13, 14};
    public static final int[] GR_G_RVND_REPTree = {4, 10, 19, 39, 6, 13, 29, 21, 25, 23, 5};
    public static final int[] GR_G_RVND_NaiveBayes = {4, 46, 25, 42, 23};
    public static final int[] GR_G_RVND_RandomForest = {4, 19, 39, 46, 6, 29, 25};
    public static final int[][] GR_G_RVND = {GR_G_RVND_RandomTree, GR_G_RVND_J48, GR_G_RVND_REPTree, GR_G_RVND_NaiveBayes, GR_G_RVND_RandomForest};


    public static final int[] F_G_VND_RandomTree = {5, 6, 15, 22, 26, 35, 37, 7, 3, 25, 21};
    public static final int[] F_G_VND_J48 = {7, 38, 37, 35, 29, 26, 23, 10, 6, 5, 1};
    public static final int[] F_G_VND_REPTree = {3, 7, 38, 37, 35, 29, 25, 21, 15, 10, 6, 4};
    public static final int[] F_G_VND_NaiveBayes = {34, 29, 6, 5, 1};
    public static final int[] F_G_VND_RandomForest = {20, 3, 7, 27, 48, 47, 46, 43};
    public static final int[][] F_G_VND = {F_G_VND_RandomTree, F_G_VND_J48, F_G_VND_REPTree, F_G_VND_NaiveBayes, F_G_VND_RandomForest};

    public static final int[] F_G_RVND_RandomTree = {5, 6, 15, 22, 26, 35, 37, 7, 3, 25, 21};
    public static final int[] F_G_RVND_J48 = {7, 38, 37, 35, 29, 26, 23, 10, 6, 5, 1};
    public static final int[] F_G_RVND_REPTree = {3, 7, 38, 37, 35, 29, 25, 21, 15, 10, 6, 4};
    public static final int[] F_G_RVND_NaiveBayes = {34, 29, 6, 5, 1};
    public static final int[] F_G_RVND_RandomForest = {20, 3, 7, 27, 33, 50, 48, 47, 46, 44, 39, 38, 37, 36, 35, 34, 32, 29, 26, 25, 23, 21, 19, 13, 9, 6, 5, 4};
    public static final int[][] F_G_RVND = {F_G_RVND_RandomTree, F_G_RVND_J48, F_G_RVND_REPTree, F_G_RVND_NaiveBayes, F_G_RVND_RandomForest};

    public static final int[] I_G_VND_RandomTree = {36, 35, 3, 15, 8, 6, 5};
    public static final int[] I_G_VND_J48 = {25, 37, 29, 7, 6, 4, 23, 26, 27, 35, 38};
    public static final int[] I_G_VND_REPTree = {8, 35, 21, 3, 6, 9, 37};
    public static final int[] I_G_VND_NaiveBayes = {42, 39, 5};
    public static final int[] I_G_VND_RandomForest = {37, 38, 5, 3, 6, 7, 8, 15, 25, 27, 35};
    public static final int[][] I_G_VND = {I_G_VND_RandomTree, I_G_VND_J48, I_G_VND_REPTree, I_G_VND_NaiveBayes, I_G_VND_RandomForest};


    public static final int[] RCL_GR = {34, 43, 31, 5, 14, 16, 23, 25, 21, 40, 29, 13, 4, 10, 41, 6, 42, 46, 39, 19};
    public static final int[] RCL_IG = {6, 27, 35, 41, 45, 47, 19, 36, 39, 46, 29, 40, 28, 42, 34, 43, 17, 31, 8, 2};
    public static final int[] IG_G_BF_RandomTree = {8, 34, 36, 46, 35};
    public static final int[] IG_G_BF_J48 = {2, 31, 19, 46, 6};
    public static final int[] IG_G_BF_REPTree = {2, 31, 19, 46, 6};
    public static final int[] IG_G_BF_NaiveBayes = {42, 28, 47, 17, 34};
    public static final int[] IG_G_BF_RandomForest = {8, 34, 36, 46, 35};
    public static final int[] IG_G_VND_RandomTree = {6, 27, 35, 8, 43};
    public static final int[] IG_G_VND_J48 = {2, 6, 35, 19, 29, 17, 43};
    public static final int[] IG_G_VND_REPTree = {19, 17, 6, 35, 29, 8, 39, 28};
    public static final int[] IG_G_VND_NaiveBayes = {46, 6};
    public static final int[] IG_G_VND_RandomForest = {17, 47, 46, 8, 29, 39, 36};
    public static final int[] IG_G_RVND_RandomTree = {6, 27, 35, 8, 43};
    public static final int[] IG_G_RVND_J48 = {2, 6, 35, 19, 29, 17, 43};
    public static final int[] IG_G_RVND_REPTree = {19, 17, 6, 35, 29, 8, 39, 28};
    public static final int[] IG_G_RVND_NaiveBayes = {46, 6};
    public static final int[] IG_G_RVND_RandomForest = {17, 47, 46, 2, 8, 29, 3};

    public SWATFeatures() {
        super(RCL_FULL, RCL_GR, RCL_I);
    }
}
