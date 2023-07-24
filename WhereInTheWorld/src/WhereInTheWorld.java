// Author: Luke Piper 3648114
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Where In The World of CS is a program that reads a users input, and converts
 * their input into a GeoJSON file that can be visualised on a map.
 * If the user inputs an invalid location, the program will print "Unable to
 * process: " followed by the invalid input.
 */
public class WhereInTheWorld {
    /**
     * Main method that reads the users input and converts it into a GeoJSON file
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Have file chooser open from current directory
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        chooser.setFileFilter(filter);
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);
        Scanner scan = null;
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                scan = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            System.exit(0);
        }
        // Create a JSONObject to store the GeoJSON data
        JSONObject geoJSON = new JSONObject();
        JSONArray features = new JSONArray();
        // Read lines from user in form of latitutde,longitude
        while (scan.hasNextLine()) {
            String input = scan.nextLine();
            // Parse input into lat and lon
            double[] coords = parseInput(input);
            // If the input is invalid, skip it
            if (coords == null)
                continue;
            System.out.println(coords[0] + ", " + coords[1]);

            // Parse the latitude and longitude from the input line
            double longitude = coords[0];
            double latitude = coords[1];
            String name = getLabel(input);

            // Create a JSONObject to store the feature
            JSONObject feature = new JSONObject();
            feature.put("type", "Feature");

            // Create a properties object for our location
            JSONObject properties = new JSONObject();
            properties.put("name", name);
            properties.put("markerColor", "blue");
            properties.put("type", "office");

            // Create a geometry object for our location
            JSONObject geometry = new JSONObject();
            geometry.put("type", "Point");
            geometry.put("coordinates", new double[] { longitude, latitude });
            // Add geometry and properties to the feature
            feature.put("geometry", geometry);
            feature.put("properties", properties);
            // Add the feature to the features array
            features.put(feature);
        }
        // Add the features array to the GeoJSON object
        geoJSON.put("type", "FeatureCollection");
        geoJSON.put("features", features);

        scan.close();

        // Call methods to write to JSON file and open in geojson.io
        writeJSONToFile(geoJSON);
        openGeoJSON(geoJSON);
    }

    /**
     * Gets the label from the input if it exists
     * @param input
     * @return
     */
    public static String getLabel(String input) {
        // If label is in ()
        if(input.contains("(")) {
            int startIndex = input.indexOf("(");
            int endIndex = input.indexOf(")");
            return input.substring(startIndex + 1, endIndex);
        }
        // If label is after a space
        int lastIndex = input.lastIndexOf(" ");
        if (lastIndex != -1) {
            if (input.substring(lastIndex + 1).matches("[a-zA-Z]+")) {
                return input.substring(lastIndex + 1);
            }
        }
        return "";
    }
    

    /**
     * Parse and validates the input into all accepted forms
     * Regex information:
     * -? = optional negative sign
     * \d+ = one or more digits
     * \.? = optional decimal point
     * \d* = zero or more digits after decimal point
     * \s* = zero or more whitespace characters
     * \d{6} = 6 digits
     * 
     * @param input
     * @return double[] coords, coords[0] = latitude, coords[1] = longitude
     */
    public static double[] parseInput(String input) {
        Matcher matcher;
        String label = "(?:\s(.*))?";
        String whitespace = "\\s*";
        String comma = ",?";
        String digit = "(-?\\d+\\.?\\d*)";
        String marker = "([NESW]?)?";

        // Special DM - example 40 d 26 m N, 79 d 58 m W Pittsburgh
       matcher = Pattern.compile("(-?\\d+)" + "\\s*d" + whitespace + "(\\d+)\\s*m" + whitespace + "([NESW])?\\s*"
        + ",?" + "\\s*(-?\\d+)" + "\\s*d" + whitespace + "(\\d+)\\s*m" + whitespace 
        + "([NESW])?\\s*" + label).matcher(input);
        if(matcher.matches()) {
            // Convert DM to lat and lon
            double coords[] = convertDM(matcher);
            return checkRange(coords[0], coords[1], input);
        }

        // Special DMS - example: 40 d 26 m 46 s N, 79 d 58 m 56 s W Pittsburgh
        matcher = Pattern.compile("(-?\\d+)" + "\\s*d" + whitespace + "(\\d+)\\s*m" + whitespace + "(\\d*.?\\d*)?\\s*s\\s*([NESW])?\\s*"
        + ",?" + "\\s*(-?\\d+)" + "\\s*d" + whitespace + "(\\d+)\\s*m" + whitespace 
        + "(\\d*.?\\d*)?\\s*s\\s*([NESW])?\\s*" + label).matcher(input);
        if(matcher.matches()) {
            // Convert DMS to lat and lon
            double coords[] = convertDMS(matcher);
            return checkRange(coords[0], coords[1], input);
        }

        // Standard form
        matcher = Pattern.compile(digit + whitespace + marker + whitespace + comma + whitespace + digit
                + whitespace + marker + whitespace + label).matcher(input);
        if (matcher.matches()) {
            // Convert DMS to lat and lon
            double coords[] = convertStandardForm(matcher);
            return checkRange(coords[0], coords[1], input);
        }

        // DMS
        matcher = Pattern.compile("(-?\\d+)" + "°?" + whitespace + "(\\d+)(?:'|′)?" + whitespace + "(\\d+\\.?\\d*)\"\\s*([NESW])?\\s*"
        + ",?" + "\\s*(-?\\d+)" + "°?" + whitespace + "(\\d+)(?:'|′)?" + whitespace
        + "(\\d+\\.?\\d*)\"\\s*([NESW])?\\s*" + label).matcher(input);
        if (matcher.matches()) {
            // Convert DMS to lat and lon
            double coords[] = convertDMS(matcher);
            return checkRange(coords[0], coords[1], input);
        }

        // DM
        matcher = Pattern.compile("(-?\\d+)" + "°?" + whitespace + "(\\d*.?\\d*)(?:'|′)?" + whitespace + "([NESW])?\\s*" + ",?"
                + "\\s*(-?\\d+)" + "°?" + whitespace + "(\\d*.?\\d*)(?:'|′)?" + whitespace + "\\s*([NESW])?\\s*" + label).matcher(input);
        if (matcher.matches()) {
            // Convert DM to lat and lon
            double coords[] = convertDM(matcher);
            return checkRange(coords[0], coords[1], input);
        }

        // If none of the patterns match, throw an exception
        System.err.println("Unable to process: " + input);
        return null;
    }

    public static double[] convertStandardForm(Matcher matcher) {
        // Grab lat and lon
        double lat = Double.parseDouble(matcher.group(1));
        double lon = Double.parseDouble(matcher.group(3));
        // Grab directional markers
        String latMarker = matcher.group(2);
        String lonMarker = matcher.group(4);
        latMarker.toUpperCase();
        lonMarker.toUpperCase();

        double coords[] = new double[2];
        // ArrayList for comparing
        ArrayList<String> ns = new ArrayList<String>(List.of("N", "S"));
        ArrayList<String> ew = new ArrayList<String>(List.of("E", "W"));

        // Both markers present
        if (!latMarker.equals("") && !lonMarker.equals("")) {
            // Find what marker is in what arraylist
            if (ns.contains(latMarker) && ew.contains(lonMarker)) {
                // If lat is S, make it negative
                if (latMarker.equals("S")) {
                    if (lat > 0)
                        lat = -lat;
                }
                // If lon is W, make it negative
                if (lonMarker.equals("W")) {
                    if (lon > 0)
                        lon = -lon;
                }
                // Add lat and lon coords to array
                coords[0] = lat;
                coords[1] = lon;
                return coords;
                // If markers are flipped
            } else if (ew.contains(latMarker) && ns.contains(lonMarker)) {
                // Flip values of lat and lon
                lat = Double.parseDouble(matcher.group(3));
                lon = Double.parseDouble(matcher.group(1));
                // If lat is W, make it negative
                if (latMarker.equals("W")) {
                    if (lon > 0)
                        lon = -lon;
                }
                // If lon is S, make it negative
                if (lonMarker.equals("S")) {
                    if (lat > 0)
                        lat = -lat;
                }
                // Add lat and lon coords to array
                coords[0] = lat;
                coords[1] = lon;
                return coords;
            }
        } else if (!latMarker.equals("")) {
            // Only lat marker present
            if (latMarker.equals("E") || latMarker.equals("W")) {
                // Flip values of lat and lon
                lat = Double.parseDouble(matcher.group(3));
                lon = Double.parseDouble(matcher.group(1));
                // If lat is W, make it negative
                if (latMarker.equals("W")) {
                    if (lon > 0)
                        lon = -lon;
                }
            }
            // Make lat negative if marker S
            if (latMarker.equals("S")) {
                if (lat > 0)
                    lat = -lat;
            }
            // Add lat and lon coords to array
            coords[0] = lat;
            coords[1] = lon;
            return coords;
        } else if (!lonMarker.equals("")) {
            // Only lon marker present
            if (lonMarker.equals("N") || lonMarker.equals("S")) {
                // Flip values of lat and lon
                lat = Double.parseDouble(matcher.group(3));
                lon = Double.parseDouble(matcher.group(1));
                // If lat is W, make it negative
                if (lonMarker.equals("S")) {
                    if (lat > 0)
                        lat = -lat;
                }
            }
            if (lonMarker.equals("W")) {
                if (lon > 0)
                    lon = -lon;
            }
            // Add lat and lon coords to array
            coords[0] = lat;
            coords[1] = lon;
            return coords;
        } else {
            // No markers present
            // If one of the values is out of range, flip lat and lon
            if (Math.abs(lat) > 90 || Math.abs(lon) > 180) {
                double temp = lat;
                lat = lon;
                lon = temp;
            }
            // Add lat and lon coords to array
            coords[0] = lat;
            coords[1] = lon;
            return coords;
        }
        return coords;
    }

    /**
     * Convert DMS to lat and lon
     * 
     * @param matcher
     * @return
     */
    public static double[] convertDMS(Matcher matcher) {
        // For lat
        double degrees = Double.parseDouble(matcher.group(1));
        double minutes = Double.parseDouble(matcher.group(2));
        double seconds = Double.parseDouble(matcher.group(3));
        // Solve lat
        double lat = degrees + (minutes / 60) + (seconds / 3600);

        // For lon
        degrees = Double.parseDouble(matcher.group(5));
        minutes = Double.parseDouble(matcher.group(6));
        seconds = Double.parseDouble(matcher.group(7));
        // Solve lon
        double lon = degrees + (minutes / 60) + (seconds / 3600);


        // Grab directional markers
        String latMarker = matcher.group(4);
        String lonMarker = matcher.group(8);
        // Check if they are both there
        boolean bothThere = matcher.group(4) != null && matcher.group(8) != null;

        // If both markers are present
        if(bothThere) {
            latMarker = latMarker.toLowerCase();
            lonMarker = lonMarker.toLowerCase();
            // Check they are in correct lat lon order
            if((latMarker.equals("n") || latMarker.equals("s")) && (lonMarker.equals("e") || lonMarker.equals("w"))) {
                // Make values negative if needed
                if(latMarker.equals("s")) {
                    if(lat > 0) {
                        lat = -lat;
                    }
                }
                if(lonMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                }
            // Check they are in flipped order
            } else if ((latMarker.equals("e") || latMarker.equals("w")) && (lonMarker.equals("n") || lonMarker.equals("s"))) {
                // Flip values of lat and lon
                double temp = lat;
                lat = lon;
                lon = temp;
                // Make values negative if needed
                if(latMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                    if(lonMarker.equals("s")) {
                        if(lat > 0) {
                            lat = -lat;
                        }
                    }
                }
            }
            // Only lat marker is present
        } else if (!latMarker.equals(null)) {
            latMarker = latMarker.toLowerCase();
            // Check its valid lat marker
            if(latMarker.equals("n") || latMarker.equals("s")) {
                if(latMarker.equals("s")) {
                    if(lat > 0) {
                        lat = -lat;
                    }
                }
                // Flip values
            } else if (latMarker.equals("e") || latMarker.equals("w")) {
                // Flip values of lat and lon
                double temp = lat;
                lat = lon;
                lon = temp;
                if(latMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                }
            }
            // Only lat marker present
        } else if (!lonMarker.equals(null)) {
            lonMarker = lonMarker.toLowerCase();
            // Check its valid lon marker
            if(lonMarker.equals("e") || lonMarker.equals("w")) {
                if(lonMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                }
                // Flip values if not valid lon marker
            } else if (lonMarker.equals("n") || lonMarker.equals("s")) {
                // Flip values of lat and lon
                double temp = lat;
                lat = lon;
                lon = temp;
                if(lonMarker.equals("s")) {
                    if(lat > 0) {
                        lat = -lat;
                    }
                }
            }
        } else {
            // No markers present
            // If one of the values is out of range, flip lat and lon
            if (Math.abs(lat) > 90 || Math.abs(lon) > 180) {
                double temp = lat;
                lat = lon;
                lon = temp;
            }
        }

        // Return lat and lon
        double[] coords = new double[2];
        coords[0] = lat;
        coords[1] = lon;
        return coords;
    }

    /**
     * Convert DM to lat and lon
     * 
     * @param matcher
     * @return
     */
    public static double[] convertDM(Matcher matcher) {
        // For lat
        double degrees = Double.parseDouble(matcher.group(1));
        double minutes = Double.parseDouble(matcher.group(2));
        // Solve lat
        double lat = degrees + (minutes / 60);

        // For lon
        degrees = Double.parseDouble(matcher.group(4));
        minutes = Double.parseDouble(matcher.group(5));
        // Solve lon
        double lon = degrees + (minutes / 60);
        // Grab directional markers
        String latMarker = matcher.group(3);
        String lonMarker = matcher.group(6);
        // Check if they are both there
        boolean bothThere = matcher.group(3) != null && matcher.group(6) != null;

        // If both markers are present
        if(bothThere) {
            latMarker = latMarker.toLowerCase();
            lonMarker = lonMarker.toLowerCase();
            // Check they are in correct lat lon order
            if((latMarker.equals("n") || latMarker.equals("s")) && (lonMarker.equals("e") || lonMarker.equals("w"))) {
                // Make values negative if needed
                if(latMarker.equals("s")) {
                    if(lat > 0) {
                        lat = -lat;
                    }
                }
                if(lonMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                }
            // Check they are in flipped order
            } else if ((latMarker.equals("e") || latMarker.equals("w")) && (lonMarker.equals("n") || lonMarker.equals("s"))) {
                // Flip values of lat and lon
                double temp = lat;
                lat = lon;
                lon = temp;
                // Make values negative if needed
                if(latMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                    if(lonMarker.equals("s")) {
                        if(lat > 0) {
                            lat = -lat;
                        }
                    }
                }
            }
            // Only lat marker is present
        } else if (!latMarker.equals(null)) {
            latMarker = latMarker.toLowerCase();
            // Check its valid lat marker
            if(latMarker.equals("n") || latMarker.equals("s")) {
                if(latMarker.equals("s")) {
                    if(lat > 0) {
                        lat = -lat;
                    }
                }
                // Flip values
            } else if (latMarker.equals("e") || latMarker.equals("w")) {
                // Flip values of lat and lon
                double temp = lat;
                lat = lon;
                lon = temp;
                if(latMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                }
            }
            // Only lat marker present
        } else if (!lonMarker.equals(null)) {
            lonMarker = lonMarker.toLowerCase();
            // Check its valid lon marker
            if(lonMarker.equals("e") || lonMarker.equals("w")) {
                if(lonMarker.equals("w")) {
                    if(lon > 0) {
                        lon = -lon;
                    }
                }
                // Flip values if not valid lon marker
            } else if (lonMarker.equals("n") || lonMarker.equals("s")) {
                // Flip values of lat and lon
                double temp = lat;
                lat = lon;
                lon = temp;
                if(lonMarker.equals("s")) {
                    if(lat > 0) {
                        lat = -lat;
                    }
                }
            }
        } else {
            // No markers present
            // If one of the values is out of range, flip lat and lon
            if (Math.abs(lat) > 90 || Math.abs(lon) > 180) {
                double temp = lat;
                lat = lon;
                lon = temp;
            }
        }

        // Return lat and lon
        double[] coords = new double[2];
        coords[0] = lat;
        coords[1] = lon;
        return coords;
    }

    /**
     * Writes the GeoJSON to a file called output.json
     * 
     * @param geoJSON
     */
    public static void writeJSONToFile(JSONObject geoJSON) {
        // Write the GeoJSON object to a file
        try (FileWriter file = new FileWriter("output.json")) {
            file.write(geoJSON.toString());
            System.out.println("GeoJSON data written to output.json");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    /**
     * Opens the GeoJSON in geojson.io
     * 
     * @param geoJSON
     */
    public static void openGeoJSON(JSONObject geoJSON) {
        // Construct the url to geojson.io
        String geoJsonData = geoJSON.toString();
        String encodedGeoJsonData = "";
        try {
            encodedGeoJsonData = URLEncoder.encode(geoJsonData, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://geojson.io/#data=data:application/json," + encodedGeoJsonData;

        // Open the GeoJSON in geojson.io
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the coordinates are within the accepted range
     * 
     * @param coords
     * @param input
     * @return coords if true, null if false
     */
    public static double[] checkRange(double lat, double lon, String input) {
        // Check if the coordinates are within the accepted range
        if (lat > 90 || lat < -90 || lon > 180 || lon < -180) {
            // If not, print an error message and return null
            System.err.println("Unable to process: " + input);
            return null;
        }
        // Else return the coordinates
        double[] coords = new double[2];
        // Flip lat lon for geoJSON format
        coords[0] = lon;
        coords[1] = lat;
        return coords;
    }
}