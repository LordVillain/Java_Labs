import java.util.List;
import java.io.IOException;

public class Controller {
    private final BinaryTree model;
    private final View view;

    public Controller(BinaryTree model, View view) {
        this.model = model;
        this.view = view;

        view.addAddButtonListener(e -> addNode());
        view.addMinButtonListener(e -> findMin());
        view.addMoveToMinButtonListener(e -> moveToMin());
        view.addPreOrderButtonListener(e -> showPreOrder());
        view.addSaveButtonListener(e -> saveTree());
        // view.addVisitButtonListener(e -> visitTree());
    }

    private void addNode() {
        int value = view.getInputValue();
        model.add(value);
        view.updateTreeArray(model.getTreeArray());
    }

    private void findMin() {
        int minTraversal = model.minUsingTraversal();
        int minStream = model.minUsingStream();
        view.showMin(minTraversal, minStream);
    }

    private void moveToMin() {
        List<Integer> path = model.moveToMin();
        view.showMoveToMin(path);
    }

    private void showPreOrder() {
        List<Integer> preOrder = model.preOrderTraversal();
        view.showPreOrder(preOrder);
    }

    private void saveTree() {
        try {
            model.save("tree.txt");
            view.showSaveSuccess();
        } catch (IOException e) {
            view.showSaveError(e.getMessage());
        }
    }

    // private void visitTree() {
    //     PrintVisitor printVisitor = new PrintVisitor();
    //     model.accept(printVisitor);
    //     String visitResult = printVisitor.getResult();
    //     view.showVisitResult(visitResult);
    // }
}
