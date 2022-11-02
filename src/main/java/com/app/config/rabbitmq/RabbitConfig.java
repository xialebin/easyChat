package com.app.config.rabbitmq;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Bean("customContainerFactory1")
    public SimpleRabbitListenerContainerFactory containerFactory1(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(8);//设置线程数
        factory.setMaxConcurrentConsumers(8);//最大线程数
        configurer.configure(factory,connectionFactory);
        return factory;
    }


    @Bean("customContainerFactory2")
    public SimpleRabbitListenerContainerFactory containerFactory2(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(8);//设置线程数
        factory.setMaxConcurrentConsumers(8);//最大线程数
        configurer.configure(factory,connectionFactory);
        return factory;
    }

    @Bean("customContainerFactory3")
    public SimpleRabbitListenerContainerFactory containerFactory3(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(8);//设置线程数
        factory.setMaxConcurrentConsumers(8);//最大线程数
        configurer.configure(factory,connectionFactory);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }

    //Redis MongoDB RabbitMQ Zookeeper Netty

  /*  private final static String PREFIX = "chat.message.";

    @Autowired
    private Environment environment;

    @Bean
    public Queue AMessage() {

        String serverId = environment.getProperty("server.id");
        return new Queue(PREFIX+serverId+".1",true);
    }

    @Bean
    public Queue BMessage() {

        String serverId = environment.getProperty("server.id");
        return new Queue(PREFIX+serverId+".2",true);
    }

    @Bean
    public Queue CMessage() {

        String serverId = environment.getProperty("server.id");
        return new Queue(PREFIX+serverId+".3",true);

        //return new Queue("fanout.C");
    }

    @Bean
    FanoutExchange AFanoutExchange() {
        return new FanoutExchange(PREFIX+"exchange1");
    }

    @Bean
    FanoutExchange BFanoutExchange() {
        return new FanoutExchange(PREFIX+"exchange2");
    }

    @Bean
    FanoutExchange CFanoutExchange() {
        return new FanoutExchange(PREFIX+"exchange3");
    }

    @Bean
    Binding bindingExchangeA(Queue AMessage, FanoutExchange AFanoutExchange) {
        return BindingBuilder.bind(AMessage).to(AFanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue BMessage, FanoutExchange BFanoutExchange) {
        return BindingBuilder.bind(BMessage).to(BFanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue CMessage, FanoutExchange CFanoutExchange) {
        return BindingBuilder.bind(CMessage).to(CFanoutExchange);
    }*/
}
