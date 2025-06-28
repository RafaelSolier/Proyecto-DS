En el terminal de docker crear nuevo contenedor:
docker run -p 5555:5432 --name e2e-postgres -e POSTGRES_PASSWORD=123 -d postg
res
