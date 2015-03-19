package faisalman.uaparser

/**
 * Ported from
 * UAParser.js v0.6.16
 * Lightweight JavaScript-based User-Agent string parser
 * https://github.com/faisalman/ua-parser-js
 *
 * Copyright © 2012-2013 Faisalman <fyzlman@gmail.com>
 * Dual licensed under GPLv2 & MIT
 **/

class Util{
	
	def has(String str1, String str2){
		return str2.toLowerCase().indexOf(str1.toLowerCase()) != -1
	}
		
	def lowerize(String str){
		return str.toLowerCase()
	}
}//UtilService