package uk.ac.ed.inf.movement.model;

public final class MovementModel {
    /**
     * The number of moves the drone can make before it runs out of battery.
     */
    private final int MAXIMUM_MOVES = 2000;

    /**
     * The coordinates (LngLat point) of the delivery point, i.e. Appleton Tower.
     */
    private final LngLat DELIVERY_POINT = new LngLat(-3.186874, 55.944494);

    private final CentralArea centralArea;
    private final NoFlyZone[] noFlyZones;
    private int movesLeft = MAXIMUM_MOVES;

    public MovementModel() {
        centralArea = CentralArea.getCentralAreaFromRestServer();
        noFlyZones = NoFlyZone.getNoFlyZonesFromRestServer();
    }


    // Calculate flightpath to a certain restaurant.
    public Flightpath calculateShortestFlightpathToRestaurant(LngLat restaurantLocation) {
        Flightpath flightpath = new Flightpath(DELIVERY_POINT, restaurantLocation, centralArea, noFlyZones);
        flightpath.findShortestPath();
        flightpath.optimiseFlightpath();
        flightpath.calculateMovesPoints();
        return flightpath;
    }
    public void adjustMovesLeft(int moves) {
        movesLeft -= moves;
    }

    public int getMovesLeft() {
        return movesLeft;
    }
}
