package com.db.sys.service;

import com.db.common.vo.PageObject;
import com.db.sys.entity.SysLog;

public interface SysLogService {
	PageObject<SysLog> findPageObjects(String username,Integer pageCurrent);
	int deleteObjects(Integer... ids);
}

