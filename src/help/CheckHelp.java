package help;

import cell.Cell;
import javafx.scene.paint.Color;

import java.util.List;

public class CheckHelp {
    Color color;
    List<Cell[][]> list;

    public CheckHelp(Color color, List<Cell[][]> list) {
        this.color = color;
        this.list = list;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Cell[][]> getList() {
        return list;
    }

    public void setList(List<Cell[][]> list) {
        this.list = list;
    }
}
