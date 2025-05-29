//===================================================================
// SaveFile.java
//      Description:
//          The purpose of this class is to handle saving output to
//          a specified file path.
//
// Created by etruyj
//===================================================================

package com.socialvagrancy.utils.ui.display;

import com.socialvagrancy.utils.ui.structures.OutputFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SaveFile {
    public static void toPath(List<OutputFormat> toSave, String outputFormat, String filePath) {
        try {
            // Expand the file path.
            filePath = filePath.replace("\\", "\\\\"); // replace Windows dir \ with \\ to allow parsing.
            filePath = filePath.replace("~", System.getProperty("user.home"));

            Path path = Paths.get(filePath);

            List<String> output = Table.format(toSave, outputFormat);

            Files.write(path, output);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to save to file: " + filePath);
        }
    }
}
