import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

class StackModel<T> {
    private ArrayList<StackElement<T>> stack;

    public StackModel() {
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
        return stack.get(stack.size() - 1).getValue();
    }


    public void push(T element) {
        stack.add(new StackElement<>(element));
    }
    

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("stack is empty");
        }
        return stack.remove(stack.size() - 1).getValue();
    }


    public void pushAll(ArrayList<T> elements) {
        for (T element : elements) {
            stack.add(new StackElement<>(element));
        }
    }    


    public boolean equals(Object obj) {
        if (this == obj) return true; // если это один и тот же объект
        if (!(obj instanceof StackModel)) return false; // если тип объекта другой
    
        StackModel<?> other = (StackModel<?>) obj;
    
        if (this.size() != other.size()) return false;
    
        for (int i = 0; i < this.size(); i++) {
            if (!Objects.equals(this.stack.get(i).getValue(), other.stack.get(i).getValue())) {
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


    public void accept(Visitor visitor) {
        ArrayList<Element> elements = new ArrayList<>(stack);
        visitor.visit(elements);
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
            return stack.get(currentIndex).getValue();
        }
        
    }


    public interface Iterator<T> {
        void first();
        boolean isDone();
        void next();
        T currentItem();
    }


    public ArrayList<T> getStackForJList() {
        ArrayList<T> result = new ArrayList<>();
        for (StackElement<T> element : stack) {
            result.add(element.getValue());
        }
        return result; // копию списка значений
    }
}



interface Visitor {
    void visit(ArrayList<Element> elements);
}



interface Element {
    void accept(Visitor visitor);
}


class StackElement<T> implements Element {
    private final T value;

    public StackElement(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void accept(Visitor visitor) {
        ArrayList<Element> elements = new ArrayList<>();
        elements.add(this);
        visitor.visit(elements);
    }

    public String toString() {
        return value.toString();
    }
}



class StackView extends JFrame {
    public JButton pushButton, popButton, topButton, sizeButton, clearButton, pushAllButton, equalsButton, showStackButton, visitorButton;
    public JTextArea textArea;

    public StackView() {
        setTitle("Stack App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 4, 5, 5));

        pushButton = new JButton("Push");
        popButton = new JButton("Pop");
        topButton = new JButton("Top");
        sizeButton = new JButton("Size");
        clearButton = new JButton("Clear");
        pushAllButton = new JButton("Push All");
        equalsButton = new JButton("Equals");
        showStackButton = new JButton("Show Stack");
        visitorButton = new JButton("Visitor");

        buttonPanel.add(pushButton);
        buttonPanel.add(popButton);
        buttonPanel.add(topButton);
        buttonPanel.add(sizeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(pushAllButton);
        buttonPanel.add(equalsButton);
        buttonPanel.add(showStackButton);
        buttonPanel.add(visitorButton);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public void print(String message) {
        textArea.append(message + "\n");
    }
}




class StackController<T> {
    private final StackModel<T> stack;
    private final StackView view;


    public StackController(StackModel<T> model, StackView view) {
        this.stack = model;
        this.view = view;

        controller();
    }


    private void controller() {
        view.pushButton.addActionListener(e -> push());
        view.popButton.addActionListener(e -> pop());
        view.topButton.addActionListener(e -> top());
        view.sizeButton.addActionListener(e -> size());
        view.clearButton.addActionListener(e -> clear());
        view.pushAllButton.addActionListener(e -> pushAll());
        view.equalsButton.addActionListener(e -> equals());
        view.showStackButton.addActionListener(e -> showStack());
        view.visitorButton.addActionListener(e -> visitor());
    }


    private void push() {
        String input = JOptionPane.showInputDialog(view, "Enter an element to push:");
        if (input == null) return;

        try {
            T value = parseInput(input);
            stack.push(value);
            view.print("Pushed: " + value);
        } catch (Exception ex) {
            view.print("Invalid input");
        }
    }


    private void pop() {
        try {
            T value = stack.pop();
            view.print("Popped: " + value);
        } catch (IllegalStateException ex) {
            view.print("Error: " + ex.getMessage());
        }
    }


    private void top() {
        try {
            T value = stack.top();
            view.print("Top element: " + value);
        } catch (IllegalStateException ex) {
            view.print("Error: " + ex.getMessage());
        }
    }


    private void size() {
        view.print("Stack size: " + stack.size());
    }


    private void clear() {
        stack.clear();
        view.print("Stack cleared");
    }


    private void pushAll() {
        String input = JOptionPane.showInputDialog(view, "Enter elements:");
        if (input == null) return;

        try {
            String[] items = input.split(" ");
            ArrayList<T> elements = new ArrayList<>();
            for (String item : items) {
                elements.add(parseInput(item.trim()));
            }
            stack.pushAll(elements);
            view.print("Pushed all: " + elements);
        } catch (Exception ex) {
            view.print("Invalid input");
        }
    }


    private void equals() {
        String input = JOptionPane.showInputDialog(view, "Enter elements for another stack:");
        if (input == null) return;

        StackModel<T> otherStack = new StackModel<>();
        try {
            String[] items = input.split(" ");
            for (String item : items) {
                otherStack.push(parseInput(item.trim()));
            }
            boolean isEqual = stack.equals(otherStack);
            view.print("Stacks are equal: " + isEqual);
        } catch (Exception ex) {
            view.print("Invalid input");
        }
    }


    private void showStack() {
        view.print(stack.toString());
    }


    private void visitor() {
        Visitor visitor = elements -> {
            for (Element element : elements) {
                if (element instanceof StackElement<?>) {
                    StackElement<?> stackElement = (StackElement<?>) element;
                    view.print("Visited element: " + stackElement.getValue());
                }
            }
        };
        stack.accept(visitor);
        view.print("Visitor visited all stack elements");
    }
    


    private T parseInput(String input) {
        try {
            return (T) input;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

}



public class Main {
    public static void main(String[] args) {
        StackModel<String> model = new StackModel<>();
        StackView view = new StackView();
        new StackController<>(model, view);
    }
}
