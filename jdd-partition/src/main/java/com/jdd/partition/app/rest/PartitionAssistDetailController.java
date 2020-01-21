package com.jdd.partition.app.rest;
import java.lang.reflect.Field;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jdd.partition.util.CamelUnderlineUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import com.jdd.partition.service.PartitionAssistDetailService;
import com.jdd.partition.entity.PartitionAssistDetail;
import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultGenerator;
import org.slf4j.Logger;
import com.jdd.partition.common.TokenUser;

import org.slf4j.LoggerFactory;
import java.util.UUID;
import com.jdd.partition.util.TokenUserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jdd.partition.util.PageStr;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 瓜分红包助力明细表 前端控制器
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/app/token/partitionAssistDetail")
public class PartitionAssistDetailController {
    private final Logger logger = LoggerFactory.getLogger(PartitionAssistDetailController.class);
	@Resource
    private PartitionAssistDetailService partitionAssistDetailService;

    @PostMapping("/add")
    public Result add(@RequestBody PartitionAssistDetail partitionAssistDetail,HttpServletRequest req) {
        TokenUser tokenUser =TokenUserUtils.getUserFromRequest(req);
    	if (null==tokenUser) {
    		return ResultGenerator.genFailResult("客户信息错误");
    	}
    	String cardcustId= tokenUser.getUserId();
    	if (StringUtils.isEmpty(cardcustId)) {
    		return ResultGenerator.genFailResult("客户信息错误");
		}
        UUID uuid = UUID.randomUUID();
    	partitionAssistDetail.setId(uuid.toString());
        partitionAssistDetailService.save(partitionAssistDetail);
        return ResultGenerator.genSuccessResult(partitionAssistDetail);
    }

    @PostMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable String id) {
        partitionAssistDetailService.removeById(id);
        return ResultGenerator.genSuccessResult(id);
    }

    @PostMapping("/updateById")
    public Result updateById(@RequestBody PartitionAssistDetail partitionAssistDetail) {
        if (StringUtils.isEmpty(partitionAssistDetail.getId())) {
    		partitionAssistDetailService.updateById(partitionAssistDetail);
    		return ResultGenerator.genSuccessResult(partitionAssistDetail);
		}else {
			return ResultGenerator.genFailResult("id为空，修改失败");
		}
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable String id) {
        PartitionAssistDetail partitionAssistDetail = partitionAssistDetailService.getById(id);
        return ResultGenerator.genSuccessResult(partitionAssistDetail);
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "15") Integer size,PartitionAssistDetail partitionAssistDetail,String select_condition_sign) {
        Page<PartitionAssistDetail> pageTemp = new Page<PartitionAssistDetail>(current,size);
        QueryWrapper<PartitionAssistDetail> wrapper = new QueryWrapper<>();
        Class cls = partitionAssistDetail.getClass();  
        Field[] fields = cls.getDeclaredFields();
        String name = "";
        if (StringUtils.isNotEmpty(select_condition_sign)) {
        	for(int i=0; i<fields.length; i++){  
        		Field f = fields[i];  
        		f.setAccessible(true);  
        		try {
        			name = CamelUnderlineUtil.underline(new StringBuffer(f.getName())).toString();
        			if (!f.getName().equals("serialVersionUID")) {
        				if (f.get(partitionAssistDetail)!=null) {
        					if (select_condition_sign.equals("1")) {
        						wrapper.eq(name, f.get(partitionAssistDetail));
        					}else if (select_condition_sign.equals("2")) {
        						wrapper.like(name, f.get(partitionAssistDetail).toString());
        					}else if (select_condition_sign.equals("3")) {
        						wrapper.ge(name, f.get(partitionAssistDetail).toString());
        					}else if (select_condition_sign.equals("4")) {
        						wrapper.le(name, f.get(partitionAssistDetail).toString());
        					}
        				}
					}
        		} catch (IllegalArgumentException e) {
        			e.printStackTrace();
        		} catch (IllegalAccessException e) {
        			e.printStackTrace();
        		} 
        		
        	}   
		}
        List<String> descs = new ArrayList<String>();
        descs.add("createTime");
        pageTemp.setDescs(descs);
        IPage<PartitionAssistDetail> page = partitionAssistDetailService.page(pageTemp,wrapper);
        PageStr pageStr = new PageStr();
        pageStr.setCurrentPage(current);
        pageStr.setShowCount(size);
        pageStr.setTotalPage((int)page.getPages());
        pageStr.setTotalResult((int)page.getTotal());
        Map map = new HashMap();
        map.put("page",page);
        map.put("pageStr",pageStr.getPageStr());
        return ResultGenerator.genSuccessResult(map);
    }
}
