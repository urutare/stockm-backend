COMPOSE_FILE = docker-compose.prod.yaml
APP = app
DATABASE_NAME = postgres_db

dev:
	docker compose -f $(COMPOSE_FILE) up --build --remove-orphans
start:
	docker compose -f $(COMPOSE_FILE) up -d --build --remove-orphans

#start:
#	docker compose -f $(COMPOSE_FILE) build --no-cache && \
#	docker compose -f $(COMPOSE_FILE) up -d --remove-orphans

dev-build:
	docker-compose -f $(COMPOSE_FILE) build

dev-up:
	docker-compose -f $(COMPOSE_FILE) up

hard-down:
	docker-compose -f $(COMPOSE_FILE) down --remove-orphans && docker volume prune -f

re-dev: dev-build dev-up

up:
	docker compose -f $(COMPOSE_FILE) up -d

down:
	docker compose -f $(COMPOSE_FILE) down --remove-orphans

build:
	docker compose -f $(COMPOSE_FILE) build --no-cache

logs:
	docker compose -f $(COMPOSE_FILE) logs -f

exec:
	docker compose -f $(COMPOSE_FILE) exec $(DATABASE_NAME) <command>

reset-db:
	docker compose -f $(COMPOSE_FILE) down -v $(DATABASE_NAME)

ps:
	docker compose -f $(COMPOSE_FILE) ps

clean:
	docker system prune --volumes --force

kubernet:
	kompose convert --file  $(COMPOSE_FILE) --out ./k8s

deploy:
	kubectl apply -f k8s

getdeployment:
	kubectl get deployments

rebuild: down build

restart: down start

reup: down up

default: up

restart:
	down start
redev:
	down dev
# .PHONY: up down build logs exec ps clean reset:db default