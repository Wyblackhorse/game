<template>
	
	<view class="playKindBet">
		<text class="playKindBet_time" >
			{{dataSrc.timeStr}}
		</text>
		
		
								
		<!-- 机器人发送的头像，显示在左侧(自己看到1010) -->
		<!-- <view class="mainchat_item_robot_left" v-else-if="item.id == 1010 && item.username == currUserID "> -->
		<!-- 这边阿道要求开放 -->
		
		<view class="playKindBet_content" >
			<view class="playKindBet_content_image">
				<image :src="robotImg" mode=""></image>
			</view>
		
			<view class="playKindBet_content_name_text" >
				<view class="playKindBet_content_name">
					{{dataSrc.name}}
					<!-- <view class="playKindBet_content_name_timer">
						{{dataSrc.timeStr }}
					</view> -->
				</view>
				<view :class="dataSrc.name != '机器人' ? 'playKindBet_content_text':'playKindBet_content_text_robotSend'">
				<!-- <view class="playKindBet_content_text"> -->
					
					<text class="playKindBet_content_text_bc" >
						@[{{dataSrc.username}}]
					</text>
					<text style="margin-left: 5px;">{{dataSrc.contentMsg}}</text>
		
				</view>
			</view>	
			<!-- <view class="mainchat_item_robot_right" v-else-if="item.id == 1010 && item.username == currUserID"> -->
			<!-- <view class="mainchat_item_robot_right" v-else-if="item.id == 1010"> -->
		</view>
	</view>
</template>

<script>
	export default {
		name:"cancelPart",
		props:{
		   dataSrc:{
			   type:Object,
			   required:true,
		   },
		   robotImg:{
			   type:String,
			   required:true
		   }
		},
		data(){
			return {
				
			}
		},
		onShow() {
			
		}
	}
	
	
</script>

<style>
	@import "../../pages/index/index.css";
</style>
