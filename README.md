# JavaCompletions
Java code completion library

This is is a standalone libary based on [JavaComp](https://github.com/tigersoldier/JavaComp) to provide Android based IDEs code completion for java. 

## Usage

Initialize the project
```java
JavaCompletions completions = new JavaCompletions();
completions.initialize(projectRootURI, new JavaCompletionOptionsImpl());
```

Getting completions
```java
Path file = /** Path to file **/
CompletionResult result = completions.getProject()
    .getCompletionResult(file, 0 /** line **/, 0 /** column **/);
```

## Indexing
The libary serializes the parse tree and class information into a json file. To produce these json files, The Indexer is used.
```java
Indexer#createIndex(List<String> jarFiles, File outputFile)
```
You can then pass the generated path as a parameter into JavaCompletionOptionsImpl()
