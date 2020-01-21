package com.jdd.partition.app.rest;
import java.lang.reflect.Field;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jdd.partition.client.rest.IcbcUserAccountClient;
import com.jdd.partition.entity.IcbcUserAccount;
import com.jdd.partition.entity.PartitionRedPacketUser;
import com.jdd.partition.service.PartitionRedPacketUserService;
import com.jdd.partition.util.CamelUnderlineUtil;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import com.jdd.partition.service.PartitionPacketCashService;
import com.jdd.partition.entity.PartitionPacketCash;
import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultGenerator;
import org.slf4j.Logger;
import com.jdd.partition.common.TokenUser;

import org.slf4j.LoggerFactory;
import com.jdd.partition.util.TokenUserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jdd.partition.util.PageStr;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 瓜分红包提现表 前端控制器
 * </p>
 *
 * @author pengbaoning
 */
@RestController
@RequestMapping("/app/token/partitionPacketCash")
public class PartitionPacketCashController {
    private final Logger logger = LoggerFactory.getLogger(PartitionPacketCashController.class);
	@Resource
    private PartitionPacketCashService partitionPacketCashService;

    @Resource
    private PartitionRedPacketUserService partitionRedPacketUserService;

    @Autowired
    private IcbcUserAccountClient icbcUserAccountClient;

    /**
     * 活动红包满一百提现
     * @param partitionPacketCash
     * @param req
     * @return
     */
    @PostMapping("/withdraw")
    public Result withdraw(@RequestBody PartitionPacketCash partitionPacketCash,HttpServletRequest req) {
        try {

            TokenUser tokenUser =TokenUserUtils.getUserFromRequest(req);
            if (null==tokenUser) {
                return ResultGenerator.genFailResult("客户信息错误");
            }
            String cardcustId= tokenUser.getUserId();
            if (StringUtils.isEmpty(cardcustId)) {
                return ResultGenerator.genFailResult("客户信息错误");
            }

            if (partitionPacketCash.getAmount() < 10000) {
                return ResultGenerator.genFailResult("红包提现金额不足");
            }
            if (partitionPacketCash.getAmount() != 10000) {
                return ResultGenerator.genFailResult("红包提现金额有误");
            }
            // 获取红包金额够不够提现标准
            if (StringUtils.isEmpty(partitionPacketCash.getPartitionId())) {
                return ResultGenerator.genFailResult("传入瓜分红包参数有误");
            }
            // 判断有没有开户
            IcbcUserAccount icbcUserAccountParam = new IcbcUserAccount();
            icbcUserAccountParam.setCardcustId(cardcustId);
            IcbcUserAccount icbcUserAccount = null;
            try {
                Result result = icbcUserAccountClient.getIcbcAccount(icbcUserAccountParam);
                if (result.getCode() == 200) {
                    if (result.getData() == null) {
                        return ResultGenerator.genFailResult("请开通电子账户");
                    } else {
                        JSONObject paramsObj = (JSONObject) JSONObject.parseObject(String.valueOf(result));
                        JSONObject json  = (JSONObject) paramsObj.get("data");
                        icbcUserAccount =JSONObject.toJavaObject(json, IcbcUserAccount.class);
                    }
                } else {
                    logger.error("调用微服务获取开户信息失败-----> " + JSONObject.toJSONString(result));
                    return ResultGenerator.genFailResult("系统调用失败");
                }
            } catch (Exception e) {
                logger.error("调用微服务获取开户信息失败-----> {}", e);
                return ResultGenerator.genFailResult("系统调用失败");
            }

            if (StringUtils.isEmpty(icbcUserAccount.getMediumId())) {
                return ResultGenerator.genFailResult("提现账号参数有误");
            }
            if (StringUtils.isEmpty(icbcUserAccount.getCustName())) {
                return ResultGenerator.genFailResult("提现用户姓名参数有误");
            }
            if (StringUtils.isEmpty(icbcUserAccount.getMobileNo())) {
                return ResultGenerator.genFailResult("提现用户手机号参数有误");
            }
            PartitionRedPacketUser partitionRedPacketUser = partitionRedPacketUserService.getById(partitionPacketCash.getPartitionId());
            if (partitionRedPacketUser == null) {
                return ResultGenerator.genFailResult("红包信息不存在");
            }
            if (partitionRedPacketUser.getTotalAmount() < 10000) {
                return ResultGenerator.genFailResult("红包提现金额不足");
            }
            if(partitionRedPacketUser.getStatus() != 1) {
                return ResultGenerator.genFailResult("你的提现受理中");
            }

            UUID uuid = UUID.randomUUID();
            partitionPacketCash.setId(uuid.toString());
            partitionPacketCash.setCreateTime(new Date());
            partitionPacketCash.setCardcustId(cardcustId);
            partitionPacketCash.setMediumId(icbcUserAccount.getMediumId());
            partitionPacketCash.setCustTel(icbcUserAccount.getMobileNo());
            partitionPacketCash.setCustName(icbcUserAccount.getCustName());
            partitionPacketCash.setIconPic(tokenUser.getIconPic());
            // 待审核
            partitionPacketCash.setStatus(0);
            boolean save = partitionPacketCashService.save(partitionPacketCash);
            if (save) {
                // 修改红包状态为待提现
                partitionRedPacketUser.setStatus(0);
                partitionRedPacketUserService.updateById(partitionRedPacketUser);
            }

        } catch ( Exception e) {
            logger.error("提现异常", e);
            return ResultGenerator.genFailResult("服务器内部异常");
        }

        return ResultGenerator.genSuccessResult(null, "提现受理成功");
    }

    @PostMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable String id) {
        partitionPacketCashService.removeById(id);
        return ResultGenerator.genSuccessResult(id);
    }

    @PostMapping("/updateById")
    public Result updateById(@RequestBody PartitionPacketCash partitionPacketCash) {
        if (StringUtils.isEmpty(partitionPacketCash.getId())) {
    		partitionPacketCashService.updateById(partitionPacketCash);
    		return ResultGenerator.genSuccessResult(partitionPacketCash);
		}else {
			return ResultGenerator.genFailResult("id为空，修改失败");
		}
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable String id) {
        PartitionPacketCash partitionPacketCash = partitionPacketCashService.getById(id);
        return ResultGenerator.genSuccessResult(partitionPacketCash);
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "15") Integer size,PartitionPacketCash partitionPacketCash,String select_condition_sign) {
        Page<PartitionPacketCash> pageTemp = new Page<PartitionPacketCash>(current,size);
        QueryWrapper<PartitionPacketCash> wrapper = new QueryWrapper<>();
        Class cls = partitionPacketCash.getClass();  
        Field[] fields = cls.getDeclaredFields();
        String name = "";
        if (StringUtils.isNotEmpty(select_condition_sign)) {
        	for(int i=0; i<fields.length; i++){  
        		Field f = fields[i];  
        		f.setAccessible(true);  
        		try {
        			name = CamelUnderlineUtil.underline(new StringBuffer(f.getName())).toString();
        			if (!f.getName().equals("serialVersionUID")) {
        				if (f.get(partitionPacketCash)!=null) {
        					if (select_condition_sign.equals("1")) {
        						wrapper.eq(name, f.get(partitionPacketCash));
        					}else if (select_condition_sign.equals("2")) {
        						wrapper.like(name, f.get(partitionPacketCash).toString());
        					}else if (select_condition_sign.equals("3")) {
        						wrapper.ge(name, f.get(partitionPacketCash).toString());
        					}else if (select_condition_sign.equals("4")) {
        						wrapper.le(name, f.get(partitionPacketCash).toString());
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
        IPage<PartitionPacketCash> page = partitionPacketCashService.page(pageTemp,wrapper);
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
