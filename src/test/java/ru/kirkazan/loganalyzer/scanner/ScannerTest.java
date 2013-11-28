package ru.kirkazan.loganalyzer.scanner;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kirkazan.loganalyzer.test.FileSystemTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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
    public void testPlainFileScanner() {
        File inputFolder = getReadOnlyTestFile("filescanner/plain");

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
        File inputFolder = getReadOnlyTestFile("filescanner/recursive");

        FileScanner scanner = new FileScanner(inputFolder.getAbsolutePath(), true);

        Set<ScannedFile> actual = scanner.scan();
        Set<ScannedFile> expected = new LinkedHashSet<ScannedFile>();
        expected.add(new ScannedFile(inputFolder, "a/aa/1.log"));
        expected.add(new ScannedFile(inputFolder, "a/aa/2.log"));
        expected.add(new ScannedFile(inputFolder, "a/1.log"));
        expected.add(new ScannedFile(inputFolder, "a/2.log"));
        expected.add(new ScannedFile(inputFolder, "b/1.log"));
        expected.add(new ScannedFile(inputFolder, "1.log"));
        expected.add(new ScannedFile(inputFolder, "2.txt"));
        assertEquals(expected, actual);
    }

}
