# OVNG Service-template based on Quarkus
The project is a sample, used as a reference for other project in OVNG.

## The project structure
![Alt text](image/repository-service-pattern-diagram.png?raw=true "Title")

The project is separated into 3 primary layers.
- Layer 1: `Controller`:  works as a gateway between your input and the domain logic, is decides what to do with the input and how to output the response. 
- Layer 2: `Service`: contains your business logic of the project. It communicates with databases, connectors, or orther services by REST APIs.
- Layer 3: There are several kinds of it:
    - `Repository` or `Entity`: Define how you can interact with your database. 
    - `Connector` to communicate to the Kafka or MQTT broker.
    - `Rest` to communicate to other services by REST.

The `controller`s role is responsible for publishing APIs and handle routing in the project. It must not handle any business logic.
It will make the call to `service`s to delegate the business logic. Depends on what the project should do to handle the request, 
it can call to the database to get the data by `repository`, sends data to a broker or call to another service by REST.

## Extensions in the project.
The project already contained some extensions in `pom.xml`. Almost of them are `Quarkus extension`. They are optimized to build 
a native binary that can run directly on the machine without JVM. If you want use a Jar that not support by Quarkus,
please take a review carefully.  

Below list contains some libraries used in the project:
- `quarkus-resteasy`
- `quarkus-arc`
- `quarkus-junit5`
- `quarkus-assured`
- `quarkus-mutiny`
- `quarkus-resteasy-mutiny`
- `quarkus-smallrye-health`
- `quarkus-smallrye-metrics`
- `quarkus-hibernate-orm-panache`
- `quarkus-jdbc-postgresql`
- `quarkus-logging-json`
- `quarkus-flyway`
- `quarkus-resteasy-jsonb`
- `quarkus-smallrye-reactive-messaging-kafka`
- `lombok`

> NOTE: Please review above list and remove libraries not used in your project or add the new one if need.

## How to clone to create new project
- Copy to this and rename the project.
    - Rename the project name, package name
    - Rename in `pom.xml`
    - Rename in `Dockerfile` in `src/main/docker`
    - Rename in kubernetes configuration files.
    
## How to run in dev mode
The command supports hot reload, so you can take the effect immediately after changing code without restart the server.
```shell script
./mvnw compile quarkus:dev
```

## Create image
First, you need to create a native executable binary.
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

Then, create the image by running:
```shell script
docker build -f src/main/docker/Dockerfile.native -t quarkus/service-template .
```

## Deploy to k8s
> NOTE: Please make sure you have an image of the project available in a registry, that the k8s cluster can pull from.

- The `service-template` application is a rest server project. That has CRUD APIs and used PostgreSQL to store the data. So, you need a Postgres database running in kubernetes. 
If not, don't worry, you can use the configuration `postgres.yaml` in `src/main/kubernetes/test` directory to create a PostgreSQL data base in k8s.

```shell script
kubectl apply -f src/main/kubernetes/test/postgres.yaml
```
    
> NOTE: The configuration `postgres.yaml` is using deployment to deploy the database and just store data on ephemeral storage. That means, no data is persisted when the Pod restarted or redeploy. 
> Just use it for testing.

- To deployment the application, just run:
```shell script
kubectl apply -f src/main/kubernetes/test/service-template.yaml
```

## Test the application
- The application created an ingress that publish the application to the outside of kubernetes. If the kubernetes already support an ingress controller,
try to run:
```shell script
curl {k8s-ingress-public-address}/api/v1alpha1/service-template/fruits
```
- In case your kubernetes cluster don't have any ingress controller available, you can try this way:
```shell script
kubectl run --image=curlimages/curl:7.74.0 --namespace ovng-test --command --rm --tty -i test-curl -- sh
```
  It runs a pod in the kubernetes and attach to the terminal of the pod. Then try with curl:
```shell script
curl service-template:8081/api/v1alpha1/service-template/fruits
```

## How to get all configuration in the project?
Use below command to export all available configuration and default config to a file `application.properties.temp` in the resource directory.
```shell script
./mvnw quarkus:generate-config -Dfile=application.properties.temp
```

## Reference link
- [Quarkus guides](https://quarkus.io/guides/): Guides for new one that don't know Quarkus. It's very easy to learn. 