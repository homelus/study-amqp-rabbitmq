### [Receiving messages](https://docs.spring.io/spring-amqp/docs/1.6.11.RELEASE/reference/html/_reference.html#receiving-messages)

#### Introduction

메시지를 받는 것은 언제나 보내는 것보다 좀 더 복잡합니다. 여기에 **Message** 를 받는 두가지 방법이 있습니다. 

간단한 방법은 한번에 하나씩 폴링 메서드를 호출해 하나의 **Message** 를 폴링하는 것 입니다. 

더 복잡하지만 더 일반적인 접근은 listener 를 등록해서 **Message** 를 그때 그때 비동기적으로 받는 것입니다.

우리는 각각의 접근 방법을 다음 하위 섹션에서 살펴볼 것입니다.

#### Polling Consumer

**AmqpTemplate** 은 폴링된 메시지 수신에 사용될 수 있습니다. 사용가능한 메시지가 없을 때 blocking 없이 null 이 즉시 반환된다. 

버전 1.5 부터 **receiveTimeout**(milliseconds) 을 설정할 수 있고, 수신 메서드는 메시지를 기다리며 최대로 오랫동안 blocking 될 것입니다.

0보다 작은 값을 설정하면 무한으로 block 됩니다. (적어도 브로커의 커넥션이 끊어질때까지)

버전 1.6 부터 각 호출마다 timeout 을 허용도록 전달하는 다양한 **receive** 메서드가 소개되었습니다.
