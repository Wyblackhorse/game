<template>
	<view>
		<view class="macthPage">
			<view class="search">
				<uni-search-bar 
					placeholder="自定义背景色" 
					bgColor="#132736"
					@confirm="search" 
					cancelButton="none"
					radius="20"
					style="color: #FFFFFF"
				 />
			</view>
			<view>
				<view>
					<u-tabs-swiper 
						ref="uTabs" 
						:list="list" 
						:current="current" 
						@change="tabsChange" 
						:is-scroll="false"
						:bold="false"
						swiperWidth="750"
						bg-color="#071a28"
						font-size="15"
						bar-width="60"
						inactive-color="#FFFFFF"
						active-color="#FFB300"
					></u-tabs-swiper>
				</view>
				<swiper :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish">
					<!-- <swiper-item class="swiper-item" v-for="(item, index) in tabs" :key="index">
						<scroll-view scroll-y style="height: 800rpx;width: 100%;" @scrolltolower="onreachBottom">
							...
						</scroll-view>
					</swiper-item> -->
				</swiper>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
					name: '所有(168)'
				}, {
					name: '今日(83)'
				}, {
					name: '昨日(85)'
				}],
				// 因为内部的滑动机制限制，请将tabs组件和swiper组件的current用不同变量赋值
				current: 0, // tabs组件的current值，表示当前活动的tab选项
				swiperCurrent: 0, // swiper组件的current值，表示当前那个swiper-item是活动的
			}
		},
		components:{
			
		},
		methods: {
			// tabs通知swiper切换
			tabsChange(index) {
				console.log("index",index);
				this.swiperCurrent = index;
			},
			// swiper-item左右移动，通知tabs的滑块跟随移动
			transition(e) {
				let dx = e.detail.dx;
				this.$refs.uTabs.setDx(dx);
			},
			// 由于swiper的内部机制问题，快速切换swiper不会触发dx的连续变化，需要在结束时重置状态
			// swiper滑动结束，分别设置tabs和swiper的状态
			animationfinish(e) {
				let current = e.detail.current;
				this.$refs.uTabs.setFinishCurrent(current);
				this.swiperCurrent = current;
				this.current = current;
			},
			// scroll-view到底部加载更多
			onreachBottom() {
				
			}
		}
	}
</script>

<style>
	
page{
	background-color: #071a28;
}

.macthPage{
	display: flex;
	flex-direction: column;
	
}

.search{
	margin: 15px 15% 0 15%;	
}


</style>
