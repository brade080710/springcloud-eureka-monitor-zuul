package ${package.Controller};
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
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
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
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/app/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>app/token/${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
    private final Logger logger = LoggerFactory.getLogger(${table.controllerName}.class);
	@Resource
    private ${table.serviceName} ${table.entityPath}Service;

    @PostMapping("/add")
    public Result add(@RequestBody ${entity} ${table.entityPath},HttpServletRequest req) {
        TokenUser tokenUser =TokenUserUtils.getUserFromRequest(req);
    	if (null==tokenUser) {
    		return ResultGenerator.genFailResult("客户信息错误");
    	}
    	String cardcustId= tokenUser.getUserId();
    	if (StringUtils.isEmpty(cardcustId)) {
    		return ResultGenerator.genFailResult("客户信息错误");
		}
        UUID uuid = UUID.randomUUID();
    	${table.entityPath}.setId(uuid.toString());
        ${table.entityPath}Service.save(${table.entityPath});
        return ResultGenerator.genSuccessResult(${table.entityPath});
    }

    @PostMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable String id) {
        ${table.entityPath}Service.removeById(id);
        return ResultGenerator.genSuccessResult(id);
    }

    @PostMapping("/updateById")
    public Result updateById(@RequestBody ${entity} ${table.entityPath}) {
        if (StringUtils.isEmpty(${table.entityPath}.getId())) {
    		${table.entityPath}Service.updateById(${table.entityPath});
    		return ResultGenerator.genSuccessResult(${table.entityPath});
		}else {
			return ResultGenerator.genFailResult("id为空，修改失败");
		}
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable String id) {
        ${entity} ${table.entityPath} = ${table.entityPath}Service.getById(id);
        return ResultGenerator.genSuccessResult(${table.entityPath});
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "15") Integer size,${entity} ${table.entityPath},String select_condition_sign) {
        Page<${entity}> pageTemp = new Page<${entity}>(current,size);
        QueryWrapper<${entity}> wrapper = new QueryWrapper<>();
        Class cls = ${table.entityPath}.getClass();  
        Field[] fields = cls.getDeclaredFields();
        String name = "";
        if (StringUtils.isNotEmpty(select_condition_sign)) {
        	for(int i=0; i<fields.length; i++){  
        		Field f = fields[i];  
        		f.setAccessible(true);  
        		try {
        			name = CamelUnderlineUtil.underline(new StringBuffer(f.getName())).toString();
        			if (!f.getName().equals("serialVersionUID")) {
        				if (f.get(${table.entityPath})!=null) {
        					if (select_condition_sign.equals("1")) {
        						wrapper.eq(name, f.get(${table.entityPath}));
        					}else if (select_condition_sign.equals("2")) {
        						wrapper.like(name, f.get(${table.entityPath}).toString());
        					}else if (select_condition_sign.equals("3")) {
        						wrapper.ge(name, f.get(${table.entityPath}).toString());
        					}else if (select_condition_sign.equals("4")) {
        						wrapper.le(name, f.get(${table.entityPath}).toString());
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
        IPage<${entity}> page = ${table.entityPath}Service.page(pageTemp,wrapper);
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
</#if>
</#if>
