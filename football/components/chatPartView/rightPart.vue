<template>
	
	<view class="playKindBet">
		<text class="playKindBet_time"  >
			{{dataSrc.timeStr}}
		</text>
		
		<!-- 自己发送的头像，显示在右侧 -->
		<!-- <view class="mainchat_item_floatrightleft" v-else-if="item.id == 2222"> -->
						
		<view class="playKindBet_content_self" >
		<!-- 	<view class="playKindBet_content_image">
				<image :src="selfHeadImg" mode=""></image>
			</view> -->
			<view class="playKindBet_content_image">
				<image :src="dataSrc.headImg" mode=""></image>
			</view>
			<view class="playKindBet_content_name_text_self">
				<view class="playKindBet_content_name_self">
					{{dataSrc.username}}
					<!-- <view class="playKindBet_content_name_timer_self">
						{{dataSrc.timeStr }}
					</view> -->
				</view>
				<view class="playKindBet_content_text_self">
					{{dataSrc.content}}
				</view>
			</view>
			
		</view>
	</view>
</template>

<script>
	export default {
		name:"rightPart",
		props:{
		   dataSrc:{
			   type:Object,
			   required:true,
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
