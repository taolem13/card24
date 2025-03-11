package org.example.card24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class Card24Controller {
    private static final String IMAGE_PATH = "/PlayingCards/";
    private static final String[] SUITS = {"clubs", "diamonds", "hearts", "spades"};

    @FXML
    private ImageView c1;

    @FXML
    private ImageView c2;

    @FXML
    private ImageView c3;

    @FXML
    private ImageView c4;

    @FXML
    private Button hintBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button verifyBtn;

    @FXML
    private TextField txtField;


    @FXML
    void onhintBtnPress(ActionEvent event) {

    }
    @FXML
    private void initialize() {
        displayRandomCards(); //Show cards when the UI loads
    }

    @FXML
    void onrefreshBtnPress(ActionEvent event) {
        displayRandomCards();
        txtField.clear(); // Reset user input when refreshing
    }

    @FXML
    void onverifyBtnPress(ActionEvent event) {
        String expression = txtField.getText().replaceAll("\\s", ""); // Remove spaces
        if (expression.isEmpty()) {
            showAlert("Invalid Input", "Please enter an expression.");
            return;
        }

        if (!isValidExpression(expression)) {
            showAlert("Invalid Input", "Only numbers and +, -, *, / are allowed.");
            return;
        }

        if (!usesAllCardValues(expression)) {
            showAlert("Invalid Input", "Your expression must use all four numbers exactly once.");
            return;
        }

        try {
            double result = evaluateExpression(expression);
            if (Math.abs(result - 24) < 1e-6) { // Allow small floating-point error
                showAlert("Correct!", "Your expression evaluates to 24!");
            } else {
                showAlert("Incorrect", "Your expression evaluates to " + result + ", not 24.");
            }
        } catch (Exception e) {
            showAlert("Error", "Invalid mathematical expression.");
        }
    }
    private Integer[] cardValues = new Integer[4];

    private void displayRandomCards() {
        Random rand = new Random();
        Set<Integer> selectedCards = new HashSet<>();

        // Select 4 unique random values from 1-13 with random suits
        while (selectedCards.size() < 4) {
            selectedCards.add(rand.nextInt(13) + 1);
        }

        cardValues = selectedCards.toArray(new Integer[0]); // Store for game logic

//        Load images and put in ImageViews
        c1.setImage(loadCardImage(cardValues[0]));
        c2.setImage(loadCardImage(cardValues[1]));
        c3.setImage(loadCardImage(cardValues[2]));
        c4.setImage(loadCardImage(cardValues[3]));
    }

    private Image loadCardImage(int cardValue) {
        Random rand = new Random();
        String suit = SUITS[rand.nextInt(SUITS.length)]; // Randomly pick a suit
        String imagePath = IMAGE_PATH + cardValue + "_" + suit + ".png";
        return new Image(getClass().getResourceAsStream(imagePath));
    }

    public Integer[] getCardValues() {
        return cardValues; // This can be used for verification logic later
    }

    private boolean isValidExpression(String expression) {
        return expression.matches("[0-9+\\-*/()]+");
    }

    private boolean usesAllCardValues(String expression) {
        List<Integer> numbersUsed = extractNumbers(expression);
        List<Integer> cardList = new ArrayList<>(Arrays.asList(cardValues));

        Collections.sort(numbersUsed);
        Collections.sort(cardList);

        return numbersUsed.equals(cardList);
    }

    private List<Integer> extractNumbers(String expression) {
        List<Integer> numbers = new ArrayList<>();
        String[] tokens = expression.split("[+\\-*/()]"); // Split by operators
        for (String token : tokens) {
            if (!token.isEmpty()) {
                try {
                    numbers.add(Integer.parseInt(token));
                } catch (NumberFormatException ignored) {}
            }
        }
        return numbers;
    }

    private double evaluateExpression(String expression) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        return (double) engine.eval(expression);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
