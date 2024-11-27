package elevator;


public class Request {
    private final int floor;
    private final String direction;

    public Request(int floor, String direction) {
        this.floor = floor;
        this.direction = direction;
    }
    
    // Retrive Floor Number
    public int getFloor() {
        return floor;
    }

    // Going up/ down
    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Floor " + floor + " (" + direction + ")";
    }
}

