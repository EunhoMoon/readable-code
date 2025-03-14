package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;

public class Minesweeper implements GameInitializable, GameRunable {

    private final GameBoard gameBoard;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public Minesweeper(GameConfig gameConfig) {
        this.gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler = gameConfig.getInputHandler();
        this.outputHandler = gameConfig.getOutputHandler();
    }

    @Override
    public void initialize() {
        this.gameBoard.initializeGame();
    }

    @Override
    public void run() {
        this.outputHandler.showGameStartComments();

        while (this.gameBoard.isInProgress()) {
            try {
                this.outputHandler.showBoard(gameBoard);

                CellPosition cellPosition = getCellInputFromUser();
                UserAction userAction = getUserActionInputFromUser();
                actOnCell(cellPosition, userAction);
            } catch (GameException e) {
                this.outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                this.outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
                e.printStackTrace();
            }

            outputHandler.showBoard(gameBoard);

            if (this.gameBoard.isWinStatus()) {
                outputHandler.showGameWinningComment();
            }
            if (this.gameBoard.isLoseStatus()) {
                outputHandler.showGameLosingComment();
            }
        }
    }

    private void actOnCell(CellPosition cellPosition, UserAction userAction) {
        if (doesUserChooseToPlantFlag(userAction)) {
            this.gameBoard.flagAt(cellPosition);
            return;
        }
        if (doesUserChooseToOpenCell(userAction)) {
            this.gameBoard.openAt(cellPosition);
            return;
        }
        throw new GameException("잘못된 번호를 선택하셨습니다.");
    }

    private boolean doesUserChooseToOpenCell(UserAction userAction) {
        return userAction == UserAction.OPEN;
    }

    private boolean doesUserChooseToPlantFlag(UserAction userAction) {
        return userAction == UserAction.FLAG;
    }

    private UserAction getUserActionInputFromUser() {
        this.outputHandler.showCommentForUserAction();
        return this.inputHandler.getUserActionFromUser();
    }

    private CellPosition getCellInputFromUser() {
        this.outputHandler.showCommentForSelectingCell();
        CellPosition cellPosition = this.inputHandler.getCellPositionFromUser();

        if (this.gameBoard.isInvalidCellPosition(cellPosition)) {
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

}
