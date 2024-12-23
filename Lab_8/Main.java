import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame implements ActionListener {
    private JButton redButton, greenButton, blueButton;
    private Color penColor = Color.RED;
    private BufferedImage img;
    private JPanel panel;
    private int lastX, lastY;

    public Main() {
        super("SimplePaint");

        img = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();

        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);  // null - не требуется специальный объект для отслеживания загрузки и отрисовка будет выполнена сразу
            }
        };

        panel.setPreferredSize(new Dimension(2000, 2000));

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        redButton = new JButton("Red");
        greenButton = new JButton("Green");
        blueButton = new JButton("Blue");
        redButton.addActionListener(this);
        greenButton.addActionListener(this);
        blueButton.addActionListener(this);
        buttonPanel.add(redButton);
        buttonPanel.add(greenButton);
        buttonPanel.add(blueButton);
        add(buttonPanel, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                drawOnImg(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
                panel.repaint();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem openItem = new JMenuItem("Open");

        saveItem.addActionListener(e -> saveImage());
        openItem.addActionListener(e -> openImage());

        fileMenu.add(saveItem);
        fileMenu.add(openItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    private void drawOnImg(int x1, int y1, int x2, int y2) {
        Graphics2D g = img.createGraphics();
        g.setColor(penColor);
        g.setStroke(new BasicStroke(5)); // толщина линии в 5 пикселей
        g.drawLine(x1, y1, x2, y2);
        g.dispose(); // освобождаем ресурсы
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == redButton) {
            penColor = Color.RED;
        } else if (e.getSource() == greenButton) {
            penColor = Color.GREEN;
        } else if (e.getSource() == blueButton) {
            penColor = Color.BLUE;
        }
    }

    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(img, "png", new File(file.getAbsolutePath() + ".png"));
                JOptionPane.showMessageDialog(this, "image saved");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage());
            }
        }
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int dialog = fileChooser.showOpenDialog(this);
        if (dialog == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                img = ImageIO.read(file);
                // устанавливаем размер панели равным размеру изображения
                panel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
                panel.revalidate(); // пересчитываем компоновку
                panel.repaint();
                JOptionPane.showMessageDialog(this, "image opened");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "error opening image: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Main frame = new Main();
        frame.setVisible(true);
    }
}
