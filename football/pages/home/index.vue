<template>
	<view>
		<view class="homepage">
			<view class="homeNav">
				<uni-nav-bar 
					backgroundColor="#00111c"
					color="#fff"
					:border="false"
					rightWidth="0px"
				>
					<view class="navBarTitle">
						  {{showTimeStatus}}:<text class="userNameText"> {{loginUser}}</text>
				    </view>
					<view class="navBarRight">
						<view class="navBarRightContainer">
							<view class="navBarCustom">
								
							</view>
							<view class="barRightIcon" @click="navBarRightClick">
								<image src="../../static/images/menu.svg" mode=""></image>
							</view>
						</view>
						
					</view>
					 <block slot="left">
						<view class="left-icon">
						 	
						 </view>
						 <view class="left-icon">
						 	<image src="../../static/images/logo.svg" mode=""></image>
						 </view>
					</block>
				
				</uni-nav-bar>
				
				
				<view class="menuModal" v-if="menuStatus">
					<view class="menuBox">
						<view class="menuItem">
							<image src="../../static/images/icon_game.svg" mode=""></image>
							<span class="text">{{$t('home.munu.game')}}</span>
						</view>
						<view class="menuItem">
							<image src="../../static/images/icon_announcement.svg" mode=""></image>
							<span class="text">{{$t('home.munu.notice')}}</span>
						</view>
						<view class="menuItem">
							<image src="../../static/images/icon_service.svg" mode=""></image>
							<span class="text">{{$t('home.munu.server')}}</span>
						</view>
						<view class="menuItem">
							<image src="../../static/images/icon_center.svg" mode=""></image>
							<span class="text">{{$t('home.munu.center')}}</span>
						</view>
						<view class="menuItem">
							<image src="../../static/images/icon_signout.svg" mode=""></image>
							<span class="text">{{$t('home.munu.logout')}}</span>
						</view>
						<view class="menuItem">
							<view class="textBox" @click="changeLang">
								<span >{{selectLangStr}}</span>
								<view class="iconLang">
									<image 
										src="../../static/images/icon_up.svg"
										:class="langSBoxtatus ? 'down' : null"
									></image>
								</view>
								<view class="langBoxList" v-if="langSBoxtatus">
									<span 
										:class="item.active ? 'menuItemActive' : null"
										v-for="(item,index) in langBoxListData" 
										:key="index"
										@click="menuItemClick(item)"
									>{{item.lang}}</span>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="homeContent" @click="homeContentClick">
				
				<view class="outsideSwiper">
					<view class="swiperWrap">
						<swiper
							class="swiper-content" 
							interval="5000" 
							duration="1000"
							autoplay="true" 
							circular="true"
							English
						>
							<swiper-item v-for="(item, index) in swiperList" :key="item.id">
								<image class="swiper-img" :src="item.imageUrl" mode="scaleToFill"></image>
							</swiper-item>
						</swiper>
					</view>
					
					<!-- <view class="outerbox">
						<u-swiper 
							:list="swiperList"
							height="180px" 
							:effect3d="false" 
							borderRadius="10" 
							bgColor="#FFFFFF" 
							mode="none"
						></u-swiper>
					</view> -->
				</view>
				
				<view class="notice">
				<!-- 	<uni-notice-bar 
						show-icon 
						scrollable
						background-color="#132736"
						text="uni-app 版正式发布，开发一次，同时发布iOS、Android、H5、微信小程序、支付宝小程序、百度小程序、头条小程序等7大平台。" />
						 -->
					<u-notice-bar 
						border-radius="50"
						mode="horizontal" 
						:list="noticeList"
						color="#cacaca"
						speed="100"
						bg-color="#132736"
					></u-notice-bar>
					
				</view>	
				
				<view class="balance">
					<view class="balance_title">
						<image src="../../static/images/wallet.svg" mode=""></image>
						<text>{{$t('home.balance')}}</text>
					</view>
					<view class="balance_content">
						<view class="numRefresh">
							<text class="balance_money">{{balanceMoneyNum}}</text>
							<image src="../../static/images/refresh.svg" mode="" class="refresh"></image>
						</view>
						<view class="wallet_icon_btn">
							<view class="recharge">
								<image src="../../static/images/recharge.svg" mode=""  class="rechargeIcon"></image>
								<view>{{$t('home.recharge')}}</view>
							</view>
							<view class="withdraw">
								<image src="../../static/images/forcash.svg" mode=""  class="withdrawIcon"></image>
								<view>{{$t('home.withdraw')}}</view>
							</view>
						</view>
					</view>
				</view>
				
				<view class="hotMatch">
					<view class="hotMatchTitle">
						{{$t('home.hot')}}
					</view>
					<view class="hotMatchList">
						<view class="homeCard" v-for="(item,index) in matchList" :key="index">
							<view class="homeCardContent">
								<view class="cardContentDate">
									<view class="cardContentDate_close" >
										{{$t('home.close')}}: <counter :endtime='item.remainingTime' ></counter>
									</view>
									<view class="cardContentDate_time">
										{{item.startTime | timestampStr}}
									</view>
								</view>
								<view class="cardContentDesc">
									{{item.allianceName}}
								</view>
								<view class="cardContentTeam">
									<view class="cardContentTeamLeft">
										<image src="../../static/images/default.png" mode="" class="logoImg"></image>
										{{item.mainName}}
									</view>
									<view class="cardContentTeamMid">
										
									</view>
									<view class="cardContentTeamRight">
										<image src="../../static/images/default.png" mode=""></image>
										{{item.guestName}}
									</view>
								</view>
								<view class="timeBg">
									
								</view>
							</view>
						</view>
					</view>
				</view>
				
				
			</view>
		</view>
	</view>
</template>


<script>
	import {
		sliderReq,
		swiperReq,
		hotReq,
		playerInfoReq,
	} from '../../api/index.js'
	import {
		getTimeState,
		formatDateToStr,
	} from '../../common/publicTool.js'
	import counter from '../../components/counterView/counter.vue';
	export default {
		components: {
			counter
		},
		filters:{
			timestampStr(value){
				// console.log("val",formatDateToStr(value));
				return formatDateToStr(value)
			}
		},
		data() {
			return {
				selectLangStr:'English',
				menuStatus:false,
				langSBoxtatus:false,
				showTimeStatus:'',
				loginUser:'',
				langBoxListData:[
					{ 
						'lang':'English',
						'val':'en',
						'active':false,
					},
					{
						'lang':'Español',
						'val':'es',
						'active':false,
					},
					{
						'lang':'Deutsch',
						'val':'ee',
						'active':false,
					},
					{
						'lang':'Français',
						'val':'fr',
						'active':false,
					},
					{
						'lang':'Türkiye',
						'val':'tr',
						'active':false,
					},
					{
						'lang':'Italiano',
						'val':'it',
						'active':false,
					},
					{
						'lang':'中文-简体',
						'val':'cn',
						'active':false,
					},
					{
						'lang':'Português',
						'val':'pt',
						'active':false,
					},
					{
						'lang':'म',
						'val':'hi',
						'active':false,
					},
				],
				swiperList:[],
				noticeList:[],
				balanceMoneyNum:null,
				matchList:[]
				
			}
		},
		onShow(){
			
			this.localLoginToken = uni.getStorageSync('footballToken');
			
			//获取用户名和余额方法
			this.getInformation()
			
			// 获取顶部时间
			this.showCurrTimeState()
		
			//获取轮播
		    this.getSlider()
			
			// 获取公告
			this.getSwiper()
			
			//获取热门赛事
			this.getHotMatch()
		},
		mounted() {
			
	
		},
		
		methods: {
			async getInformation(){
				let resR = await playerInfoReq(this.localLoginToken)
				// console.log("resR",resR);
				this.loginUser = resR.data.username
				this.balanceMoneyNum = resR.data.balance
				
			},
			async getSlider(){ //轮播请求
				let resR = await sliderReq(this.localLoginToken)
				if(resR.code === 200){
					this.swiperList = resR.data
				}
			},
			async getSwiper(){ //滚动广告请求
				let resR = await swiperReq(this.localLoginToken)
				// console.log("resR",resR);
				if(resR.code === 200){
					let noticeArray = resR.data
					noticeArray.forEach(item=>{
						this.noticeList.push(item.content)
					})
				}
			},
			async getHotMatch(){ //获取热门赛事
				let resR = await hotReq(this.localLoginToken)
				// console.log("resR",resR.data);
				this.matchList = resR.data
			},
			showCurrTimeState(){
				let textStatus = getTimeState()
				this.showTimeStatus = this.handleTitleStatus(textStatus)
			},
			handleTitleStatus(textStr){
				switch(textStr){
					case '早上好':
						return this.$t('home.welcome_morning')
					case '中午好':
						return this.$t('home.welcome_afternoon')
					case '下午好':
						return this.$t('home.welcome_afternoon')
					case '晚上好':
						return this.$t('home.welcome_night')
					default:
						return this.$t('home.welcome_morning')
				}
			},
			navBarRightClick(){
				this.menuStatus = !this.menuStatus
				
				this.langBoxListData.forEach((item,index)=>{
					if(item.val == uni.getStorageSync('lang')){
						item.active = true
						this.selectLangStr = item.lang
					}
				})
				
				// console.log("this.langBoxListData",this.langBoxListData);
				
			},
			changeLang(){
				this.langSBoxtatus = !this.langSBoxtatus
			},
			homeContentClick(){
				this.menuStatus = false
			},
			menuItemClick(item){

				this.selectLangStr = item.lang
				this.$i18n.locale = item.val;
	
				uni.setStorageSync('lang', this.$i18n.locale)
				this.showCurrTimeState()
				
				this.langBoxListData.forEach((signle,index)=>{
					signle.active = false
					if(signle.val == item.val){
						signle.active = true
					}
				})
				
			},
			
		}
	}
</script>

<style>
	
page{
	background-color: #071a28;
}

.homepage{
	position: relative;
}

.homeNav{
	
}

.navBarTitle{
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
}

.left-icon{
	line-height: 15px;
	padding-left: 10px;
}

.left-icon image{
	width: 50px;
	height: 30px;
}

.userNameText{
	margin-left: 10px;
}

.navBarRight{
	display: flex;
	align-items: center;
	justify-content: flex-end;
	/* padding-right: 17px; */
	width: 48px;
}

.navBarCustom{
	height: 100%;
	margin-right: 10px;
	display: flex;
}

.barRightIcon{
	
}

.barRightIcon image{
	width: 27px;
	height: 27px;
}

.menuModal{
	position: relative;
	top:0;
	right: 0;
	left: 0;
	bottom: 0;
	z-index: 1000;
}

.menuBox{
	background: #334756;
	color: #fff;
	position: fixed;
	right: 10px;
	top: 40px;
	border-radius: 5px;
	display: flex;
	flex-direction: column;
	padding: 0 12px 16px 12px;
}

.menuBox::before{
	content: "";
	position: fixed;
	right: 24px;
	top: 25px;
	width: 0;
	height: 0;
	border-width: 9px;
	border-style: solid;
	border-color: transparent #334756 transparent transparent;
	transform: rotate(90deg);
}

.menuItem{
	height: 25px;
	margin-top: 10px;
	display: flex;
	flex-direction: row;
	align-items: center;
	font-size: 14px;
}

.menuItem image{
	width: 25px;
	height: 25px;
}

.menuItem text{
	margin-left: 12px;
	min-width: 90px;
	text-align: left;
}

.textBox{
	height: 25px;
	display: flex;
	align-items: center;
	background: #132736;
	padding: 0 5px;
	min-width: 120px;
	justify-content: space-between;
	position: relative;
}

.iconLang image{
	width: 17px;
	height: 9px;
	transform: rotate(180deg);
}

.iconLang .down{
	transform: rotate(0deg);
}

.langBoxList{
	position: absolute;
	background: #00111c;
	top: 25px;
	right: 0;
	display: flex;
	flex-direction: column;
	width: 100%;
	/* padding: 0 5px; */
	box-shadow: 0 3px 4px -1px rgb(0 0 0 / 25%);
}

.langBoxList span{
	color: #506778;
	display: flex;
	height: 25px;
	align-items: center;
	padding: 0 5px;
}

.langBoxList .menuItemActive{
	color: #FFFFFF;
}


.homeContent{
	position: fixed;
    padding: 5px 16px 0;
    top: 44px;
    bottom: 80px;
    overflow-y: auto;
    left: 0;
    right: 0;
    margin: 0 auto;
}

.outsideSwiper{
	border-radius: 5px;
	overflow: hidden;
}

.swiper-content{
	height:140px;
}

.swiper-img{
	width:100%;
    height:100%;
}

.notice{
	margin-top: 5px;
}

.balance{
	padding:  21px 0 0 0;
}

.balance_title{
	display: flex;
	align-items: center;
	margin-bottom: 2px;
}

.balance_title image{
	width: 22px;
	height: 22px;
	margin-right: 5px;
}

.balance_title text{
	color: #fff;
}

.refresh{
	width: 22px;
	height: 22px;
	margin-left: 9px;
	margin-right: 24px;
}

.numRefresh{
	width: 66%;
}

.balance_content{
	display: flex;
}

.balance_money{
	color: #FFFFFF;
	font-size: 24px;
	white-space: normal;
	word-break: break-all;
	word-wrap: break-word;
}

.wallet_icon_btn{
	display: flex;
	position: relative;
	top:-16px
}


.recharge{
	margin-right: 30px;
}

.recharge,.withdraw {
	text-align: center;
}

.recharge,.withdraw view{
	color: #FFFFFF;
	font-size: 12px;
}

.rechargeIcon{
   width: 34px;
   height: 34px;
}

.withdrawIcon{
   width: 34px;
   height: 34px;
}


.hotMatchTitle{
	color: #FFFFFF;
}

.hotMatchList{
	margin-top: 9px;
	padding-bottom: 20px;
}

.homeCard{
	position: relative;
	display: flex;
	margin: 3px 0 15px 0;
	padding: 6px 12px;
	background: #132736 url(../../static/images/list_bg.png) no-repeat top;
	background-size: 100%;
	border-radius: 10px;
	overflow: hidden;
}


.homeCardContent{
	color: #333;
	font-size: 15px;
	flex: 1;
}

.cardContentDate{
	display: flex;
	justify-content: space-between;
	align-items: center;
	
}

.cardContentDate_close{
	font-size: 12px;
	color: #fff;
	position: inherit;
	z-index: 10;
}

.cardContentDate_time{
	display: flex;
	align-items: center;
	font-size: 12px;
	line-height: 12px;
	color: #fff;
}

.cardContentDesc{
	font-family: Hiragino Sans GB;
	font-weight: 600;
	color: #fff;
	padding-top: 20px;
	padding-bottom: 12px;
	font-size: 14px;
	text-align: center;
	max-height: 47px;
}

.cardContentTeam{
	display: flex;
	padding-top: 25px;
	padding-bottom: 10px;
	align-items: center;
	font-weight: 500;
	font-size: 12px;
	color: #fff;
}

.cardContentTeamLeft{
	font-size: 12px;
	width: 43%;
	text-align: center;
}

.cardContentTeamLeft image{
	display: block;
	margin: 0 auto 10px;
	width: 36px;
	height: 36px;
	border-radius: 50px;
}

.cardContentTeamRight{
	font-size: 12px;
	width: 43%;
	text-align: center;
}

.cardContentTeamRight image{
	display: block;
	margin: 0 auto 10px;
	width: 36px;
	height: 36px;
	border-radius: 50px;
}

.cardContentTeamMid{
	width: 40px;
	height: 40px;
	text-align: center;
	font-size: 14px;
	background: url(../../static/images/vs.png);
	background-size: 100%;
}


.timeBg{
	position: absolute;
	left: 0;
	top: 0;
	background: linear-gradient(-90deg,rgba(128,90,0,0),#ffb300);
	height: 26px;
	width: 40%;
}

</style>
