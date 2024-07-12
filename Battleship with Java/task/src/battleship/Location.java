package battleship;

public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(String location) {
        this.y = location.substring(0, 1).charAt(0) - 'A';
        this.x = Integer.parseInt(location.substring(1)) - 1;
    }

    public boolean hasDirectLine(Location otherLocation) {
        return this.getX() == otherLocation.getX() || this.getY() == otherLocation.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Location[] subLocations(Location otherLocation) {
        int maxX = Math.max(this.getX(), otherLocation.getX());
        int minX = Math.min(this.getX(), otherLocation.getX());
        int xLength = maxX - minX + 1;

        int maxY = Math.max(this.getY(), otherLocation.getY());
        int minY = Math.min(this.getY(), otherLocation.getY());
        int yLength = maxY - minY + 1;

        int distance = Math.max(xLength, yLength);

        Location[] locations = new Location[distance];
        int counter = 0;

        if (xLength > yLength) {
            for (int i = minX; i <= maxX; i++) {
                locations[counter] = new Location(i, minY);
                counter++;
            }
        } else {
            for (int i = minY; i <= maxY; i++) {
                locations[counter] = new Location(minX, i);
                counter++;
            }
        }

        return locations;
    }

    public String getUserLocation() {
        return "" + ((char) ('A' + y)) + (x + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
