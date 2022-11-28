package uk.ac.ed.inf.movement;

import uk.ac.ed.inf.movement.view.FlightpathView;

import java.util.ArrayList;
import java.util.Arrays;

public final class MovementController {
    private final String date;
    private final CentralArea centralArea;
    private final NoFlyZone[] noFlyZones;
    private final LngLat deliveryPoint;
    private int MOVES_LEFT = MovementConstants.MAXIMUM_MOVES;
    private final FlightpathView flightpathView = new FlightpathView();

    public MovementController(String date) {
        centralArea = CentralArea.getCentralAreaFromRestServer();
        noFlyZones = NoFlyZone.getNoFlyZonesFromRestServer();
//        noFlyZones[3] = new NoFlyZone(new ArrayList<>(Arrays.asList(
//                new ArrayList<>(Arrays.asList(-3.192, 55.945)),
//                new ArrayList<>(Arrays.asList(-3.192, 55.9435)),
//                new ArrayList<>(Arrays.asList(-3.19114, 55.9435)),
//                new ArrayList<>(Arrays.asList(-3.19069, 55.9436)),
//                new ArrayList<>(Arrays.asList(-3.1912, 55.9437)),
//                new ArrayList<>(Arrays.asList(-3.1907, 55.9456)),
//                new ArrayList<>(Arrays.asList(-3.192, 55.945))
//        )));
        deliveryPoint = MovementConstants.APPLETON_TOWER;
        this.date = date;
    }

    // Calculate flightpath to a certain restaurant.
    public Flightpath calculateShortestFlightpathToRestaurant(LngLat restaurantLocation) {
        Flightpath flightpath = new Flightpath(deliveryPoint, restaurantLocation, noFlyZones);
        flightpath.findShortestPath();
        flightpath.optimiseFlightpath();
        flightpath.calculateMovesPoints();
        return flightpath;
    }

    public boolean enoughMovesLeft(Flightpath flightpath) {
        return flightpath.getDroneMovesDirections().size() * 2 + 2 < MOVES_LEFT;
    }

    public void makeDelivery(Flightpath flightpath) {
        int lengthInMoves = flightpath.getDroneMovesDirections().size() * 2 + 2;
        MOVES_LEFT -= lengthInMoves;
    }

    public void outputDroneMoves(ArrayList<DroneMove> droneMoves) {
        flightpathView.createFlightpathFile(date, droneMoves);
        flightpathView.createDroneFile(date, droneMoves);
    }
}

