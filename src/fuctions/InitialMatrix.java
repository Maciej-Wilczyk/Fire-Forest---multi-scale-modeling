package fuctions;

import cell.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class InitialMatrix {

    Cell[][] tab;
    List<Cell[][]> list;
    int pomW;
    int pomH;
    int opacity;
    int wiPosition, hiPosition, basicBurningTime,putOutFire;

    public InitialMatrix() {
        tab = null;
        list = null;
        pomW = -1;
        pomH = -1;
        opacity = 1;
        wiPosition = 0;
        hiPosition = 0;
        basicBurningTime = 500;
        putOutFire = 100;
    }


    public List<Cell[][]> makeMatrixList(Image image) {
        list = new ArrayList();
        PixelReader pixelReader = image.getPixelReader();

        for (int hi = 0; hi < image.getHeight(); hi++) {
            //tab = new Double[3][3];
            for (int wi = 0; wi < image.getWidth(); wi++) {
                tab = new Cell[3][];
                for (int i = 0; i < 3; i++) {
                    tab[i] = new Cell[3];
                    for (int j = 0; j < 3; j++) {
                        tab[i][j] = new Cell();
                    }
                }
                for (int i = 0; i < tab[0].length; i++) {
                    for (int j = 0; j < tab.length; j++) {
                        if (wi + pomW == -1 || hi + pomH == -1 || wi + pomW == image.getWidth() || hi + pomH == image.getHeight()) {
                            tab[i][j].setBrightness(0);//zera na brzegach
                        } else {
                            tab[i][j].setBrightness(pixelReader.getColor(wi + pomW, hi + pomH).getBrightness());
                        }


                        pomW++;
                    }
                    pomH++;
                    pomW = -1;
                }
                pomH = -1;
                tab[1][1].setWi(wi);
                tab[1][1].setHi(hi);
                list.add(tab);
            }
        }
        return list;
    }

    public List<Cell[][]> prepareGoodMatrix(Image image) {
        list = new ArrayList();

        PixelReader pixelReader = image.getPixelReader();

        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {

                tab = new Cell[3][];
                for (int i = 0; i < 3; i++) {
                    tab[i] = new Cell[3];
                    for (int j = 0; j < 3; j++) {
                        tab[i][j] = new Cell();
                    }
                }

                for (int i = 0; i < tab[0].length; i++) {
                    for (int j = 0; j < tab.length; j++) {

                        wiPosition = wi + pomW;
                        hiPosition = hi + pomH;


                        if (wiPosition == -1 || hiPosition == -1 || wiPosition == image.getWidth() || hiPosition == image.getHeight()) {
                            tab[i][j].setColor(Color.GREEN);//zera na brzegach
                        } else {
                            //   System.out.println(pixelReader.getColor(wiPosition, hiPosition));
                            // System.out.println("wi= " + wiPosition + "hi= " + hiPosition);
                            if (pixelReader.getColor(wiPosition, hiPosition).equals(Color.GREEN)) {
                                tab[i][j].setColor(Color.GREEN);
                                //  System.out.println("zrb zielone");
                            } else if (pixelReader.getColor(wiPosition, hiPosition).equals(Color.BLUE)) {
                                tab[i][j].setColor(Color.BLUE);
                                //System.out.println("zrb niebieskie");
//                            } else if (pixelReader.getColor(wiPosition, hiPosition).equals(Color.RED)) {
//                                tab[i][j].setColor(Color.RED);
//                                tab[i][j].setTimeToCollapse(basicBurningTime);
//                                //System.out.println("zrb czerwony");
                            } else if (pixelReader.getColor(wiPosition, hiPosition).equals(Color.WHITE)) {
                                tab[i][j].setColor(Color.WHITE);
                                tab[i][j].setPutOutFire(putOutFire);
                            } else {
                                System.out.println(pixelReader.getColor(wiPosition, hiPosition));
                                System.out.println("nic nie zrb");
                            }
                        }
                        pomW++;
                    }
                    pomW = -1;
                    pomH++;
                }
                pomH = -1;
                tab[1][1].setWi(wi);
                tab[1][1].setHi(hi);

                list.add(tab);
            }
        }
        return list;
    }

    public double getForDilatation(int b, List<Cell[][]> list) {

        for (int j = 0; j < tab[0].length; j++) {
            for (int k = 0; k < tab.length; k++) {
                if (list.get(b)[j][k].getBrightness() == 1) {
                    return 1;
                }
            }
        }
        return 0;
    }


    public Image filter(Image image, List<Cell[][]> list, int ch) {

        int pom = 0;
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        double a = 0;

        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {

                if (ch == 5)
                    a = getForDilatation(pom, list);

                Color color;
                try {
                    color = new Color(a, a, a, opacity);
                } catch (Exception e) {
                    if (a < 0)
                        color = new Color(0, 0, 0, opacity);
                    else
                        color = new Color(1, 1, 1, opacity);
                }
                pixelWriter.setColor(wi, hi, color);
                pom += 1;
            }
        }
        return wImage;
    }
}
