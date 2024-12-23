import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class View extends JFrame {
    private final JTextField inputField;
    private final JTextArea treeDisplay;
    private final JLabel minLabel;
    private final JLabel moveToMinLabel;
    private final JLabel preOrderLabel;

    public View() {
        setTitle("Binary Tree");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new FlowLayout());

        inputField = new JTextField(10);
        JButton addButton = new JButton("Add");
        JButton minButton = new JButton("Find Min");
        JButton moveToMinButton = new JButton("Path to Min");
        JButton preOrderButton = new JButton("Traversal");
        JButton saveButton = new JButton("Save");
        JButton visitButton = new JButton("Visit Tree");
        treeDisplay = new JTextArea(5, 50);
        minLabel = new JLabel("Min:");
        moveToMinLabel = new JLabel("Path to Min:");
        preOrderLabel = new JLabel("Traversal:");

        add(new JLabel("Value:"));
        add(inputField);
        add(addButton);
        add(minButton);
        add(moveToMinButton);
        add(preOrderButton);
        add(saveButton);
        add(visitButton);
        add(new JScrollPane(treeDisplay));
        add(minLabel);
        add(moveToMinLabel);
        add(preOrderLabel);

        setVisible(true);
    }

    public int getInputValue() {
        return Integer.parseInt(inputField.getText());
    }

    public void addAddButtonListener(ActionListener listener) {
        ((JButton) getContentPane().getComponent(2)).addActionListener(listener);
    }

    public void addMinButtonListener(ActionListener listener) {
        ((JButton) getContentPane().getComponent(3)).addActionListener(listener);
    }

    public void addMoveToMinButtonListener(ActionListener listener) {
        ((JButton) getContentPane().getComponent(4)).addActionListener(listener);
    }

    public void addPreOrderButtonListener(ActionListener listener) {
        ((JButton) getContentPane().getComponent(5)).addActionListener(listener);
    }

    public void addSaveButtonListener(ActionListener listener) {
        ((JButton) getContentPane().getComponent(6)).addActionListener(listener);
    }

    public void addVisitButtonListener(ActionListener listener) {
        ((JButton) getContentPane().getComponent(7)).addActionListener(listener);
    }

    public void updateTreeArray(List<Integer> tree) {
        treeDisplay.setText(tree.toString());
    }

    public void showMin(int traversal, int stream) {
        minLabel.setText("Min: Traversal = " + traversal + ", Stream = " + stream);
    }

    public void showMoveToMin(List<Integer> path) {
        moveToMinLabel.setText("Path to Min: " + path);
    }

    public void showPreOrder(List<Integer> preOrder) {
        preOrderLabel.setText("Traversal: " + preOrder);
    }

    public void showVisitResult(String visitResult) {
        JOptionPane.showMessageDialog(this, "Visit Result: " + visitResult);
    }

    public void showSaveSuccess() {
        JOptionPane.showMessageDialog(this, "Tree saved successfully!");
    }

    public void showSaveError(String message) {
        JOptionPane.showMessageDialog(this, "Error saving tree: " + message);
    }
}
