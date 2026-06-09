# notification-service

Servico de notificacoes do Rota Facil. Consome eventos do RabbitMQ e envia emails HTML renderizados com Thymeleaf.

## Para que serve

- Enviar email de boas-vindas quando usuario e criado.
- Enviar email quando conta e removida.
- Enviar email para passageiros quando uma viagem e cancelada.
- Enviar email quando um usuario recebe feedback.

## Porta e nome

- Aplicacao: `notification-service`
- Porta: `8086`
- Sem controllers HTTP de negocio no codigo atual.
- Registrado no Eureka e com rota declarada no gateway em `/notifications/**`.

## Eventos consumidos

Exchange `auth.events`:

- `user.created` -> template `emails/created-account`
- `user.deleted` -> template `emails/deleted-account`

Exchange `transport.events`:

- `trip.cancelled` -> template `emails/trip-cancelled`
- evento de feedback de usuario -> template `emails/feedback-received`

Filas default:

- `notification.user.created.queue`
- `notification.user.deleted.queue`
- `notification.trip.cancelled.queue`
- `notification.user.feedback.queue`

## Templates

Templates HTML ficam em:

- `src/main/resources/templates/emails/created-account.html`
- `src/main/resources/templates/emails/deleted-account.html`
- `src/main/resources/templates/emails/trip-cancelled.html`
- `src/main/resources/templates/emails/feedback-received.html`

## Email local

O `application.properties` esta configurado por default para MailHog:

- Host: `${MAIL_HOG_HOST:localhost}`
- Porta SMTP: `1025`

O `docker-compose.yml` do servico sobe MailHog:

```bash
cd notification-service
docker compose up -d
```

Interface web default do MailHog: `http://localhost:8025`.

## Como rodar

Pre-requisitos:

- Java 21.
- Eureka.
- RabbitMQ.
- Servidor SMTP local ou real.

Comando:

```bash
cd notification-service
./mvnw spring-boot:run
```

Variaveis comuns:

- `EUREKA_URL`
- `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USER`, `RABBITMQ_PASSWORD`
- `MAIL_HOG_HOST`

As propriedades para SMTP real estao comentadas no `application.properties` e podem ser reativadas conforme ambiente.

## Especializacao

Este servico nao possui banco de dados nem deve conter regra de negocio de transporte/autenticacao. Ele transforma eventos em emails.
