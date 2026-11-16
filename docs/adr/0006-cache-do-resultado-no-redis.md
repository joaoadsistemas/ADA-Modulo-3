# ADR 0006: Cache do resultado da análise no Redis

**Status:** aceito

## Contexto
O resultado de uma análise é consultado muitas vezes (pelo app, pelo atendimento, pelo checkout) e quase nunca muda depois de gravado.

## Decisão
Cache `analises` no **Redis**, usando o Spring Cache (`@Cacheable` / `@CacheEvict` em `ConsultaAnaliseService`):
- **O que guarda:** o `AnaliseResponse` (resultado, motivo, valor, data) de `GET /analises/{transacaoId}`. A chave no Redis é `antifraude:analises::<transacaoId>`.
- **Por quanto tempo:** **10 minutos** (`spring.cache.redis.time-to-live: 10m`). É o tempo em que uma transação recente é mais consultada.
- **Quando apaga:**
  - quando vence o TTL;
  - na **revisão manual** (`PUT /analises/{transacaoId}/revisao`), com `@CacheEvict`, para que ninguém leia o resultado antigo;
  - "não encontrado" (404) e valores nulos **não** entram no cache (`cache-null-values: false`). Assim, uma consulta feita antes de a análise chegar pela fila não fica presa no cache.

## O que mais considerei
- **Cache em memória (Caffeine):** mais rápido, mas cada instância teria o seu cache, e o evict de uma não chegaria às outras.
- **`@CachePut` na revisão:** atualizaria o cache em vez de apagar. Preferi apagar porque é mais simples de explicar, e a próxima leitura busca o valor certo no banco.
- **TTL maior ou sem TTL:** como o evict cobre a revisão, um TTL maior funcionaria. Mantive 10 minutos para o Redis não acumular chaves de transações antigas.

## Consequências
- Os valores são gravados com a serialização Java padrão (o record é `Serializable`). Eles não são legíveis no `redis-cli`, mas o `TTL` e o `EXISTS` mostram o comportamento do cache.
- Se o Redis cair, a consulta falha. Aceito no escopo do projeto.
