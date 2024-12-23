import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;


public class Main extends JFrame{
    private JTextField jStep;
    private JTextField jB1;
    private JTextField jN;
    private JTextField jFileName;
    private JTextArea jInfoArea;
    private JTextField jIndex;  // Поле для ввода j-го элемента
    private Series currentSeries; // хранит текущую последовательность

    public static void main(String[] args) {
        // try {

            JFrame frame = new Main("Liner");
            frame.setSize(750, 300);
            frame.setLocation(600, 300);
            frame.setVisible(true);

        //     Series linearSeries = new Liner(2, 1, 5);
        //     System.out.println("Linear Series: " + linearSeries);
        //     System.out.println("Sum: " + linearSeries.Sum());
        //     linearSeries.writeInFile("file.txt");
        //     Series exponentialSeries = new Exponential(2, 2, 5);
        //     System.out.println("Exponential Series: " + exponentialSeries);
        //     System.out.println("Sum: " + exponentialSeries.Sum());
        //     exponentialSeries.writeInFile("file.txt");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    
    }


    public Main(String name){
        super(name);
        setLayout(new FlowLayout());
        
        jStep = new JTextField(10);
        jB1 = new JTextField(10);
        jN = new JTextField(10);
        jFileName = new JTextField(15);
        jIndex = new JTextField(5);

        jInfoArea = new JTextArea(10, 40);
        jInfoArea.setEditable(false);      // нельзя редактировать
        jInfoArea.setLineWrap(true);       // перенос строк
        jInfoArea.setWrapStyleWord(true);  // перенос слов

        JScrollPane scrollPane = new JScrollPane(jInfoArea); // прокрутка

        JButton linerButton = new JButton("Liner");
        JButton exponentialButton = new JButton("Exponential");
        JButton saveButton = new JButton("Save to File");
        saveButton.setEnabled(false);
        JButton getJElementButton = new JButton("Get J Element");
        getJElementButton.setEnabled(false);


        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Панель для ввода данных

        inputPanel.add(new JLabel("Step:"));
        inputPanel.add(jStep);
        inputPanel.add(new JLabel("First element:"));
        inputPanel.add(jB1);
        inputPanel.add(new JLabel("Number of elements:"));
        inputPanel.add(jN);
        inputPanel.add(new JLabel("Index j:"));
        inputPanel.add(jIndex);
        
        add(inputPanel);
        

        jFileName.setText("File name");
        jFileName.setForeground(Color.GRAY);
        // текст прямо в строке
        jFileName.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (jFileName.getText().equals("File name")) {
                    jFileName.setText("");
                    jFileName.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (jFileName.getText().isEmpty()) {
                    jFileName.setText("File name");
                    jFileName.setForeground(Color.GRAY);
                }
            }
        });

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Добавляем кнопки в панель
        buttonPanel.add(linerButton);
        buttonPanel.add(exponentialButton);
        buttonPanel.add(getJElementButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(jFileName);

        add(buttonPanel);
        add(scrollPane);


    // Обработчик для линейной последовательности
    linerButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            createSeries("liner", saveButton, getJElementButton); //Liner.class
        }
    });

    // Обработчик для экспоненциальной последовательности
    exponentialButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            createSeries("exponential", saveButton, getJElementButton); //Exponential.class
        }
    });


    // Обработчик для сохранения данных в файл
    saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String content = jInfoArea.getText();
            if (content.isEmpty()) {
                jInfoArea.setText("Нет данных для сохранения!");
            } else {
                try {
                    currentSeries.writeInFile("file.txt");
                } catch (IOException ex) {
                    jInfoArea.setText("Ошибка при сохранении файла!");
                }
            }
        }
    });


    getJElementButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                if (currentSeries != null) {
                    int j = Integer.parseInt(jIndex.getText());
                    if (j > 0 && j <= currentSeries.n) {
                        double element = currentSeries.getBi(j - 1);
                        DecimalFormat df = new DecimalFormat("#.###"); // Максимум 3 знака после запятой
                        jInfoArea.append("\n" + "Элемент " + j + ": " + df.format(element));
                    } else {
                        jInfoArea.setText("Индекс вне диапазона");
                    }
                } else {
                    jInfoArea.setText("Сначала выберите прогрессию");
                }
            } catch (NumberFormatException ex) {
                jInfoArea.setText("Введите корректный индекс j");
            }
        }
    });


    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Class<? extends Series> seriesClass
    private void createSeries(String seriesType, JButton saveButton, JButton getJElementButton) {
        try {
            double step = Double.parseDouble(jStep.getText());
            double b1 = Double.parseDouble(jB1.getText());
            int n = Integer.parseInt(jN.getText());
            //currentSeries = seriesClass.getConstructor(double.class, double.class, int.class).newInstance(step, b1, n);
            //метод используется для получения объекта Constructor, представляющего конструктор указанного класса
            //.class используется, чтобы получить объект класса, который представляет именно примитивный тип double, а не его обертку
            
            //В Java каждый примитивный тип имеет соответствующий объект Class, 
            //который используется для получения информации о типе и для создания экземпляров.

            if ("liner".equals(seriesType)) {
                currentSeries = new Liner(step, b1, n);
            } else if ("exponential".equals(seriesType)) {
                currentSeries = new Exponential(step, b1, n);
            }

            jInfoArea.setText("Элементы прогрессии " + seriesType + ": " + "\n" +
                    currentSeries.toString() + "\n" + "Сумма: " + currentSeries.sum()); // seriesClass.getSimpleName()
            saveButton.setEnabled(true);
            getJElementButton.setEnabled(true);
        } catch (NumberFormatException ex) {
            jInfoArea.setText("Введите корректные значения");
        } catch (Exception ex) {
            jInfoArea.setText("Ошибка при создании последовательности");
        }
    }

}


abstract class Series{
    double step; // шаг
    double b1; // первое значение
    int n; // количество элементов
    
    Series(double step, double b1, int n){
        this.step = step;
        this.b1 = b1;
        this.n = n;
    };
    
    // метод для получения j элемента
    abstract double getBi(int num);

    public String sum() {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += getBi(i);
        }
        DecimalFormat df = new DecimalFormat("#.###"); // Максимум 3 знака после запятой
        return df.format(sum);
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            DecimalFormat df = new DecimalFormat("#.###"); // Максимум 3 знака после запятой
            sb.append(df.format(getBi(i))).append("  ");        
        }
        return sb.toString();
    }


    public void writeInFile(String fileName) throws IOException{
        FileWriter writer = new FileWriter(fileName);
        writer.write("Элементы прогрессии: " + toString() + "\n");
        writer.write("Сумма: " + sum());
        writer.close();
    }
};


class Liner extends Series{
    public Liner(double step, double b1, int n){
        super(step, b1, n);
    }

    public double getBi(int num) {
        return b1 + num * step;
    }
}


class Exponential extends Series {
    
    public Exponential(double step, double b1, int n) {
        super(step, b1, n);
    }

    public double getBi(int num) {
        return b1 * Math.pow(step, num);
    }
}

