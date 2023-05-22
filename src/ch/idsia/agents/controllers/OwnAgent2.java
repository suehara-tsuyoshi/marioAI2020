/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 */

public class OwnAgent2 extends BasicMarioAIAgent implements Agent
{
int trueJumpCounter = 0;
int trueSpeedCounter = 0;

public OwnAgent2()
{
    super("OwnAgent");
    reset();
}

public void reset()
{
    action = new boolean[Environment.numberOfKeys];
    action[Mario.KEY_RIGHT] = true;
}

public boolean isObstacle(int r, int c){
	return getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BRICK
			|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
			|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.FLOWER_POT_OR_CANNON
			|| getReceptiveFieldCellValue(r, c)==GeneralizerLevelScene.LADDER;
}

public boolean isHole(int r,int c) {

	for(int i=r;i<=18;i++) {
		if(getReceptiveFieldCellValue(i,c)==0) continue;
		return false;
	}

	return true;
}

public boolean isAroundEnemy(int r,int c) {

	for(int i=-3;i<=3;i++) {
		for(int j=0;j<=3;j++) {
			int x = getEnemiesCellValue(r+i, c+j);
			if(x != Sprite.KIND_NONE&& x != Sprite.KIND_FIRE_FLOWER) return true;
		}

	}
	return false;
}
public boolean[] getAction()
{
	if(isAroundEnemy(marioEgoRow,marioEgoCol)){

		action[Mario.KEY_SPEED] = true;
		action[Mario.KEY_RIGHT] = false;
		action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;

	}else if(isObstacle(marioEgoRow, marioEgoCol + 1)){

		action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
		action[Mario.KEY_SPEED] = false;
		action[Mario.KEY_RIGHT] = true;

		if(isObstacle(marioEgoRow-3, marioEgoCol + 1)) {
			action[Mario.KEY_JUMP] = isMarioAbleToJump;
		}

	}else if(isMarioOnGround&&isHole(marioEgoRow, marioEgoCol)) {

		action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
		action[Mario.KEY_SPEED] = true;
		action[Mario.KEY_RIGHT] = true;


	}else{

		action[Mario.KEY_JUMP] = false;
		action[Mario.KEY_SPEED] = false;
		action[Mario.KEY_RIGHT] = true;

	}
    return action;

}
}