package elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages mutual exclusion with up and down arrays for each elevator.
 */
public class MutEx {
    private final List<Integer> upRequests = new ArrayList<>();
    private final List<Integer> downRequests = new ArrayList<>();
    private boolean endOne = false; // Signal to end operation
    private boolean endTwo = false;

    // Add an external request
    public synchronized void addExternalRequest(Request request) {
        if (request.getDirection().equalsIgnoreCase("up")) {
            upRequests.add(request.getFloor());
            Collections.sort(upRequests);
            System.out.println("External up request added: " + request.getFloor());
        } else {
            downRequests.add(request.getFloor());
            Collections.sort(downRequests, Collections.reverseOrder());
            System.out.println("External down request added: " + request.getFloor());
        }
        notifyAll();
    }

    // Add a passenger request
    public synchronized void addPassengerRequest(PassengerRequest request) {
        if (request.getElevatorId() == 1) {
            upRequests.add(request.getDestinationFloor());
            Collections.sort(upRequests);
            System.out.println("Passenger request added to Elevator One: " + request.getDestinationFloor());
        } else {
            downRequests.add(request.getDestinationFloor());
            Collections.sort(downRequests, Collections.reverseOrder());
            System.out.println("Passenger request added to Elevator Two: " + request.getDestinationFloor());
        }
        notifyAll();
    }

    // Get the next floor for Elevator One (up requests)
    public synchronized Integer getNextUpFloorForOne() throws InterruptedException {
        while (!endOne && upRequests.isEmpty()) {
            if (upRequests.isEmpty() && downRequests.isEmpty()) {
                return null;
            }
            wait();
        }
        return upRequests.isEmpty() ? null : upRequests.remove(0);
    }

    // Get the next floor for Elevator Two (down requests)
    public synchronized Integer getNextDownFloorForTwo() throws InterruptedException {
        while (!endTwo && downRequests.isEmpty()) {
            if (upRequests.isEmpty() && downRequests.isEmpty()) {
                return null;
            }
            wait();
        }
        return downRequests.isEmpty() ? null : downRequests.remove(0);
    }

    // Signal elevator one to end operation
    public synchronized void endOperationOne() {
        endOne = true;
        notifyAll(); // Wake up waiting threads
    }

    public synchronized boolean OneisEnd() {
        return endOne;
    }
    
    // Signal elevator two to end operation
    public synchronized void endOperationTwo() {
        endTwo = true;
        notifyAll(); // Wake up waiting threads
    }

    public synchronized boolean TwoisEnd() {
        return endTwo;
    }
}
