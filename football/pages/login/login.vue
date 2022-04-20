<template>
	<view>
		
		<view class="langView">
			<view class="currLang">
				<view class="currImg" @click="showLangsClick">
					<image :src="currSelectLangImg" mode=""></image>
					<view class="currText">
						{{currSelectLang}}
					</view>
				</view>
				<view class="currIconDownView" @click="showLangsClick">
					<i :class="LangsBool ? 'currIconDown isRotate':'currIconDown' "></i>
				</view>
				<view class="langs" v-if="LangsBool">
					<view :class="item.status === 1 ? 'langItem langActive' : 'langItem' "  v-for="(item,index) in langList" :key="index" @click="setLangClick(item)">
						<view class="langItemImg">
							<image :src="item.url" mode=""></image>
						</view>
						<view class="langItemContent">
							{{item.lang}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="wrap">
			<view class="wrapContent">
				<view class="logo">
					<image src="../../static/images/logo2.svg" mode=""></image>
				</view>
				<!-- 登录页面 -->
				<view class="loginContent" v-if="loginStatus">
					<view class="">
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/account.svg" mode=""></image>
							</view>
							<view class="mainInput">
								<input 
									type="text" 
									value="" 
									:placeholder="$t('login.account.place')"
									v-model="loginAccount"
									@input="inputUserPass"
								/>
							</view>
						</view>
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/password.svg" mode=""></image>
							</view>
							<view class="type">
								<view class="mainInput">
									<input 
										type="pass" 
										value="" 
										:placeholder="$t('login.pass.place')" 
										:password="passwordBool" 
										maxlength="20"
										v-model="loginPassword"
										@input="inputUserPass"
									/>
								</view>
								<view class="hidePassImg" @click="loginSeePassClick('login')">
									<image :src="srcImg" mode=""></image>
								</view>
							</view>
						</view>
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/verification.svg" mode=""></image>
							</view>
							<view class="type">
								<view class="mainInput">
									<input 
										type="text" 
										value="" 
										:placeholder="$t('login.vercode.place')" 
										maxlength="6"
										v-model="loginVercode"
										@input="inputUserPass"
									/>
								</view>
								<view class="inviteImg" @click="getCodeData">
									<image :src="vercodeImg" mode=""></image>
								</view>
							</view>
						</view>
					</view>
			
					<view :class="changeBtnColor ?'loginBtn active': 'loginBtn'" @click="loginRegfBigBtn">
						{{$t('login.btntitle')}}
					</view>
					<view class="regBtn" @click="changeLoginRegView">
						{{$t('reg.btntitle')}}
					</view>
					<view class="container">
						<view class="top">
							<view class="">
								
							</view>
							<view class="">
								{{$t('login.downtitle')}}
							</view>
							<view class="">
								
							</view>
						</view>
						<view class="twoIcon">
							<view class="module">
								<view class="imgPic">
									<image src="../../static/images/server.svg" mode=""></image>
								</view>
								<view class="contentText">
									{{$t('login.chattitle')}}
								</view>
							</view>
							<view class="module">
								<view class="imgPic">
									<image src="../../static/images/download.svg" mode=""></image>
								</view>
								<view class="contentText">
									{{$t('login.apptitle')}}
								</view>
							</view>
						</view>
					</view>
				</view>
				
				<!-- 注册页面 -->
				<view class="loginContent" v-if="!loginStatus">
					<view class="">
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/account.svg" mode=""></image>
							</view>
							<view class="mainInput">
								<input 
									type="text" 
									value="" 
									:placeholder="$t('reg.account.place')"
									v-model="regAccount"
									maxlength="16"
									@input="regAccoutChangeInput"
								/>
							</view>
						</view>
						<view class="alterTip" v-if="accountAlterTipBool">
							{{accountAlterTip}}
						</view>
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/password.svg" mode=""></image>
							</view>
							<view class="type">
								<view class="mainInput">
									<input 
										type="pass" 
										value="" 
										:placeholder="$t('reg.pass.place')" 
										:password="passwordRegBool" 
										maxlength="16"
										v-model="regPassword"
										@input="regPassChangeInput"
									/>
								</view>
								<view class="hidePassImg" @click="loginSeePassClick('reg')">
									<image :src="srcRegImg" mode=""></image>
								</view>
							</view>
						</view>
						<view class="alterTip" v-if="passAlterTipBool">
							{{passAlterTip}}
						</view>
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/password.svg" mode=""></image>
							</view>
							<view class="type">
								<view class="mainInput">
									<input 
										type="pass" 
										value="" 
										:placeholder="$t('reg.passconfirm.place')" 
										:password="passwordConfirmBool" 
										maxlength="16"
										v-model="regConfirmPassword"
										@input="regPassTChangeInput"
									/>
								</view>
								<view class="hidePassImg" @click="loginSeePassClick('regConf')">
									<image :src="srcConfImg" mode=""></image>
								</view>
							</view>
						</view>
						<view class="alterTip" v-if="passTAlterTipBool">
							{{passTAlterTip}}
						</view>
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/inviteicon.svg" mode=""></image>
							</view>
							<view class="mainInput">
								<input 
									type="text" 
									value="" 
									:placeholder="$t('reg.invitecode.place')"
									v-model="regInviteCode"
									@input="regInvChangeInput"
								/>
							</view>
						</view>
						<view class="item">
							<view class="loginImage">
								<image src="../../static/images/verification.svg" mode=""></image>
							</view>
							<view class="type">
								<view class="mainInput">
									<input 
										type="text" 
										value="" 
										:placeholder="$t('login.vercode.place')" 
										maxlength="4"
										v-model="regImgCode"
										@input="regVerChangeInput"
									/>
								</view>
								<view class="inviteImg" @click="getCodeData">
									<image :src="vercodeImg" mode=""></image>
								</view>
							</view>
						</view>
					</view>
					<view class="alterTip" v-if="verAlterTipBool">
						{{verAlterTip}}
					</view>
					<view class="loginSmallBtn" @click="changeLoginRegView">
						{{$t('login.btntitle')}}
					</view>	
					<view :class="changeRegBtnColor ?'loginBtn regBigBtn active': 'loginBtn regBigBtn'" @click="loginRegfBigBtn">
						{{$t('reg.btntitle')}}
					</view>
				</view>
			</view>
		</view>
		
		
		
		<u-top-tips 
			ref="uTips" 
			:navbar-height="statusBarHeight + navbarHeight"
		></u-top-tips>
		
	</view>
</template>
<script>
	import {
		loginReq,
		verifyCodeReq,
		registerReq,
		verifyCodeCheckReq,
	} from '../../api/index.js'
	import wybLoading from '@/components/wyb-loading/wyb-loading.vue'
	// import { setClipboardData, getClipboardData } from '@/uni_modules/u-clipboard/js_sdk'
	export default {
		data() {
			return {
				srcImg:'../../static/images/nosee.svg',
				srcRegImg:'../../static/images/nosee.svg',
				srcConfImg:'../../static/images/nosee.svg',
				passwordBool:true,
				passwordRegBool:true,
				passwordConfirmBool:true,
				loginAccount:'',
				loginPassword:'',
				loginVercode:'',
				vercodeImg:'null',
				changeBtnColor:false,
				LangsBool:false,
				currSelectLangImg:"../../static/images/lang_en.svg",
				currSelectLang:'CN',
				langList:[
					{
					  lang:"EN",
					  type:"EN",
					  url:"../../static/images/lang_en.svg",
					  status:1,
					},
					{
					  lang:"CN",
					  type:"CN",
					  url:"../../static/images/lang_zh.svg",
					  status:0,
					},
					{
					  lang:"HI",
					  type:"HI",
					  url:"../../static/images/lang_hi.svg",
					  status:0,
					},
					{
					  lang:"PT",
					  type:"PT",
					  url:"../../static/images/lang_pt.svg",
					  status:0,
					},
					{
					  lang:"ES",
					  type:"ES",
					  url:"../../static/images/lang_es.svg",
					  status:0,
					},
					{
					  lang:"DE",
					  type:"DE",
					  url:"../../static/images/lang_de.svg",
					  status:0,
					},
					{
					  lang:"FR",
					  type:"FR",
					  url:"../../static/images/lang_fr.svg",
					  status:0,
					},
					{
					  lang:"TR",
					  type:"TR",
					  url:"../../static/images/lang_tr.svg",
					  status:0,
					},
					{
					  lang:"IT",
					  type:"IT",
					  url:"../../static/images/lang_it.svg",
					  status:0,
					},
				],
				langList2:[
					{
					  lang:"en",
					  type:"en",
					  url:"../../static/images/lang_en.svg"
					},
					{
					  lang:"zh",
					  type:"zh",
					  url:"../../static/images/lang_zh.svg"
					},
					{
					  lang:"hi",
					  type:"hi",
					  url:"../../static/images/lang_hi.svg"
					},
					{
					  lang:"pt",
					  type:"pt",
					  url:"../../static/images/lang_pt.svg"
					},
					{
					  lang:"es",
					  type:"es",
					  url:"../../static/images/lang_es.svg"
					},
					{
					  lang:"de",
					  type:"de",
					  url:"../../static/images/lang_de.svg"
					},
					{
					  lang:"fr",
					  type:"fr",
					  url:"../../static/images/lang_fr.svg"
					},
					{
					  lang:"tr",
					  type:"tr",
					  url:"../../static/images/lang_tr.svg"
					},
					{
					  lang:"it",
					  type:"it",
					  url:"../../static/images/lang_it.svg"
					},
				],
				loginStatus:true,
				regAccount:'',
				regPassword:'',
				regConfirmPassword:'',
				regInviteCode:'',
				regImgCode:'',
				changeRegBtnColor:false,
				loginVercodehash:'',
				// 状态栏高度，H5中，此值为0，因为H5不可操作状态栏
				statusBarHeight: uni.getSystemInfoSync().statusBarHeight,
				// 导航栏内容区域高度，不包括状态栏高度在内
				navbarHeight: 0,
				accountAlterTip:'123',
				passAlterTip:'4234',
				passTAlterTip:'32423',
				verAlterTip:'324234',
				accountAlterTipBool:false,
				passAlterTipBool:false,
				passTAlterTipBool:false,
				verAlterTipBool:false,
				
				loginText:'登录',
				loginDesc:'请填写账号密码进行登录',
				tabLoginText:'切换注册',
			
				
				verifyCodeImg:'',
				
				regNickName:'',
				regQqNum:'',
				regCode:'',
				
				currAlterMsg:'',
				currHideStatus:1,//1不显示,2显示
				
				
				
			}
		},
		onLoad() {
			// this.getCodeData();
		},
		mounted(){
			
			// changeBtnColor:{
			// 	console.log("this.loginAccount",this.loginAccount);
			// 	console.log("this.loginPassword",this.loginPassword);
			// 	if(this.loginAccount && this.loginPassword){
			// 		console.log("1123123");
			// 		return true
			// 	}else{
			// 		console.log("ces");
			// 		return false
			// 	}
			// }
		},
		onShow() {
			
			// // #ifdef H5
			// 	document.title = "七喜娱乐"
			// // #endif
			
			this.currSelectLang = (uni.getStorageSync('lang') ? uni.getStorageSync('lang') : 'en').toUpperCase()
			if(this.currSelectLang !== 'CN' ){
				this.currSelectLangImg = '../../static/images/lang_'+this.currSelectLang.toLowerCase()+'.svg'
			}else{
				this.currSelectLangImg = '../../static/images/lang_zh.svg'
			}
			
			this.getCodeData();
			
			// console.log("uni.getStorageSync('isLoginKey')",uni.getStorageSync('isLoginKey'));
			
			// 先获取本地缓存的账号密码
			// this.loginAccount = uni.getStorageSync('login_account')
			// this.loginPassword = uni.getStorageSync('login_password');
				
			
			
			
	// 		if(uni.getStorageSync('isLoginKey')){
				
	// 			// // 如果已经登录,主动调
	// 			// uni.navigateTo({
	// 			// 	   url: '/pages/index/index',
	// 			// 	   animationType: 'slide-in-top',
	// 			// 	   animationDuration: 200
	// 			// })
				
	// 		}else{
	// 			// console.log("登录状态失效了,请重新登录");
				
	// 			// 先获取本地缓存的账号密码
	// 			this.loginAccount = uni.getStorageSync('login_account')
	// 			this.loginPassword = uni.getStorageSync('login_password');
	
	// 			this.getCodeData();
	// 		}
			
			
		},
		methods: {
			loginSeePassClick(str){  //显示密码和隐藏密码(3个共用)
				let imgLeftName = "../../static/images/see.svg"
				let imgRightName = "../../static/images/nosee.svg"
				switch (str) {
				  case 'login':  //登录页面中密码的显示
					//如果为真，则代表现在密码是隐藏状态,需改变图片为开眼状态
					this.srcImg = this.passwordBool ? imgLeftName:imgRightName
					this.passwordBool = !this.passwordBool;
				    break;
				  case 'reg':  //注册页面中密码的显示
					this.srcRegImg = this.passwordRegBool ? imgLeftName:imgRightName
					this.passwordRegBool = !this.passwordRegBool;
					break;
				  case 'regConf':  //注册页面中确认密码的显示
					this.srcConfImg = this.passwordConfirmBool ? imgLeftName:imgRightName
					this.passwordConfirmBool = !this.passwordConfirmBool;
				    break;
				  default:
				   
				}
			},
			inputUserPass(){ //登录页面输入框改变监听事件
				if(this.loginAccount && this.loginPassword && this.loginVercode){
					this.changeBtnColor =  true
				}else{
					this.changeBtnColor =  false
				}
			},
		    regAccoutChangeInput(){
				this.commonRuls()
				
				if(!this.regAccount){
					this.accountAlterTip = this.$t('reg.ruls.accout.empty')
					this.accountAlterTipBool = true
				}else if(this.regAccount.length < 5 ){
					this.accountAlterTip = this.$t('reg.ruls.accout.length')
					this.accountAlterTipBool = true
				}else{
					this.accountAlterTip = ""
					this.accountAlterTipBool = false
				}
				
			},
		    regPassChangeInput(){
				this.commonRuls()	
				if(!this.regPassword){
					this.passAlterTip = this.$t('reg.ruls.pass.empty')
					this.passAlterTipBool = true
				}else if(this.regPassword.length < 6 ){
					this.passAlterTip = this.$t('reg.ruls.pass.length')
					this.passAlterTipBool = true
				}else{
					this.passAlterTip = ""
					this.passAlterTipBool = false
				}
				
			},
			regPassTChangeInput(){
				this.commonRuls()	
				if(!this.regConfirmPassword){
					this.passTAlterTip = this.$t('reg.ruls.passtwo.empty')
					this.passTAlterTipBool = true
				}else if(this.regConfirmPassword.length < 6 ){
					this.passTAlterTip = this.$t('reg.ruls.passtwo.length')
					this.passTAlterTipBool = true
				}else if(this.regConfirmPassword !== this.regPassword ){
					this.passTAlterTip = this.$t('reg.ruls.passtwo.unequal')
					this.passTAlterTipBool = true
				}else{
					this.passTAlterTip = ""
					this.passTAlterTipBool = false
				}
			},
			regInvChangeInput(){
				this.commonRuls()
				
			},
			async regVerChangeInput(){
				
				this.commonRuls()
				
				if(!this.regImgCode){
					this.verAlterTip = this.$t('reg.ruls.vercode.empty')
					this.verAlterTipBool = true
				}else if( this.regImgCode && this.regImgCode.length === 4){ // 检验验证码
					let resR = await verifyCodeCheckReq(this.loginVercodehash,this.regImgCode)
					// console.log("resR",resR);
					if(resR.code !== 200){
						this.verAlterTipBool = true
						this.verAlterTip = resR.msg
					}else{
						this.verAlterTipBool = false
						this.verAlterTip = ""
					}
					
				}else{
					this.verAlterTipBool = false
					this.verAlterTip = ""
				}
				
			},
			
			commonRuls(){
				
				if(this.regAccount && this.regPassword && this.regConfirmPassword && this.regInviteCode && this.regImgCode){
					this.changeRegBtnColor =  true
				}else{
					this.changeRegBtnColor =  false
				}
				
			},
			showLangsClick(){  //显示或者隐藏语言列表点击事件
				this.LangsBool = !this.LangsBool
				
				let currLangLists = [...this.langList]
				let obj = {};
				currLangLists.filter((item,index)=>{
					// console.log("item.type.toLowerCase()",this.currSelectLang.toLowerCase());
					if(item.type === this.currSelectLang){
							item.status = 1
					}else{
						item.status = 0
					}
					
					if(item.status === 1){
						obj = item;
						currLangLists.splice(index,1)
						// return;
					}
										
					return item
				})
				
				// currLangLists.forEach((item,index)=>{
				// 	if(item.status === 1){
				// 		obj = item;
				// 		currLangLists.splice(index,1)
				// 		return;
				// 	}
				 
				// })
				 
				currLangLists.unshift(obj);
				this.langList = currLangLists

			},
			setLangClick(item){  //选择某个语言进行设置的点击方法
				
				// console.log("item",item);
				// e的参数zh-Hans // en 这种应用能重启生效 要不然用this.$i18n.locale = 'zh-Hans'
				if(item.type.toLowerCase() !== this.$i18n.locale){
					// uni.setLocale(item.type.toLowerCase());
					this.$i18n.locale = item.type.toLowerCase() ;
					uni.setStorageSync('lang', this.$i18n.locale)
				}		
				
				this.currSelectLang = item.type.toUpperCase()
				this.currSelectLangImg = item.url
				this.LangsBool = false
				
			},
			async loginRegfBigBtn(){ //注册点击事件
				if(this.loginStatus){ //点击登录页面中登录按钮
					
					if(this.loginAccount == ""){
						
						uni.showToast({
							title: this.$t('login.ruls.accout.empty'),
							icon:"none",
							duration: 2000
						});
					
						
						return false
					}
					
					if(this.loginPassword == ""){
						
						uni.showToast({
							title: this.$t('login.ruls.pass.empty'),
							icon:"none",
							duration: 2000
						});
						
						return false
					}
													
					if(this.loginVercode == ""){
						
						uni.showToast({
							title: this.$t('login.ruls.vercode.empty'),
							icon:"none",
							duration: 2000
						});
						
						return false
					}
					// this.$refs.loading.showLoading() // 显示
					uni.showLoading({
					    title: '登录中...',
					})
					let reqParam = {}
					reqParam.username = this.loginAccount
					reqParam.password = this.loginPassword
					reqParam.code = this.loginVercode,
					reqParam.verifyKey= this.loginVercodehash
					let loginRes = await loginReq(reqParam)
					this.currAlterMsg = loginRes.msg
					// this.$refs.loading.hideLoading() // 隐藏
					// console.log("loginRes",loginRes);
					this.loginVercode = ""
					this.loginVercodehash = ""
					if(loginRes.code === 200){
						
					
					// 	this.currHideStatus = 1
					
					// 	uni.setStorageSync('login_account', this.loginAccount);
					// 	uni.setStorageSync('login_password', this.loginPassword);
					
					// 	uni.setStorageSync('tokenClientH5', loginRes.result.token);
					// 	uni.setStorageSync('isLoginKey', 777);		
						uni.setStorageSync('footballUser', loginRes.data.user);	
						uni.setStorageSync('footballToken', loginRes.data.token);	
					// 	uni.setStorageSync('nickname', loginRes.result.nickname);				
					// 	uni.setStorageSync('lv', loginRes.result.consumption_level_id);			
					// 	uni.setStorageSync('head_image', loginRes.result.head_image);			
					// 	// uni.setStorageSync('money', loginRes.result.money);				
					// 	uni.setStorageSync('UID', loginRes.result.id);			
					// 	uni.setStorageSync('created_at', loginRes.result.created_at);
					// 	uni.setStorageSync('loginip', loginRes.result.this_ip);
						
					// 	if(loginRes.result.pay_password === null){
					// 		uni.setStorageSync('isSetPay', 0);
					// 	}else{
					// 		uni.setStorageSync('isSetPay', 1);
					// 	}
					
						// this.$refs.loading.hideLoading() // 隐藏
						setTimeout(()=>{
							uni.hideLoading();
							
							this.$refs.uTips.show({
								title: loginRes.msg,
								type: 'success',
								duration: '2000'
							})
							
							// uni.reLaunch({
							// 	   url: '/pages/index/index',
							// 	   animationType: 'slide-in-top',
							// 	   animationDuration: 200
							// })
							
							// window.location.reload();
							
						}, 2000);
						
					}else{
					    uni.hideLoading();
						this.$refs.uTips.show({
							title: loginRes.msg,
							type: 'error',
							duration: '2000'
						})
						this.loginVercode = ""
						this.getCodeData()
					
					
					}
									
					
					
					
				}else{//注册
					
					
					// if(this.regAccount == ""){
						
					// 	uni.showToast({
					// 		title: this.$t('reg.ruls.accout.empty'),
					// 		icon:"none",
					// 		duration: 2000
					// 	});
						
					// 	return false
					// }
					
					if(this.regAccount && this.regPassword && this.regConfirmPassword && this.regImgCode && this.loginVercodehash){
						let currRegData = {
							username: this.regAccount,
							password:this.regPassword,
							twoPassword :this.regConfirmPassword,
							code :this.regImgCode,
							verifyKey :this.loginVercodehash,
						}
						let registerRes = await registerReq(currRegData)
						
						if(registerRes.code === 200){
							
							this.$refs.uTips.show({
								title: registerRes.msg,
								type: 'success',
								duration: '1000'
							})
							
							//延迟跳转
							setTimeout(()=>{
								
								this.getCodeData()
								this.regImgCode = ""
								this.loginStatus = true
								
							}, 1300);
							
							
						}else{
							this.$refs.uTips.show({
								title: registerRes.msg,
								type: 'error',
								duration: '1000'
							})
							
							this.getCodeData()
							
						}
										
					}
					
					
				
				
				
				}
			},
			changeLoginRegView(){ //切换登录和注册视图点击事件
				this.loginStatus = !this.loginStatus
				this.getCodeData()
			},
			
			async getCodeData(){ //获取验证码方法
				this.loginVercodehash = ""
				this.loginVercode = ""
				this.regImgCode = ""
				let verifyRes = await verifyCodeReq(new Date().getTime())
				// console.log("verifyRes",verifyRes);
				if(verifyRes.code === 200){
					this.vercodeImg = verifyRes.data.img
					this.loginVercodehash = verifyRes.data.verifyKey
				}else{
					this.$refs.uTips.show({
						title: verifyRes.msg,
						type: 'error',
						duration: '2000'
					})
				}
			},
			tabLoginClick(){  //切换登录注册页面
				if(this.loginStatus === 'login'){
					
					this.loginText='注册'
					this.loginDesc ='请填写相关信息进行注册'
					this.loginStatus='reg'
					this.tabLoginText='切换登录'
					this.getCodeData()
					
				}else if(this.loginStatus === 'reg'){
					
					this.loginText='登录'
					this.loginDesc ='请填写账号密码进行登录'
					this.loginStatus='login'
					this.tabLoginText='切换注册'
					this.getCodeData()
				}
			},
			getCodeClick(){//点击验证码图片更新验证码
				// console.log("获取验证码点击")
				this.getCodeData();
			},
			hideloadfunction(){//隐藏后的关闭事件
				
				
				if(this.currHideStatus == 1){//不显示
					
					uni.showToast({
						title: this.currAlterMsg,
						duration: 2000
					});
					
					
					
				}else{//显示错误弹窗
					
					
					this.$refs.uTips.show({
						title: this.currAlterMsg,
						type: 'error',
						duration: '2000'
					})
				}
				
			},
			async loginRegBtn(){ //登录和注册点击事件
			
				if(this.loginStatus === 'login'){
					// console.log("登录",this.loginAccount,this.loginPassword);
	
					if(this.loginAccount == ""){
						
						// uni.showToast({
						// 	title: "用户名不能为空！",
						// 	icon:"none",
						// 	duration: 2000
						// });
						
						this.$refs.uTips.show({
							title:  "用户名不能为空！",
							type: 'error',
							duration: '2000'
						})
						
						return false
					}
					
					if(this.loginPassword == ""){
						
						// uni.showToast({
						// 	title: "密码不能为空！",
						// 	icon:"none",
						// 	duration: 2000
						// });
						
						this.$refs.uTips.show({
							title:  "密码不能为空！",
							type: 'error',
							duration: '2000'
						})
						
						return false
					}
													
					if(this.loginVercode == ""){
						
						// uni.showToast({
						// 	title: "验证码不能为空！",
						// 	icon:"none",
						// 	duration: 2000
						// });
						
						this.$refs.uTips.show({
							title:  "验证码不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					// this.$refs.loading.showLoading() // 显示
					uni.showLoading({
					    title: '登录中...',
					})
					let reqParam = {}
					reqParam.username = this.loginAccount
					reqParam.password = this.loginPassword
					reqParam.verifyCode= this.loginVercode,
					reqParam.verifyHash= this.loginVercodehash
					// reqParam.verifyCode= "fgo4"
					// reqParam.verifyHash= "8094b70b8778cbc03965d707cdf721c4"
					let loginRes = await loginReq(reqParam)
					this.currAlterMsg = loginRes.msg
					// this.$refs.loading.hideLoading() // 隐藏
					// console.log("loginRes",loginRes);
					if(loginRes.code === 200){

						this.currHideStatus = 1

						uni.setStorageSync('login_account', this.loginAccount);
						uni.setStorageSync('login_password', this.loginPassword);

						uni.setStorageSync('tokenClientH5', loginRes.result.token);
						uni.setStorageSync('isLoginKey', 777);		
						uni.setStorageSync('name', loginRes.result.username);	
						uni.setStorageSync('nickname', loginRes.result.nickname);				
						uni.setStorageSync('lv', loginRes.result.consumption_level_id);			
						uni.setStorageSync('head_image', loginRes.result.head_image);			
						// uni.setStorageSync('money', loginRes.result.money);				
						uni.setStorageSync('UID', loginRes.result.id);			
						uni.setStorageSync('created_at', loginRes.result.created_at);
						uni.setStorageSync('loginip', loginRes.result.this_ip);
						
						if(loginRes.result.pay_password === null){
							uni.setStorageSync('isSetPay', 0);
						}else{
							uni.setStorageSync('isSetPay', 1);
						}
					
						// this.$refs.loading.hideLoading() // 隐藏
						setTimeout(function () {
							uni.hideLoading();
							uni.reLaunch({
								   url: '/pages/index/index',
								   animationType: 'slide-in-top',
								   animationDuration: 200
							})
							
							// window.location.reload();
							
						}, 2000);
						
					}else{
					    uni.hideLoading();
						this.$refs.uTips.show({
							title: loginRes.msg,
							type: 'error',
							duration: '2000'
						})
						this.loginVercode = ""
						this.getCodeData()
						
						// this.$refs.uTips.show({
						// 	title: loginRes.msg,
						// 	type: 'error',
						// 	duration: '2000'
						// })
						
						// this.currAlterMsg = loginRes.msg
						this.currHideStatus = 2
						// this.$refs.loading.hideLoading() // 隐藏
					
					}
				
				}else{
					// console.log("注册");

					if(this.regAccount == ""){
				
						this.$refs.uTips.show({
							title:  "账号不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
					if(this.regPassword == ""){
						
						this.$refs.uTips.show({
							title: "密码不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
					if(this.regConfirmPassword == ""){
						
					
						this.$refs.uTips.show({
							title: "确认密码不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
		
					if(this.regPassword != this.regConfirmPassword){
						
						
						this.$refs.uTips.show({
							title: "两次密码不一致！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
					if(this.regNickName == ""){
						
						
						this.$refs.uTips.show({
							title: "昵称不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
					
				
					
					
					if(this.regQqNum == ""){
						
						this.$refs.uTips.show({
							title: "QQ号不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
					if(this.regCode == ""){
						
						
						this.$refs.uTips.show({
							title: "验证码不能为空！",
							type: 'error',
							duration: '2000'
						})
						return false
					}
					
					let currRegData = {
						qq_nums:this.regQqNum,
						username: this.regAccount,
						password:this.regPassword,
						password1:this.regConfirmPassword,
						verifyCode:this.regCode,
						verifyHash:this.loginVercodehash,
						nickname:this.regNickName,
					   }
					let registerRes = await registerReq(currRegData)
					
					if(registerRes.code === 200){
						
						this.$refs.uTips.show({
							title: registerRes.msg,
							type: 'success',
							duration: '1000'
						})
						
						//延迟跳转
						setTimeout(()=>{
							
							this.getCodeData()
							this.regCode = ""
							this.loginStatus = 'login'
							
						}, 1300);
						
						
					}else{
						this.$refs.uTips.show({
							title: registerRes.msg,
							type: 'error',
							duration: '1000'
						})
						
						this.getCodeData()
						
					}
					
					
					
				}
				
			}
		
		
		
		
		}
	}
</script>

<style>
	
	page{
		/* box-shadow: 0 0 8px 2px rgb(0 0 0 / 20%); */
		width: 100%;
		height: 100%;
		position: relative;
		background: url("../../static/images/login-bg.jpg") no-repeat;
		background-size: 100%;
		background-attachment: fixed;
	}
	
	.langView{
		position: fixed;
		left: 15px;
		top: 15px;
		z-index: 1000;
		color: #fff;
		height: 27px;
		display: flex;
		justify-content: center;
		align-items: center;
		border: 1px solid #334756;
		width: 75px;
		border-radius: 3px;
		font-size: 12px;
	}

	.currLang{
		display: flex;
		flex-direction: row;
	}
	
	.currImg{
		display: flex;
		flex-direction: row;
		align-items: center;
		margin-right: 30px;
	}
	
	.currImg image{
		width: 18px;
		height: 18px;
		margin: 0 5px;
	}
	
	.currIconDownView{
		background: #334756;
		position: absolute;
		top: 0;
		right: 0;
		height: 100%;
		width: 22px;
		display: flex;
		align-items: center;
	}
	
	.currIconDown{
		margin-left: 4px;
		transform: translate(2px,2px) rotate(-180deg);
		display: inline-block;
		width: 0;
		height: 0;
		border-width: 0 6px 8px;
		border-style: solid;
		border-color: transparent transparent #ffb300;
	}
	
	.isRotate{
		transform: rotate(0deg);
	}
	
	.wrap{
		position: absolute;
		left: 0;
		right: 0;
		text-align: center;
	}
	
	.wrapContent{
		width: 90%;
		margin: 0 20px;
		margin-top: 35px;
		border-radius: 8px;
		padding: 5px 0 10px;
	}
	
	
	.logo{
		text-align: center;
		margin-top: 40px;
	}
	
	.logo image{
		height: 80px;
	}
	
	.item{
		margin: 0 20px;
		margin-top: 20px;
		display: flex;
		align-items: center;
		padding: 0 20px;
		border-radius: 10px;
		background: rgba(51,71,86,.6);
		height: 47px;
		position: relative;
	}
	
	.alterTip{
		text-align: left;
		margin-top: 10px;
		margin-left: 35px;
		font-size: 12px;
		/* font-weight: bold; */
		color: red;
	}
	
	.loginImage{
		max-width: 22px;
		max-height: 22px;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.mainInput{
		font-size: 14px;
		margin-left: 16px;
		height: 30px;
		line-height: 30px;
		flex: 1;
		text-align: left;
		background: transparent;
		border: none;
		color: #fff;
	}
	
	.mainInput input{
		display: flex;
		position: relative;
		width: 100%;
		height: 100%;
		flex-direction: column;
		justify-content: center;
		font-size: 12px;
		font-family: Hiragino Sans GB;
		font-weight: 400;
	}
	
	.type{
		display: flex;
		width: 100%;
		align-items: center;
		justify-content: space-between;
	}
	
	.hidePassImg{
		display: flex;
		width: 30px;
		align-items: center;
		justify-content: center;
		margin-top: 5px;
	}
	
	.hidePassImg image{
		width: 24px;
		height: 11px;
	}
	
	.inviteImg{
		display: flex;
	}
	
	.inviteImg image{
		width: 90px;
		height: 25px;
	}
	
	.toLR{
		display: flex;
		/* justify-content: end; */
		margin: 20px 0;
		font-weight: 700;
		font-size: 12px;
		line-height: 16px;
		text-align: right;
		color: #ffb300;
	}
	
	
	.loginBtn{
		
		height: 50px;
		width: 80%;
		margin: 0 auto;
		background: #334756;
		border-radius: 10px;
		border: none;
		font-size: 16px;
		color: #FFF;
		display: flex;
		justify-content: center;
		align-items: center;
		
		margin-top: 40px;
	}
	
	.regBtn{
		height: 40px;
		width: 80%;
		margin: 0 auto;
		border-radius: 10px;
		border: none;
		font-size: 16px;
		color: #FFF;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.active{
		background: #ffb300;
	}
	
	.container .top{
		margin: 30px 0 10px;
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		font-size: 12px;
	}
	
	.container .top view:first-child,.container .top view:last-child{
		border-bottom: 1px dashed #fff;
		width: 30%;
	}
	
	.twoIcon{
		display: flex;
		justify-content: space-around;
		align-items: center;
	}
	
	.module{
		width: 40%;
		text-align: center;
	}
	

	.imgPic image{
		width: 37px;
		height: 37px;
	}
	
	.contentText{
		font-size: 12px;
		color: #fff;
		font-weight: 500;
		margin-top: 5px;
	}
	
	.langs{
		position: absolute;
		top: 26px;
		width: 75px;
		height: 150px;
		border: 1px solid #334765;
		border-radius: 0 0 3px 3px;
		overflow: auto;
	}
	
	.langs::-webkit-scrollbar{
		display:none
	}
	
	.langItem{
		height: 40px;
		display: flex;
		align-items: center;
		color: #506778;
	}
	
	.langItemImg{
		display: flex;
		align-items: center;
	}
	
	.langItemImg image{
		width: 18px;
		height: 18px;
		margin: 0 5px;
	}
	
	.langActive{
		border-right: 2px solid #ffb300;
		color: #fff;
	}
	
	.loginSmallBtn{
		line-height: 16px;
		margin: 20px 0;
		font-size: 12px;
		font-weight: 700;
		color: #ffb300;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}
	
	.regBigBtn{
		margin-top: 20px;
	}
	
</style>
