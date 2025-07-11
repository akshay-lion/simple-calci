import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    private TextField display = new TextField();
    private double num1 = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void start(Stage primaryStage) {
        display.setEditable(false);
        display.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        display.setPrefHeight(50);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(5);

        String[] buttons = {
            "7", "8", "9", "/", 
            "4", "5", "6", "*", 
            "1", "2", "3", "-", 
            "0", "C", "=", "+"
        };

        int row = 0, col = 0;
        for (String text : buttons) {
            Button btn = new Button(text);
            btn.setPrefSize(60, 60);
            btn.setStyle("-fx-font-size: 16px;");
            btn.setOnAction(e -> handleInput(text));
            grid.add(btn, col, row);

            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        VBox root = new VBox(10, display, grid);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            String key = e.getText();
            if (key.matches("[0-9]")) handleInput(key);
            else if ("+-*/".contains(key)) handleInput(key);
            else if (e.getCode().toString().equals("ENTER")) handleInput("=");
            else if (key.equalsIgnoreCase("c")) handleInput("C");
        });

        primaryStage.setTitle("JavaFX Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleInput(String text) {
        switch (text) {
            case "C":
                display.clear();
                num1 = 0;
                operator = "";
                startNewNumber = true;
                break;

            case "=":
                try {
                    double num2 = Double.parseDouble(display.getText());
                    double result = calculate(num1, num2, operator);
                    display.setText(String.valueOf(result));
                    startNewNumber = true;
                } catch (NumberFormatException | ArithmeticException ex) {
                    display.setText("Error");
                }
                break;

            case "+": case "-": case "*": case "/":
                try {
                    num1 = Double.parseDouble(display.getText());
                    operator = text;
                    startNewNumber = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
                break;

            default: // numbers
                if (startNewNumber) {
                    display.clear();
                    startNewNumber = false;
                }
                display.appendText(text);
                break;
        }
    }

    private double calculate(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new ArithmeticException("Divide by zero");
                yield a / b;
            }
            default -> 0;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}