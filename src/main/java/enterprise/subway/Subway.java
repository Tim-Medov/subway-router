
package enterprise.subway;

import java.util.*;
import java.util.stream.Collectors;

public class Subway {

    private HashMap<Integer, Line> lines;
    private TreeSet<Station> stations;
    private TreeMap<Station, TreeSet<Station>> connections;

    public Subway() {
        this.lines = new HashMap<>();
        this.stations = new TreeSet<>();
        this.connections = new TreeMap<>();
    }

    public Line getLineNumber(int number) {
        return lines.get(number);
    }

    public Station getStation(String name) {

        for (Station station : stations) {

            if (station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }

        return null;
    }

    public Station getStation(String name, int lineNumber) {

        Station query = new Station(name, getLineNumber(lineNumber));
        Station station = stations.ceiling(query);

        return station.equals(query) ? station : null;
    }

    public Set<Station> getConnectedStations(Station station) {
        return connections.containsKey(station) ?
                connections.get(station) : new TreeSet<>();
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public void addLine(Line line) {
        lines.put(line.getNumber(), line);
    }

    public void addConnection(List<Station> stations) {

        for (Station station : stations) {

            if (!connections.containsKey(station)) {
                connections.put(station, new TreeSet<>());
            }

            TreeSet<Station> connectedStations = connections.get(station);

            connectedStations.addAll(stations.stream()
                    .filter(s -> !s.equals(station)).collect(Collectors.toList()));
        }
    }
}
