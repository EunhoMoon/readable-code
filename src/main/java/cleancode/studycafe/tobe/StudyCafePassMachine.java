package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;

import static cleancode.studycafe.tobe.model.StudyCafePassType.*;

public class StudyCafePassMachine {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();

            StudyCafePassType studyCafePassType = getStudyCafePassType();

            orderPass(studyCafePassType);
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private void orderPass(StudyCafePassType studyCafePassType) {
        StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
        List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePasses();

        if (isHoliday(studyCafePassType)) {
            List<StudyCafePass> hourlyPasses = getStudyCafePassesWhenHoliday(studyCafePasses);
            outputHandler.showPassListForSelection(hourlyPasses);
            StudyCafePass selectedPass = inputHandler.getSelectPass(hourlyPasses);
            outputHandler.showPassOrderSummary(selectedPass, null);
        }

        if (isWeekly(studyCafePassType)) {
            List<StudyCafePass> weeklyPasses = getStudyCafePassesWhenWeekly(studyCafePasses);
            outputHandler.showPassListForSelection(weeklyPasses);
            StudyCafePass selectedPass = inputHandler.getSelectPass(weeklyPasses);
            outputHandler.showPassOrderSummary(selectedPass, null);
        }

        if (isFixed(studyCafePassType)) {
            List<StudyCafePass> fixedPasses = getStudyCafePassesWhenFixed(studyCafePasses);
            outputHandler.showPassListForSelection(fixedPasses);
            StudyCafePass selectedPass = inputHandler.getSelectPass(fixedPasses);

            StudyCafeLockerPass lockerPass = getStudyCafeLockerPass(studyCafeFileHandler, selectedPass);

            if (isLockerSelection(lockerPass)) {
                outputHandler.showPassOrderSummary(selectedPass, lockerPass);
                return;
            }

            outputHandler.showPassOrderSummary(selectedPass, null);
        }
    }

    private boolean isLockerSelection(StudyCafeLockerPass lockerPass) {
        boolean lockerSelection = false;
        if (lockerPass != null) {
            outputHandler.askLockerPass(lockerPass);
            lockerSelection = inputHandler.getLockerSelection();
        }
        return lockerSelection;
    }

    private static StudyCafeLockerPass getStudyCafeLockerPass(StudyCafeFileHandler studyCafeFileHandler, StudyCafePass selectedPass) {
        List<StudyCafeLockerPass> lockerPasses = studyCafeFileHandler.readLockerPasses();
        return lockerPasses.stream()
            .filter(option ->
                option.getPassType() == selectedPass.getPassType()
                    && option.getDuration() == selectedPass.getDuration()
            )
            .findFirst()
            .orElse(null);
    }

    private List<StudyCafePass> getStudyCafePassesWhenFixed(List<StudyCafePass> studyCafePasses) {
        return studyCafePasses.stream()
            .filter(studyCafePass -> isFixed(studyCafePass.getPassType()))
            .toList();
    }

    private List<StudyCafePass> getStudyCafePassesWhenWeekly(List<StudyCafePass> studyCafePasses) {
        return studyCafePasses.stream()
            .filter(studyCafePass -> isWeekly(studyCafePass.getPassType()))
            .toList();
    }

    private List<StudyCafePass> getStudyCafePassesWhenHoliday(List<StudyCafePass> studyCafePasses) {
        return studyCafePasses.stream()
            .filter(studyCafePass -> isHoliday(studyCafePass.getPassType()))
            .toList();
    }

    private boolean isFixed(StudyCafePassType studyCafePassType) {
        return studyCafePassType == FIXED;
    }

    private boolean isWeekly(StudyCafePassType studyCafePassType) {
        return studyCafePassType == WEEKLY;
    }

    private boolean isHoliday(StudyCafePassType studyCafePassType) {
        return studyCafePassType == HOURLY;
    }

    private StudyCafePassType getStudyCafePassType() {
        outputHandler.askPassTypeSelection();
        return inputHandler.getPassTypeSelectingUserAction();
    }

}
