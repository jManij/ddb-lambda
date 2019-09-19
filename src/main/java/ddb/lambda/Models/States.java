package ddb.lambda.Models;

public class States {
    enum states {
        Available,
        Assigned,
        Accepted,
        Finished,
        NothingToChange
    }

    public static String changeState(String currentStatus) {

        states newStatus = states.NothingToChange;


        switch (currentStatus) {
            case "Available":
                newStatus = States.states.Assigned;
                break;

            case "Assigned":
                newStatus = States.states.Accepted;
                break;

            case "Accepted":
                newStatus = States.states.Finished;
                break;

        }
        return newStatus.toString();

    }



}