package uk.ac.ed.inf;

import java.util.HashMap;
import org.junit.Test;
import uk.ac.ed.inf.model.Menu;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for Menu class.
 */

public class MenuTest {
    @Test
    public void getPizzasTest() {
        HashMap<String, Object>[] pizzas = new HashMap[2];
        Menu.Pizza[] pizzasExpected = new Menu.Pizza[2];
        for (int i = 0; i < 2; i++) {
            pizzas[i] = new HashMap<>();
            pizzas[i].put("name", "Pizza" + i);
            pizzas[i].put("priceInPence", 1000);
            pizzasExpected[i] = new Menu.Pizza("Pizza" + i, 1000);

        }
        Menu menu = new Menu(pizzas);

        assertArrayEquals(pizzasExpected, menu.getPizzas());
    }
}
