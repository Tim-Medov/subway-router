
package enterprise.subway;

import enterprise.Main;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubwayCreator {

    private Subway subway;

    public SubwayCreator() {
        this.subway = new Subway();
    }

    public Subway create() {

        parseSubway();

        return subway;
    }

    private String jsonToString() {

        String json = "";

        try {

            InputStream input = Main.class.getResourceAsStream("/subwayJsonMap.json");
            assert input != null;
            json = IOUtils.toString(input, StandardCharsets.UTF_8);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return json;
    }

    private void parseLines(JSONArray linesArray) {

        linesArray.forEach(lineObject -> {

            JSONObject lineJsonObject = (JSONObject) lineObject;

            Line line = new Line(Math.toIntExact((Long) lineJsonObject.get("number")),
                    (String) lineJsonObject.get("name"));

            subway.addLine(line);
        });
    }

    private void parseStations(JSONObject stationsObject) {

        stationsObject.keySet().forEach(lineNumberObject -> {

            int lineNumber = Integer.parseInt((String) lineNumberObject);

            Line line = subway.getLineNumber(lineNumber);
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);

            stationsArray.forEach(stationObject -> {

                Station station = new Station((String) stationObject, line);

                subway.addStation(station);
                line.addStation(station);
            });
        });
    }

    private void parseConnections(JSONArray connectionsArray) {

        connectionsArray.forEach(connectionObject -> {

            JSONArray connection = (JSONArray) connectionObject;
            List<Station> connectionStations = new ArrayList<>();

            connection.forEach(item -> {

                JSONObject itemObject = (JSONObject) item;
                int lineNumber = ((Long) itemObject.get("line")).intValue();
                String stationName = (String) itemObject.get("station");
                Station station = subway.getStation(stationName, lineNumber);

                if (station == null) {
                    throw new IllegalArgumentException("Station " +
                            stationName + " on line " + lineNumber + " not found");
                }

                connectionStations.add(station);
            });

            subway.addConnection(connectionStations);
        });
    }

    private void parseSubway() {

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(jsonToString());

            JSONArray linesArray = (JSONArray) jsonData.get("lines");
            parseLines(linesArray);

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            parseStations(stationsObject);

            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            parseConnections(connectionsArray);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
