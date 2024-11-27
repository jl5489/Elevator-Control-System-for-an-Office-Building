import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Represents a request made by a user for an elevator
class Request {
    int floor;
    boolean goingUp;

    public Request(int floor, boolean goingUp) {
        this.floor = floor;
        this.goingUp = goingUp;
    }
}

// Manages and assigns requests to elevators
class RequestManager {
    private Queue<Request> requests = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public void addRequest(Request request) {
        lock.lock();
        try {
            requests.add(request);
            System.out.println("Request added for floor: " + request.floor + ", Going up: " + request.goingUp);
        } finally {
            lock.unlock();
        }
    }

    public synchronized Request getRequestForElevator(int currentFloor, boolean isMovingUp) {
        lock.lock();
        try {
            for (Request request : requests) {
                if ((isMovingUp && request.floor >= currentFloor) || (!isMovingUp && request.floor <= currentFloor)) {
                    requests.remove(request);
                    return request;
                }
            }
        } finally {
            lock.unlock();
        }
        return null;
    }
}

// Elevator represents an elevator that moves between floors
class Elevator implements Runnable {
    private int currentFloor = 0;
    private final int id;
    private final RequestManager requestManager;
    private boolean movingUp = true;
    private final Lock elevatorLock = new ReentrantLock();
    private ElevatorPanel panel;
    private boolean idle = true;

    public Elevator(int id, RequestManager requestManager, ElevatorPanel panel) {
        this.id = id;
        this.requestManager = requestManager;
        this.panel = panel;
    }

    private void moveToFloor(int targetFloor) {
        idle = false;
        while (currentFloor != targetFloor) {
            if (movingUp) currentFloor++;
            else currentFloor--;

            System.out.println("Elevator " + id + " at floor " + currentFloor);
            panel.updateElevatorPosition(id, currentFloor);
            try {
                Thread.sleep(500); // Simulate time taken to move one floor
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Elevator " + id + " arrived at floor " + targetFloor);
    }

    @Override
    public void run() {
        while (true) {
            Request request = requestManager.getRequestForElevator(currentFloor, movingUp);
            if (request != null) {
                int targetFloor = request.floor;
                movingUp = (targetFloor > currentFloor);
                moveToFloor(targetFloor);
            } else {
                System.out.println("Elevator " + id + " is idle at floor " + currentFloor);
                idle = true;
                panel.setIdleState(id, true);
                
                if (panel.allElevatorsIdle()) {
                    System.out.println("All elevators are idle. Program terminating.");
                    System.exit(0);
                }

                try {
                    Thread.sleep(1000); // Idle delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

// GUI component to display elevator positions
class ElevatorPanel extends JPanel {
    private int[] elevatorPositions;
    private boolean[] elevatorIdleStates;
    private static final int NUM_FLOORS = 10;
    private static final Color[] ELEVATOR_COLORS = {Color.BLUE, Color.RED};

    public ElevatorPanel(int numElevators) {
        this.elevatorPositions = new int[numElevators];
        this.elevatorIdleStates = new boolean[numElevators];
        setPreferredSize(new Dimension(200, NUM_FLOORS * 60));
        setBackground(Color.WHITE);
    }

    public void updateElevatorPosition(int elevatorId, int floor) {
        elevatorPositions[elevatorId - 1] = NUM_FLOORS - floor - 1;
        elevatorIdleStates[elevatorId - 1] = false;
        repaint();
    }

    public void setIdleState(int elevatorId, boolean isIdle) {
        elevatorIdleStates[elevatorId - 1] = isIdle;
        repaint();
    }

    public boolean allElevatorsIdle() {
        for (boolean idle : elevatorIdleStates) {
            if (!idle) return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < NUM_FLOORS; i++) {
            g.drawString("Floor " + (NUM_FLOORS - i - 1), 10, i * 60 + 30);
            g.drawLine(0, i * 60, getWidth(), i * 60);
        }
        for (int i = 0; i < elevatorPositions.length; i++) {
            int floorY = elevatorPositions[i] * 60 + 10;
            g.setColor(ELEVATOR_COLORS[i % ELEVATOR_COLORS.length]);
            g.fillRoundRect(100 + i * 50, floorY, 30, 30, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString("Elevator " + (i + 1), 100 + i * 50, floorY + 45);
        }
    }
}

// Main class to initialize the elevators, requests, and GUI
public class ElevatorControlSystem {
    public static void main(String[] args) {
        RequestManager requestManager = new RequestManager();
        ElevatorPanel elevatorPanel = new ElevatorPanel(2);
        
        JFrame frame = new JFrame("Elevator Control System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(elevatorPanel);
        frame.pack();
        frame.setVisible(true);

        List<Thread> elevators = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            Elevator elevator = new Elevator(i, requestManager, elevatorPanel);
            Thread elevatorThread = new Thread(elevator);
            elevators.add(elevatorThread);
            elevatorThread.start();
        }

        requestManager.addRequest(new Request(3, true));
        requestManager.addRequest(new Request(7, false));
        requestManager.addRequest(new Request(5, true));
        requestManager.addRequest(new Request(2, false));
    }
}
