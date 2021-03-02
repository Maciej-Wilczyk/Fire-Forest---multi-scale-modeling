package help;

import cell.Cell;

import java.util.List;

public class DryingHelp {
    List<Cell[][]> list;
    boolean b;

    public DryingHelp(List<Cell[][]> list, boolean b) {
        this.list = list;
        this.b = b;
    }

    public List<Cell[][]> getList() {
        return list;
    }

    public void setList(List<Cell[][]> list) {
        this.list = list;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }
}
