package applications;

import dataStructures.LinkedQueue;

class Job {
    // data members
    private LinkedQueue taskQ; // this job's tasks
    private int length; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int id; // job identifier

    // constructor
    public Job(int theId) {
        id = theId;
        taskQ = new LinkedQueue();
        // length and arrivalTime have default value 0
    }

    // other methods
    public void addTask(int theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).time;
        length += theTime;
        return theTime;
    }

	/**
	 * move theJob to machine for its next task
	 * 
	 * @return false iff no next task
	 */
	public boolean moveToNextMachine() {
	    if (taskQ.isEmpty()) {// no next task
	        System.out.println("Job " + (id + 1) + " has completed at "
	                + MachineShopSimulator.timeNow + " Total wait was " + (MachineShopSimulator.timeNow - length));
	        return false;
	    } else {// theJob has a next task
	            // get machine for next task
	        int p = ((Task) taskQ.getFrontElement()).machine;
	        // put on machine p's wait queue
	        MachineShopSimulator.machine[p].jobQ.put(this);
	        arrivalTime = MachineShopSimulator.timeNow;
	        // if p idle, schedule immediately
	        if (MachineShopSimulator.eList.nextEventTime(p) == MachineShopSimulator.largeTime) {// machine is idle
	            MachineShopSimulator.changeState(p);
	        }
	        return true;
	    }
	}

	public int getArrivalTime() {
		return arrivalTime;
	}
}