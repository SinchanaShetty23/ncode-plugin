
package com.example.ncode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class GenerateTestCase extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        if (editor == null) return;

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();

        if (selectedText == null || selectedText.isEmpty()) {
            return;
        }

        // Create or update the tool window content
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Generate Test Cases");

        if (toolWindow == null) {
            toolWindow = toolWindowManager.registerToolWindow("Generate Test Cases", true, ToolWindowAnchor.RIGHT);
        }

        GenerateTestCaseToolWindowContent content = new GenerateTestCaseToolWindowContent();
        content.setSelectedCode(selectedText);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content toolWindowContent = contentFactory.createContent(content, "", false);

        toolWindow.getContentManager().removeAllContents(true);
        toolWindow.getContentManager().addContent(toolWindowContent);
        toolWindow.activate(null);
    }
}
