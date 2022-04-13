//全局请求基础域名或IP接口
// const websiteUrl = 'http://8.136.97.179';  
// const webSocketUrl = 'ws://8.136.97.179:9502';  

//获取当前网址，如： http://localhost:8888/eeeeeee/aaaa/vvvv.html
var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8888
var localhostPaht = curWwwPath.substring(0, pos);
//获取带"/"的项目名，如：/abcd
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

// curWwwPath  = 'http://8.136.97.179/rech5/index.html#/pages/login/login'

// let hashUrl = window.location.hash
// let url = hashUrl.substring(hashUrl.indexOf('?') + 1, hashUrl.length)
// let arr = (url).replace(/^\#/, '').split('&')

// console.log("curWwwPath",curWwwPath)
let hashUrl = curWwwPath.split('/#')[0]
// hashUrl = 'http://8.136.97.179'
let webHashUrl = hashUrl.split('http://')[1].split('/')[0]
// console.log("hashUrl",'http://'+webHashUrl)
//全局请求基础域名或IP接口

const websiteUrl = 'http://'+webHashUrl;  
// const webSocketUrl = 'ws://'+webHashUrl+':9502';  
const webSocketUrl = 'ws://'+webHashUrl;  
// console.log("webSocketUrl",webSocketUrl)



//这个阿里云图片地址暂时没有用上
const imageSiteUrl = ""

//输出网址地址
export default {  
  websiteUrl,
  webSocketUrl,
  imageSiteUrl
}
