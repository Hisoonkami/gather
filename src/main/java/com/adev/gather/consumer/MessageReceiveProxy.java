package com.adev.gather.consumer;

import com.adev.gather.domain.Order;
import com.adev.gather.service.OrderService;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
@Component
public class MessageReceiveProxy {

    @Autowired
    private OrderService orderService;

    @Transactional
    public void messageReceiveProxy(Channel channel, Message message) {
        System.err.println("order收到  : " + message.getBody() +"消费时间"+new Date());
        try {
            Map<String,Object> orderInfo = JSONObject.parseObject(message.getBody(), Map.class);
            Object id=orderInfo.get("id");
            Object exchange=orderInfo.get("exchange");
            Object currencyPair=orderInfo.get("currencyPair");
            Object price=orderInfo.get("price");
            Object number=orderInfo.get("number");
            Object amount=orderInfo.get("amount");
            //状态和订单号进行幂等性判断防止应用中途挂掉或异常，MQ没有收到ACK确认导致重发消息数据库重复添加
            Order order = orderService.findById(String.valueOf(id)).orElse(null);//如果已经存在就更新
            if(null==order){
                order=new Order();
            }
            order.setExchange(String.valueOf(exchange));
            order.setCurrencyPair(String.valueOf(currencyPair));
            order.setPrice(new BigDecimal(String.valueOf(price)));
            order.setNumber(new BigDecimal(String.valueOf(number)));
            order.setAmount(new BigDecimal(String.valueOf(amount)));
            orderService.save(order);
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);//deliveryTag是tag的id，由生产者生成
        } catch (IOException e) {
        }
    }
}
