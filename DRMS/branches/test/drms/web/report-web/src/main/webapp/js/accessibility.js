function _nf(){
}
	
function _findClickHandler(ele){
	if(ele.onclick) { return ele.onclick;}
	if(ele.parentNode!=null){
		return _findClickHandler(ele.parentNode);
	}
	return _nf;
}
	