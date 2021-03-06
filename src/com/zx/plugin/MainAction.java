package com.zx.plugin;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;
import com.zx.plugin.entity.TaskHolder;
import com.zx.plugin.process.FileProcessor;
import com.zx.plugin.process.PrefixProcessor;
import com.zx.plugin.process.TextFormatProcessor;
import com.zx.plugin.process.finder.AbsFieldFinder;
import com.zx.plugin.process.finder.JavaFieldFinder;
import com.zx.plugin.process.finder.LayoutXmlFieldFinder;
import com.zx.plugin.ui.FieldsDialog;

public class MainAction extends BaseGenerateAction {

    @SuppressWarnings("unused")
    public MainAction() {
        super(null);
    }

    @SuppressWarnings("unused")
    public MainAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForClass(final PsiClass targetClass) {
        return super.isValidForClass(targetClass);
    }

    @Override
    public boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return true;
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (project == null || editor == null) {
            return;
        }
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (psiFile == null) {
            return;
        }
        PsiClass psiClass = getTargetClass(editor, psiFile);
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

        AbsFieldFinder fieldFinder;
        FileType fileType = psiFile.getFileType();
        if (fileType instanceof XmlFileType) {
            fieldFinder = new LayoutXmlFieldFinder();
        } else if (fileType instanceof JavaFileType) {
            fieldFinder = new JavaFieldFinder();
        } else {
            return;
        }
//
//        ModuleManager instance = ModuleManager.getInstance(project);
//        Module[] modules = instance.getModules();

        //??????
        TaskHolder taskHolder = fieldFinder.find(psiFile);

        FileProcessor.INSTANCE.process(project, psiFile, taskHolder);
        //??????
        TextFormatProcessor.INSTANCE.process(taskHolder);
        //??????
        PrefixProcessor.INSTANCE.refreshDefaultPrefix(project, psiFile, taskHolder);

        FieldsDialog dialog = new FieldsDialog(factory, psiClass, psiFile, project, taskHolder);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);


    }

}
