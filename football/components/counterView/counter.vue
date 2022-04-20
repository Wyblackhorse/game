<!-- 自定义倒计时组件 -->
<template>
	<span class="countVier">{{timerStrCounter | formateStr}}</span>
</template>

<script>
	export default {
		name:"counter",
		props:{
		   endtime:{
			   type:Number,
			   required:true,
		   },
		},
		filters:{
			formateStr(val){
				
				let time = val/ 1000
				let day = parseInt(time / ( 60 * 60 * 24))
				let hr = parseInt((time % ( 60 * 60 * 24)) / (60 * 60))
				let min = parseInt((time % 3600) /60)
				let sec = parseInt(time % 60)
			
				// let show_day = day > 9 ? day : '0' + day
				
				let show_day = day * 24
				let show_hr = hr > 9 ? hr : '0' + hr
				let show_min = min > 9 ? min : '0' + min
				let show_sec = sec > 9 ? sec : '0' + sec
				// let timeStr =  show_day + ":" +show_hr+":"+show_min+":"+show_sec
				let timeStr =  (show_day + show_hr)+":"+show_min+":"+show_sec
			
				return timeStr
			}
		},
		data(){
			return {
				currRemainingTime:0,
				now: 0,
				showBool:false,
				countInterVal:null,
			}
		},
		onShow() {
			// console.log("dataSrc",this.endtime);
		},
		methods:{
			// formate(t){
			// 	return parseInt(t) / 1000
			// }
		},
		computed:{
			'timerStrCounter'(){
				if(!this.showBool){
					this.currRemainingTime = this.endtime
					this.showBool = true
				}
				
				// console.log("computed的时间",this.currRemainingTime);
				return this.currRemainingTime
			},
		},
		onShow(){
			
		},
		mounted(){
			this.countInterVal = setInterval(()=>{
				this.currRemainingTime = this.currRemainingTime - 1000
			}, 1000);
		},
		onUnload(){
			clearInterval(this.countInterVal)
		}
	}
	
	
</script>

<style>
	.countVier{
		margin-left: 5px;
	}
</style>
