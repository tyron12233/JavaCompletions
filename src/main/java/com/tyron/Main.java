package com.tyron;

import com.google.common.collect.ImmutableList;
import com.tyron.javacompletion.tool.Indexer;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        Indexer.createIndex(ImmutableList.of(new File("C:\\Users\\bounc\\IdeaProjects\\JavaCompGradle\\indices\\android\\31\\android-sources.jar")),
                new File("C:\\Users\\bounc\\IdeaProjects\\JavaCompGradle\\indices\\android\\31\\index.json"));
    }
}
