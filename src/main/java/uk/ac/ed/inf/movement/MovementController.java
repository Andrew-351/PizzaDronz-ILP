package uk.ac.ed.inf.movement;

import uk.ac.ed.inf.movement.model.*;

import java.util.ArrayList;

/**
 * The Movement Controller class keeps track and controls all actions solely related to the drone's movement.
 * It only directly communicates with Movement Model and Movement View.
 */

public final class MovementController {
    private final MovementModel movementModel;
    private final MovementView movementView;

    public MovementController() {
        movementModel = new MovementModel();
        movementView = new MovementView();
    }

    /**
     * Computes a flightpath to a certain restaurant.
     * @param restaurantLocation LngLat coordinates of the restaurant
     * @return a Flightpath object which represents the found path to the restaurant
     */
    public Flightpath getFlightpathToRestaurant(LngLat restaurantLocation) {
        return movementModel.calculateShortestFlightpathToRestaurant(restaurantLocation);
    }

    /**
     * Given a flightpath to a restaurant and an order currently being delivered, generates a list of drone's moves
     * in the format required for the output file.
     * The drone flies from the delivery point to a restaurant, hovers for one move, returns via the same path and hovers
     * above the delivery point for one move.
     * @param flightpath a flightpath to a certain restaurant
     * @param orderNo a string representing the number of the order currently being delivered
     * @return a list of drone's moves to collect and deliver a specific order
     */
    public ArrayList<DroneMove> generateMovesForFlightpath(Flightpath flightpath, String orderNo, long computationStartTime) {
        // The list of all moves to pick up and deliver current order.
        ArrayList<DroneMove> droneMovesForFlightpath = new ArrayList<>();

        // The list of LngLat points of the flightpath (from AT to the restaurant) for the drone to follow.
        ArrayList<LngLat> movesPoints = new ArrayList<>(flightpath.getDroneMovesPoints());

        /*
            Append the reversed list of flightpath points to movesPoint (represents hovering above the restaurant
            and flying back to AT via the same path).
            Append the coordinates of AT to represent hovering above AT when the order is collected.
         */
        LngLat firstPoint = movesPoints.get(0);
        int pointsSize = movesPoints.size();
        for (int i = pointsSize - 1; i >= 0; i--) {
            movesPoints.add(flightpath.getDroneMovesPoints().get(i));
        }
        movesPoints.add(firstPoint);

        // The list of compass directions of the flightpath for the drone to follow.
        ArrayList<CompassDirection> movesDirections = new ArrayList<>(flightpath.getDroneMovesDirections());

        /*
            Append HOVER to movesDirections (order is picked up from the restaurant).
            Append the list of opposite directions to represent flying back via the same path.
            Append HOVER (order is collected on the top of AT).
         */
        int directionsSize = movesDirections.size();
        movesDirections.add(CompassDirection.HOVER);
        for (int i = directionsSize - 1; i >= 0; i--) {
            movesDirections.add(flightpath.getDroneMovesDirections().get(i).getOppositeDirection());
        }
        movesDirections.add(CompassDirection.HOVER);

        /*
            Create a list of drone's moves in the format required for the output file.
         */
        for (int i = 0; i < movesDirections.size(); i++) {
            CompassDirection direction = movesDirections.get(i);
            Double angle = direction.getAngle();
            long ticks = System.nanoTime() - computationStartTime;
            droneMovesForFlightpath.add(new DroneMove(
                    orderNo,
                    movesPoints.get(i).lng(),
                    movesPoints.get(i).lat(),
                    angle,
                    movesPoints.get(i+1).lng(),
                    movesPoints.get(i+1).lat(),
                    ticks));
        }

        return droneMovesForFlightpath;
    }

    /**
     * Checks if the drone's battery has enough charge left to follow a given flightpath, i.e. pick-up and deliver an order.
     * @param flightpath a Flightpath used to deliver an order
     * @return true if the drone has enough moves left (not less than the length of the flightpath); false otherwise
     */
    public boolean enoughMovesLeft(Flightpath flightpath) {
        return flightpath.getDroneMovesDirections().size() * 2 + 2 <= movementModel.getMovesLeft();
    }

    /**
     * Makes appropriate changes in the Movement Model after delivering an order via a given flightpath, namely
     * reduces the drone's battery's charge (moves left).
     * @param flightpath a Flightpath used to deliver an order
     */
    public void makeDelivery(Flightpath flightpath) {
        int lengthInMoves = flightpath.getDroneMovesDirections().size() * 2 + 2;
        movementModel.adjustMovesLeft(lengthInMoves);
    }

    /**
     * Uses the Movement View to output the resulting data related to the drone's movement.
     * @param date the date on which the drone is delivering orders
     * @param droneMoves a list of calculated moves the drone will make on the given date
     */
    public void outputDroneMoves(String date, ArrayList<DroneMove> droneMoves) {
        movementView.createFlightpathFile(date, droneMoves);
        movementView.createDroneFile(date, droneMoves);
    }
}
