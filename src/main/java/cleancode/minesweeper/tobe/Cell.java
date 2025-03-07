package cleancode.minesweeper.tobe;

public class Cell {

    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMineCount;
    private boolean isLandMine;
    private boolean isFlagged;
    private boolean isOpened;

    private Cell(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    public static Cell of(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        return new Cell(nearbyLandMineCount, isLandMine, isFlagged, isOpened);
    }

    public static Cell create() {
        return of(0, false, false, false);
    }

    public void turnOnLandMine() {
        this.isLandMine = true;
    }

    public void updateNearbyLandMines(int count) {
        this.nearbyLandMineCount = count;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public boolean isChecked() {
        return this.isFlagged || this.isOpened;
    }

    public boolean isLandMine() {
        return this.isLandMine;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public boolean hasLandMineCount() {
        return this.nearbyLandMineCount != 0;
    }

    public String getSign() {
        if (this.isOpened) {
            if (this.isLandMine) {
                return LAND_MINE_SIGN;
            }
            if (this.hasLandMineCount()) {
                return String.valueOf(this.nearbyLandMineCount);
            }
            return EMPTY_SIGN;
        }

        if (this.isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }

}
