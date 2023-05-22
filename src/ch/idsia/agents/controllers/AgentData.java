package ch.idsia.agents.controllers;

import java.util.ArrayList;

public class AgentData {

	public static float score = 0.0f;
	public static ArrayList<Integer> actions;

	public AgentData() {

	}

	public AgentData(float _score, ArrayList<Integer> _actions) {
		score = _score;
		actions = _actions;
	}

}
