/*!
 *@name     jquery.barrager.js
 *@author   yaseng@uauc.net
 *@url      https://github.com/yaseng/jquery.barrager.js
 */
(function($) {

	$.fn.barrager = function(barrage) {
		barrage = $.extend({
			close:true,
			bottom: 0,
			max: 10,
			speed: 6,
			color: '#fff',
			old_ie_color : '#000000'
		}, barrage || {});

		var now=new Date(); 
		
		numOfYongDao = 1; // If PC MAC broswer, set to 8, mobile phone set to 6
	    var window_height = $(window).height() - 80;
		numOfYongDao = Math.floor( window_height / 100) +1 ;
		
		currentDanmu = new Array(numOfYongDao);
		
		if(numOfYongDao ==0){
				numOfYongDao = 1;
		}
		var maxDistance = 300;
		
		var number = now.getSeconds()%numOfYongDao;
		var previousDanmu = previous_danmus[number];
		
		var time = new Date().getTime();
		var barrager_id = 'barrage_' + time+'_YD'+number;
		var id = '#' + barrager_id;
		previous_danmus[number] = id;
		var div_barrager = $("<div class='barrage' id='" + barrager_id + "'></div>").appendTo($(this));
		
		var bottom = (barrage.bottom == 0) ? Math.floor(number* window_height /numOfYongDao + 100) : barrage.bottom;
	    // TODO Avoid dup wait for the first one! 
		
		if (bottom > window_height) 
				{bottom = bottom =100;}
				
		div_barrager.css("bottom", bottom + "px");
		
		
		var info = barrage.info;
		if (info.length < 5) {
			div_barrager.css("width",   "300px");
			div_barrager.css("right",   "-400px"); 
		} 
		else if (info.length < 10)  {
			div_barrager.css("width",   "500px");
			div_barrager.css("right",   "-600px"); 
		}
		else if (info.length < 15)  {
			div_barrager.css("width",   "800px");
			div_barrager.css("right",   "-900px"); 
		}
		else if (info.length < 20)  {
			div_barrager.css("width",   "1000px");
			div_barrager.css("right",   "-1100px"); 
		}
		else if (info.length < 30)  {
			div_barrager.css("width",   "1500px");
			div_barrager.css("right",   "-1700px"); 
		} else if (info.length < 40) {
			div_barrager.css("width",   "2000px");
			div_barrager.css("right",   "-2200px"); 
		} else {
			div_barrager.css("width",   "2500px");
			div_barrager.css("right",   "-2700px"); 
		}
		
		div_barrager_box = $("<div class='barrage_box cl'></div>").appendTo(div_barrager);
		if(barrage.img){

			div_barrager_box.append("<a class='portrait z' href=''></a>");
			var img = $("<img src='' >").appendTo(id + " .barrage_box .portrait");
			img.attr('src', barrage.img);
		}
		
		div_barrager_box.append(" <div class='z p'></div>");
		var colors = new Array();
		colors[0] = '#4FC0EA';
		colors[1] = '#5ED3B6';
		colors[2] = '#F8B238';
		colors[3] = '#F37E53'; 
		colors[4] = '#A3CDA5';
		
		var now=new Date(); 
		var number = now.getSeconds()%5;
		// alert(number);
		div_barrager_box.css('background-color',colors[number]);
		if(barrage.close){

			div_barrager_box.append(" <div class='close z'></div>");

		}
		
		var content = $("<a title='' href='' target='_blank'></a>").appendTo(id + " .barrage_box .p");
		content.attr({
			'href': barrage.href,
			'id': barrage.id
		}).empty().append(barrage.info);
		
		if(navigator.userAgent.indexOf("MSIE 6.0")>0  ||  navigator.userAgent.indexOf("MSIE 7.0")>0 ||  navigator.userAgent.indexOf("MSIE 8.0")>0  ){

			content.css('color', barrage.old_ie_color);

		}else{
			content.css('color', barrage.color);
		}
		
		var i = 0;
		var beginTime =  new Date().getTime();
		div_barrager.css('margin-right', i);
		var looper = setInterval(barrager, barrage.speed);
        
        
        
		function barrager() {
		
			var window_width = $(window).width()  ;
			var currentTime = new Date().getTime();
		    	 
			// 控制弹幕速度
			var speedRatio =0.2
			currentTime = new Date().getTime();
			
			// Time count 
			if (currentTime < beginTime + duration ) {
				// Reduce speed for mobile device
				if(window_width <800) {
					speedRatio =0.1;
				}
	
	    		var info = barrage.info;		
				currentTime = new Date().getTime();
				var firstDanmus  = new Array();
				var width = $(window).width() ;
				dmposision  = (new Date().getTime() - beginTime ) * barrage.speed*speedRatio;
				var currentdivWidth = $(id).css("width");	
				
				if( typeof currentdivWidth !==  "undefined") {
						currentdivWidth = parseFloat(currentdivWidth.replace(/px/,""));
				} else {
						currentdivWidth = 0;
				}
				
				// Move 
				$(id).css('margin-right', dmposision );
				    		  		  
				fadeoutPosision = width*0.9 ;
				if (width<600) {
						fadeoutPosision =  width *1.1;
				} 
				 if ( dmposision > fadeoutPosision ) {
				 	 $(id).fadeOut(4000);
			}
				 
			} else {
				$(id).remove();
				i=0;
 				return false;
			}

		}


		div_barrager_box.mouseover(function() {
			clearInterval(looper);
		});

		div_barrager_box.mouseout(function() {
			looper = setInterval(barrager, barrage.speed);
		});

		$(id+'.barrage .barrage_box .close').click(function(){

			$(id).remove();

		})

	}
 
	$.fn.barrager.removeAll=function(){
		 // $('.barrage').fadeOut(1000);
		 $('.barrage').remove();

	}

})(jQuery);