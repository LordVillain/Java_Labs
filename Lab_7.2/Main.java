
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Running button");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(700, 400);
        frame.setSize(500, 250);
        
        frame.setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        JLabel questionLabel = new JLabel("Вас радует размер стипендии?");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionPanel.add(questionLabel);
        frame.add(questionPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton yesButton = new JButton("Да");
        yesButton.setFont(new Font("Arial", Font.BOLD, 16));
        yesButton.addActionListener(e -> JOptionPane.showMessageDialog(frame,"Помните, стипендия — это всего лишь цифра")); //лямбда-выражение
        buttonPanel.add(yesButton);

        JButton noButton = new JButton("Нет");
        noButton.setFont(new Font("Arial", Font.BOLD, 16));
        noButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                int maxX = frame.getWidth() - noButton.getWidth() - 40;
                int maxY = frame.getHeight() - noButton.getHeight() - 40;
        
                int x = (int) (Math.random() * maxX);
                int y = (int) (Math.random() * maxY);
        
                noButton.setLocation(x, y);
            }
        });
        
        buttonPanel.add(noButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}