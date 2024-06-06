package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

class UserService {
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        users.put("user1", new User("user1", "password123", "John Doe"));
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public void updateProfile(String oldUsername, String fullName, String newUsername, String newPassword) {
        User user = users.get(oldUsername);
        if (user != null) {
            if (!newUsername.isEmpty()) {
                users.remove(oldUsername);
                user.setUsername(newUsername);
                users.put(newUsername, user);
            }
            if (!fullName.isEmpty()) {
                user.setFullName(fullName);
            }
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }
        }
    }

    public User getUser(String username) {
        return users.get(username);
    }
}

class ExamService {
    private final Map<Integer, String> questions = new HashMap<>();
    private final Map<Integer, String> userAnswers = new HashMap<>();
    private boolean examSubmitted = false;

    public ExamService() {
        questions.put(1, "Q1: What is the capital of Italy?\nA) Rome\nB) Paris\nC) London\nD) Madrid");
        questions.put(2, "Q2: What is 2 + 2?\nA) 3\nB) 4\nC) 5\nD) 6");
        questions.put(3, "Q3: What is the color of the sky?\nA) Blue\nB) Green\nC) Red\nD) Yellow");
    }

    public void selectAnswer(int questionId, String answer) {
        if (!examSubmitted) {
            userAnswers.put(questionId, answer);
        }
    }

    public void submitExam() {
        examSubmitted = true;
        System.out.println("Exam submitted!");
    }

    public Map<Integer, String> getQuestions() {
        return questions;
    }

    public Map<Integer, String> getUserAnswers() {
        return userAnswers;
    }

    public boolean isExamSubmitted() {
        return examSubmitted;
    }
}

public class OnlineExamination extends JFrame {
    private final UserService userService = new UserService();
    private final ExamService examService = new ExamService();
    private String currentUser;
    private int currentQuestionIndex = 1;
    private Timer timer;

    public OnlineExamination() {
        setTitle("Online Exam");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showLoginScreen();
    }

    private void showLoginScreen() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());
            if (userService.login(username, password)) {
                currentUser = username;
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        });

        loginPanel.add(userLabel);
        loginPanel.add(userText);
        loginPanel.add(passLabel);
        loginPanel.add(passText);
        loginPanel.add(loginButton);

        setContentPane(loginPanel);
        revalidate();
    }

    private void showMainMenu() {
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new GridLayout(3, 1));

        JButton updateProfileButton = new JButton("Update Profile");
        updateProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUpdateProfileScreen();
            }
        });

        JButton startExamButton = new JButton("Start Exam");
        startExamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startExam();
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentUser = null;
                showLoginScreen();
            }
        });

        mainMenu.add(updateProfileButton);
        mainMenu.add(startExamButton);
        mainMenu.add(logoutButton);

        setContentPane(mainMenu);
        revalidate();
    }

    private void showUpdateProfileScreen() {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameText = new JTextField();
        JLabel newUsernameLabel = new JLabel("New Username:");
        JTextField newUsernameText = new JTextField();
        JLabel newPassLabel = new JLabel("New Password:");
        JPasswordField newPassText = new JPasswordField();
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String fullName = nameText.getText();
            String newUsername = newUsernameText.getText();
            String newPassword = new String(newPassText.getPassword());
            userService.updateProfile(currentUser, fullName, newUsername, newPassword);
            currentUser = newUsername.isEmpty()? currentUser : newUsername;
            JOptionPane.showMessageDialog(null, "Profile updated.");
            showMainMenu();
        });

        profilePanel.add(nameLabel);
        profilePanel.add(nameText);
        profilePanel.add(newUsernameLabel);
        profilePanel.add(newUsernameText);
        profilePanel.add(newPassLabel);
        profilePanel.add(newPassText);
        profilePanel.add(updateButton);

        setContentPane(profilePanel);
        revalidate();
    }

    private void startExam() {
        currentQuestionIndex = 1;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int seconds = 60;

            public void run() {
                if (seconds > 0) {
                    seconds--;
                    setTitle("Online Exam - Time remaining: " + seconds + " seconds");
                } else {
                    timer.cancel();
                    examService.submitExam();
                    JOptionPane.showMessageDialog(null, "Time's up! Exam submitted automatically.");
                    showMainMenu();
                }
            }
        }, 0, 1000);
        showQuestionScreen();
    }

    private void showQuestionScreen() {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(7, 1));

        JLabel questionLabel = new JLabel("<html>" + examService.getQuestions().get(currentQuestionIndex)  + "</html>");
        questionPanel.add(questionLabel);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));

        JRadioButton optionA = new JRadioButton("A");
        JRadioButton optionB = new JRadioButton("B");
        JRadioButton optionC = new JRadioButton("C");
        JRadioButton optionD = new JRadioButton("D");

        optionsPanel.add(optionA);
        optionsPanel.add(optionB);
        optionsPanel.add(optionC);
        optionsPanel.add(optionD);

        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        questionPanel.add(optionsPanel);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String answer = "";
                if (optionA.isSelected()) {
                    answer = "A";
                } else if (optionB.isSelected()) {
                    answer = "B";
                } else if (optionC.isSelected()) {
                    answer = "C";
                } else if (optionD.isSelected()) {
                    answer = "D";
                }
                examService.selectAnswer(currentQuestionIndex, answer);

                if (currentQuestionIndex < examService.getQuestions().size()) {
                    currentQuestionIndex++;
                    showQuestionScreen();
                } else {
                    examService.submitExam();
                    timer.cancel();
                    JOptionPane.showMessageDialog(null, "Exam submitted!");
                    showMainMenu();
                }
            }
        });

        questionPanel.add(nextButton);

        setContentPane(questionPanel);
        revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new OnlineExamination().setVisible(true);
            }
        });
    }
}