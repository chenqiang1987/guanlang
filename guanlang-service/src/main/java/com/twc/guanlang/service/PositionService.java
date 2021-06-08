package com.twc.guanlang.service;


import com.twc.guanlang.common.exception.CustomException;
import com.twc.guanlang.entity.position.Position;
import com.twc.guanlang.entity.recharge.Recharge;
import com.twc.guanlang.mapper.entity.PositionMapper;
import com.twc.guanlang.mapper.entity.RechargeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;


/**
 * 位置类
 *
 * @author chenqiang
 */
@Service
public class PositionService {

    @Resource
    private PositionMapper positionMapper;
    public static Position head;
    public static Position tail;
    @Value("${position.offset}")
    Double positionOffset;
    @Resource
    private RechargeMapper rechargeMapper;

    /**
     * 寻找离当前位置最近的充电桩
     *
     * @param x
     * @param y
     */
    public Recharge searchNearestCharge(float x, float y) throws CustomException {
        /**
         * 取最短距离的充电桩
         */
        double minDis = -1;
        Recharge target = null;
        List<Recharge> all = rechargeMapper.selectAll();
        for (Recharge recharge : all) {
            if (!recharge.getStatus().equalsIgnoreCase(Recharge.STATUS_ENUM.NORMAL.getCode())) {
                continue;
            } else if (recharge.getStatus().equalsIgnoreCase(Recharge.STATUS_ENUM.NORMAL.getCode())) {
                double newDis = Double.parseDouble(PositionService.getDisBetween(x, y, recharge.getX(), recharge.getY()));
                if (minDis == -1 || minDis > newDis || minDis == newDis) {
                    target = recharge;
                }
            }
        }
        if (target == null) {
            throw new CustomException("没有找到合适的充电桩!");
        }
        return target;
    }

    /**
     * 是否到达管廊头
     * 空间距离是否在一定范围
     *
     * @return
     */
    public boolean hasReachHead(float x, float y) {
        if (head == null) {
            Example example = new Example(Position.class);
            example.createCriteria().andEqualTo("name", Position.POSITION_NAME_ENUM.HEAD.getCode());
            head = positionMapper.selectOneByExample(example);
        }
        double rs = Double.parseDouble(getDisBetween(x, y, head.getX(), head.getY()));
        if (rs < positionOffset || rs == positionOffset) {
            return true;
        }
        return false;
    }

    /**
     * 是否到达管廊尾
     *
     * @param x
     * @param y
     * @return
     */
    public boolean hasReachTail(float x, float y) {

        if (tail == null) {
            Example example = new Example(Position.class);
            example.createCriteria().andEqualTo("name", Position.POSITION_NAME_ENUM.TAIL.getCode());
            tail = positionMapper.selectOneByExample(example);
        }
        float xx = Math.abs(x - tail.getX());
        float yy = Math.abs(y - tail.getY());
        double rs = Double.parseDouble(getDisBetween(x, y, tail.getX(), tail.getY()));
        if (rs < positionOffset || rs == positionOffset) {
            return true;
        }
        return false;
    }

    public static void main(String s[]) {

        getDisBetween(1, 2, 3, 4);
    }

    public static String formatDouble(Double d) {
        DecimalFormat decimalFormat = new DecimalFormat("#######0.00");
        return decimalFormat.format(d);
    }

    public static String getDisBetween(float x, float y, float x1, float y1) {
        float xx = Math.abs(x - x1);
        float yy = Math.abs(y - y1);
        Double rs = Math.sqrt(Math.abs(xx * xx + yy * yy));
        return formatDouble(rs);
    }
}