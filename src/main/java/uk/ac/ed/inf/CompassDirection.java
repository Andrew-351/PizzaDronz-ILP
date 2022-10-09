package uk.ac.ed.inf;

/**
 * Representation of a compass direction of the drone's move.
 */

public enum CompassDirection {
    /**
     * A "hover" move corresponds to the null direction value.
     */
    HOVER(null),

    /**
     * East
     */
    E(0.0),

    /**
     * East North East
     */
    ENE(22.5),

    /**
     * North East
     */
    NE(45.0),

    /**
     * North North East
     */
    NNE(67.5),

    /**
     * North
     */
    N(90.0),

    /**
     * North North West
     */
    NNW(112.5),

    /**
     * North West
     */
    NW(135.0),

    /**
     * West North West
     */
    WNW(157.5),

    /**
     * West
     */
    W(180.0),

    /**
     * West South West
     */
    WSW(202.5),

    /**
     * South West
     */
    SW(225.0),

    /**
     * South South West
     */
    SSW(247.5),

    /**
     * South
     */
    S(270.0),

    /**
     * South South East
     */
    SSE(292.5),

    /**
     * South East
     */
    SE(315.0),

    /**
     * East South East
     */
    ESE(337.5);

    /**
     * The angle in degrees corresponding to the compass direction.
     */
    private final double angle;

    /**
     * Constructor to initialise an instance variable.
     * @param angle angle (in degrees) corresponding to a compass direction
     */
    CompassDirection(Double angle) {
        this.angle = angle;
    }

    /**
     * Returns an angle in degrees corresponding to the compass direction.
     * @return an angle in degrees corresponding to the compass direction.
     */
    public double getAngle() {
        return angle;
    }
}
