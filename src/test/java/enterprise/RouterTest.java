
package enterprise;

import enterprise.subway.Line;
import enterprise.subway.Router;
import enterprise.subway.Station;
import enterprise.subway.Subway;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

public class RouterTest extends TestCase {

    private Subway subway = new Subway();
    private List<Station> route = new ArrayList<>();

    public void setUp() throws Exception {

        Line animals = new Line(1, "Animals");
        Station dog = new Station("Dog", animals);
        Station cat = new Station("Cat", animals);
        Station cow = new Station("Cow", animals);

        animals.addStation(dog);
        animals.addStation(cat);
        animals.addStation(cow);

        Line cars = new Line(2, "Cars");
        Station dodge = new Station("Dodge", cars);
        Station ford = new Station("Ford", cars);
        Station toyota = new Station("Toyota", cars);

        cars.addStation(dodge);
        cars.addStation(ford);
        cars.addStation(toyota);

        Line fruits = new Line(3, "Fruits");
        Station banana = new Station("Banana", fruits);
        Station orange = new Station("Orange", fruits);
        Station apple = new Station("Apple", fruits);
        Station pear = new Station("Pear", fruits);

        fruits.addStation(banana);
        fruits.addStation(orange);
        fruits.addStation(apple);
        fruits.addStation(pear);

        subway.addLine(animals);
        subway.addLine(cars);
        subway.addLine(fruits);

        subway.addStation(dog);
        subway.addStation(cat);
        subway.addStation(cow);

        subway.addStation(dodge);
        subway.addStation(ford);
        subway.addStation(toyota);

        subway.addStation(banana);
        subway.addStation(orange);
        subway.addStation(apple);
        subway.addStation(pear);

        List<Station> connectionCatOrange = new ArrayList<>();
        connectionCatOrange.add(subway.getStation("Cat"));
        connectionCatOrange.add(subway.getStation("Orange"));
        subway.addConnection(connectionCatOrange);

        List<Station> connectionFordApple = new ArrayList<>();
        connectionFordApple.add(subway.getStation("Ford"));
        connectionFordApple.add(subway.getStation("Apple"));
        subway.addConnection(connectionFordApple);

        route.add(dog);
        route.add(cat);
        route.add(orange);
        route.add(apple);
        route.add(ford);
        route.add(toyota);

        /*
            The subway scheme:
                                 Line "Fruits"
                                        1
                                        |
                                        |
            Line "Animals"     A-------B-2-------C
                                        |
                                        |
                                        |
            Line "Cars"        X-------Y-3-------Z
                                        |
                                        |
                                        4

            Stations of the line "Animals": Dog = A, Cat = B, Cow = C
            Stations of the line "Cars": Dodge = X, Ford = Y, Toyota = Z
            Stations of the line "Fruits": Banana = 1, Orange = 2, Apple = 3, Pear = 4

            Test route with two transfers was laid from the Dog(A) station to the Toyota(Z) station
        */

        super.setUp();
    }

    public void testCalculateShortestRoute() {

        Router router = new Router(subway);

        List<Station> actual = router.calculateShortestRoute(subway.getStation("Dog"),
                subway.getStation("Toyota"));
        List<Station> expected = route;

        assertEquals(expected, actual);
    }

    public void testCalculateDuration() {

        Router router = new Router(subway);

        double actual = router.calculateDuration(route);
        double expected = 14.5;

        assertEquals(expected, actual);
    }
}
