
For a database quickstart, you can use Docker command:

```
docker run --name servicea -e POSTGRES_PASSWORD=servicea -e POSTGRES_USER=servicea -e POSTGRES_DB=servicea -d -p 5432:5433 postgres
```