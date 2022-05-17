
For a database quickstart, you can use Docker command:

```
docker run --name serviceb -e POSTGRES_PASSWORD=serviceb -e POSTGRES_USER=serviceb -e POSTGRES_DB=serviceb -d -p 5432:5434 postgres
```