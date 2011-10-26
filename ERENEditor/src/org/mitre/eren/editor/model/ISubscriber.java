package org.mitre.eren.editor.model;

/**
 * Anything pulling from a model should implement this.
 * Subscribers have to manage their own subscriptions.
 */

public interface ISubscriber
{
	// Called when subscribers need to pull from the model.
	void update();
	
	// Called when subscribers need to push to the model.
	void push();
}
