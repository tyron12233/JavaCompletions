package com.tyron.javacompletion.completion;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.tyron.javacompletion.file.SimpleFileManager;
import com.tyron.javacompletion.model.FileScope;
import com.tyron.javacompletion.model.Module;
import com.tyron.javacompletion.options.IndexOptions;
import com.tyron.javacompletion.parser.AstScanner;
import com.tyron.javacompletion.parser.ParserContext;
import com.tyron.javacompletion.project.PositionContext;
import com.tyron.javacompletion.project.SimpleModuleManager;
import org.junit.jupiter.api.Test;
import org.openjdk.source.tree.LineMap;
import org.openjdk.tools.javac.tree.JCTree.JCCompilationUnit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

public abstract class CompletorTest {
    private static final String COMPLETION_POINT_MARK = "/** @complete */";
    private static final String INSERTION_POINT_MARK = "/** @insert */";
    private static String TEST_DATA_DIR = "";

    private final SimpleFileManager fileManager = new SimpleFileManager();
    private final SimpleModuleManager moduleManager = new SimpleModuleManager();
    
    public CompletorTest() {
        TEST_DATA_DIR = getTestDirectory();
    }

    public String getTestDirectory() {
        return Paths.get(".").toFile().getAbsolutePath() + "/src/test/java/com" +
                "/tyron/javacompletion/completion/testdata/";
    }

    protected Path getInputFilePath(String filename) {
        return Paths.get(TEST_DATA_DIR + filename).toAbsolutePath();
    }

    protected List<CompletionCandidate> completeTestFile(String filename) {
        String testDataContent = getFileContent(filename);
        return completeContent(filename, testDataContent);
    }

    protected String getFileContent(String filename) {
        Optional<CharSequence> fileContent = fileManager.getFileContent(getInputFilePath(filename));
        if (fileContent.isPresent()) {
            return fileContent.get().toString();
        }
        Path path = getInputFilePath(filename);
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected CompletionParams createCompletionParams(
            String inputFilename, String testDataContent, String... otherFiles) {

        Path inputFilePath = getInputFilePath(inputFilename);
        ParserContext parserContext = new ParserContext();
        // FileContentFixer fileContentFixer = new FileContentFixer(parserContext);
        // parserContext.setupLoggingSource(inputFilename);

        assertThat(testDataContent).contains(COMPLETION_POINT_MARK);
        int completionPoint = testDataContent.indexOf(COMPLETION_POINT_MARK);
        testDataContent = testDataContent.replace(COMPLETION_POINT_MARK, "");

        LineMap lineMap = parserContext.tokenize(testDataContent, false).getLineMap();
        // Completion line and column numbers are 0-based, while LineMap values are 1-based.
        int line = (int) lineMap.getLineNumber(completionPoint) - 1;
        int column = (int) lineMap.getColumnNumber(completionPoint) - 1;


        moduleManager.getFileManager().openFileForSnapshot(inputFilePath.toUri(), testDataContent);
        moduleManager.addOrUpdateFile(inputFilePath, /* fixContentForParsing= */ true);

        Module otherModule = new Module();
        moduleManager.addDependingModule(otherModule);

        otherModule.addDependingModule(moduleManager.getModule());

        List<String> otherFilesWithObject =
                new ImmutableList.Builder<String>().add(otherFiles).add("Object.java").build();
        for (String otherFile : otherFilesWithObject) {
            String content = getFileContent(otherFile);
            JCCompilationUnit otherCompilationUnit = parserContext.parse(otherFile, content);
            FileScope fileScope =
                    new AstScanner(IndexOptions.FULL_INDEX_BUILDER.build())
                            .startScan(otherCompilationUnit, otherFile, content);
            otherModule.addOrReplaceFileScope(fileScope);
        }

        return new CompletionParams(line, column);
    }

    protected void assertCompletion(String filename, String toComplete, String... expectedCandidates) {
        assertCompletion(filename, ImmutableList.of(toComplete), expectedCandidates);
    }

    protected void assertCompletion(
            String filename, List<String> toCompleteCases, String... expectedCandidates) {
        for (String toComplete : toCompleteCases) {
            List<CompletionCandidate> candidates = completeWithContent(filename, toComplete);
            assertThat(extractCompletionPrefixWithContent(filename, toComplete))
                    .isEqualTo("");
            assertThat(getCandidateNames(candidates))
                    .containsExactly((Object[]) expectedCandidates);
        }

        Multimap<Character, String> candidatePrefixMap = ArrayListMultimap.create();
        for (String candidate : expectedCandidates) {
            char prefix = candidate.charAt(0);
            char lowerPrefix = Character.toLowerCase(prefix);
            char upperPrefix = Character.toUpperCase(prefix);
            candidatePrefixMap.put(lowerPrefix, candidate);
            candidatePrefixMap.put(upperPrefix, candidate);
        }

        for (String toComplete : toCompleteCases) {
            int dotPos = toComplete.indexOf("." + COMPLETION_POINT_MARK);
            if (dotPos == -1) {
                continue;
            }

            for (char prefix : candidatePrefixMap.keySet()) {
                String toCompleteWithMember =
                        toComplete.substring(0, dotPos + 1) + prefix + toComplete.substring(dotPos + 1);
                List<CompletionCandidate> candidates = completeWithContent(filename, toCompleteWithMember);
                assertThat(extractCompletionPrefixWithContent(filename, toCompleteWithMember))
                        .isEqualTo("" + prefix);
                assertThat(getCandidateNames(candidates))
                        .containsExactlyElementsIn(candidatePrefixMap.get(prefix));
            }
        }
    }

    protected List<CompletionCandidate> completeContent(
            String inputFilename, String testDataContent, String... otherFiles) {
        CompletionParams params = createCompletionParams(inputFilename, testDataContent, otherFiles);
        return new Completor(moduleManager.getFileManager())
                .getCompletionResult(
                        moduleManager, getInputFilePath(inputFilename), params.line, params.column)
                .getCompletionCandidates();
    }

    protected static List<String> getCandidateNames(List<CompletionCandidate> candidates) {
        return candidates.stream().map(CompletionCandidate::getName).collect(Collectors.toList());
    }

    protected List<CompletionCandidate> completeWithContent(
            String filename, String toInsert, String... otherFiles) {
        String testDataContent = getFileContent(filename);
        String newContent = testDataContent.replace(INSERTION_POINT_MARK, toInsert);
        return completeContent(filename, newContent, otherFiles);
    }

    protected String extractCompletionPrefixWithContent(String filename, String toInsert) {
        String testDataContent = getFileContent(filename);
        String newContent = testDataContent.replace(INSERTION_POINT_MARK, toInsert);
        assertThat(newContent).contains(COMPLETION_POINT_MARK);
        CompletionParams params = createCompletionParams(filename, newContent);
        Path filePath = getInputFilePath(filename);
        PositionContext positionContext =
                PositionContext.createForPosition(moduleManager, filePath, params.line, params.column)
                        .get();
        return ContentWithLineMap.create(
                        positionContext.getFileScope(), moduleManager.getFileManager(), filePath)
                .extractCompletionPrefix(params.line, params.column);
    }

    private static class CompletionParams {
        private final int line;
        private final int column;

        private CompletionParams(int line, int column) {
            this.line = line;
            this.column = column;
        }
    }
}
