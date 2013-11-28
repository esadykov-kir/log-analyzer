package ru.kirkazan.loganalyzer.scanner;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kirkazan.loganalyzer.test.FileSystemTest;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author esadykov
 * @since 28.11.13 17:51
 */
public class ScannerTest extends FileSystemTest {
    Logger log = LoggerFactory.getLogger(ScannerTest.class);

    @Test
    public void detectDefaultFolder() {

        try {
            File foobar = getReadOnlyTestFile("foobar");
            log.info("absolute path for foobar: {}", foobar.getAbsolutePath());
            log.info("canonical path for foobar: {}", foobar.getCanonicalPath());
        } catch (IOException e) {
            log.error("error on detectDefaultFolder", e);
        }
    }

    @Test
    public void testFileScannerExceptions() {

        try {
            new FileScanner(null);
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testPlainFileScanner() {
        File inputFolder = getReadOnlyTestFile("filescanner");

        FileScanner scanner = new FileScanner(inputFolder.getAbsolutePath());

        Set<ScannedFile> files = scanner.scan();
        Object[] expected = Arrays.asList(
                new ScannedFile(inputFolder, "1.log"),
                new ScannedFile(inputFolder, "2.txt")
        ).toArray();
        assertArrayEquals(expected, files.toArray());
    }

    @Test
    public void testRecursiveFileScanner() {
        File inputFolder = getReadOnlyTestFile("filescanner");

        FileScanner scanner = new FileScanner(inputFolder.getAbsolutePath(), true);

        Set<ScannedFile> actual = scanner.scan();
        Set<ScannedFile> expected = new LinkedHashSet<ScannedFile>();
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-08.log"));
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-09.log"));
        expected.add(new ScannedFile(inputFolder, "a/1.log"));
        expected.add(new ScannedFile(inputFolder, "a/2.log"));
        expected.add(new ScannedFile(inputFolder, "b/1.log"));
        expected.add(new ScannedFile(inputFolder, "1.log"));
        expected.add(new ScannedFile(inputFolder, "2.txt"));
        assertEquals(expected, actual);
    }

    @Test
    public void testSingleFilterFileScanner() {
        File inputFolder = getReadOnlyTestFile("filescanner");

        Set<ScannerFilter> filter = new HashSet<ScannerFilter>();
        filter.add(new ScannerFilter("2.*.log"));

        FileScanner scanner = new FileScanner(inputFolder.getAbsolutePath(), true, filter);

        Set<ScannedFile> actual = scanner.scan();
        Set<ScannedFile> expected = new LinkedHashSet<ScannedFile>();
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-08.log"));
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-09.log"));
        expected.add(new ScannedFile(inputFolder, "a/2.log"));
        assertEquals(expected, actual);
    }

    @Test
    public void testMultiFilterFileScanner() {
        File inputFolder = getReadOnlyTestFile("filescanner");

        Set<ScannerFilter> filter = new HashSet<ScannerFilter>();
        filter.add(new ScannerFilter("2.*.log"));
        filter.add(new ScannerFilter(".*txt"));
        FileScanner scanner = new FileScanner(inputFolder.getAbsolutePath(), true, filter);

        Set<ScannedFile> actual = scanner.scan();
        Set<ScannedFile> expected = new LinkedHashSet<ScannedFile>();
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-08.log"));
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-09.log"));
        expected.add(new ScannedFile(inputFolder, "a/2.log"));
        expected.add(new ScannedFile(inputFolder, "2.txt"));
        assertEquals(expected, actual);
    }

    @Test
    public void testComplexFileScanner() {
        File inputFolder = getReadOnlyTestFile("filescanner");

        Set<ScannerFilter> filter = new HashSet<ScannerFilter>();
        filter.add(new ScannerFilter("2.(\\d\\d\\d\\d-\\d\\d-\\d\\d).log", "yyyy-MM-dd"));
        filter.add(new ScannerFilter(".*txt"));
        FileScanner scanner = new FileScanner(inputFolder.getAbsolutePath(), true, filter);

        Set<ScannedFile> actual = scanner.scan();
        Set<ScannedFile> expected = new LinkedHashSet<ScannedFile>();
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-08.log", new Date(2008 - 1900, 11 - 1, 8)));
        expected.add(new ScannedFile(inputFolder, "a/aa/2.2008-11-09.log", new Date(2008 - 1900, 11 - 1, 9)));
        expected.add(new ScannedFile(inputFolder, "2.txt"));
        assertEquals(expected, actual);
    }

}
