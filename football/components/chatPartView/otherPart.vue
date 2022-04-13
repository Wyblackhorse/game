<template>
	
	
		<view class="playKindBet">
			<text class="playKindBet_time" >
				{{dataSrc.timeStr }}
			</text>
			
			<!-- 输入1指令返回  1275386302 乾巽 上1795 下0 回185 余795 -->

			<!-- 输入2指令返回 
				@821235888[大咖] 回复详情
				当前流动: 29082 X 2.20% = 639
				历史回复: 631
				当前回复: 639 - 631 = 8 + 剩余 657
				当前剩余: 665
			-->
			
			<!-- 历史回复  离兑离离坤艮离乾兑震巽兑巽巽震乾坎艮巽离坎坤坤乾艮兑离乾坤艮坎巽震兑震震坎坎兑艮乾巽离巽坎乾艮乾乾乾离坤坎乾兑兑兑坎巽兑 -->
				
			


			 <!-- 开奖结果  -->
			 <view class="playKindBet_content" v-if="dataSrc.id == 8888">
					<view class="playKindBet_content_image">
						<image :src="robotImg" mode=""></image>
					</view>
					
					<view class="playKindBet_content_name_text" >
						<view class="playKindBet_content_name">
							{{dataSrc.name}}
							<!-- <view class="playKindBet_content_name_timer">
								{{dataSrc.timeStr}}
							</view> -->
						</view>
						<view :class="dataSrc.name != '机器人' ? 'playKindBet_content_text':'playKindBet_content_text_robotSend'">
						<!-- <view class="playKindBet_content_text"> -->
							<view class="">
								开{{dataSrc.content | contenToBaguaStr}}
							</view>
						</view>
					</view>	
			 </view>
			 
			 <!-- 上下分回昵称余额  -->
			 <view class="playKindBet_content" v-if="dataSrc.id == 2202">
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
							<!-- <view class="">
								{{item.content}}
							</view> -->
							<text class="playKindBet_content_text_bc" >
								@[{{dataSrc.content.nickname}}]
							</text>
							<view class="" >
								
								<!-- 	<view class="">
										{{itemDe}}
									</view> -->
							
								 上<text class="playKindBet_content_text_detailContent">{{dataSrc.content.top_up | delTwoStr}}</text>下<text class="playKindBet_content_text_detailContent">{{dataSrc.content.withdraw| delTwoStr}}</text>水<text class="playKindBet_content_text_detailContent">{{dataSrc.content.back_money| delTwoStr}}</text>余<text class="playKindBet_content_text_detailContent">{{dataSrc.content.money| delTwoStr}}</text>	
							
								
								
							</view>
						</view>
					</view>	
			 </view>
			 
			 
			 <!-- 指令2 返回  -->
			 <!-- <view class="mainchat_item_robot_recordList_right"  v-else-if="item.id == 8888"> -->
			 <view class="playKindBet_content" v-if="dataSrc.id == 2203">
					<view class="playKindBet_content_image">
						<image :src="robotImg" mode=""></image>
					</view>
					
					<view class="playKindBet_content_name_text" >
						<view class="playKindBet_content_name">
							{{dataSrc.name}}
						<!-- 	<view class="playKindBet_content_name_timer">
								{{dataSrc.timeStr }}
							</view> -->
						</view>
						<view :class="dataSrc.name != '机器人' ? 'playKindBet_content_text':'playKindBet_content_text_robotSend'">			
						<!-- <view class="playKindBet_content_text"> -->
													
							<text class="playKindBet_content_text_bc" >
								@[{{dataSrc.nickname}}] 
							</text>
							<text style="margin-left: 5px;">
								回复详情
							</text>
							<view class="" >			
								<view class="">
									<!-- 2.20% -->
									当前流动: {{dataSrc.content.running}} X {{rateNumber | toPercent}} = {{dataSrc.content.currflows}}
								</view>
								<view class="">
									历史回复: {{dataSrc.content.already_running_back}}
								</view>
								<view class="">
									当前回复: {{dataSrc.content.currflows}} - {{dataSrc.content.already_running_back}} = {{dataSrc.content.runBackNum}} + 剩余 {{dataSrc.content.money}}
								</view>
								<view class="">
									当前剩余: {{dataSrc.content.remainMoney}}
								</view>
											
							</view>
						</view>
					</view>	
			 </view>
			 
			
			<!-- 系统的机器人消息,各玩家输赢结果的页面,所有人都能看到  -->
			<!-- <view class="mainchat_item_robot_recordList_right"  v-else-if="item.id == 1011"> -->
			<view class="playKindBet_content" v-if="dataSrc.id == 1011">
					<view class="playKindBet_content_image">
						<image :src="robotImg" mode=""></image>
					</view>
					
					<view class="playKindBet_content_name_text" >
						<view class="playKindBet_content_name">
							{{dataSrc.name}}
						<!-- 	<view class="playKindBet_content_name_timer">
								{{dataSrc.timeStr }}
							</view> -->
						</view>
						<view :class="dataSrc.name != '机器人' ? 'playKindBet_content_text':'playKindBet_content_text_robotSend'">
						<!-- <view class="playKindBet_content_text"> -->
							<!-- v-if="item.id == 2222  && item.username != currUserID " -->
					<!-- 		战神：-6650.00
							=== 22抽奖：离 离 坤=====
							生日快乐：乾坤巽兑坎巽坤坎巽艮震乾兑乾巽乾巽震坤坎震坤巽坎乾坎坤坎艮乾乾兑离巽离离坤巽兑震巽巽乾离离震坤坤离震坤乾艮震震兑兑坎离兑乾坎巽乾离震震坤乾坤乾巽坤巽坤离坤震震离离震震艮离坎艮巽坤艮兑震坤坤坤乾乾离巽震坤乾兑兑兑巽乾震坤离巽坤兑离震乾震兑兑兑坎坤巽乾坎震兑坤震乾艮坤坤震乾乾震坎兑艮离乾 -->
							
							<view class="" style="margin-bottom: 30px;">
								-->本轮是：{{dataSrc.consequence}}
							</view>
							<view>=======输赢统计========</view>
							<view class="" v-for="(itemC,indexC) in dataSrc.content" :key="indexC">
								<!-- {{item.content}} -->
								<!-- 11 -->
								 <view class="winlose">
									 <view class="winlose_name">
										{{itemC.name}}:
									 </view>
									 <view class="winlose_content">
										{{itemC.content}}
									 </view>
									<!-- {{itemC.name}}: 
									<text style="margin-left: 10px;">{{itemC.content}}</text> -->
								 </view>
							</view>
							<view style="margin-bottom: 10px;"></view>
							<view>=======余额统计========</view>
							<view class="" v-for="(itemC,indexC) in dataSrc.contentMoney" :key="indexC+'aaa'">
								<!-- {{item.content}} -->
								<!-- 11 -->
								<view class="moneyTotal">
									 <view class="moneyTotal_name">
										{{itemC.name}}: 
									 </view>
									 <view class="moneyTotal_content">
										{{itemC.content}}
									 </view>
									<!-- {{itemC.name}}: 
									<text style="margin-left: 10px;">{{itemC.content}}</text> -->
								</view>
							</view>
							<!-- <text>====================</text> -->
							<!-- <text>=== 22抽奖：离 离 坤=====</text> -->
							<view style="margin-bottom: 10px;"></view>
							<view>=======历史开奖========</view>
							<!-- 历史： -->
							<view class="">
								{{dataSrc.trend}}
							</view>
							<view class="">
								一律以清单为主-不争议
							</view>
						</view>
					</view>	
			</view>
			<!-- 系统的机器人消息,开奖结果的页面,所有人都能看到  -->
			<!-- <view class="mainchat_item_robot_lottery_results_right"  v-else-if="item.id == 8888"> -->
			
			<!-- 系统的机器人消息,投注账单的页面,所有人都能看到  -->
			<!-- <view class="mainchat_item_robot_recordList_right"  v-else-if="item.id == 9999"> -->
			<view class="playKindBet_content" v-if="dataSrc.id == 9999">
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
							<!-- v-if="item.id == 2222  && item.username != currUserID " -->
							<text class="">
								投注按账单为准，账单没有的一律无效不争议！！！
							</text>
							<view>================= </view>
							<view class="" v-for="(itemC,indexC) in dataSrc.content" :key="indexC">
								<!-- {{item.content}} -->
								<!-- 11 -->
								 <view class="">
									{{itemC.name}}: {{itemC.content}}
								 </view>
							</view>
							<text>===================== </text>
						</view>
					</view>	
			</view>
			
			
		</view>
		
</template>

<script>
	export default {
		name:"otherPart",
		props:{
		   dataSrc:{
			   type:Object,
			   required:true,
		   },
		   robotImg:{
			   type:String,
			   required:true
		   },
		   rateNumber:{
			   type:String,
		   }
		},
		filters:{
			timeStrFilters(value){
				// console.log("value",value);
				return value
				// return formatDateToStr(value)
			},
			delTwoStr(value){
				// console.log("value",value);
				// return value.split('.')[0]
				if(value){
					return value.split('.')[0]
				}else{
					return ''
				}
			},
			// 小数转化为百分比
			toPercent(point){
				var str=Number(point*100).toFixed(2);
				str+="%";
				return str;
			},
			contenToBaguaStr(value){  //开奖结果数字转八卦对应文字
				if(value == 1){
					return '乾'
				}else if(value == 2){
					return '巽'
				}else if(value == 3){
					return '坎'
				}else if(value == 4){
					return '艮'
				}else if(value == 5){
					return '坤'
				}else if(value == 6){
					return '震'
				}else if(value == 7){
					return '离'
				}else if(value == 8){
					return '兑'
				}
			}
		},
		data(){
			return {
				
			}
		},
		onShow() {
			
		},
		methods:{
		

		}
	}
	
	
</script>

<style>
	@import "../../pages/index/index.css";
</style>
