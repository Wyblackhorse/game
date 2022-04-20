<script>
	export default {
		onLaunch: function() {
			// console.log('App Launch')
		},
		onShow: function() {
			// console.log('App Show')
			let console_pre = 'height: 20px;line-height:20px;background: #39455E; color: #fff; border-radius: 3px 0 0 3px;'
			let console_back = 'height: 20px;line-height:20px;background: #007AFF; color: #fff; border-radius: 0 3px 3px 0;'
			console.log(`%c football %c Welcome to this platform `, console_pre, console_back)
		},
		onHide: function() {
			// console.log('App Hide')
		}
	}
</script>

<style >
	@import 'uview-ui/theme.scss';
	/*每个页面公共css */
	@import url("./static/iconfont/iconfont.css");
	@import url("./static/iconfont2/iconfont.css");

	uni-tabbar .uni-tabbar .uni-tabbar__item{
		margin-top: 6px;
		padding-bottom: 20px;
		 
	}
	
	
</style>
