/**
 * Interfaces between controllers and the model.
 * All changes to the model triggers updateAll().
 * Subscribers must handle subscribing/unsubscribing themselves.
 */

package org.mitre.eren.editor.model;

import java.util.Vector;

public class Publisher
{
	private Vector<ISubscriber> subscribers = new Vector<ISubscriber>();

	public void attach( ISubscriber sub )
	{
		if(subscribers.contains(sub) == false)
		{
			subscribers.addElement( sub );
		}
	}

	public void detach(ISubscriber sub)
	{
		subscribers.removeElement(sub);
	}

	public void updateAll()
	{
		for(int x = 0; x < subscribers.size(); ++x)
		{
			ISubscriber sub = (ISubscriber)subscribers.elementAt(x);
			sub.update();
		}
	}
	
	public void pushAll()
	{
		for(int x = 0; x < subscribers.size(); ++x)
		{
			ISubscriber sub = (ISubscriber)subscribers.elementAt(x);
			sub.push();
		}
	}
	
	public void syncAll()
	{
		pushAll();
		updateAll();
	}
}
