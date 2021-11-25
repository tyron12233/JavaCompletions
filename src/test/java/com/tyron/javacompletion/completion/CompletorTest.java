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

public class CompletorTest {
    private static final String COMPLETION_POINT_MARK = "/** @complete */";
    private static final String INSERTION_POINT_MARK = "/** @insert */";
    private static String TEST_DATA_DIR = "";

    private final SimpleFileManager fileManager = new SimpleFileManager();
    private final SimpleModuleManager moduleManager = new SimpleModuleManager();
    
    public CompletorTest() {
        TEST_DATA_DIR = Paths.get(".").toFile().getAbsolutePath() + "/src/test/java/com" +
                "/tyron/javacompletion/completion/testdata/";
    }

    private Path getInputFilePath(String filename) {
        return Paths.get(TEST_DATA_DIR + filename).toAbsolutePath();
    }

    private List<CompletionCandidate> completeTestFile(String filename) {
        String testDataContent = getFileContent(filename);
        return completeContent(filename, testDataContent);
    }

    private String getFileContent(String filename) {
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

    private CompletionParams createCompletionParams(
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

    private void assertCompletion(String filename, String toComplete, String... expectedCandidates) {
        assertCompletion(filename, ImmutableList.of(toComplete), expectedCandidates);
    }

    private void assertCompletion(
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

    private List<CompletionCandidate> completeContent(
            String inputFilename, String testDataContent, String... otherFiles) {
        CompletionParams params = createCompletionParams(inputFilename, testDataContent, otherFiles);
        return new Completor(moduleManager.getFileManager())
                .getCompletionResult(
                        moduleManager, getInputFilePath(inputFilename), params.line, params.column)
                .getCompletionCandidates();
    }

    private static List<String> getCandidateNames(List<CompletionCandidate> candidates) {
        return candidates.stream().map(CompletionCandidate::getName).collect(Collectors.toList());
    }

    private List<CompletionCandidate> completeWithContent(
            String filename, String toInsert, String... otherFiles) {
        String testDataContent = getFileContent(filename);
        String newContent = testDataContent.replace(INSERTION_POINT_MARK, toInsert);
        return completeContent(filename, newContent, otherFiles);
    }

    private String extractCompletionPrefixWithContent(String filename, String toInsert) {
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

    /** TESTS **/

    @Test
    public void completeNewStatement() throws Exception {
        List<String> keywords =
                Arrays.stream(KeywordCompletionCandidate.values())
                        .map(KeywordCompletionCandidate::getName)
                        .collect(Collectors.toList());
        List<String> expectedMembers =
                ImmutableList.of(
                        "CompleteNewStatement",
                        "param1",
                        "stringParam",
                        "CONSTANT",
                        "InnerClass",
                        "List",
                        "subClassMemberField",
                        "memberField",
                        "memberMethod",
                        "staticMethod",
                        "staticMethod", // TODO: Fix duplicate.
                        "com",
                        // From java.lang
                        "java",
                        "Object",
                        // From Object
                        "toString",
                        "toString");
        assertThat(getCandidateNames(completeTestFile("CompleteNewStatement.java")))
                .containsExactlyElementsIn(Iterables.concat(expectedMembers, keywords));
    }

    @Test
    public void completeMemberSelection() throws Exception {
        String baseAboveCompletion = "above./** @complete */";
        List<String> aboveCases =
                ImmutableList.of(baseAboveCompletion, baseAboveCompletion + "\nabove.aboveMethod();");
        assertCompletion("CompleteInMethod.java", aboveCases, "aboveField", "aboveMethod", "toString");

        String baseBelowCompletion = "below./** @complete */";
        List<String> belowCases =
                ImmutableList.of(
                        baseBelowCompletion,
                        baseBelowCompletion + "\nbelow.belowMethod();",
                        "above.;" + baseBelowCompletion,
                        "self.new BelowClass()./** @complete */");
        assertCompletion("CompleteInMethod.java", belowCases, "belowField", "belowMethod", "toString");
    }

    @Test
    public void completeStaticMemberSelection() throws Exception {
        assertCompletion(
                "CompleteInMethod.java",
                ImmutableList.of("BelowClass./** @complete */"),
                "STATIC_BELOW_FIELD",
                "staticBelowMethod");
        assertCompletion(
                "CompleteInMethod.java",
                ImmutableList.of("CompleteInMethod./** @complete */"),
                "AboveClass",
                "BelowClass",
                "STATIC_FIELD",
                "staticMethod",
                "staticMethod" /* overload of staticMethod */);
    }

    @Test
    public void completeMethodReference() throws Exception {
        assertCompletion(
                "CompleteInMethod.java",
                ImmutableList.of("CompleteInMethod::/** @complete */"),
                "staticMethod");
        assertCompletion(
                "CompleteInMethod.java",
                ImmutableList.of("this::/** @complete */"),
                "completeMethod",
                "toString");
    }

    @Test
    public void completeTwoDots() throws Exception {
        String toComplete = "above./** @complete */.something";

        List<CompletionCandidate> candidates = completeWithContent("CompleteInMethod.java", toComplete);
        assertThat(getCandidateNames(candidates))
                .containsAtLeast("aboveField", "aboveMethod", "toString");
    }

    @Test
    public void completeMemberSelectionInOtherFile() throws Exception {
        List<CompletionCandidate> candidates =
                completeWithContent(
                        "CompleteInMethod.java",
                        "new OtherClass().innerClass./** @complete */",
                        "OtherClass.java");
        assertThat(getCandidateNames(candidates))
                .containsExactly("innerInnerClass", "getInnerInnerClass", "toString");
    }

    @Test
    public void completeImport() throws Exception {
        String baseImportCompletion = "import com.tyron.javacompletion./** @complete */";
        List<String> cases =
                ImmutableList.of(
                        baseImportCompletion,
                        baseImportCompletion + "\nimport java.util.List;",
                        "import java.util.List;\n" + baseImportCompletion);
        assertCompletion("CompleteOutOfClass.java", cases, "completion");
    }

    @Test
    public void completeImportPackage() throws Exception {
        List<String> cases =
                ImmutableList.of(
                        "import com.tyron.javacompletion.completion./** @complete */;",
                        "import static com.tyron.javacompletion.completion./** @complete */;");
        assertCompletion("CompleteOutOfClass.java", cases, "testdata");
    }

    @Test
    public void completeImportInnerClass() throws Exception {
        String toComplete = "import com.tyron.javacompletion.completion.testdata.OtherClass./** @complete */";
        List<CompletionCandidate> candidates =
                completeWithContent("CompleteOutOfClass.java", toComplete, "OtherClass.java");
        assertThat(getCandidateNames(candidates)).containsExactly("InnerClass");
    }

    @Test
    public void completeWithTypeCast() throws Exception {
        assertThat(
                getCandidateNames(
                        completeWithContent(
                                "CompleteInMethod.java", "((BelowClass) above)./** @complete */")))
                .contains("belowField");
    }
}
