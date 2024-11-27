package elevator;

import java.util.Arrays;
import java.util.List;


public class Elevator {

    public static void main(String[] args) {
        ElevatorGUI gui = new ElevatorGUI();
        MutEx mainMutEx = new MutEx();
        ElevatorOne eOne = new ElevatorOne(mainMutEx, 0, gui);  // Start at Ground level
        ElevatorTwo eTwo = new ElevatorTwo(mainMutEx, 9, gui); // Start at level 9


        // Simulate external floor requests
        List<Request> externalRequests = Arrays.asList(
            new Request(3, "up"),
            new Request(5, "up"),
            new Request(7, "up"),
            new Request(8, "down"),
            new Request(4, "down")
        );

        for (Request request : externalRequests) {
            mainMutEx.addExternalRequest(request);
        }

        // Simulate passengers inside the elevator selecting floors
        List<PassengerRequest> passengerRequests = Arrays.asList(
            new PassengerRequest(1, 8),  // Passenger in Elevator One selects floor 8
            new PassengerRequest(2, 6),  // Passenger in Elevator Two selects floor 6
            new PassengerRequest(2, 2),  // Passenger in Elevator Two selects floor 2
            new PassengerRequest(1, 4)   // Passenger in Elevator One selects floor 4
        );

        for (PassengerRequest request : passengerRequests) {
            mainMutEx.addPassengerRequest(request);
        }
        
        eOne.start();
        eTwo.start();
    }
}
