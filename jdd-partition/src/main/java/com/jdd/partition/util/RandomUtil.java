package com.jdd.partition.util;

import java.util.Random;

public class RandomUtil {
    private static Random random;

    /**
     * 获取一个 区间 的随机 整数
     * @param from 开始(包含)
     * @param to 结束(包含)
     * @return 随机值
     */
    public static Integer randomFromTo(Integer from, Integer to){
        return getRandom().nextInt(to -from + 1) + from;
    }

    private static Random getRandom(){
        if(random == null){
            synchronized (RandomUtil.class){
                if(random == null){
                    random = new Random();
                }
            }
        }
        return random;
    }

}
