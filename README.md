# Rabbit MQ

> [참조](https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html)

RabbitMQ 는 메시지를 수용하고 전달하는 메시지 브로커이다.
RabbitMQ 를 우체국으로 생각해 봅시다.
만약 보내고 싶은 메일을 우편함에 넣어 놓으면 우편배달부가 메일을 수신자에게 전달한다는 것을 확신할 수 있습니다.
이 비유에서 Rabbit MQ 는 우편함, 우체국, 우편배달부 역할을 합니다.

RabbitMQ 와 우체국사이에 가장 큰 다른점은 종이를 다루지 않는 다는 점 입니다. 그 대신 메시지의 이진 blobs 을 수용하고, 저장하고, 보냅니다.
일반적으로 메시징과 RabbitMQ  는 일부 전문 용어를 사용합니다.

- *Producing* 은 보내는 것 이상을 의미하지 않습니다. 메시지를 보내는 프로그램은 생산자 입니다.
- *queue* 는 RabbitMQ 내부에 존재하는 우편함을 위한 이름입니다. 메시지들이 RabbitMQ 와 Application 을 통해 전달된다 하더라도 queue 내부에만 저장되어 질 수 있습니다. *queue* 는 단지 호스트의 메모리와 디스크 제한에 의해 바인딩되며 본직적으로 큰 메시지 버퍼입니다. 많은 생산자들은 하나의 *큐*에 메시지를 전달할 수 있고 많은 소비자들은 하나의 *큐*로부터 데이터를 받는 것을 시도할 수 있습니다.
- *Consuming* 은 수신과 비슷한 의미를 갖습니다. 소비자는 메시지 수신을 기다리는 프로그램입니다.

생산자, 소비자와 브로커는 같은 곳에 존재하지 않는데 실제 대부분의 애플리케이션에서 떨어져있습니다.
애플리케이션은 생산자이면서 소비자일 수 있습니다.

### [Spring AMQP 번역](https://docs.spring.io/spring-amqp/docs/1.6.11.RELEASE/reference/html/) (1.6.11.RELEASE)

#### [3.1.1.AMQP Abstraction](/documents/3.1.1.AMQP-Abstraction.md)

#### [3.1.2.Connection and Resource Management](/documents/3.1.2.Connection-and-Resource-Management.md)

#### [3.1.4.AmqpTemplate.md](/documents/3.1.4.AmqpTemplate.md)

#### [3.1.6.Receiving Messages](/documents/3.1.6.Receiving-messages.md)

#### [3.1.10.Configuring the broker](/documents/3.1.10.Configuring-the-broker.md)

#### [3.4.Testing Support](/documents/3.4.Testing-support.md)
