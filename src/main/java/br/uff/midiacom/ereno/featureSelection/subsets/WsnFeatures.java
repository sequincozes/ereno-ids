/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.featureSelection.subsets;

/**
 * @author silvio
 */
public class WsnFeatures extends FeatureSubsets {


    public WsnFeatures() {
        super(WSN_FULL, WSN_RCL_GR, WSN_RCL_I);
    }
    public static final int[] RCL_WSN_IWSSR_NaiveBayes = new int[]{2, 3, 18}; // 15 minutes
    public static final int[] RCL_WSN_IWSSR_RandomTree = new int[]{2, 6, 8, 10, 14, 15, 17, 18};
    public static final int[] RCL_WSN_IWSSR_J48 = new int[]{2, 4, 7, 8, 9, 10, 14, 15, 18}; //2 h 14 min
    public static final int[] RCL_WSN_IWSSR_RandomForest = new int[]{2, 4, 6, 7, 10, 14, 15, 16, 17, 1}; //  1 d 05:02 h
    public static final int[] RCL_WSN_IWSSR_RepTree = new int[]{2, 4, 6, 10, 11, 14, 15, 18}; // 30+ minutes
    public static final int[] RCL_WSN_IWSSR[] = {RCL_WSN_IWSSR_RandomTree, RCL_WSN_IWSSR_J48, RCL_WSN_IWSSR_RepTree, RCL_WSN_IWSSR_NaiveBayes, RCL_WSN_IWSSR_RandomForest};

    public static int[] WSN_IG = new int[]{12, 18, 13, 3, 6};
    public static int[] WSN_GR = new int[]{8, 10, 13, 6, 3};
    public static int[] WSN_OneR = new int[]{9, 7, 18, 3, 6}; //      Dataset Bugado
    //    public static int[] WSN_IG = new int[]{3, 6, 12, 13, 18};
    //    public static int[] WSN_GR = new int[]{3, 6, 10, 12, 13};
    //    public static int[] WSN_GBDT = new int[]{2, 3, 4, 15, 18};

    public static int[] GR_G_BF_NaiveBayes_5 = new int[]{18, 8, 7, 13, 6};
    public static int[] GR_G_BF_RandomTree_5 = new int[]{9, 15, 3, 2, 6};
    public static int[] GR_G_BF_J48_5 = new int[]{9, 14, 6, 7, 15};
    public static int[] GR_G_BF_RandomForest_5 = new int[]{6, 9, 10, 18, 15};
    public static int[] GR_G_BF_RepTree_5 = new int[]{10, 3, 7, 13, 15};
    public static int[] GR_G_BF[] = {GR_G_BF_RandomTree_5, GR_G_BF_J48_5, GR_G_BF_RepTree_5, GR_G_BF_NaiveBayes_5, GR_G_BF_RandomForest_5};

    public static final int[] I_G_VND_RandomTree = new int[]{2, 3, 15, 9, 6};
    public static final int[] I_G_VND_J48 = new int[]{4, 3, 18, 9, 15, 14, 11, 10, 7, 6, 2};
    public static final int[] I_G_VND_RepTree = new int[]{10, 15, 14, 11, 6, 4, 2};
    public static final int[] I_G_VND_NaiveBayes = new int[]{6, 13, 7, 8};
    public static final int[] I_G_VND_RandomForest = new int[]{18, 10, 7, 9, 6, 16, 15, 2};
    public static final int[][] I_G_VND = {I_G_VND_RandomTree, I_G_VND_J48, I_G_VND_RepTree, I_G_VND_NaiveBayes, I_G_VND_RandomForest};

    //    public static final int[] RCL_VND_RandomTree = new int[]{2, 9, 6, 15, 4, 3, 8};
//    public static final int[] RCL_VND_J48 = new int[]{4, 16, 15, 17, 14, 13, 11, 10, 9};
//    public static final int[] RCL_VND_RepTree = new int[]{18, 4, 16, 15, 17, 14, 13};
//    public static final int[] RCL_VND_NaiveBayes = new int[]{6, 7};
//    public static final int[] RCL_VND_RandomForest = new int[]{18, 1, 15};
//    public static final int[] FULL_RCL_VND[] = {RCL_VND_RandomTree, RCL_VND_J48, RCL_VND_RepTree, RCL_VND_NaiveBayes, RCL_VND_RandomForest};
//    public static final int[] RCL_RVND_RandomTree = new int[]{2, 9, 6, 15, 4, 3, 8};
//    public static final int[] RCL_RVND_J48 = new int[]{18, 4, 15, 14, 11, 10, 9, 7, 6, 2};
//    public static final int[] RCL_RVND_RepTree = new int[]{18, 1, 4, 16, 15, 17, 14, 13, 12, 11, 10, 9, 7, 6, 2};
//    public static final int[] RCL_RVND_NaiveBayes = new int[]{6, 7};
//    public static final int[] RCL_RVND_RandomForest = new int[]{9, 6, 16, 15};
//    public static final int[] FULL_RCL_RVND[] = {RCL_RVND_RandomTree, RCL_RVND_J48, RCL_RVND_RepTree, RCL_RVND_NaiveBayes, RCL_RVND_RandomForest};
    public static final int[] GR_G_RVND_RandomTree = new int[]{9, 15, 6, 3, 2};
    public static final int[] GR_G_RVND_J48 = new int[]{4, 3, 18, 9, 15, 14, 11, 10, 7, 6, 2};
    public static final int[] GR_G_RVND_RepTree = new int[]{3, 10, 15, 14, 6, 4, 2};
    public static final int[] GR_G_RVND_NaiveBayes = new int[]{6, 13, 7, 8};
    public static final int[] GR_G_RVND_RandomForest = new int[]{6, 9, 10, 18, 15};
    public static final int[] GR_G_RVND[] = {GR_G_RVND_RandomTree, GR_G_RVND_J48, GR_G_RVND_RepTree, GR_G_RVND_NaiveBayes, GR_G_RVND_RandomForest};

    public static final int[] GR_G_VND_RandomTree = new int[]{9, 15, 6, 3, 2};
    public static final int[] GR_G_VND_J48 = new int[]{4, 3, 18, 9, 15, 14, 11, 10, 7, 6, 2};
    public static final int[] GR_G_VND_RepTree = new int[]{3, 10, 15, 14, 6, 4, 2};
    public static final int[] GR_G_VND_NaiveBayes = new int[]{7, 6, 8, 13};
    public static final int[] GR_G_VND_RandomForest = new int[]{6, 9, 7, 10, 18};
    public static final int[] GR_G_VND[] = {GR_G_VND_RandomTree, GR_G_VND_J48, GR_G_VND_RepTree, GR_G_VND_NaiveBayes, GR_G_VND_RandomForest};

    // Standard FeatureSubsets RCLSs
    public static final int[] WSN_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    public static final int[] WSN_RCL_GR = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    public static final int[][] WSN_RCL_I = RCL_WSN_IWSSR;

    // GRASP CIoT
    public static int[] WSN_BLACKHOLE_IG_5 = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_BLACKHOLE_GR_5 = new int[]{3, 6, 9, 10, 13};
    public static int[] WSN_BLACKHOLE_ONER_5 = new int[]{3, 4, 6, 7, 18};
    public static int[] WSN_GRAYHOLE_IG_5 = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_GRAYHOLE_GR_5 = new int[]{3, 6, 8, 12, 13};
    public static int[] WSN_GRAYHOLE_ONER_5 = new int[]{3, 6, 7, 15, 18};
    public static int[] WSN_FLOODING_IG_5 = new int[]{3, 6, 7, 13, 16};
    public static int[] WSN_FLOODING_GR_5 = new int[]{3, 6, 12, 13, 17};
    public static int[] WSN_FLOODING_ONER_5 = new int[]{4, 6, 7, 8, 17};

    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RandomTree_5 = new int[]{3, 10, 15, 1, 14};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_J48_5 = new int[]{15, 18, 12, 14, 3};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RepTree_5 = new int[]{8, 9, 14, 15, 7};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_NaiveBayes_5 = new int[]{16, 18, 8, 1, 6};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RandomForest_5 = new int[]{1, 14, 16, 6, 9};

    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomTree_5 = new int[]{15, 12, 4, 9, 3};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_J48_5 = new int[]{15, 5, 4, 3, 9};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RepTree_5 = new int[]{6, 18, 15, 3, 9};
    public static int[] WSN_GRAYHOLE_GRASP_BTIFLIP_NaiveBayes_5 = new int[]{7, 11, 8, 6, 2};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomForest_5 = new int[]{2, 13, 6, 14, 15};

    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomTree_5 = new int[]{17, 12, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_J48_5 = new int[]{18, 1, 2, 17, 6};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RepTree_5 = new int[]{1, 6, 11, 4, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_NaiveBayes_5 = new int[]{17, 8, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomForest_5 = new int[]{12, 4, 2, 6, 14};

}
