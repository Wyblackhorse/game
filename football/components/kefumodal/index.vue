<template>
	
	<u-modal v-model="showModelView" order-radius=40  :show-title="false" :show-confirm-button="false" :show-cancel-button="false" :mask-close-able="popupClose"  >
			<view class="mainModel">

				
				<view class="mainModel_item" @click="copykefu1">
					<view class="mainModel_item_left">
						<image src="../../static/images/kf1.jpg" mode=""></image>
					</view>
					<view class="mainModel_item_right">
						<view class="mainModel_item_right_name">
							线下充值客服-小雯
							<image class="yrzImage" src="../../static/images/yrz.png" mode=""></image>
						</view>
						<view class="mainModel_item_right_content">
							QQ:{{kefuQQ}}
						</view>
					</view>
					<view class="mainModel_item_copyview" @click="copykefu1">
						<image src="../../static/images/copyicon.png" mode=""></image>
					</view>
				</view>
				
				
				<view class="mainModel_item" @click="copykefu2">
					<view class="mainModel_item_left">
						<image src="../../static/images/kefu2.jpg" mode=""></image>
					</view>
					<view class="mainModel_item_right">
						<view class="mainModel_item_right_name">
							线下充值客服-小静
							<image class="yrzImage" src="../../static/images/yrz.png" mode=""></image>
						</view>
						<view class="mainModel_item_right_content">
							WX:{{kefuWX}}
						</view>
					</view>
					<view class="mainModel_item_copyview" @click="copykefu2">
						<image src="../../static/images/copyicon.png" mode=""></image>
					</view>
				</view>
				
				<!-- <view class="mainModel_item" @click="copykefu3">
					<view class="mainModel_item_left">
						<image src="../../../static/images/kefu3.jpg" mode=""></image>
					</view>
					<view class="mainModel_item_right">
						<view class="mainModel_item_right_name">
							线下充值客服-小汐
							<image class="yrzImage" src="../../../static/images/yrz.png" mode=""></image>
						</view>
						<view class="mainModel_item_right_content" >
							QQ:{{kefutxnum1}}
						</view>
					</view>
					<view class="mainModel_item_copyview" @click="copykefu3">
						<image src="../../../static/images/copyicon.png" mode=""></image>
					</view>
				</view> -->
				
				<!-- <view class="mainModel_item" @click="copykefu4">
					<view class="mainModel_item_left">
						<image src="../../../static/images/kefu4.jpg" mode=""></image>
					</view>
					<view class="mainModel_item_right">
						<view class="mainModel_item_right_name">
							线下充值客服-小雪
							<image class="yrzImage" src="../../../static/images/yrz.png" mode=""></image>
						</view>
						<view class="mainModel_item_right_content">
							QQ:{{kefutxnum2}}
						</view>
					</view>
					<view class="mainModel_item_copyview" @click="copykefu4">
						<image src="../../../static/images/copyicon.png" mode=""></image>
					</view>
				</view> -->
			</view>
	</u-modal>

</template>

<script>
	export default {
		//props,是父组件/页面传进来的参数
		props: {
			showModelView: {
				type: Boolean,
				default: false
			},
			kefuQQ: {
				type: String,
				default: ''
				// require: true // 必填
			},
			kefuWX: {
				type: String,
				default: ''
				// require: true // 必填
			},
			popupClose: {
				type: Boolean,
				default: false
			},
		},
		methods: {
			// 点击遮罩关闭modal，设置v-model的值为false，否则无法第二次弹起modal
			popupClose() {
				this.$emit('childEvent', false);
			},
			
		}
	}
		
</script>

<style>
	
	.mainModel{
		width: 100%;
		height: 100%;
	}
	
	
	.mainModel_item{
		
		width: 100%;
		height: 75px;
		border-bottom: 1px solid #e5e5e5;
	}
	
	.mainModel_item_left{
		width: 20%;
		float: left;
		padding-left: 10px;
		padding-top: 10px;
		
		border-radius: 5px;
	}
	
	.mainModel_item_left image{
		width: 55px;
		height: 55px;
		border-radius: 5px;
	}
	
	
	.mainModel_item_right{
		width: 60%;
		float: left;
		padding-left: 20px;
		padding-top: 10px;
	}
	
	.mainModel_item_right_name{
		padding-top: 5px;
	}
	
	.yrzImage{
		margin-left: 3px;
		width: 13px;
		height: 13px;
	}
	
	.mainModel_item_right_content{
		padding-top: 5px;
		font-size: 15px;
		color: #05CAC7;
	}
	
	
	.mainModel_item_copyview{
		width: 20%;
		padding-top: 15px;
		float: left;
		height: 55px;
		line-height: 55px;
		text-align: center;
		margin-left: -5px;
		opacity: 0.7;
		
	}
	
	.mainModel_item_copyview image{
		
		width: 25px;
		height: 25px;
	}
	
	
</style>
