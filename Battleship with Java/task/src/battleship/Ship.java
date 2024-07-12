package battleship;

import java.util.Arrays;

public class Ship {
    private Location[] locations;
    private int timesHit = 0;

    public Ship(Location start, Location end) {
        if (!start.hasDirectLine(end)) {
            throw new IllegalArgumentException();
        }
        this.locations = start.subLocations(end);

//        System.out.println(Arrays.asList(locations));
    }

    public Location[] getLocations() {
        return locations;
    }

    public int getLength() {
        return locations.length;
    }

    public Location getStart() {
        return locations[0];
    }

    public Location getEnd() {
        return locations[locations.length - 1];
    }

    public void hit() {
        timesHit++;
    }

    public boolean isSunk() {
        return timesHit >= locations.length;
    }

    public boolean isRow() {
        return locations[0].getY() == locations[locations.length - 1].getY();
    }

    public String getParts() {
        StringBuilder builder = new StringBuilder();

        for (Location location : locations) {
            builder.append(location.getUserLocation());
            builder.append(" ");
        }

        return builder.toString().trim();
    }
}
