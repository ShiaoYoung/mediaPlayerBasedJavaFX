package com.rome4306.myMedioPlayer;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileRead {
    private FileChooser fileChooser;
    private File file;
    private String initPath;

    public FileRead() {
        initPath = "C:/";
        file = new File(initPath);
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(file);
    }
    public FileRead(String init_path) {
        initPath = init_path;
        file = new File(initPath);
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(file);
    }

    public File getFile(String title) {
        fileChooser.setTitle(title);
        //文件选择器样式
        Stage stage = new Stage();
        stage.setHeight(1000);

        file = fileChooser.showOpenDialog(stage);
        return file;
    }
}
