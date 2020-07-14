/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstractClassification;

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

}
