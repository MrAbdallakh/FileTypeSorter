import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;

public class FileTypeSorter {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String scPath;

        while (true) {
            System.out.print("Folder: ");
            scPath = sc.nextLine();

            if (scPath.equals("exit")) {
                break;
            }

            Path path = Path.of(scPath);
            File fileOne = new File(scPath);
            int fileCount = 0;

            //=============================================================
            // Count how many files are in the folder
            //
            for (File fileNames : Objects.requireNonNull(fileOne.listFiles())) {

                if (fileNames.isFile()) {
                    fileCount++;
                }
            }

            if (!Files.isDirectory(path) || fileCount == 0) {

                System.err.println("\nIt should be a directory or there should be some files!\n");

            } else {
                sortFiles(scPath, path);
            }
        }
    }


    private static void sortFiles(String scPath, Path path) {
        //=============================================================
        // Count how many files are in the folder and check which files
        // are existing in directory
        //
        File folder = new File(scPath);
        String[] files = new String[Objects.requireNonNull(folder.listFiles()).length];
        int fileCount = 0;


        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {

            if (fileEntry.isFile()) {
                files[fileCount] = String.valueOf(fileEntry);
                fileCount++;
            }
        }


        //=============================================================
        // Look which kind of end Data it is
        //
        String[] endFile = new String[Objects.requireNonNull(folder.listFiles()).length];
        int endDATAposition = 0;
        boolean exist;

        try {
            for (String file : files) {
                exist = false;

                if (file == null) {
                    break;
                }

                //=============================================================
                // endFile take the type of the file, not the name of file
                //

                String[] line = file.split("\\.");

                if (endDATAposition == 0) {
                    endFile[endDATAposition] = line[1];
                    endDATAposition++;

                } else {

                    for (int i = 0; endFile[i] != null; i++) {
                        if (endFile[i].equals(line[1])) {
                            exist = true;
                            break;

                        }
                    }

                    if (!exist) {
                        endFile[endDATAposition] = line[1];
                        endDATAposition++;
                    }
                }
            }


            //=============================================================
            // Create folder for the type file and put the files with the
            // same type
            //
            StringBuilder temporaryNewName = new StringBuilder();

            for (String stringTypeFile : endFile) {

                if (stringTypeFile == null) {
                    break;
                }

                File dir = new File(path + "\\." + stringTypeFile);

                if (!dir.exists()) {
                    dir.mkdir();
                }

                for (String file : files) {

                    if (file == null) {
                        break;
                    }

                    String[] splitFiles = file.split("\\.");

                    //=============================================================
                    // Create a default file and change the name and give him
                    // the information from the previous file
                    //

                    if (splitFiles[1].equals(stringTypeFile)) {
                        String filePath = dir + "\\def." + stringTypeFile;
                        File createNewFile = new File(filePath);
                        createNewFile.createNewFile();

                        Files.move(Paths.get(file), Paths.get(String.valueOf(createNewFile)), StandardCopyOption.ATOMIC_MOVE);

                        //=============================================================
                        // Give the default file the real name from the previous file
                        //

                        for (int i = splitFiles[0].length() - 1; splitFiles[0].charAt(i) != '\\'; i--) {

                            temporaryNewName.append(splitFiles[0].charAt(i));
                        }

                        String[] name = temporaryNewName.toString().split("");
                        String help;

                        for (int j = 0, k = name.length - 1; j < name.length; j++, k--) {

                            if (name.length % 2 == 0 && j < k) {
                                help = name[j];
                                name[j] = name[k];
                                name[k] = help;

                            } else if (name.length % 2 == 1 && j != k) {
                                help = name[j];
                                name[j] = name[k];
                                name[k] = help;

                            } else {
                                break;
                            }
                        }

                        temporaryNewName.delete(0, temporaryNewName.length());

                        for (int i = 0; i < name.length; i++) {

                            temporaryNewName.append(name[i]);
                        }

                        File newNameString = new File(dir + "\\" + temporaryNewName + "." + stringTypeFile);

                        while (true) {
                            if (newNameString.exists()) {
                                temporaryNewName.append(" - Copy");
                                newNameString = new File(dir + "\\" + temporaryNewName + "." + stringTypeFile);
                            } else {
                                break;
                            }
                        }

                        createNewFile.renameTo(newNameString);
                        temporaryNewName.delete(0, temporaryNewName.length());
                    }
                }
            }
            System.out.println("\nFiles are sorted\n");
        } catch (IOException e) {
            System.err.println("\nError to sort the files!\n");
        }
    }
}