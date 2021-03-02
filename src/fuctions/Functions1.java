package fuctions;

import cell.Cell;
import help.CheckHelp;
import help.DryingHelp;
import help.FireHelp;
import help.PointHelp;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.jar.JarOutputStream;

public class Functions1 {


    public Image makeByte(Image image, double threshold) {
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {
                Color color = pixelReader.getColor(wi, hi);
                if (color.getBrightness() > threshold) {
                    color = color.BLACK;
                } else {
                    color = color.WHITE;
                }
                pixelWriter.setColor(wi, hi, color);
            }
        }
        return wImage;
    }

    public Image makeGreenBlue(Image image) {
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {
                Color color = pixelReader.getColor(wi, hi);
                if (color.equals(Color.WHITE)) {
                    color = Color.BLUE;
                } else {
                    color = Color.GREEN;
                }
                pixelWriter.setColor(wi, hi, color);
            }
        }
        return wImage;
    }

    private final int basicBurningTime = 500;
    private static final int putOutFire = 150;


    public Cell[][] checkNeighbors(List<Cell[][]> list, int b, int wind, double sW, int season, int humidity) {


        double strongWind = sW;
        if (wind == 0)
            strongWind = 1;

        double step, propC = 1, propR = 1; // combustion random

        switch (season) {
            case 0:
                propR = 1.2;
                propC = 0.8;
                break;
            case 1:
                propR = 1.8;
                propC = 1.2;
                break;
            case 2:
                propR = 0.7;
                propC = 0.7;
                break;
            case 3:
                propR = 0.05;
                propC = 0.5;
                break;
        }

        int counter = 0, randomNum;
        Cell[][] mainCell;

        mainCell = new Cell[3][];

        for (int i = 0; i < 3; i++) {
            mainCell[i] = new Cell[3];
            for (int j = 0; j < 3; j++) {
                mainCell[i][j] = new Cell();
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (list.get(b)[i][j].getColor().equals(Color.GREEN)) {
                    mainCell[i][j].setPutOutFire(list.get(b)[i][j].getPutOutFire());
                    mainCell[i][j].setColor(Color.GREEN);
                    mainCell[i][j].setRainTime(list.get(b)[i][j].getRainTime());
                } else if (list.get(b)[i][j].getColor().equals(Color.RED)) {
                    if (i == 1 && j == 1) {
                        mainCell[i][j].setColor(Color.RED);
                        mainCell[i][j].setTimeToCollapse(list.get(b)[i][j].getTimeToCollapse());
                        mainCell[i][j].setPutOutFire(list.get(b)[i][j].getPutOutFire());
                        mainCell[i][j].setRainTime(list.get(b)[i][j].getRainTime());
                        continue;
                    }
                    mainCell[i][j].setColor(Color.RED);
                    mainCell[i][j].setTimeToCollapse(list.get(b)[i][j].getTimeToCollapse());
                    mainCell[i][j].setPutOutFire(list.get(b)[i][j].getPutOutFire());
                    mainCell[i][j].setRainTime(list.get(b)[i][j].getRainTime());


                    switch (wind) {
                        case 0:
                            counter++;
                            break;
                        case 1:
                            if (i == 0)
                                counter++;
                            break;
                        case 2:
                            if (i == 2)
                                counter++;
                            break;
                        case 3:
                            if (j == 2)
                                counter++;
                            break;
                        case 4:
                            if (j == 0)
                                counter++;
                            break;
                        case 5:
                            if ((i == 0 && j == 2) || (i == 0 && j == 1) || (i == 1 && j == 2))
                                counter++;
                            break;
                        case 6:
                            if (i == 0 && j == 0 || (i == 0 && j == 1) || (i == 1 && j == 0))
                                counter++;
                            break;
                        case 7:
                            if ((i == 2 && j == 2) || (i == 2 && j == 1) || (i == 1 && j == 2))
                                counter++;
                            break;
                        case 8:
                            if ((i == 2 && j == 0) || (i == 2 && j == 1) || (i == 1 && j == 0))
                                counter++;
                            break;
                    }

                } else if (list.get(b)[i][j].getColor().equals(Color.BLACK)) {
                    mainCell[i][j].setColor(Color.BLACK);

                } else if (list.get(b)[i][j].getColor().equals(Color.BLUE)) {
                    mainCell[i][j].setColor(Color.BLUE);
                } else if (list.get(b)[i][j].getColor().equals(Color.GRAY)) {
                    mainCell[i][j].setColor(Color.GRAY);
                } else if (list.get(b)[i][j].getColor().equals(Color.DARKGREEN)) {
                    mainCell[i][j].setColor(Color.DARKGREEN);
                    mainCell[i][j].setPutOutFire((list.get(b)[i][j].getPutOutFire()));
                }
            }
        }


        if (mainCell[1][1].getColor().equals(Color.RED)) {//spalanie drzewa

            if (mainCell[1][1].getTimeToCollapse() <= 0) {
                mainCell[1][1].setColor(Color.BLACK);
            } else if (counter == 0) {
                step = 100 / (8.0 / propC);
                mainCell[1][1].decTime(step);

            } else if (counter == 1 || counter == 2) {
                step = 100 / (7.0 / propC);
                mainCell[1][1].decTime(step);
            } else if (counter == 3) {
                step = 100 / (6.0 / propC);
                mainCell[1][1].decTime(step);
            } else if (counter == 4) {
                step = 100 / (5.0 / propC);
                mainCell[1][1].decTime(step);
            } else if (counter == 5 || counter == 6) {
                step = 100 / (4.0 / propC);
                mainCell[1][1].decTime(step);
            } else if (counter == 7 || counter == 8) {
                step = 100 / (3.0 / propC);
                mainCell[1][1].decTime(step);
            }

            if (mainCell[1][1].getPutOutFire() != -100) {
                randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
                if (randomNum == 0)
                    mainCell[1][1].decPutOutFire(20);
                if (mainCell[1][1].getPutOutFire() <= 0) {
                    mainCell[1][1].setColor(Color.DARKGREEN);
                }
            }
        }


        if (mainCell[1][1].getColor().equals(Color.GREEN)) { //podpalanie
            randomNum = ThreadLocalRandom.current().nextInt(0, 1000 + 1);

            //TODO ZA DUZE PRZY MAKSACH

            if (counter == 1 || counter == 2) {
                if (randomNum < 7 * strongWind * propR * humidity) {
                    //   System.out.println("podpalam");
                    mainCell[1][1].setColor(Color.RED);
                    mainCell[1][1].setTimeToCollapse(basicBurningTime);
                }
            }
            if (counter == 3) {
                if (randomNum < 15 * strongWind * propR * humidity) {
                    mainCell[1][1].setColor(Color.RED);
                    mainCell[1][1].setTimeToCollapse(basicBurningTime);
                }
            }
            if (counter == 4) {
                if (randomNum < 21 * strongWind * propR * humidity) {
                    mainCell[1][1].setColor(Color.RED);
                    mainCell[1][1].setTimeToCollapse(basicBurningTime);
                }
            }
            if (counter == 5 || counter == 6) {
                if (randomNum < 30 * strongWind * propR * humidity) {
                    mainCell[1][1].setColor(Color.RED);
                    mainCell[1][1].setTimeToCollapse(basicBurningTime);
                }
            }
            if (counter == 7 || counter == 8) {
                if (randomNum < 40 * strongWind * propR * humidity) {
                    mainCell[1][1].setColor(Color.RED);
                    mainCell[1][1].setTimeToCollapse(basicBurningTime);
                }
            }
            if (mainCell[1][1].getPutOutFire() != -100) {
                randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
                if (randomNum == 0)
                    mainCell[1][1].decPutOutFire(20);
                if (mainCell[1][1].getPutOutFire() <= 0) {
                    mainCell[1][1].setColor(Color.DARKGREEN);
                }
            }

        }
        return mainCell;
    }

    public List<List<Cell[][]>> mainCellList(List<Cell[][]> list, int wind, double sW, int season, int humidity) {
        List<List<Cell[][]>> newList = new ArrayList<>();
        List<Cell[][]> nl = null;
        int help = 0;
        for (int i = 0; i < 330; i++) {
            nl = new ArrayList<>();
            for (int j = 0; j < 600; j++) {
                nl.add(checkNeighbors(list, help, wind, sW, season, humidity));
                help++;
            }
            newList.add(nl);
        }
        return newList;
    }

    public FireHelp fire(Image image, List<Cell[][]> list, int wind, double sW, int season, int humidity) {
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();


        List<List<Cell[][]>> oldList = mainCellList(list, wind, sW, season, humidity);
        List<Cell[][]> newList = new ArrayList<>();
        Cell[][] tab;
        int pomW = -1, pomH = -1, wiPosition = 0, hiPosition = 0, help = 0, sh = 0;
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
                            try {
                                if (oldList.get(hiPosition).get(wiPosition)[1][1].getColor().equals(Color.GREEN)) {
                                    tab[i][j].setColor(Color.GREEN);
                                    tab[i][j].setPutOutFire(oldList.get(hiPosition).get(wiPosition)[1][1].getPutOutFire());
                                    tab[i][j].setRainTime(oldList.get(hiPosition).get(wiPosition)[1][1].getRainTime());
                                    //  System.out.println("zrb zielone");
                                } else if (oldList.get(hiPosition).get(wiPosition)[1][1].getColor().equals(Color.BLUE)) {
                                    tab[i][j].setColor(Color.BLUE);
                                    //System.out.println("zrb niebieskie");
                                } else if (oldList.get(hiPosition).get(wiPosition)[1][1].getColor().equals(Color.RED)) {
                                    sh++;
                                    tab[i][j].setColor(Color.RED);
                                    tab[i][j].setTimeToCollapse(oldList.get(hiPosition).get(wiPosition)[i][j].getTimeToCollapse());
                                    tab[i][j].setPutOutFire(oldList.get(hiPosition).get(wiPosition)[1][1].getPutOutFire());
                                    tab[i][j].setRainTime(oldList.get(hiPosition).get(wiPosition)[1][1].getRainTime());
                                    //  if (i == 1 && j == 1) System.out.println("zrb czerwony"+ i + j);
                                } else if (oldList.get(hiPosition).get(wiPosition)[1][1].getColor().equals(Color.BLACK)) {
                                    tab[i][j].setColor(Color.BLACK);
                                    tab[i][j].setTimeToCollapse(oldList.get(hiPosition).get(wiPosition)[1][1].getTimeToCollapse());
                                    tab[i][j].setRainTime(oldList.get(hiPosition).get(wiPosition)[1][1].getRainTime());
                                    //System.out.println("zrb zielony");
                                } else if (oldList.get(hiPosition).get(wiPosition)[1][1].getColor().equals(Color.GRAY)) {
                                    tab[i][j].setColor(Color.GRAY);
                                } else if (oldList.get(hiPosition).get(wiPosition)[1][1].getColor().equals(Color.DARKGREEN)) {
                                    tab[i][j].setColor(Color.DARKGREEN);
                                    tab[i][j].setPutOutFire(oldList.get(hiPosition).get(wiPosition)[1][1].getPutOutFire());
                                    // tab[i][j].setRainTime(oldList.get(hiPosition).get(wiPosition)[1][1].getRainTime());
                                } else {
                                    System.out.println("nic nie zrb");
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();

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
                pixelWriter.setColor(wi, hi, tab[1][1].getColor());

                help += 1;
//                if(a.equals(tab))
//                    System.out.println(true);
                //  System.out.println(help);
                newList.add(tab);
            }
        }
        //System.out.println("fire");
        // System.out.println();
        return new FireHelp(wImage, newList);
    }

    public static List<Cell[][]> fireFighting(List<Cell[][]> oldList, int power, int x_center, int y_center) {
        List<PointHelp> pointHelpList1;
        for (int k = -1; k <= 1; k++) {
            for (int j = 1; j <= power; j++) {
                pointHelpList1 = PointHelp.midPointCircleDraw1(x_center + k, y_center, j);
                for (PointHelp i : pointHelpList1) {
                    try {
                        if ((i.getY() + 1) * i.getX() > 600 * (i.getY() + 1))
                            continue;

                        else if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLUE))
                            continue;

                        else if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLACK))
                            continue;

                        else if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.RED)) {
                            oldList.get((600 * i.getY()) + i.getX())[1][1].setPutOutFire(putOutFire);
                            continue;
                        } else {
                            oldList.get((600 * i.getY()) + i.getX())[1][1].setPutOutFire(putOutFire);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }

        for (int k = -1; k <= 1; k++) {
            if (k == 0)
                continue;
            for (int j = 1; j <= power; j++) {
                pointHelpList1 = PointHelp.midPointCircleDraw1(x_center, y_center + k, j);
                for (PointHelp i : pointHelpList1) {
                    try {
                        if ((i.getY() + 1) * i.getX() > 600 * (i.getY() + 1))
                            continue;

                        if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLUE))
                            continue;

                        if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLACK))
                            continue;

                        if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.RED)) {
                            oldList.get((600 * i.getY()) + i.getX())[1][1].setPutOutFire(putOutFire);
                            continue;
                        }

                        //oldList.get((600 * i.getY()) + i.getX())[1][1].setColor(Color.DARKGREEN);
                        oldList.get((600 * i.getY()) + i.getX())[1][1].setPutOutFire(putOutFire);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        oldList.get((600 * y_center) + x_center)[1][1].setColor(Color.GRAY);
        return oldList;
    }

    public static List<Cell[][]> rainfall(List<Cell[][]> oldList, int power) {
        int randomNum;
        for (Cell[][] i : oldList) {
            randomNum = ThreadLocalRandom.current().nextInt(0, 13 - power + 1);
            if (!i[1][1].getColor().equals(Color.BLUE) && !i[1][1].getColor().equals(Color.BLACK) && !i[1][1].getColor().equals(Color.DARKGREEN) && !i[1][1].getColor().equals(Color.GRAY)) {
                if (i[1][1].getRainTime() <= 0)
                    i[1][1].setColor(Color.DARKGREEN);

                else if (randomNum == 0)
                    i[1][1].decRainTime(10 + power);

            }
        }
        return oldList;
    }

    public static DryingHelp drying(List<Cell[][]> oldList) {
        int randomNum, counter = 0, flag = 0;
        for (Cell[][] i : oldList) {
            randomNum = ThreadLocalRandom.current().nextInt(0, 5 + 1);
            if (i[1][1].getColor().equals(Color.DARKGREEN) && i[1][1].getPutOutFire() == -100) {
                flag++;
                if (i[1][1].getRainTime() == 100 && randomNum == 0) {
                    i[1][1].setColor(Color.GREEN);
                } else if (i[1][1].getRainTime() > 100 && randomNum == 0) {
                    i[1][1].setColor(Color.GREEN);
                    i[1][1].setRainTime(100);
                } else if (randomNum == 0) {
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            if (k == 1 && j == 1)
                                continue;
                            if (i[j][k].getColor().equals(Color.RED)) counter++;
                        }
                    }
                    switch (counter) {
                        case 0:
                            i[1][1].addRainTime(10);
                            break;
                        case 1:
                            i[1][1].addRainTime(12);
                            break;
                        case 2:
                            i[1][1].addRainTime(14);
                            break;
                        case 3:
                            i[1][1].addRainTime(16);
                            break;
                        case 4:
                            i[1][1].addRainTime(18);
                            break;
                        case 5:
                            i[1][1].addRainTime(20);
                            break;
                        case 6:
                            i[1][1].addRainTime(22);
                            break;
                        case 7:
                            i[1][1].addRainTime(24);
                            break;
                        case 8:
                            i[1][1].addRainTime(26);
                            break;
                    }
                }

            }
        }
        if (flag > 0)
            return new DryingHelp(oldList,true);
        else
            return new DryingHelp(oldList,false);
    }

}

