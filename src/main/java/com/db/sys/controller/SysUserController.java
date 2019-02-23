package com.db.sys.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.common.vo.JsonResult;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysUserService;

@Controller
@RequestMapping("/user/")
public class SysUserController {
	@RequestMapping("doUserListUI")
	public String doUserListUI(){
		return "sys/user_list";
	}
	@Autowired
	private SysUserService sysUserService;
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(String username,Integer pageCurrent){
		return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	}
	@ResponseBody
	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid){
		SysUser user=(SysUser) SecurityUtils.getSubject().getPrincipal();
		sysUserService.validById(id, valid, user.getUsername());
		return new JsonResult("update ok");
	}
	@RequestMapping("doUserEditUI")
	public String doUserEditUI(){
		return "sys/user_edit";
	}
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(SysUser entity,Integer[] roleIds){
		sysUserService.savaObject(entity, roleIds);
		return new JsonResult("save ok");
	}
	@RequestMapping("doFindObjectById")
	@ResponseBody
	public JsonResult doFindObjectById(Integer id){
		Map<String, Object> map = sysUserService.findObjectById(id);	
		return new JsonResult(map);
	}
	@ResponseBody
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser entity,Integer[] roleIds){
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update ok");
	}
	@RequestMapping("doLogin")
	@ResponseBody
	public JsonResult doLogin(String username,String password){
		//1.对用户信息进行封装
		UsernamePasswordToken token=new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		//2.提交用户信息(交给shiro框架进行认证)
		Subject subject=SecurityUtils.getSubject();
		subject.login(token);
		//token会传递给securityManager
		//securityManager会将token传递给认证管理器
		return new JsonResult("login ok");
	}
	@RequestMapping("doUpdatePwdUI")
	public String doUpdatePwdUI(){
		return "sys/pwd_edit";
	}
	@RequestMapping("UpdatePassword")
	@ResponseBody
	public JsonResult UpdatePassword(String password,String newPassword,String cfgPassword){
		sysUserService.updatePassword(password, newPassword, cfgPassword);
		return new JsonResult("修改密码成功");
	}
}
