//
//package com.example.ncode;
//
//import javax.swing.*;
//import java.awt.*;
//import javax.swing.border.*;
//import com.intellij.openapi.project.Project;
//import javax.swing.text.*;
//
//public class GenerateDocumentationToolWindowContent extends JPanel {
//    private JTextPane selectedCodeArea;
//    private JTextPane documentationOutputArea;
//
//    public GenerateDocumentationToolWindowContent(Project project) {
//        setLayout(new BorderLayout(15, 15)); // Cleaner layout
//        setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding for spacing
//        setBackground(new Color(15, 15, 15, 230)); // Subtle transparency
//
//        // === Selected Code Area (Compact) ===
//        selectedCodeArea = createTextPane(new Color(45, 45, 45), Color.WHITE, new Font("JetBrains Mono", Font.BOLD, 12));
//        selectedCodeArea.setBorder(createRoundedBorder("Selected Code"));
//
//        JScrollPane codeScrollPane = createStyledScrollPane(selectedCodeArea, new Dimension(500, 90)); // Smaller height
//        add(codeScrollPane, BorderLayout.NORTH);
//
//        // === Documentation Output Area (More Focus) ===
//        documentationOutputArea = createTextPane(new Color(30, 30, 30), new Color(200, 200, 200), new Font("SansSerif", Font.PLAIN, 14));
//        documentationOutputArea.setBorder(createRoundedBorder("Generated Documentation"));
//
//        JScrollPane outputScrollPane = createStyledScrollPane(documentationOutputArea, new Dimension(500, 280)); // More space
//        add(outputScrollPane, BorderLayout.CENTER);
//
//        // Smooth shadow effect
//        setDropShadowEffect(this);
//
//        // Dummy Output
//        setDummyDocumentation();
//    }
//
//    public void setSelectedCode(String code) {
//        selectedCodeArea.setText(code);
//    }
//
//    private void setDummyDocumentation() {
//        String dummyText = "\n\uD83D\uDE80This is auto-generated documentation for the selected code.\n\n";
//        documentationOutputArea.setText(dummyText);
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
//
//

package com.example.ncode;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import com.intellij.openapi.project.Project;
import javax.swing.text.*;

public class GenerateDocumentationToolWindowContent extends JPanel {
    private JTextPane selectedCodeArea;
    private JTextPane documentationOutputArea;

    public GenerateDocumentationToolWindowContent(Project project) {
        setLayout(new BorderLayout(15, 15)); // Cleaner layout
        setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding for spacing
        setBackground(new Color(15, 15, 15, 230)); // Subtle transparency

        // === Selected Code Area (Compact) ===
        selectedCodeArea = createTextPane(new Color(45, 45, 45), Color.WHITE, new Font("JetBrains Mono", Font.BOLD, 12));
        selectedCodeArea.setBorder(createRoundedBorder("Selected Code"));

        JScrollPane codeScrollPane = createStyledScrollPane(selectedCodeArea, new Dimension(500, 90)); // Smaller height
        add(codeScrollPane, BorderLayout.NORTH);

        // === Documentation Output Area (More Focus) ===
        documentationOutputArea = createTextPane(new Color(30, 30, 30), new Color(200, 200, 200), new Font("SansSerif", Font.PLAIN, 14));
        documentationOutputArea.setBorder(createRoundedBorder("Generated Documentation"));

        JScrollPane outputScrollPane = createStyledScrollPane(documentationOutputArea, new Dimension(500, 280)); // More space
        add(outputScrollPane, BorderLayout.CENTER);

        // Smooth shadow effect
        setDropShadowEffect(this);
    }

    public void setSelectedCode(String code) {
        selectedCodeArea.setText(code);
        generateDocumentation(code); // Auto-generate documentation when code is set
    }

    private void generateDocumentation(String code) {
        if (code.trim().isEmpty()) {
            documentationOutputArea.setText("⚠️ No code selected for documentation.");
            return;
        }

        // Display loading message
        documentationOutputArea.setText("⏳ Generating documentation...");

        // Prepare prompt
        String prompt = "Generate documentation for the following Java code:\n\n" + code;

        // Run API call in a separate thread
        new Thread(() -> {
            String aiResponse = VertexAIChatbot.getVertexAIResponse(prompt);

            // Update UI on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                documentationOutputArea.setText(aiResponse.isEmpty() ? "⚠️ No response from AI." : aiResponse);
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
