package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;


public class OwnAgent3 extends BasicMarioAIAgent implements Agent {

    public static final int numOfAction = 8;
    public static boolean[] aciton;
    public static boolean flag = false;
    public float dist;

    public OwnAgent3 () {
		super("OwnAgent3");
		reset();
	}

	public void reset(){
	    for(int i = 0; i < Environment.numberOfKeys; ++i){
			action[i] = false;
		}
	}

	public boolean isObstacle(int r, int c){
		return getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BRICK
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.FLOWER_POT_OR_CANNON
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.LADDER;
	}



	public boolean isHole(int r,int c) {

		for(int i=1;i<=7;i++) {
			if(getReceptiveFieldCellValue(r+i,c)==0
					||getReceptiveFieldCellValue(r+i,c)==2) continue;
			return false;
		}

		return true;
	}
	public boolean isHoleS(int r,int c) {

		for(int i=1;i<=4;i++) {
			if(isHole(r,c+i))continue;
			return false;
		}

		return true;
	}
	public boolean isEnemy(int r,int c) {

		for(int i=0;i<=2;i++) {
			for(int j=1;j<=3;j++) {
				int x = getEnemiesCellValue(r-i, c+j);
				if(x != Sprite.KIND_NONE&&x != Sprite.KIND_FIRE_FLOWER) return true;
			}

		}
		return false;
	}

	public void intToAction(int n){
		/*
		if(n == 0 || (n > 4 && n < 11))
			action[Mario.KEY_JUMP] = true;
		if(n == 1 || n == 5 || n == 9 || n == 10)
			action[Mario.KEY_SPEED] = true;
		if(n == 2 || n == 6 || n == 9)
			action[Mario.KEY_RIGHT] = true;
		if(n == 3 || n == 7 || n == 10)
			action[Mario.KEY_LEFT] = true;
		if(n == 4 || n == 8)
			action[Mario.KEY_DOWN] = true;
		*/

		/*
		action[Mario.KEY_JUMP] = (n==2||n==3||n==6||n==7);
		action[Mario.KEY_SPEED] = (n==0||n==1||n==2||n==3);
		action[Mario.KEY_RIGHT] = (n==1||n==3||n==5||n==7);
		action[Mario.KEY_LEFT] = (n==0||n==2||n==4||n==6);
		*/

		action[Mario.KEY_SPEED] = (n==4||n==5||n==6||n==7);
		action[Mario.KEY_JUMP] = (n==1||n==3||n==5||n==7);
		action[Mario.KEY_RIGHT] = (n==2||n==3||n==6||n==7);
	}

	public boolean check(int r,int c) {

		if(getReceptiveFieldCellValue(r+4,c+4)==-24
			&&getReceptiveFieldCellValue(r+1,c)==-60) return true;
		return false;
	}

	public boolean isAbleToPassOn(int r,int c) {

		boolean ok = true;
		for(int i=1;i<=2;i++) {
			if(getReceptiveFieldCellValue(r+1,c+i)==0
					&&getEnemiesCellValue(r+1,c+i)==0) {
				ok = false;
			}
		}
		return ok;
	}

	public boolean[] getAction(){

		dist = distancePassedPhys;

		if(isHoleS(marioEgoRow, marioEgoCol)) {

			action[Mario.KEY_JUMP] = false;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(12);

		}else if(check(marioEgoRow, marioEgoCol)){

			action[Mario.KEY_JUMP] = true;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(marioFloatPos[0]);


		}else if(!isMarioOnGround&&isHole(marioEgoRow, marioEgoCol)) {

			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(1);

		}else if(isMarioOnGround&&isHole(marioEgoRow, marioEgoCol+1)){

			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(2);


		}else if(getEnemiesCellValue(marioEgoRow, marioEgoCol) != Sprite.KIND_NONE
				 ||getEnemiesCellValue(marioEgoRow, marioEgoCol+1) != Sprite.KIND_NONE
				 ||getEnemiesCellValue(marioEgoRow, marioEgoCol+2) != Sprite.KIND_NONE
				 ||getEnemiesCellValue(marioEgoRow, marioEgoCol+3) != Sprite.KIND_NONE){

			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_SPEED] = isMarioAbleToShoot;
			action[Mario.KEY_RIGHT] = false;
			System.out.println(3);

		}else if(isEnemy(marioEgoRow, marioEgoCol)) {

			action[Mario.KEY_JUMP] = isMarioAbleToJump|| !isMarioOnGround;
			action[Mario.KEY_SPEED] = isMarioAbleToShoot;
			action[Mario.KEY_RIGHT] = false;
			System.out.println(4);

		}else if(getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+1)==0
					&&getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+2)==0
					&&getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol+1)!=0
					&&getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol+2)!=0
					&&getEnemiesCellValue(marioEgoRow, marioEgoCol + 2) == Sprite.KIND_NONE
					&&getEnemiesCellValue(marioEgoRow, marioEgoCol + 1) == Sprite.KIND_NONE){

				action[Mario.KEY_JUMP] = false;
				action[Mario.KEY_SPEED] = false;
				action[Mario.KEY_RIGHT] = true;
				System.out.println(5);



		}else if(isMarioOnGround
				&&getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol)!=0
				&&getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol+1)==0
				&&getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol+2)!=0) {

			action[Mario.KEY_JUMP] = false;
			if(dist<=2200) action[Mario.KEY_SPEED] = true;
			else action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(6);

		}else if(dist<=2200
				&&(isObstacle(marioEgoRow, marioEgoCol+1)
				||isObstacle(marioEgoRow-1,marioEgoCol+1)
				||isObstacle(marioEgoRow-2,marioEgoCol+1)
				||isObstacle(marioEgoRow-3,marioEgoCol+1))){

			action[Mario.KEY_JUMP] = isMarioAbleToJump|| !isMarioOnGround;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = false;
			System.out.println(7);

		}else if(dist>2200
				&&isObstacle(marioEgoRow, marioEgoCol+1)){

			action[Mario.KEY_JUMP] = isMarioAbleToJump|| !isMarioOnGround;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = false;
			System.out.println(7);

		}
		else if(isMarioOnGround
				&&(isObstacle(marioEgoRow-2,marioEgoCol+2)
						||isObstacle(marioEgoRow-3,marioEgoCol+3)
						||isObstacle(marioEgoRow-3,marioEgoCol+1))){

			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(8);

		}else if(!isMarioOnGround
				&&getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+1)!=0) {

			action[Mario.KEY_JUMP] = isMarioAbleToJump|| !isMarioOnGround;
			action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(9);

	    }else if(marioFloatPos[1]<=-25){

			action[Mario.KEY_JUMP] = false;
			action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_RIGHT] = false;
			System.out.println(10);

		}else if(isObstacle(marioEgoRow, marioEgoCol+2)
				&&isObstacle(marioEgoRow-4,marioEgoCol+2)) {

			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(14);

		}else{

			action[Mario.KEY_JUMP] = false;
			action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_RIGHT] = true;
			System.out.println(11);

		}
	    return action;
	}

}
