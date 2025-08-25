NAMESPACE=kafka-demo
YAML_FILES=$(wildcard *.yml)

PRODUCER_IMAGE=producer-service:latest
CONSUMER_IMAGE=consumer-service:latest

.PHONY: up down status logs clean load-images

## Carrega as imagens locais no Minikube
load-images:
	@echo "Carregando imagens locais para o Minikube..."
	minikube image load $(PRODUCER_IMAGE)
	minikube image load $(CONSUMER_IMAGE)

## Sobe todos os recursos definidos nos YAMLs
up: load-images
	@echo "Criando namespace (se não existir)..."
	-kubectl create namespace $(NAMESPACE) || true
	@echo "Aplicando manifests..."
	kubectl apply -n $(NAMESPACE) -f .

## Remove todos os recursos definidos nos YAMLs
down:
	@echo "Removendo recursos..."
	kubectl delete -n $(NAMESPACE) -f .

## Mostra status dos pods e services
status:
	@echo "Status dos Pods:"
	kubectl get pods -n $(NAMESPACE)
	@echo "Status dos Services:"
	kubectl get svc -n $(NAMESPACE)

## Mostra logs do Kafka
logs-kafka:
	kubectl logs -n $(NAMESPACE) statefulset/kafka

## Mostra logs do Zookeeper
logs-zookeeper:
	kubectl logs -n $(NAMESPACE) statefulset/zookeeper

## Mostra logs do Producer
logs-producer:
	kubectl logs -n $(NAMESPACE) deployment/producer-service

## Mostra logs do Consumer
logs-consumer:
	kubectl logs -n $(NAMESPACE) deployment/consumer-service

## Limpa tudo (inclusive o namespace)
clean:	down
	@echo "Removendo namespace $(NAMESPACE) e todos os recursos..."
	kubectl delete namespace $(NAMESPACE) --ignore-not-found
