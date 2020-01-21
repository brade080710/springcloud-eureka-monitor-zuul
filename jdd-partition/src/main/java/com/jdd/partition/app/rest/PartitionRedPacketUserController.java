package com.jdd.partition.app.rest;

import java.lang.reflect.Field;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.jdd.partition.client.rest.IcbcUserAccountClient;
import com.jdd.partition.entity.IcbcUserAccount;
import com.jdd.partition.entity.PartitionAssistDetail;
import com.jdd.partition.entity.PartitionPacketCash;
import com.jdd.partition.service.PartitionAssistDetailService;
import com.jdd.partition.service.PartitionPacketCashService;
import com.jdd.partition.util.*;

import java.net.URLEncoder;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.jdd.partition.vo.ElectronicAssist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.jdd.partition.service.PartitionRedPacketUserService;
import com.jdd.partition.entity.PartitionRedPacketUser;
import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultGenerator;
import org.slf4j.Logger;
import com.jdd.partition.common.TokenUser;

import org.slf4j.LoggerFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 助力红包用户基本信息表 前端控制器
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/app/token/partitionRedPacketUser")
public class PartitionRedPacketUserController {
    private final Logger logger = LoggerFactory.getLogger(PartitionRedPacketUserController.class);
    @Resource
    private PartitionRedPacketUserService partitionRedPacketUserService;
    @Resource
    private PartitionPacketCashService partitionPacketCashService;
    @Resource
    private PartitionAssistDetailService partitionAssistDetailService;
    @Value("${jdd.share.url}")
    private String sharUrl;
    @Value("${jdd.request.short.url}")
    private String requestUrl;
    @Value("${jdd.has.packet.url}")
    private String hasPacketUrl;
    @Value("${jdd.has.no.packet.url}")
    private String hasNoPacketUrl;
    @Value("${jdd.has.icbc.account.url}")
    private String hasIcbcAccount;
    @Value("${jdd.has.no.icbc.account.url}")
    private String hasNoIcbcAccount;
    @Autowired
    private IcbcUserAccountClient icbcUserAccountClient;

    // 初始化金额数据
    private static int STARTAMOUNT = 8500;
    private static int ENDAMOUNT = 9950;

    /**
     * 瓜分红包
     *
     * @param partitionRedPacketUser
     * @param req
     * @return
     */
    @PostMapping("/partition")
    public Result partition(@RequestBody PartitionRedPacketUser partitionRedPacketUser, HttpServletRequest req) {
        Map<String, Object> params = new HashMap<>();
        try {
            TokenUser tokenUser = TokenUserUtils.getUserFromRequest(req);
            if (null == tokenUser) {
                return ResultGenerator.genFailResult("客户信息错误");
            }
            String cardcustId = tokenUser.getUserId();
            if (StringUtils.isEmpty(cardcustId)) {
                return ResultGenerator.genFailResult("客户信息错误");
            }
            // 查询有没有获取过红包,领取过时间没有过期判断，不能重复领取
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("cardcust_id", cardcustId);
            queryWrapper.eq("status", 1);
            queryWrapper.orderByDesc("create_time");
            List<PartitionRedPacketUser> list = partitionRedPacketUserService.list(queryWrapper);
            if (list.size() > 0 && list.get(0).getExpireTime().getTime() > new Date().getTime()) {
                return ResultGenerator.genFailResult("你已领取过");
            }

            UUID uuid = UUID.randomUUID();
            if (StringUtils.isNotEmpty(tokenUser.getNeName())) {
                partitionRedPacketUser.setCustName(tokenUser.getNeName());
            } else {
                partitionRedPacketUser.setCustName(tokenUser.getMobile());
            }

            partitionRedPacketUser.setCustTel(tokenUser.getMobile());
            partitionRedPacketUser.setMediumId(tokenUser.getMediumId());
            partitionRedPacketUser.setId(uuid.toString());
            partitionRedPacketUser.setCardcustId(cardcustId);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            partitionRedPacketUser.setCreateTime(calendar.getTime());
            // 设置过期时间
            calendar.add(Calendar.DATE, 10);
            partitionRedPacketUser.setExpireTime(calendar.getTime());
            // 可用
            partitionRedPacketUser.setStatus(1);
            int initAmout = RandomUtil.randomFromTo(STARTAMOUNT, ENDAMOUNT);
            partitionRedPacketUser.setInitAmout(initAmout);
            partitionRedPacketUser.setTotalAmount(initAmout);
            // 分享链接
            partitionRedPacketUser.setPartitionUrl(sharUrl + "?cardcustId=" + cardcustId + "&partitionId=" + uuid);
            try {
                String shortUrl = HttpUtil.get(requestUrl + URLEncoder.encode(partitionRedPacketUser.getPartitionUrl(), "utf-8"));
                if (StringUtils.isNotEmpty(shortUrl)) {

                    partitionRedPacketUser.setPartitionShortUrl((String) JSONObject.parseObject(shortUrl).get("url"));
                }
            } catch(Exception e) {
                logger.error("调取短连接异常：{}", e.getMessage());
                partitionRedPacketUser.setPartitionShortUrl("");
            }

            partitionRedPacketUser.setVersion(1);
            partitionRedPacketUser.setIconPic(tokenUser.getIconPic());
            partitionRedPacketUserService.save(partitionRedPacketUser);
            // 已成功领取
            int count = partitionRedPacketUserService.count();
            Page<PartitionRedPacketUser> page = new Page<PartitionRedPacketUser>(1, 15);
            QueryWrapper operate = new QueryWrapper();
            operate.orderByDesc("create_time");
            IPage<PartitionRedPacketUser> pageList = partitionRedPacketUserService.page(page, operate);
            params.put("partitionRedPacketUser", partitionRedPacketUser);
            params.put("count", count + 1200);
            params.put("list", pageList.getRecords());
        } catch (Exception e) {
            logger.error("瓜分红包异常-->{}", e);
            return ResultGenerator.genFailResult("服务器内部异常");
        }

        return ResultGenerator.genSuccessResult(params);
    }

    /**
     * 助力红包
     *
     * @param partitionAssistDetail
     * @param req
     * @return
     */
    @PostMapping("/assist")
    public Result assist(@RequestBody PartitionAssistDetail partitionAssistDetail, HttpServletRequest req) {
        Map<String, Object> params = new HashMap<>();
        try {
            TokenUser tokenUser = TokenUserUtils.getUserFromRequest(req);
            if (null == tokenUser) {
                return ResultGenerator.genFailResult("客户信息错误");
            }
            if (StringUtils.isEmpty(partitionAssistDetail.getCardcustId())) {
                return ResultGenerator.genFailResult("客户信息错误");
            }
            if (StringUtils.isEmpty(partitionAssistDetail.getPartitionId())) {
                return ResultGenerator.genFailResult("助力红包信息错误");
            }
            if (StringUtils.isEmpty(partitionAssistDetail.getInvitedCardcustId())) {
                return ResultGenerator.genFailResult("助力人信息错误");
            }
            if (partitionAssistDetail.getCardcustId().equals(partitionAssistDetail.getInvitedCardcustId())) {
                return ResultGenerator.genFailResult("同一个用户不可助力");
            }

            // 判断当前用户是否已助力过
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("partition_id", partitionAssistDetail.getPartitionId());
            queryWrapper.eq("cardcust_id", partitionAssistDetail.getCardcustId());
            queryWrapper.eq("invited_cardcust_id", partitionAssistDetail.getInvitedCardcustId());
            queryWrapper.eq("assist_type", 3);
            List<PartitionAssistDetail> list = partitionAssistDetailService.list(queryWrapper);
            if (list.size() > 0) {
                return ResultGenerator.genFailResult("你已助力过，请勿重复助力");
            }

            // 查询助力红包有没有过期
            PartitionRedPacketUser partitionRedPacketUser = partitionRedPacketUserService.getById(partitionAssistDetail.getPartitionId());
            if (partitionRedPacketUser == null) {
                return ResultGenerator.genFailResult("助力红包信息错误");
            } else {
                if (partitionRedPacketUser.getExpireTime().getTime() < new Date().getTime()) {
                    return ResultGenerator.genFailResult("助力红包已过期");
                }
                if (partitionRedPacketUser.getTotalAmount() >= 10000) {
                    return ResultGenerator.genFailResult("助力红包已满100，可以提现啦");
                }
            }

            // 获取助力老用户数量
            QueryWrapper countQueryWrapper = new QueryWrapper();
            countQueryWrapper.eq("partition_id", partitionAssistDetail.getPartitionId());
            countQueryWrapper.eq("assist_type", 3);
            int countOldAssistCustomer = partitionAssistDetailService.count(countQueryWrapper);
            if (countOldAssistCustomer > 4) {
                return ResultGenerator.genFailResult("邀请新用户再助力吧");
            } else {

                // 获取助力金额
                int assistAmount = 0;
                int totalAmount = partitionRedPacketUser.getTotalAmount();

                if (totalAmount < ENDAMOUNT) {
                    assistAmount = RandomUtil.randomFromTo(1, ENDAMOUNT - totalAmount);

                } else {
                    assistAmount = 1;
                }
                UUID uuid = UUID.randomUUID();
                partitionAssistDetail.setId(uuid.toString());
                partitionAssistDetail.setAssistType(3);
                partitionAssistDetail.setAmount(assistAmount);
                if (StringUtils.isNotEmpty(tokenUser.getNeName())) {
                    partitionAssistDetail.setCustName(tokenUser.getNeName());
                } else {
                    partitionAssistDetail.setCustName(tokenUser.getMobile());
                }
                partitionAssistDetail.setCustTel(tokenUser.getMobile());
                partitionAssistDetail.setCreateTime(new Date());
                partitionAssistDetail.setRemark("帮你发起助力红包奖励");
                partitionAssistDetail.setIconPic(tokenUser.getIconPic());
                partitionAssistDetail.setPriCustName(partitionRedPacketUser.getCustName());
                partitionAssistDetail.setPriCustTel(partitionRedPacketUser.getCustTel());
                boolean save = partitionAssistDetailService.save(partitionAssistDetail);
                // 更新助力红包
                if (save) {
                    partitionRedPacketUser.setTotalAmount(partitionRedPacketUser.getTotalAmount() + assistAmount);
                    partitionRedPacketUserService.updateById(partitionRedPacketUser);
                    params.put("partitionRedPacketUser", partitionRedPacketUser);
                }

                params.put("partitionAssistDetail", partitionAssistDetail);
            }

        } catch (Exception e) {
            logger.error("助力红包异常---> {}", e);
            return ResultGenerator.genFailResult("服务器内部异常");
        }

        return ResultGenerator.genSuccessResult(params);
    }


    /**
     * 点击开户按钮，保存助力信息关系
     *
     * @param partitionAssistDetail
     * @param req
     * @return
     */
    @PostMapping("/clickAssist")
    public Result clickAssist(@RequestBody PartitionAssistDetail partitionAssistDetail, HttpServletRequest req) {
        Map<String, Object> params = new HashMap<>();
        try {
            TokenUser tokenUser = TokenUserUtils.getUserFromRequest(req);
            if (null == tokenUser) {
                return ResultGenerator.genFailResult("客户信息错误");
            }

            if (StringUtils.isEmpty(partitionAssistDetail.getCardcustId())) {
                return ResultGenerator.genFailResult("助力红包客户信息错误");
            }
            if (StringUtils.isEmpty(partitionAssistDetail.getPartitionId())) {
                return ResultGenerator.genFailResult("助力红包参数信息错误");
            }
            if (StringUtils.isEmpty(partitionAssistDetail.getInvitedCardcustId())) {
                return ResultGenerator.genFailResult("助力人信息有误");
            }
            if (StringUtils.isEmpty(partitionAssistDetail.getCustName())) {
                return ResultGenerator.genFailResult("助力人名称有误");
            }

            // 查询当前用户是否助力过，没助力插入信息，有则更新
            QueryWrapper queryWrapper = new QueryWrapper();

            queryWrapper.eq("invited_cardcust_id", partitionAssistDetail.getInvitedCardcustId());
            queryWrapper.eq("assist_type", 0);

            List<PartitionAssistDetail> list = partitionAssistDetailService.list(queryWrapper);
            if (list.size() > 0) {
                for (PartitionAssistDetail assistDetail : list) {
                    if (assistDetail.getCardcustId().equals(partitionAssistDetail.getCardcustId()) &&
                            assistDetail.getPartitionId().equals(partitionAssistDetail.getPartitionId())) {
                        params.put("partitionAssistDetail", assistDetail);
                        return ResultGenerator.genSuccessResult(params);
                    } else {
                        assistDetail.setInvitedCardcustId(partitionAssistDetail.getInvitedCardcustId());
                        assistDetail.setPartitionId(partitionAssistDetail.getPartitionId());
                        assistDetail.setCardcustId(partitionAssistDetail.getCardcustId());
                        boolean update = partitionAssistDetailService.updateById(assistDetail);
                        if (update) {
                            params.put("partitionAssistDetail", assistDetail);
                        }
                    }
                }

            } else {
                UUID uuid = UUID.randomUUID();
                partitionAssistDetail.setId(uuid.toString());
                partitionAssistDetail.setAssistType(0);
                partitionAssistDetail.setOpenId(partitionAssistDetail.getOpenId());
                partitionAssistDetail.setPartitionId(partitionAssistDetail.getPartitionId());
                partitionAssistDetail.setCardcustId(partitionAssistDetail.getCardcustId());
                partitionAssistDetail.setInvitedCardcustId(partitionAssistDetail.getInvitedCardcustId());
                partitionAssistDetail.setCustName(partitionAssistDetail.getCustName());
                partitionAssistDetail.setCustTel(partitionAssistDetail.getCustTel());
                partitionAssistDetail.setCreateTime(new Date());
                partitionAssistDetail.setIconPic(tokenUser.getIconPic());
                boolean save = partitionAssistDetailService.save(partitionAssistDetail);
                if (save) {
                    params.put("partitionAssistDetail", partitionAssistDetail);
                }
            }

        } catch (Exception e) {
            logger.error("点击开户按钮，保存助力信息关系异常-->{}", e);
            return ResultGenerator.genFailResult("服务器内部异常");
        }

        return ResultGenerator.genSuccessResult(params);
    }

    /**
     * 查询电子账户助力情况
     *
     * @param electronicAssist
     * @param req
     * @return
     */
    @PostMapping("/electronicAssist")
    public Result electronicAssist(@RequestBody ElectronicAssist electronicAssist, HttpServletRequest req) {
        Map<String, Object> params = new HashMap<>();
//        try {
        if (StringUtils.isEmpty(electronicAssist.getInvitedCardcustId())) {
            return ResultGenerator.genFailResult("用户信息错误");
        }
//            if(StringUtils.isEmpty(electronicAssist.getBankType())) {
//                return ResultGenerator.genFailResult("银行类型信息错误");
//            }

        // 判断当前用户开通电子账户助力情况
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("invited_cardcust_id", electronicAssist.getInvitedCardcustId());
        queryWrapper.eq("assist_type", 1);
        queryWrapper.orderByDesc("create_time");
        List<PartitionAssistDetail> listEleAssist = partitionAssistDetailService.list(queryWrapper);
        if (listEleAssist.size() > 0) {
            params.put("partitionAssistDetail", listEleAssist.get(0));
            return ResultGenerator.genSuccessResult(params);
        }
        params.put("partitionAssistDetail", null);
        return ResultGenerator.genSuccessResult(params);
//            // 预开户助力信息
//            QueryWrapper query = new QueryWrapper();
//            query.eq("invited_cardcust_id", electronicAssist.getInvitedCardcustId());
//            query.eq("assist_type", 0);
//            query.orderByDesc("create_time");
//            List<PartitionAssistDetail> listReadyAssist = partitionAssistDetailService.list(query);
//            if (listReadyAssist.size() == 0) {
//                return ResultGenerator.genFailResult("获取预开户助力信息错误");
//            }
//
//            // 获取当前瓜分红包信息
//            PartitionRedPacketUser partitionRedPacketUser = partitionRedPacketUserService.getById(listReadyAssist.get(0).getPartitionId());
//            if (partitionRedPacketUser == null) {
//
//                return ResultGenerator.genFailResult("助力钱包信息有误");
//            } else {
//                if (partitionRedPacketUser.getExpireTime().getTime() < new Date().getTime()) {
//                    return ResultGenerator.genFailResult("助力钱包信息已过期");
//                }
//            }
//
//            // 钱包目前金额
//            int totalAmount = partitionRedPacketUser.getTotalAmount();
//            int assistAmount = 0;
//
//
//            // 判断当前红包开户助力情况
//            int bankCount = Integer.parseInt(electronicAssist.getBankType()) == 1 ? 1 : 2;
//            int otherBank = 0;
//            int icbc = 0;
//            QueryWrapper otherQuery = new QueryWrapper();
//            otherQuery.eq("partition_id", listReadyAssist.get(0).getPartitionId());
//            otherQuery.eq("cardcust_id", partitionRedPacketUser.getCardcustId());
//            otherQuery.ge("create_time", partitionRedPacketUser.getCreateTime());
//            otherQuery.le("create_time", partitionRedPacketUser.getExpireTime());
//            otherQuery.in("bank_type", Lists.newArrayList(0, 1));
//            List<PartitionAssistDetail> list = partitionAssistDetailService.list(otherQuery);
//            for (PartitionAssistDetail partitionAssistDetail : list) {
//                if (partitionAssistDetail.getBankType() == 1) {
//                    icbc = icbc + 1;
//                } else {
//                    otherBank = otherBank + 1;
//                }
//
//            }
//
//            int inviteTotal = otherBank * 2 + icbc + bankCount;
//            if (inviteTotal >= 20) {
//                assistAmount = 10000 - totalAmount;
//                // 设置 100元
//                totalAmount = 10000;
//                // 更新钱包信息
//                partitionRedPacketUser.setTotalAmount(totalAmount);
//                partitionRedPacketUserService.updateById(partitionRedPacketUser);
//                // 记录助力信息
//                PartitionAssistDetail partitionAssistDetail = listReadyAssist.get(0);
//                partitionAssistDetail.setAssistType(1);
//                partitionAssistDetail.setAmount(assistAmount);
//                partitionAssistDetail.setCreateTime(new Date());
//                partitionAssistDetail.setRemark("开户发起助力红包奖励");
//                partitionAssistDetail.setBankType(Integer.parseInt(electronicAssist.getBankType()));
//                partitionAssistDetailService.updateById(partitionAssistDetail);
//                params.put("electronicAssist", partitionAssistDetail);
//                return ResultGenerator.genSuccessResult(params);
//            } else {
//                if (totalAmount < ENDAMOUNT) {
//                    assistAmount = RandomUtil.randomFromTo(1, ENDAMOUNT - totalAmount);
//                } else {
//                    assistAmount = 1;
//                }
//
//                PartitionAssistDetail partitionAssistDetail = listReadyAssist.get(0);
//                partitionAssistDetail.setAssistType(1);
//                partitionAssistDetail.setAmount(assistAmount);
//                partitionAssistDetail.setCreateTime(new Date());
//                partitionAssistDetail.setRemark("开户发起助力红包奖励");
//                partitionAssistDetail.setBankType(Integer.parseInt(electronicAssist.getBankType()));
//                boolean save = partitionAssistDetailService.updateById(partitionAssistDetail);
//                // 更新助力红包
//                if (save) {
//                    partitionRedPacketUser.setTotalAmount(partitionRedPacketUser.getTotalAmount() + assistAmount);
//                    partitionRedPacketUserService.updateById(partitionRedPacketUser);
//                }
//
//                params.put("electronicAssist", partitionAssistDetail);
//                return ResultGenerator.genSuccessResult(params);
//
//            }
//
//        } catch (Exception e) {
//            logger.error("点击开户按钮，保存助力信息关系",e);
//            return ResultGenerator.genFailResult("服务器内部异常");
//        }

    }

    /**
     * 判断用户是否开红包
     *
     * @return
     */
    @PostMapping("/selectUserPacket")
    public Result selectUserPacket(HttpServletRequest req) {
        Map<String, String> params = new HashMap<>();
        TokenUser tokenUser = TokenUserUtils.getUserFromRequest(req);
        if (null == tokenUser) {
            return ResultGenerator.genFailResult("客户信息错误");
        }
        String cardCustId = tokenUser.getUserId();
        if (StringUtils.isEmpty(cardCustId)) {
            return ResultGenerator.genFailResult("客户信息错误");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cardcust_id", cardCustId);
        queryWrapper.orderByDesc("create_time");
        List<PartitionRedPacketUser> list = partitionRedPacketUserService.list(queryWrapper);
        boolean isHasPacket = false;
        if (list.size() > 0) {
            // 过期或者不是可用状态
            if (list.get(0).getExpireTime().getTime() < new Date().getTime() || list.get(0).getStatus() != 1) {
                isHasPacket = false;
            } else {
                isHasPacket = true;
            }
        } else {
            isHasPacket = false;

        }
        if (isHasPacket) {
            params.put("url", hasPacketUrl);
        } else {
            params.put("url", hasNoPacketUrl);
        }
        return ResultGenerator.genSuccessResult(params);
    }

    /**
     * 判断用户是否开户，返回不同的地址
     *
     * @return
     */
    @PostMapping("/getIcbcAccount")
    public Result getIcbcAccount(HttpServletRequest req) {

        Map<String, String> params = new HashMap<>();
        TokenUser tokenUser = TokenUserUtils.getUserFromRequest(req);
        if (null == tokenUser) {
            return ResultGenerator.genFailResult("客户信息错误");
        }
        String cardCustId = tokenUser.getUserId();
        if (StringUtils.isEmpty(cardCustId)) {
            return ResultGenerator.genFailResult("客户信息错误");
        }
        boolean hasAccount = false;
//        IcbcUserAccount icbcUserAccount = null;
        IcbcUserAccount icbcUserAccountParam = new IcbcUserAccount();
        icbcUserAccountParam.setCardcustId(cardCustId);
        try {
            Result<JSONObject> result = icbcUserAccountClient.getIcbcAccount(icbcUserAccountParam);

            if (result.getCode() == 200) {
                if (result.getData() == null) {
                    hasAccount = false;
                } else {
                    hasAccount = true;
                }
//            icbcUserAccount =JSONObject.toJavaObject(result.getData(), IcbcUserAccount.class);
            } else {
                logger.error("调用微服务获取开户信息失败-----> " + JSONObject.toJSONString(result));
                return ResultGenerator.genFailResult("系统调用失败");
            }
        } catch (Exception e) {
            logger.error("调用微服务获取开户信息失败-----> {}", e);
            return ResultGenerator.genFailResult("系统调用超时");
        }
        if (hasAccount) {
            params.put("url", hasIcbcAccount);
//            params.put("bankType", "1");// 工行
        } else {
            params.put("url", hasNoIcbcAccount);
        }

        return ResultGenerator.genSuccessResult(params);

    }

    /**
     * 根据用户id，获取红包信息
     *
     * @param cardcustId
     * @return
     */
    @PostMapping("/selectPacketDetail")
    public Result selectPacketDetail(String cardcustId, HttpServletRequest req) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isEmpty(cardcustId)) {
            return ResultGenerator.genFailResult("参数错误");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cardcust_id", cardcustId);
        queryWrapper.orderByDesc("create_time");
        List<PartitionRedPacketUser> list = partitionRedPacketUserService.list(queryWrapper);

        if (list.size() > 0) {
            // 过期或者不是可用状态
//            if (list.get(0).getExpireTime().getTime() < new Date().getTime() || list.get(0).getStatus() != 1) {
            params.put("partitionRedPacketUser", list.get(0));
            // 助力信息
            Page<PartitionAssistDetail> page = new Page<PartitionAssistDetail>(1, 15);
            QueryWrapper operate = new QueryWrapper();
            operate.orderByDesc("create_time");
            operate.ge("create_time", list.get(0).getCreateTime());
            operate.le("create_time", list.get(0).getExpireTime());
            operate.eq("cardcust_id", cardcustId);
            operate.ne("assist_type", 0);
            IPage<PartitionAssistDetail> pageList = partitionAssistDetailService.page(page, operate);
            List<PartitionAssistDetail> temp = pageList.getRecords();
            if (temp.isEmpty()) {
                temp = Lists.newArrayList();
            }
            QueryWrapper cash = new QueryWrapper();
            cash.eq("cardcust_id", cardcustId);
            cash.eq("status", 2);
            List<PartitionPacketCash> cashList = partitionPacketCashService.list(cash);
            for (PartitionPacketCash partitionPacketCash : cashList) {
                PartitionAssistDetail partitionAssistDetail = new PartitionAssistDetail();
                partitionAssistDetail.setCreateTime(partitionPacketCash.getCreateTime());
                partitionAssistDetail.setAmount(partitionPacketCash.getAmount());
                partitionAssistDetail.setId(partitionPacketCash.getId());
                partitionAssistDetail.setCustName(partitionPacketCash.getCustName());
                // 4 提现
                partitionAssistDetail.setAssistType(4);
                partitionAssistDetail.setIconPic(list.get(0).getIconPic());
                temp.add(partitionAssistDetail);
            }

            params.put("partitionAssistDetail", temp);
//            }
        } else {
            params.put("partitionRedPacketUser", null);
            params.put("partitionAssistDetail", null);
        }
        // 累计送出红包
        int count = partitionRedPacketUserService.count();
        params.put("count", count + 1200);
        params.put("currentTime", new Date());
        return ResultGenerator.genSuccessResult(params);
    }

    /**
     * 随机获取十条数据
     *
     * @return
     */
    @PostMapping("/selectRandom")
    public Result selectRandom() {
        Map<String, Object> params = new HashMap<>();
        List<PartitionRedPacketUser> speedName = partitionRedPacketUserService.randomTen();
        params.put("speedName", speedName);
        return ResultGenerator.genSuccessResult(params);
    }

    @PostMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "15") Integer size, PartitionRedPacketUser partitionRedPacketUser, String select_condition_sign) {
        Page<PartitionRedPacketUser> pageTemp = new Page<PartitionRedPacketUser>(current, size);
        QueryWrapper<PartitionRedPacketUser> wrapper = new QueryWrapper<>();
        Class cls = partitionRedPacketUser.getClass();
        Field[] fields = cls.getDeclaredFields();
        String name = "";
        if (StringUtils.isNotEmpty(select_condition_sign)) {
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                try {
                    name = CamelUnderlineUtil.underline(new StringBuffer(f.getName())).toString();
                    if (!f.getName().equals("serialVersionUID")) {
                        if (f.get(partitionRedPacketUser) != null) {
                            if (select_condition_sign.equals("1")) {
                                wrapper.eq(name, f.get(partitionRedPacketUser));
                            } else if (select_condition_sign.equals("2")) {
                                wrapper.like(name, f.get(partitionRedPacketUser).toString());
                            } else if (select_condition_sign.equals("3")) {
                                wrapper.ge(name, f.get(partitionRedPacketUser).toString());
                            } else if (select_condition_sign.equals("4")) {
                                wrapper.le(name, f.get(partitionRedPacketUser).toString());
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
        IPage<PartitionRedPacketUser> page = partitionRedPacketUserService.page(pageTemp, wrapper);
        PageStr pageStr = new PageStr();
        pageStr.setCurrentPage(current);
        pageStr.setShowCount(size);
        pageStr.setTotalPage((int) page.getPages());
        pageStr.setTotalResult((int) page.getTotal());
        Map map = new HashMap();
        map.put("page", page);
        map.put("pageStr", pageStr.getPageStr());
        return ResultGenerator.genSuccessResult(map);
    }
}
