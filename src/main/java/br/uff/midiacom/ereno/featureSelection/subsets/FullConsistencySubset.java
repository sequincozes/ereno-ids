package br.uff.midiacom.ereno.featureSelection.subsets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author silvio
 */
public class FullConsistencySubset {

    public static int[] FULL_IG = {18,24,33,14,15,41,16,20,21,22,23,25,26,28,29,31,34,19,27,30,32,36,1,37,39,40,17,42,35,5,2,6,3,4,7,11,8,12,38,10,9,13};
    public static int[] FULL_IG_REVERSE = {13,9,10,38,12,8,11,7,4,3,6,2,5,35,42,17,37,39,40,1,36,19,27,30,32,20,21,22,23,25,26,28,29,31,34,16,41,15,14,18,24,33};

    public static int[] FULL = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};

    public static int[] SV_GOOSE = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35}; //without diff features

    public static int[] IG_5 = {
        33, 18, 24, 14, 15};

    public static int[] IG_10 = {33, 18, 24, 14, 15, 41, 16, 21, 20, 34};

    public static int[] IG_15 = {33, 18, 24, 14, 15, 41, 16, 21, 20, 34, 22, 26, 31, 23, 29};

    public static int[] IG_20 = {33, 18, 24, 14, 15, 41, 16, 21, 20, 34, 22, 26, 31, 23, 29, 28, 25, 32, 27, 19};

    public static int[] IG_25 = {33, 18, 24, 14, 15, 41, 16, 21, 20, 34, 22, 26, 31, 23, 29, 28, 25, 32, 27, 19, 30, 36, 1, 40, 37};

    public static int[] IG_30 = {33, 18, 24, 14, 15, 41, 16, 21, 20, 34, 22, 26, 31, 23, 29, 28, 25, 32, 27, 19, 30, 36, 1, 40, 37, 39, 17, 42, 35, 5};

    public static int[] IG_35 = {33, 18, 24, 14, 15, 41, 16, 21, 20, 34, 22, 26, 31, 23, 29, 28, 25, 32, 27, 19, 30, 36, 1, 40, 37, 39, 17, 42, 35, 5, 2, 6, 3, 4, 7};

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
