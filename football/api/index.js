// import {websiteUrl,webSocketUrl,imageSiteUrl} from "../common/base.js"
import ajax, {addQueryString_objectKey, addQueryString_real} from './ajax'

// let requestCurr = "/curr"
// let requestCurr = ""


//获取当前网址，如： http://localhost:8888/eeeeeee/aaaa/vvvv.html
var curWwwPath = window.document.location.href;
//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
//获取主机地址，如： http://localhost:8888
var localhostPaht = curWwwPath.substring(0, pos);
//获取带"/"的项目名，如：/abcd
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);


curWwwPath  = 'http://8.136.97.179/bgh5/index.html#/pages/login/login'



let hashUrl = curWwwPath.split('/#')[0]
let webHashUrl = hashUrl.split('http://')[1].split('/')[0]

//全局请求基础域名或IP接口
const websiteUrl = 'http://'+webHashUrl;  
const webSocketUrl = 'ws://'+webHashUrl;  

// console.log("websiteUrl",websiteUrl);

let requestCurr = websiteUrl

// console.log("requestCurr",requestCurr);

export const MaxWidthNum = 750
export const websiteUrlStr = websiteUrl

// 登录的请求接口
export const loginReq = (queryParam) => {
    return ajax(requestCurr+'/client/login',queryParam,'POST')
}

// 注册的请求接口
export const registerReq = (queryParam) => {
    return ajax(requestCurr+'/client/register',queryParam,'POST')
}

// 获取验证码请求接口
export const verifyCodeReq = () => {
    return ajax(requestCurr+'/tools/verifyCode','GET')
}

// 查询余额请求接口
export const getBalanceReq = (queryParam) => {
    return ajax(requestCurr+'/client/get_balance',queryParam,'POST')
}

// 获取公告通知接口
export const getNotifyReq = (queryParam) => {
    return ajax(requestCurr+'/client/get_notify',queryParam,'POST')
}

// 修改头像(及昵称相关信息)请求接口
export const changeInformationForNicknameReq = (queryParam) => {
    return ajax(requestCurr+'/client/changeInformationForNickname',queryParam,'POST')
}

// 修改登录密码请求接口
export const alterPasswordReq = (queryParam) => {
    return ajax(requestCurr+'/client/alter_password',queryParam,'POST')
}

// 获取系统配置请求接口
export const getConfigReq = (queryParam) => {
    return ajax(requestCurr+'/client/get_config',queryParam,'POST')
}

// 注销/推出请求接口
export const loginOutReq = (queryParam) => {
    return ajax(requestCurr+'/client/loginOut',queryParam,'POST')
}



// 获取初始请求地址和初始请求ws地址
export const requestWebsiteUrl = websiteUrl
export const requestWebSocketUrl = webSocketUrl
/**
 * 已知前后文 取中间文本
 * @param str 全文
 * @param start 前文
 * @param end 后文
 * @returns 中间文本 || null
 */
export function getStr(str, start, end) {
    let res = str.match(new RegExp(`${start}(.*?)${end}`))
    return res ? res[1] : null
}

