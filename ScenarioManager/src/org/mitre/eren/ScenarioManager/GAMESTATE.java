package org.mitre.eren.ScenarioManager;


//WAITING_ON_PLAYERS means we do not have enough players for a game
//CRITICAL_MASS means we have enough players for a game
//STARTING means the game is transitioning to a starting state
//RUNNING means the game is running
public enum GAMESTATE {
	WAITING_ON_PLAYERS, //don't have enough players to start the game 
	CRITICAL_MASS,  //all roles have enough people but not all clients are ready
	READY_TO_START, //all clients are ready
	//STARTING, //starting the game 
	RUNNING, //Game is in running stage
	STOP //who knows what this means
}
