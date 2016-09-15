/***************************************************************************************

util.js   javascript / op-dash utility methods  

www.akuacom.com - Automating Demand Response
 com.akuacom.utils.StackTraceUtil.java - Copyright(c)1994 to 2011 by Akuacom . All rights reserved. 
 Redistribution and use in source and binary forms, with or without modification, is prohibited.

 ***************************************************************************************/
var akuacom = akuacom || {}
akuacom.util = {
		Constants : new function() {
			this.generalTimeout = 5000
			this.httpl = "http://localhost"
		}(),
		amin: function (data,idx) {
		   if(! data) {
			   throw "no data"
		   }
		   var m,d;
		   if(data[0]) {
				for(var i in data) {
					d = arguments.length > 1 ? data[i][idx] : data[i]
					if( (!m && m != 0) || d < m ) {
						m = d;
					} 
				}
		    }
		    return m;
		},

		amax: function (data,idx) {
		   if(! data) {
			   throw "no data"
		   }
		   var m,d;
		   if(data[0]) {
				for(var i in data) {
					d = arguments.length > 1 ? data[i][idx] : data[i]
					if( (!m && m != 0) || d > m ) {
						m = d;
					} 
				}
		    }
		    return m;
		},

		methodName : function (m) {
			var nm ="" + m.toString(); 
			return nm.substring(0, nm.indexOf("("));
		},
		
		timefnArg: function (arg) {
			var s = new Date()
			arg.fn.apply(arg.ths,arg.args)
			alert(new Date()-s)
		},

		dump: function(o,id){
			var s = o.toString() + " "
			var nl = "<br/>\n"
			var el = document.getElementById(id)
			function iterate() {
				var obj
				for(var i in o) {
					try {
						obj = o[i]
						switch(typeof(obj)) {
							case "function":
								break
							default:
								s += i + ": " + obj + ", "
						}
					} catch(ex) {
						s += "exception!, "
					}
				}
			}
			if(el && el.innerHTML) {	
				iterate()
				el.innerHTML = s.substring(0, s.length-1)
			} else {
				nl = "\n"
				iterate()
				alert(s.substring(0, s.length-1))
			}
	 	},
	 	
	 	baseUrl: function () {
	 		with(akuacom.util) {
		 	    var u = location.href
		 	    var b = u.substring(0, u.indexOf('/', 14))
		 	    if (b.indexOf(Constants.httpl) != -1) {
		 	        return u.substr(0,  u.indexOf("/", u.indexOf(location.pathname) + 1)) + "/"
		 	    }
	 	        return b + "/"
	 		}
	 	},
	 	
		Y : function (le) {
		    	return ( function (f) {
		    		return f(f);
		    	}(function (f) {
		    		return le(function (x) {
			    		return f(f)(x)
		    		})
			    }))
		},
		
		linterp : function(a0,a1,b0,b1,b) {
			return (a1-a0)/(b1-b0)*(b-b0)+a0
		},
		nowDec : function() {
			var nD = new Date()
			return nD.getHours() + nD.getMinutes()/60.
		},
		head: function() {
			return document.getElementsByTagName("head")[0]
		},
		
		loadJs : function(arg) {
			with(akuacom.util) {
				with(arg) {
					// path,test,cb
					if(!test || ( typeof(test)=="string" && !eval(test)) ) {
						var fileref=document.createElement('script')
						fileref.type = "text/javascript"
						cb = cb || (console ? function () {console.log("loaded " + path)} : cb)
						fileref.onload=cb
						fileref.onreadystatechange = function () {
							if (this.readyState == 'complete') {
								cb()
							}
						}
						fileref.src = path
						head().appendChild(fileref)
					} else {
						if(test && eval(test)) {
							alert(jspath + " was already loaded")
						}
					}
				}
			}
		},
		loadJsRel: function(arg) {
			with(akuacom.util) {
				return loadJs(arg)
			}
		},
		forEach: function(ary,v, fn) {
			for(var i in ary) {
				v = ary[i]
				fn()
			}
		},
		
		contains: function(ary,exp) {
			for(var i in ary) {
				if(eval( 'ary[i]' + exp)) {
					return true
				}
			}
			return false
	    },
	    
	    wrap: function(str, opt_w) {
	    	var w = opt_w && opt_w > 0 ? opt_w : 40
	    	var ret = ""
	    	for(var i = 0; i < str.length; i++) {
	    		ret+=str.charAt(i)
	    		if(i % w == 0 && i > 0 ) {
	    			ret+="\n"
	    		}
	    	}
	    	return ret
	    },
	    
	    moreAlert: function(str, opt_h, opt_w) {
	    	var h = opt_h && opt_h > 0 ? opt_h : 60
   	    	var w = opt_w && opt_w > 0 ? opt_w : 90
 	    	var msg = ""
 	    	var lines = 0
 	    	var tc
 	    	var currW = 0
 	    	for(var i = 0; i < str.length; i++) {
 	    		tc = str.charAt(i)
 	    		msg+=tc
 	    		currW++
 	    		if(currW > w ) {
 	    			msg+="\n"
 	    			lines++
 	    			currW = 0
 	    		} else if(tc =="\n") {
 	    			currW = 0
 	    		}
 	    		
 	    		if(lines > h) {
 	    			alert(msg)
 	    			msg = ""
 	    			lines=0
 	    		}
 	    	}
	    },
	    
	    dec2 : function(d) {
	    	return Math.round(d*100)/100
	    }
}


