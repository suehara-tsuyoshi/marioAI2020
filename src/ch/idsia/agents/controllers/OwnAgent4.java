package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;

public class OwnAgent4 extends BasicMarioAIAgent implements Agent {

	public static final int numOfAction = 8;
    public static boolean[] aciton;
    public float dist;

    public OwnAgent4 () {
		super("OwnAgent4");
		reset();
	}

	public void reset(){
	    for(int i = 0; i < Environment.numberOfKeys; ++i){
			action[i] = false;
		}
	}

	//障害物の有無
	public boolean isObstacle(int r, int c){
		return getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BRICK
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.FLOWER_POT_OR_CANNON
				|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.LADDER;
	}

	//１つ前のマスの列の障害物の有無
	public boolean isFrontObstacle(int r,int c) {
		return isObstacle(r, c+1)
				||isObstacle(r-1,c+1)
				||isObstacle(r-2,c+1)
				||isObstacle(r-3,c+1);
	}

	//飛び越えなければいけない穴の有無
	public boolean isHole(int r,int c) {

		for(int i=1;i<=7;i++) {
			if(getReceptiveFieldCellValue(r+i,c)==0
					||getReceptiveFieldCellValue(r+i,c)==2) continue;
			return false;
		}

		return true;
	}

	//高い位置のブロックの有無
	public boolean isHighBlock(int r,int c) {
		return isObstacle(r-3,c+1)
				||isObstacle(r-2,c+2);
	}

	//マリオ周辺の敵の有無
	public boolean isAroundEnemy(int r,int c) {

		for(int i=0;i<=2;i++) {
			for(int j=0;j<=3;j++) {
				int x = getEnemiesCellValue(r-i, c+j);
				if(x != Sprite.KIND_NONE&&x != Sprite.KIND_FIRE_FLOWER) return true;
			}

		}
		return false;
	}

	//水平方向にそのまま通過できるかどうか
	public boolean isAbleToPassOn(int r,int c) {

		return getReceptiveFieldCellValue(r,c+1)==0
				&&getReceptiveFieldCellValue(r,c+2)==0
				&&getReceptiveFieldCellValue(r+1,c+1)!=0
				&&getReceptiveFieldCellValue(r+1,c+2)!=0
				&&getEnemiesCellValue(r, c+2) == Sprite.KIND_NONE
				&&getEnemiesCellValue(r, c+1) == Sprite.KIND_NONE;

	}

	//ダッシュで通過できる１マス空きのブロック
	public boolean isAbleToPassByDash(int r,int c) {

		return isMarioOnGround
				&&getReceptiveFieldCellValue(r+1,c)!=0
				&&getReceptiveFieldCellValue(r+1,c+1)==0
				&&getReceptiveFieldCellValue(r+1,c+2)!=0;

	}


	//行動へ変換

	public void intToAction(int n) {
		switch(n) {

		case 0: //画面外にいるとき
			action[Mario.KEY_RIGHT] = false;
			action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_JUMP] = false;
			break;

		case 1: //前方に何もないとき
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_SPEED] = false;
			action[Mario.KEY_JUMP] = false;
			break;

		case 2: //周辺に敵がいるとき
			action[Mario.KEY_RIGHT] = false;
			action[Mario.KEY_SPEED] = isMarioAbleToShoot;
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			break;

		case 3: //ダッシュで通過できる穴の上を通るとき
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_JUMP] = false;
			break;

		case 4: //周辺に落とし穴もしくは高い位置にブロックがあるとき
			action[Mario.KEY_RIGHT] = true;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_JUMP] =  isMarioAbleToJump || !isMarioOnGround;
			break;

		case 5: //飛び越えるブロックがあるとき
			action[Mario.KEY_RIGHT] = false;
			action[Mario.KEY_SPEED] = true;
			action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
			break;
		default:
		}

	}

	public boolean[] getAction() {
		//行動のパターンの変数
		int idx = 1;
		//距離の更新
		dist = distancePassedPhys;
		// マリオの位置
		int r = marioEgoRow;
		int c = marioEgoCol;


		if(!isMarioOnGround&&isHole(r,c)) idx = 4;
		else if(isMarioOnGround&&isHole(r,c+1)) idx = 4;
		else if(isAroundEnemy(r,c)) idx = 2;
		else if(isAbleToPassOn(r,c)) idx = 1;
		else if(isAbleToPassByDash(r,c)) idx = 3;
		else if(dist<=2200&&isFrontObstacle(r,c)) idx = 5;
		else if(dist>2200&&isObstacle(r,c+1)) idx = 5;
		else if(isMarioOnGround&&isHighBlock(r,c)) idx = 4;
		else if(marioFloatPos[1]<=-25) idx= 0;
		else idx = 1;
		intToAction(idx);
		return action;
	}

}
