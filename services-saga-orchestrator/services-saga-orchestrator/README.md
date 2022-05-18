
For a database quickstart, you can use Docker command:

```
docker run --name orchestrator -e POSTGRES_PASSWORD=orchestrator -e POSTGRES_USER=orchestrator -e POSTGRES_DB=orchestrator -d -p 5436:5432 postgres
```