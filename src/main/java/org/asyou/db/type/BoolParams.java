package org.asyou.db.type;

public class BoolParams {
    private boolean contain;
    private boolean or;
    private boolean not;

    public boolean getContain() {
        return contain;
    }

    public BoolParams setContain(boolean contain) {
        this.contain = contain;
        return this;
    }

    public boolean getOr() {
        return or;
    }

    public BoolParams setOr(boolean or) {
        this.or = or;
        return this;
    }

    public boolean getNot() {
        return not;
    }

    public BoolParams setNot(boolean not) {
        this.not = not;
        return this;
    }

    public static BoolParams buildAnd() {
        return new BoolParams()
                .setContain(false)
                .setNot(false)
                .setOr(false)
                ;
    }

    public static BoolParams buildContain() {
        return new BoolParams()
                .setContain(true)
                .setNot(false)
                .setOr(false);
    }

    public static BoolParams buildOr() {
        return new BoolParams()
                .setContain(false)
                .setNot(false)
                .setOr(true);
    }
}