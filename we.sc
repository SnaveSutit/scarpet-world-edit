__command()->(
	return('Scarpet World Edit by andrew_10 and SnaveSutit')
);

//Globals

global_playerData=m();
global_maxHistoryLength=20;
global_wand='wooden_axe';
global_brush='wooden_shovel';
global_brush_step=0.025;
global_brush_distance=100;
global_maxSize=1000000;

//Events

__on_player_clicks_block(player, block, face)->(
	if(_checkGamemode()&&get(query(player,'holds','mainhand'),0)==global_wand,
		pos=pos(block);
		schedule(0,'_scheduledPlace',block(pos));
		without_updates(set(pos,'air'));
		message=_setPos(0,pos);
		if(message,
			print(message)
		);
	)
);

_scheduledPlace(block)->(
	without_updates(set(pos(block),block));
);

__on_player_right_clicks_block(player,item,hand,block,face,hitvec)->(
	if(_checkGamemode()&&item:0==global_wand&&hand=='mainhand',
		message=_setPos(1,pos(block));
		if(message,
			print(message)
		)
	)
);

__on_player_uses_item(player,item,hand)->(
    if(hand!='mainhand'||!_checkGamemode(),return());
    block=block(query(player,'look')*5+pos(player));

    if(item:0==global_wand,
        message=_setPos(1,pos(block));
        if(message,
        	print(message)
        )
    )
	if(item:0==global_brush,
		if(!_checkPallet(),
			print(_getErrorPallet());
			return()
		);
		pallet=_getPlayerData('pallet');
		pos=_getLookingPos();
		if(pos==null,
			print('Selected block too far away');
			return()
		);
		radius=_getPlayerData('brush_size');
		replace=_getPlayerData('brush_replace');
		cx=get(pos,0);
		cy=get(pos,1);
		cz=get(pos,2);
		size=length(pallet);
		st=-radius+1;
		x=st;
		while(x<radius,1000,
			y=st;
			while(y<radius,1000,
				z=st;
				while(z<radius,1000,
					pos=l(cx+x,cy+y,cz+z);
					if(!(replace=='all'||replace==block(pos)),
						z+=1;
						continue()
					);
					distSqr=x*x+y*y+z*z;
					if(distSqr<radius*radius,
						_setBlock(pos,_randomFrom(pallet,size))
					);
					z+=1
				);
				y+=1
			);
			x+=1
		);
		_saveHistory()
	)
);

__on_tick()->(
    for(players('*'),
        if(!_checkGamemode(),return());
		positions=_getPlayerData('positions');
		l(x1,y1,z1)=positions:0;
		l(x2,y2,z2)=positions:1;
		particle_rect('dust 1 0 0 0.5', x1,y1,z1,x1+1,y1+1,z1+1,1);
		particle_rect('dust 0 1 0 0.5', x2,y2,z2,x2+1,y2+1,z2+1,1);
		if(_checkPositions()&&_getPlayerData('show_selection'),
		    l(xmin,ymin,zmin)=l(min(x1,x2),min(y1,y2),min(z1,z2));
		    l(xmax,ymax,zmax)=l(max(x1,x2),max(y1,y2),max(z1,z2));
			particle_rect('dust 0 0 1 1', xmin,ymin,zmin,xmax+1,ymax+1,zmax+1,4)
		)
	)
);
//'
//Lib

_getS(count)->(
	if(count>1,
		return('s')
	);
	return('')
);

_getAngle2d(x,z)->(
	d=sqrt(x*x+z*z);
	if(d==0,
		return(0)
	);
	if(asin(z/d)<0,
		return(360-acos(x/d))
	);
	return(acos(x/d))
);

_randomFrom(list,size)->(
	return(get(list,floor(rand(size))))
);

_createPlayerData()->(
	put(global_playerData,player(),m(
	    l('positions',l(null,null)),
	    l('pallet',null),
	    l('history',l(l())),
	    l('brush_size',1),
	    l('brush_replace','all'),
	    l('clipboard',m(l('pos',null),l('blocks',l()))),
	    l('show_selection',0)
	));
);

_getPlayerData(fieldName)->(
	if(!has(global_playerData,player()),
		_createPlayerData()
	);
	return(get(get(global_playerData,player()),fieldName))
);

_setPlayerData(fieldName,value)->(
	if(!has(global_playerData,player()),
		_createPlayerData()
	);
	put(get(global_playerData,player()),fieldName,value)
);

_checkPositions()->(
	return(get(_getPlayerData('positions'),0)!=null&&get(_getPlayerData('positions'),1)!=null)
);

_getErrorPositions()->(
	return('Positions not set')
);

_checkSize()->(
	return(_getVolume()<=global_maxSize)
);

_getErrorSize()->(
	return('Selected area is too big')
);

_checkPallet()->(
	return(_getPlayerData('pallet')!=null)
);

_getErrorPallet()->(
	return('Pallet not set')
);

_checkClipboard()->(
	return(_getPlayerData('clipboard'):'pos'!=null)
);

_getErrorClipboard()->(
	return('Clipboard is empty')
);

_checkGamemode()->(
	return(query(player(),'gamemode')=='creative')
);

_getErrorGamemode()->(
	return('You must be in creative mode to use World Edit')
);

_setBlock(pos,block)->(
	put(get(_getPlayerData('history'),0),null,block(pos));
	without_updates(set(pos,block));
);

_setBlockProperties(pos,block,properties)->(
	put(get(_getPlayerData('history'),0),null,block(pos));
	keys=keys(properties);
	values=values(properties);
	if(length(properties)==1,
		without_updates(set(pos,block,keys:0,values:0));
		return();
	);
	if(length(properties)==2,
		without_updates(set(pos,block,keys:0,values:0,keys:1,values:1));
		return();
	);
	if(length(properties)==3,
		without_updates(set(pos,block,keys:0,values:0,keys:1,values:1,keys:2,values:2));
		return();
	);
	if(length(properties)==4,
		without_updates(set(pos,block,keys:0,values:0,keys:1,values:1,keys:2,values:2,keys:3,values:3));
		return();
	);
	if(length(properties)==5,
		without_updates(set(pos,block,keys:0,values:0,keys:1,values:1,keys:2,values:2,keys:3,values:3,keys:4,values:4));
		return();
	);
	if(length(properties)==6,
		without_updates(set(pos,block,keys:0,values:0,keys:1,values:1,keys:2,values:2,keys:3,values:3,keys:4,values:4,keys:5,values:5));
		return();
	);
	if(length(properties)==7,
		without_updates(set(pos,block,keys:0,values:0,keys:1,values:1,keys:2,values:2,keys:3,values:3,keys:4,values:4,keys:5,values:5,keys:6,values:6));
		return();
	)
);

_saveHistory()->(
	put(_getPlayerData('history'),1,get(_getPlayerData('history'),0),'insert');
	if(length(_getPlayerData('history'))-1>global_maxHistoryLength,
		delete(_getPlayerData('history'),global_maxHistoryLength+1)
	);
	put(_getPlayerData('history'),0,l())
);

_getVolume()->(
	positions=_getPlayerData('positions');
	return((abs(positions:0:0-positions:1:0)+1)*(abs(positions:0:1-positions:1:1)+1)*(abs(positions:0:2-positions:1:2)+1))
);

_setPos(id,pos)->(
	if(pos!=get(_getPlayerData('positions'),id),
		oldpos=_getPlayerData('positions'):id;
		put(_getPlayerData('positions'),id,pos);
		volume='';
		if(_checkPositions(),
			if(!_checkSize(),
				put(_getPlayerData('positions'),id,oldpos);
				return(_getErrorSize())
			);
			volume=' ['+_getVolume()+']'
		);
		return('Set position '+(id+1)+' to '+pos+volume)
	)
);

_getLookingPos()->(
	l(x,y,z)=query(player(),'trace',global_brush_distance/global_brush_step,'blocks');
    return(l(round(x),round(y),round(z)))
);

_getDirectionFromAngle(angle)->(
	angle=round(angle/90)*90;
	while(angle>=360,1000,
		angle=angle-360
	);
	while(angle<0,1000,
		angle+=360
	);
	if(angle==0,
		return('east')
	);
	if(angle==90,
		return('south')
	);
	if(angle==180,
		return('west')
	);
	if(angle==270,
		return('north')
	)
);

_getDirectionAngle(direction)->(
	if(direction=='east',
		return(0)
	);
	if(direction=='south',
		return(90)
	);
	if(direction=='west',
		return(180)
	);
	if(direction=='north',
		return(270)
	);
	return(null)
);

_rotateDirection(direction,angle)->(
	rotated=_getDirectionFromAngle(_getDirectionAngle(direction)+angle);
	if(rotated==null,
		return(direction)
	);
	return(rotated)
);

_rotateAxis(axis,angle)->(
	if(axis=='y',
		return('y')
	);
	if((abs(round(angle/90)))%2,
		if(axis=='x',
			return('z')
		);
		return('x')
	);
	return(axis)
);

_flipType(type)->(
	if(type=='top',
		return('bottom')
	);
	if(type=='bottom',
		return('top')
	);
	return(type)
);

_flipFace(face)->(
	if(face=='ceiling',
		return('floor')
	);
	if(face=='floor',
		return('ceiling')
	);
	return(face)
);

_flipFacing(facing)->(
	if(facing=='up',
		return('down')
	);
	if(facing=='down',
		return('up')
	);
	return(facing)
);

//Commands

help()->(
	print(
		'/we help - Displays this message.'+'\n'+
		'/we wand - Gives you Wand tool. Left click a block to set first position and right click to set second position.'+'\n'+
		'/we pos1 - Sets first position to your current position.'+'\n'+
		'/we pos2 - Sets second position to your current position.'+'\n'+
		'/we show_selection - Toggles the selection box. By default turned off. Causes lag when selecting big areas.'+'\n'+
		'/we brush - Gives you Brush tool. Right click to place a sphere at the block you are looking at. Needs a pallet of at least 1 block.'+'\n'+
		'/we brush_size <radius> - Sets radius of the Brush tool. Does not need to be a full number.'+'\n'+
		'/we brush_replace <block> - Makes brush only replace <block>. Use <all> to replace all blocks. <all> is the default value.'+'\n'+
		'/we set_pallet - Sets your pallet to blocks in selected area. Pallet is used by Brush tool and commands like /we random_fill.'+'\n'+
		'/we fill <block> - Fills selected area with <block>.'+'\n'+
		'/we fill_replace <block1> <block2> - Replaces all <block1> in selected area with <block2>.'+'\n'+
		'/we random_fill - Fills selected area with random blocks from your pallet.'+'\n'+
		'/we random_fill_replace <blocks> - Replaces all <block> in selected area with random blocks from your pallet.'+'\n'+
		'/we clone - Copies all blocks in selected area to your clipboard relative to your position.'+'\n'+
		'/we rotate <angle> - Rotates copied blocks clockwise by <angle> degrees relative to your copying position. Works good only with angles 90/-90/180.'+'\n'+
		'/we flip - Flips copied blocks vertically relative to your copying position.'+'\n'+
		'/we paste - Pastes copied blocks relative to your position.'+'\n'+
		'/we paste_replace <block> - Pastes copied blocks relative to your position but only replaces <block>.'+'\n'+
		'/we move <x> <y> <z> - Moves blocks in selected area by <x> <y> <z> blocks in each direction. e.g. To move something 1 block up use /we move 0 1 0.'+'\n'+
		'/we stack <x> <y> <z> - Stacks blocks in selected area <x> <y> <z> times in each direction. e.g. To stack something 1 time upwards use /we stack 0 1 0.'+'\n'+
		'/we undo - Cancles your previous action. Only actions that change blocks in-world are saved. By default only last 20 actions are saved.'+'\n'+
		'/we clear_positions - Clears your selected positions'+'\n'+
		'/we clear_pallet - Clears your pallet'+'\n'+
		'/we clear_clipboard - Clears your clipboard'+'\n'+
		'/we clear_history - Clears your action history'
	);
	return('');
);

wand()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	run('give '+player()+' '+global_wand+'{display:{Name:\'{"text":"Wand","italic":"false","color":"gold"}\'}}')
);

pos1()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	return(_setPos(0,pos(block(pos(player())))))
);

pos2()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	return(_setPos(1,pos(block(pos(player())))))
);

show_selection()->(
	state=_getPlayerData('show_selection');
	_setPlayerData('show_selection',!state);
	if(state,
		return('Selection box will now not be showed')
	);
	return('Selection box will now be showed')
);

brush()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	run('give '+player()+' '+global_brush+'{display:{Name:\'{"text":"Brush","italic":"false","color":"gold"}\'}}')
);

brush_size(size)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	_setPlayerData('brush_size',size);
	return('Set brush size to '+size)
);

brush_replace(block)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	_setPlayerData('brush_replace',block);
	return('Brush will now replace '+block)
);

set_pallet()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	blocks=l();
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				blocks+=block(x,y,z);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	_setPlayerData('pallet',blocks);
	return('Pallet set')
);

fill(block)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				_setBlock(l(x,y,z),block);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	_saveHistory();
	filled=((xmax-xmin+1)*(ymax-ymin+1)*(zmax-zmin+1));
	return('Filled '+filled+' block'+_getS(filled))
);

fill_replace(block1,block2)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	replaced=0;
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				if(block(x,y,z)==block(block1),
					_setBlock(l(x,y,z),block2);
					replaced+=1;
				);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	_saveHistory();
	return('Replaced '+replaced+' block'+_getS(replaced))
);

random_fill()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	if(!_checkPallet(),
		return(_getErrorPallet())
	);
	pallet=_getPlayerData('pallet');
	size=length(pallet);
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				_setBlock(l(x,y,z),_randomFrom(pallet,size));
				z+=1
			);
			y+=1
		);
		x+=1
	);
	_saveHistory();
	filled=((xmax-xmin+1)*(ymax-ymin+1)*(zmax-zmin+1));
	return('Filled '+filled+' block'+_getS(filled))
);

random_fill_replace(block)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	if(!_checkPallet(),
		return(_getErrorPallet())
	);
	pallet=_getPlayerData('pallet');
	size=length(pallet);
	replaced=0;
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				if(block(x,y,z)==block(block),
					_setBlock(l(x,y,z),_randomFrom(pallet,size));
					replaced+=1
				);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	_saveHistory();
	return('Replaced '+replaced+' block'+getS(replaced))
);

clone()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	clear_clipboard();
	pos=pos(block(pos(player())));
	pos:0+=0.5;
	pos:2+=0.5;
	put(_getPlayerData('clipboard'),'pos',pos);
	blocks=l();
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				properties=m();
				for(block_properties(x,y,z),
					put(properties,_,property(x,y,z,_))
				);
				blocks+=l(l(x+0.5,y,z+0.5),block(x,y,z),properties);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	put(_getPlayerData('clipboard'),'blocks',blocks);
	copied=length(blocks);
	return('Copied '+copied+' block'+_getS(copied))
);

rotate(angle)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkClipboard(),
		return(_getErrorClipboard())
	);
	center=_getPlayerData('clipboard'):'pos';
	for(_getPlayerData('clipboard'):'blocks',
		pos=_:0;
		x=pos:0;
		z=pos:2;
		dx=x-center:0;
		dz=z-center:2;
		nangle=angle+_getAngle2d(dx,dz);
		d=sqrt(dx*dx+dz*dz);
		x=d*cos(nangle)+center:0;
		z=d*sin(nangle)+center:2;
		put(_,0,l(x,pos:1,z));
		
		facing=_:2:'facing';
		if(facing!=null,
			put(get(_,2),'facing',_rotateDirection(facing,angle))
		);
		axis=_:2:'axis';
		if(axis!=null,
			put(get(_,2),'axis',_rotateAxis(axis,angle))
		);
		east=_:2:'east';
		west=_:2:'west';
		south=_:2:'south';
		north=_:2:'north';
		if(east!=null,
			put(get(_,2),_rotateDirection('east',angle),east);
			put(get(_,2),_rotateDirection('west',angle),west);
			put(get(_,2),_rotateDirection('south',angle),south);
			put(get(_,2),_rotateDirection('north',angle),north)
		)
	);
	return('Rotated copied blocks by '+angle+' degrees')
);

flip()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkClipboard(),
		return(_getErrorClipboard())
	);
	for(_getPlayerData('clipboard'):'blocks',
		pos=_:0;
		half=_:2:'half';
		if(half!=null,
			put(get(_,2),'half',_flipType(half))
		);
		type=_:2:'type';
		if(type!=null,
			put(get(_,2),'type',_flipType(type))
		);
		face=_:2:'face';
		if(face!=null,
			put(get(_,2),'face',_flipFace(face))
		);
		facing=_:2:'facing';
		if(facing!=null,
			put(get(_,2),'facing',_flipFacing(facing))
		);
		put(_,0,l(pos:0,2*(_getPlayerData('clipboard'):'pos':1)-pos:1,pos:2))
	);
	blocks=length(_getPlayerData('clipboard'):'blocks');
	return('Flipped '+blocks+' block'+_getS(blocks))
);

paste()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkClipboard(),
		return(_getErrorClipboard())
	);
	pos1=_getPlayerData('clipboard'):'pos';
	pos2=pos(block(pos(player())));
	dx=pos2:0-pos1:0+0.5;
	dy=pos2:1-pos1:1;
	dz=pos2:2-pos1:2+0.5;
	for(_getPlayerData('clipboard'):'blocks',
		pos=_:0;
		npos=l(pos:0+dx,pos:1+dy,pos:2+dz);
		properties=_:2;
		if(properties!=m(),
			_setBlockProperties(npos,_:1,properties)
		);
		if(properties==m(),
			_setBlock(npos,_:1)
		)
	);
	_saveHistory();
	pasted=length(_getPlayerData('clipboard'):'blocks');
	return('Pasted '+pasted+' block'+_getS(pasted))
);

paste_replace(block)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkClipboard(),
		return(_getErrorClipboard())
	);
	pos1=_getPlayerData('clipboard'):'pos';
	pos2=pos(block(pos(player())));
	dx=pos2:0-pos1:0;
	dy=pos2:1-pos1:1;
	dz=pos2:2-pos1:2;
	pasted=0;
	for(_getPlayerData('clipboard'):'blocks',
		pos=_:0;
		npos=l(pos:0+dx,pos:1+dy,pos:2+dz);
		if(block(npos)==block,
			properties=_:2;
			if(properties!=m(),
				_setBlockProperties(npos,_:1,properties)
			);
			if(properties==m(),
				_setBlock(npos,_:1)
			);
			pasted+=1;
		)
	);
	_saveHistory();
	return('Pasted '+pasted+' block'+_getS(pasted))
);

move(dx,dy,dz)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	blocks=l();
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	nxmin=xmin+dx;
	nymin=ymin+dy;
	nzmin=zmin+dz;
	nxmax=xmax+dx;
	nymax=ymax+dy;
	nzmax=zmax+dz;
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				put(blocks,null,block(x,y,z));
				if(!(x>=nxmin&&x<=nxmax&&y>=nymin&&y<=nymax&&z>=nzmin&&z<=nzmax),
					_setBlock(l(x,y,z),'air')
				);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	for(blocks,
		pos=pos(_);
		x=pos:0+dx;
		y=pos:1+dy;
		z=pos:2+dz;
		_setBlock(l(x,y,z),_)
	);
	_saveHistory();
	moved=((xmax-xmin+1)*(ymax-ymin+1)*(zmax-zmin+1));
	return('Moved '+moved+' block'+_getS(moved))
);

stack(stx,sty,stz)->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	if(!_checkPositions(),
		return(_getErrorPositions())
	);
	positions=_getPlayerData('positions');
	x1=positions:0:0;
	y1=positions:0:1;
	z1=positions:0:2;
	x2=positions:1:0;
	y2=positions:1:1;
	z2=positions:1:2;
	xmin=min(x1,x2);
	ymin=min(y1,y2);
	zmin=min(z1,z2);
	xmax=max(x1,x2);
	ymax=max(y1,y2);
	zmax=max(z1,z2);
	xsize=xmax-xmin+1;
	ysize=ymax-ymin+1;
	zsize=zmax-zmin+1;
	if(stx<0,
		stx=stx*-1;
		xsize=xsize*-1
	);
	if(sty<0,
		sty=sty*-1;
		ysize=ysize*-1
	);
	if(stz<0,
		stz=stz*-1;
		zsize=zsize*-1
	);
	x=xmin;
	while(x<=xmax,1000,
		y=ymin;
		while(y<=ymax,1000,
			z=zmin;
			while(z<=zmax,1000,
				block=block(x,y,z);
				dx=0;
				loop(stx+1,
					dy=0;
					loop(sty+1,
						dz=0;
						loop(stz+1,
							if(dx!=0||dy!=0||dz!=0,
								_setBlock(l(x+dx,y+dy,z+dz),block)
							);
							dz+=zsize
						);
						dy+=ysize
					);
					dx+=xsize
				);
				z+=1
			);
			y+=1
		);
		x+=1
	);
	_saveHistory();
	times=((stx+1)*(sty+1)*(stz+1)-1);
	stacked=((xmax-xmin+1)*(ymax-ymin+1)*(zmax-zmin+1));
	return('Stacked '+stacked+' block'+_getS(stacked)+' '+times+' time'+_getS(times))
);

undo()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	history=_getPlayerData('history');
	if(length(history)==1,
		return('Nothing to undo')
	);
	blocks=get(history,1);
	size=length(blocks);
	i=size-1;
	loop(size,
		without_updates(set(pos(blocks:i),blocks:i));
		i=i-1
	);
	delete(_getPlayerData('history'),1);
	return('Undid '+size+' block change'+_getS(size))
);

clear_history()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	_setPlayerData('history',l(l()));
	return('Cleared history')
);

clear_positions()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	_setPlayerData('positions',l(null,null));
	return('Cleared positions')
);

clear_pallet()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	_setPlayerData('pallet',null);
	return('Cleared pallet')
);

clear_clipboard()->(
	if(!_checkGamemode(),
		return(_getErrorGamemode())
	);
	_setPlayerData('clipboard',m(l('pos',null),l('blocks',l())));
	return('Cleared clipboard')
)