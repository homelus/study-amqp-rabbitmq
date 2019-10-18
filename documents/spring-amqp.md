## [Receiving messages](https://docs.spring.io/spring-amqp/docs/1.6.11.RELEASE/reference/html/_reference.html#receiving-messages)

### Introduction

메시지를 받는 것은 언제나 보내는 것보다 좀 더 복잡합니다. 여기에 **Message** 를 받는 두가지 방법이 있습니다. 

간단한 방법은 한번에 하나씩 폴링 메서드를 호출해 하나의 **Message** 를 폴링하는 것 입니다. 

더 복잡하지만 더 일반적인 접근은 listener 를 등록해서 **Message** 를 그때 그때 비동기적으로 받는 것입니다.

우리는 각각의 접근 방법을 다음 하위 섹션에서 살펴볼 것입니다.

### [Polling Consumer](https://docs.spring.io/spring-amqp/docs/1.6.11.RELEASE/reference/html/_reference.html#polling-consumer)

**AmqpTemplate** 은 폴링된 메시지 수신에 사용될 수 있습니다. 사용가능한 메시지가 없을 때 blocking 없이 null 이 즉시 반환된다. 

버전 1.5 부터 **receiveTimeout**(milliseconds) 을 설정할 수 있고, 수신 메서드는 메시지를 기다리며 최대로 오랫동안 blocking 될 것입니다.

0보다 작은 값을 설정하면 무한으로 block 됩니다. (적어도 브로커의 커넥션이 끊어질때까지)

버전 1.6 부터 각 호출마다 timeout 을 허용도록 전달하는 다양한 **receive** 메서드가 소개되었습니다.

> :fire: Caution<br>
> 수신 작업이 각 메시지에 새로운 **QueueingConsumer** 를 만드므로 이 기술은 실제로 대량 환경에 적합하지 않습니다.<br>
> 비동기 consumer 를 이용하는 방법을 고민하거나 **receiveTimeout** 을 0으로 설정하는 것을 고려해보세요

사용 가능한 4가지 간단한 *receive* 메서드가 있습니다. 보내는 쪽에서 Exchange 처럼 템플릿 자체에 큐 속성을 직접 설정해야 하는 메서드가 있고 런타임에 큐의 매개 변수를 설정할 수 있는 메서드가 있습니다.

1.6 버전에는 기본 요청마다 **receiveTimeout** 를 재정의한 **timeoutMillis** 가 허용되는 다양한 것들이 소개되었습니다.

```java
Message receive() throws AmqpException;

Message receive(String queueName) throws AmqpException;

Message receive(long timeoutMillis) throws AmqpException;

Message receive(String queueName, long timeoutMillis) throws AmqpException;
```

메시지를 보내는 예제들 처럼 **AmqpTemplate** 은 **Message** 인스턴스 대신에 POJO 객체를 받을 수 있는 편리한 메소드들을 몇몇 가지고 있다
또한 Template 의 구현체는 반환받는 **Object** 를 만드는데 **Message Converter** 를 이용하여 커스터마이즈하는 방법을 제공해줄 것이다.

```java
Object receiveAndConvert() throws AmqpException;

Object receiveAndConvert(String queueName) throws AmqpException;

Message receiveAndConvert(long timeoutMillis) throws AmqpException;

Message receiveAndConver(String queueName, long timeoutMillis) throws AmqpException;
```
**sendAndReceive** 메서드와 유사하게 1.3 버전부터 **AmqpTemplate** 은 메시지에 동기적으로 수신, 처리, 응답하기 위한 몇개의 편리한 **receiveAndReply** 메서드를 가지고 있다.

```java
<R, S> boolean receiveAndReply(ReceiveAndReplyCallback<R, S> callback) throws AmqpException;

<R, S> boolean receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback) throws AmqpException;

<R, S> boolean receiveAndReply(ReceiveAndReplyCallback<R, S> callback, String replyExchange, String replyRoutingKey) throws AmqpException;

<R, S> boolean receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback, String replyExchange, String replyRoutingKey) throws AmqpException;

<R, S> boolean receiveAndReply(ReceiveAndReplyCallback<R, S> callback, ReplyToAddressCallback<S> replyToAddressCallback) throws AmqpException;

<R, S> boolean receiveAndReply(String queueName, ReceiveAndReplyCallback<R, S> callback, ReplyToAddressCallback<S> replyToAddressCallback) throws AmqpException;
```



