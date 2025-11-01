# Comandos Docker Compose e Testes

Este guia contém os comandos essenciais para (re)construir as imagens, subir os serviços, validar a criação do banco e rodar os testes da aplicação, tudo via Docker.

## 1) Build/Rebuild das imagens

```bash
# Reconstroi as imagens do backend e do banco (sem cache)
docker compose build --no-cache
```

## 2) Subir os serviços

```bash
# Sobe db (Postgres) e backend
docker compose up -d
```

## 3) (Opcional) Recriar containers e volume do Postgres
Use quando quiser forçar a reexecução dos scripts de inicialização do banco (db/init-scripts/*).

```bash
# Para tudo e remove containers, rede e volume persistente
# ATENÇÃO: Isso apaga os dados do Postgres (volume)
docker compose down -v

# Reconstroi e sobe novamente
docker compose build --no-cache
docker compose up -d
```

## 4) Testar a criação do banco (scripts de init)

- Verificar o schema da tabela `users`:
```bash
docker compose exec db psql -U admin -d meu_banco -c "\\d+ users"
```

- Rodar o script de testes do banco `99-tests.sql` (ele limpa e valida chaves/relacionamentos):
```bash
docker compose exec db psql -U admin -d meu_banco -f /docker-entrypoint-initdb.d/99-tests.sql
```

Saída esperada (sucesso): `NOTICE:  Testes mínimos concluídos com sucesso`.

## 5) Rodar testes da aplicação (JUnit) dentro de container Maven

- Rodar todos os testes:
```bash
docker run --rm \
  --network gerenciador-net \
  --env-file .env \
  -v "$PWD":/workspace \
  -w /workspace \
  maven:3.9.7-eclipse-temurin-21-jammy \
  mvn -q test
```

- Rodar somente a classe `UserTest`:
```bash
docker run --rm \
  --network gerenciador-net \
  --env-file .env \
  -v "$PWD":/workspace \
  -w /workspace \
  maven:3.9.7-eclipse-temurin-21-jammy \
  mvn -q -Dtest=com.gerenciador.eventos.UserTest test
```

Observações:
- O `--network gerenciador-net` garante que os testes se conectem ao Postgres do `docker-compose` via host `db:5432`.
- O `--env-file .env` repassa as credenciais de banco e a `SPRING_DATASOURCE_URL` para o processo de testes.

## 6) Parar e remover serviços (sem apagar dados)

```bash
docker compose down
```

## 7) Logs úteis (opcional)

```bash
# Logs do banco
docker compose logs -f db

# Logs do backend
docker compose logs -f backend
```