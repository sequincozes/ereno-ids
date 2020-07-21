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

    public static int[] WSN_IG = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_GR = new int[]{3, 6, 10, 12, 13};
    public static int[] WSN_GBDT = new int[]{2, 3, 4, 15, 18};

    // GRASP 
    public static int[] WSN_GRASPP_BITFLIP_NaiveBayes = new int[]{17, 8, 7, 6, 2};
    public static int[] WSN_GRASPP_BITFLIP_RandomTree = new int[]{7, 14, 15, 12, 6};
    public static int[] WSN_GRASP_BITFLIP_J48 = new int[]{14, 6, 15, 8, 2};
    public static int[] WSN_GRASPP_BITFLIP_RandomForest = new int[]{8, 4, 2, 6, 15}; // Not Final
    public static int[] WSN_GRASPP_BITFLIP_RepTree = new int[]{7, 6, 15, 4, 9};

    public static int[] WSN_BLACKHOLE_IG = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_BLACKHOLE_GR = new int[]{3, 6, 9, 10, 13};
    public static int[] WSN_BLACKHOLE_ONER = new int[]{3, 4, 6, 7, 18};
    public static int[] WSN_GRAYHOLE_IG = new int[]{3, 6, 12, 13, 18};
    public static int[] WSN_GRAYHOLE_GR = new int[]{3, 6, 8, 12, 13};
    public static int[] WSN_GRAYHOLE_ONER = new int[]{3, 6, 7, 15, 18};
    public static int[] WSN_FLOODING_IG = new int[]{3, 6, 7, 13, 16};
    public static int[] WSN_FLOODING_GR = new int[]{3, 6, 12, 13, 17};
    public static int[] WSN_FLOODING_ONER = new int[]{4, 6, 7, 8, 17};

    // GRASP 
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RandomTree = new int[]{3, 10, 15, 1, 14};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_J48 = new int[]{15, 18, 12, 14, 3};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RepTree = new int[]{8, 9, 14, 15, 7};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_NaiveBayes = new int[]{16, 18, 8, 1, 6};
    public static int[] WSN_BLACKHOKE_GRASP_BITFLIP_RandomForest = new int[]{1, 14, 16, 6, 9};

    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomTree = new int[]{15, 12, 4, 9, 3};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_J48 = new int[]{15, 5, 4, 3, 9};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RepTree = new int[]{6, 18, 15, 3, 9};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_NaiveBayes = new int[]{7, 11, 8, 6, 2};
    public static int[] WSN_GRAYHOLE_GRASP_BITFLIP_RandomForest = new int[]{2, 13, 6, 14, 15};

    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomTree = new int[]{17, 12, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_J48 = new int[]{18, 1, 2, 17, 6};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RepTree = new int[]{1, 6, 11, 4, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_NaiveBayes = new int[]{17, 8, 6, 1, 2};
    public static int[] WSN_FLOODING_GRASP_BITFLIP_RandomForest = new int[]{12, 4, 2, 6, 14};
    
    public static int[] WSN_FULL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
}
