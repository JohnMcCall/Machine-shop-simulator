package applications;

import dataStructures.LinkedQueue;

public class Machine {
    // data members
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private int id;
    private Job activeJob; // job currently active on this machine


    // constructor
    public Machine(int id) {
    	this.id = id;
        jobQ = new LinkedQueue();
    }
    
    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */
    public Job changeState() {// Task on theMachine has finished,
                                            // schedule next one.
        Job lastJob;
		if (this.activeJob == null) {// in idle or change-over
                                                    // state
            lastJob = null;
            // wait over, ready for new job
            if (this.jobQ.isEmpty()) // no waiting job
            	setFinishTime(MachineShopSimulator.idleTime);
            else {// take job off the queue and work on it
                this.activeJob = (Job) this.jobQ
                        .remove();
                this.totalWait += MachineShopSimulator.timeNow
                        - this.activeJob.getArrivalTime();
                this.numTasks++;
                int t = this.activeJob.removeNextTask();
                setFinishTime(MachineShopSimulator.timeNow + t);
            }
        } else {// task has just finished on machine[theMachine]
                // schedule change-over time
            lastJob = this.activeJob;
            this.activeJob = null;
            setFinishTime(MachineShopSimulator.timeNow
    		        + this.changeTime);
        }

        return lastJob;
    }

	private void setFinishTime(int time) {
		MachineShopSimulator.geteList().setFinishTime(id, time);
	}

	int getNumTasks() {
		return numTasks;
	}

	int getTotalWait() {
		return totalWait;
	}

	void setChangeTime(int changeTime) {
		this.changeTime = changeTime;
	}
	
	void addJob(Job job) {
		jobQ.put(job);
	}
}