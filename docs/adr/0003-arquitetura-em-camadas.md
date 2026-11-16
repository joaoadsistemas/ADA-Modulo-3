# ADR 0003: Pacotes organizados em camadas

**Status:** aceito

## Contexto
Os dois serviços misturavam controller, entidade, repositório e mensageria no mesmo pacote. Isso dificulta ver o que é regra de negócio e o que é detalhe técnico (HTTP, RabbitMQ, Redis, JPA).

## Decisão
Os dois serviços seguem a mesma estrutura de pacotes:

| Pacote | O que tem | Pode depender de |
|---|---|---|
| `api` (`controller`, `dto`, `handler`) | REST: entrada e saída HTTP e a tradução de exceções para HTTP | `application`, `domain` |
| `application` (`service`, `dto`, `port`) | Casos de uso: criar, reenviar, analisar, consultar, revisar | `domain`, portas |
| `domain` (`model`, `service`, `exception`) | Entidades, regras de fraude e exceções de negócio | nada de web ou de fila |
| `infrastructure` (`persistence`, `messaging`, `config`) | Spring Data, RabbitMQ e a configuração dos beans | as outras camadas |

Pontos principais:
- **Portas e adaptadores nos dois lados da fila.** No servico-transacoes, a aplicação usa a porta `TransacaoEventPublisher`, e o `RabbitTransacaoEventPublisher` implementa essa porta. No servico-antifraude, o `TransacaoListener` traduz a mensagem da fila para `TransacaoParaAnalise`. Assim, a aplicação não conhece o formato da mensagem.
- **`RegrasAntifraude` é Java puro**, sem Spring e sem banco. Ela recebe o valor e a quantidade de transações recentes, e por isso é testada sem mocks. O bean é montado em `infrastructure.config` com os valores do `application.yml`.
- **As exceções de domínio não têm `@ResponseStatus`.** O `GlobalExceptionHandler` da camada `api` traduz cada uma para o HTTP (404, com `ProblemDetail`).

## O que mais considerei
- **Pacote por funcionalidade** (`transacao/`, `analise/`): bom em sistemas grandes, mas com uma funcionalidade só por serviço tudo cairia no mesmo pacote, que era o problema.
- **Hexagonal completa**, com portas também para o repositório e uma entidade JPA separada do modelo de domínio: mais desacoplada, mas dobra o número de classes num projeto deste tamanho. A entidade JPA serve de modelo de domínio, e a aplicação usa o repositório Spring Data direto.

## Consequências
- Trocar o RabbitMQ por outro broker mexe só em `infrastructure.messaging`.
- A anotação JPA continua no modelo de domínio. É uma concessão consciente.
