
package enterprise;

import enterprise.subway.SubwayCreator;
import enterprise.subway.Router;
import enterprise.subway.Station;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        Router router = new Router(new SubwayCreator().create());

        System.out.println("\n--Program for calculating the route of the subway--\n");

        while (true) {

            Station from = router.requestStation("Enter the departure station:");
            Station to = router.requestStation("Enter destination station:");

            List<Station> route = router.calculateShortestRoute(from, to);

            System.out.println("Route:");

            router.printRoute(route);

            System.out.println("Duration: " +
                    router.calculateDuration(route) + " minutes");
        }
    }
}
