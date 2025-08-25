# K8s Resiliente

## Estrutura de arquivos
```
k8s/
 ├── README.md
 ├── Makefile
 ├── 00-namespace.yml
 ├── zookeeper-statefulset.yml
 ├── kafka-statefulset.yml
 ├── producer-deployment.yml
 ├── consumer-deployment.yml
```

## Comandos do Makefile
```bash
$ make up
$ make status
$ make logs-kafka
$ make logs-producer
$ make logs-consumer
$ make down
$ make clean
```

## Executando...

1. Verificar o estado do Minikube:

    Comando:
    ```bash
    chat-app-k8s-resilient (main) $ minikube status
    ```
    Saída:
    ```bash
    minikube
    type: Control Plane
    host: Stopped
    kubelet: Stopped
    apiserver: Stopped
    kubeconfig: Stopped
    ```

    Se o Minikube estiver parado, iniciar o Minikube:
    ```bash
    chat-app-k8s-resilient (main) $ minikube start
    ```
    Saída:
    ```bash
    😄  minikube v1.36.0 on Ubuntu 24.04 (docker/amd64)
    ✨  Using the docker driver based on existing profile
    👍  Starting "minikube" primary control-plane node in "minikube" cluster
    🚜  Pulling base image v0.0.47 ...
    🔄  Restarting existing docker container for "minikube" ...
    🐳  Preparing Kubernetes v1.33.1 on Docker 28.1.1 ...
    🔎  Verifying Kubernetes components...
        ▪ Using image gcr.io/k8s-minikube/storage-provisioner:v5
    🌟  Enabled addons: default-storageclass, storage-provisioner
    🏄  Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default
    ```

1. Criar as imagens

    No diretório chat-app:
    ```bash
    chat-app (main) $ ./build-image.sh 
    ```
    Saída:
    ```bash
    [+] Building 1.8s (8/8) FINISHED                                                                                                                                                           docker:default
    => [internal] load build definition from Dockerfile                                                                                                                                                 0.0s
    => => transferring dockerfile: 215B                                                                                                                                                                 0.0s
    => [internal] load metadata for docker.io/library/openjdk:11-jre-slim                                                                                                                               0.0s
    => [internal] load .dockerignore                                                                                                                                                                    0.0s
    => => transferring context: 2B                                                                                                                                                                      0.0s
    => [1/3] FROM docker.io/library/openjdk:11-jre-slim                                                                                                                                                 0.0s
    => [internal] load build context                                                                                                                                                                    1.7s
    => => transferring context: 20.33MB                                                                                                                                                                 1.6s
    => CACHED [2/3] WORKDIR /app                                                                                                                                                                        0.0s
    => CACHED [3/3] COPY target/producer-service-jar-with-dependencies.jar producer-service.jar                                                                                                         0.0s
    => exporting to image                                                                                                                                                                               0.0s
    => => exporting layers                                                                                                                                                                              0.0s
    => => writing image sha256:90d2139ffad532c3aa0dc167c10ad55c8d905325018c65b8e8265501d173856a                                                                                                         0.0s
    => => naming to docker.io/library/producer-service:latest                                                                                                                                           0.0s
    [+] Building 1.6s (8/8) FINISHED                                                                                                                                                           docker:default
    => [internal] load build definition from Dockerfile                                                                                                                                                 0.0s
    => => transferring dockerfile: 215B                                                                                                                                                                 0.0s
    => [internal] load metadata for docker.io/library/openjdk:11-jre-slim                                                                                                                               0.0s
    => [internal] load .dockerignore                                                                                                                                                                    0.0s
    => => transferring context: 2B                                                                                                                                                                      0.0s
    => [1/3] FROM docker.io/library/openjdk:11-jre-slim                                                                                                                                                 0.0s
    => [internal] load build context                                                                                                                                                                    1.5s
    => => transferring context: 20.05MB                                                                                                                                                                 1.5s
    => CACHED [2/3] WORKDIR /app                                                                                                                                                                        0.0s
    => CACHED [3/3] COPY target/consumer-service-jar-with-dependencies.jar consumer-service.jar                                                                                                         0.0s
    => exporting to image                                                                                                                                                                               0.0s
    => => exporting layers                                                                                                                                                                              0.0s
    => => writing image sha256:62e380c14d04279bee0617e43abab8dd70d5ce8c4e1d26bba7048f5da299924c                                                                                                         0.0s
    => => naming to docker.io/library/consumer-service:latest  
     ```

1. Subir no Minikube
    ```bash
    chat-app-k8s-resilient (main) $ make up
    ```
    Saída:
    ```bash
    Carregando imagens locais para o Minikube...
    minikube image load producer-service:latest
    minikube image load consumer-service:latest
    Criando namespace (se não existir)...
    kubectl create namespace kafka-demo || true
    namespace/kafka-demo created
    Aplicando manifests...
    kubectl apply -n kafka-demo -f .
    Warning: resource namespaces/kafka-demo is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.
    namespace/kafka-demo configured
    service/consumer-service created
    deployment.apps/consumer-service created
    service/kafka created
    statefulset.apps/kafka created
    service/producer-service created
    deployment.apps/producer-service created
    service/zookeeper created
    statefulset.apps/zookeeper created
    ```

1. Para verificar o estado:
    ```bash
    chat-app-k8s-resilient (main) $ make status
    ```
    Saída:
    ```bash
    Status dos Pods:
    kubectl get pods -n kafka-demo
    NAME                               READY   STATUS    RESTARTS   AGE
    consumer-service-bf64f8d44-cfmvl   1/1     Running   0          8m50s
    kafka-0                            1/1     Running   0          8m50s
    producer-service-646cb6c5d-g2lkm   1/1     Running   0          8m50s
    zookeeper-0                        1/1     Running   0          8m49s
    Status dos Services:
    kubectl get svc -n kafka-demo
    NAME               TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
    consumer-service   NodePort    10.102.45.53    <none>        8080:30081/TCP   8m51s
    kafka              ClusterIP   None            <none>        9092/TCP         8m50s
    producer-service   NodePort    10.103.22.242   <none>        8080:30080/TCP   8m50s
    zookeeper          ClusterIP   None            <none>        2181/TCP         8m50s    
    ```

1. Testar os serviços:
    ```bash
    chat-app-k8s-resilient (main) $ minikube ip
    ```
    Saída:
    ```bash
    192.168.49.2
    ```

1. Redirecione as portas:
    * Porta do produtor:
    ```bash
    chat-app/frontend (main) $ kubectl port-forward svc/producer-service -n kafka-demo 8080:8080
    ```
    Resultado
    ```bash
    Forwarding from 127.0.0.1:8080 -> 8080
    Forwarding from [::1]:8080 -> 8080
    ```
    * Porta do consumidor
    ```bash
    chat-app/frontend (main) $ kubectl port-forward svc/consumer-service -n kafka-demo 8081:8080 &
    ```
    Resultado:
    ```bash
    Forwarding from 127.0.0.1:8081 -> 8080
    Forwarding from [::1]:8081 -> 8080
    ```

1. Para para toda a aplicação (pode demorar um pouco):
    ```bash
    chat-app-k8s-resilient (main) $ make down
    ```
    Resultado:
    ```bash
    Removendo recursos...
    kubectl delete -n kafka-demo -f .
    Warning: deleting cluster-scoped resources, not scoped to the provided namespace
    namespace "kafka-demo" deleted
    service "consumer-service" deleted
    deployment.apps "consumer-service" deleted
    service "kafka" deleted
    statefulset.apps "kafka" deleted
    service "producer-service" deleted
    deployment.apps "producer-service" deleted
    service "zookeeper" deleted
    statefulset.apps "zookeeper" deleted
    ```
    Comando:
    ```bash
    chat-app-k8s-resilient (main) $ make clean
    ```
    Resultado:
    ```bash
    Removendo namespace kafka-demo e todos os recursos...
    kubectl delete namespace kafka-demo --ignore-not-found
    ```
    


