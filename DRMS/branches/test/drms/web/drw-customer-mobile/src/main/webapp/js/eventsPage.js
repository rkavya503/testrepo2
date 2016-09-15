(function($) {
	
	var hon ={};
	hon.eventstatus = function( myMap,rtpFlag  ) {
	function parseResult(input){
	if(input) return [].concat(input);
	return [];

	}

function addTask(url_value,callback){
	$.ajax({
		url: url_value,
		dataType: "json"
	}).done(function(response) {
		callback(response);
	}).fail(function(){
		//alert( "ERROR:Retrieve system service error,can't fetch response data." );
	});
}

function ViewModel() {
	var self = this;
	self.systemUpdateTime = ko.observable();
	self.date= ko.observable();
	self.week= ko.observable();
	self.category= ko.observable();
	self.categoryComputed = ko.computed(function(){
		return self.category()?self.category()+'*':'N/A';
	});
	self.forcastRTP = ko.observableArray();
	 for (key in myMap)
	 {
		eval("self."+myMap[key] + "= ko.observableArray();");
	 }
	 for (key in myMap)
	 {
		eval("self.timestamp_"+myMap[key] + "= ko.observable(0);");
	 }
	self.sync= function() {
	var url="resteasy/services/getEventStore?rtp="+rtpFlag+"&";
	 for (key in myMap)
	 {
		 url += "categoryNames="+key+"&";
	 }
	// Load initial state from server, convert it to Task instances, then populate self.tasks
	addTask(url, function(allData) {
		self.systemUpdateTime(allData.eventStore.updateTime);
		//------------------ RTP[BEGIN] ------------------
		if(allData.eventStore.todayWeather){
			self.date(allData.eventStore.todayWeather.date);
			self.week(allData.eventStore.todayWeather.dayOfWeeK);
			self.category(allData.eventStore.todayWeather.pricingCategory);
			var array = parseResult(allData.eventStore.forecastWeather);
			var result = [];
			for(var i=0;i<array.length;i++){
				var rtp = array[i];
				if((typeof rtp.pricingCategory=='undefined')){
					rtp.pricingCategory="N/A";
				}
				result.push(rtp);
			}
			self.forcastRTP(result);
		//------------------ RTP[END] ------------------
		}
		
		$.map(parseResult(allData.eventStore.events.map), 
			function(item,index) {
			for (key in myMap)
			 {
				if (key == item.key)
					eval("self."+myMap[key]+"(parseResult(item.eventsList))");
			 }
		});
		
		$.map(parseResult(allData.eventStore.lastUpdateTime.item), 
		function(temp,index) {
			eval("self.timestamp_"+myMap[temp.key]+"("+temp.value+");");
		});
	 }); 
		 return self;
	};
	
	self.getLastUpdateTime= function() {
	// Load initial state from server, convert it to Task instances, then populate self.tasks
		var _url="resteasy/services/getLastUpdateTime?";
		 for (key in myMap)
		 {
			 _url += "category="+key+"&";
		 }
		 
	$.getJSON(_url, function(allData) {
		var change=0;
		var url="resteasy/services/getEventStore?rtp="+rtpFlag+"&";
		for (key in myMap)
		 { 
			if(eval("self.timestamp_"+myMap[key]+"()")!=eval("allData.timeStamp."+key)){
				change=1;
				url += "categoryNames="+key+"&";			
			}
		 }
		 if(change ==1){
			addTask(url, function(allData) {
			self.systemUpdateTime(allData.eventStore.updateTime);
				$.map(parseResult(allData.eventStore.events.map), 
				function(item,index) {
				for (key in myMap)
				 {
					if (key == item.key) eval("self."+myMap[key]+"(parseResult(item.eventsList))");
				 }
				});
				$.map(parseResult(allData.eventStore.lastUpdateTime.item), 
				function(temp,index) {
					eval("self.timestamp_"+myMap[temp.key]+"("+temp.value+");");
				});
		 }); 
		 }
	 }); 
	 return self;
	};
}
var vm = new ViewModel().sync();
ko.applyBindings(vm);

var auto_refresh = setInterval(function() {
   vm.getLastUpdateTime();
},30000);
	
};
 
window.hon=hon;

})(jQuery);