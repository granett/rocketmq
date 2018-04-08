package rocketmq.rocketmq.broadcasting;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


public class Consumer {

	public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("example_group_name");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr("192.168.1.205:9876");

        //set to broadcast mode
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.subscribe("TopicBroad", "TagA");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                ConsumeConcurrentlyContext context) {
                System.out.println(Thread.currentThread().getName() + " 收到新消息: ");
                for(MessageExt me:msgs){  
                    try {  
                        System.out.println(new String(me.getBody(),"utf-8"));  
                    } catch (UnsupportedEncodingException e) {  
                        e.printStackTrace();  
                    }  
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Broadcast Consumer Started.");
    }
}
