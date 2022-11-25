package uk.ac.ed.inf.movement.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.*;
import uk.ac.ed.inf.movement.DroneMove;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FlightpathView {
    public void createFlightpathFile(String date, ArrayList<DroneMove> droneMoves) {
        try {
            String fileName = "flightpath-" + date + ".json";
            new ObjectMapper().writeValue(new FileOutputStream(fileName), droneMoves);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createDroneFile(String date, ArrayList<DroneMove> droneMoves) {
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
            String fileName = "drone-" + date + ".geojson";
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, false);
            fileOutputStream.write(featureCollection.toJson().getBytes());

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
