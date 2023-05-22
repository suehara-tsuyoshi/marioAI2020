package ch.idsia.agents;

public final class OwnKeyOfMC {

    private final int state;
    private final int cliff;
    private final int ableToJump;
    //private final int bigcliff;
    private final int action;

    public OwnKeyOfMC(int state, int cliff, int ableToJump,int action) {
        this.state = state;
        this.cliff = cliff;
        this.ableToJump = ableToJump;
        this.action = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OwnKeyOfMC) {
        	OwnKeyOfMC key = (OwnKeyOfMC) obj;
            return this.state == key.state && this.cliff == key.cliff
            		&&this.ableToJump == key.ableToJump && this.action == action;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return state + cliff + ableToJump + action;
    }
    public int getState(){
    	return state;
    }
    public int getCliff(){
    	return cliff;
    }
   /* public int getBigCliff() {
    	return bigcliff;
    }
    */
    public int getAbleToJump(){
    	return ableToJump;
    }
    public int getAction(){
    	return action;
    }
}
