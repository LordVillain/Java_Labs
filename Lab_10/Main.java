import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

class Stack<T> {
    private ArrayList<T> stack;

    public Stack() {
        this.stack = new ArrayList<>();
    }


    public int size() {
        return stack.size();
    }


    public boolean isEmpty() {
        return stack.isEmpty();
    }


    public void clear() {
        stack.clear();
    }


    public T top() {
        if (isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }
        return stack.get(stack.size() - 1);
    }


    public void push(T element) {
        stack.add(element);
    }


    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }
        return stack.remove(stack.size() - 1);
    }


    public void pushAll(ArrayList<T> elements) {
        stack.addAll(elements);
    }


    public boolean equals(Object o) {
        if (this == o) return true; // если это один и тот же объект
        if (!(o instanceof Stack)) return false; // если тип объекта другой
    
        Stack<?> other = (Stack<?>) o;
    
        if (this.size() != other.size()) return false;
    
        for (int i = 0; i < this.size(); i++) {
            if (!Objects.equals(this.stack.get(i), other.stack.get(i))) {
                return false;
            }
        }
        return true;
    }
    

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("stack elements: [");
    
        StackIterator iterator = new StackIterator();
        iterator.first();
        
        while (!iterator.isDone()) {
            sb.append(iterator.currentItem());
            iterator.next();
            
            if (!iterator.isDone()) {
                sb.append(", ");
            }
        }
        
        sb.append("]");
        return sb.toString();
    }


    public Iterator<T> iterator() {
        return new StackIterator();
    }


    private class StackIterator implements Iterator<T> {
        private int currentIndex = 0;


        public void first() {
            currentIndex = 0;
        }


        public boolean isDone() {
            return currentIndex >= stack.size();
        }


        public void next() {
            if (isDone()) {
                throw new IllegalStateException("iterator out of bounds");
            }
            currentIndex++;
        }


        public T currentItem() {
            if (isDone()) {
                throw new IllegalStateException("iterator out of bounds");
            }
            return stack.get(currentIndex);
        }
    }


    public interface Iterator<T> {
        void first();       // move to the first element
        boolean isDone();   // check if iteration is complete
        void next();        // move to the next element
        T currentItem();    // get the current item
    }


    public ArrayList<T> getStackForJList() {
        return new ArrayList<>(stack);  // return a copy of the stack
    }
}



public class Main<T> {
    private Stack<T> stack;
    private JTextArea textArea;

    public Main() {
        stack = new Stack<>();
        JFrame frame = new JFrame("Stack App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 4, 5, 5));

        JButton pushButton = new JButton("Push");
        JButton popButton = new JButton("Pop");
        JButton topButton = new JButton("Top");
        JButton sizeButton = new JButton("Size");
        JButton clearButton = new JButton("Clear");
        JButton pushAllButton = new JButton("Push All");
        JButton equalsButton = new JButton("Equals");
        JButton showStackButton = new JButton("Show Stack");

        buttonPanel.add(pushButton);
        buttonPanel.add(popButton);
        buttonPanel.add(topButton);
        buttonPanel.add(sizeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(pushAllButton);
        buttonPanel.add(equalsButton);
        buttonPanel.add(showStackButton);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);


        pushButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter an element to push:");
            if (cancelled(input)) return;
            try {
                T value = parseInput(input);
                stack.push(value);
                print("Pushed: " + value);
            } catch (Exception ex) {
                print("Invalid input");
            }
        });


        popButton.addActionListener(e -> {
            try {
                T value = stack.pop();
                print("Popped: " + value);
            } catch (IllegalStateException ex) {
                print("Error: " + ex.getMessage());
            }
        });


        topButton.addActionListener(e -> {
            try {
                T value = stack.top();
                print("Top element: " + value);
            } catch (IllegalStateException ex) {
                print("Error: " + ex.getMessage());
            }
        });


        sizeButton.addActionListener(e -> print("Stack size: " + stack.size()));


        clearButton.addActionListener(e -> {
            stack.clear();
            print("Stack cleared");
        });


        pushAllButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter elements:");
            if (cancelled(input)) return;
            try {
                String[] items = input.split(" ");
                ArrayList<T> elements = new ArrayList<>();
                for (String item : items) {
                    elements.add(parseInput(item.trim()));
                }
                stack.pushAll(elements);
                print("Pushed all: " + elements);
            } catch (Exception ex) {
                print("Invalid input");
            }
        });


        equalsButton.addActionListener(e -> {
            Stack<T> otherStack = new Stack<>();
            String input = JOptionPane.showInputDialog(frame, "Enter elements for another stack:");
            if (cancelled(input)) return;
            try {
                String[] items = input.split(" ");
                for (String item : items) {
                    otherStack.push(parseInput(item.trim()));
                }
                boolean isEqual = stack.equals(otherStack);
                print("Stacks are equal: " + isEqual);
            } catch (Exception ex) {
                print("Invalid input");
            }
        });


        showStackButton.addActionListener(e -> print(stack.toString()));


        frame.setVisible(true);
    }


    private T parseInput(String input) {
        try {
            T value = (T) input;
            return value;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid input");
        }
    }


    private void print(String message) {
        textArea.append(message + "\n");
    }


    private boolean cancelled(String input) {
        if (input == null) {
            print("Operation cancelled");            
            return true;
        }
        return false;
    }

    
    public static void main(String[] args) {
        new Main();
    }
}
