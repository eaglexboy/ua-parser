package faisalman.uaparser

import java.util.regex.Pattern

/**
 * Ported from
 * UAParser.js v0.6.16
 * Lightweight JavaScript-based User-Agent string parser
 * https://github.com/faisalman/ua-parser-js
 *
 * Copyright © 2012-2013 Faisalman <fyzlman@gmail.com>
 * Dual licensed under GPLv2 & MIT
 */

class MapperService {
	def util
	
	/** Constants */
	def EMPTY       = ''
	def UNKNOWN     = '?'
	def FUNC_TYPE   = 'function'
	def UNDEF_TYPE  = 'undefined'
	def OBJ_TYPE    = 'object'
	def MAJOR       = 'major'
	def MODEL       = 'model'
	def NAME        = 'name'
	def TYPE        = 'type'
	def VENDOR      = 'vendor'
	def VERSION     = 'version'
	def ARCHITECTURE= 'architecture'
	def CONSOLE     = 'console'
	def MOBILE      = 'mobile'
	def TABLET      = 'tablet'
	
	
	/** String map **/
	Map maps = [
		browser : [
			oldsafari : [
				major : [
					'1' : ['/8', '/1', '/3'],
					'2' : '/4',
					'?' : '/'
				],
				version : [
					'1.0'   : '/8',
					'1.2'   : '/1',
					'1.3'   : '/3',
					'2.0'   : '/412',
					'2.0.2' : '/416',
					'2.0.3' : '/417',
					'2.0.4' : '/419',
					'?'     : '/'
				]
			]
		],

		device : [
			sprint : [
				model : [
					'Evo Shift 4G' : '7373KT'
				],
				vendor : [
					'HTC'       : 'APA',
					'Sprint'    : 'Sprint'
				]
			]
		],

		os : [
			windows : [
				version : [
					'ME'        : '4.90',
					'NT 3.11'   : 'NT3.51',
					'NT 4.0'    : 'NT4.0',
					'2000'      : 'NT 5.0',
					'XP'        : ['NT 5.1', 'NT 5.2'],
					'Vista'     : 'NT 6.0',
					'7'         : 'NT 6.1',
					'8'         : 'NT 6.2',
					'8.1'       : 'NT 6.3',
					'RT'        : 'ARM'
				]
			]
		]
	]
	
	/** Regex map **/
	Map regexes = [
		browser : [
			[
				// Presto based
				new Pattern('/(opera\\smini)\\/((\\d+)?[\\w\\.-]+)/i'),                                // Opera Mini
				new Pattern('/(opera\\s[mobiletab]+).+version\\/((\\d+)?[\\w\\.-]+)/i'),               // Opera Mobi/Tablet
				new Pattern('/(opera).+version\\/((\\d+)?[\\w\\.]+)/i'),                               // Opera > 9.80
				new Pattern('/(opera)[\\/\\s]+((\\d+)?[\\w\\.]+)/i')                                   // Opera < 9.80
	
			],
			[NAME, VERSION, MAJOR],
			[
				new Pattern('/\\s(opr)\\/((\\d+)?[\\w\\.]+)/i')                                         // Opera Webkit
			],
			[
				[NAME, 'Opera'],
				VERSION, MAJOR
			],
			[
				// Mixed
				new Pattern('/(kindle)\\/((\\d+)?[\\w\\.]+)/i'),                                       // Kindle
				new Pattern('/(lunascape|maxthon|netfront|jasmine|blazer)[\\/\\s]?((\\d+)?[\\w\\.]+)*/i'),
																									 // Lunascape/Maxthon/Netfront/Jasmine/Blazer
	
				// Trident based
				new Pattern('/(avant\\s|iemobile|slim|baidu)(?:browser)?[\\/\\s]?((\\d+)?[\\w\\.]*)/i'), // Avant/IEMobile/SlimBrowser/Baidu
				new Pattern('/(?:ms|\\()(ie)\\s((\\d+)?[\\w\\.]+)/i'),                                  // Internet Explorer
		
				// Webkit/KHTML based
				new Pattern('/(rekonq)((?:\\/)[\\w\\.]+)*/i'),                                        // Rekonq
				new Pattern('/(chromium|flock|rockmelt|midori|epiphany|silk|skyfire|ovibrowser|bolt|iron)\\/((\\d+)?[\\w\\.-]+)/i')
																									 // Chromium/Flock/RockMelt/Midori/Epiphany/Silk/Skyfire/Bolt/Iron
			],
			[NAME, VERSION, MAJOR],
			[
				new Pattern('/(trident).+rv[:\\s]((\\d+)?[\\w\\.]+).+like\\sgecko/i')                   // IE11
			],
			[
				[NAME, 'IE'],
				VERSION,
				MAJOR
			],
			[
				new Pattern('/(yabrowser)\\/((\\d+)?[\\w\\.]+)/i')                                     // Yandex
			],
			[
				[NAME, 'Yandex'],
				VERSION,
				MAJOR
			],
			[
				new Pattern('/(comodo_dragon)\\/((\\d+)?[\\w\\.]+)/i')                                 // Comodo Dragon
			],
			[
				[NAME, new Pattern('/_/g'), ' '],
				VERSION,
				MAJOR
			],
			[
				new Pattern('/(chrome|omniweb|arora|[tizenoka]{5}\\s?browser)\\/v?((\\d+)?[\\w\\.]+)/i')
																									 // Chrome/OmniWeb/Arora/Tizen/Nokia
			],
			[NAME, VERSION, MAJOR],
			[
				new Pattern('/(dolfin)\\/((\\d+)?[\\w\\.]+)/i')                                        // Dolphin
			],
			[
				[NAME, 'Dolphin'],
				VERSION,
				MAJOR
			],
			[
				new Pattern('/((?:android.+)crmo|crios)\\/((\\d+)?[\\w\\.]+)/i')                       // Chrome for Android/iOS
			],
			[
				[NAME, 'Chrome'],
				VERSION,
				MAJOR
			],
			[
				new Pattern('/version\\/((\\d+)?[\\w\\.]+).+?mobile\\/\\w+\\s(safari)/i')                 // Mobile Safari
			],
			[
				VERSION,
				MAJOR,
				[NAME, 'Mobile Safari']
			],
			[
				new Pattern('/version\\/((\\d+)?[\\w\\.]+).+?(mobile\\s?safari|safari)/i')             // Safari & Safari Mobile
			],
			[VERSION, MAJOR, NAME],
			[
				new Pattern('/webkit.+?(mobile\\s?safari|safari)((\\/[\\w\\.]+))/i')                   // Safari < 3.0
			],
			[
				NAME,
				[MAJOR, mapper.str, maps.browser.oldsafari.major],
				[VERSION, mapper.str, maps.browser.oldsafari.version]
			],
			[
				new Pattern('/(konqueror)\\/((\\d+)?[\\w\\.]+)/i'),                                    // Konqueror
				new Pattern('/(webkit|khtml)\\/((\\d+)?[\\w\\.]+)/i')
			],
			[NAME, VERSION, MAJOR],
			[
				// Gecko based
				new Pattern('/(navigator|netscape)\\/((\\d+)?[\\w\\.-]+)/i')                           // Netscape
			],
			[
				[NAME, 'Netscape'],
				VERSION,
				MAJOR
			],
			[
				new Pattern('/(swiftfox)/i'),                                                      	// Swiftfox
				new Pattern('/(icedragon|iceweasel|camino|chimera|fennec|maemo\\sbrowser|minimo|conkeror)[\\/\\s]?((\\d+)?[\\w\\.\\+]+)/i'),
																									// IceDragon/Iceweasel/Camino/Chimera/Fennec/Maemo/Minimo/Conkeror
				new Pattern('/(firefox|seamonkey|k-meleon|icecat|iceape|firebird|phoenix)\\/((\\d+)?[\\w\\.-]+)/i'),
																									// Firefox/SeaMonkey/K-Meleon/IceCat/IceApe/Firebird/Phoenix
				new Pattern('/(mozilla)\\/((\\d+)?[\\w\\.]+).+rv\\:.+gecko\\/\\d+/i'),              // Mozilla
	
				// Other
				new Pattern('/(uc\\s?browser|polaris|lynx|dillo|icab|doris|amaya|w3m|netsurf|qqbrowser)[\\/\\s]?((\\d+)?[\\w\\.]+)/i'),
																				// UCBrowser/Polaris/Lynx/Dillo/iCab/Doris/Amaya/w3m/NetSurf/QQBrowser
				new Pattern('/(links)\\s\\(((\\d+)?[\\w\\.]+)/i'),                                     // Links
				new Pattern('/(gobrowser)\\/?((\\d+)?[\\w\\.]+)*/i'),                                  // GoBrowser
				new Pattern('/(ice\\s?browser)\\/v?((\\d+)?[\\w\\._]+)/i'),                            // ICE Browser
				new Pattern('/(mosaic)[\\/\\s]((\\d+)?[\\w\\.]+)/i')                                   // Mosaic
			],
			[NAME, VERSION, MAJOR]
		],
	
		cpu : [
			[
				new Pattern('/(?:(amd|x(?:(?:86|64)[_-])?|wow|win)64)[;\\)]/i')                     // AMD64
			],
			[
				[ARCHITECTURE, 'amd64']
			],
			[
				new Pattern('/((?:i[346]|x)86)[;\\)]/i')                                            // IA32
			],
			[
				[ARCHITECTURE, 'ia32']
			],
			[
				// PocketPC mistakenly identified as PowerPC
				new Pattern('/windows\\s(ce|mobile);\\sppc;/i')
			],
			[
				[ARCHITECTURE, 'arm']
			],
			[
				new Pattern('/((?:ppc|powerpc)(?:64)?)(?:\\smac|;|\\))/i')                           // PowerPC
			],
			[
				[ARCHITECTURE, /ower/, '', util.lowerize]
			],
			[
				new Pattern('/(sun4\\w)[;\\)]/i')                                                    // SPARC
			],
			[
				[ARCHITECTURE, 'sparc']
			],
			[
				new Pattern('/(ia64(?=;)|68k(?=\\))|arm(?=v\\d+;)|(?:irix|mips|sparc)(?:64)?(?=;)|pa-risc)/i')
																							 // IA64, 68K, ARM, IRIX, MIPS, SPARC, PA-RISC
			],
			[ARCHITECTURE, util.lowerize]
		],
	
		device : [
			[
				new Pattern('/\\((ipad|playbook);[\\w\\s\\);-]+(rim|apple)/i')                         // iPad/PlayBook
			],
			[
				MODEL,
				VENDOR,
				[TYPE, TABLET]
			],
			[
				new Pattern('/(hp).+(touchpad)/i'),                                                    // HP TouchPad
				new Pattern('/(kindle)\\/([\\w\\.]+)/i'),                                              // Kindle
				new Pattern('/\\s(nook)[\\w\\s]+build\\/(\\w+)/i'),                                    // Nook
				new Pattern('/(dell)\\s(strea[kpr\\s\\d]*[\\dko])/i')                                  // Dell Streak
			],
			[
				VENDOR,
				MODEL,
				[TYPE, TABLET]
			],
			[
				new Pattern('/\\((ip[honed|\\s\\w*]+);.+(apple)/i')                                    // iPod/iPhone
			],
			[
				MODEL,
				VENDOR,
				[TYPE, MOBILE]
			],
			[
				new Pattern('/(blackberry)[\\s-]?(\\w+)/i'),                                         // BlackBerry
				new Pattern('/(blackberry|benq|palm(?=\\-)|sonyericsson|acer|asus|dell|huawei|meizu|motorola)[\\s_-]?([\\w-]+)*/i'),
																									 // BenQ/Palm/Sony-Ericsson/Acer/Asus/Dell/Huawei/Meizu/Motorola
				new Pattern('/(hp)\\s([\\w\\s]+\\w)/i'),                                             // HP iPAQ
				new Pattern('/(asus)-?(\\w+)/i')                                                     // Asus
			],
			[
				VENDOR,
				MODEL,
				[TYPE, MOBILE]
			],
			[
				new Pattern('/\\((bb10);\\s(\\w+)/i')                                                 // BlackBerry 10
			],
			[
				[VENDOR, 'BlackBerry'],
				MODEL,
				[TYPE, MOBILE]
			],
			[
				new Pattern('/android.+((transfo[prime\\s]{4,10}\\s\\w+|eeepc|slider\\s\\w+))/i')       // Asus Tablets
			],
			[
				[VENDOR, 'Asus'],
				MODEL,
				[TYPE, TABLET]
			],
			[
				new Pattern('/(sony)\\s(tablet\\s[ps])/i')                                           // Sony Tablets
			],
			[VENDOR, MODEL, [TYPE, TABLET]],
			[
				new Pattern('/(nintendo)\\s([wids3u]+)/i')                                          // Nintendo
			],
			[VENDOR, MODEL, [TYPE, CONSOLE]],
			[
				new Pattern('/((playstation)\\s[3portablevi]+)/i')                                  // Playstation
			],
			[[VENDOR, 'Sony'], MODEL, [TYPE, CONSOLE]],
			[
				new Pattern('/(sprint\\s(\\w+))/i')                                                  // Sprint Phones
			],
			[
				[VENDOR, mapper.str, maps.device.sprint.vendor],
				[MODEL, mapper.str, maps.device.sprint.model],
				[TYPE, MOBILE]
			],
			[
				new Pattern('/(htc)[;_\\s-]+([\\w\\s]+(?=\\))|\\w+)*/i'),                        // HTC
				new Pattern('/(zte)-(\\w+)*/i'),                                                 // ZTE
				new Pattern('/(alcatel|geeksphone|huawei|lenovo|nexian|panasonic|(?=;\\s)sony)[_\\s-]?([\\w-]+)*/i')
																								 // Alcatel/GeeksPhone/Huawei/Lenovo/Nexian/Panasonic/Sony
			],
			[VENDOR, [MODEL, new Pattern('/_/g'), ' '], [TYPE, MOBILE]],
			[																			// Motorola
				new Pattern('/\\s((milestone|droid(?:[2-4x]|\\s(?:bionic|x2|pro|razr))?(:?\\s4g)?))[\\w\\s]+build\\//i'),
				new Pattern('/(mot)[\\s-]?(\\w+)*/i')
			],
			[[VENDOR, 'Motorola'], MODEL, [TYPE, MOBILE]],
			[
				new Pattern('/android.+\\s((mz60\\d|xoom[\\s2]{0,2}))\\sbuild\\//i')
			],
			[[VENDOR, 'Motorola'], MODEL, [TYPE, TABLET]],
			[
				new Pattern('/android.+((sch-i[89]0\\d|shw-m380s|gt-p\\d{4}|gt-n8000|sgh-t8[56]9))/i')
			],
			[[VENDOR, 'Samsung'], MODEL, [TYPE, TABLET]],
			[
				// Samsung
				new Pattern('/((s[cgp]h-\\w+|gt-\\w+|galaxy\\snexus))/i'),
				new Pattern('/(sam[sung]*)[\\s-]*(\\w+-?[\\w-]*)*/i'),
				new Pattern('/sec-((sgh\\w+))/i')
			],
			[[VENDOR, 'Samsung'], MODEL, [TYPE, MOBILE]],
			[
				new Pattern('/(sie)-(\\w+)*/i')                                                     // Siemens
			],
			[[VENDOR, 'Siemens'], MODEL, [TYPE, MOBILE]],
			[
				new Pattern('/(maemo|nokia).*(n900|lumia\\s\\d+)/i'),                                // Nokia
				new Pattern('/(nokia)[\\s_-]?([\\w-]+)*/i')
			],
			[[VENDOR, 'Nokia'], MODEL, [TYPE, MOBILE]],
			[
				new Pattern('/android\\s3\\.[\\s\\w-;]{10}((a\\d{3}))/i')                               // Acer
			],
			[[VENDOR, 'Acer'], MODEL, [TYPE, TABLET]],
			[
				new Pattern('/android\\s3\\.[\\s\\w-;]{10}(lg?)-([06cv9]{3,4})/i')                     // LG
			],
			[[VENDOR, 'LG'], MODEL, [TYPE, TABLET]],
			[
				new Pattern('/((nexus\\s[45]))/i'),
				new Pattern('/(lg)[e;\\s-\\/]+(\\w+)*/i')
			],
			[[VENDOR, 'LG'], MODEL, [TYPE, MOBILE]],
			[
				new Pattern('/android.+((ideatab[a-z0-9\\-\\s]+))/i')                               // Lenovo
			],
			[[VENDOR, 'Lenovo'], MODEL, [TYPE, TABLET]],
			[
				new Pattern('/(mobile|tablet);.+rv\\:.+gecko\\//i')                                  // Unidentifiable
			],
			[TYPE, VENDOR, MODEL]
		],
	
		engine : [
			[
				new Pattern('/(presto)\\/([\\w\\.]+)/i'),                                             // Presto
				new Pattern('/(webkit|trident|netfront|netsurf|amaya|lynx|w3m)\\/([\\w\\.]+)/i'),     // WebKit/Trident/NetFront/NetSurf/Amaya/Lynx/w3m
				new Pattern('/(khtml|tasman|links)[\\/\\s]\\(?([\\w\\.]+)/i'),                          // KHTML/Tasman/Links
				new Pattern('/(icab)[\\/\\s]([23]\\.[\\d\\.]+)/i')                                      // iCab
			],
			[NAME, VERSION],
			[
				new Pattern('/rv\\:([\\w\\.]+).*(gecko)/i')                                           // Gecko
			],
			[VERSION, NAME]
		],
	
		os : [
			[
				// Windows based
				new Pattern('/(windows)\\snt\\s6\\.2;\\s(arm)/i'),                                     // Windows RT
				new Pattern('/(windows\\sphone(?:\\sos)*|windows\\smobile|windows)[\\s\\/]?([ntce\\d\\.\\s]+\\w)/i')
			],
			[NAME, [VERSION, mapper.str, maps.os.windows.version]],
			[
				new Pattern('/(win(?=3|9|n)|win\\s9x\\s)([nt\\d\\.]+)/i')
			],
			[[NAME, 'Windows'], [VERSION, mapper.str, maps.os.windows.version]],
			[
				// Mobile/Embedded OS
				new Pattern('/\\((bb)(10);/i')                                                      // BlackBerry 10
			],
			[[NAME, 'BlackBerry'], VERSION],
			[
				new Pattern('/(blackberry)\\w*\\/?([\\w\\.]+)*/i'),                                    // Blackberry
				new Pattern('/(tizen)\\/([\\w\\.]+)/i'),                                              // Tizen
				new Pattern('/(android|webos|palm\\os|qnx|bada|rim\\stablet\\sos|meego)[\\/\\s-]?([\\w\\.]+)*/i')
																								 // Android/WebOS/Palm/QNX/Bada/RIM/MeeGo
			],
			[NAME, VERSION],
			[
				new Pattern('/(symbian\\s?os|symbos|s60(?=;))[\\/\\s-]?([\\w\\.]+)*/i')                 // Symbian
			],
			[[NAME, 'Symbian'], VERSION],
			[
				new Pattern('/mozilla.+\\(mobile;.+gecko.+firefox/i')                               // Firefox OS
			],
			[[NAME, 'Firefox OS'], VERSION],
			[
				// Console
				new Pattern('/(nintendo|playstation)\\s([wids3portablevu]+)/i'),                    // Nintendo/Playstation
		
				// GNU/Linux based
				new Pattern('/(mint)[\\/\\s\\(]?(\\w+)*/i'),                                           // Mint
				new Pattern('/(joli|[kxln]?ubuntu|debian|[open]*suse|gentoo|arch|slackware|fedora|mandriva|centos|pclinuxos|redhat|zenwalk)[\\/\\s-]?([\\w\\.-]+)*/i'),
																					// Joli/Ubuntu/Debian/SUSE/Gentoo/Arch/Slackware
																					// Fedora/Mandriva/CentOS/PCLinuxOS/RedHat/Zenwalk
				new Pattern('/(hurd|linux)\\s?([\\w\\.]+)*/i'),                                       // Hurd/Linux
				new Pattern('/(gnu)\\s?([\\w\\.]+)*/i')                                               // GNU
			],
			[NAME, VERSION],
			[
				new Pattern('/(cros)\\s[\\w]+\\s([\\w\\.]+\\w)/i')                                       // Chromium OS
			],
			[[NAME, 'Chromium OS'], VERSION],
			[
				// Solaris
				new Pattern('/(sunos)\\s?([\\w\\.]+\\d)*/i')                                           // Solaris
			],
			[[NAME, 'Solaris'], VERSION],
			[
				// BSD based
				new Pattern('/\\s([frentopc-]{0,4}bsd|dragonfly)\\s?([\\w\\.]+)*/i')                   // FreeBSD/NetBSD/OpenBSD/PC-BSD/DragonFly
			],
			[NAME, VERSION],
			[
				new Pattern('/(ip[honead]+)(?:.*os\\s*([\\w]+)*\\slike\\smac|;\\sopera)/i')             // iOS
			],
			[[NAME, 'iOS'], [VERSION, new Pattern('/_/g'), '.']],
			[
				new Pattern('/(mac\\sos\\sx)\\s?([\\w\\s\\.]+\\w)*/i')                                    // Mac OS
			],
			[NAME, [VERSION, new Pattern('/_/g'), '.']],
			[
				// Other
				new Pattern('/(haiku)\\s(\\w+)/i'),                                                  // Haiku
				new Pattern('/(aix)\\s((\\d)(?=\\.|\\)|\\s)[\\w\\.]*)*/i'),                               // AIX
				new Pattern('/(macintosh|mac(?=_powerpc)|plan\\s9|minix|beos|os\\/2|amigaos|morphos|risc\\sos)/i'),
																					// Plan9/Minix/BeOS/OS2/AmigaOS/MorphOS/RISCOS
				new Pattern('/(unix)\\s?([\\w\\.]+)*/i')                                              // UNIX
			],
			[NAME, VERSION]
		]
	]
	
	
	
	def rgx(arguments) {
		def result
		def i = 0
		def j
		def k
		def q
		def matches
		def match
		def args
		
		// loop through all regexes maps
        for (args = arguments; i < args.size(); i += 2) {

                def regex = args[i]       // even sequence (0,2,4,..)
                def props = args[i + 1];   // odd sequence (1,3,5,..)

                // construct object barebones
                if ( typeof(result) == UNDEF_TYPE) {
                    result = {};
                    for(p in props) {
                        q = props[p];
                        if (typeof(q) == OBJ_TYPE) {
                            result[q[0]] = null;
                        } else {
                            result[q] = null;
                        }
                    }
                }

                // try matching uastring with regexes
                for (j = k = 0; j < regex.size(); j++) {
                    matches = regex[j].exec(this.getUA());
                    if (!!matches) {
                        for (p = 0; p < props.length; p++) {
                            match = matches[++k];
                            q = props[p];
                            // check if given property is actually array
                            if (typeof(q) == OBJ_TYPE && q.length > 0) {
                                if (q.length == 2) {
                                    if (typeof(q[1]) == FUNC_TYPE) {
                                        // assign modified match
                                        result[q[0]] = q[1].call(this, match);
                                    } else {
                                        // assign given value, ignore regex match
                                        result[q[0]] = q[1];
                                    }
                                } else if (q.length == 3) {
                                    // check whether function or regex
                                    if (typeof(q[1]) == FUNC_TYPE && !(q[1].exec && q[1].test)) {
                                        // call function (usually string mapper)
                                        result[q[0]] = match ? q[1].call(this, match, q[2]) : undefined;
                                    } else {
                                        // sanitize match using given regex
                                        result[q[0]] = match ? match.replace(q[1], q[2]) : undefined;
                                    }
                                } else if (q.length == 4) {
                                        result[q[0]] = match ? q[3].call(this, match.replace(q[1], q[2])) : undefined;
                                }
                            } else {
                                result[q] = match ? match : undefined;
                            }
                        }
                        break;
                    }
                }

                if(!!matches) break; // break the loop immediately if match found
            }
            return result;
        }

        def str(str, map) {
            for (def i in map) {
                // check if array
                if(typeof(map[i]) == OBJ_TYPE && map[i].size() > 0) {
                    for (def j = 0; j < map[i].size(); j++) {
                        if (util.has(map[i][j], str)) {
                            return (i == UNKNOWN) ? null : i;
                        }
                    }
                } else if (util.has(map[i], str)) {
                    return (i == UNKNOWN) ? null : i;
                }
            }
            return str;
        }// str
		
		String typeof(object){
			List theClass = object.getClass().toString().split()[1].split(/\./).toList()
			return theClass[ theClass.size() - 1 ].toLowerCase()
		}// typeof
}//MapperService