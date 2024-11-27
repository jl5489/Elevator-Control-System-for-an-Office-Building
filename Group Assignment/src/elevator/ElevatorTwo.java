package elevator;

public class ElevatorTwo extends Thread {
    private final MutEx mutex;
    private int currentFloor;
    private final ElevatorGUI gui;

    public ElevatorTwo(MutEx m, int startingFloor, ElevatorGUI gui) {
        this.mutex = m;
        this.currentFloor = startingFloor;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (mutex.TwoisEnd()) {
                    moveToFloor(9); // Return to default floor
                    System.out.println("Elevator Two is shutting down at default floor 9.");
                    break;
                }

                Integer nextFloor = mutex.getNextDownFloorForTwo();
                if (nextFloor != null) {
                    moveToFloor(nextFloor);
                }else{
                    mutex.endOperationTwo();}
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveToFloor(int targetFloor) throws InterruptedException {
        System.out.println("Elevator Two moving from " + currentFloor + " to " + targetFloor);
        while (currentFloor != targetFloor) {
            gui.updateElevator(2, currentFloor, targetFloor);

            if (currentFloor < targetFloor) {
                currentFloor++; // Move up
            } else {
                currentFloor--; // Move down
            }
            
            Thread.sleep(500); // Delay 0.5 seconds per floor
        }
        
        currentFloor = targetFloor;
        gui.updateElevator(2, currentFloor, targetFloor);
        System.out.println("Elevator Two reached floor " + currentFloor);
        Thread.sleep(3000); // Stop for 3 seconds at the destination
    }
}
