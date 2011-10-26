package org.mitre.eren.data.game 
{
	/**
	 * Object representation of the score message sent by the model.
	 * 
	 * @author Amanda Anganes
	 */
	public class Score 
	{
		
		public var exposed:int;
		public var morbidity:int;
		public var mortality:int;
		public var treated:int;
		
		public function Score(e:int, morb:int, mort:int, t:int) 
		{
			exposed = e;
			morbidity = morb;
			mortality = mort;
			treated = t;
		}
		
	}

}