package org.mitre.eren.display.elements 
{
	import flash.display.Bitmap;
	import flash.display.DisplayObject;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.geom.Rectangle;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.display.Loader;
	import flash.system.LoaderContext;
	import flash.utils.Dictionary;
	
	/**
	 * Image display class.  On command, loads an image from a URL and displays
	 * it in a MovieClip object. Uses a lazy-cache of previously loaded images,
	 * so that if one ImageLoader is used to repeatedly display several images
	 * it doesn't have to reload them all each time.
	 * 
	 * @author Amanda Anganes
	 */
	public class ImageLoader extends MovieClip 
	{
		
		private var ldr:Loader;
		private var urlReq:URLRequest;
		private var retry:Boolean = true;
		private var lc:LoaderContext;
		private var image:DisplayObject;
		private var hasImage:Boolean = false;
		private var imageCache:Dictionary;
		private var scalePercent:Number = 1;
		
		/**
		 * Constructor
		 */
		public function ImageLoader() 
		{
			imageCache = new Dictionary();
		}
		
		/**
		 * Load and display an image from the given URL. A scale factor may be applied.
		 * 
		 * @param	url          url of the image to display
		 * @param	addScaling   if true, checkScaleFactor and apply scaling. Disables caching.
		 * @param	scaleFactor  percentage to scale by, if addScaling = true
		 */
		public function displayImage(url:String, addScaling:Boolean = false, scaleFactor:Number = 1) 
		{
			trace("Image Loader trying to load image at url: " + url);
			if (addScaling) 
			{
				trace("ImageLoader: attempting to scale image");
				//Don't try to cache, this may be a differently sized version of
				//an image we already have so it would require reloading in that case
				scalePercent = scaleFactor;
				ldr = new Loader();
				urlReq = new URLRequest(url);
				lc = new LoaderContext(true);
				//TODO: this throws an error - cannot create property on LoaderContext
				//lc.allowLoadBytesCodeExecution = false;
				retry = true;
				ldr.load(urlReq ,lc);
				ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, scaleLoadedImage);
			}
			else 
			{
				if (imageCache.hasOwnProperty(url)) 
				{
					//We already have this image cached
					if (hasImage && (numChildren > 0)) 
					{
						removeChildAt(this.numChildren - 1);
					}
					addChild(imageCache[url]);
					hasImage = true;
				} 
				else 
				{
					ldr = new Loader();
					urlReq = new URLRequest(url);
					lc = new LoaderContext(true);
					//TODO: this throws an error - cannot create property on LoaderContext
					//lc.allowLoadBytesCodeExecution = false;
					retry = true;
					ldr.load(urlReq ,lc);
					ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, imgLoaded);
				}
			}
			
		}
		
		private function imgLoaded(e:Event):void 
		{
			//trace("Image loaded!");
			if (ldr.content != null) {
				if (hasImage && (numChildren > 0)) 
				{
					removeChildAt(this.numChildren - 1);
				}
				var bitmap:Bitmap = (ldr.content as Bitmap);
				bitmap.smoothing = true;
				imageCache[urlReq.url] = bitmap;
				hasImage = true;
				this.addChild(bitmap);
				ldr.contentLoaderInfo.removeEventListener(Event.COMPLETE, imgLoaded);
			}
			else 
			{
				if (retry) 
				{					
					retry = false;
					ldr.load(urlReq ,lc);
					ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, imgLoaded);
				}
				else 
				{
					trace("Error when attempting to load image " + urlReq.url);
				}
			}
		}
		
		private function scaleLoadedImage(e:Event):void
		{
			if (ldr.content != null) 
			{
				ldr.contentLoaderInfo.removeEventListener(Event.COMPLETE, scaleLoadedImage);
				var bitmap:Bitmap = (ldr.content as Bitmap);
				bitmap.smoothing = true;
				trace("Bitmap = " + bitmap.toString());
				//trace(bitmap.bitmapData.getPixels(new Rectangle(0, 0, 100, 100)).toString());
				var holderMc:MovieClip = new MovieClip();
				addChild(bitmap);
				trace("Image loader: scaling image " + urlReq.url + " by " + scalePercent + "%");
				bitmap.scaleX = scalePercent;
				bitmap.scaleY = scalePercent;
				addChild(bitmap);
			}
			else 
			{
				if (retry) 
				{					
					retry = false;
					ldr.load(urlReq ,lc);
					ldr.contentLoaderInfo.addEventListener(Event.COMPLETE, scaleLoadedImage);
				}
				else 
				{
					trace("Error when attempting to load image " + urlReq.url);
				}
			}
		}
	}
}