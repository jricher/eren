package org.mitre.eren.data.scenario 
{
	import flash.utils.Dictionary;
	import org.mitre.eren.game.utils.ErenUtilities;
	
	/**
	 * Scenario List data structure
	 * 
	 * !!
	 * @author a_anganes
	 */
	public class ScenarioListItem
	{
		
		public var name:String;
		public var ID:String;
		public var description:String;
		public var roles:Array;
		public var xml:XML;
		public var duration:String;
		public var gametime:String;
		public var minPlayers:Number = 0;
		public var maxPlayers:Number = 3;
		public var baseURL:String;
		public var scenPicURL:String = "resources/images/Locations/Norfolk.png";
		
		public function ScenarioListItem(x:XML) 
		{
			
			//trace("Got scenario list item: xml = \n");
			//trace(x);
			
			var dlg:Namespace = new Namespace("urn:mitre:eren:dlg:1.0");
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var xpil:Namespace = new Namespace("urn:oasis:names:tc:ciq:xpil:3");
			var xnl:Namespace = new Namespace("urn:oasis:names:tc:ciq:xnl:3");
			var msg:Namespace = new Namespace("urn:mitre:eren:dlg:1.0:msg");
			
			this.xml = x;
			
			ID = x.attribute("ID");
			name = x.eren::name.text();
			description = x.eren::description.text();
			
			baseURL = x.eren::baseUrl.text();
			scenPicURL = baseURL + x.eren::image.text();
			
			//Timing
			var ngametime:Number = parseInt(x.eren::timing.eren::gametime.text());
			var nduration:Number = parseInt(x.eren::timing.eren::walltime.text());
			
			duration = ErenUtilities.formatTime(nduration);
			gametime = ErenUtilities.formatTime(ngametime);
			
			roles = new Array();
			
			var xRoles = x.eren::roles.eren::role;
			var i:int = 0;
			for each (var xRole in xRoles) {

				//make role objects 
				var t:String = xRole.eren::title.text();
				var d:String = xRole.eren::description.text();
				var b:String = xRole.eren::briefing.text();
				var id:String = xRole.attribute("ID");
				var p:String = baseURL + xRole.eren::image.text();
				roles[i] = new RoleObject(1, 1, id, t, d, b, p);
				//trace("made role object " + roles[i]);
				i++;
			}
			
		}
		
		public function toString() {
			/**/
			return "scenario[name=" + name + ",description=" + description + 
				",ID=" + ID + ",roles=" + roles +"]";
			/**/
			//return xml.toString();
		}
		
		
	}

}