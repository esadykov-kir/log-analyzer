package ru.kirkazan.loganalyzer.test;

import java.io.File;

/**
 * @author esadykov
 * @since 28.11.13 18:12
 */
public abstract class FileSystemTest {

    public static final File READ_ONLY_TEST_FOLDER = new File("src/test/resources/test/");

    /**
     * "ReadOnly" - only convention
     *
     * @param relativePath relative path will be cocatenated with base test folder
     * @return file in base test folder
     */
    public File getReadOnlyTestFile(String relativePath) {
        if (relativePath == null || relativePath.isEmpty())
            throw new IllegalArgumentException("relativePath can not be null or empty");
        return new File(READ_ONLY_TEST_FOLDER, relativePath);
    }
}
