package uk.ac.ed.inf.movement;

import uk.ac.ed.inf.movement.model.*;

public final class MovementController {
    private final CentralArea centralArea;
    private final NoFlyZone[] noFlyZones;
    private final LngLat deliveryPoint;

    public MovementController() {
        centralArea = CentralArea.getCentralAreaFromRestServer();
        noFlyZones = NoFlyZone.getNoFlyZonesFromRestServer();
        deliveryPoint = MovementConstants.APPLETON_TOWER;
    }

    // Calculate flightpath to a certain restaurant.
    public Flightpath calculateShortestFlightpathToRestaurant(LngLat restaurantLocation) {
        Flightpath flightpath = new Flightpath(deliveryPoint, restaurantLocation, centralArea, noFlyZones);
        flightpath.findShortestPath();
        flightpath.optimiseFlightpath();
        flightpath.calculateMovesPoints();
        return flightpath;
    }
}

