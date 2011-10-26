package org.mitre.eren.data.scenario 
{
	/**
	 * Person class to represent Non-Player-Characters (NPCs).
	 * 
	 * @author a_anganes
	 */
	public class Person
	{
		
		public var ID:String;
		public var firstName:String;
		public var lastName:String;
		public var orgName:String;
		public var npcRole:String;
		public var imageURL:String;
		
		public function Person(i:String, fn:String, ln:String, on:String, nrole:String, imgurl:String) {
			
			this.ID = i;
			this.firstName = fn;
			this.lastName = ln;
			this.orgName = on;
			this.npcRole = nrole;
			this.imageURL = imgurl;
			
		}
		
	}

}