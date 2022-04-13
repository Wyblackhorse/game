// 聊天页面的JS方法集合(101和103)

//typeStr 总类型值为 setKinds 时通过websock获取记录接收的操作
//typeStr 总类型值为 kinds 时加载历史记录
// 引入公共类
import {formatDateTime} from '../common/publicTool.js'

/*
	机器人方法集合
*/

// 001机器押注 就是1
// if(acceptObj.setKinds == 1){	
// {"action":101,"indexId":"1640338050787","content":{"back_message":"277 大双 ","back_array":[277,"大双"]},"time":1640338050,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":1,"fd":null,"robot":{"username":"赌鬼","head_images":"-1","level":1},"chat_switch":null,"username":null,"date_time":"2021-12-24 17:27:30"}	
export const robotBetFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	
	if(typeStr == 'setKinds'){
		// console.log("acceptObj",acceptObj);
		let usernameS= acceptObj.robot.username;
		let date_time_str = acceptObj.date_time
		let levelS= acceptObj.robot.level; 				
		// var headImgS= '../../../static/images/tx/tx'+acceptObj.robot.head_images+".png";  				
		let headImgS= '../../static/images/icon_hy_xj_default_head.png'; 	
		
		let regImg = /^([1-9]\d|\d)$/
		if (regImg.test(parseInt(acceptObj.robot.head_images))) {
			// console.log("acceptObj.robot.head_images",acceptObj.robot.head_images);
			headImgS= '../../static/images/tx/tx'+acceptObj.robot.head_images+".png"; 
					
		}else{
			// console.log("不存在头像,-1");
			headImgS= '../../static/images/icon_hy_xj_default_head.png'; 
		}
		
		let strb = []
		let stra=[]
		let total = []
		let totalStr = ""
		let totalNum = 0 
		let currBetTotal =0
		let tempbetJson = acceptObj.content
		let tempbetArr = tempbetJson.back_array
		let tempper = tempbetJson.periods_num
		
		let tempLvNum = tempbetJson.consumption_level_id
				
		for(let i=0;i<tempbetArr.length;i++) {
		  if(i%2==0) {//奇数
			strb.push(tempbetArr[i])
			
		  }else{//偶数
			stra.push(tempbetArr[i])
									
									
		  }
		}
		
					
		if(strb.length == stra.length){
			
			strb.forEach((item,index)=>{
				
				totalNum = totalNum+parseInt(item)
				// currBetTotal  = currBetTotal + totalNum
				total.push({name:stra[index],num:strb[index]})
				totalStr = totalStr + strb[index] +" "+stra[index] + " "
				// this.allTotalNum.push({name:stra[index],num:strb[index]})
			})					
		}
		
		let resNomal = {name:usernameS,content:tempbetJson.back_message,level:levelS,id:'1111',type:2,username:usernameS,headImg:headImgS,timeStr:date_time_str,chatload_id:Date.parse(new Date())+acceptObj.indexId}
		
		let resRobot = {name:'机器人',content:totalStr,level:'00',id:'7777',contactname:'@玩家('+thisCurrUserID+')',totalNum:totalNum,currpernum:tempper,username:usernameS,splitMoney:tempbetJson.money.toString().split('.')[0],timeStr:date_time_str,chatload_id:Date.parse(new Date())+acceptObj.indexId+totalStr+tempper}
		
		
		return {resNomal,resRobot}
		
		// return {name:usernameS,content:tempbetJson.back_message,level:levelS,id:'1111',type:2,username:usernameS,headImg:headImgS,timeStr:date_time_str,chatload_id:acceptObj.indexId}
		
	}else{
		
		let item = acceptObj
		let acceptStr = item.content;
		let tempDateTime = handleTimeStr(item.created_at)
		// console.log("acceptStr",typeof(acceptStr),acceptStr);
		
		let acceptObj_a = JSON.parse(acceptStr);
		
		
		let usernameS= acceptObj_a.username; 		
		let tempImage = acceptObj_a.head_images
		// var headImgS= '../../../static/images/tx/tx'+acceptObj.head_images+".png";
		// console.log("tempImage",tempImage);
		let headImgS= '../../static/images/icon_hy_xj_default_head.png';
		
		let regImg = /^([1-9]\d|\d)$/
		if (regImg.test(parseInt(tempImage))) {
			// console.log("acceptObj.robot.head_images",acceptObj.robot.head_images);
			headImgS= '../../static/images/tx/tx'+tempImage+".png"; 
		
		}else{
			// console.log("不存在头像,-1");
			headImgS= '../../static/images/icon_hy_xj_default_head.png'; 
		}
		
		let levelS = acceptObj_a.level; 
		
		let strb = []
		let stra=[]
		let total = []
		let totalStr = ""
		let totalNum = 0 
		let currBetTotal =0
		let tempbetJson = acceptObj_a.content

		let tempbetArr = tempbetJson.back_array

				
		for(let i=0;i<tempbetArr.length;i++) {
		  if(i%2==0) {//奇数
			strb.push(tempbetArr[i])
			
		  }else{//偶数
			stra.push(tempbetArr[i])							
		  }
		}
						
		if(strb.length == stra.length){
			
			strb.forEach((item,index)=>{
				
				totalNum = totalNum+parseInt(item)
				// currBetTotal  = currBetTotal + totalNum
				total.push({name:stra[index],num:strb[index]})
				totalStr = totalStr + strb[index] +" "+stra[index] + " "
				// this.allTotalNum.push({name:stra[index],num:strb[index]})
			})					
		}
		

		let resNomal = {name:usernameS,content:tempbetJson.back_message,level:levelS,id:'1111',type:2,username:usernameS,headImg:headImgS,chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
		
		let resRobot = {name:'机器人',content:totalStr,level:'00',id:'7777',totalNum:totalNum,username:usernameS,splitMoney:tempbetJson.money.toString().split('.')[0],timeStr:tempDateTime}
		
		return {resNomal,resRobot}
	}
	
	
}


// if(acceptObj.setKinds == 1399){//1399机器人下分消息
// {"action":101,"indexId":"1641453390499","content":{"username":"阿布","money":"下465","headImage":"5"},"time":1641453390,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":1399,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-06 15:16:30"}
export const robotWithDrawFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let usernameS= acceptObj.content.username;
		let date_time_str = acceptObj.date_time
		let levelS= '1111'; 				
		// var headImgS= '../../../static/images/tx/tx'+acceptObj.robot.head_images+".png";  				
		let headImgS= '../../static/images/icon_hy_xj_default_head.png'; 	
		
		let regImg = /^([1-9]\d|\d)$/
		if (regImg.test(parseInt(acceptObj.content.head_images))) {
			// console.log("acceptObj.robot.head_images",acceptObj.robot.head_images);
			headImgS= '../../static/images/tx/tx'+acceptObj.content.head_images+".png"; 
				
		}else{
			// console.log("不存在头像,-1");
			headImgS= '../../static/images/icon_hy_xj_default_head.png'; 
		}
		
		
		return {name:usernameS,content:acceptObj.content.money,level:levelS,id:'1111',type:2,username:usernameS,headImg:headImgS,timeStr:date_time_str}
	}else{
		
		let item = acceptObj
		// console.log("item",item);
		let acceptObj_content = item.content
		// console.log("acceptObj_content",typeof acceptObj_content);
		let tempDateTime = handleTimeStr(item.created_at)
		let acceptObj_contentOBJ = JSON.parse(acceptObj_content)
		// console.log("acceptObj_content",acceptObj_contentOBJ);
				
		let tempUsername = acceptObj_contentOBJ.username
		
		let tempImage = acceptObj_contentOBJ.head_image
		
		// let tempLvNum = acceptObj_contentOBJ.consumption_level_id
		
		if(tempImage == null || tempImage == ''){
			
			tempImage = thisOtherPerImg
		}
								
		return {name:tempUsername,content:acceptObj_contentOBJ.money,level:0,id:'1111',type:2,username:tempUsername,headImg:tempImage,timeStr:tempDateTime}
		
	}
	
}


// if(acceptObj.setKinds == 87){// 机器人下分后返回的消息
// {"action":101,"indexId":"1641819419588","content":{"username":"赌鬼","money":1000,"headImage":"-1","change_money":4848.16},"time":1641819419,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":87,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-10 20:56:59"}
export const robotSearchFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {
	if(typeStr == 'setKinds'){
		let detaillistobjs =  acceptObj.content
		let date_time_str = acceptObj.date_time
		let contentValStr = {}
		// console.log("parseInt(detaillistobjs.withdraw)",parseInt(detaillistobjs.withdraw));
		contentValStr.withdraw = (detaillistobjs.withdraw) ? parseInt(detaillistobjs.withdraw) : ''
		// let top_up_num = 0
		// let change_money_num = 0
		// if(parseInt(detaillistobjs.top_up)){
		// 	top_up_num = parseInt(detaillistobjs.top_up)
		// }else{
		// 	top_up_num = 0
		// }
		
		// if(parseInt(detaillistobjs.change_money)){
		// 	change_money_num = parseInt(detaillistobjs.change_money)
		// }else{
		// 	change_money_num = 0
		// }
		
		contentValStr.top_up = (detaillistobjs.top_up) ? parseInt(detaillistobjs.top_up) : ''
		contentValStr.change_money = (detaillistobjs.change_money) ? parseInt(detaillistobjs.change_money):''
		contentValStr.money = (detaillistobjs.money) ? parseInt(detaillistobjs.money) :''
		contentValStr.nickname = detaillistobjs.username
						
		return {name:'机器人',content:contentValStr,id:'89',chatload_id:Date.parse(new Date())+acceptObj.indexId,timeStr:date_time_str}
	}else{
		let item = acceptObj
		let acceptObj_content = item.content
		let tempDateTime = handleTimeStr(item.created_at)
		let detaillistobjs = JSON.parse(acceptObj_content)			
		let contentValStr = {}
		// console.log("parseInt(detaillistobjs.withdraw)",parseInt(detaillistobjs.withdraw));
		contentValStr.withdraw = (detaillistobjs.withdraw) ? parseInt(detaillistobjs.withdraw) : ''
		// let top_up_num = 0
		// let change_money_num = 0
		// if(parseInt(detaillistobjs.top_up)){
		// 	top_up_num = parseInt(detaillistobjs.top_up)
		// }else{
		// 	top_up_num = 0
		// }
		
		// if(parseInt(detaillistobjs.change_money)){
		// 	change_money_num = parseInt(detaillistobjs.change_money)
		// }else{
		// 	change_money_num = 0
		// }
		
		contentValStr.top_up = (detaillistobjs.top_up) ? parseInt(detaillistobjs.top_up) : ''
		contentValStr.change_money = (detaillistobjs.change_money) ? parseInt(detaillistobjs.change_money):''
		contentValStr.money = (detaillistobjs.money) ? parseInt(detaillistobjs.money) :''
		contentValStr.nickname = detaillistobjs.username
		return {name:'机器人',content:contentValStr,id:'89',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
	}
}

// if(acceptObj.setKinds == 1499){// 机器人指令2返回的结果
// {"action":101,"indexId":"1642242915167","content":{"nickname":"我是你爸爸","headImage":"-1","running":"61139.00","already_running_back":0,"money":5142},"time":1642242915,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":1499,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-15 18:35:15"}
export const robotCommandTwoFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {	
	// console.log("测试acceptObj",acceptObj);
	const currRateNumber = parseFloat(uni.getStorageSync('currRateNum'))
	
	if(typeStr == 'setKinds'){
		let detaillistobjs =  acceptObj.content
		// console.log("测试detaillistobjs",typeof detaillistobjs);
		let date_time_str = acceptObj.date_time
		// console.log("detaillistobjs",detaillistobjs,typeof detaillistobjs);
		let contentValStr = {}
		contentValStr.running = parseInt(detaillistobjs.running)
		contentValStr.already_running_back = parseInt(detaillistobjs.already_running_back)
		contentValStr.money = parseInt(detaillistobjs.money)
		contentValStr.currflows = parseInt(parseInt(detaillistobjs.running) * currRateNumber)
		contentValStr.runBackNum =  parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)
		contentValStr.remainMoney = (parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)) + parseInt(detaillistobjs.money)
		
		
		return {name:'机器人',content:contentValStr,id:'2203',chatload_id:Date.parse(new Date())+acceptObj.indexId,nickname:detaillistobjs.nickname,timeStr:date_time_str}
	}else{
		let item = acceptObj
		let currContent =  item.content
	
		let tempDateTime = handleTimeStr(item.created_at)
		let detaillistobjs = JSON.parse(currContent);
		
		// console.log("detaillistobjs",detaillistobjs,typeof detaillistobjs);
		let contentValStr = {}
		contentValStr.running = parseInt(detaillistobjs.running)
		contentValStr.already_running_back = parseInt(detaillistobjs.already_running_back)
		contentValStr.money = parseInt(detaillistobjs.money)
		contentValStr.currflows = parseInt(parseInt(detaillistobjs.running) * currRateNumber)
		contentValStr.runBackNum =  parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)
		contentValStr.remainMoney = (parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)) + parseInt(detaillistobjs.money)
		
		return {name:'机器人',content:contentValStr,id:'2203',chatload_id:Date.parse(new Date())+item.id,nickname:detaillistobjs.nickname,timeStr:tempDateTime}
			
	}
	
	
}


// {"action":101,"indexId":"1641453389970","content":{"username":"我是你爸爸","money":"查1000","headImage":"-1"},"time":1641453389,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":1398,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-06 15:16:29"}









/*
	用户方法集合
*/

// if(acceptObj.setKinds == 107){//取消下注(所有),面对所有人开放
// {"action":101,"indexId":"1641455102258","content":"已取消所有下注","time":"2022-01-06 15:45:02","close_time":null,"PeriodsStatus":null,"type":"text","setKinds":107,"fd":36,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-06 15:45:02"}
export const userCancelFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let currFromUserFd =  acceptObj.fd
		let date_time_str = acceptObj.date_time
		currFromUserFd = "a_"+currFromUserFd
		let tempFdArrayStr = uni.getStorageSync('currChatListArray')
		let tempFdArrayObjFDDD = tempFdArrayStr[currFromUserFd]						
		let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)	
		let currNickName = tempFdRealObj.username
						
		return {name:'机器人',content:'',id:'1010',username:currNickName,contentMsg:acceptObj.content,timeStr:date_time_str}
	}else{
		let item = acceptObj
		let currNickName = item.username
		let tempDateTime = handleTimeStr(item.created_at)	
		let total = 0
		return {name:'机器人',content:total,id:'1010',username:currNickName,contentMsg:item.content,timeStr:tempDateTime}
	}
	
}

// if(acceptObj.setKinds == 205){//这个还不知道是什么功能




// if(acceptObj.setKinds == 71){////取消下注(单个),面对所有人开放,八卦的没有用上
// {"action":101,"content":{"name":"2","periods_num":"20211120011","money":"100.00"},"time":"2021-11-20 22:04:13","close_time":null,"PeriodsStatus":null,"type":"text","setKinds":71,"fd":256,"robot":null,"chat_switch":null,"username":null}
export const userCancelSignleFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	let currFromUserFd =  acceptObj.fd
	let date_time_str = acceptObj.date_time
	currFromUserFd = "a_"+currFromUserFd
	let tempFdArrayStr = uni.getStorageSync('currChatListArray')
	let tempFdArrayObjFDDD = tempFdArrayStr[currFromUserFd]						
	let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)	
	let currNickName = tempFdRealObj.username
	
	let acceptObj_content = acceptObj.content
	// console.log("acceptObj_content",acceptObj_content)
	let tempUsername = acceptObj_content.name
	let tempPeriodsNum = acceptObj_content.periods_num
	let tempMoney = acceptObj_content.money

					
    return {name:'机器人',level:'00',id:'71',contactname:'@玩家('+currNickName+')',cancelBetName:tempUsername,cancelBetNum:tempMoney,currpernum:tempPeriodsNum,username:thisCurrUserID,timeStr:date_time_str}
	
}


// if(acceptObj.setKinds == 2){//002 就是2 允许聊天,是给所有人看的，显示在左边
// {"action":101,"content":"哈哈","time":1628348680,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":2,"fd":5,"robot":null,"chat_switch":null}
export const userAllowChatFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {
	
	if(typeStr == 'setKinds'){
		let currFromUserFd =  acceptObj.fd
		let date_time_str = acceptObj.date_time
		currFromUserFd = "a_"+currFromUserFd		
		let tempFdArrayStr = uni.getStorageSync('currChatListArray')
		let tempFdArrayObjFDDD = tempFdArrayStr[currFromUserFd]								
		let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)
		let currNickName = tempFdRealObj.username
		let currLV = tempFdRealObj.consumption_level_id
		let currHead = tempFdRealObj.imageHead
		let currType = acceptObj.type
		// console.log("currType",currType)
		let headImgS ='';
		if(currHead == null || currHead == ''){
		
			 headImgS= '../../static/images/icon_hy_xj_default_head.png'
		}else{
			
			headImgS = currHead
		}
		
		// console.log("headImgS",headImgS);
		
		let currLVS = null 
		if(currLV == null){
			
			currLVS = 0
		}else{
			
			currLVS = currLV
		}
		
		if(currNickName == thisCurrUserID){
			// return 

		}else{
			
			if(acceptObj.hasOwnProperty('type') && currType === 'admin'){
				
				// 这里的显示名称字段是name
				return {name:'超级管理员',content:acceptObj.content,level:currLVS,id:'1111',username:currNickName,chatload_id:Date.parse(new Date())+acceptObj.indexId,headImg:headImgS,timeStr:date_time_str}
				
			}else{

				return {name:currNickName,content:acceptObj.content,level:currLVS,id:'1111',username:currNickName,chatload_id:Date.parse(new Date())+acceptObj.indexId,headImg:headImgS,timeStr:date_time_str}
				
			}
			
			
		}
	
	}else{
		let item = acceptObj
		let tempSlStr = item.content
		
		// console.log("tempSlStr",tempSlStr);
		
		let tempUsername = item.username
		let tempDateTime = handleTimeStr(item.created_at)	
		let tempImage = item.head_image
		
		let tempLvNum = item.consumption_level_id
		
		if(tempImage == null || tempImage == ''){
			
			tempImage = thisOtherPerImg
		}
		// console.log("tempImage",tempImage);
		// var tempbetJson = JSON.parse(tempSlStr)
												
		if(item.username == thisCurrUserID){
			//这里应该只有只有自己看到,自己的信息，无法看到其中的
		
			return {name:thisCurrUserID,content:tempSlStr,level:tempLvNum,id:'2222',username:thisCurrUserID,chatload_id:Date.parse(new Date())+item.id,headImg:tempImage,timeStr:tempDateTime}

		}else{
	
			return {name:item.username,content:tempSlStr,level:tempLvNum,id:'1111',username:item.username,chatload_id:Date.parse(new Date())+item.id,headImg:tempImage,timeStr:tempDateTime}

			
		}			
		
	}
	
	
					
     
	
}									


// if(acceptObj.setKinds == 104){//封盘提示(慢了,下次请早/慢了,下次取消请早)，这个是机器人通知的那个内容
// {"action":101,"indexId":"1640671083889","content":"慢了,下次取消请早!","time":1640671083,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":104,"fd":67,"robot":null,"chat_switch":null,"username":null,"date_time":"2021-12-28 13:58:03"}					
export const userSlowFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let currFromUserFd =  acceptObj.fd
		let date_time_str = acceptObj.date_time
		// console.log("currFromUserFd",currFromUserFd);
		currFromUserFd = "a_"+currFromUserFd
		
		let tempFdArrayStr = uni.getStorageSync('currChatListArray')
		
		let tempFdArrayObjFDDD = tempFdArrayStr[currFromUserFd]
		// console.log("tempFdArrayObjFDDD",tempFdArrayObjFDDD,typeof tempFdArrayObjFDDD);
		let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)
										
		let currNickName = tempFdRealObj.username
		// 104401		
		return {name:'机器人',level:'00',id:'104401',username:currNickName,contentMsg:acceptObj.content,timeStr:date_time_str}
		
	}else{
		let item = acceptObj
		let acceptObj_content = item.content
		let tempDateTime = handleTimeStr(item.created_at)

		// 104401								
		return {name:'机器人',level:'00',id:'104401',username:item.username,contentMsg:acceptObj_content,timeStr:tempDateTime}
		
	}
	
}

// if(acceptObj.setKinds == 1041){//封盘提示(慢了,下次请早/慢了,下次取消请早)，这个是右边显示的发送内容
// {"action":101,"indexId":"1640671702819","content":"100 123","time":1640671702,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":1041,"fd":133,"robot":null,"chat_switch":null,"username":null,"date_time":"2021-12-28 14:08:22"}
export const userEarlyFunc = (acceptObj,thisCurrUserID='',thisOtherPerImg,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let currFromUserFd =  acceptObj.fd
		let date_time_str = acceptObj.date_time
		 
		// console.log("currFromUserFd",currFromUserFd);
		currFromUserFd = "a_"+currFromUserFd
		
		let tempFdArrayStr = uni.getStorageSync('currChatListArray')
		
		let tempFdArrayObjFDDD = tempFdArrayStr[currFromUserFd]
		// console.log("tempFdArrayObjFDDD",tempFdArrayObjFDDD,typeof tempFdArrayObjFDDD);
		let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)
										
		let currNickName = tempFdRealObj.username
		
		// 104401					
		var tempLVN = tempFdRealObj.consumption_level_id
		
		if(tempLVN == null){
			tempLVN = 0
		}
		
		let tempImage = tempFdRealObj.imageHead
		if(tempImage == null || tempImage == ''){
			
			tempImage = thisOtherPerImg
		}
		
		
		let resOtherPerson = {name:tempFdRealObj.username,content:acceptObj.content,level:tempLVN,id:'1111',type:2,username:tempFdRealObj.username,headImg:tempImage,timeStr:date_time_str}		
		
		
		let niceNameStr= uni.getStorageSync('nickname')
		let resSelfPerson = {name:tempFdRealObj.username,content:acceptObj.content,level:tempLVN,id:'2222',username:niceNameStr,headImg:tempImage,timeStr:date_time_str}
		
		return {resOtherPerson,resSelfPerson}
				
		// return {name:'机器人',level:'00',id:'104401',username:currNickName,contentMsg:acceptObj.content,timeStr:date_time_str}
		
	}else{
		// console.log("item",item);
		let item = acceptObj
		let acceptObj_content = item.content
		let tempDateTime = handleTimeStr(item.created_at)
						
		let tempUsername = item.username
		let tempImage = item.head_image	
		// let tempLvNum = item.consumption_level_id
		
		if(tempImage == null || tempImage == ''){
			
			tempImage = thisOtherPerImg
		}						
	
		let otherPerson = {name:tempUsername,content:acceptObj_content,level:0,id:'1111',type:2,username:tempUsername,headImg:tempImage,timeStr:tempDateTime}
		
		let niceNameStr= uni.getStorageSync('nickname')
		let selfPerson = {name:tempUsername,content:acceptObj_content,level:0,id:'2222',username:niceNameStr,headImg:tempImage,timeStr:tempDateTime}

		
		return {otherPerson,selfPerson}
		
		
	}
	
	
}


// if(acceptObj.setKinds == 188){//输赢结果列表
// {"action":101,"indexId":"1642524321899","content":"1|单|小|小单@20220119010","time":1642524321,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":188,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-19 00:45:21"}
export const userSignleResultFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	if(typeStr == 'setKinds'){
		// this.currpercontent = '结算完毕'
		let tempContent = acceptObj.content
		let date_time_str = acceptObj.date_time
		let tempContent_perNum = tempContent.split('@')[1]
		// this.lastPerNum = tempContent_perNum
		// console.log("tempContent_perNum",tempContent_perNum)
		let tempContent_result = tempContent.split('@')[0]
		// console.log("tempContent_result",tempContent_result)
		let tempImgNumA = tempContent_result.split('|')
		// console.log("当前类型:",typeof(tempContent));
		
		if(typeof(tempContent_result) == "string"){
			// tempContent = tempContent.replace('|', '，');
			// 正则替换,/|/g 中 /| 是将 | 转义，/g 表示替换所有字符串。
			// tempContent = tempContent.replace(/|/g,'，');
			//// 字符串分解连接替换法 
			
			tempContent_result = tempContent_result.split('|').join('，');
		    tempContent_result = tempContent_result.split('，')[0]
			// this.currPerResultImgNum = tempImgNumA[0]
			// // this.currPerResultImg = "../../../static/images/img/icon_mhxy_result_"+tempImgNumA[0]+".png"		
			// this.mh_icon_img = this.currPerResultImg	
			// this.currPerResultContent = tempContent_result
		
			return {name:'机器人',content:tempContent_result,id:'8888',chatload_id:Date.parse(new Date())+acceptObj.indexId,timeStr:date_time_str}
			
		}
	}else{
		let item = acceptObj
		// console.log(item.content);
		let tempDateTime = handleTimeStr(item.created_at)
		var kaijiangStr = item.content;
		var tempContent_perNum = kaijiangStr.split('@')[1]
		// this.lastPerNum = tempContent_perNum
		var kaijiangStr_result = kaijiangStr.split('@')[0]
		var tempImgNum = kaijiangStr_result.split('|')
		// console.log("tempImgNumA开奖结果",tempImgNum[0]);
		kaijiangStr_result = kaijiangStr_result.split('|').join('，');
		kaijiangStr_result = kaijiangStr_result.split('，')[0]
		// kaijiangStr_result = "("+kaijiangStr_result+")"
						
						
		// console.log("赋值前:",this.currPerResultImgNum);				
		// var currPerResultImgNumS = tempImgNum[0]
		// this.currPerResultImgNum = tempImgNum[0]
			
		// console.log("赋值后:",this.currPerResultImgNum);		
							
		// this.currPerResultContent = kaijiangStr_result
		// console.log("item.kind",typeof(kaijiangOBj),kaijiangOBj);
		
		return {name:'机器人',content:kaijiangStr_result,id:'8888',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
	}
}



// if(acceptObj.setKinds == 201){//输赢结果列表
// {"action":101,"content":{"name":"机器人","content":[{"name":"牛逼008","result":[{"detailKindName":"小","detailPlayNum":"100.00","result":2}],"detailPlayTotal":"100.00"}],"currRecord":20210807011},"time":1628337921,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":201,"fd":null,"robot":null}
export const userWinAndLoseResultFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let date_time_str = acceptObj.date_time			
		let zhangdanlistobjs =  acceptObj.content
		let zdlistobj = zhangdanlistobjs.content
		
		// this.lastPerNum = zhangdanlistobjs.currRecord
		// console.log("zdlistobj",zdlistobj,zdlistobj.length);
		if(zdlistobj.length == 0){
			
			// // console.log("进来咯");
		
			let resZero = {name:'机器人',content:[
				{name:'暂无人投注',result:[
				],detailplaytotal:'0'},
			],currRecord:'第'+zhangdanlistobjs.currRecord+'期玩家投注账单',id:'1011',consequence:zhangdanlistobjs.consequence,timeStr:date_time_str}
			
			return {status:0,resContent:resZero}
			
		}else{
			let niceNameStr= uni.getStorageSync('nickname')
			let currContent = acceptObj.content
			let currContentTwo = currContent.content
			// var cuurContentResult = currContent.result
			let currRecord = currContent.currRecord
			
			if(currContent.only_see_win_switch === 1 ){ //为1，结果列表仅仅发送个人显示,这个待修改
			
				// console.log("niceNameStr",niceNameStr)
				// console.log("currContent.name",currContent.name)
				// console.log("currContentTwo.name",currContentTwo.name)
				
										
				var currContentTwo_new =  currContentTwo.filter(item=>{
					 if(item.name == niceNameStr){
						 // console.log("item",item)
						return item
					 }
				 })
				// console.log("结果列表仅仅发送个人显示",currContentTwo_new)
		   
			   let resOne = {name:currContent.name,content:currContentTwo_new,currRecord:'第'+currRecord+'期玩家输赢结果',id:'1011',chatload_id:Date.parse(new Date())+acceptObj.indexId,consequence:currContent.consequence,timeStr:date_time_str}
			   return {status:zdlistobj.length,resContent:{switch:1,resOne}}
			   
									
			}else{ //为0，结果列表发送所有人显示
				// console.log("结果列表发送所有人显示",currContentTwo_new)
				
				let contentWinObj = []
				let contentMoneyObj = []
				// console.log("currContent",currContent);
				// console.log("currContentTwo",currContentTwo);
				currContentTwo.forEach(item=>{
					// console.log("item",item.win_money);
					contentWinObj.push({name:item.name,content:item.win_money.toString().split('.')[0]})
					contentMoneyObj.push({name:item.name,content:item.money.toString().split('.')[0]})
				})
				// chatload_id
				// console.log("chatload_id:acceptObj.indexId",acceptObj.indexId);
			
				
				let resMore = {name:currContent.name,content:contentWinObj,currRecord:'第'+currRecord+'期玩家输赢结果',id:'1011',chatload_id:Date.parse(new Date())+acceptObj.indexId,trend:currContent.trend,contentMoney:contentMoneyObj,consequence:currContent.consequence,timeStr:date_time_str}
				
				 return {status:zdlistobj.length,resContent:{switch:currContent.only_see_win_switch,resMore}}
				
				
			}
			
			
			
		}
		
	}else{
		let item = acceptObj
		let currContent = item.content
		let tempDateTime = handleTimeStr(item.created_at)
		
		let acceptObj_a = JSON.parse(currContent);
		
		// console.log("acceptObj_a",typeof(acceptObj_a),acceptObj_a);
		
		let currContent_a = acceptObj_a.content
		
		if(currContent_a.length == 0){
			
			// return false
			let resZero = {name:'机器人',content:[
				{name:'暂无人投注',result:[
				],detailplaytotal:'0'},
			],currRecord:'第'+'期玩家投注账单',id:'1011',consequence:acceptObj_a.consequence,timeStr:tempDateTime}
			
			return {status:0,resContent:resZero}
		}else{
			// var currContentTwo = currContent.content
			// // var cuurContentResult = currContent.result
			var currRecord = acceptObj_a.currRecord
			let contentWinObj = []
			let contentMoneyObj = []
			// console.log("currContent",currContent);
			currContent_a.forEach(item=>{
				// console.log("item",item.money);
				contentWinObj.push({name:item.name,content:item.win_money.toString().split('.')[0]})
				contentMoneyObj.push({name:item.name,content:item.money.toString().split('.')[0]})
			})
			
			// currContent
			let resMore = {name:acceptObj_a.name,content:contentWinObj,currRecord:'第'+currRecord+'期玩家输赢结果',id:'1011',chatload_id:Date.parse(new Date())+item.id,trend:acceptObj_a.trend,consequence:acceptObj_a.consequence,contentMoney:contentMoneyObj,timeStr:tempDateTime}
		    
			return {status:currContent_a.length,resContent:resMore}
		
			
		}
		
		
		
		
	}
	
	
			
}


// if(acceptObj.setKinds == 88){// 查1000 提交到后台后，后台确认点击的后返回的通知(加血完成)
// {"action":101,"indexId":"1641644909231","content":{"withdraw":null,"top_up":null,"back_money":"22","money":"101972","nickname":"超级管理员","change_money":"10.00"},"time":1641644909,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":88,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 20:28:29"}
export const userAddBloodFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let detaillistobjs =  acceptObj.content
		let date_time_str = acceptObj.date_time
		let contentValStr = {}
		// console.log("parseInt(detaillistobjs.withdraw)",parseInt(detaillistobjs.withdraw));
		contentValStr.withdraw = (detaillistobjs.withdraw) ? parseInt(detaillistobjs.withdraw) : ''
		let top_up_num = 0
		let change_money_num = 0
		if(parseInt(detaillistobjs.top_up)){
			top_up_num = parseInt(detaillistobjs.top_up)
		}else{
			top_up_num = 0
		}
		
		if(parseInt(detaillistobjs.change_money)){
			change_money_num = parseInt(detaillistobjs.change_money)
		}else{
			change_money_num = 0
		}
		
		contentValStr.top_up = top_up_num + change_money_num
		contentValStr.change_money = (detaillistobjs.change_money) ? parseInt(detaillistobjs.change_money):''
		contentValStr.money = (detaillistobjs.money) ? parseInt(detaillistobjs.money) :''
		contentValStr.nickname = detaillistobjs.nickname
						
		return {name:'机器人',content:contentValStr,id:'88',chatload_id:Date.parse(new Date())+acceptObj.indexId,timeStr:date_time_str}
	}else{
		let item = acceptObj
		let acceptObj_content = item.content
		let tempDateTime = handleTimeStr(item.created_at)
		let detaillistobjs = JSON.parse(acceptObj_content)			
		let contentValStr = {}
		// console.log("parseInt(detaillistobjs.withdraw)",parseInt(detaillistobjs.withdraw));
		contentValStr.withdraw = (detaillistobjs.withdraw) ? parseInt(detaillistobjs.withdraw) : ''
		let top_up_num = 0
		let change_money_num = 0
		if(parseInt(detaillistobjs.top_up)){
			top_up_num = parseInt(detaillistobjs.top_up)
		}else{
			top_up_num = 0
		}
		
		if(parseInt(detaillistobjs.change_money)){
			change_money_num = parseInt(detaillistobjs.change_money)
		}else{
			change_money_num = 0
		}
		
		contentValStr.top_up = top_up_num + change_money_num
		contentValStr.change_money = (detaillistobjs.change_money) ? parseInt(detaillistobjs.change_money):''
		contentValStr.money = (detaillistobjs.money) ? parseInt(detaillistobjs.money) :''
		contentValStr.nickname = detaillistobjs.nickname
		return {name:'机器人',content:contentValStr,id:'88',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
	}
	
	
}


// if(acceptObj.setKinds == 89){//(回血完成)
// {"action":101,"indexId":"1641645168267","content":{"withdraw":"10.00","top_up":"30.00","back_money":"22","money":"101982","nickname":"超级管理员","change_money":"10.00"},"time":1641645168,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":89,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 20:32:48"}
export const userBloodReturnFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		var detaillistobjs =  acceptObj.content
		let date_time_str = acceptObj.date_time
		let contentValStr = {}
		// console.log("parseInt(detaillistobjs.withdraw)",parseInt(detaillistobjs.withdraw));
		contentValStr.withdraw = (detaillistobjs.withdraw) ? parseInt(detaillistobjs.withdraw) : ''
		let top_up_num = 0
		let change_money_num = 0
		if(parseInt(detaillistobjs.top_up)){
			top_up_num = parseInt(detaillistobjs.top_up)
		}else{
			top_up_num = 0
		}
		
		if(parseInt(detaillistobjs.change_money)){
			change_money_num = parseInt(detaillistobjs.change_money)
		}else{
			change_money_num = 0
		}
		
		// contentValStr.top_up = top_up_num + change_money_num
		contentValStr.top_up = top_up_num
		contentValStr.change_money = (detaillistobjs.change_money) ? parseInt(detaillistobjs.change_money):''
		contentValStr.money = (detaillistobjs.money) ? parseInt(detaillistobjs.money) :''
		contentValStr.nickname = detaillistobjs.nickname
		
		
		return {name:'机器人',content:contentValStr,id:'89',chatload_id:Date.parse(new Date())+acceptObj.indexId,timeStr:date_time_str}
		
	}else{
		let item = acceptObj
		let acceptObj_content = item.content
		// console.log("acceptObj_content",acceptObj_content);
		let tempDateTime = handleTimeStr(item.created_at)
		let detaillistobjs = JSON.parse(acceptObj_content)			
		let contentValStr = {}
		// console.log("parseInt(detaillistobjs.withdraw)",parseInt(detaillistobjs.withdraw));
		contentValStr.withdraw = (detaillistobjs.withdraw) ? parseInt(detaillistobjs.withdraw) : ''
		let top_up_num = 0
		let change_money_num = 0
		if(parseInt(detaillistobjs.top_up)){
			top_up_num = parseInt(detaillistobjs.top_up)
		}else{
			top_up_num = 0
		}
		
		if(parseInt(detaillistobjs.change_money)){
			change_money_num = parseInt(detaillistobjs.change_money)
		}else{
			change_money_num = 0
		}
		
		// contentValStr.top_up = top_up_num + change_money_num
		contentValStr.top_up = top_up_num
		contentValStr.change_money = (detaillistobjs.change_money) ? parseInt(detaillistobjs.change_money):''
		contentValStr.money = (detaillistobjs.money) ? parseInt(detaillistobjs.money) :''
		contentValStr.nickname = detaillistobjs.nickname
		return {name:'机器人',content:contentValStr,id:'89',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
	}
	
}


// if(acceptObj.setKinds == 123){//上下分返回昵称余额相关信息,指令1,
// {"action":101,"indexId":"1641645403874","content":[{"withdraw":"10.00","top_up":"30.00","back_money":"22","money":"101982","nickname":"超级管理员"}],"time":1641645403,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":123,"fd":383,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 20:36:43"}
export const userCommandOneFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let detaillistobjs =  acceptObj.content
		let date_time_str = acceptObj.date_time
		// console.log("detaillistobjs",detaillistobjs,typeof detaillistobjs);
		return {name:'机器人',content:detaillistobjs[0],id:'2202',chatload_id:Date.parse(new Date())+acceptObj.indexId,timeStr:date_time_str}
		
	}else{
		let item = acceptObj
		// console.log("item",item);
		let currContent = item.content;
		// console.log("currContent",currContent);
		let tempDateTime = handleTimeStr(item.created_at)
		let acceptObj_a = JSON.parse(currContent)
		return {name:'机器人',content:acceptObj_a[0],id:'2202',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
	}
}



// if(acceptObj.setKinds == 124){ //指令2，相当于 [返] 的功能
// {"action":101,"indexId":"1640614114810","content":{"running":"10969.00","already_running_back":"241.34","money":"27327.84"},"time":1640614114,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":124,"fd":91,"robot":null,"chat_switch":null,"username":null,"date_time":"2021-12-27 22:08:34"}
export const userCommandTwoFunc = (acceptObj,thisCurrUserID,typeStr='setKinds') => {	
	const currRateNumber = parseFloat(uni.getStorageSync('currRateNum'))
	
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let detaillistobjs =  acceptObj.content
		let date_time_str = acceptObj.date_time
		// console.log("detaillistobjs",detaillistobjs,typeof detaillistobjs);
		let contentValStr = {}
		contentValStr.running = parseInt(detaillistobjs.running)
		contentValStr.already_running_back = parseInt(detaillistobjs.already_running_back)
		contentValStr.money = parseInt(detaillistobjs.money)
		contentValStr.currflows = parseInt(parseInt(detaillistobjs.running) * currRateNumber)
		contentValStr.runBackNum =  parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)
		contentValStr.remainMoney = (parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)) + parseInt(detaillistobjs.money)
		
		
		return {name:'机器人',content:contentValStr,id:'2203',chatload_id:Date.parse(new Date())+acceptObj.indexId,nickname:detaillistobjs.nickname,timeStr:date_time_str}
	}else{
		let item = acceptObj
		let currContent =  item.content
	
		let tempDateTime = handleTimeStr(item.created_at)
		let detaillistobjs = JSON.parse(currContent);
		
		// console.log("detaillistobjs",detaillistobjs,typeof detaillistobjs);
		let contentValStr = {}
		contentValStr.running = parseInt(detaillistobjs.running)
		contentValStr.already_running_back = parseInt(detaillistobjs.already_running_back)
		contentValStr.money = parseInt(detaillistobjs.money)
		contentValStr.currflows = parseInt(parseInt(detaillistobjs.running) * currRateNumber)
		contentValStr.runBackNum =  parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)
		contentValStr.remainMoney = (parseInt(parseInt(detaillistobjs.running) * currRateNumber) - parseInt(detaillistobjs.already_running_back)) + parseInt(detaillistobjs.money)
		
		return {name:'机器人',content:contentValStr,id:'2203',chatload_id:Date.parse(new Date())+item.id,nickname:detaillistobjs.nickname,timeStr:tempDateTime}
			
	}
	
	
}



// if(acceptObj.setKinds == 180){//投注成功，所有人都能收到
// {"action":101,"indexId":"1641646610145","content":{"bet":["100","乾","100","巽","100","坎"],"periods_num":20220108008,"content":"100 123@101582"},"time":1641646610,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":180,"fd":502,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 20:56:50"}
export const userBetFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {	
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		let currFdNum = acceptObj.fd;
		let date_time_str = acceptObj.date_time
		let currFdNumS ="a_"+currFdNum
		// console.log("currFdNum",currFdNum,currFdNumS);
		let tempFdArrayStr = uni.getStorageSync('currChatListArray')

		let tempFdArrayObjFDDD = tempFdArrayStr[currFdNumS]
										
		let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)
		
	
		
		let tempImage = tempFdRealObj.imageHead
		if(tempImage == null || tempImage == ''){
			
			tempImage = thisOtherPerImg
		}
		
		let strb = []
		let stra=[]
		let total = []
		let totalStr = ""
		let totalNum = 0 
		let currBetTotal =0
		let tempbetJson = acceptObj.content
		let tempbetArr = tempbetJson.bet
		let tempper = tempbetJson.periods_num
		
		// let tempLvNum = tempbetJson.consumption_level_id
				
		for(let i=0;i<tempbetArr.length;i++) {
		  if(i%2==0) {//奇数
			strb.push(tempbetArr[i])
			
		  }else{//偶数
			stra.push(tempbetArr[i])
									
									
		  }
		}
		
					
		if(strb.length == stra.length){
			
			strb.forEach((item,index)=>{
				
				totalNum = totalNum+parseInt(item)
				// currBetTotal  = currBetTotal + totalNum
				total.push({name:stra[index],num:strb[index]})
				totalStr = totalStr + strb[index] +" "+stra[index] + " "
				// this.allTotalNum.push({name:stra[index],num:strb[index]})
			})
			
							
		}
		
		
		// console.log("tempbetJson.content",acceptObj);
		// console.log("tempFdRealObj.username",tempFdRealObj.username);
		
		let resOtherPerson = {name:tempFdRealObj.username,content:tempbetJson.content.split('@')[0],level:0,id:'1111',type:2,username:tempFdRealObj.username,headImg:tempImage,timeStr:date_time_str}
		
		let niceNameStr= uni.getStorageSync('nickname')
		let resSelfPerson = {name:tempFdRealObj.username,content:tempbetJson.content.split('@')[0],level:0,id:'2222',username:niceNameStr,headImg:tempImage,timeStr:date_time_str}
		
		let resRobot = {name:'机器人',content:totalStr,level:'00',id:'7777',contactname:'@玩家('+thisCurrUserID+')',totalNum:totalNum,currpernum:tempper,username:tempFdRealObj.username,splitMoney:parseInt(tempbetJson.content.split('@')[1]),timeStr:date_time_str}
		
		return {resOtherPerson,resSelfPerson,resRobot}
	}else{ //聊天记录
		// console.log("聊天记录");
		let item = acceptObj
		let tempSlStr = item.content
		
		// console.log("tempSlStr",tempSlStr,item);
		let tempUsername = item.username
		
		let tempImage = item.head_image
		
		let tempDateTime = handleTimeStr(item.created_at)	
		// console.log("tempDateTime",tempDateTime);
		// let tempLvNum = item.consumption_level_id
		
		// if(tempImage == null || tempImage == ''){
			
		// 	tempImage = this.ohterPerImg
		// }
		
		
	
		var tempbetJson = JSON.parse(tempSlStr)

			
		
		var strb = []
		var stra=[]
		var total = []
		var totalStr = ""
		var totalNum = 0 
		var currBetTotal =0
		// var tempbetJson = acceptObj.content
		var tempbetArr = tempbetJson.bet
		var tempper = tempbetJson.periods_num
		
		for(let i=0;i<tempbetArr.length;i++) {
		  if(i%2==0) {//奇数
			strb.push(tempbetArr[i])
		  }else{//偶数
			stra.push(tempbetArr[i])
		  }
		}
		

		if(strb.length == stra.length){
			
			strb.forEach((item,index)=>{
	
				total.push({name:stra[index],num:strb[index]})
				totalStr = totalStr + strb[index] +" "+stra[index] +" "
				
			})
			
			totalNum = strb.reduce((pre,item)=>{
				// console.log("item123",item,typeof item)
				pre = pre + parseInt(item)
				return pre
			},0)

		}
		let resNormal = {}
		if(item.username == thisCurrUserID){
			//这里应该只有只有自己看到,自己的信息，无法看到其中的
			let resSelf = {name:thisCurrUserID,content:tempbetJson.content.split('@')[0],level:0,id:'2222',username:thisCurrUserID,chatload_id:Date.parse(new Date())+item.id,headImg:tempImage,timeStr:tempDateTime}
			resNormal = resSelf
		}else{
			let resNoSelf = {name:item.username,content:tempbetJson.content.split('@')[0],level:0,id:'1111',username:item.username,chatload_id:Date.parse(new Date())+item.id,headImg:tempImage,timeStr:tempDateTime}
			resNormal = resNoSelf
			
		}	
						
		// console.log("item.id",item.id);
		let resRobot = {name:'机器人',content:totalStr,id:'7777',contactname:'@玩家('+tempUsername+')',totalNum:totalNum,currpernum:tempper,username:tempUsername,chatload_id:Date.parse(new Date())+(item.id).toString()+"--"+tempper+tempUsername,splitMoney:parseInt(tempbetJson.content.split('@')[1]),timeStr:tempDateTime}
		
		return {resNormal,resRobot}
		
		
		
	}
	
}



// if(acceptObj.setKinds == 139){//查1000,上分指令
// {"action":101,"indexId":"1640688820852","content":"查1000","time":1640688820,"close_time":null,"PeriodsStatus":null,"type":"admin","setKinds":139,"fd":42,"robot":null,"chat_switch":null,"username":null,"date_time":"2021-12-28 18:53:40"}
export const userSearchFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {	
	let currFdNum = acceptObj.fd;
	let date_time_str = acceptObj.date_time
	let currFdNumS ="a_"+currFdNum
	// console.log("currFdNum",currFdNum,currFdNumS);
	let tempFdArrayStr = uni.getStorageSync('currChatListArray')
	
	// console.log("tempFdArrayStr",typeof(tempFdArrayStr),tempFdArrayStr);
	// var tempFdArrayObj =  JSON.parse(tempFdArrayStr);
	
	// console.log("tempFdArrayObj",typeof(tempFdArrayObj),tempFdArrayObj);
	let tempFdArrayObjFDDD = tempFdArrayStr[currFdNumS]
									
	let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)
	
	
	// this.currFdNum = tempFdRealObj
	
	// console.log("tempFdRealObj",tempFdRealObj);
	
	// var tempLVN = tempFdRealObj.consumption_level_id
	
	// if(tempLVN == null){
	// 	tempLVN = 0
	// }
	
	let tempImage = tempFdRealObj.imageHead
	if(tempImage == null || tempImage == ''){
		
		tempImage = thisOtherPerImg
	}
	
						
	// console.log("tempbetJson.content",acceptObj);	
	let resOtherPerson = {name:tempFdRealObj.username,content:acceptObj.content,level:0,id:'1111',type:2,username:tempFdRealObj.username,headImg:tempImage,timeStr:date_time_str}
	
	
	let niceNameStr= uni.getStorageSync('nickname')
	let resSelfPerson = {name:tempFdRealObj.username,content:acceptObj.content,level:0,id:'2222',username:niceNameStr,headImg:tempImage,timeStr:date_time_str}
	
	return {resOtherPerson,resSelfPerson}
}


// if(acceptObj.setKinds == 191){//还差封盘先40秒
// {"action":101,"indexId":"1641648170573","content":"80秒","time":1641648170,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":191,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 21:22:50"}
export const userCountDownFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {	
	let date_time_str = ""
	if(typeStr == 'setKinds'){
		date_time_str = acceptObj.date_time
	}else{
		date_time_str = formatDateTime(acceptObj.created_at)
	}
	
	return {name:'机器人',content:acceptObj.content,level:'02',id:'3333',timeStr:date_time_str}
	
}

// if(acceptObj.setKinds == 1911){//提前封盘
// {"action":101,"indexId":"1641648309793","content":"WWWW 提前封盘","time":1641648309,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":1911,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 21:25:09"}

export const userForwardFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {
	let date_time_str = ""
	if(typeStr == 'setKinds'){
	    date_time_str = acceptObj.date_time
	}else{
	    date_time_str = formatDateTime(acceptObj.created_at)
	}
	
	return {name:'机器人',content:acceptObj.content,level:'02',id:'3333',timeStr:date_time_str}
}


// if(acceptObj.setKinds == 192){//对所有人的显示账单页面
// {"action":101,"indexId":"1641648309964","content":{"name":"机器人","content":[],"currRecord":20220108014},"time":1641648309,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":192,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2022-01-08 21:25:09"}
// {"action":101,"indexId":"1640247907845","content":{"name":"机器人","content":[{"name":"生死为逼","result":[{"detailKindName":"大双","detailPlayNum":"155.00"}],"detailPlayTotal":"155.00"},{"name":"阿布","result":[{"detailKindName":"6","detailPlayNum":"119.00"}],"detailPlayTotal":"119.00"},{"name":"把把开上把","result":[{"detailKindName":"小","detailPlayNum":"60.00"},{"detailKindName":"1","detailPlayNum":"60.00"}],"detailPlayTotal":120}],"currRecord":20211223020},"time":1640247907,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":192,"fd":null,"robot":null,"chat_switch":null,"username":null,"date_time":"2021-12-23 16:25:07"}
export const userBillFunc = (acceptObj,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {	
	// console.log("acceptObj",acceptObj);
	if(typeStr == 'setKinds'){
		var zhangdanlistobjs =  acceptObj.content
		let date_time_str = acceptObj.date_time
		var zdlistobj = zhangdanlistobjs.content
		// console.log("zdlistobj",typeof(zdlistobj),zdlistobj,zdlistobj.length);
		// console.log("zhangdanlistobjs",zhangdanlistobjs)
		if(zdlistobj.length == 0){
		
			let resZero = {name:'机器人',content:[
				{name:'暂无人投注',result:[
				],detailplaytotal:'0'},
			],currRecord:'第'+zhangdanlistobjs.currRecord+'期玩家投注账单',id:'9999',timeStr:date_time_str}
			
			return {status:0,resContent:resZero}
			
		}else{
			var niceNameStr= uni.getStorageSync('nickname')
			var currContent = acceptObj.content
					
			var currContentTwo = currContent.content
			// var cuurContentResult = currContent.result
			
			var currRecord = currContent.currRecord
						
			// console.log("结果列表开关状态值",currContent.only_see_bill_switch)
						
			if(currContent.only_see_bill_switch === 1 ){ //为1，结果列表仅仅发送个人显示
			
				let currContentTwo_new =  currContentTwo.filter(item=>{
					 if(item.name == niceNameStr){
						 // console.log("item",item)
					 	return item
					 }
				 })
				let resOne = {name:currContent.name,content:currContentTwo_new,currRecord:'第'+currRecord+'期玩家投注账单',level:'11',id:'9999',timeStr:date_time_str}
				 return {status:zdlistobj.length,resContent:{switch:1,resOne}}
								
			}else{ //为0，结果列表发送所有人显示
		
				let contentObjArray = []
				zdlistobj.forEach(item=>{
					let itemSubStr = ""
					
					item.result.forEach(itemSub=>{
						itemSubStr = itemSubStr +itemSub.detailPlayNum.split('.')[0]+ " " + itemSub.detailKindName +" "
					})
					contentObjArray.push({name:item.name,content:itemSubStr})
						
				})
				
				let resMore = {name:currContent.name,content:contentObjArray,currRecord:'第'+currRecord+'期玩家投注账单',level:'11',id:'9999',timeStr:date_time_str}
			    return {status:zdlistobj.length,resContent:{switch:currContent.only_see_win_switch,resMore}}
			}
			
			
			
		}
					
	}else{
		let item = acceptObj
		let tempDateTime = handleTimeStr(item.created_at)
		var zhangdanOBj = JSON.parse(item.content);
		var tempzhangdanOBJ = zhangdanOBj.content
		// console.log("进来",zhangdanOBj);
		if(tempzhangdanOBJ.length == 0){//没有人投注就不显示吗？
			// console.log("进来咯");
			let resZero = {name:'机器人',content:[
				{name:'暂无人投注',result:[
				],detailplaytotal:'0'},
			],currRecord:'第'+zhangdanOBj.currRecord+'期玩家投注账单',id:'9999',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
			
			return {status:0,resContent:resZero}
			
		}else{
			
			let zhangdanOBjContent = zhangdanOBj.content
			let contentObjArray = []
	
			zhangdanOBjContent.forEach(item=>{
				let itemSubStr = ""
				item.result.forEach(itemSub=>{
					// console.log("itemSub",itemSub);
					itemSubStr = itemSubStr +itemSub.detailPlayNum.split('.')[0]+ " " + itemSub.detailKindName +" "
				})
		
				contentObjArray.push({name:item.name,content:itemSubStr})

				
			})
			
			let resMore = {name:zhangdanOBj.name,content:contentObjArray,currRecord:'第'+zhangdanOBj.currRecord+'期玩家投注账单',id:'9999',chatload_id:Date.parse(new Date())+item.id,timeStr:tempDateTime}
			return {status:tempzhangdanOBJ.length,resContent:resMore}
			
		}				
		
	}
		
									
}


export const handleTimeStr = (value)=>{
	return formatDateTime(value)
}







/*
	这个是仅仅是只有自己看见的的方法(即action=103),开始
	------------------------------------------------
*/

// if(acceptObj.kind == 3){//允许聊天(这个是给自己的)
// {"action":103,"fromUserFd":2059,"content":"你好","type":"text","sendTime":"2022-01-09 23:51:06","kind":3,"indexId":"1641743466599","date_time":"2022-01-09 23:51:06"}
export const onlyUserSeeChatFunc = (acceptObjFrom,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {	
		// console.log("acceptObjFrom",acceptObjFrom);
		if(typeStr == 'setKinds'){
			
			let date_time_str = acceptObjFrom.date_time
			let currFromUserFd =  acceptObjFrom.fromUserFd
			currFromUserFd = "a_"+currFromUserFd
			let tempFdArrayStr = uni.getStorageSync('currChatListArray')
			let tempFdArrayObjFDDD = tempFdArrayStr[currFromUserFd]							
			let tempFdRealObj = JSON.parse(tempFdArrayObjFDDD)
			let currNickName = tempFdRealObj.username
			let currLV = tempFdRealObj.consumption_level_id
			
			let currHead = tempFdRealObj.imageHead
			let currType = acceptObjFrom.type
			// console.log("currType",currType);
			
			let headImgS ='';
			if(currHead == null || currHead == ''){
			
				headImgS= '../../static/images/icon_hy_xj_default_head.png'
			}else{
				
				headImgS = currHead
			}
			// console.log("headImgS",headImgS);
			let currLVS = null 
			if(currLV == null){
				
				currLVS = 0
				
			}else{
				
				currLVS = currLV
			}
			
			
			let niceNameStr= uni.getStorageSync('nickname')
			
				
			if(acceptObjFrom.hasOwnProperty('type') && currType === 'admin'){
				// console.log("jinlai");
				return {name:thisCurrUserID,headImg:headImgS,content:acceptObjFrom.content,level:currLVS,id:'2222',username:'超级管理员',timeStr:date_time_str}
			}else{
				return {name:thisCurrUserID,headImg:headImgS,content:acceptObjFrom.content,level:currLVS,id:'2222',username:niceNameStr,timeStr:date_time_str}
			}
			
		}
	
		
	
}





// if(acceptObj.kind == 105){ //投注,押注，这个暂时没有需要的准备，可以删掉
// {"action":103,"fromUserFd":502,"content":{"bet":["100","乾","100","巽","100","坎"],"periods_num":20220108008,"content":"100 123@101582"},"type":"text","sendTime":"2022-01-08 20:56:50","kind":105,"indexId":"1641646610120","date_time":"2022-01-08 20:56:50"}
export const onlyUserSeeBetFunc = (acceptObjFrom,thisCurrUserID,thisOtherPerImg,typeStr='setKinds') => {	
	// var tempLvNumS = null;
	// if(this.currFdNum.consumption_level_id == null){
		
	// 	tempLvNumS = 0
	// }else{
		
	// 	tempLvNumS = this.currFdNum.consumption_level_id
	// }
	
	
	const that = this
	// console.log("tempLvNumS",tempLvNumS);
	
	
	var strb = []
	var stra=[]
	var total = []
	var totalStr = ""
	var totalNum = 0 
	var currBetTotal =0
	var tempbetJson = acceptObj.content
	var tempbetArr = tempbetJson.bet
	var tempper = tempbetJson.periods_num
	
	var tempLvNum = tempbetJson.consumption_level_id
	// var newArray = []
	// var newType = []
	// tempbetArr = ["10", "1", "10", "1", "10", "1", "10", "1", "10", "1", "100", "2","1000","单"];
	for(let i=0;i<tempbetArr.length;i++) {
	  if(i%2==0) {//奇数
		strb.push(tempbetArr[i])
		
	  }else{//偶数
		stra.push(tempbetArr[i])
		
		// // 
		// if(newType.indexOf(tempbetArr[i]) === -1){//不存在这个类型  创建类型数组
		// 	 newArray[tempbetArr[i]]=tempbetArr[i-1];
		// 	 newType.push(tempbetArr[i])
		// }else{ //存在这个类型
		// 	 newArray[tempbetArr[i]] = (newArray[parseInt(tempbetArr[i]) + parseInt(tempbetArr[i-1])]).toString();
		// }
								
	  }
	}
	
	// console.log("newArray",newArray,newType)
	// console.log("押注金额",strb);
	// console.log("押注玩法名字",stra);
	
	if(strb.length == stra.length){
		
		strb.forEach((item,index)=>{
			
			totalNum = totalNum+parseInt(item)
			// currBetTotal  = currBetTotal + totalNum
			total.push({name:stra[index],num:strb[index]})
			totalStr = totalStr + strb[index] +" "+stra[index] + " "
			// this.allTotalNum.push({name:stra[index],num:strb[index]})
		})
		
		
		// console.log("totalNum",totalNum);
	}
		
	
}




/*
	这个是获取在聊天室内的所有人的FD集合,开始
	------------------------------------------------
*/

// if(acceptObj.setKinds == 195){//所有人的FD值，主要为了分辨是谁	
// {"action":101,"content":{"a_652":"{\"username\":\"niubi003\",\"imageHead\":\"..\\/..\\/..\\/static\\/images\\/tx\\/tx0.png\",\"consumption_level_id\":null}","a_648":"{\"username\":\"yang\",\"imageHead\":null,\"consumption_level_id\":null}"},"time":1627974826,"close_time":null,"PeriodsStatus":null,"type":null,"setKinds":195,"fd":null}	

export const getAllFdArray = (acceptObjFrom)=>{
	let tempChatListArray = acceptObjFrom.content
	// console.log("tempChatListArray",tempChatListArray);
	uni.setStorageSync('currChatListArray','');
	uni.setStorageSync('currChatListArray',tempChatListArray);

	return tempChatListArray
}