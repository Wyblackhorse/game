<template>
	
	<view class="playKindBet">
		<text class="playKindBet_time" >
			{{dataSrc.timeStr}}
		</text>
		
		<!-- 机器人发送的头像，显示在左侧(自己看到88) 主要是针对充值提醒-->
		<view class="playKindBet_content"  >
			<view class="playKindBet_content_image">
				<image :src="robotImg" mode=""></image>
			</view>
			
			<!-- 机器人发送的具体聊天内容，显示在右侧 -->
			<!-- 系统的机器人消息,押注的页面,只有自己能看到 -->	
			<view class="playKindBet_content_name_text" >
				<view class="playKindBet_content_name">
					{{dataSrc.name}}
					<!-- <view class="playKindBet_content_name_timer">
						{{dataSrc.timeStr }}
					</view> -->
				</view>
				<view :class="dataSrc.name != '机器人' ? 'playKindBet_content_text':'playKindBet_content_text_robotSend'">
				<!-- <view class="playKindBet_content_text"> -->
					<!-- v-if="item.id == 2222  && item.username != currUserID " -->
					<text class="playKindBet_content_text_bc" >
						@[{{dataSrc.content.nickname}}]
					</text>
					<text style="margin-left: 5px;">加血完成: {{dataSrc.content.change_money}}</text>
					<view class="">
						上{{dataSrc.content.top_up}} 下{{dataSrc.content.withdraw}} 余{{dataSrc.content.money}}
					</view>
				</view>
			</view>	
			
			
		</view>
	</view>
</template>

<script>
	export default {
		name:"rechargePart",
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
