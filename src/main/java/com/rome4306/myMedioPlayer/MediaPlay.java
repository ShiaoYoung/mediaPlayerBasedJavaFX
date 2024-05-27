package com.rome4306.myMedioPlayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

public class MediaPlay {
    private File file;
    int type; // 0-> music, 1-> video
    Media media;
    MediaPlayer mediaPlayer;

    public MediaPlay(File file, int file_type) {
        this.file = file;
        this.type = file_type;
        try {
            media = new Media(this.file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            System.out.println("文件错误！！！");
            throw new RuntimeException(e);
        }
        mediaPlayer = new MediaPlayer(media);
    }
    public int getType() {return type;}


}
