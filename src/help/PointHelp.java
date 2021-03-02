package help;

import java.util.ArrayList;
import java.util.List;

public class PointHelp {

    int x, y;

    public PointHelp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

//    public static List<PointHelp> midPointCircleDraw(int x_centre,
//                                                     int y_centre, int r) {
//        List<PointHelp> list = new ArrayList<>();
//        int x = r, y = 0;
//
//        list.add(new PointHelp(x + x_centre,y + y_centre));
//
//        if (r > 0) {
//
//            list.add(new PointHelp(x + x_centre,-y + y_centre));
//            list.add(new PointHelp(y + x_centre,x + y_centre));
//            list.add(new PointHelp(y + x_centre,-x + y_centre));
//            list.add(new PointHelp(-y + x_centre,-x + y_centre));
//            list.add(new PointHelp(-y + x_centre,x + y_centre));
//            list.add(new PointHelp(-x + x_centre,y + y_centre));
//            list.add(new PointHelp(-x + x_centre,-y + y_centre));
//            list.add(new PointHelp(x + x_centre,-y + y_centre));
//            list.add(new PointHelp(-y + x_centre,x + y_centre));
//        }
//
//        int P = 1 - r;
//        while (x > y) {
//
//            y++;
//
//            if (P <= 0)
//                P = P + 2 * y + 1;
//
//            else {
//                x--;
//                P = P + 2 * y - 2 * x + 1;
//            }
//
//            if (x < y)
//                break;
//
//            list.add(new PointHelp(x + x_centre,y + y_centre));
//            list.add(new PointHelp(-x + x_centre,y + y_centre));
//            list.add(new PointHelp(x + x_centre,-y + y_centre));
//            list.add(new PointHelp(-x + x_centre,-y + y_centre));
//
//            if (x != y) {
//                list.add(new PointHelp(y + x_centre,x + y_centre));
//                list.add(new PointHelp(-y + x_centre,x + y_centre));
//                list.add(new PointHelp(y + x_centre,-x + y_centre));
//                list.add(new PointHelp(-y + x_centre,-x + y_centre));
//            }
//        }
//        return list;
//    }

    public static List<PointHelp> midPointCircleDraw1(final int centerX, final int centerY, final int radius) {
        int d = (5 - radius * 4) / 4;
        int x = 0;
        int y = radius;
        List<PointHelp> list = new ArrayList<>();


        do {
            list.add(new PointHelp(centerX + x, centerY + y));
            list.add(new PointHelp(centerX + x, centerY - y));

            list.add(new PointHelp(centerX - x, centerY + y));
            list.add(new PointHelp(centerX - x, centerY - y));

            list.add(new PointHelp(centerX + y, centerY + x));
            list.add(new PointHelp(centerX + y, centerY - x));


            list.add(new PointHelp(centerX - y, centerY + x));
            list.add(new PointHelp(centerX - y, centerY - x));


            if (d < 0) {
                d += 2 * x + 1;
            } else {
                d += 2 * (x - y) + 1;
                y--;
            }
            x++;
        } while (x <= y);
        return list;
    }
}
