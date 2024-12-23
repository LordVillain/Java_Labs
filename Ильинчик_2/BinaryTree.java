import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BinaryTree {
    private final List<Integer> tree;

    public BinaryTree() {
        this.tree = new ArrayList<>();
        this.tree.add(0); // корень
    }

    public void add(int value) {
        int i = 0;
        while (true) {
            if (tree.size() <= i) {
                while (tree.size() <= i) {
                    tree.add(0);
                }
            }
    
            if (tree.get(i) == 0) {
                tree.set(i, value);
                return;
            }
    
            if (value <= tree.get(i)) {
                i = 2 * i + 1; // левый ребенок
            } else {
                i = 2 * i + 2; // правый ребенок
            }
        }
    }
    
    
    public void Size() {
        int i = 0;
        while (i < tree.size()) {
            i = 2 * i + 2;
        }
        while (tree.size() <= i) {
            tree.add(0);
        }
    }
    

    public int minUsingTraversal() {
        int min = Integer.MAX_VALUE;
        for (int val : tree) {
            if (val != 0 && val < min) {
                min = val;
            }
        }
        return min;
    }

    public int minUsingStream() {
        return tree.stream().filter(v -> v != 0).min(Integer::compare).orElse(0);
    }
    

    public List<Integer> moveToMin() {
        List<Integer> path = new ArrayList<>();
        if (tree.isEmpty() || tree.get(0) == 0) return path;
    
        int i = 0;
        int min = minUsingTraversal();
    
        while (i < tree.size() && tree.get(i) != 0) {
            path.add(tree.get(i));
            if (tree.get(i) == min) break;
    
            int leftIndex = 2 * i + 1;
            int rightIndex = 2 * i + 2;
    
            if (leftIndex < tree.size() && tree.get(leftIndex) != 0) {
                if (rightIndex < tree.size() && tree.get(rightIndex) != 0) {
                    i = tree.get(leftIndex) <= tree.get(rightIndex) ? leftIndex : rightIndex;
                } else {
                    i = leftIndex;
                }
            } else if (rightIndex < tree.size() && tree.get(rightIndex) != 0) {
                i = rightIndex;
            } else {
                break;
            }
        }
        return path;
    }
    


    public interface Visitor {
        void visit(int value);
    }

    public void visit(Visitor visitor) {
        for (Integer value : tree) {
            if (value != 0) {
                visitor.visit(value);
            }
        }
    }
    
    
    
    class TreeIterator {
    private final List<Integer> tree;
    private int currentIndex = 0;

    public TreeIterator(List<Integer> tree) {
        this.tree = tree;
    }

    public boolean hasNext() {
        while (currentIndex < tree.size() && tree.get(currentIndex) == 0) {
            currentIndex++; // Пропуск пустых узлов
        }
        return currentIndex < tree.size();
    }

    public int next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in the tree.");
        }
        return tree.get(currentIndex++);
    }
}
    
    

    public List<Integer> preOrderTraversal() {
        List<Integer> result = new ArrayList<>();
        preOrderHelper(0, result);
        return result;
    }

    private void preOrderHelper(int index, List<Integer> result) {
        if (index >= tree.size() || tree.get(index) == 0) return;
        result.add(tree.get(index)); // Вершина
        preOrderHelper(2 * index + 1, result); // Левый
        preOrderHelper(2 * index + 2, result); // Правый
    }

    public void save(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int val : tree) {
                writer.write(val + " ");
            }
        }
    }


    public List<Integer> getTreeArray() {
        return tree;
    }





    


}
