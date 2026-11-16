# ADR 0004: RabbitMQ como fila entre os serviços

**Status:** aceito

## Contexto
O servico-transacoes precisa entregar cada transação ao servico-antifraude sem esperar pela análise e sem perder a mensagem se o antifraude estiver fora do ar.

## Decisão
Usar o **RabbitMQ** com uma fila durável, `antifraude.transacoes`:
- **Quem manda:** o servico-transacoes (`RabbitTransacaoEventPublisher`, que implementa a porta `TransacaoEventPublisher`). Ele publica no exchange padrão, com a routing key igual ao nome da fila. A mensagem vai em JSON, com o `messageId` igual ao `transacaoId`.
- **Quem recebe:** o servico-antifraude (`TransacaoListener`, com `@RabbitListener`).
- Os dois serviços declaram a fila. Declarar de novo é inofensivo, e assim a ordem em que eles sobem não importa.
- O ack é automático no fim do listener: se der exceção, a mensagem volta para a fila e é entregue de novo. Por isso quem recebe precisa ser idempotente (ADR 0005).

## O que mais considerei
- **Kafka:** é ótimo para guardar o histórico de eventos e reprocessar, mas é mais pesado de rodar e operar, e o projeto só precisa de uma fila ponto a ponto.
- **Chamada HTTP síncrona:** mais simples, mas a transação falharia sempre que o antifraude estivesse fora do ar.

## Consequências
- O servico-transacoes grava no banco e depois publica, fora de uma transação única. Se o RabbitMQ cair entre as duas coisas, a transação fica gravada sem ir para a fila. O endpoint `POST /transacoes/{id}/reenviar` permite mandar de novo. A solução completa seria o padrão Outbox, que deixei fora do escopo.
- Sem DLQ: uma mensagem que sempre falha volta para a fila indefinidamente. Retry com DLQ é um extra opcional do projeto.
