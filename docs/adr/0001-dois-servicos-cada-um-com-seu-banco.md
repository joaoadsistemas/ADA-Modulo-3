# ADR 0001: Dois serviços, cada um com o seu banco

**Status:** aceito

## Contexto
O sistema antifraude recebe transações e decide se cada uma é aprovada ou reprovada. Receber a transação e analisar são responsabilidades diferentes, que podem crescer e falhar de forma independente.

## Decisão
Dividir em dois serviços Spring Boot (Java 21):
- **servico-transacoes**: recebe a transação pela API, grava no banco `transacoes` e manda para a fila.
- **servico-antifraude**: recebe da fila, aplica as regras e grava o resultado no banco `antifraude`.

Cada serviço tem o **seu próprio PostgreSQL**, em containers separados. Um serviço nunca lê o banco do outro: eles só conversam pela fila. O contrato da mensagem (`TransacaoCriadaEvento`) é copiado nos dois serviços, sem biblioteca compartilhada.

## O que mais considerei
- **Um serviço só (monólito):** mais simples, mas não atende ao projeto e mistura a API com a análise.
- **Um PostgreSQL com dois schemas:** economiza um container, mas deixa fácil um serviço ler a tabela do outro. Dois containers deixam a separação visível.
- **Biblioteca compartilhada com o contrato:** evita duplicar o record, mas acopla o deploy dos dois serviços.

## Consequências
- Os dados ficam duplicados de propósito (conta e valor existem nos dois bancos).
- O resultado da análise não volta para o servico-transacoes: quem quer saber o resultado consulta o servico-antifraude.
- O schema é criado pelo Hibernate (`ddl-auto: update`), o que basta para o projeto. Em produção eu usaria Flyway.
