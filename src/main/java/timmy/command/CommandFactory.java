package timmy.command;

import   timmy.command.impl.*;
import timmy.exceptions.NotRealizedMethodException;

public class CommandFactory {

    public static Command getCommand(long id) {
        Command result = getCommandWithoutReflection((int) id);
        if (result == null) throw new NotRealizedMethodException("Not realized for type: " + id);
        return result;
    }

    private static Command getCommandWithoutReflection(int id) {
        switch (id) {
            case 1:
                return new id001_ShowInfo();
            case 2:
                return new id002_SelectionLanguage();
            case 3:
                return new id003_Files();
            case 5:
                return new id005_Question();
            case 6:
                return new id006_Application();
            case 7:
                return new id007_Suggestion();
            case 8:
                return new id008_Complaint();
            case 10:
                return new id010_PrivateOffice();
            case 11:
                return new id011_Investments();
            case 12:
                return new id012_Yes();
            case 13:
                return new id013_EditMenu();
            case 21:
                return new id021_Registration();
            case 29:
                return new id029_Reminder();
            case 30:
                return new id030_UserReport();
            case 31:
                return new id031_InvestmentsReport();
            case 777:
                return new id777_ShowAdminInfo();
            case 555:
                //return  new id555_Quest();
                return new id556_Quest();
            case 333:
                return new id333_Admin();
            case 222:
                return new id222_UpdateQuest();
            case 888:
                return new id888_Zae();
            case 999:
                return new id999_Zaem();
            case 1000:
                return new id1000_Zaem();
            case 1111:
                return new id1111_Zaem();
            case 2222:
                return new id2222_Zaem();
            case 2323:
                return new id2323_CreditDo55();
            case 23231:
                return new id2323_CreditDo55KAZ();
            case 2324:
                return new id2324_CreditDo55();
            case 23241:
                return new id2324_CreditDo55KAZ();
            case 9999:
                return new id9999_ShowInfo1();
        }


        return null;
    }
}

