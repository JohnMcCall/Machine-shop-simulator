/** machine shop simulation */

package applications;

import utilities.MyInputStream;
import exceptions.MyInputException;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";
    
    // data members of MachineShopSimulator
    static int timeNow; // current time
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    static EventList eList; // pointer to event list
    static Machine[] machine; // array of machines
    static int largeTime; // all machines finish before this

    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */
    static Job changeState(int theMachine) {
        Machine currentMachine = machine[theMachine];
        return currentMachine.changeState(theMachine);
    }

    /** input machine shop data */
    static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        numMachines = keyboard.readInteger();
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);

        // create event and machine queues
        eList = new EventList(numMachines, largeTime);
        createMachineQueue(keyboard);

        createJobs(keyboard);
        
    }

	private static void createJobs(MyInputStream keyboard) {
		// input the jobs
        Job theJob;
        for (int i = 0; i < numJobs; i++) {
            System.out.println("Enter number of tasks for job " + (i+1));
            int tasks = keyboard.readInteger(); // number of tasks
            int firstMachine = 0; // machine for first task
            if (tasks < 1)
                throw new MyInputException(EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK);

            // create the job
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            for (int j = 0; j < tasks; j++) {// get tasks for job i
                int theMachine = keyboard.readInteger() - 1;
                int theTaskTime = keyboard.readInteger();
                if (theMachine < 0 || theMachine > numMachines
                        || theTaskTime < 1)
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                if (j == 0)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(theMachine, theTaskTime); // add to
            } // task queue
            machine[firstMachine].addJob(theJob);
        }
	}

	private static void createMachineQueue(MyInputStream keyboard) {
		machine = new Machine[numMachines];
        for (int i = 0; i < numMachines; i++)
            machine[i] = new Machine();

        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 0; j < numMachines; j++) {
            int ct = keyboard.readInteger();
            if (ct < 0)
                throw new MyInputException(CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0);
            machine[j].setChangeTime(ct);
        }
	}

    /** load first jobs onto each machine */
    static void startShop() {
        for (int p = 0; p < numMachines; p++)
            changeState(p);
    }

    /** process all jobs to completion */
    static void simulate() {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !theJob.moveToNextMachine())
                numJobs--;
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 0; p < numMachines; p++) {
            System.out.println("Machine " + (p + 1) + " completed "
                    + machine[p].getNumTasks() + " tasks");
            System.out.println("The total wait time was "
                    + machine[p].getTotalWait());
            System.out.println();
        }
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        largeTime = Integer.MAX_VALUE;
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        timeNow = 0;
        inputData(); // get machine and job data
        startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        outputStatistics(); // output machine wait times
    }
}
