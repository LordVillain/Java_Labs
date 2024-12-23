import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private JButton button;
    private JLabel statusBar;

    public Main() {
        setTitle("Mouse Tracking App");
        setLocation(300, 150);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        add(mainPanel, BorderLayout.CENTER);

        button = new JButton("Click");
        button.setBounds(50, 50, 80, 30);
        mainPanel.add(button);

        statusBar = new JLabel();
        add(statusBar, BorderLayout.SOUTH);

        // адаптер для обновления координат независимо от компонента
        MouseMotionAdapter motionAdapter = new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                updateStatusBar(e);
            }

            public void mouseDragged(MouseEvent e) {
                updateStatusBar(e);
                if (e.isControlDown() && e.getSource() == button) { //добавил
                    //преобразует координаты точки события из локальных координат button в координаты панели mainPanel
                    Point location = SwingUtilities.convertPoint(button, e.getPoint(), mainPanel);
                    button.setLocation(location.x - button.getWidth() / 2, location.y - button.getHeight() / 2);
                }
            }
        };


        mainPanel.addMouseMotionListener(motionAdapter);
        button.addMouseMotionListener(motionAdapter);


        mainPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                button.setLocation(e.getX() - button.getWidth() / 2, e.getY() - button.getHeight() / 2);
            }
        });


        // обработчик клавиатуры
        button.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                // является ли введённый символ буквой, цифрой, пробелом
                if (Character.isLetterOrDigit(e.getKeyChar()) || Character.isWhitespace(e.getKeyChar())) {
                    button.setText(button.getText() + e.getKeyChar());
                }
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && button.getText().length() > 0) {
                    String currentText = button.getText();
                    button.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        setVisible(true);
    }

    // обновление координат
    private void updateStatusBar(MouseEvent e) {
        Point location = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), getContentPane());
        //e.getComponent() — получает компонент, на котором произошло событие
        //e.getPoint() — координаты точки
        //getContentPane() — возвращает главный контейнер окна (content pane) окна JFrame
        //convertPoint() — преобразует координаты точки события в координаты относительно getContentPane().
        statusBar.setText("Mouse Coordinates: X = " + location.x + ", Y = " + location.y);
    }


    public static void main(String[] args) {
        new Main();
    }
    
}
