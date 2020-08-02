/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.abstractclassification;

/**
 *
 * @author silvio
 */
public class FeatureSubsets {

    public static final int[] WSN_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    public static final int[] RCL_CICIDS_IG = new int[]{28, 36, 73, 43, 42, 8, 7, 13, 55, 14, 39, 11, 6, 66, 76, 40, 67, 68, 70, 1};
    public static final int[] CICIDS_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78};
    public static final int[] KDD_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41};

    // RCLs
    public static final int[] RCL_CICIDS_IWSSR_NaiveBayes = new int[]{1, 3, 4, 6, 8, 10, 11, 19, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 38, 78};
    public static final int[] RCL_CICIDS_IWSSR_RandomTree = new int[]{1, 4, 13, 43, 49, 51, 56, 66, 67, 68, 78};
    public static final int[] RCL_CICIDS_IWSSR_J48 = new int[]{1, 6, 10, 16, 24, 25, 33, 34, 39, 44, 46, 47, 48, 49, 50, 51, 52, 57, 58, 59, 60, 61, 62, 67, 68, 72, 75, 78};
    public static final int[] RCL_CICIDS_IWSSR_RandomForest = new int[]{1, 4, 6, 7, 11, 18, 20, 25, 44, 52, 56, 67, 68, 78};
    public static final int[] RCL_CICIDS_IWSSR_RepTree = new int[]{1, 10, 11, 25, 26, 27, 31, 32, 33, 34, 44, 45, 46, 49, 50, 51, 57, 58, 59, 60, 61, 62, 63, 65, 66, 67, 68, 69, 71, 72, 73, 76, 78};
    public static final int[] RCL_CICIDS_IWSSR[] = {RCL_CICIDS_IWSSR_RandomTree, RCL_CICIDS_IWSSR_J48, RCL_CICIDS_IWSSR_RepTree, RCL_CICIDS_IWSSR_NaiveBayes, RCL_CICIDS_IWSSR_RandomForest};

    public static final int[] RCL_WSN_IWSSR_NaiveBayes = new int[]{6, 7, 8, 13, 18};
    public static final int[] RCL_WSN_IWSSR_RandomTree = new int[]{2, 3, 4, 6, 7, 9, 15, 18};
    public static final int[] RCL_WSN_IWSSR_J48 = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18};
    public static final int[] RCL_WSN_IWSSR_RandomForest = new int[]{2, 6, 7, 9, 10, 15, 16, 17, 18};
    public static final int[] RCL_WSN_IWSSR_RepTree = new int[]{2, 3, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    public static final int[] RCL_WSN_IWSSR[] = {RCL_WSN_IWSSR_RandomTree, RCL_WSN_IWSSR_J48, RCL_WSN_IWSSR_RepTree, RCL_WSN_IWSSR_NaiveBayes, RCL_WSN_IWSSR_RandomForest};

    public static final int[] RCL_KDD_IWSSR_NaiveBayes = new int[]{3, 4, 8, 9, 11, 13, 14, 15, 18, 20, 21, 41};
    public static final int[] RCL_KDD_IWSSR_RandomTree = new int[]{3, 4, 5, 6, 12, 14, 17, 22, 23, 26, 27, 29, 30, 40, 41};
    public static final int[] RCL_KDD_IWSSR_J48 = new int[]{1, 3, 4, 5, 6, 7, 12, 13, 15, 17, 19, 20, 24, 25, 27, 29, 30, 31, 32, 33, 41};
    public static final int[] RCL_KDD_IWSSR_RandomForest = new int[]{1, 3, 4, 5, 6, 12, 16, 23, 30, 34, 37, 38, 39, 40, 41};
    public static final int[] RCL_KDD_IWSSR_RepTree = new int[]{1, 3, 4, 5, 6, 7, 8, 9, 12, 16, 17, 18, 19, 20, 21, 24, 25, 26, 27, 30, 32, 34, 37, 40, 41};
    public static final int[] RCL_KDD_IWSSR[] = {RCL_KDD_IWSSR_RandomTree, RCL_KDD_IWSSR_J48, RCL_KDD_IWSSR_RepTree, RCL_KDD_IWSSR_NaiveBayes, RCL_KDD_IWSSR_RandomForest};

    public static int[] RCL = {};

    public static int[] CICIDS_OneR = new int[]{2, 37, 19, 17, 24};
    public static int[] CICIDS_IG = new int[]{2, 37, 19, 17, 16}; // same oneR, coincidentally
    public static int[] CICIDS_GR = new int[]{40, 67, 68, 70, 1};

    public static int[] WSN_IG = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_GR = new int[]{3, 6, 10, 12, 13};
    public static int[] WSN_GBDT = new int[]{2, 3, 4, 15, 18};

    // GRASP 
    public static int[] WSN_GRASPP_BITFLIP_NaiveBayes_5 = new int[]{17, 8, 7, 6, 2};
    public static int[] WSN_GRASPP_BITFLIP_RandomTree_5 = new int[]{7, 14, 15, 12, 6};
    public static int[] WSN_GRASP_BITFLIP_J48_5 = new int[]{14, 6, 15, 8, 2};
    public static int[] WSN_GRASPP_BITFLIP_RandomForest_5 = new int[]{8, 4, 2, 6, 15}; // Not Final
    public static int[] WSN_GRASPP_BITFLIP_RepTree_5 = new int[]{7, 6, 15, 4, 9};

    public static int[] WSN_BLACKHOLE_IG_5 = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_BLACKHOLE_GR_5 = new int[]{3, 6, 9, 10, 13};
    public static int[] WSN_BLACKHOLE_ONER_5 = new int[]{3, 4, 6, 7, 18};
    public static int[] WSN_GRAYHOLE_IG_5 = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_GRAYHOLE_GR_5 = new int[]{3, 6, 8, 12, 13};
    public static int[] WSN_GRAYHOLE_ONER_5 = new int[]{3, 6, 7, 15, 18};
    public static int[] WSN_FLOODING_IG_5 = new int[]{3, 6, 7, 13, 16};
    public static int[] WSN_FLOODING_GR_5 = new int[]{3, 6, 12, 13, 17};
    public static int[] WSN_FLOODING_ONER_5 = new int[]{4, 6, 7, 8, 17};

    // GRASP 
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RandomTree_5 = new int[]{3, 10, 15, 1, 14};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_J48_5 = new int[]{15, 18, 12, 14, 3};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RepTree_5 = new int[]{8, 9, 14, 15, 7};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_NaiveBayes_5 = new int[]{16, 18, 8, 1, 6};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RandomForest_5 = new int[]{1, 14, 16, 6, 9};

    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomTree_5 = new int[]{15, 12, 4, 9, 3};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_J48_5 = new int[]{15, 5, 4, 3, 9};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RepTree_5 = new int[]{6, 18, 15, 3, 9};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_NaiveBayes_5 = new int[]{7, 11, 8, 6, 2};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomForest_5 = new int[]{2, 13, 6, 14, 15};

    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomTree_5 = new int[]{17, 12, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_J48_5 = new int[]{18, 1, 2, 17, 6};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RepTree_5 = new int[]{1, 6, 11, 4, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_NaiveBayes_5 = new int[]{17, 8, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomForest_5 = new int[]{12, 4, 2, 6, 14};

}
