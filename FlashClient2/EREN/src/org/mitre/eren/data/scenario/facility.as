package org.mitre.eren.data.scenario 
{
	import com.google.maps.LatLng;
	import flash.display.DisplayObject;
	import flash.utils.Dictionary;
	
	/**
	 * Base class for facilities
	 * 
	 * @author a_anganes
	 */
	public class facility
	{
		
		private var _ID:String;
		private var _Name:String; 
		private var _Status:String;
		private var _Location:LatLng;
		private var _StatusToIconMap:Dictionary;
		public  var hasAnyActions:Boolean = false;
		
		public function facility(id:String, name:String, stat:String, loc:LatLng, stiMap:Dictionary) 
		{
			this._ID = id;
			this._Name = name;
			this._Status = stat;
			this._Location = loc;
			this._StatusToIconMap = stiMap;
		}
		
		public function get ID():String 
		{
			return _ID;
		}
		
		public function get Name():String 
		{
			return _Name;
		}
		
		public function get Status():String 
		{
			return _Status;
		}
		
		public function get Location():LatLng 
		{
			return _Location;
		}
		
		public function get StatusToIconMap():Dictionary
		{
			return _StatusToIconMap;
		}
		
		public function set ID(s:String):void 
		{
			_ID = s;
		}
		
		public function set Name(s:String):void 
		{
			_Name = s;
		}
		
		public function set Status(s:String):void 
		{
			_Status = s;
		}
		
		public function set Location(l:LatLng):void 
		{
			_Location = l;
		}
		
		public function set StatusToIconMap(sti:Dictionary):void
		{
			_StatusToIconMap = sti;
		}
		
	}

}