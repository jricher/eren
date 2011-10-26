package org.mitre.eren.data.actions 
{
	/**
	 * Input object, representing an "act:input" node from the Actions File.
	 * An input always has a type, name, fromId (id of the npc the question text
	 * should appear to come from), and questionText. Min, max, and the set of 
	 * options are optional.
	 * 
	 * @author Amanda Anganes
	 */
	public class Input 
	{
		public static const INTEGER:String = "integer";
		public static const CHOOSE:String = "choose";
		
		public var type:String;
		public var name:String;
		public var fromId:String;
		public var questionText:String;
		public var min:int;
		public var max:int;
		public var options:Vector.<String>;
		
		//Store user's inputs in this same object
		public var filled:Boolean = false;
		public var integerValue:int;
		public var choiceValue:String;
		
		public function Input(type, name, fromId, questionText) 
		{
			this.options = new Vector.<String>();
			this.type = type;
			this.name = name;
			this.fromId = fromId;
			this.questionText = questionText;
		}
		
	}

}