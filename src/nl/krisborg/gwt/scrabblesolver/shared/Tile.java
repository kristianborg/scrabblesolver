package nl.krisborg.gwt.scrabblesolver.shared;

/**
 * User: Kris
 * Since: 15-10-11 0:06
 */
public enum Tile {
    A(1),
    B(3),
    C(5),
    D(2),
    E(1),
    F(4),
    G(3),
    H(4),
    I(2), // was 1
    J(4),
    K(3),
    L(3),
    M(3),
    N(1),
    O(1),
    P(3),
    Q(10),
    R(2),
    S(2),
    T(2),
    U(2), // was 4
    V(4),
    W(5),
    X(8),
    Y(8),
    Z(4);

    private int points;

    private Tile(int points){
        this.points = points;
    }

    public int getPoints(){
        return this.points;
    }

    public static Tile valueOfChar(Character c){
        return valueOf(c.toString().toUpperCase());
    }
}
