# Microservice application

This the readme file describe general conception and step for start application local.

## General conception

All precess include three steps. (service-a -> service-b -> service-c). For manage flow use saga orchestration.
Blow on image show how it works 
![](Saga orchestrator flow.jpg)

## Steps for start application locally

1. Clone sources from gitHub repository:

````
git@github.com:Prof0Soft/micro-services-saga.git
````

2. Go to project directory:

````
   cd micro-services-saga
````

3. Run databases using docker compose:

````
docker-compose run
````

4. Build modules from source

````
mvn package <module src dir>
````

6. Run step by step services

````
java -jar <module jar filepath>
````

### How look like process
````
orchestrator logs:
2022-05-20 13:58:45.819  INFO 14804 --- [nio-8080-exec-9] c.s.s.service.impl.OrderServiceImpl      : Start create order process
2022-05-20 13:58:45.826  INFO 14804 --- [nio-8080-exec-9] c.s.s.s.i.SagaOrchestratorServiceImpl    : Initiate saga service.
2022-05-20 13:59:00.818  INFO 14804 --- [nio-8080-exec-3] c.s.s.service.impl.OrderServiceImpl      : Cancel order for id c0a86430-80e1-12a7-8180-e11eadbe0004
2022-05-20 13:59:00.821  INFO 14804 --- [nio-8080-exec-3] c.s.s.s.i.SagaOrchestratorServiceImpl    : Run cancel process with task id c0a86430-80e1-12a7-8180-e11eadbe0004
2022-05-20 13:59:37.184  INFO 14804 --- [nio-8080-exec-2] c.s.s.s.i.SagaOrchestratorServiceImpl    : Run revert flow.

services logs:
[nio-8081-exec-1] c.s.order.service.impl.TaskServiceImpl   : Task c0a86430-80e1-12a7-8180-e11eadbe0004 created
[scheduling-1] c.s.o.service.impl.TaskSchedulerImpl        : Task c0a86430-80e1-12a7-8180-e11eadbe0004 is scheduled for execution
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Looking room for task with id: c0a86430-80e1-12a7-8180-e11eadbe0004
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...0%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...10%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...20%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...30%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...40%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...50%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...60%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...70%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...80%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task processing...90%
[scheduler-pool1] c.s.order.service.impl.TaskExecutorImpl  : Task was canceled.
[scheduler-pool1] c.s.o.s.impl.SagaClientServiceImpl       : send result to orchestrator, task id c0a86430-80e1-12a7-8180-e11eadbe0004
````
### Database

Database initiate from docker compose which is located in root folder in project.
