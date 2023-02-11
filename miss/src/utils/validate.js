/**
 * Created by jiachenpan on 16/11/18.
 */

export function isvalidUsername(str) {
  // const valid_map = ['admin', 'editor']
  // return valid_map.indexOf(str.trim()) >= 0
  return true;
}

//判断字符是否为空的方法
export function isEmpty(obj){
  if(typeof obj == "undefined" || obj == null || obj == ""){
    return true;
  }else{
    return false;
  }
}
/* 非负整数 */
export function validateNum(num){
  const reg = /^[1-9]\d*$/
  return reg.test(num)
}

/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/* 大小写字母*/
export function validateAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * validate email
 * @param email
 * @returns {boolean}
 */
export function validateEmail(email) {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return re.test(email)
}
/**
* 办理人姓名输入验证，最大只能输入15个字符，限制能输入16个，实时验证输入字数个数
* @author   tcf
*/
export function blrVerificationNer(rule, value, callback) {
	//debugger
	//console.log(value.length);
	if (value.length > 15) {
		callback(new Error('办理人姓名最多输入15个字'));
	}
		callback();
};



