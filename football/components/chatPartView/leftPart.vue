<template>
	
	<view class="playKindBet">
		<text class="playKindBet_time" >
			{{dataSrc.timeStr }}
		</text>
		
		
		
		<!-- 其他人发送的头像，显示在左侧 -->
		<view class="playKindBet_content"  >
			<view class="playKindBet_content_image">
				<image :src="dataSrc.headImg" mode=""></image>
			</view>
			
			<!-- 其他人发送的具体聊天内容，显示在左侧  -->
			<view class="playKindBet_content_name_text" >
				<view class="playKindBet_content_name">
					{{dataSrc.name}} 
					<!-- <view class="playKindBet_content_name_timer">
						{{dataSrc.timeStr }}
					</view> -->
				</view>
				
				<view class="playKindBet_content_text">
					<!-- <text class="playKindBet_content_text_bc" v-if="item.id == 2222  && item.username != currUserID ">
						@{{item.username}}
					</text> -->
					{{dataSrc.content}}
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name:"leftPart",
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
			// console.log("this.dataSrc",this.dataSrc);
		}
	}
	
	
</script>

<style>
	@import "../../pages/index/index.css";
</style>
