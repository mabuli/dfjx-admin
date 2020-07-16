package io.dfjx.module.sys.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjx.common.util.PageUtils;
import io.dfjx.common.util.R;
import io.dfjx.module.sys.entity.SysTnmtEntity;
import io.dfjx.module.sys.service.SysTnmtService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * 租户ID
 *
 * @author mazong
 * @email mazong@gmail.com
 * @date 2019-08-18 21:17:59
 */
@RestController
@RequestMapping("/sys/tnmt")
public class SysTnmtController {
    @Autowired
    private SysTnmtService sysTnmtService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("sys:tnmt:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysTnmtService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tnmtId}")
    //@RequiresPermissions("sys:tnmt:info")
    public R info(@PathVariable("tnmtId") Integer tnmtId){
		SysTnmtEntity sysTnmt = sysTnmtService.getById(tnmtId);

        return R.ok().put("sysTnmt", sysTnmt);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("sys:tnmt:save")
    public R save(@RequestBody SysTnmtEntity sysTnmt){
		sysTnmtService.save(sysTnmt);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("sys:tnmt:update")
    public R update(@RequestBody SysTnmtEntity sysTnmt){
		sysTnmtService.updateById(sysTnmt);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("sys:tnmt:delete")
    public R delete(@RequestBody Integer[] tnmtIds){
		sysTnmtService.removeByIds(Arrays.asList(tnmtIds));

        return R.ok();
    }

}
