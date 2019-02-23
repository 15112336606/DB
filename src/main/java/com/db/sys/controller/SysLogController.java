package com.db.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.common.vo.JsonResult;
import com.db.common.vo.PageObject;
import com.db.sys.entity.SysLog;
import com.db.sys.service.SysLogService;

@Controller
@RequestMapping("/log/")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	/**
	 * 返回日志页面的操作
	 * @return
	 */
	@RequestMapping("doLogListUI")
	public String doLogListUI(){
		return "sys/log_list";
	}
	
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObject(String username,Integer pageCurrent){
		PageObject<SysLog> pageObject = 
				sysLogService.findPageObjects(username, pageCurrent);
		return new JsonResult(pageObject);
	}
	
	@RequestMapping("doDeleteObjects")
	@ResponseBody
	public JsonResult doDeleteObjects(Integer... ids){
		int rows = sysLogService.deleteObjects(ids);
		return new JsonResult("成功删除"+rows+"条记录");
	}
}
