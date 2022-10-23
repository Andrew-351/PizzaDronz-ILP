package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.model.CentralArea;
import uk.ac.ed.inf.model.CompassDirection;
import uk.ac.ed.inf.model.LngLat;
import uk.ac.ed.inf.model.MovementConstants;
import static org.junit.Assert.*;

/**
 * Unit tests for LngLat class.
 */

public class LngLatTest {
    private final CentralArea centralArea = CentralArea.getCentralAreaFromRestServer();
    private final LngLat pointInCentralArea = new LngLat(-3.19, 55.944);
    private final LngLat pointNotInCentralArea = new LngLat(-3.1924731, 55.946233);
    private final double DELTA = 1e-12;

    @Test
    public void distanceToTestSamePoint() {
        LngLat lngLat = new LngLat(-3.19, 55.944);
        assertEquals(0, pointInCentralArea.distanceTo(lngLat), DELTA);
    }

    @Test
    public void distanceToTestDifferentPoint() {
        LngLat lngLat = new LngLat(-3.2, 55.9);
        assertEquals(0.04512205669071391076367, pointInCentralArea.distanceTo(lngLat), DELTA);
    }

    @Test
    public void inCentralAreaNo() {
        assertFalse(pointNotInCentralArea.inCentralArea(centralArea));
    }

    @Test
    public void inCentralAreaYes() {
        assertTrue(pointInCentralArea.inCentralArea(centralArea));
    }

    @Test
    public void inCentralAreaYesOnTheEdge() {
        LngLat point = new LngLat(-3.192473, 55.945);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void inCentralAreaYesInTheCorner() {
        LngLat point = new LngLat(-3.184319, 55.942617);
        assertTrue(point.inCentralArea(centralArea));
    }

    @Test
    public void closeToTestNo() {
        LngLat point = new LngLat(-3.184319, 55.946233);
        assertFalse(pointNotInCentralArea.closeTo(point));
    }

    @Test
    public void closeToTestYes() {
        LngLat point = new LngLat(-3.192473, 55.946233);
        assertTrue(pointNotInCentralArea.closeTo(point));
    }

    @Test
    public void nextPositionTestHover() {
        assertEquals(pointInCentralArea, pointInCentralArea.nextPosition(CompassDirection.HOVER));
    }

    @Test
    public void nextPositionTestSouth() {
        assertEquals(new LngLat(pointInCentralArea.lng(), pointInCentralArea.lat() - MovementConstants.MOVE_LENGTH), pointInCentralArea.nextPosition(CompassDirection.S));
    }

    @Test
    public void nextPositionTestNorthNorthEast() {
        assertEquals(new LngLat(-3.189942597485145236534, 55.94413858192987669301), pointInCentralArea.nextPosition(CompassDirection.NNE));
    }
}
