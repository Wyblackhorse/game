<template>
	<view class="content" @touchstart.stop="clickevent">
		<view
		:class="active" 
		>
		 </view>
	</view>
</template>

<script>
	export default {
		props:{
			istab: {
			  type: Boolean,
			  default: false
			}		
		},
		data(){
			return{
				active:''
			}
		},
		watch:{
			 istab(val){
				if(val){
					this.clickevent();
				}
			} 
		},
		methods: {
		 clickevent(){
			   this.active='active'
			   setTimeout(()=>{
				this.active=''
			   },1800) 
		}
		}
	}
</script>

<style>
.content{
	width: 100%;
	height: 100%; 
	position: relative;
	overflow: hidden;
    display: flex;
	justify-content: center;
	align-items: center; 
}
.active{
	position: absolute;
	border-radius: 50%;
	animation: animated 1.8s; 
	pointer-events: none;
	background: linear-gradient(45deg,
				rgba(255,0,0,.5),
				rgba(255,255,0,.5),
				rgba(0,255,255,.5),
				rgba(0,0,255,.5)
			);
}
@keyframes animated{
	0%{
		width: 0px;
		height: 0px;
		opacity: 1;
		background: rgba(0,0,0,.25);
	}
	100%{
		width: 1000px;
		height: 1000px;
		opacity: 0;
		background: transparent;
	}
}
</style>
