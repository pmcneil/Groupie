// Sphere v0.3
// Design/Programming by Ulyses
// Â© 2007 ColorJack.com

function $(v) { return((typeof(v)=='object'?v:document).getElementById(v)); }
function $S(v) { return(document.getElementById(v).style); }
function $SS(v) { var b=document.styleSheets[0]; if(b.cssRules) b=b.cssRules; else b=b.rules; return(b[v].style); }
function $T(v,i) { return((typeof(i)=='object'?i:(i?$(i):document)).getElementsByTagName(v)); }
function absPos(e) { var r={x:e.offsetLeft,y:e.offsetTop}; if(e.offsetParent) { var v=absPos(e.offsetParent); r.x+=v.x; r.y+=v.y; } return(r); }
function agent(v) { return(Math.max(navigator.userAgent.toLowerCase().indexOf(v),0)); }
function cookieStab(f,v) { document.cookie=f+"="+v+"; path=/"; }
function cookieGrab(v) { var b=v+"=",d=document.cookie.split(';'); for(var i=0; i<d.length; i++) { var c=d[i]; while(c.charAt(0)==' ') c=c.substring(1,c.length); if(c.indexOf(b)==0) return(c.substring(b.length,c.length)); } }
function XY(e,v) { var z=agent('msie')?Array(event.clientX+document.body.scrollLeft,event.clientY+document.body.scrollTop):Array(e.pageX,e.pageY); return(v==3?z:z[zero(v)]); }
function XYwin(v) { var z=agent('msie')?[document.body.clientHeight,document.body.clientWidth]:[window.innerHeight,window.innerWidth]; return(!isNaN(v)?z[v]:z); }
function zindex(d) { d.style.zIndex=zINDEX++; }
function zero(v) { v=parseFloat(v); return(!isNaN(v)?v:0); }

/* GLOBALS */

var W=360, W2=W/2, W3=W2/2, xy=[244,113], zINDEX=27, stop=1;

var func=function(v){ return(websafe(fBlind[opVal('blind')](hsv2rgb(ryb2hsv(v))))); };

var fu={'Neutral':[0,15,30,45,60,75],
		'Analogous':[0,30,60,90,120,150],
		'Clash':[0,[0,30],90,[90,30],270,[270,30]],
		'Complementary':[0,[0,-30],[0,30],180,[180,-30],[180,30]],
		'Split-Complementary':[0,[0,30],150,[150,30],210,[210,30]],
		'Split-Complementary (CW)':[0,[0,30],150,[150,30],300,[300,30]],
		'Split-Complementary (CCW)':[0,[0,30],60,[60,30],210,[210,30]],
		'Triadic':[0,[0,30],120,[120,30],240,[240,30]],
		'Tetradic':[0,90,[90,30],180,270,[270,30]],
		'Four-Tone (CW)':[0,60,[60,30],180,240,[240,30]],
		'Four-Tone (CCW)':[0,120,[120,30],180,300,[300,30]],
		'Five-Tone (A)':[0,[0,30],115,155,205,245],
		'Five-Tone (B)':[0,40,90,130,245,[245,30]],
		'Five-Tone (C)':[0,50,90,205,[205,30],320],
		'Five-Tone (D)':[0,40,155,[155,30],270,310],
		'Five-Tone (E)':[0,115,[115,30],230,270,320],
		'Six-Tone (CW)':[0,30,120,150,240,270],
		'Six-Tone (CCW)':[0,90,120,210,240,330],
		'Spiral':[0,[5,15],[10,25],[15,35],[20,45],[25,55]] };

/* SPHERE */

function Sphere() { var oL=18, oT=62, R=Math.PI*2;

	if(flash) { with(flax) { var R2=R/359, O=(90/360)*R;

		for(var i=0; i<=359; i++) {

			var rad=(i/360)*R-O, color=[0xFFFFFF, '0x'+func([i,100,100]), 0x000000];

			callFunction("beginGradientFill", "linear", color, [100, 100, 100], [128, 192, 255], {matrixType:"box", x:oL, y:oT, w:W, h:W, r:rad});

			moveTo(W2+oL, W2+oT); lineTo((Math.cos(rad)*W2)+W2+oL, (Math.sin(rad)*W2)+W2+oT); lineTo((Math.cos(rad+R2)*W2)+W2+oL, (Math.sin(rad+R2)*W2)+W2+oT);

	} } } else { with(S2D) { var H=W/2, O=R*(1/360);

		for(var i=0; i<=359; i++) { var g=createLinearGradient(H+oL, oT, H+oL, H+oT);

			with(g) { addColorStop(0,'#000'); addColorStop(.5,'#'+func([i, 100, 100])); addColorStop(1,'#FFF'); }

			beginPath(); moveTo(H+oL, oT); lineTo(H+oL, H+oT); lineTo(H+oL+1, H+oT); lineTo(H+oL+4, oT); fillStyle=g; fill();

			translate(H+oL, H+oT); rotate(O); translate(-(H+oL), -(H+oT));

} } } }

/* KONTROL */

var rgb=[255,0,0], hsv=[0,100,100], R1={'red':255,'green':255,'blue':255}, R2={'hue':359,'saturation':100,'balance':100};

function kVal(v,n) { return(parseInt((1-(zero($S(v+'Cur').left)+4)/121)*n)); }

function Kontrol() { var z='';

	var r={'hue':"function(v,b) { hsv[0]=kVal('hue',360); rgb=hsv2rgb(hsv); Scheme(0,0,hsv); }",
		   'saturation':"function(v,b) { hsv[1]=kVal('saturation',100); rgb=hsv2rgb(hsv); Scheme(0,0,hsv); }",
		   'balance':"function(v,b) { hsv[2]=kVal('balance',100); rgb=hsv2rgb(hsv); Scheme(0,0,hsv); }",
		   'red':"function(v,b) { rgb[0]=kVal('red',255); Scheme(0,0,rgb2hsv(rgb)); }",
		   'green':"function(v,b) { rgb[1]=kVal('green',255); Scheme(0,0,rgb2hsv(rgb)); }",
		   'blue':"function(v,b) { rgb[2]=kVal('blue',255); Scheme(0,0,rgb2hsv(rgb)); }"};

	for(var i in r) { z+='<div onmousedown="coreXY(\''+i+"Cur',event,[117,0,-4,0,-4],1,"+r[i]+')" onmouseup="save()"><span class="west">'+i+"<\/span><span id=\""+i+"Me\" class=\"east\"><\/span>"+'<br><div id="'+i+'Cur">'+"<\/div><\/div>"; }

	$('kontrol').innerHTML=z;

};

function Run() { var j=0;

	var R={'hue':[[0,[0,hsv[1],hsv[2]]],[0.15,[300,hsv[1],hsv[2]]],[0.30,[240,hsv[1],hsv[2]]],[0.50,[180,hsv[1],hsv[2]]],[0.65,[120,hsv[1],hsv[2]]],[0.85,[60,hsv[1],hsv[2]]],[1,[0,hsv[1],hsv[2]]]],
		   'saturation':[[0,[hsv[0],100,hsv[2]]],[1,[hsv[0],0,hsv[2]]]],
		   'balance':[[0,[hsv[0],hsv[1],100]],[1,[hsv[0],hsv[1],0]]],
		   'red':[[0,rgb2hsv([255,rgb[1],rgb[2]])],[1,rgb2hsv([0,rgb[1],rgb[2]])]],
		   'green':[[0,rgb2hsv([rgb[0],255,rgb[2]])],[1,rgb2hsv([rgb[0],0,rgb[2]])]],
		   'blue':[[0,rgb2hsv([rgb[0],rgb[1],255])],[1,rgb2hsv([rgb[0],rgb[1],0])]]};

	function pos(j) { function z(v) { $S(i+'Cur').left=parseInt((121-(v*121))-4)+'px'; }

		if(R1[i]) { z(rgb[j%3]/R1[i]); $(i+'Me').innerHTML=rgb[j%3]; }

		else if(R2[i]) { z(hsv[j%3]/R2[i]); $(i+'Me').innerHTML=Math.round(hsv[j%3]); }

	};

	if(flash) { with(flax) { for(var i in R) { var k=0, color=[], alpha=[], ratio=[]; var oX=415, oY=125+(j*44); pos(j++);

		for(var ii in R[i]) { if(!isNaN(R[i][ii][0])) { color[k]='0x'+func(R[i][ii][1]); alpha[k]=100; ratio[k]=R[i][ii][0]*255; k++; } }

		callFunction("beginGradientFill", "linear", color, alpha, ratio, {matrixType:"box", x:oX, y:oY, w:120, h:18, r:0});

		moveTo(oX, oY); lineTo(oX, 18+oY); lineTo(120+oX, 18+oY); lineTo(120+oX, oY);

	} } }

	else { with(S2D) { for(var i in R) { var oX=415, oY=125+(j*44); pos(j++);

		var g=createLinearGradient(oX,oY,120+oX,18+oY);

		for(var ii in R[i]){

			if(R[i][ii][0]!='_') g.addColorStop(R[i][ii][0],'#'+func(R[i][ii][1]));
		};

		beginPath(); moveTo(oX, oY); lineTo(oX, 18+oY); lineTo(120+oX, 18+oY); lineTo(120+oX, oY); fillStyle=g; fill();

} } } }

function Scheme(v,m,s) { var r=fu[opVal('fu')]; xy=v;

	if(!s) { var x=v[0]-W2, y=W-v[1]-W2, SV=Math.sqrt(Math.pow(x,2)+Math.pow(y,2)), hue=Math.atan2(x,y)/(Math.PI*2);

		hsv=[Math.round((hue*360)+(hue>0?0:360)), Math.round(SV<W3?(SV/W3)*100:100), Math.round(SV>=W3?Math.max(0,1-((SV-W3)/W3))*100:100)];

		rgb=hsv2rgb(hsv);

	} else hsv=s;

	var hyp=(hsv[1]+(100-hsv[2]))/100*W3;

	$SS(8).color=$SS(15).color=(hsv[2]<=80?'#FFF':'#000');

	$('degree').innerHTML=Math.round(hsv[0])+'&deg;';

	for(var i=0; i<=5; i++) {

		if(typeof(r[i])!='undefined') {

			var bb=!isNaN(r[i][1])?r[i][1]:0, cc=!isNaN(r[i][0])?r[i][0]:r[i];
			var h=func([hue=(hsv[0]+cc+360)%360, hsv[1]<50?hsv[1]+(bb/1.5):hsv[1]-(bb*1.5), hsv[2]<50?hsv[2]+(bb/3):hsv[2]-(bb/2)]);

			$T('span','hex'+i)[0].innerHTML=h; $S('hex'+i).background='#'+h;

			if(1||!isNaN(r[i])) { var rad=(hue/360)*(Math.PI*2);

				$S('curr'+i).left=Math.abs(Math.round(Math.sin(rad)*hyp)+W2)+'px';
				$S('curr'+i).top=Math.abs(Math.round(Math.cos(rad)*hyp)-W2)+'px';

	} } }

	if(m=='up') save();

	Run();

};

function iHTML(a,b,z) {

	if(!agent('msie')) { $T(a,b)[0].innerHTML=z; }

	else { var temp=document.createElement('span'); temp.innerHTML='<table>'+z;

		$(b).replaceChild(temp.firstChild.firstChild, $T(a,b)[0]);

	}
};

function Slide() { var o=['cpu','cpv','am','ayi'], z='', b;

	for(var i in o) { b=parseFloat(rBlind.custom[o[i]]); z+='<tr><td><div onmousedown="coreXY(\'slide'+i+'\',event,[93,0,0,0,-6],0,function(v,b) { var c=Math.min(Math.max((v[0]/(93/2))-1,-1),1); rBlind[\'custom\'][\''+o[i]+'\']=c; $(\'in'+i+'\').innerHTML=Math.round(c*100)/100; if(b==\'up\') Sphere(); else Scheme(0,0,hsv); });" class="slide"><div id="slide'+i+'" style="left: '+Math.round((b+1)*(93/2))+'px">'+"<\/div><\/div><\/td><td id=\"in"+i+"\">"+(Math.round(b*100)/100)+"<\/td><\/tr>"; }

	iHTML('tbody','custom',z);

};

function Rand() { var o=['cpu','cpv','am','ayi'], b;

	for(var i in o) { b=(Math.random()*2); rBlind.custom[o[i]]=b-1; $S('slide'+i).left=Math.round(b*(93/2))+'px'; $('in'+i).innerHTML=Math.round((b-1)*100)/100; }

	Sphere(); Scheme(0,0,hsv);

};

/* CUSTOMIZE */

var col={'black':['#000','#3a3a3a'],'white':['#FFF','#EEE']}, colMe='black', dot='dot';

function BlackWhite() {

	$SS(0).background=col[colMe][0];
	$SS(0).color=(colMe=='black'?'#444':'#BBB');
	$SS(1).color=(colMe=='black'?'#AAA':'#777');
	$SS(19).color=(colMe=='black'?'#555':'#AAA');
	$SS(22).borderTop='1px solid '+(colMe=='black'?'#333':'#DDD');;

	$('black').className=(colMe=='black'?'this':'');
	$('white').className=(colMe=='white'?'this':'');

	if(!agent('msie')) { var r=$T('img','hex'); for(var i=0; i<=5; i++) { r[i].src=(colMe=='black'?'media/transBlack.png':'media/transWhite.png'); } }

	BG(); Sphere();

}

function Click() { var r=fu[opVal('fu')];

	for(var i=0; i<=5; i++) {

		if(dot=='num' && !isNaN(r[i])) $('curr'+i).innerHTML=i;

		$S('curr'+i).display=isNaN(r[i])?'none':'block';
		$S('hex'+i).display=(typeof(r[i])=='undefined')?'none':'block';
		$('deg'+i).innerHTML=isNaN(r[i])?'':r[i]+"&deg;";

} }

function DotNum(v) { var rr=$T('div','curr'), j=0; dot=v; save();

	$('numbers').className=(v=='num')?'this':'';
	$('dots').className=(v=='dot')?'this':'';
	$SS(15).background=(v=='num')?'':'url(media/miniCurr.gif)';

	for(var i=0; i<=5; i++) rr[i].innerHTML=(v=='num'&&rr[i].style.display=='block')?++j:'';

}

/* MOUSE */

function coreXY(o,e,xy,z,fu) {

	function M(v,a,z) { return(Math.max(!isNaN(z)?z:0,!isNaN(a)?Math.min(a,v):v)); }
	function func(v) { if(fu) fu([Z[0]+oX,Z[1]+oY],v); }

	function commit(v,s) { if(!s) func('move');

		if(xy) v=[M(v[0],xy[0],xy[2]), M(v[1],xy[1],xy[3])]; // XY LIMIT

		if(!xy || xy[0]) d.left=v[0]+'px'; if(!xy || xy[1]) d.top=v[1]+'px';

	}

	if(stop) { stop=0; var d=$S(o), Z=XY(e,3); if(!z) zindex($(o));

		if(xy) { var ab=absPos($(o).parentNode); commit([Z[0]-ab['x']+zero(xy[4]),Z[1]-ab['y']+zero(xy[5])],1); } // XY CENTER

		var oX=zero(d.left)-Z[0], oY=zero(d.top)-Z[1]; if(xy) func('down');

		document.onselectstart=function(){ return false; }
		document.onmousemove=function(e){ if(!stop) { Z=XY(e,3); commit([Z[0]+oX,Z[1]+oY]); } };
		document.onmouseup=function(){ stop=1; func('up'); document.onmousemove=''; document.onselectstart=''; document.onmouseup=''; if(!xy) winCP(o,['block',d.left,d.top]); };

} }

/* MATH */

function toHex(v) { v=Math.round(Math.min(Math.max(0,v),255)); return("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16)); }
function hex2rgb(r) { return({0:parseInt(r.substr(0,2),16),1:parseInt(r.substr(2,2),16),2:parseInt(r.substr(4,2),16)}); }
function rgb2hex(r) { return(toHex(r[0])+toHex(r[1])+toHex(r[2])); }

function rgb2hsv(r) { // easyrgb.com/math.php?MATH=M20#text20

	var max=Math.max(r[0],r[1],r[2]),delta=max-Math.min(r[0],r[1],r[2]),H,S,V;

	if(max!=0) { S=Math.round(delta/max*100);

		if(r[0]==max) H=(r[1]-r[2])/delta; else if(r[1]==max) H=2+(r[2]-r[0])/delta; else if(r[2]==max) H=4+(r[0]-r[1])/delta;

		var H=Math.min(Math.round(H*60),360); if(H<0) H+=360;

	}

	return({0:H?H:0,1:S?S:0,2:Math.round((max/255)*100)});

}

function hsv2rgb(r) { // easyrgb.com/math.php?MATH=M21#text21

    var F,R,B,G,H=r[0]/360,S=r[1]/100,V=r[2]/100;

    if(S>0) { if(H>=1) H=0;

        H=6*H; F=H-Math.floor(H);
        A=Math.round(255*V*(1-S));
        B=Math.round(255*V*(1-(S*F)));
        C=Math.round(255*V*(1-(S*(1-F))));
        V=Math.round(255*V);

        switch(Math.floor(H)) {

            case 0: R=V; G=C; B=A; break;
            case 1: R=B; G=V; B=A; break;
            case 2: R=A; G=V; B=C; break;
            case 3: R=A; G=B; B=V; break;
            case 4: R=C; G=A; B=V; break;
            case 5: R=V; G=A; B=B; break;

        }

        return([R?R:0,G?G:0,B?B:0]);

    }
    else return([(V=Math.round(V*255)),V,V]);

}

function ryb2hsv(h) { if(!$('model').selectedIndex) return(h); else { var v;

	if(h[0]>=240 && h[0]<=360) v=(0.0022*Math.pow(h[0],2))-(0.2257*h[0])+155.18;
	else if(h[0]>=0 && h[0]<=60) v=(-0.0042*Math.pow(h[0],2))+(0.8386*h[0]);
	else if(h[0]>=60 && h[0]<=180) v=(0.0047*Math.pow(h[0],2))-(0.427*h[0])+45.166;
	else if(h[0]>=180 && h[0]<=240) v=(-0.0084*Math.pow(h[0],2))+(5.3235*h[0])-567.45;

	return([Math.round(v),h[1],h[2]]);

} }

function websafe(r) { var s=$('mode').selectedIndex;

	if(s) var n=(s==2?51:17), z=[Math.round(r[0]/n)*n,Math.round(r[1]/n)*n,Math.round(r[2]/n)*n];

	return(rgb2hex(z?z:r));

}

function rgba(r,v) { return('rgba('+r[0]+','+r[1]+','+r[2]+','+(v?v:(isNaN(r[3])?1:r[3]))+')'); }

function round(v) { return(Math.round(parseFloat(v)*100000)/100000); }

/* EXPORT */

function loc(v,b) { if(v.indexOf('LoadJack')!=-1) { b=v.indexOf('LoadJack'); return(v.substr(b+9)?v.substr(b+9).split(','):null); } }

function load(v,b) { if(v=loc(window.location.search)) { for(var i in v) { b=v[i].split(':');

	if(b[0] in {'fu':1,'blind':1,'mode':1,'model':1}) $(b[0]).selectedIndex=b[1];
	else if(b[0]=='maly') { $(b[0]).checked=b[1]; $('omylize').innerHTML=!b[1]?'anomylize':'denomylize'; }
	else if(b[0]=='hue') { hsv[0]=Math.round(zero(b[1])); }
	else if(b[0]=='sat') { hsv[1]=Math.round(zero(b[1])); }
	else if(b[0]=='val') { hsv[2]=Math.round(zero(b[1])); }
	else if(b[0]=='hex') { rgb=hex2rgb(b[1]); }
	else if(b[0]=='bg') { colMe=b[1]; }
	else if(b[0]=='dot') { DotNum(dot=b[1]); }
	else if(b[0]=='currMe') { xy=b[1].split('x'); xy[0]=xy[0]>1?xy[0]:round(xy[0]*W); xy[1]=xy[1]>1?xy[1]:round(xy[1]*W); }
	else if(b[0] in {'cpu':1,'cpv':1,'abu':1,'abv':1,'aeu':1,'aev':1}) { rBlind.custom[b[0]]=b[1]; }

} } }

function save() { var a='', b='', r=$T('span','hex');

	for(var i=0; i<=5; i++) { a+=r[i].innerHTML+','; } a=a.substr(0,a.length-1);
	for(var i in rBlind.custom) { b+=i+':'+round(rBlind.custom[i])+','; }

	$('illustrator').href='http://www.colorjack.com/studio/export.php?req=illustrator&q='+a;
	$('photoshop').href='http://www.colorjack.com/studio/export.php?req=photoshop&q='+a;
	$('eStudio').href='http://www.colorjack.com/studio/?LoadJack='+a;
	$('eUrl').href='http://www.colorjack.com/?swatch='+a;
	$('eSphere').href='?LoadJack=fu:'+$('fu').selectedIndex+',blind:'+$('blind').selectedIndex+',mode:'+$('mode').selectedIndex+',model:'+$('model').selectedIndex+',maly:'+($('maly').checked?1:0)+',hex:'+rgb2hex(rgb)+',bg:'+colMe+',dot:'+dot+($('blind').selectedIndex==9?','+b.substr(0,b.length-1):'');

}

/* WINDOWS */

var win={'custom':['none',321,110]};

function tog(v,s,r) { if($(v)) { var o=$S(v), w=win[v]; s=s&&isNaN(s)?s:(o.display=='block'?'none':'block');

	if(r && isNaN(r)) { o.left=r[0]+'px'; o.top=r[1]+'px'; } else if(w && o.display!=s) { winCP(v,[s,zero(o.left),r?win[v][2]:zero(o.top)]); }

	if(w) zindex($(v)); o.display=s;

} }

function winCP(o,r) { var z=''; if(r) win[o]=r;

	for(var i in win) { z+=i+','+win[i][0]+','+parseInt(win[i][1])+','+parseInt(win[i][2])+':'; }

    if(r) cookieStab('windows',z); else return(z);

}

function winMK(r) { for(var i in r=r.split(':')) { if(r[i]) { var v=r[i].split(',');

	win[v[0]]=[v[1],v[2],v[3]]; tog(v[0],v[1],[v[2],v[3]]);

} } }

/* MODE */

var oBlind, oMode, oModel, oFu;

function opVal(v) { return($(v).options[$(v).selectedIndex].text); }

function isCur() {

	if(oBlind!=opVal('blind') || oMode!=opVal('mode') || oModel!=opVal('model') || oFu!=opVal('fu')) {

		oBlind=opVal('blind'); oMode=opVal('mode'); oModel=opVal('model'); oFu=opVal('fu'); return(1);

} }

function onBlind() { if(isCur()) {

	if(opVal('blind')=='Customaly...') { b='block'; } else { $('maly').checked=false; b='none'; }

	winCP('custom',[b,parseInt($S('custom').left),parseInt($S('custom').top)]); $S('custom').display=b;

	Sphere();

} }

/* LOAD */

function feedMe() { var r;

	load(); BlackWhite(); Kontrol(); Slide();

	if(rgb) { hsv=rgb2hsv(rgb); Scheme(0,0,hsv); }

	else if(hsv) { rgb=hsv2rgb(hsv); Scheme(0,0,hsv); }

	else Scheme(xy); Click(); save();

    if((r=cookieGrab('windows')) && opVal('blind')=='Customaly...') winMK(r); else winMK(winCP());

	if(!agent('firefox')) $SS(20).top=0;

}

function BG() {

	if(flash) { with(flax) {

		var color=[0x000000, 0x3a3a3a, 0x000000], alpha=[100, 100, 100], ratio=[0, 133, 255];

		callFunction("beginGradientFill", "linear", color, alpha, ratio, {matrixType:"box", x:0, y:0, w:750, h:440, r:Math.PI*2*(90/360)});

		moveTo(0, 0); lineTo(750, 0); lineTo(750, 102); lineTo(400, 102); lineTo(400, 379); lineTo(750, 379); lineTo(750, 440); lineTo(0, 440);

	} }
	else { with(S2D) { var g=createLinearGradient(0,0,0,440);

		with(g) { addColorStop(0,col[colMe][0]); addColorStop(.52,col[colMe][1]); addColorStop(1,col[colMe][0]); }

		beginPath(); moveTo(0, 0); lineTo(750, 0); lineTo(750, 102); lineTo(400, 102); lineTo(400, 379); lineTo(750, 379); lineTo(750, 440); lineTo(0, 440);

		fillStyle=g; fill();

} } }

/* ONLOAD */

var flash=0;

function onResize() { $S('bot').top=(XYwin(0)-42)+'px'; $S('bot').display='block'; }

document.onfocus=onResize(); if(window.addEventListener) window.addEventListener("resize",onResize,false);

if(agent('msie')) { flash=1;

	var flax={}, aflax=new AFLAX("aflax.swf", false, true);

	aflax.insertFlash(750, 440, '#FFFFFF', function(){

		flax=new AFLAX.MovieClip(aflax);

		feedMe();

	});

	// clean up

	Click();

	$S('bot').display='none';

}
else { var S2D=$('sphere').getContext('2d');

	if(agent('firefox')) feedMe(); else window.onload=feedMe;

};

/* JUNK */

function TEST(v,s) { var o=$('T');

	if(typeof(v)=='object') { var z='';

		for(var i in v) { z+=i+':'+v[i]+', '; }

		v=z.substr(0,z.length-2);

	};

	if(!s) { o.innerHTML=v; }

	else { o.innerHTML+=v; }

};
