# ADR 0005: Idempotência de quem recebe pelo transacaoId

**Status:** aceito

## Contexto
O RabbitMQ garante entrega "pelo menos uma vez": a mesma mensagem pode chegar duas vezes (reentrega depois de erro, reenvio do produtor). Se a mesma mensagem chegar duas vezes, a análise tem de ser gravada uma vez só.

## Decisão
O `transacaoId` (UUID gerado pelo servico-transacoes) é a chave de idempotência. Há duas barreiras no `AnaliseService`:
1. **Consulta antes de processar:** se `existsByTransacaoId` for verdadeiro, a mensagem é ignorada e só gera um log.
2. **Unique constraint** `uk_analise_transacao_id` na coluna `transacao_id`: se duas cópias passarem pela consulta ao mesmo tempo, o banco recusa a segunda. A `DataIntegrityViolationException` é tratada como "mensagem repetida", e a mensagem recebe o ack normalmente.

## O que mais considerei
- **Só a consulta:** falha quando duas cópias são processadas em paralelo.
- **Só a constraint:** funciona, mas toda repetição viraria uma exceção. A consulta deixa o caso comum mais limpo.
- **Tabela separada de mensagens processadas, ou chave no Redis:** fazem sentido quando o processamento não gera um registro natural. Aqui a própria análise já é esse registro.

## Consequências
- Para ver a idempotência funcionando, basta chamar `POST /transacoes/{id}/reenviar` várias vezes: o banco `antifraude` continua com uma linha só.
- Testado em `AnaliseServiceTest`.
