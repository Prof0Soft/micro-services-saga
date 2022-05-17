
For a database quickstart, you can use Docker command:

```
docker run --name servicec -e POSTGRES_PASSWORD=servicec -e POSTGRES_USER=servicec -e POSTGRES_DB=servicec -d -p 5432:5434 postgres
```