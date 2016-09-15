var akuacom = akuacom || {}

akuacom.widgets  = {
		PI : Math.PI,
		cs : Math.cos,
		sn : Math.sin,
		amax : akuacom.util.amax,
		amin : akuacom.util.amin,
		
		drawShedGauge: function ( arg) {
			with(akuacom.widgets) {
				with(arg) {
					if(!id || !w || !g || ( !m && !v && !prog)) {
						throw "drawShedGauge illegal arg: must be {id,w:width (m:max value,v:current value | prog: js program obj) ,g:useGradient]}"
					}
				    var ox = w / 2
			 		var div = document.getElementById(id)
			 		while(div.childNodes.length >= 1) {
			 			div.removeChild(div.firstChild)
			 		}
				    var paper = Raphael(div, w, ox)
				    var oy = ox
				    var r = ox
				    var lr = .9 * r
				    var m = arg.m
				    if(typeof(m) == "undefined" && typeof(prog) != "undefined") {
				    	m =  prog.totShed
				    }
				    var v = arg.v
				    if(typeof(v) == "undefined" && typeof(prog) != "undefined") {
				    	v =  akuacom.data.availShed(prog)
				    }
				    
				    var a = (1 - v /m) * PI
				    var la = .95 * a
				    var ma = 1.05 * a
				    var arX = ox + r * cs(a)
				    var arY = oy - r * sn(a)
				    var delX = ox - arX
				    var delY = oy - arY
				
				    var at1X = ox + lr * cs(la)
				    var at1Y = oy - lr * sn(la)
				    var at2X = ox + lr * cs(ma)
				    var at2Y = oy - lr * sn(ma)
				
		    	    var arc = paper.path("M0," + ox + " H" + w + " A" + r + "," + r + " 0 1,0 0," +ox)
		    	    if(!!g) {
		    	    	var fillc = a > PI/2 ? "#0ff" : "#0f0"
		    	    	arc.attr({fill:"r("+ox+ ","+oy+")#fff-" + fillc})
		    	    }
		    	    paper.path( "M" + ox + "," + oy + " L" + arX + ", " + arY),
		    	    paper.path( "M" + arX + "," + arY + " L" + at1X + ", " + at1Y + " M" + arX + "," + arY + " L"  + at2X + ", " + at2Y)
				}
			}
		},
		
		drawEventGauge: function (arg) {
			with(akuacom.widgets) {
				with(akuacom.util) {
					with(arg) {
						
						if(!id || !w || !begin || !near || !start || !stop || !end || !nstyle) {
							throw "drawEventGauge illegal arg: should be {id, w, begin, near, start, stop, end, nstyle}"
						} 
						if(! ( begin < near && near < start && start < stop && stop < end)) {
							throw "drawEventGauge illegal arg ! ( begin < near < start < stop < end)" 
						}
					    var trot = -35
					    var farc = {fill: "rgb(220, 230, 242)"}
					    var nearc = {fill: "rgb(69, 129, 188)"}
					    var actc = {fill: "rgb(4, 172, 85)"}
						var height = w/6
						var scaleW = .9 * w/ (end-begin)
						
						var orgX = 5
						var orgY = 20
						
						var farW = (near - begin) * scaleW
						var nearW = (start - near) * scaleW
						var activeW = (stop-start) * scaleW
						var noneW = (end-stop) * scaleW
						
						var strtTx = orgX + farW + nearW
						var strtTy = orgY - 10
						var stpTx = orgX + farW + nearW + activeW
						var stpTy = orgY - 10
						var now = nowDec()
						var snow = now * scaleW
						var paper = Raphael(document.getElementById(id), w, w/2)
						var rFar = paper.rect(orgX, orgY, farW, height)
						rFar.attr(farc)
						var rNear = paper.rect(orgX + farW, orgY, nearW, height)
					    rNear.attr(nearc)
						var rActive = paper.rect(orgX + farW + nearW, orgY, activeW, height)
						    rActive.attr(actc)
						var flSa = Math.floor(start)
						var flSo = Math.floor(stop)
						var tStart = paper.text(strtTx, strtTy, flSa < 12 ? flSa + " am" : ( flSa > 12 ? flSa - 12 : flSa) +" pm")
						tStart.rotate(trot)
						tStart.attr({fill: "rgb(4, 172, 85)"})
						
						var tStop = paper.text(stpTx, stpTy, flSo < 12 ? flSo + " am" : ( flSo > 12 ? flSo - 12 : flSo) +" pm")
						tStop.rotate(trot)
						tStop.attr({fill: "rgb(229, 10, 16)"})
						
						var rNone = paper.rect(orgX + farW + nearW + activeW, orgY, noneW, height);
						var lEnd = paper.path("M" + (orgX + farW + nearW + activeW) + " " + orgY + " L"+(orgX + farW + nearW + activeW) + " " + (height + orgY));
						lEnd.attr({stroke: "rgb(229, 10, 16)"});
						
						
						if(contains(nstyle, '=="gradient"')) {
							if(now <= begin) {
							    rFar.attr({opacity:.5})
							} else if(now > begin && now < near) {
								rFar.attr({opacity:.5})
								paper.rect(paper.rect(orgX, orgY, (now-begin)*scaleW, height)).attr(farc)
							} 
			   				if(now <= near) {
			   					rNear.attr({opacity:.5})
			   				} else if(now > near && now < start) {
			   					rNear.attr({opacity:.5})
								paper.rect(paper.rect(orgX + farW, orgY, (now- near)*scaleW, height)).attr(nearc)
			   				}
			   				if(now <= start) {
			   					rActive.attr({opacity:.5})
			   				} else if(now > start && now < now.stop) {
			   					rActive.attr({opacity:.5})
								paper.rect(paper.rect(orgX + farW + nearW, orgY, (now- start)*scaleW, height)).attr(actc)
			   				}
						}
						if(contains(nstyle,'=="arrow"')) {
							var arrY = orgY
							var arrX = orgX
							var sx =0
							var sy =0
							var h1x = 0
							var h1y = 0
							var h2x = 0
							var h2y = 0
							var arrX = orgX
							var mOA = 0
							if(now >= begin && now <= end) {
								arrX += (now - begin) * scaleW
								sy = -15
								h1x = -5
								h1y = -5
								h2x = 5
								h2y = -5
							} else if(now < begin) {
								sx = 15
								arrY -= 10
								h1x = 5
								h1y = -5
								h2x = 5
								h2y = 5
							} else if(now > end) {
								sx = -15
								arrX += .9 * width 
								arrY -= 10
								h1x = -5
								h1y = -5
								h2x = -5
								h2y = 5
							}
							mOA = "M"+arrX+" "+arrY
							var arrowNow=paper.path(mOA+" L"+(arrX + sx)+" "+(arrY + sy)+mOA+" L"+(arrX +h1x)+" "+(arrY+h1y)+mOA+" L"+(arrX+h2x)+" "+(arrY+h2y))
								arrowNow.attr({"stroke-width":2})
						}
						
						var textY = orgY + height + 10
						var tFarX = (orgX + farW)/2
						var tNearX = orgX + farW + nearW/2
						var tActiveX = orgX + farW + nearW + activeW/2
						var tNoneX = orgX + farW + nearW + activeW + noneW/2
						var labels = paper.set();
						labels.push(paper.text(tFarX, textY, "Far").rotate(trot), 
								paper.text(tNearX, textY, "Near").rotate(trot),
								paper.text(tActiveX, textY, "Active").rotate(trot),
								paper.text(tNoneX, textY, "None").rotate(trot))
					    labels.attr({'font-size':6})
					}
				}
			}
		},
		drawMap: function(id, myCenter, markerData) {
			with(akuacom.widgets) {
		        try {
			        var myOptions = {
			               zoom: 4,
			               center: myCenter,
			               mapTypeId: google.maps.MapTypeId.ROADMAP,
			        }
			        var map = new google.maps.Map(document.getElementById(id), myOptions);
			               
		            for(var i in markerData) {
		                var m = new google.maps.Marker( {
		                       position: new google.maps.LatLng(markerData[i][0], markerData[i][1]),
		                       title: markerData[i][2],
		                       map:map,
		                       icon: "/pss2.utility/images/build.png"})
		            }
		        }catch(e) {
		            alert("error " + e)
		        }
			}
	    },
	    
		shedLegend: function(arg) {
			with(arg) {
				if(!id || !w || !h) {
					throw "showLegend illegal arg: should be {id, w, h}"
				}
				with(akuacom.widgets) {
			        try {
				        var p = Raphael(document.getElementById(id), w < 100 ? 100 : w, h < 75 ? 75 : h);
				        p.path("M0,20 h20").attr({stroke: "rgb(213, 171, 153)", 'stroke-width': 3, opacity: .9})
				        p.text(60, 20, "Baseline")
				        p.path("M0,40 h20").attr({stroke: "rgb(33, 33, 200)", 'stroke-width':3, opacity: .9})
				        p.text(60, 40, "Real-Time\nEnergy Use")
			        }catch(e) {
			            alert("error " + e)
			        }
				}
			}
	    },
	    
	    drawShedGraph: function (arg) {
			with(akuacom.widgets) {
				//alert(arg.toSource())
				// arg.id, arg.w, arg.h, arg.xoff, arg.yoff, arg.starts,
				// arg.stops, arg.prog, useGrid
				if(!arg.id || !arg.w || !arg.h || !arg.xoff || !arg.yoff || !arg.prog || !arg.useGrid ) {
					throw "shedGraph illegal arg: should be {id, w, h, xoff,yoff, starts, stops, prog, useGrid}"
				}
				var xoff = arg.xoff, yoff = arg.yoff
				var blData = akuacom.data.line(arg.prog, 1)
				var aData = akuacom.data.line(arg.prog,0)
				var seMs = 2000
		        var yMarg = 30
		        function yNs(ya) {return hp-ya+yoff}
		        function yAct(ya) {return hp-ya*ySc+yoff}
		        function xAct(x) {
		            return xoff + xSc * x
		        }
		        
		        function axes() {
		            p.path("M"+xoff+" "+(arg.h-yoff)+"v"+-hp);
		            p.path("M"+xoff+" "+(arg.h-yoff)+"arg.h"+wp);
		        }
	
		        function xticks(step,dist) {
		            var hlo = 15
		            var lbls = p.set()
		            for(var i=0;i<=wp;i+=step){
		                var _x=i/xSc
		                var _l=_x<12?"AM":"PM"
		                if(_x==24||_x==0) {
		                        _l="midnight"
		                }else{
		                    _x= _x >12 ?_x-12 :_x
		                    _l=_x.toFixed(0)+" "+_l
		                }
		                p.path("M"+(xoff+i)+" "+yAct(0)+"v"+dist).attr({stroke: "rgb(75, 76, 76)", 'stroke-width': 1,opacity:.75})
	
		                lbls.push(p.text(xoff+i,yAct(0) + hlo, _l ).rotate(90))
		            }
		            lbls.attr({"font-size": "6pt"})
		        }
	
		        function yticks(step,dist,l){
		            var vlo=-15
		            var lbls=p.set()
		            for(var i=0;i<=hp;i+=step) {
		                var _y =i/ySc
		                p.path("M"+xoff+" "+yNs(i)+"arg.h"+-dist).attr({stroke:"rgb(75, 76, 76)",'stroke-width':1,opacity:.75})
		                lbls.push(p.text(xoff+vlo,yNs(i),(_y).toFixed(0)+" "+l))
		            }
		            lbls.attr({"font-size":"6pt"})
		        }
	
		        function grid(stpx,stpy){
		            var _p =""
		            for(var i=xoff;i<wp+xoff;i+=stpx) {
		                _p+=" M"+i+" "+yoff+"v"+hp
		            }
		            for(var j=0;j<hp;j+=stpy) {
		                _p+=" M"+xoff+" "+yNs( j)+"arg.h"+wp
		            }
		            p.path(_p).attr({stroke:"rgb(225, 226, 226)",'stroke-width':1,opacity:.75})
		        }
	
		        function startLs(sx){
		            var TO=-5
		            for(var i in sx) {
		                var sX = sx[i]
			            p.path("M"+(xoff+(sX*xSc))+" "+(arg.h-yoff)+"v"+-hp).attr({stroke:"rgb(118,213,161)","stroke-width":2,opacity:1}) // .animate({opacity:1},seMs)
			            p.text(xoff+(sX*xSc)+TO,arg.h-3*yoff,"Start " + i).rotate(-90).attr({fill:"rgb(0,176,80)","font-size":"6pt"}) // .animate({y:2*yoff},seMs)
		            }
		        }
	
		        function stopLs(sx){
		            var TO=5
		            for(var i in sx) {
		            	var sX = sx[i]
		                p.path("M"+(xoff+(sX*xSc))+" "+(arg.h-yoff)+"v"+-hp).attr({stroke: "rgb(255,125,125)",'stroke-width':2,opacity:1}) // .animate({opacity:1},seMs)
		                p.text(xoff+(sX*xSc)+TO,2*yoff,"Stop "+i).rotate(-90).attr({fill:"rgb(255,13,13)","font-size":"6pt"}) // .animate({y:arg.h-2*yoff},seMs)
		            }
		        }
	
		        var nX
		        function nowL(){
		            nX = akuacom.util.nowDec()
				    if(nX > minX && nX < maxX) {
				            var textOff = 5
				            p.set(
				            		  p.path("M"+(xoff + (nX* xSc))+" "+(arg.h-yoff)+"v"+-hp).attr({'stroke-width':2}),
				            		  p.text(xoff + (nX* xSc)+textOff, arg.h - 3 * yoff, "Now").attr({'stroke-width':1,"font-size":"6pt","font-weight":"normal"}).rotate(-90)
				            		  ).attr({stroke: "rgb(125,125,125)", opacity:1});
				    }
		        }
	
		        function graph(data, attr, off, isBase) {
		            if(!off) {
		                off = 0
		            }
		            var gpath = "M" +  xAct(0) + " " + yAct(data[0])
		            if(isBase) {
			            for(var i = 1; i < data.length; i++) {
			                gpath += " L" +  (xAct(i * .25)+off)  + " " + (yAct(data[i]) + off)
			            }
		            } else {
			            for(var i = 1; i < data.length && i < nX*4; i++) {
			                gpath += " L" +  (xAct(i * .25)+off)  + " " + (yAct(data[i]) + off)
			            }
		            }
		            p.path(gpath).attr(attr)
		        }
		        if(!document.getElementById(arg.id)) {
		        	alert('no ' + arg.id)
		        }
		 		var div = document.getElementById(arg.id)
		 		while(div.childNodes.length >= 1) {
		 			div.removeChild(div.firstChild)
		 		}

		        var p = Raphael(div, arg.w, arg.h + yMarg)
		        var wp = arg.w - 2*xoff, hp = arg.h - 2*yoff
		        var min = Math.min, max = Math.max
		        var maxStrt = amax(arg.prog.starts)
		        var maxStop = amax(arg.prog.stops)
		        var minX=0, maxX=24
		        var minY=0, maxY=arg.prog.totShed
		        var xsp = maxX-minX
		        var xSc = wp/xsp
		        var ysp = maxY-minY
		        var ySc = hp/ysp
		        axes()
		        if(arg.useGrid) { grid( xSc,10*ySc) }
		        yticks( 20*ySc, 5, "kW")
		        xticks( 3*xSc, 5)

		        startLs(arg.prog.starts)
		        stopLs(arg.prog.stops)
		        nowL()
		        graph(blData, {stroke: "rgb(35, 35, 45)", 'stroke-width':3, opacity: .25},2,1)
		        graph(blData, {stroke: "rgb(213, 171, 153)", 'stroke-width': 3, opacity: .9},0,1)
	
		       // graph(aData, {stroke: "rgb(75, 76, 76)", 'stroke-width':2, opacity: .3},3,0)
		        graph(aData, {stroke: "rgb(75, 75, 170)", 'stroke-width':3, opacity: .8},2,0)
		        graph(aData, {stroke: "rgb(33, 33, 200)", 'stroke-width':3, opacity: .9},0,0)
	
			}
	    },
	    updateChart: function(theId, theProg) {
	    	return akuacom.widgets.drawShedGraph({
				id : theId,
				w : 300,
				h : 100,
				xoff : 35,
				yoff : 10,
				prog : theProg,
				useGrid : true
			})
	    }, 
	 	fontPtsForWidth: function(s, w) {
	 		with(akuacom.util) { 
		 		var pts = 0
		 		var cons = Constants.fontConvs
		 		loadJs({jspath:baseUrl() + "js/incl/fontConvs.js", test:"akuacom.util.Constants.fontConvs",cb:0})
		 		if(typeof s == "string" && w > 0) {
		 			var pxs = w / s.length
		 			var row,px,pt,lastpx,lastpt
		 			for(var i in cons) {
		 				[pt,px] = cons[i]
		 				if(pxs == px) {
		 					return pt
		 				}
		 				if( px > pxs ) {
							if(i == 0) {
								return pt/2
							}
		 					return linterp(pt,lastpt,px,lastpx,pxs)
		 				}
		 				if( i == cons.length -1) {
		 					return pt+1
		 				}
		 				[lastpx,lastpt]=[px,pt]
		 			}
		 		}
	 		}
	 	}
	 	
	
}