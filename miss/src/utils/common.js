/**
 * 公共方法和定义
 */
//性别format
export function sexFormat(row,column,rowValue) {
  let name = '';
  if(!rowValue){
    return '';
  }
  if(rowValue== 1){
    name = '男';
  }else if(rowValue == 0){
    name = '女';
  }else{
    name = '未知';
  }
  return name;
}
//返回更新时提示描述
export function formatChange(ifChange){
  if(ifChange){
    return "new";
  }
  return "";
}
//计算进度条百分比
export function computePercent(total,complete){
  let num = parseFloat(((complete/total)*100).toFixed(2));
  return num;
}

//机构级别 分类    @tcf
export function orgLvlCdeCation(row,column,rowValue){
	let name = '';
	  if(!rowValue){
	    return '';
	  }
	  if(rowValue== 1){
	    name = '正局级';
	  }else if(rowValue == 2){
	    name = '副局级';
	  }else{
	    name = '未知';
	  }
	  return name;
}

//机构类型 分类  @anthor tcf
export function orgTypeCdeCation(row,column,rowValue){
	let name = '';
	  if(!rowValue){
	    return '';
	  }
	  if(rowValue== 1){
	    name = '会机关';
	  }else if(rowValue == 2){
	    name = '派出机构';
	  }else if(rowValue == 3){
			name = '会管单位';
		}else{
	    name = '未知';
	  }
	  return name;
}
//职务级别分类   @anthor tcf
export function titleLvlCdeCation(row,column,rowValue){
	let name = '';
	  if(!rowValue){
	    return '';
	  }
	  if(rowValue== 1){
	    name = '正局级';
	  }else if(rowValue == 2){
	    name = '副局级';
	  }else if(rowValue == 3){
			name = '正处级';
		}else{
	    name = '未知';
	  }
	  return name;
}
//职务类型分类   @anthor tcf
export function titleTypeCdeCation(row,column,rowValue){
	let name = '';
	  if(!rowValue){
	    return '';
	  }
	  if(rowValue== 1){
	    name = '会机关';
	  }else if(rowValue == 2){
	    name = '派出机构';
	  }else if(rowValue == 3){
			name = '会管单位';
		}else if(rowValue == 4){
			name = '党内职务'
		}else{
	    name = '未知';
	  }
	  return name;
}
