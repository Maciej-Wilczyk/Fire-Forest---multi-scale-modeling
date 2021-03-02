package gui;

import cell.Cell;
import fuctions.Functions1;
import fuctions.InitialMatrix;
import help.FireHelp;
import help.PointHelp;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MainWindow implements Initializable {
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Canvas canvas;
    @FXML
    TextField textField;
    @FXML
    TextField textFieldF;
    @FXML
    public Label label;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public ChoiceBox choiceBoxS;
    @FXML
    public RadioButton radio1;
    @FXML
    public RadioButton radio2;
    @FXML
    public Slider slider1;
    @FXML
    public Slider slider2;
    @FXML
    public Slider slider3;


    Functions1 functions1 = new Functions1();
    InitialMatrix initialMatrix = new InitialMatrix();
    GraphicsContext gc;


    private String imagePath = "assets/obrazekDark.bmp";
    private Image image;
    private boolean byteCheck;
    WritableImage wImage;
    PixelWriter pixelWriter;
    PixelReader pixelReader;
    private boolean startStop = true, samolocikFlag = true, wasRain = false;
    private Integer hours = 0, minutes = 0;
    private int wind, season, basicBurningTime = 500, radioFlag = 0, putOutFire = 100, x_center, y_center, r;
    double x = -5, y = -5;
    List<Cell[][]> oldList;
    List<PointHelp> pointHelpList = new ArrayList<>();
    List<PointHelp> pointHelpList1 = new ArrayList<>();


    public void dilatation() {
        // functions1 = new Functions1();
        // initialMatrix = new InitialMatrix();
        gc.clearRect(0, 0, 600, 330);
        if (byteCheck == false) {
            image = functions1.makeByte(image, Double.parseDouble(textField.getText()));
        }
        List<Cell[][]> list = initialMatrix.makeMatrixList(image);
        image = initialMatrix.filter(image, list, 5);
        gc.drawImage(image, 0, 0);
        byteCheck = true;
    }

    public void makeGreenBlueWithDilatationX2() {
        //   functions1 = new Functions1();
        // initialMatrix = new InitialMatrix();
        gc.clearRect(0, 0, 600, 330);
        dilatation();
        dilatation();
        image = functions1.makeGreenBlue(image);
        oldList = initialMatrix.prepareGoodMatrix(image);
        // oldList = initialMatrix.prepareGoodMatrix(image);
        gc.drawImage(image, 0, 0);
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        //label.setText("00:00");
        slider1.setValue(1);
        slider2.setValue(10);
        slider3.setValue(0);
        textField.setText("0.3");
        textField.setVisible(false);
        textFieldF.setText("15");
        image = new Image((imagePath));
        gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
        byteCheck = false;
        choiceBox.setValue("NO WIND");
        choiceBoxS.setValue("SPRING");
        final ToggleGroup group = new ToggleGroup();
        radio1.setToggleGroup(group);
        radio1.setSelected(true);
        radio2.setToggleGroup(group);

//        group.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
//            radioFlag = Integer.parseInt(group.getSelectedToggle().getUserData().toString());
//            System.out.println(radioFlag);
//        });
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (radio1 == group.getSelectedToggle())
                    radioFlag = 0;
                else if (radio2 == group.getSelectedToggle())
                    radioFlag = 1;
                System.out.println(radioFlag);
            }
        });


        canvas.setOnMouseClicked(event -> {
            wImage = new WritableImage(
                    (int) image.getWidth(),
                    (int) image.getHeight());

            pixelWriter = wImage.getPixelWriter();
            pixelReader = image.getPixelReader();
            x = event.getX();
            y = event.getY();


            if (radioFlag == 0) {
                for (int hi = 0; hi < image.getHeight(); hi++) {
                    for (int wi = 0; wi < image.getWidth(); wi++) {
                        pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
                    }
                }

                image = wImage;

                if (pixelReader.getColor((int) x, (int) y).equals(Color.BLUE))
                    System.err.println("WATER!!!");
                else {
                    System.out.println("Put down New Fire");
                    pixelWriter.setColor((int) x, (int) y, Color.RED);
                    oldList.get((600 * (int) y) + (int) x)[1][1].setColor(Color.RED);
                    oldList.get((600 * (int) y) + (int) x)[1][1].setTimeToCollapse(basicBurningTime);
                }

            } else if (radioFlag == 1) {
                x_center = (int) x;
                y_center = (int) y;

                for (int hi = 0; hi < image.getHeight(); hi++) {
                    for (int wi = 0; wi < image.getWidth(); wi++) {
                        pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
                    }
                }


                image = wImage;
                //pixelReader = wImage.getPixelReader();
                if (pixelReader.getColor((int) x, (int) y).equals(Color.BLUE))
                    System.err.println("WATER!!!");
                else {
                    System.out.println("put firefighter team");
                    pixelWriter.setColor((int) x, (int) y, Color.GRAY);
                    oldList = Functions1.fireFighting(oldList, Integer.parseInt(textFieldF.getText()), x_center, y_center);
                }
            }
            gc.drawImage(image, 0, 0);
        });


    }

    int a = 0;

    public void start() {

        startStop = true;

        Thread thread = new Thread(() -> {
            if (a <= 600)
                a++;

            FireHelp fireHelp;
            while (startStop) {

                fireHelp = functions1.fire(image, oldList, wind, slider1.getValue(), season, (int)slider2.getValue());
                image = fireHelp.getImage();
                oldList = fireHelp.getList();
                if((int)slider3.getValue() != 0 ) {
                    oldList = Functions1.rainfall(oldList, (int) slider3.getValue());
                    wasRain = true;
                }
                 if ((int)slider3.getValue() == 0 && wasRain == true){
                    if(Functions1.drying(oldList).isB() == false) {
                        oldList = Functions1.drying(oldList).getList();

                        wasRain = false;
                    } else
                        oldList = Functions1.drying(oldList).getList();

                    }



                gc.drawImage(image, 0, 0);
                minutes += 5;
                if (minutes > 59) {
                    minutes = 0;
                    hours++;
                }
                Platform.runLater(() -> label.setText("h:" + hours.toString() + " min:" + minutes.toString()));

            }
        });

        thread.start();

    }

    public void stop() {
        startStop = false;
    }

    public void random() {
        wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        pixelWriter = wImage.getPixelWriter();
        pixelReader = image.getPixelReader();
        int x, y, random;
        random = ThreadLocalRandom.current().nextInt(5, 20 + 1);

        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {
                pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
            }
        }
        for (int i = 0; i < random; i++) {
            x = ThreadLocalRandom.current().nextInt(0, 599 + 1);
            y = ThreadLocalRandom.current().nextInt(0, 330 + 1);
            if (!pixelReader.getColor(x, y).equals(Color.BLUE) && !pixelReader.getColor(x, y).equals(Color.BLACK) && !pixelReader.getColor(x, y).equals(Color.DARKGREEN)) {
                pixelWriter.setColor(x, y, Color.RED);
                oldList.get((600 * y) + x)[1][1].setColor(Color.RED);
                oldList.get((600 * y) + x)[1][1].setTimeToCollapse(basicBurningTime);
                System.out.println("Put down New Fire");
            }
        }
        image = wImage;
        gc.drawImage(image, 0, 0);
    }

    public void randomFF() {
        wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        pixelWriter = wImage.getPixelWriter();
        pixelReader = image.getPixelReader();
        int x, y, random, random1;
        random = ThreadLocalRandom.current().nextInt(5, 20 + 1);


        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {
                pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
            }
        }
        for (int i = 0; i < random; i++) {
            x = ThreadLocalRandom.current().nextInt(0, 599 + 1);
            y = ThreadLocalRandom.current().nextInt(0, 330 + 1);
            if (!pixelReader.getColor(x, y).equals(Color.BLUE) && !pixelReader.getColor(x, y).equals(Color.BLACK) && !pixelReader.getColor(x, y).equals(Color.DARKGREEN)) {
                random1 = ThreadLocalRandom.current().nextInt(3, 40);
                pixelWriter.setColor(x, y, Color.GRAY);
                System.out.println("put firefighter team");
                oldList = Functions1.fireFighting(oldList, random1, x, y);
            }
        }
        image = wImage;
        gc.drawImage(image, 0, 0);
    }

    public void reset() {
        gc.clearRect(0, 0, 600, 330);
        label.setText("h:00 min:00");
        image = new Image(imagePath);
        wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        pixelWriter = wImage.getPixelWriter();
        gc.drawImage(image, 0, 0);
        byteCheck = false;
        hours = 0;
        minutes = 0;
        oldList = null;
        makeGreenBlueWithDilatationX2();
    }

    public void checkChoiceBoxV() {
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            wind = t1.intValue();
        });
    }

    public void checkChoiceBoxS() {
        choiceBoxS.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            season = t1.intValue();
        });
    }




}
