package ru.kirkazan.loganalyzer.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author esadykov
 * @since 28.11.13 18:24
 */
public class FileScanner {
    Logger log = LoggerFactory.getLogger(FileScanner.class);

    private final File folder;
    private final boolean recursive;
    private final Set<ScannerFilter> filter;

    public FileScanner(String folder) {
        this(folder, false, null);
    }

    public FileScanner(String folder, boolean recursive) {
        this(folder, recursive, null);
    }

    public FileScanner(String folder, boolean recursive, Set<ScannerFilter> filter) {
        if (folder == null)
            throw new IllegalArgumentException("folder can not be null");
        this.folder = new File(folder);
        if (!this.folder.canRead())
            throw new IllegalArgumentException(MessageFormat.format("folder {0} can not be read", folder));
        this.recursive = recursive;
        if (filter != null) {
            this.filter = new HashSet<ScannerFilter>(filter);
        } else
            this.filter = Collections.emptySet();

        log.debug("new scanner for folder: {} recursive is: {}", folder, true);
    }

    public Set<ScannedFile> scan() {
        return scan(this.folder);
    }

    private Set<ScannedFile> scan(File folder) {
        File[] files = folder.listFiles();
        if (files == null)
            return Collections.emptySet();
        Set<ScannedFile> scannedFiles = new LinkedHashSet<ScannedFile>(files.length);
        for (File f : files) {
            if (f.isFile()) {
                if (FileScanner.this.filter.isEmpty())
                    scannedFiles.add(new ScannedFile(f));
                else {
                    ScannedFile filteredFile;
                    for (ScannerFilter filterItem : FileScanner.this.filter) {
                        filteredFile = filterItem.filter(f);
                        if (filteredFile != null) {
                            scannedFiles.add(filteredFile);
                            break;
                        }
                    }
                }
            } else if (recursive && f.isDirectory())
                scannedFiles.addAll(scan(f));
        }
        return scannedFiles;
    }
}
