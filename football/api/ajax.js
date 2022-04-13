//能发生ajax请求异步的函数模块
//封装axios库
//函数的返回值是promise对象
// import axios from 'axios'

// ,timeout=10000
export default function ajax(url,data={},method='GET') {

    return new Promise((resolve, reject)=>{
        let promise;
        if(method === 'GET'){ //GET请求方式
            promise = uni.request({
			    url, //仅为示例，并非真实接口地址。
			    data: data,
				method,
			    header: {
					// // #ifdef APP-PLUS
					
					// 	"user-agent":getApp().globalData.G_UA,
								
					// // #endif
			       'Content-Type': 'application/x-www-form-urlencoded'
			    },
			});
			
			
			
        }else{
            promise =  uni.request({
			    url, //仅为示例，并非真实接口地址。
			    data: data,
				method,
			    header: {
			       'Content-Type': 'application/x-www-form-urlencoded'
			    },
			});
        }
		
		// console.log("promise",promise);

        promise.then(response =>{
	
			if(response[0]){ //如果这个为1,可能有错误
				// resolve({code:-101,msg:'超时了'})
				console.log("存在错误",response[0].errMsg);
				uni.showToast({
					title: "存在错误"+response[0].errMsg,
					icon:"none",
					duration: 2000
				});
			}else{
				resolve(response[1].data)
			}
        }).catch(error=>{
			console.log("请求出错了"+error);
			
			// console.log("请求出错了"+error.message);
            // message.error("请求出错了"+error.message)
            // 请求出错了timeout of 10000ms exceeded
            // if(error.message.indexOf('timeout') !== -1){ //超时了,返回对应的
            //     resolve({code:-101,msg:'超时了'})
            // }else{
            //     // Message({
            //     //     showClose: true,
            //     //     message: "请求出错了"+error.message,
            //     //     type: 'error'
            //     // });
            // }


        })



    })


}


export function addQueryString_real(params) {

    let paramsData = '';
    for (let Key in params) {  //for..in 遍历对象的可枚举属性包括自身属性与原型 的属性
        paramsData += `${Key}=${params[Key]}&`;
    }
    paramsData = paramsData.substr(0, paramsData.length - 1);
    return paramsData;
}


export function addQueryString_objectKey(params) {

    // params={name:'张三',age:'18'}
    let requestUrl = ""
    //直接返回以key为索引的数组,如['name','age']
    let currArray = Object.keys(params) //仅遍历对象自身的可枚举属性
    currArray.forEach(item=>{
        requestUrl = requestUrl + item + "=" + params[item] + "&"
    })


    if(requestUrl !== ''){
        // 删除最后一个字符 几种方式
        requestUrl = requestUrl.substr(0, requestUrl.length - 1);
        // requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf('&'));
    }

    // console.log("requestUrl",requestUrl)

    return requestUrl

    // let paramsData = '';
    // for (var Key in params) {
    //     paramsData += `${Key}=${params[Key]}&`;
    // }
    // paramsData = paramsData.substr(0, paramsData.length - 1);
    // return paramsData;
}
