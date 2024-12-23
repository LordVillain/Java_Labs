import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("First App", createListTransferPanel());
        tabbedPane.addTab("Second App", createGridButtonPanel());
        tabbedPane.addTab("Third App", createRadioButtonPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }


    private static JPanel createListTransferPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> leftListModel = new DefaultListModel<>();
        JList<String> leftList = new JList<>(leftListModel);
        // Разрешаем множественный выбор элементов в списке
        leftList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        for (int i = 1; i <= 10; i++) {
            leftListModel.addElement("Button ");
        }

        DefaultListModel<String> rightListModel = new DefaultListModel<>();
        JList<String> rightList = new JList<>(rightListModel);
        rightList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton rightButton = new JButton(">>>");
        JButton leftButton = new JButton("<<<");

        buttonPanel.add(rightButton, BorderLayout.NORTH);
        buttonPanel.add(leftButton, BorderLayout.SOUTH);

        
        rightButton.addActionListener(e -> {
            int[] selectedIndices = leftList.getSelectedIndices();
            
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                int index = selectedIndices[i];
                rightListModel.addElement(leftListModel.get(index));
                leftListModel.remove(index);
            }
        });


        leftButton.addActionListener(e -> {
            int[] selectedIndices = rightList.getSelectedIndices();
            
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                int index = selectedIndices[i];
                leftListModel.addElement(rightListModel.get(index));
                rightListModel.remove(index);
            }
        });


        panel.add(new JScrollPane(leftList), BorderLayout.WEST);
        panel.add(new JScrollPane(rightList), BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }


    private static JPanel createGridButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 5));
        ButtonHandler handler = new ButtonHandler();

        for (int i = 0; i < 25; i++) {
            JButton button = new JButton("Button " + (i + 1));
            button.setBackground(Color.LIGHT_GRAY);
            button.addMouseListener(handler);
            panel.add(button);
        }

        return panel;
    }


    private static JPanel createRadioButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        // Создание пользовательских иконок для каждого состояния
        Icon normalIcon = createIcon(Color.LIGHT_GRAY, "Normal");
        Icon hoverIcon = createIcon(Color.YELLOW, "Hover");
        Icon selectedIcon = createIcon(Color.GREEN, "Selected");
        Icon pressedIcon = createIcon(Color.BLUE, "Pressed");
    
        ButtonGroup group = new ButtonGroup();
        //используется, чтобы гарантировать, что только одна радиокнопка в группе может быть выбрана в любой момент времени
    
        for (int i = 1; i <= 6; i++) {
            JRadioButton radioButton = new JRadioButton("Region " + i);
            
            radioButton.setIcon(normalIcon);
            radioButton.setRolloverIcon(hoverIcon);
            radioButton.setSelectedIcon(selectedIcon);
            radioButton.setPressedIcon(pressedIcon);
    
            group.add(radioButton);
            panel.add(radioButton);
        }
    
        return panel;
    }
    

    private static Icon createIcon(Color color, String text) {
        int width = 100;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawString(text, 10, 20);
        g.dispose();
        return new ImageIcon(image);
    }


    static class ButtonHandler implements MouseListener {
        private String currentText;

        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setBackground(Color.GRAY);
        }

        public void mouseExited(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setBackground(Color.LIGHT_GRAY);
        }

        public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            currentText = button.getText();
            button.setText("Clicked!");
        }

        public void mouseReleased(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setText(currentText);
        }

        public void mouseClicked(MouseEvent e) {}
    }
}

// следующая версия 25 11
