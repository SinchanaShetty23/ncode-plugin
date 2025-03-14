//package com.example.ncode;
//
//import javax.swing.*;
//import java.awt.*;
//import javax.swing.border.*;
//
//public class GenerateTestCaseToolWindowContent extends JPanel {
//    private JTextPane selectedCodeArea;
//    private JTextPane testCaseOutputArea;
//
//    public GenerateTestCaseToolWindowContent() {
//        setLayout(new BorderLayout(15, 15)); // Better spacing
//        setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding around content
//        setBackground(new Color(15, 15, 15, 230)); // Subtle transparency for a modern look
//
//        // === Selected Code Area ===
//        selectedCodeArea = createTextPane(new Color(45, 45, 45), Color.WHITE, new Font("JetBrains Mono", Font.BOLD, 12));
//        selectedCodeArea.setBorder(createRoundedBorder("Selected Code"));
//
//        JScrollPane codeScrollPane = createStyledScrollPane(selectedCodeArea, new Dimension(500, 90)); // Smaller height
//        add(codeScrollPane, BorderLayout.NORTH);
//
//        // === Test Case Output Area ===
//        testCaseOutputArea = createTextPane(new Color(30, 30, 30), new Color(200, 200, 200), new Font("SansSerif", Font.PLAIN, 14));
//        testCaseOutputArea.setBorder(createRoundedBorder("Generated Test Cases"));
//
//        JScrollPane outputScrollPane = createStyledScrollPane(testCaseOutputArea, new Dimension(500, 280)); // More space
//        add(outputScrollPane, BorderLayout.CENTER);
//
//        // Smooth shadow effect
//        setDropShadowEffect(this);
//
//        // Dummy Output
//        setDummyTestCases();
//    }
//
//    public void setSelectedCode(String code) {
//        selectedCodeArea.setText(code);
//        setDummyTestCases(); // Generate test cases dynamically
//    }
//
//    private void setDummyTestCases() {
//        String dummyText = "\nTest Case 1: ✅ Passed\nTest Case 2: ✅ Passed\nTest Case 3: ❌ Failed\n\n";
//        testCaseOutputArea.setText(dummyText);
//    }
//
//    private JTextPane createTextPane(Color bgColor, Color fgColor, Font font) {
//        JTextPane textPane = new JTextPane();
//        textPane.setEditable(false);
//        textPane.setBackground(bgColor);
//        textPane.setForeground(fgColor);
//        textPane.setFont(font);
//        textPane.setMargin(new Insets(10, 10, 10, 10));
//        return textPane;
//    }
//
//    private JScrollPane createStyledScrollPane(JTextPane textPane, Dimension dimension) {
//        JScrollPane scrollPane = new JScrollPane(textPane);
//        scrollPane.setPreferredSize(dimension);
//        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
//        scrollPane.getViewport().setBackground(textPane.getBackground());
//        return scrollPane;
//    }
//
//    private Border createRoundedBorder(String title) {
//        Border line = new LineBorder(new Color(120, 120, 120), 1, true);
//        Border empty = new EmptyBorder(10, 10, 10, 10);
//        return BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(line, empty), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14), Color.WHITE);
//    }
//
//    private void setDropShadowEffect(JComponent component) {
//        component.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(100, 100, 100, 150)), // Subtle shadow
//                BorderFactory.createEmptyBorder(10, 10, 10, 10)
//        ));
//    }
//
//    public JPanel getPanel() {
//        return this;
//    }
//}

package com.example.ncode;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class GenerateTestCaseToolWindowContent extends JPanel {
    private JTextPane selectedCodeArea;
    private JTextPane testCaseOutputArea;

    public GenerateTestCaseToolWindowContent() {
        setLayout(new BorderLayout(15, 15)); // Better spacing
        setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding around content
        setBackground(new Color(15, 15, 15, 230)); // Subtle transparency

        // === Selected Code Area ===
        selectedCodeArea = createTextPane(new Color(45, 45, 45), Color.WHITE, new Font("JetBrains Mono", Font.BOLD, 12));
        selectedCodeArea.setBorder(createRoundedBorder("Selected Code"));

        JScrollPane codeScrollPane = createStyledScrollPane(selectedCodeArea, new Dimension(500, 90)); // Smaller height
        add(codeScrollPane, BorderLayout.NORTH);

        // === Test Case Output Area ===
        testCaseOutputArea = createTextPane(new Color(30, 30, 30), new Color(200, 200, 200), new Font("SansSerif", Font.PLAIN, 14));
        testCaseOutputArea.setBorder(createRoundedBorder("Generated Test Cases"));

        JScrollPane outputScrollPane = createStyledScrollPane(testCaseOutputArea, new Dimension(500, 280)); // More space
        add(outputScrollPane, BorderLayout.CENTER);

        // Smooth shadow effect
        setDropShadowEffect(this);
    }

    public void setSelectedCode(String code) {
        selectedCodeArea.setText(code);
        generateTestCases(code); // Auto-generate test cases when code is set
    }

    private void generateTestCases(String code) {
        if (code.trim().isEmpty()) {
            testCaseOutputArea.setText("⚠️ No code selected for test case generation.");
            return;
        }

        // Display loading message
        testCaseOutputArea.setText("⏳ Generating test cases...");

        // Prepare prompt
        String prompt = "Generate test cases for the following Java function:\n\n" + code;

        // Run API call in a background thread
        new Thread(() -> {
            String aiResponse = VertexAIChatbot.getVertexAIResponse(prompt);

            // Update UI on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                testCaseOutputArea.setText(aiResponse.isEmpty() ? "⚠️ No response from AI." : aiResponse);
            });
        }).start();
    }

    private JTextPane createTextPane(Color bgColor, Color fgColor, Font font) {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(bgColor);
        textPane.setForeground(fgColor);
        textPane.setFont(font);
        textPane.setMargin(new Insets(10, 10, 10, 10));
        return textPane;
    }

    private JScrollPane createStyledScrollPane(JTextPane textPane, Dimension dimension) {
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(dimension);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        scrollPane.getViewport().setBackground(textPane.getBackground());
        return scrollPane;
    }

    private Border createRoundedBorder(String title) {
        Border line = new LineBorder(new Color(120, 120, 120), 1, true);
        Border empty = new EmptyBorder(10, 10, 10, 10);
        return BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(line, empty), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14), Color.WHITE);
    }

    private void setDropShadowEffect(JComponent component) {
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(100, 100, 100, 150)), // Subtle shadow
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    public JPanel getPanel() {
        return this;
    }
}
