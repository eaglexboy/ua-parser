package faisalman.uaparser

/**
 * Ported from
 * UAParser.js v0.6.16
 * Lightweight JavaScript-based User-Agent string parser
 * https://github.com/faisalman/ua-parser-js
 *
 * Copyright © 2012-2013 Faisalman <fyzlman@gmail.com>
 * Dual licensed under GPLv2 & MIT
 */


class UAParser {
	def mapper = new Mapper()
	
	String ua
	
	
	public UAParserService(String ua){
		setUA(ua)
	}
	
	def getBrowser(){
		return mapper.rgx(regexes.cpu)
	}
	
	def getDevice() {
        return mapper.rgx(regexes.device);
    }
	
	def getEngine() {
		return mapper.rgx(regexes.engine);
	}
	
	def getOS() {
		return mapper.rgx(regexes.os);
	}
	
	def getResult() {
		return [
			ua      : getUA(),
			browser : getBrowser(),
			engine  : getEngine(),
			os      : getOS(),
			device  : getDevice(),
			cpu     : getCPU()
		]
	}
	
	def getUA() {
        return ua;
    }
	
	def setUA(String uastring) {
		ua = uastring
		return this
	}
}
