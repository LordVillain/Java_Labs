import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String key);
}
    

interface Observer {
    void update(String key);
}


class KeyPressModel {
    private final ArrayList<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String key) {
        for (Observer observer : observers) {
            observer.update(key);
        }
    }
    
}

// Наблюдатель для JLabel
class JLabelObserver implements Observer {
    private final JLabel label;

    public JLabelObserver(JLabel label) {
        this.label = label;
    }

    public void update(String key) {
        label.setText("key pressed: " + key);
    }
}


class JListObserver implements Observer {
    private final DefaultListModel<String> listModel;

    public JListObserver(DefaultListModel<String> listModel) {
        this.listModel = listModel;
    }

    public void update(String key) {
        listModel.addElement(key);
    }
}


public class Main {
    public static void main(String[] args) {
        KeyPressModel model = new KeyPressModel();

        JFrame frame = new JFrame("APP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("Key Pressed: ", SwingConstants.CENTER);
        keyLabel.setFont(new Font("Arial", Font.BOLD, 30));
        frame.add(keyLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> keyJList = new JList<>(listModel);
        frame.add(new JScrollPane(keyJList), BorderLayout.CENTER);

        model.attach(new JLabelObserver(keyLabel));
        model.attach(new JListObserver(listModel));

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        String keyText = KeyEvent.getKeyText(e.getKeyCode());
                        model.notifyObservers(keyText);
                    }
                    return false;
                });

        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
}
