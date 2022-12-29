
package enterprise.subway;

import enterprise.Main;
import java.util.*;

public class Router {

    private Subway subway;
    private double averageDurationBetweenStations;
    private double averageDurationBetweenConnections;

    public Router(Subway subway) {
        this.subway = subway;
        this.averageDurationBetweenStations = 2.5;
        this.averageDurationBetweenConnections = 3.5;
    }

    private List<Station> getRouteOnTheLine(Station from, Station to) {

        if (!from.getLine().equals(to.getLine())) {
            return null;
        }

        List<Station> route = new ArrayList<>();
        List<Station> stations = from.getLine().getStations();

        int direction = 0;

        for (Station station : stations) {

            if (direction == 0) {

                if (station.equals(from)) {

                    direction = 1;

                } else if (station.equals(to)) {

                    direction = -1;

                }
            }

            if (direction != 0) {
                route.add(station);
            }

            if ((direction == 1 && station.equals(to)) ||
                    (direction == -1 && station.equals(from))) {
                break;
            }
        }

        if (direction == -1) {
            Collections.reverse(route);
        }

        return route;
    }

    private List<Station> getRouteViaConnectedLine(Station from, Station to) {

        Set<Station> fromConnected = subway.getConnectedStations(from);
        Set<Station> toConnected = subway.getConnectedStations(to);

        for (Station srcStation : fromConnected) {

            for (Station dstStation : toConnected) {

                if (srcStation.getLine().equals(dstStation.getLine())) {
                    return getRouteOnTheLine(srcStation, dstStation);
                }
            }
        }

        return null;
    }

    private boolean isConnected(Station station1, Station station2) {

        Set<Station> connected = subway.getConnectedStations(station1);

        return connected.contains(station2);
    }

    private List<Station> getRouteWithOneConnection(Station from, Station to) {

        if (from.getLine().equals(to.getLine())) {
            return null;
        }

        List<Station> route = new ArrayList<>();
        List<Station> fromLineStations = from.getLine().getStations();
        List<Station> toLineStations = to.getLine().getStations();

        for (Station srcStation : fromLineStations) {

            for (Station dstStation : toLineStations) {

                if (isConnected(srcStation, dstStation)) {

                    ArrayList<Station> way = new ArrayList<>();

                    way.addAll(getRouteOnTheLine(from, srcStation));
                    way.addAll(getRouteOnTheLine(dstStation, to));

                    if (route.isEmpty() || route.size() > way.size()) {
                        route.clear();
                        route.addAll(way);
                    }
                }
            }
        }

        return route;
    }

    private List<Station> getRouteWithTwoConnections(Station from, Station to) {

        if (from.getLine().equals(to.getLine())) {
            return null;
        }

        ArrayList<Station> route = new ArrayList<>();
        List<Station> fromLineStations = from.getLine().getStations();
        List<Station> toLineStations = to.getLine().getStations();

        for (Station srcStation : fromLineStations) {

            for (Station dstStation : toLineStations) {

                List<Station> connectedLineRoute =
                        getRouteViaConnectedLine(srcStation, dstStation);

                if (connectedLineRoute == null) {
                    continue;
                }

                ArrayList<Station> way = new ArrayList<>();

                way.addAll(getRouteOnTheLine(from, srcStation));
                way.addAll(connectedLineRoute);
                way.addAll(getRouteOnTheLine(dstStation, to));

                if (route.isEmpty() || route.size() > way.size()) {
                    route.clear();
                    route.addAll(way);
                }
            }
        }

        return route;
    }

    public List<Station> calculateShortestRoute(Station from, Station to) {

        List<Station> route = getRouteOnTheLine(from, to);

        if (route != null) {
            return route;
        }

        route = getRouteWithOneConnection(from, to);

        assert route != null;

        if (!route.isEmpty()) {
            return route;
        }

        route = getRouteWithTwoConnections(from, to);

        return route;
    }

    public double calculateDuration(List<Station> route) {

        double duration = 0;
        Station previousStation = null;

        for (int i = 0; i < route.size(); i++) {

            Station station = route.get(i);

            if (i > 0) {
                duration += previousStation.getLine().equals(station.getLine()) ?
                        averageDurationBetweenStations : averageDurationBetweenConnections;
            }

            previousStation = station;
        }

        return duration;
    }

    public Station requestStation(String announcement) {

        while (true) {

            System.out.println(announcement);

            String inputStation = Main.input.nextLine().trim();
            Station station = subway.getStation(inputStation);

            if (station != null) {
                return station;
            }

            System.out.println("Station not found...");
        }
    }

    public void printRoute(List<Station> route) {

        Station previousStation = null;

        for (Station station : route) {

            if (previousStation != null) {

                Line prevLine = previousStation.getLine();
                Line nextLine = station.getLine();

                if (!prevLine.equals(nextLine)) {
                    System.out.println("\tTransfer to the station: " +
                            station.getName() + " (" + nextLine.getName() + ")");
                }
            }

            System.out.println("\t" + station.getName());

            previousStation = station;
        }
    }
}
