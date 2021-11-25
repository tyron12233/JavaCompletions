package com.tyron.javacompletion;

import com.tyron.javacompletion.completion.CompletionCandidate;
import com.tyron.javacompletion.completion.CompletionResult;
import com.tyron.javacompletion.completion.Completor;
import com.tyron.javacompletion.file.FileManager;
import com.tyron.javacompletion.file.SimpleFileManager;
import com.tyron.javacompletion.model.FileScope;
import com.tyron.javacompletion.model.Module;
import com.tyron.javacompletion.options.IndexOptions;
import com.tyron.javacompletion.options.JavaCompletionOptions;
import com.tyron.javacompletion.parser.AstScanner;
import com.tyron.javacompletion.parser.ParserContext;
import com.tyron.javacompletion.project.ModuleManager;
import com.tyron.javacompletion.project.Project;
import com.tyron.javacompletion.project.SimpleModuleManager;
import com.tyron.javacompletion.storage.IndexStore;
import com.tyron.javacompletion.tool.Indexer;
import org.openjdk.source.tree.LineMap;
import org.openjdk.tools.javac.tree.JCTree;

import javax.annotation.Nullable;
import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

public class Main {
    private static final String COMPLETION_POINT_MARK = "/** @complete */";
    private static final String INSERTION_POINT_MARK = "/** @insert */";
    private static String TEST_CONTENT = "class Main {\n" +
            "   private static final Runnable FIELD = () -> { };\n" +
            "   private static void test(Runnable runnable) { }\n" +
            "   public static void main(String[] args) {\n" +
            "      " + COMPLETION_POINT_MARK + ";\n" +
            "   }\n" +
            "}";
    private static final Path TEST_PATh = Paths.get("Test.java");

    private static final SimpleModuleManager moduleManager = new SimpleModuleManager();
    private static final FileManager fileManager = new SimpleFileManager();

    public static void main(String[] args) {
        JavaCompletions completions = new JavaCompletions();

        Path root = Paths.get("C:\\Users\\bounc\\IdeaProjects\\JavaCompGradle");
        completions.initialize(root.toUri(), new JavaCompletionOptions() {
            @Nullable
            @Override
            public String getLogPath() {
                return null;
            }

            @Nullable
            @Override
            public Level getLogLevel() {
                return null;
            }

            @Override
            public List<String> getIgnorePaths() {
                return null;
            }

            @Override
            public List<String> getTypeIndexFiles() {
                return null;
            }
        });
        CompletionResult completionResult = completions.getProject().getCompletionResult(Paths.get("C:\\Users\\bounc\\IdeaProjects\\JavaCompGradle\\src\\main\\java\\com\\tyron\\javacompletion\\Main.java"),
                73, 4);
        completionResult.getCompletionCandidates().stream().map(CompletionCandidate::getName)
                .forEach(System.out::println);
    }

}
