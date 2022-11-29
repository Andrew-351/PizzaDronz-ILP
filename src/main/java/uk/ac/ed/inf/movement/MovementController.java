package uk.ac.ed.inf.movement;

import uk.ac.ed.inf.movement.view.FlightpathView;
import java.util.ArrayList;

public final class MovementController {
    /**
     * The number of moves the drone can make before it runs out of battery.
     */
    private final static int MAXIMUM_MOVES = 2000;

    /**
     * The coordinates (LngLat point) of the delivery point, i.e. Appleton Tower.
     */
    private final static LngLat DELIVERY_POINT = new LngLat(-3.186874, 55.944494);

    private final String date;
    private final CentralArea centralArea;
    private final NoFlyZone[] noFlyZones;
    private int movesLeft = MAXIMUM_MOVES;
    private final FlightpathView flightpathView = new FlightpathView();

    public MovementController(String date) {
        this.date = date;
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

    public boolean enoughMovesLeft(Flightpath flightpath) {
        return flightpath.getDroneMovesDirections().size() * 2 + 2 < movesLeft;
    }

    public void makeDelivery(Flightpath flightpath) {
        int lengthInMoves = flightpath.getDroneMovesDirections().size() * 2 + 2;
        movesLeft -= lengthInMoves;
    }

    public void outputDroneMoves(ArrayList<DroneMove> droneMoves) {
        flightpathView.createFlightpathFile(date, droneMoves);
        flightpathView.createDroneFile(date, droneMoves);
    }
}

