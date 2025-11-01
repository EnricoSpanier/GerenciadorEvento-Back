# Usar a imagem oficial do PostgreSQL
FROM postgres:16-alpine

# Variáveis de ambiente serão fornecidas em tempo de execução via docker-compose (.env)

# Copiar scripts de inicialização (opcional, para criar tabelas ou inserir dados)
COPY ./init-scripts/ /docker-entrypoint-initdb.d/

# Expor a porta padrão do PostgreSQL
EXPOSE 5432

# Definir o volume para persistência de dados
VOLUME /var/lib/postgresql/data

# Comando padrão para testes
#docker exec -i gerenciador-db \psql -U admin -d meu_banco \-f /docker-entrypoint-initdb.d/99-tests.sql