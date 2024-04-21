package fr.arcane.spellcast.spells;

public enum Spells {

    AEROLITHE(Aerolithe.ID, Aerolithe.COLOR, Aerolithe.NAME),
    REPULSIUS(Repulsius.ID, Repulsius.COLOR, Repulsius.NAME),
    PARTISAN(Partisan.ID, Partisan.COLOR, Partisan.NAME);

    private final String ID;
    private final int COLOR;
    private final String NAME;

    Spells(String id, int color, String name) {
        this.ID = id;
        this.COLOR = color;
        this.NAME = name;
    }

    public String getID() {
        return this.ID;
    }

    public int getColor() {
        return this.COLOR;
    }

    public String getName() {
        return this.NAME;
    }
}
