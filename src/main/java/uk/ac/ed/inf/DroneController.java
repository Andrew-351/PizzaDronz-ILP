package uk.ac.ed.inf;

import uk.ac.ed.inf.movement.MovementController;
import uk.ac.ed.inf.movement.model.DroneMove;
import uk.ac.ed.inf.movement.model.Flightpath;
import uk.ac.ed.inf.orders.OrdersController;
import uk.ac.ed.inf.orders.model.Order;
import java.util.ArrayList;

/**
 * The drone controller keeps track of and controls all aspects of the drone-based pizza delivery system.
 * It also has functionality which requires using different components of the system (sub-packages).
 */
public final class DroneController {
    private final MovementController movementController;
    private final OrdersController ordersController;
    private final DroneModel droneModel;


    /**
     * Creates an instance of DroneController class.
     * @param date a date on which orders are to be delivered
     * @param serverBaseUrl the base address of the server where to retrieve the data from
     * @throws NullPointerException if the ordersController throws NullPointerException after not having received
     * some data from the server
     */
    public DroneController(String date, String serverBaseUrl) throws NullPointerException {
        RestServerClient.setBaseUrl(serverBaseUrl);
        movementController = new MovementController();
        ordersController = new OrdersController(date);
        droneModel = new DroneModel(date);
    }

    /**
     * Launches the drone-based pizza delivery system.
     */
    public void startSession() {
        droneModel.setRestaurantsAndOrders(ordersController.getRestaurantsWithValidOrders());

        // For each restaurant from which there are orders on a given date, calculate the shortest flightpath.
        for (var restaurant : droneModel.getRestaurantsAndOrders().keySet()) {
            droneModel.recordFlightpathToRestaurant(
                    restaurant,
                    movementController.getFlightpathToRestaurant(restaurant.getLocation())
            );
        }

        droneModel.sortRestaurantsByDistance();
        deliverOrders();

        // Create output files with the information about deliveries and drone's flightpath for a given date.
        ordersController.outputDeliveries(droneModel.getDate());
        movementController.outputDroneMoves(droneModel.getDate(), droneModel.getDroneMoves());
    }

    /**
     * Delivers orders from the restaurants while the drone's battery is not exhausted.
     */
    private void deliverOrders() {
        // Go over the restaurants in the order "from the nearest to the farthest".
        for (var restaurant : droneModel.getOrderOfRestaurantsByDistance()) {
            // Flightpath to currently the nearest restaurant.
            Flightpath flightpath = droneModel.getFlightpathsToRestaurants().get(restaurant);

            // All valid but not yet delivered orders from the restaurant.
            ArrayList<Order> orders = droneModel.getRestaurantsAndOrders().get(restaurant);
            for (var order : orders) {
                // If the battery charge is insufficient to deliver the order, stay.
                if (!movementController.enoughMovesLeft(flightpath)) {
                    return;
                }
                
                ArrayList<DroneMove> moves = movementController.generateMovesForFlightpath(
                        flightpath, order.orderNo(), droneModel.getComputationStartTime());

                droneModel.recordDeliveryMoves(moves);
                movementController.makeDelivery(flightpath);
                ordersController.orderDelivered(order);
            }
        }
    }
}
