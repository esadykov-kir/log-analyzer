package ru.kirkazan.loganalyzer.scanner;

import java.io.File;
import java.util.Date;

/**
 * @author esadykov
 * @since 28.11.13 18:31
 */
public class ScannedFile {
    private File file;
    private Date date;

    public ScannedFile(String file) {
        this(new File(file));
    }

    public ScannedFile(File parent, String file) {
        this(new File(parent, file));
    }

    public ScannedFile(File file) {
        this(file, (Date) null);
    }

    public ScannedFile(File file, Date date) {
        this.file = file.getAbsoluteFile();
        this.date = date;
    }

    public File getFile() {
        return file;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ScannedFile{" +
                "file=" + file +
                ", date=" + date +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScannedFile that = (ScannedFile) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (!file.equals(that.file)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = file.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
