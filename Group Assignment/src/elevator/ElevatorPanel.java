package elevator;

import javax.swing.*;
import java.awt.*;

// ElevatorPanel class to visually represent elevators moving across floors
public class ElevatorPanel extends JPanel {
    private static final int NUM_FLOORS = 10; // Total number of floors
    private static final Color[] ELEVATOR_COLORS = {Color.CYAN, Color.BLUE}; // Elevator colors
    private static final Color ARRIVED_COLOR = Color.GREEN; // Color when elevator reaches destination
    private int[] elevatorPositions; // Tracks elevator floor positions
    private boolean[] elevatorIdleStates; // Tracks idle states of elevators

    public ElevatorPanel(int numElevators) {
        this.elevatorPositions = new int[numElevators];
        this.elevatorIdleStates = new boolean[numElevators];
        setPreferredSize(new Dimension(300, NUM_FLOORS * 60)); // Set panel size
        setBackground(Color.WHITE); // Panel background color
    }

    // Update the floor position of an elevator
    public void updateElevator(int elevatorId, int currentFloor, int targetFloor) { 
        elevatorPositions[elevatorId - 1] = NUM_FLOORS - currentFloor - 1; // Convert to graphical coordinates
        elevatorIdleStates[elevatorId - 1] = false;
        repaint();
    }

    // Set the idle state of an elevator
    public void setIdleState(int elevatorId, boolean isIdle) {
        elevatorIdleStates[elevatorId - 1] = isIdle;
        repaint();
    }

    // Check if all elevators are idle
    public boolean allElevatorsIdle() {
        for (boolean idle : elevatorIdleStates) {
            if (!idle) 
                return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(90, 0, 90, getHeight()); // Elevator & Floor separator lines
        for (int i = 0; i < NUM_FLOORS; i++) {
            g.drawString("Floor " + (NUM_FLOORS - i - 1), 10, i * 60 + 30); // Floor labels
            g.drawLine(0, i * 60, getWidth(), i * 60); // Floor separator lines
        }
        for (int i = 0; i < elevatorPositions.length; i++) {
            int floorY = elevatorPositions[i] * 60 + 10; // Calculate Y position
            g.setColor(ELEVATOR_COLORS[i % ELEVATOR_COLORS.length]); // Set color
            g.fillRoundRect(140 + i * 90, floorY, 30, 30, 10, 10); // Draw elevator rectangle
            g.setColor(Color.BLACK);
            g.drawString("Elevator " + (i + 1), 130 + i * 90, floorY + 45); // Label elevator
        }
    }
}
