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
public class WsnFeatures {

    public static final int[] RCL_CICIDS_IG = new int[]{28, 36, 73, 43, 42, 8, 7, 13, 55, 14, 39, 11, 6, 66, 76, 40, 67, 68, 70, 1};

    public static final int[] WSN_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

    public static final int[] RCL_WSN_IWSSR_NaiveBayes = new int[]{6, 7, 8, 13, 18};
    public static final int[] RCL_WSN_IWSSR_RandomTree = new int[]{2, 3, 4, 6, 7, 9, 15, 18};
    public static final int[] RCL_WSN_IWSSR_J48 = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18};
    public static final int[] RCL_WSN_IWSSR_RandomForest = new int[]{2, 6, 7, 9, 10, 15, 16, 17, 18};
    public static final int[] RCL_WSN_IWSSR_RepTree = new int[]{2, 3, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    public static final int[] RCL_WSN_IWSSR[] = {RCL_WSN_IWSSR_RandomTree, RCL_WSN_IWSSR_J48, RCL_WSN_IWSSR_RepTree, RCL_WSN_IWSSR_NaiveBayes, RCL_WSN_IWSSR_RandomForest};

    public static int[] RCL = {};

    public static int[] CICIDS_OneR = new int[]{2, 37, 19, 17, 24};
    public static int[] CICIDS_IG = new int[]{2, 37, 19, 17, 16}; // same oneR, coincidentally
    public static int[] CICIDS_GR = new int[]{40, 67, 68, 70, 1};

    public static int[] WSN_IG = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_GR = new int[]{3, 6, 10, 12, 13};
    public static int[] WSN_GBDT = new int[]{2, 3, 4, 15, 18};

    public static int[] WSN_GRASPP_BITFLIP_NaiveBayes_5 = new int[]{17, 8, 7, 6, 2};
    public static int[] WSN_GRASPP_BITFLIP_RandomTree_5 = new int[]{7, 14, 15, 12, 6};
    public static int[] WSN_GRASP_BITFLIP_J48_5 = new int[]{14, 6, 15, 8, 2};
    public static int[] WSN_GRASPP_BITFLIP_RandomForest_5 = new int[]{8, 4, 2, 6, 15}; // Not Final
    public static int[] WSN_GRASPP_BITFLIP_RepTree_5 = new int[]{7, 6, 15, 4, 9};
    public static int[] WSN_GRASPP_BITFLIP[] = {WSN_GRASPP_BITFLIP_RepTree_5, WSN_GRASP_BITFLIP_J48_5, WSN_GRASPP_BITFLIP_RepTree_5, WSN_GRASPP_BITFLIP_NaiveBayes_5, WSN_GRASPP_BITFLIP_RandomForest_5};

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
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_NaiveBayes_5 = new int[]{7, 11, 8, 6, 2};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomForest_5 = new int[]{2, 13, 6, 14, 15};

    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomTree_5 = new int[]{17, 12, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_J48_5 = new int[]{18, 1, 2, 17, 6};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RepTree_5 = new int[]{1, 6, 11, 4, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_NaiveBayes_5 = new int[]{17, 8, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomForest_5 = new int[]{12, 4, 2, 6, 14};

}
