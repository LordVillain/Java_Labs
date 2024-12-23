// М - модель: BinaryTree (пассивная модель, выполняет только хранение и обработку данных)
// V - вид: View (предоставляет интерфейс взаимодействия с пользователем)
// C - контроллер: Controller (обрабатывает взаимодействие между моделью и видом)

public class Main {
    public static void main(String[] args) {
        
        BinaryTree model = new BinaryTree();
        View view = new View();
        new Controller(model, view);
    }
}
