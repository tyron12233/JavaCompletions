package org.openjdk.tools.javac.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.openjdk.tools.javac.util.Context;

/**
 * Get meta-info about files. Default direct (non-caching) implementation.
 * @see CacheFSInfo
 *
 * <p><b>This is NOT part of any supported API.
 * If you write code that depends on this, you do so at your own risk.
 * This code and its internal interfaces are subject to change or
 * deletion without notice.</b>
 */
public class FSInfo {

    /** Get the FSInfo instance for this context.
     *  @param context the context
     *  @return the Paths instance for this context
     */
    public static org.openjdk.tools.javac.file.FSInfo instance(Context context) {
        org.openjdk.tools.javac.file.FSInfo instance = context.get(org.openjdk.tools.javac.file.FSInfo.class);
        if (instance == null)
            instance = new org.openjdk.tools.javac.file.FSInfo();
        return instance;
    }

    protected FSInfo() {
    }

    protected FSInfo(Context context) {
        context.put(org.openjdk.tools.javac.file.FSInfo.class, this);
    }

    public File getCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file.getAbsoluteFile();
        }
    }

    public Path getCanonicalFile(Path path) {
        return getCanonicalFile(path.toFile()).toPath();
    }
    public boolean exists(File file) {
        return file.exists();
    }

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    public boolean isFile(File file) {
        return file.isFile();
    }

    public boolean isFile(Path path) {
        return Files.isRegularFile(path);
    }

    public List<Path> getJarClassPath(Path path) throws IOException {
        return getJarClassPath(path.toFile()).stream().map(File::toPath).collect(Collectors.toList());
    }

    public List<File> getJarClassPath(File file) throws IOException {
        String parent = file.getParent();
        JarFile jarFile = new JarFile(file);
        try {
            Manifest man = jarFile.getManifest();
            if (man == null)
                return Collections.emptyList();

            Attributes attr = man.getMainAttributes();
            if (attr == null)
                return Collections.emptyList();

            String path = attr.getValue(Attributes.Name.CLASS_PATH);
            if (path == null)
                return Collections.emptyList();

            List<File> list = new ArrayList<File>();

            for (StringTokenizer st = new StringTokenizer(path); st.hasMoreTokens(); ) {
                String elt = st.nextToken();
                File f = (parent == null ? new File(elt) : new File(parent, elt));
                list.add(f);
            }

            return list;
        } finally {
            jarFile.close();
        }
    }

    private FileSystemProvider jarFSProvider;

    public synchronized FileSystemProvider getJarFSProvider() {
        if (jarFSProvider != null) {
            return jarFSProvider;
        }
        for (FileSystemProvider provider: FileSystemProvider.installedProviders()) {
            if (provider.getScheme().equals("jar")) {
                return (jarFSProvider = provider);
            }
        }
        return null;
    }
}
