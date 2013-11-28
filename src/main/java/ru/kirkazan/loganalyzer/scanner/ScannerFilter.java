package ru.kirkazan.loganalyzer.scanner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @author esadykov
 * @since 28.11.13 20:19
 */
public class ScannerFilter {
    private final Pattern filePattern;
    private final SimpleDateFormat dateFormat;

    public ScannerFilter(String filePattern) {
        this(filePattern, null);
    }

    public ScannerFilter(String filePattern, String dateFormat) {
        if (filePattern == null || filePattern.isEmpty())
            throw new IllegalArgumentException("filePattern can not be null");

        this.filePattern = Pattern.compile(filePattern);

        if (dateFormat != null && !filePattern.isEmpty())
            this.dateFormat = new SimpleDateFormat(dateFormat);
        else
            this.dateFormat = null;
    }

    public boolean filter(File f) {
        return filePattern.matcher(f.getName()).matches();
    }
}
