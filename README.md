# statistics
microservice statistics

```bash
mvn clean package
docker build -t statistics .   
docker tag statistics tomaz12345/statistics:v1.2 
docker push tomaz12345/statistics:v1.2
docker network ls  
docker network rm rso
docker network create rso
docker run -d --name pg-expenses -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=expenses -p 5432:5432 --network rso postgres:13 #docker inspect pg-expenses
docker run -p 8081:8081 --network rso -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-expenses:5432/expenses tomaz12345/statistics
```