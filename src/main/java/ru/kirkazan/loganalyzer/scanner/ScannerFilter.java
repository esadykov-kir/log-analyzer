package ru.kirkazan.loganalyzer.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author esadykov
 * @since 28.11.13 20:19
 */
public class ScannerFilter {
    Logger log = LoggerFactory.getLogger(ScannerFilter.class);
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

    public ScannedFile filter(File f) {
        Matcher matcher = filePattern.matcher(f.getName());
        if (!matcher.matches())
            return null;
        if (dateFormat == null)
            return new ScannedFile(f);

        Date date = null;
        try {
            date = dateFormat.parse(matcher.group(1));
        } catch (ParseException e) {
            log.warn("could not parse date: {} for file {} and filter {}",
                    matcher.group(1), f.getAbsolutePath(), filePattern.pattern());
        }
        return new ScannedFile(f, date);
    }
}
