package com.ejadaSolutions.common.utils.files;

import com.ejadaSolutions.common.utils.logs.MyLogger;
import com.ejadaSolutions.common.utils.properties.PropertiesManager;
import org.apache.logging.log4j.core.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * this class used for interacting with files
 *
 * @author Mahmoud Osama
 */
public class FileUtil {
    public static File dir_folder = null;
    private static Logger log = new MyLogger().getLogger();
    private final PropertiesManager propertiesManager = new PropertiesManager();

    public FileUtil() {
        ClassLoader classloader = FileUtil.class.getClassLoader();
        projDirFolder(classloader.getResource("").getPath().replace("test-classes/", "classes/"));
    }

    /**
     * create a directory folder for framework
     *
     * @param f_name directory name
     * @return directory folder
     */
    File projDirFolder(String f_name) {
        dir_folder = new File(f_name);
        return dir_folder;
    }

    /**
     * get framework directory file object
     *
     * @return dir_folder
     */
    public File getProjDirFolder() {
        return dir_folder;
    }

    /**
     * create directory folder
     *
     * @param dirName directory path
     * @return directory file object
     */
    public synchronized File createDir(String dirName) {
        File dir = new File(dirName);
        if (dir.exists()) {
            log.info("Found directory folder : {}", dir.getPath());
        } else {
            if (dir.mkdirs()) {
                log.info("Directory folder created : {}", dir.getPath());
            } else {
                log.info("Directory folder could not be created!");
            }
        }
        return dir;
    }

    /**
     * search file in a given path
     *
     * @param dirPath directory path
     * @param fname   file name
     * @return file path if found
     */
    public String searchDir(String dirPath, String fname) {
        final String[] trgtPath = new String[1];
        ClassLoader loader = FileUtil.class.getClassLoader();
        File file = new File(loader.getResource("").getPath().replace("target/test-classes/", dirPath));
        if (file.isDirectory()) {
            try {
                Files.walk(file.toPath()).filter(Files::isRegularFile).forEach(f -> {
                    if (f.getFileName().toString().equals(fname)) {
                        trgtPath[0] = f.toAbsolutePath().toString();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            trgtPath[0] = file.getAbsolutePath();
        }
        return trgtPath[0];

    }

    /**
     * create a file inside framework folder directory
     *
     * @param filename file path
     * @return created file object
     */
    public File createFile(String directory, String filename) {
        File f;
        synchronized (filename.intern()) {
            f = new File(directory + File.separator + filename);
            if (f.exists()) {
                log.info("{} exist.", filename);
            } else {
                try {
                    f.createNewFile();
                    log.info("{} created.", filename);
                } catch (IOException e) {
                    log.catching(e);
                }
            }
        }
        return f;
    }

    /**
     * copy file from source to destination
     *
     * @param source source path
     * @param dest   destination path
     */
    public void copyFile(File source, File dest) {
        try {
            InputStream input = new FileInputStream(source);
            OutputStream output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            input.close();
            output.close();
        } catch (IOException e) {
            log.catching(e);
        }
    }

    public String getResPath(String fileName) {
        return searchDir("target", fileName);
    }

    public String readFile(String directory, String fileName) {
        ClassLoader classloader = FileUtil.class.getClassLoader();
        String content = "";
        try (InputStream in = classloader.getResourceAsStream(Paths.get(directory, fileName).toString().replace("test-classes", "classes"));
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content = content + line + "\n";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public File getFile(String filePath, String fileName, String ext) {
        return new File(propertiesManager.getProp("test.data.path") + filePath + fileName + ext);
    }

    public void writeFile(String filePath, String fileName, String ext, String fileContent) {
        try {
            FileWriter fileWriter = new FileWriter(propertiesManager.getProp("test.data.path") + filePath + fileName + ext);
            fileWriter.write(fileContent);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFileWithSync(File file, String text) {
        synchronized (file) {
            try (FileWriter out = new FileWriter(file)) {
                out.write(text);
            } catch (IOException e) {
                log.catching(e);
            }
        }
    }
}
