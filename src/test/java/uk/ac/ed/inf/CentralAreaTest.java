package uk.ac.ed.inf;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.model.CentralArea;
import uk.ac.ed.inf.model.LngLat;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for Central Area class.
 */

public class CentralAreaTest {
    private CentralArea centralArea;

    @Before
    public void setUp() {
        centralArea = CentralArea.getCentralAreaFromRestServer();
    }

    @Test
    public void getVertexCoordinatesTest() {
        LngLat[] coordinates = new LngLat[4];
        coordinates[0] = new LngLat(-3.192473, 55.946233);
        coordinates[1] = new LngLat(-3.192473, 55.942617);
        coordinates[2] = new LngLat(-3.184319, 55.942617);
        coordinates[3] = new LngLat(-3.184319, 55.946233);

        assertArrayEquals(coordinates, centralArea.vertexCoordinates());
    }

}
