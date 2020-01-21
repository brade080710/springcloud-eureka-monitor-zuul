package com.jdd.partition.rabbitMq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.jdd.partition.common.MqQueueConstant;
import com.jdd.partition.entity.PartitionAssistDetail;
import com.jdd.partition.entity.PartitionRedPacketUser;
import com.jdd.partition.service.PartitionAssistDetailService;
import com.jdd.partition.service.PartitionRedPacketUserService;
import com.jdd.partition.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
@RabbitListener(queues = MqQueueConstant.PARTITION_PACKET_QUEUE)
public class PartitionPacketListener {
    private final Logger log = LoggerFactory.getLogger(PartitionPacketListener.class);
    @Resource
    private PartitionRedPacketUserService partitionRedPacketUserService;

    @Resource
    private PartitionAssistDetailService partitionAssistDetailService;
    private static int ENDAMOUNT = 9950;
    // 开户数量
    private static int LIMITCOUNT = 20;
//    private static int LIMITCOUNT = 6;

    @RabbitHandler
    public void receiveString(Map<String, String> msg) {
        try {
            log.info("瓜分红包消息中心接收到发送请求-->msg：{} ", msg);
            if (msg.size() > 0) {
                String partitionId = msg.get("partitionId");
                String invitedCardcustId = msg.get("invitedCardcustId");
                String assistType = msg.get("assistType");
                String bankType = msg.get("bankType");
                String userName = msg.get("userName");
                String mobile = msg.get("mobile");
                if (StringUtils.isEmpty(invitedCardcustId) || StringUtils.isEmpty(assistType)) {
                    log.error("MQ消息获取用户助力钱包信息有空值");
                    return;
                }
                // 用户注册助力
                if ("2".equals(assistType)) {
                    // 获取当前瓜分红包信息
                    PartitionRedPacketUser partitionRedPacketUser = partitionRedPacketUserService.getById(partitionId);
                    if (partitionRedPacketUser == null) {
                        log.error("MQ消息获取用户助力钱包信息有误");
                        return;
                    } else {
                        if (partitionRedPacketUser.getExpireTime().getTime() < new Date().getTime()) {
                            log.info("助力红包已过期");
                            return;
                        }
                    }
                    if (partitionRedPacketUser.getTotalAmount() >= 10000) {
                        log.info("MQ消息注册助力红包已到100元");
                        return;
                    }
                    // 钱包目前金额
                    int totalAmount = partitionRedPacketUser.getTotalAmount();
                    int assistAmount = 0;

                    // 获取用户注册助力的数量
                    QueryWrapper countQueryWrapper = new QueryWrapper();
                    countQueryWrapper.eq("partition_id", partitionId);
                    countQueryWrapper.eq("assist_type", 2);
                    int count = partitionAssistDetailService.count(countQueryWrapper);
                    if (count > 4) {
                        log.info("新用户邀请注册超过5个无助力");
                        return;
                    }

                    if (totalAmount < ENDAMOUNT) {
                        assistAmount = RandomUtil.randomFromTo(1, ENDAMOUNT - totalAmount);
                    } else {
                        assistAmount = 1;
                    }
                    PartitionAssistDetail partitionAssistDetail = new PartitionAssistDetail();
                    UUID uuid = UUID.randomUUID();
                    partitionAssistDetail.setId(uuid.toString());
                    partitionAssistDetail.setAssistType(2);
                    partitionAssistDetail.setCardcustId(partitionRedPacketUser.getCardcustId());
                    partitionAssistDetail.setPartitionId(partitionId);
                    partitionAssistDetail.setInvitedCardcustId(invitedCardcustId);
                    partitionAssistDetail.setAmount(assistAmount);
                    if (StringUtils.isNotEmpty(userName)) {
                        partitionAssistDetail.setCustName(userName);
                    } else {
                        partitionAssistDetail.setCustName(mobile);
                    }
                    partitionAssistDetail.setCustTel(mobile);
                    partitionAssistDetail.setCreateTime(new Date());
                    partitionAssistDetail.setRemark("注册发起助力红包奖励");
                    partitionAssistDetail.setPriCustTel(partitionRedPacketUser.getCustTel());
                    partitionAssistDetail.setPriCustName(partitionRedPacketUser.getCustName());
                    boolean save = partitionAssistDetailService.save(partitionAssistDetail);
                    // 更新助力红包
                    if (save) {
                        partitionRedPacketUser.setTotalAmount(partitionRedPacketUser.getTotalAmount() + assistAmount);
                        partitionRedPacketUserService.updateById(partitionRedPacketUser);
                    }

                    // 用户开户助力
                } else if ("1".equals(assistType)) {
                    if (StringUtils.isEmpty(bankType)) {
                        log.error("MQ消息开户助力开户银行类型为--->{}", bankType);
                        return;
                    }
                    QueryWrapper query = new QueryWrapper();
                    query.eq("invited_cardcust_id", invitedCardcustId);
                    query.eq("assist_type", 1);
                    List<PartitionAssistDetail> listEleAssist = partitionAssistDetailService.list(query);
                    if (listEleAssist.size() > 0) {
                        log.info("当前用户开通电子账户已助力过");
                        return;
                    }
                    // 预开户助力信息
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("invited_cardcust_id", invitedCardcustId);
                    queryWrapper.eq("assist_type", 0);
                    queryWrapper.orderByDesc("create_time");
                    List<PartitionAssistDetail> listReadyAssist = partitionAssistDetailService.list(queryWrapper);
                    if (listReadyAssist.size() == 0) {
                        log.info("MQ消息点击开户按钮，助力信息关系不存在");
                        return;
                    }

                    // 获取当前瓜分红包信息
                    PartitionRedPacketUser partitionRedPacketUser = partitionRedPacketUserService.getById(listReadyAssist.get(0).getPartitionId());
                    if (partitionRedPacketUser == null) {
                        log.error("MQ消息开户获取用户助力钱包信息有误");
                        return;
                    } else {
                        if (partitionRedPacketUser.getExpireTime().getTime() < new Date().getTime()) {
                            log.info("MQ消息开户助力红包已过期");
                            return;
                        }
                    }

                    if (partitionRedPacketUser.getTotalAmount() >= 10000) {
                        log.info("MQ消息开户助力红包已到100元");
                        return;
                    }
                    // 钱包目前金额
                    int totalAmount = partitionRedPacketUser.getTotalAmount();
                    int assistAmount = 0;

                    // 判断当前红包开户助力情况
                    int bankCount = Integer.parseInt(bankType) == 1 ? 1 : 2;
                    int otherBank = 0;
                    int icbc = 0;
                    QueryWrapper otherQuery = new QueryWrapper();
                    otherQuery.eq("partition_id", listReadyAssist.get(0).getPartitionId());
                    otherQuery.eq("cardcust_id", partitionRedPacketUser.getCardcustId());
                    otherQuery.ge("create_time", partitionRedPacketUser.getCreateTime());
                    otherQuery.le("create_time", partitionRedPacketUser.getExpireTime());
                    otherQuery.in("bank_type", Lists.newArrayList(0, 1));
                    List<PartitionAssistDetail> list = partitionAssistDetailService.list(otherQuery);
                    for (PartitionAssistDetail partitionAssistDetail : list) {
                        if (partitionAssistDetail.getBankType() == 1) {
                            icbc = icbc + 1;
                        } else {
                            otherBank = otherBank + 1;
                        }

                    }

                    int inviteTotal = otherBank * 2 + icbc + bankCount;
                    if (inviteTotal >= LIMITCOUNT) {
                        assistAmount = 10000 - totalAmount;
                        // 设置 100元
                        totalAmount = 10000;

                        partitionRedPacketUser.setTotalAmount(totalAmount);
                        partitionRedPacketUserService.updateById(partitionRedPacketUser);

                        PartitionAssistDetail partitionAssistDetail = listReadyAssist.get(0);
                        partitionAssistDetail.setAssistType(1);
                        partitionAssistDetail.setAmount(assistAmount);
                        partitionAssistDetail.setCreateTime(new Date());
                        partitionAssistDetail.setRemark("开户发起助力红包奖励");
                        partitionAssistDetail.setBankType(Integer.parseInt(bankType));
                        partitionAssistDetail.setPriCustTel(partitionRedPacketUser.getCustTel());
                        partitionAssistDetail.setPriCustName(partitionRedPacketUser.getCustName());
                        partitionAssistDetailService.updateById(partitionAssistDetail);
                        return;
                    } else {
                        if (totalAmount < ENDAMOUNT) {
                            assistAmount = RandomUtil.randomFromTo(1, ENDAMOUNT - totalAmount);
                        } else {
                            assistAmount = 1;
                        }

                        PartitionAssistDetail partitionAssistDetail = listReadyAssist.get(0);
                        partitionAssistDetail.setAssistType(1);
                        partitionAssistDetail.setAmount(assistAmount);
                        partitionAssistDetail.setCreateTime(new Date());
                        partitionAssistDetail.setRemark("开户发起助力红包奖励");
                        partitionAssistDetail.setBankType(Integer.parseInt(bankType));
                        partitionAssistDetail.setPriCustTel(partitionRedPacketUser.getCustTel());
                        partitionAssistDetail.setPriCustName(partitionRedPacketUser.getCustName());
                        boolean save = partitionAssistDetailService.updateById(partitionAssistDetail);
                        // 更新助力红包
                        if (save) {
                            partitionRedPacketUser.setTotalAmount(partitionRedPacketUser.getTotalAmount() + assistAmount);
                            partitionRedPacketUserService.updateById(partitionRedPacketUser);
                        }

                    }

                }

            }

        } catch (Exception e) {
            log.error("获取rabbitmq消息处理瓜分红包失败: {}", e);
        }

    }
}
