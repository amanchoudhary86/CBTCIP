import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class NumberGuessingGame {
    private final JTextField guessField;
    private final JLabel resultLabel;
    private final JLabel hintLabel;
    private final JButton guessButton;
    private final JButton tryAgainButton;
    private int numberToGuess;
    private int attemptsLeft;

    public NumberGuessingGame() {
        numberToGuess = new Random().nextInt(101);
        attemptsLeft = 10;

        JFrame frame = new JFrame("Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JLabel instructionsLabel = new JLabel("Guess a number between 0 and 100:");
        topPanel.add(instructionsLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));
        guessField = new JTextField(10);
        centerPanel.add(guessField);
        guessButton = new JButton("Guess");
        guessButton.addActionListener(new GuessButtonListener());
        centerPanel.add(guessButton);
        frame.add(centerPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("You have " + attemptsLeft + " attempts left.");
        frame.add(resultLabel, BorderLayout.SOUTH);

        hintLabel = new JLabel("");
        frame.add(hintLabel, BorderLayout.EAST);

        tryAgainButton = new JButton("Try Again");
        tryAgainButton.addActionListener(new TryAgainButtonListener());
        tryAgainButton.setVisible(false);
        frame.add(tryAgainButton, BorderLayout.WEST);

        frame.setSize(400, 200);
        frame.setVisible(true);
    }

    private class GuessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int guess = Integer.parseInt(guessField.getText());
            guessField.setText(""); // clear the text field

            if (guess < 0 || guess > 100) {
                resultLabel.setText("Invalid input! Please enter a number between 0 and 100.");
            } else if (guess == numberToGuess) {
                resultLabel.setText("Congratulations! You guessed the number correctly.");
                guessButton.setEnabled(false);
                tryAgainButton.setVisible(true);
            } else {
                attemptsLeft--;
                if (attemptsLeft > 0) {
                    resultLabel.setText("You have " + attemptsLeft + " attempts left. Try again!");
                    if (Math.abs(guess - numberToGuess) <= 10) {
                        hintLabel.setText("You're very close!");
                    } else if (guess > numberToGuess) {
                        hintLabel.setText("You're too high!");
                    } else {
                        hintLabel.setText("You're too low!");
                    }
                } else {
                    resultLabel.setText("You didn't guess the number. The correct answer was " + numberToGuess + ".");
                    guessButton.setEnabled(false);
                    tryAgainButton.setVisible(true);
                }
            }
        }
    }

    private class TryAgainButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            numberToGuess = new Random().nextInt(101);
            attemptsLeft = 10;
            resultLabel.setText("You have " + attemptsLeft + " attempts left.");
            hintLabel.setText("");
            guessButton.setEnabled(true);
            tryAgainButton.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingGame::new);
    }
}