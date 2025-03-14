
package com.example.ncode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class GenerateDocumentation extends AnAction {
    private static GenerateDocumentationToolWindowContent content = null;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Get the current project
        Project project = e.getProject();
        if (project == null) return;

        // Get the editor
        Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        if (editor == null) return;

        // Get selected text
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();

        // Ensure selectedText is not null
        if (selectedText == null || selectedText.isEmpty()) {
            return;
        }

        // Get or create the tool window
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Generate Documentation");

        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Generate Documentation", true, ToolWindowAnchor.RIGHT);
        }

        // If the content is null, create a new instance and add it to the tool window
        if (content == null) {
            content = new GenerateDocumentationToolWindowContent(project);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content toolWindowContent = contentFactory.createContent(content, "", false);
            toolWindow.getContentManager().addContent(toolWindowContent);
        }

        // Update the existing panel with the new selected code
        content.setSelectedCode(selectedText);

        toolWindow.activate(null);
    }
}
