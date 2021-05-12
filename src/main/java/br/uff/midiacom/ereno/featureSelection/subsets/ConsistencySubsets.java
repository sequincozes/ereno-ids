package br.uff.midiacom.ereno.featureSelection.subsets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author silvio
 */
public class ConsistencySubsets {

    public static int[] UC01RankedIG = new int[]{15, 16, 42, 38, 40, 41, 19, 25, 34, 1, 14, 17, 18, 36, 37, 43, 39, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC02RankedIG = new int[]{19, 25, 34, 42, 15, 16, 38, 40, 41, 14, 17, 18, 36, 37, 43, 39, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC03RankedIG = new int[]{18, 14, 15, 38, 40, 41, 43, 16, 19, 25, 34, 42, 37, 39, 17, 36, 1, 5, 7, 4, 3, 6, 2, 11, 9, 12, 10, 13, 8, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC04RankedIG = new int[]{1, 14, 15, 16, 37, 42, 39, 43, 38, 40, 41, 17, 5, 36, 18, 19, 25, 34, 7, 6, 2, 3, 4, 11, 8, 13, 9, 10, 12, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC05RankedIG = new int[]{20, 21, 22, 24, 26, 27, 28, 29, 30, 32, 35, 31, 37, 23, 36, 39, 19, 25, 34, 42, 15, 38, 40, 41, 14, 18, 16, 17, 43, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 33};
    public static int[] UC06RankedIG = new int[]{16, 17, 20, 21, 22, 24, 26, 27, 28, 29, 30, 32, 35, 31, 37, 36, 23, 19, 25, 34, 39, 38, 40, 41, 42, 15, 14, 18, 43, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 33};
    public static int[] UC07RankedIG = new int[]{16, 19, 20, 21, 22, 24, 25, 26, 27, 28, 29, 30, 32, 34, 35, 37, 43, 36, 18, 14, 15, 42, 1, 38, 40, 41, 17, 5, 2, 3, 4, 6, 7, 13, 12, 11, 10, 8, 9, 39, 23, 31, 33};
    public static int[][] UC0 = new int[][]{new int[]{},UC01RankedIG, UC02RankedIG, UC03RankedIG, UC04RankedIG, UC05RankedIG, UC06RankedIG, UC07RankedIG};

    public static int[] UC01RankedIGExcludingTop3 = new int[]{38, 40, 41, 19, 25, 34, 1, 14, 17, 18, 36, 37, 43, 39, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC02RankedIGExcludingTop3 = new int[]{42, 15, 16, 38, 40, 41, 14, 17, 18, 36, 37, 43, 39, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC03RankedIGExcludingTop3 = new int[]{38, 40, 41, 43, 16, 19, 25, 34, 42, 37, 39, 17, 36, 1, 5, 7, 4, 3, 6, 2, 11, 9, 12, 10, 13, 8, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC04RankedIGExcludingTop3 = new int[]{16, 37, 42, 39, 43, 38, 40, 41, 17, 5, 36, 18, 19, 25, 34, 7, 6, 2, 3, 4, 11, 8, 13, 9, 10, 12, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 35};
    public static int[] UC05RankedIGExcludingTop3 = new int[]{24, 26, 27, 28, 29, 30, 32, 35, 31, 37, 23, 36, 39, 19, 25, 34, 42, 15, 38, 40, 41, 14, 18, 16, 17, 43, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 33};
    public static int[] UC06RankedIGExcludingTop3 = new int[]{21, 22, 24, 26, 27, 28, 29, 30, 32, 35, 31, 37, 36, 23, 19, 25, 34, 39, 38, 40, 41, 42, 15, 14, 18, 43, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 33};
    public static int[] UC07RankedIGExcludingTop3 = new int[]{21, 22, 24, 25, 26, 27, 28, 29, 30, 32, 34, 35, 37, 43, 36, 18, 14, 15, 42, 1, 38, 40, 41, 17, 5, 2, 3, 4, 6, 7, 13, 12, 11, 10, 8, 9, 39, 23, 31, 33};
    public static int[][] exTop3UC0 = new int[][]{new int[]{},UC01RankedIGExcludingTop3, UC02RankedIGExcludingTop3, UC03RankedIGExcludingTop3, UC04RankedIGExcludingTop3, UC05RankedIGExcludingTop3, UC06RankedIGExcludingTop3, UC07RankedIGExcludingTop3};


    /*@relation compiledtraffic
@attribute Time numeric
@attribute isbA numeric
@attribute isbB numeric
@attribute isbC numeric
@attribute ismA numeric
@attribute ismB numeric
@attribute ismC numeric
@attribute vsbA numeric
@attribute vsbB numeric
@attribute vsbC numeric
@attribute vsmA numeric
@attribute vsmB numeric
@attribute vsmC numeric
@attribute GooseTimestamp numeric
@attribute SqNum numeric
@attribute StNum numeric
@attribute cbStatus numeric
@attribute frameLen numeric
@attribute ethDst {01:a0:f4:08:2f:77, FF:FF:FF:FF:FF:FF}
@attribute ethSrc {FF:FF:FF:FF:FF:FF, 00:a0:f4:08:2f:77}
@attribute ethType {0x000077b7, 0x000088b8}
@attribute gooseTimeAllowedtoLive numeric
@attribute gooseAppid {0x00003002, 0x00003001}
@attribute gooseLen numeric
@attribute TPID {0x7101, 0x8100}
@attribute gocbRef {LD/LLN0$IntLockB, LD/LLN0$GO$gcbA}
@attribute datSet {LD/LLN0$IntLockA, AA1C1Q01A1LD0/LLN0$InterlockingC}
@attribute goID {InterlockingF, InterlockingA}
@attribute test {TRUE, FALSE}
@attribute confRev numeric
@attribute ndsCom {TRUE, FALSE}
@attribute numDatSetEntries numeric
@attribute APDUSize numeric
@attribute protocol {SV, GOOSE}
@attribute stDiff numeric
@attribute sqDiff numeric
@attribute gooseLenghtDiff numeric
@attribute cbStatusDiff numeric
@attribute apduSizeDiff numeric
@attribute frameLenthDiff numeric
@attribute timestampDiff numeric
@attribute tDiff numeric*/
}
