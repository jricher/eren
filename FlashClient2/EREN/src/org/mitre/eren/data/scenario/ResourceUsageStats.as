package org.mitre.eren.data.scenario 
{
	/**
	 * ...
	 * @author Amanda Anganes
	 */
	public class ResourceUsageStats 
	{
		
		public var stats:Vector.<ResourceUsageStat>;
		
		public function ResourceUsageStats(x:XML) 
		{
			stats = new Vector.<ResourceUsageStat>();
			
			//trace("Got resource Usage Stats: " + x);
			
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			
			var xstats:XMLList = x.eren::resourceUse;
			//trace("List of resource stats is " + xstats);
			for each (var xstat:XML in xstats) 
			{
				//trace("Got stat : " + xstat.toString());
				var type:String = xstat.@eren::resourceType;
				var available:String = xstat.@eren::resourceAvailable;
				var total:String = xstat.@eren::resourceTotal;
				
				if (type != null && available != null && total != null) 
				{	
					//trace("Creating new resourceUsageStat with " + type + ", available = " + available + ", total = " + total);
					var theStat:ResourceUsageStat = new ResourceUsageStat(type, parseInt(available), parseInt(total));
					stats.push(theStat);
				}
				else
				{
					//trace("Invalid resourceUsageStat: " + type + ", available = " + available + ", total = " + total);
				}
			}
		}
		
	}

}