package com.rome4306.myMedioPlayer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;

public class myMediaPlayer extends Application {

    // 屏幕信息
    Rectangle2D primaryScreenBounds;

    // 布局
    VBox rVBox = new VBox();
    VBox vBoxUp = new VBox();
    VBox vBoxDown = new VBox();
    HBox hBoxPgsBar = new HBox();
    HBox hBoxBtn = new HBox();


    // 媒体文件
    File curFile;
    int curPlay = 0; // 0->music, 1->video

    // 播放器组件
    Media media = null;
    MediaPlayer player;
    MediaView mediaView;

    // 播放器控件
    Slider sliderBar;
    boolean isPlay = true;
    double totalLen;
    Label curTime;
    Label totalTime;
    Button play_pause;
    Button go_ahead;
    Button go_back;

    @Override
    public void init() throws Exception {
        // 屏幕信息
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        // 测试
        curFile = new File("D:\\桌面\\大学\\面向对象开发技术\\实验\\素材\\[DHR][Uma Musume][01][BIG5][720P][AVC_AAC].mp4");
        curPlay = 1;
        // 媒体画面
        mediaView = new MediaView(player);
        mediaView.setFitWidth(primaryScreenBounds.getWidth());
        mediaView.setFitHeight(500);
        vBoxUp.setMinHeight(700);
        // 进度条
        sliderBar = new Slider();
        sliderBar.setPrefHeight(5);
        sliderBar.setPrefWidth(primaryScreenBounds.getWidth()*0.92);
        sliderBar.setValue(0);
        // 时长
        curTime = new Label("00:00:00");
        totalTime = new Label("00:00:00");
        // 控件
        play_pause = new Button();
        go_ahead = new Button();
        go_back = new Button();
        play_pause.setStyle("-fx-background-radius: 1000px;" + "-fx-background-insets: 0, 0 0 0 0;" + "-fx-font-size: x-large;" + "-fx-background-color: rgb(244,244,244)");
        go_ahead.setStyle("-fx-background-radius: 1000px;" + "-fx-background-insets: 0, 0 0 0 0;" + "-fx-font-size: x-large;" + "-fx-background-color: rgb(244,244,244)");
        go_back.setStyle("-fx-background-radius: 1000px;" + "-fx-background-insets: 0, 0 0 0 0;" + "-fx-font-size: x-large;" + "-fx-background-color: rgb(244,244,244)");
        play_pause.setPrefHeight(70); play_pause.setPrefWidth(70);
        go_ahead.setPrefHeight(70); go_ahead.setPrefWidth(70);
        go_back.setPrefHeight(70); go_back.setPrefWidth(70);
    }

    @Override
    public void start(Stage stage) {
        // 标题
        stage.setTitle("好兄弟播放器");
        stage.getIcons().add(new Image("player.png"));


        vBoxUp.setStyle("-fx-alignment: center;" + "-fx-pref-height:80%;" + "-fx-background-color: black");
        vBoxUp.setPrefHeight(750);
        vBoxDown.setStyle("-fx-alignment: center;" + "-fx-pref-height: 20%");
        hBoxPgsBar.setStyle("-fx-alignment: center;" + "-fx-start-margin: 20");
        hBoxBtn.setStyle("-fx-alignment: center;" + "-fx-margin-top: 20");
        hBoxBtn.setPrefHeight(100);


        // 菜单
        // MenuBar
        MenuBar menuBar = new MenuBar();
        // Menu
        Menu m1 = new Menu("File");
        Menu m2 = new Menu("Help");
        // Menu -> MenuBar
        menuBar.getMenus().addAll(m1,m2);
        // MenuItem
        ImageView msc = new ImageView("music.png");
        msc.setFitHeight(10);
        msc.setFitWidth(10);
        ImageView vdo = new ImageView("video.png");
        vdo.setFitHeight(10);
        vdo.setFitWidth(10);
        MenuItem mi11 = new MenuItem("打开新音频", msc);
        MenuItem mi12 = new MenuItem("打开新视频", vdo);
        MenuItem mi21 = new MenuItem("关于我们");
        // MenuItem -> Menu
        m1.getItems().addAll(mi11,mi12);
        m2.getItems().addAll(mi21);
        // 菜单功能实现
        mi11.setOnAction(x -> {
                FileRead fileRead = new FileRead();
                try {
                    curFile = fileRead.getFile("请选择要播放的音频文件");
                    curPlay = 0;
                    if (player != null)
                        player.pause();
                    playMedia();
                } catch (Exception e) {
                    System.out.println("请选择音频文件");
                }
        });
        mi12.setOnAction(x-> {
                FileRead fileRead = new FileRead();
                try {
                    curFile = fileRead.getFile("请选择要播放的视频文件");
                    curPlay = 1;
                    if (player != null)
                        player.pause();
                    playMedia();
                } catch (Exception e) {
                    System.out.println("请选择视频文件");
                }
        });
        mi21.setOnAction(x -> {
            DialogPane dialog = new DialogPane();
            //设置弹窗样式
            //dialog.setHeaderText("简单计算器");
            dialog.setContentText("开发者：刘晓阳、马茂元");
            dialog.setStyle("-fx-content-display: text-only;" + "-fx-column-halignment: center;" + "-fx-row-valignment: center");
            //弹窗舞台
            Stage dialogStage = new Stage();
            Scene dialogScene = new Scene(dialog);
            dialogStage.setScene(dialogScene);
            dialogStage.setTitle("好兄弟播放器");
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image("player.png"));
            dialogStage.show();
        });

        // 播放器画面
        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (isPlay) {
                    isPlay = false;
                    setPause();
                    player.pause();
                }
                else {
                    isPlay = true;
                    setPlay();
                    player.play();
                }
            }
        });

        // 控件
        // 设置“播放/暂停”控件状态
        setPause();
        play_pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isPlay) {
                    setPause();
                    player.pause();
                    isPlay = false;
                }
                else {
                    setPlay();
                    player.play();
                    isPlay = true;
                }
            }
        });
        // 设置“快进/快退”
        Image image1 = new Image("/go_ahead.png",50, 50, true, true);
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(50);
        imageView1.setFitHeight(50);
        go_ahead.setGraphic(imageView1);
        Image image2 = new Image("/go_back.png",50, 50, true, true);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitWidth(50);
        imageView2.setFitHeight(50);
        go_back.setGraphic(imageView2);
        // 设置监听事件
        go_ahead.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Duration cur_tm = player.getCurrentTime();
                Duration toAdd = new Duration(10000);
                cur_tm = cur_tm.add(toAdd);
                player.seek(cur_tm);
                sliderBar.setValue(cur_tm.toSeconds()/ totalLen * sliderBar.getMax());
                curTime.setText(timeFormatter(cur_tm.toSeconds(), totalLen,1));
                if (!isPlay) {
                    isPlay = true;
                    player.play();
                }
            }
        });
        go_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Duration cur_tm = player.getCurrentTime();
                Duration toSub = new Duration(10000);
                cur_tm = cur_tm.subtract(toSub);
                player.seek(cur_tm);
                sliderBar.setValue(cur_tm.toSeconds()/ totalLen * sliderBar.getMax());
                curTime.setText(timeFormatter(cur_tm.toSeconds(), totalLen,1));
                if (!isPlay) {
                    isPlay = true;
                    player.play();
                }
            }
        });

        // 布局的层次、组件的装入
        rVBox.getChildren().addAll(menuBar,vBoxUp,vBoxDown);
        vBoxDown.getChildren().addAll(hBoxPgsBar,hBoxBtn);


        // 组件装入布局

        vBoxUp.getChildren().add(mediaView);
        VBox.setVgrow(vBoxUp, Priority.ALWAYS);
        VBox.setVgrow(vBoxDown, Priority.ALWAYS);

//        vBoxDown.getChildren().add(sliderBar);
        hBoxPgsBar.getChildren().addAll(curTime,sliderBar,totalTime);
        hBoxBtn.getChildren().addAll(go_back,play_pause,go_ahead);



        // 布局装入场景
        Scene scene = new Scene(rVBox);

        // 装入样式
        scene.getStylesheets().add(this.getClass().getResource("/slider.css").toExternalForm());

        // 展示舞台 并设置全屏
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }

    public void playMedia() {
        // 播放器组件
//        player.pause();
        media = null;
        try {
            media = new Media(curFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        player = new MediaPlayer(media);
        // 在这里，设置一下， if music->icon; else ->video
        if (curPlay == 0) {
            // 设置样式
            vBoxUp.setStyle("-fx-alignment: center;" + "-fx-fit-to-height: true");
            vBoxDown.setStyle("-fx-alignment: center;" + "-fx-fit-to-height: true");
            Image image = new Image("/wyyyy.png");
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            vBoxUp.setBackground(new Background(backgroundImage));
            // 设置播放器样式
            mediaView.setVisible(false);
            mediaView.setMediaPlayer(player);

        } else if (curPlay == 1){
            // 设置样式
            vBoxUp.setStyle("-fx-alignment: center;" + "-fx-background-color: black;" + "-fx-fit-to-height: true");
            vBoxDown.setStyle("-fx-alignment: center;" + "-fx-fit-to-height: true");
            // 设置播放器样式
            mediaView.setFitHeight(740);

            mediaView.setVisible(true);
            mediaView.setMediaPlayer(player);
        }
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                totalLen = media.getDuration().toSeconds();
                // 进度条绑定
                sliderBar.setMin(0);
                sliderBar.setMax(totalLen);
                sliderBar.setValue(20);
                sliderBar.setOnMousePressed(x -> {
                    isPlay = false;
                    System.out.println("正在被点击");
                    System.out.println("总长度：" + sliderBar.getMax());
                });
                sliderBar.setOnMouseReleased(x -> {
                    player.seek(Duration.seconds(sliderBar.getValue()/sliderBar.getMax() * totalLen));
//                    sliderBar.setValue();
                    System.out.println("设置视频进度：" + sliderBar.getValue()/sliderBar.getMax() * totalLen);
                    isPlay = true;
                    player.play();
                });

            }
        });

        // 时长绑定
        ChangeListener<Duration> changeListener = (x, y, z) -> {
            if (isPlay){
                // 设置时长
                double cur = player.getCurrentTime().toSeconds();
                sliderBar.setValue(cur/totalLen * sliderBar.getMax());
                curTime.setText(timeFormatter(cur,totalLen,1));
                totalTime.setText(timeFormatter(0,totalLen,2));
                setPlay();
            }
            else {
                setPause();
            }
        };
        player.currentTimeProperty().addListener(changeListener);

        isPlay = true;
        player.play();
    }

    private String timeFormatter(double cur_time, double total_time, int type) {
        String for_cur = String.format("%02d:%02d:%02d", (int)cur_time/3600, (int)cur_time%3600/60, (int)cur_time%60);
        String for_total = String.format("%02d:%02d:%02d", (int)total_time/3600, (int)total_time%3600/60, (int)total_time%60);
        // type->1 : cur ; type -> 2 : total
        if (type == 1)
            return for_cur;
        if (type == 2)
            return for_total;
        return "时长获取错误";
    }
    private void setPlay() {
        // 设置“播放/暂停”控件状态
        Image image = new Image("/pause.png",50, 50, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        play_pause.setGraphic(imageView);
    }
    private void setPause(){
        // 设置“播放/暂停”控件状态
        Image image = new Image("/play.png",50, 50, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        play_pause.setGraphic(imageView);
    }
}
