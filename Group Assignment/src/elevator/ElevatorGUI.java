package elevator;

import javax.swing.*;

public class ElevatorGUI extends JFrame {
    private ElevatorPanel elevatorPanel;
    private static final int NUM_ELEVATORS = 2; // Number of elevators in the simulation

    public ElevatorGUI() {
        setTitle("Elevator System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        elevatorPanel = new ElevatorPanel(NUM_ELEVATORS); // Initialize elevator panel with the number of elevators
        add(elevatorPanel);

        pack(); // Adjust frame size to fit components
        setVisible(true); // Display the frame
    }

    // Update the position and colour of an elevator
    public void updateElevator(int elevatorId, int currentFloor, int targetFloor) {
        SwingUtilities.invokeLater(() -> elevatorPanel.updateElevator(elevatorId, currentFloor, targetFloor));
    }

    // Set the idle state of an elevator
    public void setIdleState(int elevatorId, boolean isIdle) {
        SwingUtilities.invokeLater(() -> elevatorPanel.setIdleState(elevatorId, isIdle));
    }

    // Check if all elevators are idle
    public boolean allElevatorsIdle() {
        return elevatorPanel.allElevatorsIdle();
    }
}
