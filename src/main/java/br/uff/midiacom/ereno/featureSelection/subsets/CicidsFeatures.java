/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.subsets;

/**
 *
 * @author silvio
 */
public class CicidsFeatures {

    public static final int[] RCL_IG = new int[]{28, 36, 73, 43, 42, 8, 7, 13, 55, 14, 39, 11, 6, 66, 76, 40, 67, 68, 70, 1};

    public static final int[] FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78};
    public static final int[][] FULLs = {FULL, FULL, FULL, FULL, FULL};

    public static final int[] GRASP_VND_FULL_RCL_F1_RandomTree = new int[]{1, 40, 56, 65, 67, 68, 11, 51};
    public static final int[] GRASP_VND_FULL_RCL_F1_J48 = new int[]{14, 1, 25, 37, 64, 67, 68, 11, 40, 7, 5, 2};
    public static final int[] GRASP_VND_FULL_RCL_F1_REPTree = new int[]{1, 10, 25, 47, 49, 67, 68, 78, 66, 77, 71, 8};
    public static final int[] GRASP_VND_FULL_RCL_F1_NaiveBayes = new int[]{31, 1, 2, 7, 23, 26, 39, 45, 68, 74, 44, 9, 14, 20, 24, 52, 55, 67, 42};
    public static final int[] GRASP_VND_FULL_RCL_F1_RandomForest = new int[]{11, 78, 77, 76, 74, 70, 69, 68, 67, 66, 64, 63, 54, 53, 51, 49, 25, 7, 1};
    public static final int[] GRASP_VND_FULL_RCL_F1[] = {GRASP_VND_FULL_RCL_F1_RandomTree, GRASP_VND_FULL_RCL_F1_J48, GRASP_VND_FULL_RCL_F1_REPTree, GRASP_VND_FULL_RCL_F1_NaiveBayes, GRASP_VND_FULL_RCL_F1_RandomForest};

    public static final int[] GRASP_VND_IWSSR_RandomTree = new int[]{68, 78, 67, 56, 49, 43, 1};
    public static final int[] GRASP_VND_IWSSR_J48 = new int[]{49, 1, 6, 10, 16, 25, 39, 67, 68, 75, 47};
    public static final int[] GRASP_VND_IWSSR_NaiveBayes = new int[]{28, 31, 26, 24, 23, 8, 1};
    public static final int[] GRASP_VND_IWSSR_RandomForest = new int[]{25, 78, 68, 67, 56, 52, 11, 7, 6, 1};
    public static final int[] GRASP_VND_IWSSR_RepTree = new int[]{1, 10, 25, 49, 66, 67, 68, 78, 71};
    public static final int[][] I_G_VND = {GRASP_VND_IWSSR_RandomTree, GRASP_VND_IWSSR_J48, GRASP_VND_IWSSR_RepTree, GRASP_VND_IWSSR_NaiveBayes, GRASP_VND_IWSSR_RandomForest};

    public static final int[] GRASP_VND5_IG_RandomTree = new int[]{36, 76, 67, 43, 68};
    public static final int[] GRASP_VND5_IG_J48 = new int[]{73, 67, 1, 43, 68};
    public static final int[] GRASP_VND5_IG_NaiveBayes = new int[]{1, 7, 40, 66, 67};
    public static final int[] GRASP_VND5_IG_RandomForest = new int[]{76, 1, 67, 70, 40};
    public static final int[] GRASP_VND5_IG_RepTree = new int[]{1, 67, 73, 36, 40};

    public static final int[] GRASP_RVND5_IG_RandomTree = new int[]{8, 14, 6, 67, 14};
    public static final int[] GRASP_RVND5_IG_J48 = new int[]{14, 8, 6, 66, 67};
    public static final int[] GRASP_RVND5_IG_NaiveBayes = new int[]{1, 7, 13, 67, 36};
    public static final int[] GRASP_RVND5_IG_RandomForest = new int[]{8, 14, 6, 67, 14};
    public static final int[] GRASP_RVND5_IG_RepTree = new int[]{14, 8, 6, 66, 6};

    public static final int[] GRASP_VND_IG_RandomTree = new int[]{40, 1, 67, 68, 36, 63, 62, 49, 4};
    public static final int[] GRASP_VND_IG_J48 = new int[]{8, 66, 77, 15, 1, 25, 31, 35, 49, 64, 67, 68, 70, 78, 56, 38, 54, 53, 43, 39, 13, 7, 3};
    public static final int[] GRASP_VND_IG_RepTree = new int[]{3, 1, 20, 25, 39, 40, 49, 67, 68, 78, 66, 26, 8};
    public static final int[] GRASP_VND_IG_NaiveBayes = new int[]{1, 8, 23, 42, 45, 74, 47, 44, 21, 16, 12, 9, 3};
    public static final int[] GRASP_VND_IG_RandomForest = new int[]{6, 76, 46, 65, 16, 1, 25, 35, 39, 43, 49, 67, 68, 69, 70, 20, 14, 13, 10};
    public static final int[][] GRASP_VND_IG = {GRASP_VND_IG_RandomTree, GRASP_VND_IG_J48, GRASP_VND_IG_RepTree, GRASP_VND_IG_NaiveBayes, GRASP_VND_IG_RandomForest};

    public static final int[] GRASP_RVND_IG_RandomTree = new int[]{40, 1, 67, 68, 36, 63, 62, 49, 4};
    public static final int[] GRASP_RVND_IG_J48 = new int[]{8, 66, 77, 15, 1, 25, 31, 35, 49, 64, 67, 68, 70, 78, 56, 38, 54, 53, 43, 39, 13, 7, 3};
    public static final int[] GRASP_RVND_IG_RepTree = new int[]{3, 1, 20, 25, 39, 40, 49, 67, 68, 78, 66, 26};
    public static final int[] GRASP_RVND_IG_NaiveBayes = new int[]{1, 8, 23, 42, 45, 74, 47, 44, 21, 16, 12, 9, 4};
    public static final int[] GRASP_RVND_IG_RandomForest = new int[]{6, 1, 25, 35, 43, 49, 54, 67, 68, 78, 12, 48};
    public static final int[][] GRASP_RVND_IG = {GRASP_RVND_IG_RandomTree, GRASP_RVND_IG_J48, GRASP_RVND_IG_RepTree, GRASP_RVND_IG_NaiveBayes, GRASP_RVND_IG_RandomForest};

    public static final int[] GRASP_IG_RandomTree = new int[]{1, 73, 43, 68, 67};
    public static final int[] GRASP_IG_J48 = new int[]{67, 1, 36, 40, 73};
    public static final int[] GRASP_IG_NaiveBayes = new int[]{68, 11, 67, 42, 1};
    public static final int[] GRASP_IG_RandomForest = new int[]{76, 68, 1, 67, 42};
    public static final int[] GRASP_IG_RepTree = new int[]{67, 76, 36, 68, 40};
    public static final int[][] GRASP_IG = {GRASP_IG_RandomTree, GRASP_IG_J48, GRASP_IG_RepTree, GRASP_IG_NaiveBayes, GRASP_IG_RandomForest};

    public static final int[] GRASP_RVND_FULL_RCL_F1_RandomTree = new int[]{1, 40, 56, 65, 67, 68, 11, 46};
    public static final int[] GRASP_RVND_FULL_RCL_F1_J48 = new int[]{14, 1, 25, 37, 64, 67, 68, 11, 40, 7, 5, 2};
    public static final int[] GRASP_RVND_FULL_RCL_F1_REPTree = new int[]{1, 10, 25, 47, 49, 67, 68, 78, 66, 70, 77, 71, 57};
    public static final int[] GRASP_RVND_FULL_RCL_F1_NaiveBayes = new int[]{31, 1, 2, 7, 23, 26, 39, 45, 68, 74, 44, 9, 14, 20, 24, 52, 55, 67, 42, 32};
    public static final int[] GRASP_RVND_FULL_RCL_F1_RandomForest = new int[]{11, 78, 77, 76, 74, 70, 69, 68, 67, 66, 64, 63, 54, 53, 51, 49, 25, 7, 1};
    public static final int[] GRASP_RVND_FULL_RCL_F1[] = {GRASP_RVND_FULL_RCL_F1_RandomTree, GRASP_RVND_FULL_RCL_F1_J48, GRASP_RVND_FULL_RCL_F1_REPTree, GRASP_RVND_FULL_RCL_F1_NaiveBayes, GRASP_RVND_FULL_RCL_F1_RandomForest};

    // RCLs
    public static final int[] RCL_IWSSR_NaiveBayes = new int[]{1, 3, 4, 6, 8, 10, 11, 19, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 38};
    public static final int[] RCL_IWSSR_RandomTree = new int[]{1, 4, 13, 43, 49, 51, 56, 66, 67, 68, 78};
    public static final int[] RCL_IWSSR_J48 = new int[]{1, 6, 10, 16, 24, 25, 33, 34, 39, 44, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 67, 68, 72, 75};
    public static final int[] RCL_IWSSR_RandomForest = new int[]{1, 4, 6, 7, 11, 18, 20, 25, 44, 52, 56, 67, 68};
    public static final int[] RCL_IWSSR_RepTree = new int[]{1, 10, 11, 25, 26, 27, 31, 32, 33, 34, 44, 45, 46, 49, 50, 51, 57, 58, 59, 60, 61, 62, 63, 65, 66, 67, 68, 69, 71, 72, 73, 76, 78};
    public static final int[] RCL_IWSSR[] = {RCL_IWSSR_RandomTree, RCL_IWSSR_J48, RCL_IWSSR_RepTree, RCL_IWSSR_NaiveBayes, RCL_IWSSR_RandomForest};

    public static int[] OneR = new int[]{2, 37, 19, 17, 24};
    public static int[] IG = new int[]{2, 37, 19, 17, 16}; // same oneR, coincidentally
    public static int[] GR = new int[]{40, 67, 68, 70, 1};
}
