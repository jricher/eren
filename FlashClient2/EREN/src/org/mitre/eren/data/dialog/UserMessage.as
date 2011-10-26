package org.mitre.eren.data.dialog 
{
	import flash.utils.Dictionary;
	import org.as3commons.collections.LinkedMap;
	
	/**
	 * User Message data structure.  Instantiated with a UserMessage XML object, which is parsed
	 * and broken down into members of this class.
	 * 
	 * @author a_anganes
	 */
	public class UserMessage
	{
		
		private var id:String;
		private var priority:int;
		private var type:String;
		private var summary:String;
		private var text:String;
		private var responseOptions:LinkedMap;
		private var imgurl:String;
		private var location:String;
		private var name:String;
		private var occupation:String;
		private var baseURL:String;
		private var kmlLayers:Vector.<KmlLayer>;
		private var npcId:String;
		
		public var hasKML:Boolean = false;
		public var hasPreceding:Boolean = false;
		public var precedingID:String;
		public var minValue:Number;
		public var maxValue:Number;
		public var hasFollowup:Boolean = false;
		
		
		/**
		 * Constructor. Takes in an xml document and the base url of the application.
		 * 
		 * @param	x_xml User Message XML document
		 * @param	bu    Base URL of the running application
		 */
		public function UserMessage(x_xml:XML, bu:String) 
		{
			this.baseURL = bu;
			
			var dlg:Namespace = new Namespace("urn:mitre:eren:dlg:1.0");
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var xpil:Namespace = new Namespace("urn:oasis:names:tc:ciq:xpil:3");
			var xnl:Namespace = new Namespace("urn:oasis:names:tc:ciq:xnl:3");
			var msg:Namespace = new Namespace("urn:mitre:eren:dlg:1.0:msg");

			
			id = x_xml.dlg::messageID.text();
			precedingID = x_xml.dlg::precedingMessageID.text();
			if (precedingID.length >= 1) {
				hasPreceding = true;
			} else {
				hasPreceding = false;
			}
			priority = parseInt(x_xml.dlg::priority.text());
			type = x_xml.dlg::messageType.text();
			var hfqname:QName = new QName(dlg, "hasFollowup");
			if (type == "integer") {
				
				var minString:String = x_xml.dlg::responseRange.dlg::responseMin.text();
				var maxString:String = x_xml.dlg::responseRange.dlg::responseMax.text();
				
				if (minString != null && minString.length >= 1) {
					minValue = parseInt(minString);
				} else {
					minValue = 0;
				}
				
				if (maxString != null && minString.length >= 1) {
					maxValue = parseInt(maxString);
				} else {
					maxValue = 100;
				}
				
				
				if (x_xml.hasOwnProperty(hfqname)) {
					var follow:String = x_xml.@dlg::followup;
					hasFollowup = (follow == "true") ? true : false;
					trace("User message is of type integer and has global followup boolean set to " + hasFollowup);
				} else {
					hasFollowup = false;
				}
			} else {
				minValue = maxValue = 0;
			}
			if (type == "notify") {
				//Some notifications may start dialog chains
				if (x_xml.hasOwnProperty(hfqname)) {
					var follows:String = x_xml.@dlg::followup;
					hasFollowup = (follows == "true") ? true : false;
					trace("User message is of type notify and has global followup boolean set to " + hasFollowup);
				} else {
					hasFollowup = false;
				}
			}
			summary = x_xml.dlg::summary.text();
			text = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span>" 
						+ x_xml.dlg::messageText.text() + "</span></TextFlow>";
						
			var pattern:RegExp = /&lt;br&gt;/g;
			text = text.replace(pattern, "<br/>");
			
			var pattern2:RegExp = /<br>/g;
			text = text.replace(pattern2, "<br/>");
			
			//parse url from message
			imgurl = x_xml.dlg::sender.@dlg::photoURL;
			if (imgurl.length < 1) {
				imgurl = "/resources/images/Untitled.png";
			}
			
			npcId = x_xml.dlg::sender.@xpil::ID;
			trace("*********************User message, sender id is " + npcId + "*****************************");
			
			//Get name and occupation
			name = x_xml.dlg::sender.xpil::PartyName.xnl::PersonName.xnl::NameElement[0].text() + " " 
				+ x_xml.dlg::sender.xpil::PartyName.xnl::PersonName.xnl::NameElement[1].text()
			occupation = x_xml.dlg::sender.xpil::Occupations.xpil::Occupation.xpil::OccupationElement.text() 
				+ ", " + x_xml.dlg::sender.xpil::Occupations.xpil::Occupation.xpil::Employer.xnl::NameElement.text()
			if (name == null) {
				name = "unknown";
			}
			if (occupation == null) {
				occupation = "unknown";
			}
			
			//Get facility tag
			location = x_xml.dlg::location.eren::facility.text();
			if (location == null) {
				location = "";
			}
			
			//Get a list of all the response options
			var x_options:XMLList = x_xml.dlg::responseOption;
			
			responseOptions = new LinkedMap();
			
			//For each XML object in the response options list, create a responseOption
			//object and add it to the responseOptions array.
			for each (var x:XML in x_options) {
				var i:String = x.dlg::responseID.text();
				var s:String = x.dlg::shortResponse.text();
				var l:String = x.dlg::longResponse.text();
				var f:String = x.@dlg::followup;
				var fb:Boolean;
				if (f == "true") {
					fb = true;
				} else {
					fb = false;
				}
				
				//Each response option has an ID, short response, and long response.  The long response
				//may be empty.
				var resp:ResponseOption = new ResponseOption(i, s, l, fb);
				trace("Response option " + i + " has followup: " + fb);
				trace("");
				trace(" ");
				responseOptions.add(i, resp);
			}
			
			var kmlLayerQName:QName = new QName(dlg, "kmlLayer");
			if (x_xml.hasOwnProperty(kmlLayerQName)) {
				//There is at least one KML layer here
				hasKML = true;
				kmlLayers = new Vector.<KmlLayer>();
				var layers:XMLList = x_xml.dlg::kmlLayer;
				for each (var ly:XML in layers) {
					
					var layer:KmlLayer = new KmlLayer(ly.@dlg::layerID);
					var urls:XMLList = ly.dlg::kmlurl;
					
					for each (var url:XML in urls) {
						
						layer.addURL(baseURL + url.text());
					}
					
					kmlLayers.push(layer);
				}
			}
		}
		
		//************************************
		// Getters for private members
		//************************************
		
		public function getID():String {
			return id;
		}
		
		public function getPriority():int {
			return priority;
		}
		
		public function getType():String {
			return type;
		}
		
		public function getSummary():String {
			return summary;
		}
		
		public function getText():String {
			return text;
		}
		
		public function getLocation():String {
			return location;
		}
		
		public function getResponseOptions():LinkedMap {
			return responseOptions;
		}
		
		public function getImage():String {
			return imgurl;
		}
		
		public function getName():String {
			return name;
		}
		
		public function getOccupation():String {
			return occupation;
		}
		
		public function getKmlLayers():Vector.<KmlLayer> {
			return kmlLayers;
		}
		
		public function getNpcId():String {
			return npcId;
		}
		
	}

}