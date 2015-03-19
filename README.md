# ua-parser
Groovy port of faisalman's ua-parser-js (http://faisalman.github.io/ua-parser-js/)


* Author : Faisal Salman <<fyzlman@gmail.com>>
* Groovy Port Author : Eleazar Castellanos <<eagle_boy@rocketmail.com>>

## Features

Extract detailed type of web browser, layout engine, operating system, cpu architecture, and device type/model purely from user-agent string.

## Methods

* `getBrowser()`
    * returns `{ name: '', major: '', version: '' }`

```
# Possible 'browser.name':
Amaya, Arora, Avant, Baidu, Blazer, Bolt, Camino, Chimera, Chrome, Chromium, 
Comodo Dragon, Conkeror, Dillo, Dolphin, Doris, Epiphany, Fennec, Firebird, 
Firefox, Flock, GoBrowser, iCab, ICE Browser, IceApe, IceCat, IceDragon, 
Iceweasel, IE [Mobile], Iron, Jasmine, K-Meleon, Konqueror, Kindle, Links, 
Lunascape, Lynx, Maemo, Maxthon, Midori, Minimo, [Mobile] Safari, Mosaic, Mozilla, 
Netfront, Netscape, NetSurf, Nokia, OmniWeb, Opera [Mini/Mobi/Tablet], Phoenix, 
Polaris, QQBrowser, RockMelt, Silk, Skyfire, SeaMonkey, SlimBrowser, Swiftfox, 
Tizen, UCBrowser, w3m, Yandex

# 'browser.version' & 'browser.major' determined dynamically
```

* `getDevice()`
    * returns `{ model: '', type: '', vendor: '' }` 

```
# Possible 'device.type':
console, mobile, tablet, smarttv, wearable, embedded

# Possible 'device.vendor':
Acer, Alcatel, Amazon, Apple, Archos, Asus, BenQ, BlackBerry, Dell, GeeksPhone, 
Google, HP, HTC, Huawei, Jolla, Lenovo, LG, Meizu, Microsoft, Motorola, Nexian, 
Nintendo, Nokia, Nvidia, Ouya, Palm, Panasonic, Polytron, RIM, Samsung, Sharp, 
Siemens, Sony-Ericsson, Sprint, Xbox, ZTE

# 'device.model' determined dynamically
```

* `getEngine()`
    * returns `{ name: '', version: '' }`

```
# Possible 'engine.name'
Amaya, Gecko, iCab, KHTML, Links, Lynx, NetFront, NetSurf, Presto, Tasman, 
Trident, w3m, WebKit

# 'engine.version' determined dynamically
```

* `getOS()`
    * returns `{ name: '', version: '' }`

```
# Possible 'os.name'
AIX, Amiga OS, Android, Arch, Bada, BeOS, BlackBerry, CentOS, Chromium OS, Contiki,
Fedora, Firefox OS, FreeBSD, Debian, DragonFly, Gentoo, GNU, Haiku, Hurd, iOS, 
Joli, Linpus, Linux, Mac OS, Mageia, Mandriva, MeeGo, Minix, Mint, Morph OS, NetBSD, 
Nintendo, OpenBSD, OpenVMS, OS/2, Palm, PCLinuxOS, Plan9, Playstation, QNX, RedHat, 
RIM Tablet OS, RISC OS, Sailfish, Series40, Slackware, Solaris, SUSE, Symbian, Tizen, 
Ubuntu, UNIX, VectorLinux, WebOS, Windows [Phone/Mobile], Zenwalk

# 'os.version' determined dynamically
```

* `getCPU()`
    * returns `{ architecture: '' }`

```
# Possible 'cpu.architecture'
68k, amd64, arm, arm64, avr, ia32, ia64, irix, irix64, mips, mips64, pa-risc, 
ppc, sparc, sparc64
```

* `getResult()`
    * returns `{ ua: '', browser: {}, cpu: {}, device: {}, engine: {}, os: {} }`

* `getUA()`
    * returns UA string of current instance

* `setUA(uastring)`
    * set & parse UA string
	
## Example

```UAParser uaParser = new UAParser()

    // let's test a custom user-agent string as an example
    def uastring = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.2 (KHTML, like Gecko) Ubuntu/11.10 Chromium/15.0.874.106 Chrome/15.0.874.106 Safari/535.2";
    uaParser.setUA(uastring);

    def result = uaParser.getResult();

    println result.browser;            // {name: "Chromium", major: "15", version: "15.0.874.106"}
    println result.device;             // {model: undefined, type: undefined, vendor: undefined}
    println result.os;                 // {name: "Ubuntu", version: "11.10"}
    println result.os.version;         // "11.10"
    println result.engine.name;        // "WebKit"
    println result.cpu.architecture;   // "amd64"

    // do some other tests
    var uastring2 = "Mozilla/5.0 (compatible; Konqueror/4.1; OpenBSD) KHTML/4.1.4 (like Gecko)";
    println uaParser.setUA(uastring2).getBrowser().name; // "Konqueror"
    println uaParser.getOS();                            // {name: "OpenBSD", version: undefined}
    println uaParser.getEngine();                        // {name: "KHTML", version: "4.1.4"}

    var uastring3 = 'Mozilla/5.0 (PlayBook; U; RIM Tablet OS 1.0.0; en-US) AppleWebKit/534.11 (KHTML, like Gecko) Version/7.1.0.7 Safari/534.11';
    println uaParser.setUA(uastring3).getDevice().model; // "PlayBook"
    println uaParser.getOS()                             // {name: "RIM Tablet OS", version: "1.0.0"}
    println uaParser.getBrowser().name;                  // "Safari"
```
	
### Extending regex patterns

* `UAParser(uastring[, extensions])`

Pass your own regexes to extend the limited matching rules.

```groovy
// Example:
var uaString = 'ownbrowser/1.3';
var ownBrowser = [[/(ownbrowser)\/((\d+)?[\w\.]+)/i], [UAParser.BROWSER.NAME, UAParser.BROWSER.VERSION, UAParser.BROWSER.MAJOR]];
var parser = new UAParser(uaString, {browser: ownBrowser});
println parser.getBrowser();   // {name: "ownbrowser", major: "1", version: "1.3"}
```

## License

Dual licensed under GPLv2 & MIT

Copyright © 2015 Eleazar Castellanos <<eagle_boy@rocketmail.com>>

Permission is hereby granted, free of charge, to any person obtaining a copy of 
this software and associated documentation files (the "Software"), to deal in 
the Software without restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.