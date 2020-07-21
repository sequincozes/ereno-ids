package br.uff.midiacom.legacy.substation;



public class Attack {

    private String attackName;
    private int numberAttacks;
    private int numberNormals;

    public Attack(String attackName, int numberAttacks, int numberNormals) {
        this.attackName = attackName;
        this.numberAttacks = numberAttacks;
        this.numberNormals = numberNormals;
    }

    public String getAttackName() {
        return attackName;
    }

    public void setAttackName(String attackName) {
        this.attackName = attackName;
    }



    public int getNumberAttacks() {
        return numberAttacks;
    }

    public void setNumberAttacks(int numberAttacks) {
        this.numberAttacks = numberAttacks;
    }

    public int getNumberNormals() {
        return numberNormals;
    }

    public void setNumberNormals(int numberNormals) {
        this.numberNormals = numberNormals;
    }
}
