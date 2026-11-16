# ADR 0002: Perfil de execução A (Docker)

**Status:** aceito

## Contexto
O projeto aceita três perfis: A (Docker), B (só Java) e C (conceitual). A máquina de desenvolvimento tem Docker.

## Decisão
**Perfil A.** Um `docker-compose.yml` sobe os dois PostgreSQL, o RabbitMQ, o Redis e os dois serviços. Os serviços são compilados dentro do container com um Dockerfile multi-stage (Maven + Temurin 21), então não é preciso ter Java instalado para rodar.

## O que mais considerei
- **Perfil B:** usaria H2 e filas e caches em memória, o que esconderia justamente os pontos que o projeto quer mostrar (bancos separados, fila de verdade, Redis com TTL).

## Consequências
- O `depends_on` com healthcheck garante que os serviços só sobem quando os bancos, o RabbitMQ e o Redis estão prontos.
- Também dá para subir só a infraestrutura no Docker e rodar os serviços pela IDE (veja o README).
