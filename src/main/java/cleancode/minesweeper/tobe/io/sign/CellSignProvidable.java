package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;

public interface CellSignProvidable {

    boolean supports(CellSnapshot snapshot);

    String provide(CellSnapshot cellSnapshot);

}
