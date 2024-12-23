import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class Main extends JFrame {
    private JTextArea inputField;  // поле исходных данных
    private JTextArea outputField; // поле отображения результатов
    private JButton openButton;    // кнопка открытия файла
    private JButton saveButton;    // кнопка сохранения данных
    private JTextField numberField; // поле ввода номера зачетной книжки
    private JTextField lastNameField; // поле ввода фамилии
    private JTextField courseField; // поле ввода курса
    private JTextField groupField; // поле ввода группы
    private JComboBox<String> strategyComboBox; // список для выбора стратегии

    private Context context; // контекст для управления стратегиями
    // private Strategy strategy; // стратегия 

    private File currentFile; // файл

    public Main(String title) {

        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 600);
        setLocation(600, 300);

        JPanel panel = new JPanel(new BorderLayout());

        inputField = new JTextArea(10, 40);
        outputField = new JTextArea(10, 40);
        inputField.setEditable(false);
        outputField.setEditable(false);

        JScrollPane inputScrollPane = new JScrollPane(inputField);
        JScrollPane outputScrollPane = new JScrollPane(outputField);

        openButton = new JButton("Open file");
        saveButton = new JButton("Add student");

        // выпадающий список для выбора стратегии
        String[] strategies = {"Stream API", "Other processing"};
        strategyComboBox = new JComboBox<>(strategies);
        strategyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStrategy = (String) strategyComboBox.getSelectedItem();
                if ("Stream API".equals(selectedStrategy)) {
                    context.setStrategy(new StreamStrategy());
                } else {
                    context.setStrategy(new OtherStrategy());
                }
            }
        });

        // Инициализация контекста с базовой стратегией
        context = new Context(new OtherStrategy());
        // панель для ввода данных нового студента
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Record Number:"));
        numberField = new JTextField();
        inputPanel.add(numberField);

        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Group:"));
        groupField = new JTextField();
        inputPanel.add(groupField);

        inputPanel.add(new JLabel("Select Strategy:"));
        inputPanel.add(strategyComboBox);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveStudentToFile();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(inputScrollPane, BorderLayout.CENTER);
        panel.add(outputScrollPane, BorderLayout.SOUTH);
        panel.add(inputPanel, BorderLayout.EAST);

        add(panel);
        setVisible(true);
    }

    interface Strategy {
        List<Student> execute(List<Student> students);
    }


    // private List<Student> processStudents(List<Student> students) {
    //     return strategy.execute(students);
    // }


    // Класс контекста
    class Context {
        private Strategy strategy;

        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }

        public List<Student> doSomething(List<Student> students) {
            return strategy.execute(students);
        }
    }
    
    
    class StreamStrategy implements Strategy {
        public List<Student> execute(List<Student> students) {
            Map<String, Long> mapLastNameCount = students.stream()
                    .collect(Collectors.groupingBy(Student::getLastName, Collectors.counting()));
    
            return students.stream()
                    .filter(s -> mapLastNameCount.get(s.getLastName()) > 1)
                    .sorted(Comparator.comparing(Student::getCourse)
                            .thenComparing(Student::getGroup)
                            .thenComparing(Student::getLastName)
                            .thenComparing(Student::getNumber))
                    .collect(Collectors.toList());
        }
    }

    class OtherStrategy implements Strategy {
        public List<Student> execute(List<Student> students) {
            // Подсчет дубликатов вручную
            Map<String, Integer> lastNameCount = new HashMap<>();
            for (Student s : students) {
                lastNameCount.put(s.getLastName(), lastNameCount.getOrDefault(s.getLastName(), 0) + 1);
            }
    
            List<Student> duplicateStudents = new ArrayList<>();
            for (Student s : students) {
                if (lastNameCount.get(s.getLastName()) > 1) {
                    duplicateStudents.add(s);
                }
            }
    
            // Сортировка вручную с использованием Comparator
            Collections.sort(duplicateStudents, new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    int result = s1.getCourse().compareTo(s2.getCourse());
                    if (result == 0) {
                        result = s1.getGroup().compareTo(s2.getGroup());
                    }
                    if (result == 0) {
                        result = s1.getLastName().compareTo(s2.getLastName());
                    }
                    if (result == 0) {
                        result = s1.getNumber().compareTo(s2.getNumber());
                    }
                    return result;
                }
            });
    
            return duplicateStudents;
        }
    }
    

    
    // открытие файла
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                //List<Student> students = readStudentsFromFile(currentFile);

                List<Student> students;
                if (currentFile.getName().endsWith(".xml")) {
                    String[] options = {"DOM", "SAX"};
                    int choice = JOptionPane.showOptionDialog(
                        this, 
                        "Выберите метод чтения XML:", 
                        "Выбор метода", 
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        options, 
                        options[0]
                    );
                    if (choice == 0) {
                        students = readStudentsWithDOM(currentFile);
                    } else {
                        students = readStudentsWithSAX(currentFile);
                    }
                } else {
                    students = readStudentsFromFile(currentFile);
                }
                displayInput(students);
                List<Student> duplicateStudents = context.doSomething(students);
                displayOutput(duplicateStudents);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ошибка при чтении файла: " + e.getMessage());
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage());
            }
        }
    }


    // сохранение нового студента в файл
    private void saveStudentToFile() {
        if (currentFile == null) {
            JOptionPane.showMessageDialog(this, "Сначала откройте файл");
            return;
        }

        String number = numberField.getText();
        String lastName = lastNameField.getText();
        String course = courseField.getText();
        String group = groupField.getText();

        if (number.isEmpty() || lastName.isEmpty() || course.isEmpty() || group.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все поля справа для добавления студента");
            return;
        }

        if (!number.matches("\\d{7}")) {
            JOptionPane.showMessageDialog(this, "Введено неверное значение номера зачетки, можно использовать только семизначное число");
            return;
        }

        if (!lastName.matches("[а-яА-Я]+")) {
            JOptionPane.showMessageDialog(this, "Введена неверная фамилия, можнно использовать только буквы русского алфавита");
            return;
        }

        if (!course.matches("\\d") || course.length() != 1) {
            JOptionPane.showMessageDialog(this, "Введено неверное значение курса, можно вводить только 1 цифру");
            return;
        }

        if (!group.matches("\\d+") || Integer.parseInt(group) < 1 || Integer.parseInt(group) > 100) {
            JOptionPane.showMessageDialog(this, "Введено неверное значение группы, можно ввести только числа от 1 до 100");
            return;
        }

        try {
            Student newStudent = new Student(number, lastName, course, group);
            writeStudentToFile(newStudent);

            // обновление файла после добавления студента
            List<Student> students = readStudentsFromFile(currentFile);
            displayInput(students);
            List<Student> duplicateStudents = context.doSomething(students);
            displayOutput(duplicateStudents);

            clearInputFields();
        } 
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "ошибка при записи в файл" + e.getMessage());
        }
    }


    // запись нового студента в файл
    private void writeStudentToFile(Student student) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile, true))) { //true - данные будут добавляться в конец файла, а не перезаписывать его
            writer.write(student.toString());
            writer.newLine();
        }
    }


    // очистка полей ввода
    private void clearInputFields() {
        numberField.setText("");
        lastNameField.setText("");
        courseField.setText("");
        groupField.setText("");
    }


    // чтение студентов из файла
    private List<Student> readStudentsFromFile(File file) throws IOException {
        List<Student> students = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+"); //space, one or more
                if (parts.length == 4) {
                    String number = parts[0];
                    String lastName = parts[1];
                    String course = parts[2];
                    String group = parts[3];
                    students.add(new Student(number, lastName, course, group));
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Ошибка: файл не найден: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка при чтении файла: " + e.getMessage());
        }
        
        return students;
    }





    private List<Student> readStudentsWithDOM(File file) throws Exception {
        List<Student> students = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
    
        NodeList studentNodes = document.getElementsByTagName("student");
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Element studentElement = (Element) studentNodes.item(i);
    
            String number = studentElement.getElementsByTagName("number").item(0).getTextContent();
            String lastName = studentElement.getElementsByTagName("lastName").item(0).getTextContent();
            String course = studentElement.getElementsByTagName("course").item(0).getTextContent();
            String group = studentElement.getElementsByTagName("group").item(0).getTextContent();
    
            students.add(new Student(number, lastName, course, group));
        }
    
        return students;
    }
    




    private List<Student> readStudentsWithSAX(File file) throws Exception {
        List<Student> students = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {
            private String currentElement;
            private String number, lastName, course, group;

            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                currentElement = qName;
            }

            public void characters(char[] ch, int start, int length) {
                String content = new String(ch, start, length).trim();
                if (content.isEmpty()) return;

                switch (currentElement) {
                    case "number" -> number = content;
                    case "lastName" -> lastName = content;
                    case "course" -> course = content;
                    case "group" -> group = content;
                }
            }

            public void endElement(String uri, String localName, String qName) {
                if ("student".equals(qName)) {
                    students.add(new Student(number, lastName, course, group));
                    number = lastName = course = group = null;
                }
            }
        };

        parser.parse(file, handler);
        return students;
    }






    // отображение исходных данных
    private void displayInput(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        for (Student student : students) {
            sb.append(student).append("\n");
        }
        inputField.setText(sb.toString());
    }


    // вывод результата
    private void displayOutput(List<Student> students) {
        StringBuilder message = new StringBuilder();
        for (Student student : students) {
            message.append(student).append("\n");
        }
        outputField.setText(message.toString());
    }


    class Student {
        private String number;
        private String lastName;
        private String course;
        private String group;

        public Student(String number, String lastName, String course, String group) {
            this.number = number;
            this.lastName = lastName;
            this.course = course;
            this.group = group;
        }

        public String getNumber() {
            return number;
        }

        public String getLastName() {
            return lastName;
        }

        public String getCourse() {
            return course;
        }

        public String getGroup() {
            return group;
        }

        public String toString() {
            return number + " " + lastName + " " + course + " " + group;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Main("Sort file");
        frame.setVisible(true);
    }
}
