public class Main {
    public static void main(String[] args) {
        Tree<Integer> intTree = new Tree<>();
        //Tree<Integer> intTree = new Tree<>(10);

        intTree.insert(10);
        intTree.insert(5);
        intTree.insert(15);
        intTree.insert(3);
        intTree.insert(8);
        intTree.insert(11); 
        intTree.insert(21);
        //intTree.insert(10);

        ////intTree.printTree();

        intTree.remove(5);
        System.out.println();

        ////intTree.printTree();



        // Tree<Student> studentTree = new Tree<>();

        // studentTree.insert(new Student(10));
        // studentTree.insert(new Student(5));
        // studentTree.insert(new Student(15));
        // studentTree.insert(new Student(3));
        // studentTree.insert(new Student(8));
        // studentTree.insert(new Student(11));
        // studentTree.insert(new Student(21));

        //// studentTree.printTree();

        // studentTree.remove(new Student(5));
        // System.out.println();
        //// studentTree.printTree();

        System.out.println("обход дерева Вершина-Левый-Правый");
        intTree.traversalTopLeftRight();

        System.out.println("обход дерева Левый-Вершина-Правый");
        intTree.traversalLeftTopRight();

        System.out.println("обход дерева Левый-Правый-Вершина");
        intTree.traversalLeftRightTop();

        System.out.println("Find 10: " + intTree.find(10));
        System.out.println("Find 20: " + intTree.find(20));
        System.out.println("Find 11: " + intTree.find(11));

    }
}

class Tree <T extends Comparable<T>>{

    class Node{
        T value;
        Node left;
        Node right;
        Node(T value){
            this.value = value;
            left = right = null;
        }
    }

    private Node root;

    public Tree() {
        this.root = null;
    }

    Tree(T value){
        root = new Node(value);
    };
    

    // обход Вершина-Левый-Правый
    public void traversalTopLeftRight() {
        traversalTopLeftRight(root);
        System.out.println();
    }

    private void traversalTopLeftRight(Node node) {
        if (node != null) {
            System.out.print(node.value + " ");
            traversalTopLeftRight(node.left);
            traversalTopLeftRight(node.right);
        }
    }


    // обход Левый-Вершина-Правый
    public void traversalLeftTopRight() {
        traversalLeftTopRight(root);
        System.out.println();
    }

    private void traversalLeftTopRight(Node node) {
        if (node != null) {
            traversalLeftTopRight(node.left);
            System.out.print(node.value + " ");
            traversalLeftTopRight(node.right);
        }
    }


    // обход Левый-Правый-Вершина
    public void traversalLeftRightTop() {
        traversalLeftRightTop(root);
        System.out.println();
    }

    private void traversalLeftRightTop(Node node) {
        if (node != null) {
            traversalLeftRightTop(node.left);
            traversalLeftRightTop(node.right);
            System.out.print(node.value + " ");
        }
    }


    // добавление вершины по значению
    public void insert(T value){
        root = insertRec(root, value);
    }

    private Node insertRec(Node node, T value){
        if (node == null) {
            return new Node(value);
        }

        int compare = value.compareTo(node.value);
        if (compare < 0) {
            node.left = insertRec(node.left, value);
        } else if (compare > 0) {
            node.right = insertRec(node.right, value);
        }
        else{ System.out.println("Элемент " + value + " уже есть в дереве");}
        return node;
    }


    // ищет элемент по значению
    T find(T value){
        Node current = root;
        while(current != null){
            int compale = value.compareTo(current.value);

            if (compale == 0)
            return value;

            else if (compale < 0)
            current = current.left;

            else
            current = current.right;
        }
        return null;
    }

    
    // удаляет вершину
    public void remove(T value) {
        root = remove(root, value);
    }

    private Node remove(Node node, T value) {
        if (node == null) {
            return null;
        }

        int compare = value.compareTo(node.value);
        if (compare < 0) {
            node.left = remove(node.left, value);
        } else if (compare > 0) {
            node.right = remove(node.right, value);
        } else {
            // Вершина найдена
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // находим минимальное значение в правом поддереве
            Node minNode = node.right;
            while (minNode.left != null) {
                minNode = minNode.left;
            }
            node.value = minNode.value;

            node.right = remove(node.right, minNode.value);
        }

        return node;
    }


    public void printTree() {
        printTree(root, 0);
    }
    
    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void printTree(Node node, int level) {
        if (node == null) {
            return;
        }
        printTree(node.right, level + 1); // выводим правое поддерево
        System.out.println(repeat(" ", level * 4) + node.value); // вывод текущего узела
        printTree(node.left, level + 1); // выводим левое поддерево
    }
}



class Student implements Comparable<Student> {
    int id;

    public Student(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}