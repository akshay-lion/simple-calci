import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleCalculator extends Application {

    private TextField display = new TextField();
    private String operand1 = "";
    private String operator = "";
    private boolean startNewInput = true;

    @Override
    public void start(Stage stage) {
        display.setEditable(false);
        display.setPrefHeight(50);
        display.setStyle("-fx-font-size: 20px;");

        GridPane grid = createGrid();

        VBox root = new VBox(10, display, grid);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 320, 450);
        stage.setTitle("Simple Calculator");
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        String[][] buttons = {
            {"C", "←", "±", "%"},
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String label = buttons[row][col];
                Button btn = new Button(label);
                btn.setPrefSize(60, 60);
                btn.setStyle("-fx-font-size: 18px;");
                btn.setOnAction(e -> handleInput(label));
                grid.add(btn, col, row);
            }
        }

        return grid;
    }

    private void handleInput(String value) {
        switch (value) {
            case "C" -> {
                display.clear();
                operand1 = "";
                operator = "";
                startNewInput = true;
            }
            case "←" -> {
                String text = display.getText();
                if (!text.isEmpty()) {
                    display.setText(text.substring(0, text.length() - 1));
                }
            }
            case "±" -> {
                String text = display.getText();
                if (!text.isEmpty()) {
                    if (text.startsWith("-")) {
                        display.setText(text.substring(1));
                    } else {
                        display.setText("-" + text);
                    }
                }
            }
            case "%" -> {
                try {
                    double val = Double.parseDouble(display.getText());
                    display.setText(formatResult(val / 100));
                } catch (Exception e) {
                    display.setText("Error");
                }
            }
            case "+", "-", "*", "/" -> {
                operand1 = display.getText();
                operator = value;
                startNewInput = true;
            }
            case "=" -> {
                try {
                    double num1 = Double.parseDouble(operand1);
                    double num2 = Double.parseDouble(display.getText());
                    double result = calculate(num1, num2, operator);
                    display.setText(formatResult(result));
                    startNewInput = true;
                } catch (Exception e) {
                    display.setText("Error");
                }
            }
            case "." -> {
                if (!display.getText().contains(".")) {
                    display.appendText(".");
                }
            }
            default -> {
                if (startNewInput) {
                    display.clear();
                    startNewInput = false;
                }
                display.appendText(value);
            }
        }
    }

    private double calculate(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> (b == 0) ? Double.NaN : a / b;
            default -> 0;
        };
    }

    private String formatResult(double result) {
        if (result == (long) result)
            return String.format("%d", (long) result);
        else
            return String.format("%.6f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
