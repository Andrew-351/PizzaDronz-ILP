package uk.ac.ed.inf.movement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.*;
import uk.ac.ed.inf.movement.model.DroneMove;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Movement View class is responsible for outputting resulting data related to the drone's movement.
 */

public final class MovementView {
    /**
     * Creates/overwrites a file containing an array of JSON records which represent all moves the drone will make
     * on the given date.
     * @param date the date on which the drone is delivering orders
     * @param droneMoves a list of calculated moves the drone will make on the given date
     */
    public void createFlightpathFile(String date, ArrayList<DroneMove> droneMoves) {
        String fileName = "flightpath-%s.json".formatted(date);
        try {
            new ObjectMapper().writeValue(new FileOutputStream(fileName), droneMoves);
        } catch (IOException e) {
            System.err.printf("Could not write %s.%n", fileName);
        }
    }

    /**
     * Creates/overwrites a GeoJSON file containing the drone's coordinates for each move it makes throughout the day.
     * @param date the date on which the drone is delivering orders
     * @param droneMoves a list of calculated moves the drone will make on the given date
     */
    public void createDroneFile(String date, ArrayList<DroneMove> droneMoves) {
        String fileName = "drone-%s.geojson".formatted(date);
        try {
            ArrayList<Point> coordinates = new ArrayList<>();
            if (droneMoves != null) {
                coordinates.add(Point.fromLngLat(droneMoves.get(0).fromLongitude(),
                        droneMoves.get(0).fromLatitude()));
                for (var move : droneMoves) {
                    coordinates.add(Point.fromLngLat(move.toLongitude(), move.toLatitude()));
                }
            }
            LineString lineString = LineString.fromLngLats(coordinates);
            FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString));
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName, false)) {
                fileOutputStream.write(featureCollection.toJson().getBytes());
            }
        } catch (IOException e) {
            System.err.printf("Could not write %s.%n", fileName);
        }
    }
}
