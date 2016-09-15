/***************************************************************************************

usagedata.js   javascript / op-dash usage data utility methods  

www.akuacom.com - Automating Demand Response
 com.akuacom.utils.StackTraceUtil.java - Copyright(c)1994 to 2011 by Akuacom . All rights reserved. 
 Redistribution and use in source and binary forms, with or without modification, is prohibited.

 ***************************************************************************************/
var akuacom = akuacom || {}
akuacom.data = {
		usage_n :
		[[0, 0.57],[.5,0.555],[1,0.535],
		[1.5,0.525],[2,0.515],[2.5,.505],[3,.51],[3.5,.52],[4,.535],[4.5,.56],
		[5,.58],[5.5,.62],[6,.655], [6.5,.695],[7,.725],[7.5,.79],[8,.815],[8.5,.835],
		[9,.89],[9.5,.91],[10,.93],[10.5,.96],[11,.97],[11.5,.97],[12,.98],[12.5,.99],
		[13,1],[13.5,.98],[14,.97],[14.5,.97],[15,.96],[15.5,.95],[16,.95],[16.5,.94],
		[17,.94],[17.5,.93],[18,.91],[18.5,.925],[19,.95],[19.5,.925],[20,.9],[20.5,.84],
		[21,.795],[21.5,.75],[22,.705],[22.5,.655],[23,.615],[23.5,.595],[24,.57],[24.5,.555]],
		
		interp : function(max, hour) {
			if(hour < 0 || hour > 24) {
				throw hour + " not between 0 and 24"
			}
			with(akuacom.data){ 
				var idx = Math.round(2 * hour)
				var [[t0,v0],[t1,v1]] = usage_n.slice(idx, idx+2)
				return akuacom.util.linterp(v0,v1,t0,t1,hour)*max
			}
		},
		
		chgOpt: function(prog, name) {
			var mbrs = prog["mbrs"] 
			var mbr 
			for(var i = 0; i < mbrs.length; i++) {
				mbr = mbrs[i]
				if(mbr.name == name) {
					mbr.inIt = !mbr.inIt
				}
			}
		},
		
		availShed: function(prog) {
			var shed = 0
			var mbr
			var mbrs = prog["mbrs"]
			for(var i = 0; i < mbrs.length; i++) {
				mbr = mbrs[i]
				if(!mbr.inIt && !mbr.offline) {
					shed+=mbr.shed
				} 
			}
			return shed
		},
		
	 	totalShed: function(prog) {
	 		var tShed = 0
	 		var mbrs = prog["mbrs"]
	 		for(var i = 0; i < mbrs.length; i++) {
	 			tShed += mbrs[i].shed
	 		}
	 		prog.totShed = tShed
	 		return tShed
	 	},
		
		line: function(prog,isBase) {
			if(!prog) {
				throw "no prog"
			}
			var usage = []
			for(var i = 0 ; i < 96; i++ ) {
				usage.push(0.0);
			}
			var mbrs = prog.mbrs
			if(!mbrs) {
				throw "no mbrs"
			}
			var mbr
			var ln
			var cmax, omax, lmax
			for(var i = 0; i < mbrs.length; i++) {
				mbr = mbrs[i]
				ln = isBase ? mbr.baseline : mbr.actual
				if(isBase || (mbr.inIt && !mbr.offline)) {
					for(var j in ln) {
						usage[j] += mbr.shed * ln[j]/ 2 
					}
				}
			}
			return usage
		},
		
	 	offline: function( id, prog) {
	 		var res = ""
	 		var div = document.getElementById(id)
	 		while(div.childNodes.length >= 1) {
	 			div.removeChild(div.firstChild)
	 		}
	 		var mbrs = prog["mbrs"]
	 		var mbr
	 		for(var i = 0; i < mbrs.length; i++) {
	 			mbr = mbrs[i]
	 			if(mbr.offline) {
	 				res += mbr.name + "\n"
	 			}
	 		}
	 		div.appendChild(document.createTextNode(res))
	 	},
	 	
	 	optOut: function(id, prog) {
	 		
	 		var res = ""
	 		var div = document.getElementById(id)
	 		while(div.childNodes.length >= 1) {
	 			div.removeChild(div.firstChild)
	 		}
	 		var mbrs = prog["mbrs"]
	 		var mbr
	 		for(var i = 0; i < mbrs.length; i++) {
	 			mbr = mbrs[i]
	 			if(!mbr.inIt) {
		 			div.appendChild(document.createTextNode(mbr.name + "(" + mbr.shed + ")"))
		 			var btn = document.createElement("button")
		 			var oc = "akuacom.data.chgOpt(progs[\"" + prog.name+ "\"],\"" + mbr.name + "\"); "
				        +"akuacom.data.optOut(\"divOO"+ prog.name + "\",progs[\""+ prog.name + "\"]);\n"
				        +"akuacom.widgets.drawShedGauge({id:\"divLShedGauge"+ prog.name + "\",w : 100,prog:progs[\""+ prog.name + "\"],g:true});\n"
				        +"akuacom.widgets.updateChart(\"divUc"+ prog.name + "\",progs[\"" + prog.name + "\"])"
		 			btn.setAttribute("onclick", oc)
		 			btn.setAttribute("value","change")
		 			div.appendChild(btn)
		 			div.appendChild(document.createElement("br"))
	 			}
	 		}
	 	}
		
		
}