package applications;

class EventList {
    // data members
    int[] finishTime; // finish time array

    // constructor
    EventList(int theNumMachines, int theLargeTime) {// initialize
                                                             // finish
                                                             // times for
                                                             // m
                                                             // machines
        if (theNumMachines < 1)
            throw new IllegalArgumentException(MachineShopSimulator.NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1);
        finishTime = new int[theNumMachines];

        // all machines are idle, initialize with
        // large finish time
        for (int i = 0; i < theNumMachines; i++)
            finishTime[i] = theLargeTime;
    }

    /** @return machine for next event */
    int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int p = 0;
        int t = finishTime[0];
        for (int i = 1; i < finishTime.length; i++)
            if (finishTime[i] < t) {// i finishes earlier
                p = i;
                t = finishTime[i];
            }
        return p;
    }

    int nextEventTime(int theMachine) {
        return finishTime[theMachine];
    }

    void setFinishTime(int theMachine, int theTime) {
        finishTime[theMachine] = theTime;
    }
}