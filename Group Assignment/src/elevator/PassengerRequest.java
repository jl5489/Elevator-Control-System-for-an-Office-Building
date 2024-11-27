package elevator;

public class PassengerRequest {
    private final int elevatorId; // Which elevator the passenger is in (1 or 2)
    private final int destinationFloor;

    public PassengerRequest(int elevatorId, int destinationFloor) {
        this.elevatorId = elevatorId;
        this.destinationFloor = destinationFloor;
    }

    // Retrive elevator ID (0 or 1)
    public int getElevatorId() {
        return elevatorId;
    }

    // Get Floor Instruction
    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public String toString() {
        return "Passenger in Elevator " + elevatorId + " selected floor " + destinationFloor;
    }
}
